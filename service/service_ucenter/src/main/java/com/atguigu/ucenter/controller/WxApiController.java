package com.atguigu.ucenter.controller;

import com.atguigu.oss.commonutils.JwtUtils;
import com.atguigu.oss.servicebase.exceptionhandler.GuliException;
import com.atguigu.ucenter.entity.Member;
import com.atguigu.ucenter.service.MemberService;
import com.atguigu.ucenter.util.ConstantPropertiesUtil;
import com.atguigu.ucenter.util.HttpClientUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;


/**
 * @author liuzixuan
 * @ClassName WxApiController
 * @Date 2021.06.16
 */
//@CrossOrigin
@Controller // RestController注解不使用的原因：它标记的类就是一个SpringMVC Controller 对象，但不需要返回json数据，使用的话会返回url的json数据
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/login")
    // 返回String 用于实现重定向
    public String getWXLogin() {
        // 返回微信二维码
        // 微信开放平台授权baseUrl
        // %s为占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
        "?appid=%s" +
        "&redirect_uri=%s" +
        "&response_type=code" +
        "&scope=snsapi_login" +
        "&state=%s" +
        "#wechat_redirect";
        // 给重定向url进行编码
        String redirectUrl = ConstantPropertiesUtil.REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl,"utf-8");
        }catch (UnsupportedEncodingException e){
            throw new GuliException(20001,e.getMessage());
        }
        // 给url的占位符对应的赋值
        String good = String.format(baseUrl, ConstantPropertiesUtil.APP_ID, redirectUrl, "atguigu");
        return "redirect:"+good;
    }

    // 此接口用于测试，实际开发中，不用如此
    @GetMapping("/callback")
    public String callback(String state,String code) {
        System.out.println("state="+state+" code="+code);
        //1.向认证服务器发送请求换取access_token，获取token和id
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
        "?appid=%s" +
        "&secret=%s" +
        "&code=%s" +
        "&grant_type=authorization_code";
        String accessTokenUrl = String.format(baseAccessTokenUrl, ConstantPropertiesUtil.APP_ID, ConstantPropertiesUtil.APP_SECRET, code);
        try{
            // 通过httpclient发送请求，得到返回结果
            String accessToken = HttpClientUtils.get(accessTokenUrl);
            // json转换工具
            Gson gson = new Gson();
            // 将字符串转map,然后获取其中的access_token和openid,openid相当于微信的id号，独一无二
            HashMap hashMap = gson.fromJson(accessToken, HashMap.class);
            String access_token = (String) hashMap.get("access_token");
            String openid = (String) hashMap.get("openid");
            // 将微信用户信息存入表中
            // 首先判断当前用户信息是否已存入数据表中
            Member vxmember= memberService.judgeOpenid(openid);
            if(vxmember==null){
                //2.访问微信的资源服务器，将access_token和openid传入URL中，用以获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                // 再次通过httpclient发送请求，获取用户信息
                String resultUserInfo = HttpClientUtils.get(userInfoUrl);
                HashMap hashMap2 = gson.fromJson(resultUserInfo,HashMap.class);
                String nickname = (String) hashMap2.get("nickname");
                String headimgurl = (String) hashMap2.get("headimgurl");

                Member member = new Member();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }
            // 通过id和昵称生成token,(此处不能用openid来生成，被坑惨了)
            String jwtToken = JwtUtils.getJwtToken(vxmember.getId(), vxmember.getNickname());
            return "redirect:http://localhost:3000?token="+jwtToken;
        }catch (Exception e){
            throw new GuliException(20001, "微信登录失败");
        }
    }

}
