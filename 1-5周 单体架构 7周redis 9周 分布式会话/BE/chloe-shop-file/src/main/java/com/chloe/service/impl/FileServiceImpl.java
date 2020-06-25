package com.chloe.service.impl;

import com.chloe.mapper.UsersMapper;
import com.chloe.model.FileResource;
import com.chloe.model.pojo.Users;
import com.chloe.service.FileService;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
public class FileServiceImpl implements FileService {
    @Resource
    private FastFileStorageClient storageClient;
    @Resource
    private FileResource fileResource;
    @Resource
    private UsersMapper usersMapper;

    @Override
    public Users upload(MultipartFile file, String fileExtName, String userId) throws Exception {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null);

        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setFace(fileResource.getHost() + storePath.getFullPath());

        usersMapper.updateByPrimaryKeySelective(users);

        return users;
    }
}
