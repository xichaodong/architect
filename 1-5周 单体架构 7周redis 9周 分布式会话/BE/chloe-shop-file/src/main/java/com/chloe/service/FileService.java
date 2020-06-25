package com.chloe.service;

import com.chloe.model.pojo.Users;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Users upload(MultipartFile file, String fileExtName, String userId) throws Exception;
}
