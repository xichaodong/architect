package com.tristeza.user.api.front;

import com.tristeza.user.model.pojo.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("address-api")
@FeignClient(value = "address-api")
public interface AddressApi {
    @GetMapping("address")
    UserAddress queryAddressById(@RequestParam("userId") String userId,
                                 @RequestParam(value = "addressId", required = false) String addressId);
}
