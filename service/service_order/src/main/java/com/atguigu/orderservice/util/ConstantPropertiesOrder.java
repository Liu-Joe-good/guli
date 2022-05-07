package com.atguigu.orderservice.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liuzixuan
 * @ClassName ConstantPropertiesOrder
 * @Date 20210716
 */
@Component
public class ConstantPropertiesOrder implements InitializingBean {
    @Value("${appid}")
    private String appId;
    @Value("${mchid}")
    private String mchId;
    @Value("${spbillcreateip}")
    private String spbillCreateIp;
    @Value("${notifyUrl}")
    private String notifyUrl;
    @Value("${appKey}")
    private String appKey;

    public static String APP_ID;
    public static String MCH_ID;
    public static String SPBILL_CREATE_IP;
    public static String NOTIFY_URL;
    public static String APP_KEY;

    @Override
    public void afterPropertiesSet() throws Exception {
        APP_ID=appId;
        MCH_ID=mchId;
        SPBILL_CREATE_IP=spbillCreateIp;
        NOTIFY_URL=notifyUrl;
        APP_KEY=appKey;
    }
}
