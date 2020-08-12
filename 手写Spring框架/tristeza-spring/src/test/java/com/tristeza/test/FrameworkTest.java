package com.tristeza.test;

import com.tristeza.springframework.utils.ClassUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * @author chaodong.xi
 * @date 2020/8/12 1:39 上午
 */
public class FrameworkTest {
    @DisplayName("提取目标类方法:extractPackageClass")
    @Test
    public void extractPackageClass() {
        System.out.println(ClassUtil.extractPackageClass("com.tristeza"));
    }
}
