package com.tristeza.springframework.inject;

import com.tristeza.springframework.core.BeanContainer;
import com.tristeza.springframework.inject.annotation.Autowired;
import com.tristeza.springframework.utils.ClassUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chaodong.xi
 * @date 2020/8/15 12:13 下午
 */
public class DependencyInjector {
    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyInjector.class);
    private final BeanContainer beanContainer;

    public DependencyInjector() {
        beanContainer = BeanContainer.getInstance();
    }

    public void doIoc() {
        if (CollectionUtils.isEmpty(beanContainer.getClasses())) {
            LOGGER.warn("nothing in bean container");
            return;
        }
        for (Class<?> clazz : beanContainer.getClasses()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String autowiredValue = autowired.value();
                    Object fieldInstance = getFieldInstance(field.getType(), autowiredValue);
                    if (Objects.isNull(fieldInstance)) {
                        throw new RuntimeException("unable to inject relevant type,target class is:" + field.getType().getName());
                    } else {
                        Object targetBean = beanContainer.getBean(clazz);
                        ClassUtil.setFields(field, targetBean, fieldInstance, true);
                    }
                }
            }
        }
    }

    private Object getFieldInstance(Class<?> fieldClass, String autowiredValue) {
        Object fieldValue = beanContainer.getBean(fieldClass);
        if (Objects.nonNull(fieldValue)) {
            return fieldValue;
        }
        Class<?> implementedClass = getImplementClass(fieldClass, autowiredValue);
        if (Objects.nonNull(implementedClass)) {
            return beanContainer.getBean(implementedClass);
        }
        return null;
    }

    private Class<?> getImplementClass(Class<?> fieldClass, String autowiredValue) {
        Set<Class<?>> classesBySuper = beanContainer.getClassesBySuper(fieldClass);
        if (CollectionUtils.isEmpty(classesBySuper)) {
            return null;
        }
        if (StringUtils.isBlank(autowiredValue)) {
            if (classesBySuper.size() == 1) {
                return classesBySuper.iterator().next();
            } else {
                throw new RuntimeException("multiple implemented classed for " + fieldClass.getName() + " please set @Autowired value");
            }
        }
        Set<Class<?>> implementClass = classesBySuper.stream()
                .filter(clazz -> clazz.getSimpleName().equals(autowiredValue))
                .collect(Collectors.toSet());
        if (implementClass.size() == 1) {
            return classesBySuper.iterator().next();
        } else {
            throw new RuntimeException("multiple implemented classed for " + fieldClass.getName() + " please set @Autowired value unique");
        }
    }
}
