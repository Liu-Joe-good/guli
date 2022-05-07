package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-07-12
 */
public interface TPayLogService extends IService<TPayLog> {
    /**
     * 查询订单支付状态
     * @param orderId
     * @return
     */
    Map<String, String> judgePay(String orderId);

    /**
     * 修改支付状态，并记录支付日志
     * @param map
     */
    void updateOrderStatus(Map<String, String> map);
}
