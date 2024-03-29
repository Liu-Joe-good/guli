package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface uploadVideoService {
    String uploadFile(MultipartFile file) throws IOException;

    void removeVideo(String videoSourceId) throws Exception;

    void removeVideoByChapterId(List<String> id);

    /**
     * 通过视频id获取阿里云视频凭证，从而实现播放
     * @param id
     */
    String getPlayauthById(String id);
}
