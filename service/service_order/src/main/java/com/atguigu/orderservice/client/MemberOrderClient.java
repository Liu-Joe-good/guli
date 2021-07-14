package com.atguigu.orderservice.client;

import com.atguigu.oss.commonutils.orderVO.MemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter",fallback = MemberOrderFeignClient.class)
public interface MemberOrderClient {

    /**
     * 通过id获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/api/ucenter/member/getMemberOrder/{id}")
    public MemberOrder getMemberOrder(@PathVariable(value = "id") String id);
}
