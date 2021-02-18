package com.atguigu.vod.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtils implements InitializingBean { // 实现这个接口后
    @Value("${aliyun.vod.file.keyid}")
    private String keyId;
    @Value("${aliyun.vod.file.keysecret}")
    private String keySecret;
    public static String KEY_ID;
    public static String KEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        KEY_ID =keyId;
        KEY_SECRET=keySecret;
    }
}
