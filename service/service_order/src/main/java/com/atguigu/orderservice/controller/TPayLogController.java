package com.atguigu.orderservice.controller;


import com.atguigu.orderservice.service.TPayLogService;
import com.atguigu.oss.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-12
 */
@RestController
@RequestMapping("/orderservice/t-pay-log")
//@CrossOrigin
public class TPayLogController {

    @Autowired
    TPayLogService payLogService;

    /**
     * 获取支付状态接口
     * @param orderNo
     * @return
     */
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        // 判断订单支付是否成功
        Map<String, String> map=payLogService.judgePay(orderNo);
        System.out.println("查询订单状态map集合"+map);
        if (map == null) { //出错
            return R.error().message("支付出错");
        }
        // 成功则把修改支付状态，并记录支付日志
        if (map.get("trade_state").equals("SUCCESS")) { // 如果成功
            //更改订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        // 当状态码为25000时，表示为支付中，前端不做任何处理（前端有前端response拦截器）
        return R.ok().code(25000).message("支付中");
    }
}

