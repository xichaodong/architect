package com.chloe.service.impl;

import com.chloe.common.enums.SexEnum;
import com.chloe.common.org.n3r.idworker.Sid;
import com.chloe.common.utils.DateUtil;
import com.chloe.common.utils.MD5Utils;
import com.chloe.mapper.UsersMapper;
import com.chloe.model.bo.UserBO;
import com.chloe.model.pojo.Users;
import com.chloe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UsersMapper usersMapper;
    @Resource
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUsernameIsExist(String username) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();

        criteria.andEqualTo("username", username);
        Users users = usersMapper.selectOneByExample(userExample);

        return Objects.nonNull(users);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users createUser(UserBO userBo) {
        Users users = new Users();

        users.setId(sid.nextShort());
        users.setUsername(userBo.getUsername());
        users.setNickname(userBo.getUsername());
        users.setFace(USER_FACE);
        users.setBirthday(DateUtil.stringToDate("1900-01-01"));
        users.setSex(SexEnum.SECRET.type);
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());

        try {
            users.setPassword(MD5Utils.getMD5Str(userBo.getConfirmPassword()));
        } catch (Exception e) {
            LOGGER.error("用户注册密码MD5加密失败", e);
        }

        usersMapper.insert(users);

        return users;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserForLogin(UserBO userBo) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();

        criteria.andEqualTo("username", userBo.getUsername());
        try {
            criteria.andEqualTo("password", MD5Utils.getMD5Str(userBo.getPassword()));
        } catch (Exception e) {
            LOGGER.error("用户登录密码MD5加密失败", e);
        }

        return usersMapper.selectOneByExample(userExample);
    }
}
