package com.tristeza.scanner;

import com.tristeza.annotation.Cmd;
import com.tristeza.annotation.Module;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author chaodong.xi
 * @date 2021/1/28 上午11:28
 */
@Component
public class NettyProcessBeanScanner implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Module module = bean.getClass().getAnnotation(Module.class);

        if (Objects.isNull(module)) {
            return bean;
        }

        String moduleValue = module.module();
        Method[] methods = bean.getClass().getMethods();

        if (methods.length > 0) {
            for (Method method : methods) {
                Cmd cmd = method.getAnnotation(Cmd.class);

                if (Objects.isNull(cmd)) {
                    continue;
                }

                String cmdValue = cmd.cmd();

                if (Objects.isNull(InvokerTable.getInvoker(moduleValue, cmdValue))) {
                    InvokerTable.addInvoker(moduleValue, cmdValue, new Invoker(method, bean));
                } else {
                    System.err.println("模块下的命令缓存已经存在，module：" + moduleValue + "cmd:" + cmdValue);
                }
            }
        }

        return null;
    }
}
