package com.atguigu.oss.controller;

import com.atguigu.oss.commonutils.R;
import com.atguigu.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
//@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;
    //MultipartFile 文件上传
    @PostMapping
    public R uploadOssFile(MultipartFile file){
        // 上传文件
        String url= ossService.uploadFileAvatar(file);
        // 返回url路径
        return R.ok().data("url",url);
    }
}
