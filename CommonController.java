package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
public class CommonController {


    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        //上传文件
        String url = null;
        try {
            String fileName = file.getOriginalFilename();

            //获取文件后缀名
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            //防止出现相同名称所以随机生成
            String objectName = UUID.randomUUID().toString() + suffix;

            url = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
