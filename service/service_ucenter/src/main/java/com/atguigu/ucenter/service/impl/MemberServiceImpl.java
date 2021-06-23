package com.atguigu.ucenter.service.impl;

import com.atguigu.oss.commonutils.JwtUtils;
import com.atguigu.oss.commonutils.MD5;
import com.atguigu.oss.servicebase.exceptionhandler.GuliException;
import com.atguigu.ucenter.entity.Member;
import com.atguigu.ucenter.entity.RegisterVo;
import com.atguigu.ucenter.mapper.MemberMapper;
import com.atguigu.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.reflections.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-02-08
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String login(Member member) {
            String mobile = member.getMobile();
            String password = member.getPassword();
            if(Utils.isEmpty(mobile) || Utils.isEmpty(password)){
                throw new GuliException(20001,"登录失败");
            }
            // 密码加盐值
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            byte[] digest = md5.digest(password.getBytes("utf-8"));
//            BASE64Encoder base64Encoder = new BASE64Encoder();
//            String encode = base64Encoder.encode(digest);
            // 上述代码可实现，也可用老师的MD5工具包：
            String encrypt = MD5.encrypt(password);

            QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
            memberQueryWrapper.eq("mobile",mobile);
            //memberQueryWrapper.eq("password",s);
            Member one = memberService.getOne(memberQueryWrapper);

            // 参数校验
            if(one==null){
                throw new GuliException(20001,"登录失败");
            }
            // System.out.println(encrypt);
            if(!one.getPassword().equals(encrypt)){
                throw new GuliException(20001,"登录失败");
            }

            if(one.getIsDisabled()){
                throw new GuliException(20001,"登录失败");
            }

            // 使用JWT工具加token,得到id和昵称来生成token
            String jwtToken = JwtUtils.getJwtToken(one.getId(), one.getNickname());
            return jwtToken;

    }

    @Override
    public boolean register(RegisterVo registerVo) {
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String code = registerVo.getCode();
        String password = registerVo.getPassword();
        if(Utils.isEmpty(mobile) || Utils.isEmpty(password) || Utils.isEmpty(nickname) || Utils.isEmpty(code)){
            throw new GuliException(20001,"必须填写完整");
        }
        // 首先判断数据库中是否已存在
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mobile",mobile);
        Member one = this.getOne(memberQueryWrapper);
        if(one!=null){
            throw new GuliException(20001,"手机号已注册");
        }
        QueryWrapper<Member> memberQueryWrapper2 = new QueryWrapper<>();
        memberQueryWrapper2.eq("nickname",nickname);
        Member two = memberService.getOne(memberQueryWrapper2);
        if(two!=null){
            throw new GuliException(20001,"昵称已存在");
        }
        // 获取redis中key键对应的值
        String o = redisTemplate.opsForValue().get(mobile);

        if(o==null || !o.equals(code)){
            throw new GuliException(20001,"验证码错误");
        }
        //        System.out.println(o.toString());
        // 判断都成功即入库
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setAvatar("https://edu-2020-7-5.oss-cn-shenzhen.aliyuncs.com/2020/10/12/ec795a9a21f843a894422ccc46078d99file.png");
        member.setIsDisabled(false);
        // 下面不行，因为registerVo有的短信属性menber对象没有，所以加不上，只能上述方式加
        // BeanUtils.copyProperties(member,registerVo);
        int save = baseMapper.insert(member);
        if (save!=0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Member judgeOpenid(String openid) {
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        Member one = memberService.getOne(wrapper);
        return one;
    }
}
