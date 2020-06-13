package com.chloe.config;

import com.chloe.interceptor.UserTokenInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final String FACE_RESOURCE_LOCATION = String.format("file:..%sface/", File.separator);
    private static final String SWAGGER_RESOURCE_LOCATION = String.format("classpath:%sMETA-INF%sresources%s", File.separator, File.separator, File.separator);

    @Resource
    private UserTokenInterceptor userTokenInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(SWAGGER_RESOURCE_LOCATION)
                .addResourceLocations(FACE_RESOURCE_LOCATION);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder templateBuilder) {
        return templateBuilder.build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userTokenInterceptor)
                .addPathPatterns("/shopCart/*")
                .addPathPatterns("/address/*")
                .addPathPatterns("/orders/*")
                .addPathPatterns("/center/*")
                .addPathPatterns("/userInfo/*")
                .addPathPatterns("/myorders/*")
                .excludePathPatterns("/myorders/deliver")
                .excludePathPatterns("/orders/notifyMerchantOrderPaid");

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
