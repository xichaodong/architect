package com.tristeza.user.service.front;

import com.tristeza.user.model.bo.AddressBO;
import com.tristeza.user.model.pojo.UserAddress;

import java.util.List;

public interface AddressService {
    List<UserAddress> queryAll(String userId);

    UserAddress queryAddressById(String userId, String addressId);

    void addAddress(AddressBO address);

    void updateAddress(AddressBO address);

    void deleteAddress(String userId, String addressId);

    void setDefaultAddress(String userId, String addressId);
}
