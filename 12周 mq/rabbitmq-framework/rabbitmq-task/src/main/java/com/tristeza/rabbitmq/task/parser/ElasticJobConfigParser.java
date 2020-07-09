package com.tristeza.rabbitmq.task.parser;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.tristeza.rabbitmq.task.annotation.ElasticJobConfig;
import com.tristeza.rabbitmq.task.enums.ElasticJobType;
import com.tristeza.rabbitmq.task.model.JobZookeeperProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.tristeza.rabbitmq.task.enums.ElasticJobType.DATAFLOW;
import static com.tristeza.rabbitmq.task.enums.ElasticJobType.SIMPLE;

/**
 * @author chaodong.xi
 * @date 2020/7/8 2:54 下午
 */
public class ElasticJobConfigParser implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticJobConfigParser.class);

    private ZookeeperRegistryCenter zookeeperRegistryCenter;
    private JobZookeeperProperties jobZookeeperProperties;

    public ElasticJobConfigParser(JobZookeeperProperties jobZookeeperProperties,
                                  ZookeeperRegistryCenter zookeeperRegistryCenter) {
        this.zookeeperRegistryCenter = zookeeperRegistryCenter;
        this.jobZookeeperProperties = jobZookeeperProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        ApplicationContext applicationContext = applicationReadyEvent.getApplicationContext();
        Map<String, Object> beans = applicationReadyEvent.getApplicationContext()
                .getBeansWithAnnotation(ElasticJobConfig.class);

        beans.values().forEach(object -> {
            Class<?> clazz = object.getClass();
            if (clazz.getName().contains("$")) {
                String className = clazz.getName();
                try {
                    clazz = Class.forName(className.substring(0, className.indexOf("$")));
                } catch (ClassNotFoundException e) {
                    LOGGER.error("elastic job 注解解析异常", e);
                }
            }
            String jobType = clazz.getInterfaces()[0].getSimpleName();

            ElasticJobConfig conf = clazz.getAnnotation(ElasticJobConfig.class);

            JobCoreConfiguration coreConfiguration = buildJobCoreConfiguration(conf);
            JobTypeConfiguration typeConfiguration;

            if (SIMPLE.getType().equals(jobType)) {
                typeConfiguration = new SimpleJobConfiguration(coreConfiguration, jobType);
            } else if (DATAFLOW.getType().equals(jobType)) {
                typeConfiguration = new DataflowJobConfiguration(coreConfiguration, jobType, conf.streamingProcess());
            } else {
                typeConfiguration = new ScriptJobConfiguration(coreConfiguration, conf.scriptCommandLine());
            }

            LiteJobConfiguration jobConfiguration = LiteJobConfiguration.newBuilder(typeConfiguration)
                    .overwrite(conf.overwrite())
                    .disabled(conf.disabled())
                    .monitorPort(conf.monitorPort())
                    .monitorExecution(conf.monitorExecution())
                    .maxTimeDiffSeconds(conf.maxTimeDiffSeconds())
                    .jobShardingStrategyClass(conf.jobShardingStrategyClass())
                    .reconcileIntervalMinutes(conf.reconcileIntervalMinutes())
                    .build();

            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
            factory.setInitMethodName("init");
            factory.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);

            //	1.添加bean构造参数，相当于添加自己的真实的任务实现类
            if (!ElasticJobType.SCRIPT.getType().equals(jobType)) {
                factory.addConstructorArgValue(object);
            }
            //	2.添加注册中心
            factory.addConstructorArgValue(this.zookeeperRegistryCenter);
            //	3.添加LiteJobConfiguration
            factory.addConstructorArgValue(jobConfiguration);

            //	4.如果有eventTraceRdbDataSource 则也进行添加
            if (StringUtils.hasText(conf.eventTraceRdbDataSource())) {
                BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
                rdbFactory.addConstructorArgReference(conf.eventTraceRdbDataSource());
                factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
            }

            //  5.添加监听
            List<?> elasticJobListeners = getTargetElasticJobListeners(conf);
            factory.addConstructorArgValue(elasticJobListeners);

            // 	接下来就是把factory 也就是 SpringJobScheduler注入到Spring容器中
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

            String registerBeanName = conf.name() + "SpringJobScheduler";
            defaultListableBeanFactory.registerBeanDefinition(registerBeanName, factory.getBeanDefinition());
            SpringJobScheduler scheduler = (SpringJobScheduler) applicationContext.getBean(registerBeanName);
            scheduler.init();

            LOGGER.info("启动elastic-job作业: " + conf.name());
        });
    }

    private List<BeanDefinition> getTargetElasticJobListeners(ElasticJobConfig conf) {
        List<BeanDefinition> result = new ManagedList<BeanDefinition>(2);
        String listeners = conf.listener();
        if (StringUtils.hasText(listeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listeners);
            factory.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
            result.add(factory.getBeanDefinition());
        }

        String distributedListeners = conf.distributedListener();
        long startedTimeoutMilliseconds = conf.startedTimeoutMilliseconds();
        long completedTimeoutMilliseconds = conf.completedTimeoutMilliseconds();

        if (StringUtils.hasText(distributedListeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListeners);
            factory.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
            factory.addConstructorArgValue(startedTimeoutMilliseconds);
            factory.addConstructorArgValue(completedTimeoutMilliseconds);
            result.add(factory.getBeanDefinition());
        }
        return result;
    }

    private JobCoreConfiguration buildJobCoreConfiguration(ElasticJobConfig conf) {
        String jobName = this.jobZookeeperProperties.getNamespace() + "." + conf.name();
        String cron = conf.cron();
        String shardingItemParameters = conf.shardingItemParameters();
        String description = conf.description();
        String jobParameter = conf.jobParameter();
        String jobExceptionHandler = conf.jobExceptionHandler();
        String executorServiceHandler = conf.executorServiceHandler();
        boolean failover = conf.failover();
        boolean misfire = conf.misfire();

        int shardingTotalCount = conf.shardingTotalCount();

        return JobCoreConfiguration
                .newBuilder(jobName, cron, shardingTotalCount)
                .shardingItemParameters(shardingItemParameters)
                .description(description)
                .failover(failover)
                .jobParameter(jobParameter)
                .misfire(misfire)
                .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobExceptionHandler)
                .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), executorServiceHandler)
                .build();
    }
}
