package com.tristeza.annotation;

import java.lang.annotation.*;

/**
 * @author chaodong.xi
 * @date 2020/8/9 8:20 下午
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimiter {
    int limit();

    String methodKey() default "";
}
