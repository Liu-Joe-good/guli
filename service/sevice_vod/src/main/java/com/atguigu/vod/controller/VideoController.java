package com.atguigu.vod.controller;

import com.atguigu.oss.commonutils.R;
import com.atguigu.vod.service.uploadVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("eduVod/video")
@CrossOrigin
public class VideoController {
    @Autowired
    uploadVideoService uploadVideoService;

    /**
     * 视频上传功能实现
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/UploadVideo") // 可以用putmapping吗? 不可以，上传文件为postmapping
    public R UploadVideo(MultipartFile file) throws IOException { // 为啥用MultipartFile？上传
        String videoId=uploadVideoService.uploadFile(file);
        return R.ok().data("videoId",videoId);
    }
    @DeleteMapping("/deleteVideo/{videoSourceId}")
    public R deleteVideo(@PathVariable String videoSourceId) throws Exception {
        // 根据videoSourceId来删除上传的视频
        uploadVideoService.removeVideo(videoSourceId);
        return R.ok();
    }
    @DeleteMapping("/deleteVideoBatch")
    public R deleteVideoByChapterId(@RequestParam(value = "id") List<String> id){
        uploadVideoService.removeVideoByChapterId(id);
        return R.ok();
    }

    /**
     * 通过视频的id获取阿里云视频凭证，从而实现播放
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/gitPlayauthById/{id}") // 用@PathVariable 则传的为/gitPlayauthById/1而不是/gitPlayauthById?id=1,
    // 因为动态路由，故用/gitPlayauthById/1的形式
    public R gitPlayauthById(@PathVariable String id) {
        String playAuth = uploadVideoService.getPlayauthById(id);
        return R.ok().data("playAuth",playAuth);
    }
}
