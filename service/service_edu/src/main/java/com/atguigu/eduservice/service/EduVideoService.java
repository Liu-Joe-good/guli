package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-09
 */
public interface EduVideoService extends IService<EduVideo> {

    List<EduVideo> getVideolistWithChapter(String chapterId);

    List<VideoVo> getVideolistWithCourse(String courseId);

    void deleteVideolistByCourseId(String courseId);
}
