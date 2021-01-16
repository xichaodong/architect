package com.tristeza;

import com.tristeza.config.RibbonConfig1;
import com.tristeza.config.RibbonConfig2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

/**
 * @author chaodong.xi
 * @date 2020/8/13 3:34 下午
 */
@SpringBootApplication
@EnableDiscoveryClient
@RibbonClients(value = {
        @RibbonClient(name = "eureka-client1", configuration = RibbonConfig1.class),
        @RibbonClient(name = "eureka-client2", configuration = RibbonConfig2.class),
})
public class EurekaClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
