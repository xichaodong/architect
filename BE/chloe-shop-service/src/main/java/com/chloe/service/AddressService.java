package com.chloe.service;

import com.chloe.model.bo.AddressBO;
import com.chloe.model.pojo.UserAddress;
import java.util.List;

public interface AddressService {
    List<UserAddress> queryAll(String userId);

    void addAddress(AddressBO address);

    void updateAddress(AddressBO address);

    void deleteAddress(String userId, String addressId);

    void setDefaultAddress(String userId, String addressId);
}
