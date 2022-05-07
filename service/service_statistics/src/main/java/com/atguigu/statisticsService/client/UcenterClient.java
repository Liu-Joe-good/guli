package com.atguigu.statisticsService.client;

import com.atguigu.oss.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter",fallback = UcenterFeignClient.class)
public interface UcenterClient {
    /**
     * 获取某天注册人数
     * @param day
     * @return
     */
    @GetMapping("/api/ucenter/member/getRegisterCount/{day}")
    public R getRegisterCount(@PathVariable(value = "day") String day);
}
