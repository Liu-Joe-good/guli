package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-order",fallback = OrderFeignClient.class)
public interface OrderClient {
    @GetMapping("orderservice/t-order/getOrderStatus/{courseId}/{userId}")
    public boolean getOrderStatus(@PathVariable("courseId") String courseId,@PathVariable("userId") String userId);
}
