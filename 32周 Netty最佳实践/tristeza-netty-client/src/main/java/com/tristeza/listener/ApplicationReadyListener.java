package com.tristeza.listener;

import com.tristeza.client.NettyClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author chaodong.xi
 * @date 2021/1/28 下午3:45
 */
@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        NettyClient.getInstance().init();
    }
}
