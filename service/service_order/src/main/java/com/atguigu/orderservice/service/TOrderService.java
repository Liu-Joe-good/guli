package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-07-12
 */
public interface TOrderService extends IService<TOrder> {
    /**
     * 生成订单
     * @param userId
     * @param courseId
     * @return
     */
    String saveOrder(String userId, String courseId);

    /**
     * 生成微信支付二维码
     * @param orderId
     * @return
     */
    Map createNative(String orderId);
}
