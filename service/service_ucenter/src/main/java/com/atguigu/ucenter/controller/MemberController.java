package com.atguigu.ucenter.controller;


import com.atguigu.oss.commonutils.JwtUtils;
import com.atguigu.oss.commonutils.R;
import com.atguigu.oss.commonutils.orderVO.MemberOrder;
import com.atguigu.ucenter.entity.Member;
import com.atguigu.ucenter.entity.RegisterVo;
import com.atguigu.ucenter.service.MemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-02-08
 */
@RestController
@RequestMapping("/api/ucenter/member")
@CrossOrigin
public class MemberController {
    @Autowired
    private MemberService memberService;

    // 登录
    @PostMapping("/login")
    public R loginUser(@RequestBody Member member){
            // 取到用户名和密码，看与数据库是否有，有则提示登录成功
            // token用于单点登录
            String token=memberService.login(member);
            return R.ok().data("token",token);
    }

    // 注册
    @PostMapping("/register")
    public R register(@RequestBody RegisterVo registerVo){
        // 取到用户名和密码，看与数据库是否有，有则提示登录成功
        boolean flag=memberService.register(registerVo);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    /**
     * 通过token获取用户id
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest httpServletRequest){
        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(httpServletRequest);
        Member byId = memberService.getById(memberIdByJwtToken);
        return R.ok().data("memberInfo",byId);
    }

    /**
     * 通过用户id获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/getMember/{id}")
    public Member getMember(@PathVariable String id){
        Member getMember = memberService.getById(id);
        return getMember;
    }

    /**
     * 通过用户id获取用户信息,用于接口调用
     * @param id
     * @return
     */
    @GetMapping("/getMemberOrder/{id}")
    public MemberOrder getMemberOrder(@PathVariable String id){
        Member getMember = memberService.getById(id);
        MemberOrder memberOrder = new MemberOrder();
        BeanUtils.copyProperties(getMember,memberOrder);
        return memberOrder;
    }
}

