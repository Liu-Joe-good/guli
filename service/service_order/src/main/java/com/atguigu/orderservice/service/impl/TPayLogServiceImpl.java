package com.atguigu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.entity.TPayLog;
import com.atguigu.orderservice.mapper.TPayLogMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.service.TPayLogService;
import com.atguigu.orderservice.util.ConstantPropertiesOrder;
import com.atguigu.orderservice.util.HttpClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-07-12
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    private TOrderService tOrderService;

    @Override
    public Map<String, String> judgePay(String orderId) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", ConstantPropertiesOrder.APP_ID);// #微信支付ID
            m.put("mch_id", ConstantPropertiesOrder.MCH_ID);// #商户号
            m.put("out_trade_no", orderId);
            m.put("nonce_str", WXPayUtil.generateNonceStr());// 随机参数
            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateOrderStatus(Map<String, String> map) {
        String orderNo=map.get("out_trade_no");
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        TOrder one = tOrderService.getOne(wrapper);
        Integer status = one.getStatus();
        // 如果支付状态已经改变，return
        if(status==1)
            return;
        one.setStatus(1);
        // 修改支付状态为1
        boolean b = tOrderService.updateById(one);

        // 增加支付记录
        TPayLog tPayLog = new TPayLog();
        tPayLog.setOrderNo(orderNo);
        tPayLog.setPayTime(new Date());
        tPayLog.setTotalFee(one.getTotalFee());
        tPayLog.setTransactionId(map.get("transaction_id"));
        tPayLog.setTradeState(map.get("trade_state"));
        tPayLog.setPayType(1);// 为微信支付
        tPayLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(tPayLog);
    }
}
