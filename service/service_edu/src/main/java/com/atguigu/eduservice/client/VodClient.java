package com.atguigu.eduservice.client;

import com.atguigu.oss.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)
public interface VodClient {
    @DeleteMapping("eduVod/video/deleteVideo/{videoSourceId}")
    public R deleteVideo(@PathVariable("videoSourceId") String videoSourceId);
    @DeleteMapping("eduVod/video/deleteVideoBatch")
    public R deleteVideoByChapterId(@RequestParam(value = "id") List<String> ids);
}
