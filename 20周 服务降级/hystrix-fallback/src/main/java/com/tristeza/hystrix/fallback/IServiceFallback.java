package com.tristeza.hystrix.fallback;

import com.tristeza.hystrix.service.IService;
import org.springframework.stereotype.Component;

/**
 * @author chaodong.xi
 * @date 2021/2/24 3:28 下午
 */
@Component
public class IServiceFallback implements IService {
    @Override
    public String sayHi() {
        return null;
    }

    @Override
    public String error() {
        return "hystrix error";
    }

    @Override
    public String timeout() {
        return "hystrix timeout";
    }
}
