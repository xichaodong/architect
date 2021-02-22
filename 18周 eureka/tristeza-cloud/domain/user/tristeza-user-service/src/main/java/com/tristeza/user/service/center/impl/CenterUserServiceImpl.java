package com.tristeza.user.service.center.impl;

import com.tristeza.user.mapper.UsersMapper;
import com.tristeza.user.model.bo.center.CenterUserBO;
import com.tristeza.user.model.pojo.Users;
import com.tristeza.user.service.center.CenterUserService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;

@Service
public class CenterUserServiceImpl implements CenterUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CenterUserServiceImpl.class);

    @Resource
    private UsersMapper usersMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);

        return doMask(users);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users user = new Users();
        BeanUtils.copyProperties(centerUserBO, user);

        user.setId(userId);
        user.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(user);

        return queryUserInfo(userId);
    }

    @Override
    public Users uploadUserFace(String userId, MultipartFile multipartFile, String fileSavePath, String imgServerUrl) {
        FileOutputStream outputStream = null;

        File outFile = new File(fileSavePath);

        if (Objects.nonNull(outFile.getParentFile())) {
            outFile.getParentFile().mkdirs();
        }

        try {
            outputStream = new FileOutputStream(outFile);
            InputStream inputStream = multipartFile.getInputStream();

            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            LOGGER.error("头像保存失败", e);
        } finally {
            if (Objects.nonNull(outputStream)) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    LOGGER.error("输出流关闭失败", e);
                }
            }
        }
        return saveFace2DB(fileSavePath, userId);
    }

    private Users saveFace2DB(String imgUrl, String userId) {
        Users users = new Users();
        users.setId(userId);
        users.setFace(imgUrl);

        usersMapper.updateByPrimaryKeySelective(users);

        return users;
    }

    private Users doMask(Users origin) {
        origin.setPassword(null);
        origin.setCreatedTime(null);
        origin.setUpdatedTime(null);

        return origin;
    }
}
