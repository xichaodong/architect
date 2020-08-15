package com.tristeza.test;

import com.tristeza.controller.frontend.MainPageController;
import com.tristeza.service.solo.HeadLineService;
import com.tristeza.springframework.core.BeanContainer;
import com.tristeza.springframework.core.annotation.Controller;
import com.tristeza.springframework.utils.ClassUtil;
import org.junit.jupiter.api.*;

import java.util.Set;

/**
 * @author chaodong.xi
 * @date 2020/8/12 1:39 上午
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FrameworkTest {
    private static BeanContainer beanContainer;

    @BeforeAll
    static void init() {
        beanContainer = BeanContainer.getInstance();
    }

    @DisplayName("提取目标类方法:extractPackageClass")
    @Test
    public void extractPackageClass() {
        System.out.println(ClassUtil.extractPackageClass("com.tristeza.controller"));
    }

    @DisplayName("测试bean容器:loadBeanTest")
    @Order(1)
    @Test
    public void loadBeanTest() {
        beanContainer.loadBeans("com.tristeza.controller");
    }

    @DisplayName("获取bean:getBeanTest")
    @Order(2)
    @Test
    public void getBeanTest() {
        beanContainer.loadBeans("com.tristeza.service");
        Set<Class<?>> classesByAnnotation = beanContainer.getClassesBySuper(HeadLineService.class);
        Assertions.assertNotNull(classesByAnnotation);
    }
}
