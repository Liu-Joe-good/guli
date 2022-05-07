package com.atguigu.orderservice.service.impl;

import com.atguigu.orderservice.client.CourseOrderClient;
import com.atguigu.orderservice.client.MemberOrderClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.mapper.TOrderMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.util.ConstantPropertiesOrder;
import com.atguigu.orderservice.util.HttpClient;
import com.atguigu.oss.commonutils.OrderNoUtil;
import com.atguigu.oss.commonutils.orderVO.EduCourseOrder;
import com.atguigu.oss.commonutils.orderVO.MemberOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
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

    @Autowired
    private TOrderService tOrderService;

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
        try {
            // 首先通过订单id获取订单信息
            QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderId);
            TOrder tOrder = baseMapper.selectOne(wrapper);
            HashMap<String, String> m = new HashMap<>();
            // 1、设置支付参数
            m.put("appid", ConstantPropertiesOrder.APP_ID);// #微信支付ID
            m.put("mch_id", ConstantPropertiesOrder.MCH_ID);// #商户号

            m.put("nonce_str", WXPayUtil.generateNonceStr()); // 生成一个随机字符串，使生成的二维码都不一样
            m.put("body", tOrder.getCourseTitle()); // 课程名称
            m.put("out_trade_no", tOrder.getOrderNo()); // 订单单号
            // 因为订单价格以分为单位，元的单位要乘以100，最后转为long类型，以字符串的形式
            m.put("total_fee", tOrder.getTotalFee().multiply(new BigDecimal("100")).longValue() + ""); // 订单价格

            m.put("spbill_create_ip", ConstantPropertiesOrder.SPBILL_CREATE_IP); // 服务器ip地址为本地，故为此
            m.put("notify_url", ConstantPropertiesOrder.NOTIFY_URL);// #回调地址：支付之后做个回调
            m.put("trade_type", "NATIVE");// 类型：根据价格生成二维码的类型

            // 2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");// 去请求微信支付生成二维码的地址
            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, ConstantPropertiesOrder.APP_KEY)); // 根据微信商户key，把map转xml进行加密，更加安全
            client.setHttps(true);
            client.post();
            //3、获取请求后返回的内容(为xml格式)
            String xml = client.getContent();
            // xml格式需转换下给前端
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml); // xml格式转map
            //4、封装返回结果集(因为还需要客户单号，课程id,价格，等信息也得装入)
            Map map = new HashMap<>();
            map.put("out_trade_no", tOrder.getOrderNo());
            map.put("course_id", tOrder.getCourseId());
            map.put("total_fee", tOrder.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));
            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);
            return map;
        }catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public boolean getOrderStatus(String courseId, String userId) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",userId);
        wrapper.eq("status","1");
        int count = tOrderService.count(wrapper);
        if (count!=0){
            return true;
        }
        return false;
    }


}
