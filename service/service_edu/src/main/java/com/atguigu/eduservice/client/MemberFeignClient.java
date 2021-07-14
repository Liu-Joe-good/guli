package com.atguigu.eduservice.client;

import com.atguigu.eduservice.entity.Member;
import org.springframework.stereotype.Component;

/**
 * @author liuzixuan
 * @ClassName MemberFeignClient
 * @Date
 */
@Component
public class MemberFeignClient implements MemberClient{
    @Override
    public Member getMember(String id) {
        // 为Null表示没有调用成功
        return null;
    }
}
