package com.tristeza.springframework.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.tristeza.springframework.core.annotation.Component;
import com.tristeza.springframework.core.annotation.Controller;
import com.tristeza.springframework.core.annotation.Repository;
import com.tristeza.springframework.core.annotation.Service;
import com.tristeza.springframework.utils.ClassUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chaodong.xi
 * @date 2020/8/13 10:35 下午
 */
public class BeanContainer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanContainer.class);

    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    private static final Set<Class<? extends Annotation>> BEAN_ANNOTATION = ImmutableSet.of(
            Component.class, Controller.class, Repository.class, Service.class
    );

    public static BeanContainer getInstance() {
        return ContainHolder.HOLDER.INSTANCE;
    }

    private BeanContainer() {
    }

    private enum ContainHolder {
        HOLDER;
        private final BeanContainer INSTANCE;

        ContainHolder() {
            INSTANCE = new BeanContainer();
        }
    }

    public void loadBeans(String packageName) {
        Set<Class<?>> classSet = ClassUtil.extractPackageClass(packageName);
        if (CollectionUtils.isEmpty(classSet)) {
            return;
        }

        classSet.stream().filter(clazz -> BEAN_ANNOTATION.stream().anyMatch(clazz::isAnnotationPresent))
                .forEach(clazz -> beanMap.put(clazz, ClassUtil.newInstance(clazz, true)));

    }
}
