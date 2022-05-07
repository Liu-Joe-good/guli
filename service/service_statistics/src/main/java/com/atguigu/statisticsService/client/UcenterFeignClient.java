package com.atguigu.statisticsService.client;

import com.atguigu.oss.commonutils.R;
import org.springframework.stereotype.Component;

/**
 * @author liuzixuan
 * @ClassName UcenterClient
 * @Date
 */
@Component
public class UcenterFeignClient implements UcenterClient{
    @Override
    public R getRegisterCount(String day) {
        System.out.println("远程调用....");
        return null;
    }
}
