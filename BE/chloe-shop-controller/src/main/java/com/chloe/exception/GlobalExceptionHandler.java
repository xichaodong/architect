package com.chloe.exception;

import com.chloe.common.utils.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JsonResult uploadFileSizeExceedHandler(MaxUploadSizeExceededException e) {
        return JsonResult.errorMsg("上传文件大小不能超过500k");
    }
}
