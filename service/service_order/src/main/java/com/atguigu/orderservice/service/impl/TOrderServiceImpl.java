package com.atguigu.orderservice.service.impl;

import com.atguigu.orderservice.client.CourseOrderClient;
import com.atguigu.orderservice.client.MemberOrderClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.mapper.TOrderMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.oss.commonutils.OrderNoUtil;
import com.atguigu.oss.commonutils.orderVO.EduCourseOrder;
import com.atguigu.oss.commonutils.orderVO.MemberOrder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-07-12
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {
    @Autowired
    private CourseOrderClient courseOrderClient;

    @Autowired
    private MemberOrderClient memberOrderClient;

    @Override
    public String saveOrder(String userId, String courseId) {
        // 通过用户Id获取用户信息
        MemberOrder memberOrder = memberOrderClient.getMemberOrder(userId);
        // 通过课程Id获取课程信息
        EduCourseOrder courseInfo = courseOrderClient.getCourseOrder(courseId);
        //创建订单
        TOrder order = new TOrder();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        // 将课程具体信息存入
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfo.getTitle());
        order.setCourseCover(courseInfo.getCover());
        order.setTotalFee(courseInfo.getPrice());
        order.setTeacherName(courseInfo.getTeacherName());
        // 将用户具体信息存入
        order.setMemberId(userId);
        order.setMobile(memberOrder.getMobile());
        order.setNickname(memberOrder.getNickname());
        // status为0未支付，支付种类为微信
        order.setStatus(0);
        order.setPayType(1);

        baseMapper.insert(order);
        return order.getOrderNo();
    }

    @Override
    public Map createNative(String orderId) {
        // 首先通过订单id获取订单信息
        //
        return null;
    }
}
