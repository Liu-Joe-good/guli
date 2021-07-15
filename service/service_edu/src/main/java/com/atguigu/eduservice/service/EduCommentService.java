package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-07-07
 */
public interface EduCommentService extends IService<EduComment> {
    /**
     * 获取用户的信息，并把课程id，讲师id存入用户评论表
     * @param id
     * @param eduComment
     */
    boolean addComment(String id, EduComment eduComment);
}
