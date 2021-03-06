package com.atguigu.ucenter.service;

import com.atguigu.ucenter.entity.Member;
import com.atguigu.ucenter.entity.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-02-08
 */
public interface MemberService extends IService<Member> {

    String login(Member member);

    boolean register(RegisterVo registerVo);
}
