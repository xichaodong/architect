package com.chloe.service.impl;

import com.chloe.service.FileService;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FileServiceImpl implements FileService {
    @Resource
    private FastFileStorageClient storageClient;

    @Override
    public String upload() throws Exception {
        return null;
    }
}
