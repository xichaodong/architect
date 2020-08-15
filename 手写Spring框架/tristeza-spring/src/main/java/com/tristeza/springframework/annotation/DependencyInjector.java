package com.tristeza.springframework.annotation;

import com.tristeza.springframework.core.BeanContainer;
import com.tristeza.springframework.inject.annotation.Autowired;
import com.tristeza.springframework.utils.ClassUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author chaodong.xi
 * @date 2020/8/14 4:46 下午
 */
public class DependencyInjector {
    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyInjector.class);
    private BeanContainer beanContainer;

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
                Object fieldInstance = ClassUtil.getFieldInstance(field.getType());
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
