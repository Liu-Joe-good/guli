package com.atguigu.orderservice.client;

import com.atguigu.oss.commonutils.orderVO.MemberOrder;
import org.springframework.stereotype.Component;

/**
 * @author liuzixuan
 * @ClassName MemberFeignClient
 * @Date
 */
@Component
public class MemberOrderFeignClient implements MemberOrderClient {
    @Override
    public MemberOrder getMemberOrder(String id) {
        return null;
    }
}
