package com.tristeza.aspect;

import com.tristeza.annotation.AccessLimiter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chaodong.xi
 * @date 2020/8/9 8:23 下午
 */
@Aspect
@Component
public class AccessLimiterAspect {
    @Resource
    private com.tristeza.service.AccessLimiter accessLimiter;

    @Pointcut("@annotation(com.tristeza.annotation.AccessLimiter)")
    public void cut() {

    }

    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        AccessLimiter annotation = method.getAnnotation(AccessLimiter.class);
        if (Objects.isNull(annotation)) {
            return;
        }
        
        String key = annotation.methodKey();
        Integer limit = annotation.limit();

        if (StringUtils.isEmpty(key)) {
            Class<?>[] type = method.getParameterTypes();
            key = method.getName();

            if (type.length > 0) {
                String paramTypes = Arrays.stream(type).map(Class::getName)
                        .collect(Collectors.joining(","));
                key += "#" + paramTypes;
            }
        }
        accessLimiter.limitAccess(key, limit);
    }
}
