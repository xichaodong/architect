package com.tristeza.kafka.collector.controller;

import com.tristeza.kafka.collector.util.FastJsonConvertUtil;
import com.tristeza.kafka.collector.util.InputMDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chaodong.xi
 * @date 2020/7/9 8:41 下午
 */
@RestController
public class IndexController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/index")
    public String index() {
        InputMDC.putMDC();

        LOGGER.info("info1111");
        LOGGER.warn("warn1111");
        LOGGER.error("error1111");

        return "success";
    }
}
