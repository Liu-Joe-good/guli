package com.atguigu.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.utils.StringUtils;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msm.service.MsmService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(HashMap<String, String> sixBitRandom, String phone) {
        if(StringUtils.isEmpty(phone)) return false;
        // 默认文件：加上自己阿里云的id和秘钥
        DefaultProfile profile =
        DefaultProfile.getProfile("default", "LTAI4GCirUTAXerwExiYpJuN", "2gd5WWLGpnbaWVWQFpUhUbQo4XhbC8");
        IAcsClient client = new DefaultAcsClient(profile);

        // 设置相关固定参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        // 设置发送相关的参数
        request.putQueryParameter("PhoneNumbers", phone); // 手机号
        request.putQueryParameter("SignName", "我的谷粒在线教育网站"); // 阿里云里签名管理里的名称
        request.putQueryParameter("TemplateCode", "SMS_211491062"); // 阿里云里模板管理里的模板code
        // JSONObject.toJSONString为maven里面引入的fastjson依赖里转换工具里的,传的是json格式的(传map的好处)
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(sixBitRandom));

        // 最终发送
        try{
            CommonResponse commonResponse = client.getCommonResponse(request);
            boolean success = commonResponse.getHttpResponse().isSuccess();
            return success;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
