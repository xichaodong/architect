package com.tristeza.scanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author chaodong.xi
 * @date 2021/1/28 上午11:47
 */
public class Invoker {
    private Method method;

    private Object target;

    public Invoker(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object invoke(Object... params) {
        try {
            return method.invoke(target, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
