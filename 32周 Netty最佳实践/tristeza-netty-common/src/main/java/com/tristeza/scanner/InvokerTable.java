package com.tristeza.scanner;

import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chaodong.xi
 * @date 2021/1/28 上午11:54
 */
public class InvokerTable {
    private final static ConcurrentHashMap<String, Map<String, Invoker>> INVOKER_TABLE = new ConcurrentHashMap<>();

    public static void addInvoker(String module, String cmd, Invoker invoker) {
        Map<String, Invoker> cmdMap = INVOKER_TABLE.get(module);

        if (CollectionUtils.isEmpty(cmdMap)) {
            cmdMap = new HashMap<>();
        }

        cmdMap.put(cmd, invoker);
        INVOKER_TABLE.put(module, cmdMap);
    }

    public static Invoker getInvoker(String module, String cmd) {
        Map<String, Invoker> cmdMap = INVOKER_TABLE.get(module);

        if (CollectionUtils.isEmpty(cmdMap)) {
            return null;
        }

        return cmdMap.get(cmd);
    }
}
