package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-09
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo getCoursePublish(String id);

    void CourseRelease(String courseId);

    Map pageCondition(long current, long limit, CourseQuery courseQuery);

    void removeCourse(String courseId);

    List<EduCourse> getAllHotCourse();

    /**
     * 获取前台的课程列表
     * @param page
     * @param courseQueryVo
     * @return
     */
    Map<String, Object> frontCourselist(Long current,Page<EduCourse> page, CourseQueryVo courseQueryVo);

    /**
     * 获取课程的详细信息
     * @param id
     * @return
     */
    CourseWebVo getCourseById(String id);
}
