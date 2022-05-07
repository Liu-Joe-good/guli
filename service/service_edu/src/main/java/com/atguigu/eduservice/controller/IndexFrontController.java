package com.atguigu.eduservice.controller;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.oss.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("/eduservice/indexfront")
@Api("前台页面数据管理")
public class IndexFrontController {
    @Autowired
    EduCourseService eduCourseService;

    @Autowired
    EduTeacherService eduTeacherService;

//    // 热门课程显示前八条数据
//    @GetMapping("/getHotCourse")
//    public R getHotCourse() {
//        List<EduCourse> eduCourses = eduCourseService.getAllHotCourse();
//        return R.ok().data("list", eduCourses);
//    }
//    // 热门讲师显示前4条数据
//    @GetMapping("/getHotTeacher")
//    public R getHotTeacher() {
//        List<EduTeacher> list = eduTeacherService.getAllHotTeacher();
//        return R.ok().data("list", list);
//    }
    // 可合成为以下接口
    @GetMapping("/getIndex")
    public R index() {
        List<EduCourse> courseList = eduCourseService.getAllHotCourse();
        List<EduTeacher> teacherList = eduTeacherService.getAllHotTeacher();
        return R.ok().data("courseList", courseList).data("teacherList",teacherList);
    }
}
