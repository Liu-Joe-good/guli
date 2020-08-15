package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.oss.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 以及课程简介 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-09
 */
@RestController
@CrossOrigin
@RequestMapping("/eduservice/edu-course")
@Api(description = "课程简介管理")
public class EduCourseController {
    // 新增课程
    @Autowired
    EduCourseService eduCourseService;
    @PostMapping("/addCourseInfo")
    public R addCourse(@RequestBody CourseInfoVo courseInfoVo){
        String id=eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id); // 此处返回的是课程id，没有此返回值，前端页面下一步所需课程id就无法确定
    }
}

