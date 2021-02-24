package com.tristeza.user.remote;

import com.tristeza.user.api.front.AddressApi;
import com.tristeza.user.model.pojo.UserAddress;
import com.tristeza.user.service.front.AddressService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chaodong.xi
 * @date 2021/2/24 11:58 上午
 */
@RestController
public class AddressApiImpl implements AddressApi {
    @Resource
    private AddressService addressService;

    @Override
    public UserAddress queryAddressById(String userId, String addressId) {
        return addressService.queryAddressById(userId, addressId);
    }
}
