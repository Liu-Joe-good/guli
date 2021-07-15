package com.atguigu.orderservice.controller;


import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.oss.commonutils.JwtUtils;
import com.atguigu.oss.commonutils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-12
 */
@RestController
@RequestMapping("/orderservice/t-order")
@CrossOrigin
public class TOrderController {
    @Autowired
    TOrderService tOrderService;

    /**
     * 生成订单
     * @return
     */
    @PostMapping("/saveOrder/{courseId}")
    public R saveOrder(HttpServletRequest httpServletRequest, @PathVariable String courseId){
        // 获取token中用户id
        String userId = JwtUtils.getMemberIdByJwtToken(httpServletRequest);
        String tOrderNo = tOrderService.saveOrder(userId,courseId);
        return R.ok().data("tOrderNo",tOrderNo);
    }

    /**
     * 通过订单id获取订单信息
     * @param orderId
     * @return
     */
    @GetMapping("getOrder/{orderId}")
    public R getOrder(@PathVariable String orderId) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        TOrder order = tOrderService.getOne(wrapper);
        return R.ok().data("item", order);
    }

    /**
     * 生成微信支付二维码
     * @param orderId
     * @return
     */
    @GetMapping("createNative/{orderId}")
    public R createNative(@PathVariable String orderId){
        // 返回包括二维码以及其他需要的信息
        Map map=tOrderService.createNative(orderId);
        return R.ok().data("map",map);
    }
}

