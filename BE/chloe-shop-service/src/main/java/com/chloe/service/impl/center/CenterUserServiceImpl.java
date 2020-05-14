package com.chloe.service.impl.center;

import com.chloe.mapper.UsersMapper;
import com.chloe.model.bo.center.CenterUserBO;
import com.chloe.model.pojo.Users;
import com.chloe.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CenterUserServiceImpl implements CenterUserService {
    @Resource
    private UsersMapper usersMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);

        return doMask(users);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users user = new Users();
        BeanUtils.copyProperties(centerUserBO, user);

        user.setId(userId);
        user.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(user);

        return queryUserInfo(userId);
    }

    private Users doMask(Users origin) {
        origin.setPassword(null);
        origin.setCreatedTime(null);
        origin.setUpdatedTime(null);

        return origin;
    }
}
