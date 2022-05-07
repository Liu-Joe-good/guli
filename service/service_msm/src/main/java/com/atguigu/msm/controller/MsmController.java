package com.atguigu.msm.controller;

import com.atguigu.msm.service.MsmService;
import com.atguigu.msm.utils.RandomUtil;
import com.atguigu.oss.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 短信发送的方法
     * @param phone
     * @return
     */
    @GetMapping("/send/{phone}")
    public R code(@PathVariable String phone){
        // 如何设置验证码时间呢？使用redis，来设置时间
        // 获取redis中key为phone的值
        String o = redisTemplate.opsForValue().get(phone);
        // 不为空表示已发送，不做下面的处理
        if (!StringUtils.isEmpty(o)){
            return R.ok();
        }

        // 调用util生成随机数并存入hashmap中
        String sixBitRandom = RandomUtil.getSixBitRandom();
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("code",sixBitRandom);

        // 调用service
        //boolean flag=msmService.send(stringStringHashMap,phone);
//        if(flag){
//            // 设置超时为5分钟
//            redisTemplate.opsForValue().set(phone,o, 5,TimeUnit.MINUTES);
//            return R.ok();
//        }else {
//            return R.error().message("发送短信失败");
//        }
        // 因为阿里云要网站备案，故现在可模拟阿里云生成短信服务,把上面的注释掉
        redisTemplate.opsForValue().set(phone,sixBitRandom,5,TimeUnit.MINUTES);
        return R.ok();
    }
}
