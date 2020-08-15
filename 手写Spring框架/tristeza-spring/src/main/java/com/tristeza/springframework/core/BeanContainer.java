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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author chaodong.xi
 * @date 2020/8/13 10:35 下午
 */
public class BeanContainer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanContainer.class);

    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    private boolean loaded = false;

    private static final Set<Class<? extends Annotation>> BEAN_ANNOTATION = ImmutableSet.of(
            Component.class, Controller.class, Repository.class, Service.class
    );

    public static BeanContainer getInstance() {
        return ContainHolder.HOLDER.INSTANCE;
    }

    private BeanContainer() {
    }

    public boolean isLoaded() {
        return loaded;
    }

    public int size() {
        return beanMap.size();
    }

    private enum ContainHolder {
        HOLDER;
        private final BeanContainer INSTANCE;

        ContainHolder() {
            INSTANCE = new BeanContainer();
        }
    }

    public Object addBean(Class<?> clazz, Object bean) {
        return beanMap.put(clazz, bean);
    }

    public Object removeBean(Class<?> clazz, Object bean) {
        return beanMap.remove(clazz, bean);
    }

    public Object getBean(Class<?> clazz) {
        return beanMap.get(clazz);
    }

    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }

    public Set<Object> getBeans() {
        return Sets.newHashSet(beanMap.values());
    }

    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
        Set<Class<?>> classSet = beanMap.keySet();
        if (CollectionUtils.isEmpty(classSet)) {
            LOGGER.warn("nothing in bean container");
            return ImmutableSet.of();
        }

        return classSet.stream().filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    public Set<Class<?>> getClassesBySuper(Class<?> interfaceOrClass) {
        if (Objects.isNull(interfaceOrClass)) {
            throw new RuntimeException("interfaceOrClass can't be null");
        }

        Set<Class<?>> classSet = getClasses();

        if (CollectionUtils.isEmpty(classSet)) {
            LOGGER.warn("nothing in bean container");
            return ImmutableSet.of();
        }

        return classSet.stream().filter(interfaceOrClass::isAssignableFrom)
                .filter(clazz -> !interfaceOrClass.equals(clazz))
                .collect(Collectors.toSet());
    }

    public synchronized void loadBeans(String packageName) {
        if (isLoaded()) {
            LOGGER.warn("bean container has been loaded");
            return;
        }

        Set<Class<?>> classSet = ClassUtil.extractPackageClass(packageName);
        if (CollectionUtils.isEmpty(classSet)) {
            return;
        }

        classSet.stream().filter(clazz -> BEAN_ANNOTATION.stream().anyMatch(clazz::isAnnotationPresent))
                .forEach(clazz -> beanMap.put(clazz, ClassUtil.newInstance(clazz, true)));

        loaded = true;
    }
}
