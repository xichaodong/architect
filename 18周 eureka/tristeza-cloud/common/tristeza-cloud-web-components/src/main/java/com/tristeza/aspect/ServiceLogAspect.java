package com.tristeza.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Around("execution(* com.tristeza..*.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        LOGGER.info("===== 开始执行 {} {} =====", proceedingJoinPoint.getTarget().getClass(),
                proceedingJoinPoint.getSignature().getName());

        long begin = System.currentTimeMillis();

        Object result = proceedingJoinPoint.proceed();

        long end = System.currentTimeMillis();

        long processTime = end - begin;

        if (processTime > 3000) {
            LOGGER.error("===== 执行结束 耗时:{} 毫秒=====", processTime);
        } else if (processTime > 2000) {
            LOGGER.warn("===== 执行结束 耗时:{} 毫秒=====", processTime);
        } else {
            LOGGER.info("===== 执行结束 耗时:{} 毫秒=====", processTime);
        }

        return result;
    }
}
