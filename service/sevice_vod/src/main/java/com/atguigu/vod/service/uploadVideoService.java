package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface uploadVideoService {
    String uploadFile(MultipartFile file) throws IOException;

    void removeVideo(String videoSourceId) throws Exception;

    void removeVideoByChapterId(List<String> id);
}
