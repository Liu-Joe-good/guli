package com.atguigu.eduservice.client;

import com.atguigu.eduservice.entity.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter",fallback = MemberFeignClient.class)
public interface MemberClient {
    @GetMapping("api/ucenter/member/getMember/{id}")
    public Member getMember(@PathVariable("id") String id);
}
