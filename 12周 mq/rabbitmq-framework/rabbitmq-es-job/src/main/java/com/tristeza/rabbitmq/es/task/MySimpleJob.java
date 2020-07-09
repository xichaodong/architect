package com.tristeza.rabbitmq.es.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.tristeza.rabbitmq.es.annotation.JobTrace;
import com.tristeza.rabbitmq.task.annotation.ElasticJobConfig;
import org.springframework.stereotype.Component;

//@ElasticJobConfig(name = "test", cron = "0/5 * * * * ?", shardingTotalCount = 5, overwrite = true)
@Component
public class MySimpleJob implements SimpleJob {


    @JobTrace
    @Override
    public void execute(ShardingContext shardingContext) {

        System.err.println("---------	开始任务 MySimpleJob	---------");
//		
//		System.err.println(shardingContext.getJobName());
//		System.err.println(shardingContext.getJobParameter());
//		System.err.println(shardingContext.getShardingItem());
//		System.err.println(shardingContext.getShardingParameter());
//		System.err.println(shardingContext.getShardingTotalCount());
//		System.err.println("当前线程 : ---------------" + Thread.currentThread().getName());
//		
//		System.err.println("----------结束任务------");
    }


}