package com.atguigu.eduservice.client;

import org.springframework.stereotype.Component;

/**
 * @author liuzixuan
 * @ClassName OrderFeignClient
 * @Date 20210720
 */
@Component
public class OrderFeignClient implements OrderClient{
    @Override
    public boolean getOrderStatus(String courseId, String userId) {
        System.out.println("正在服务调用中");
        return false;
    }
}
