package com.tristeza.user.api.front;

import com.tristeza.user.model.bo.AddressBO;
import com.tristeza.user.model.pojo.UserAddress;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("address-api")
public interface AddressApi {
    @GetMapping("addressList")
    List<UserAddress> queryAll(@RequestParam("userId") String userId);

    @GetMapping("address")
    UserAddress queryAddressById(@RequestParam("userId") String userId,
                                 @RequestParam(value = "addressId", required = false) String addressId);

    @PostMapping("address")
    void addAddress(@RequestBody AddressBO address);

    @PutMapping("address")
    void updateAddress(@RequestBody AddressBO address);

    @DeleteMapping("address")
    void deleteAddress(@RequestParam("userId") String userId, @RequestParam("addressId") String addressId);

    @PostMapping("setDefaultAddress")
    void setDefaultAddress(@RequestParam("userId") String userId, @RequestParam("addressId") String addressId);
}
