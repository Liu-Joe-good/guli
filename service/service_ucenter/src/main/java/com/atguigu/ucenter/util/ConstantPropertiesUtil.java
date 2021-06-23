package com.atguigu.ucenter.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liuzixuan
 * @ClassName ConstantPropertiesUtil
 * @Date
 */
// InitializingBean此接口用来初始化bean
@Component
public class ConstantPropertiesUtil implements InitializingBean {
    @Value("${appid}")
    private String appId;
    @Value("${appsecret}")
    private String appSecret;
    @Value("${redirecturl}")
    private String redirectUrl;

    public static String APP_ID;
    public static String APP_SECRET;
    public static String REDIRECT_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        APP_ID=appId;
        APP_SECRET=appSecret;
        REDIRECT_URL=redirectUrl;
    }
}
