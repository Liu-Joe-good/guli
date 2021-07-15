package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.MemberClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.Member;
import com.atguigu.eduservice.mapper.EduCommentMapper;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-07-07
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Autowired
    private MemberClient memberClient;

    @Override
    public boolean addComment(String id, EduComment eduComment) {
        // 远程调用通过token获取id的接口
        Member r = memberClient.getMember(id);
        int insert=0;
        if (r!=null){
            eduComment.setMemberId(r.getId());
            eduComment.setNickname(r.getNickname());
            eduComment.setAvatar(r.getAvatar());
            insert = baseMapper.insert(eduComment);
        }
        // 当用户存在，则填入数据库
        if (insert!=0){
            return true;
        }
        return false;
    }
}
