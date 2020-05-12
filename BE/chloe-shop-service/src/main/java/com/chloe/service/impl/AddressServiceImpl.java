package com.chloe.service.impl;

import com.chloe.common.enums.BooleanEnum;
import com.chloe.common.org.n3r.idworker.Sid;
import com.chloe.mapper.UserAddressMapper;
import com.chloe.model.bo.AddressBO;
import com.chloe.model.pojo.UserAddress;
import com.chloe.service.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.swing.text.StyledEditorKit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private UserAddressMapper userAddressMapper;
    @Resource
    private Sid sid;

    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress address = new UserAddress();
        address.setUserId(userId);

        return userAddressMapper.select(address);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addAddress(AddressBO address) {
        List<UserAddress> oldAddresses = queryAll(address.getUserId());
        Integer isDefault = CollectionUtils.isEmpty(oldAddresses) ? BooleanEnum.TRUE.type : BooleanEnum.FALSE.type;

        UserAddress newAddress = new UserAddress();

        BeanUtils.copyProperties(address, newAddress);

        newAddress.setId(sid.nextShort());
        newAddress.setCreatedTime(new Date());
        newAddress.setUpdatedTime(new Date());
        newAddress.setIsDefault(isDefault);

        userAddressMapper.insert(newAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAddress(AddressBO address) {
        UserAddress newAddress = new UserAddress();

        BeanUtils.copyProperties(address, newAddress);

        newAddress.setId(address.getAddressId());
        newAddress.setUpdatedTime(new Date());

        userAddressMapper.updateByPrimaryKeySelective(newAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();

        address.setId(addressId);
        address.setUserId(userId);

        userAddressMapper.delete(address);
    }

    @Override
    public void setDefaultAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();
        address.setUserId(userId);
        address.setIsDefault(BooleanEnum.TRUE.type);

        UserAddress defaultAddress = userAddressMapper.selectOne(address);

        if (!Objects.isNull(defaultAddress)) {
            defaultAddress.setIsDefault(BooleanEnum.FALSE.type);
            userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
        }

        address.setId(addressId);
        address.setIsDefault(BooleanEnum.TRUE.type);

        userAddressMapper.updateByPrimaryKeySelective(address);
    }
}
