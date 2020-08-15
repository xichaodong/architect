package com.tristeza.springframework.utils;

import com.tristeza.springframework.enums.ProtocolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author chaodong.xi
 * @date 2020/8/11 10:14 下午
 */
public class ClassUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);
    private static final String CLASS_SUFFIX = ".class";
    private static final String CLASS_SEPARATOR = ".";
    private static final String CHARSET = "utf-8";

    public static Set<Class<?>> extractPackageClass(String packageName) {
        ClassLoader classLoader = getClassLoader();
        URL url = classLoader.getResource(packageName.replace('.', '/'));
        if (Objects.isNull(url)) {
            LOGGER.warn("unable to retrieve anything from package:{}", packageName);
            return null;
        }
        Set<Class<?>> classSet = new HashSet<>();
        ;
        if (url.getProtocol().equalsIgnoreCase(ProtocolType.FILE.getCode())) {
            try {
                File packageDirectory = new File(URLDecoder.decode(url.getPath(), CHARSET));
                extractClassFile(classSet, packageDirectory, packageName);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("not support utf-8 charset");
            }
        }
        return classSet;
    }

    public static <T> T newInstance(Class<T> clazz, boolean accessible) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(accessible);

            return constructor.newInstance();
        } catch (Exception e) {
            LOGGER.error("new instance error", e);
        }
        return null;
    }

    public static void setFields(Field field, Object object, Object value, boolean accessible) {
        field.setAccessible(accessible);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            LOGGER.error("set field error", e);
            throw new RuntimeException("set field error", e);
        }
    }

    public static Object getFieldInstance(Class<?> fieldClass) {
        return newInstance(fieldClass, true);
    }

    private static void extractClassFile(Set<Class<?>> classSet, File fileSource, String packageName) {
        if (!fileSource.isDirectory()) {
            return;
        }
        File[] files = fileSource.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                String absolutePath = file.getAbsolutePath();
                if (absolutePath.endsWith(CLASS_SUFFIX)) {
                    add2ClassSet(absolutePath);
                }
                return false;
            }

            private void add2ClassSet(String absolutePath) {
                absolutePath = absolutePath.replace(File.separator, ".");

                String className = absolutePath.substring(absolutePath.indexOf(packageName));
                className = className.substring(0, className.lastIndexOf(CLASS_SEPARATOR));

                Class<?> clazz = loadClass(className);
                classSet.add(clazz);
            }
        });
        if (Objects.nonNull(files)) {
            Arrays.stream(files).forEach(file -> extractClassFile(classSet, file, packageName));
        }
    }

    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            LOGGER.error("load class error", e);
            throw new RuntimeException("load class error", e);
        }
    }

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
