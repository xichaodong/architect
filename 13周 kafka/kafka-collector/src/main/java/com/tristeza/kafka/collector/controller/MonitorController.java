package com.tristeza.kafka.collector.controller;

import com.alibaba.fastjson.JSON;
import com.tristeza.kafka.collector.model.AccurateWatcherMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chaodong.xi
 * @date 2020/7/14 8:19 下午
 */
@RestController
public class MonitorController {
    @PostMapping("accurateWatch")
    public AccurateWatcherMessage watch(@RequestBody AccurateWatcherMessage message) {
        System.out.println("=======告警内容=======");
        System.out.println(JSON.toJSON(message));
        System.out.println("=====================");

        return message;
    }
}
