package com.chloe.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:file-upload-dev.properties")
public class FileUpload {
    @Value("${file.image.server.url}")
    private String imgServerUrl;

    public String getImgServerUrl() {
        return imgServerUrl;
    }

    public void setImgServerUrl(String imgServerUrl) {
        this.imgServerUrl = imgServerUrl;
    }
}
