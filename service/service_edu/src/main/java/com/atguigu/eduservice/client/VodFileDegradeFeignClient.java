package com.atguigu.eduservice.client;

import com.atguigu.oss.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public R deleteVideo(String videoSourceId) {
        return R.error().message("删除视频失败，服务器宕机"); // 此处的message没有返回到前端页面上，因为没有拿其值
    }

    @Override
    public R deleteVideoByChapterId(List<String> ids) {
        return R.error().message("批量删除视频失败，服务器宕机");
    }
}
