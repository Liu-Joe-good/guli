package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.oss.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Autowired
    EduCourseService eduCourseService;

    /**
     * 新增课程
     *
     * @param courseInfoVo 前端页面课程各个属性
     * @return 此处返回的是课程id，没有此返回值，前端页面下一步所需课程id就无法确定
     */
    @ApiOperation("新增课程")
    @PostMapping("/addCourseInfo")
    public R addCourse(@ApiParam("课程的属性") @RequestBody CourseInfoVo courseInfoVo) {
        String id = eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    /**
     * 返回上一步，回显功能实现
     *
     * @param courseId 获取url上的id
     * @return 返回已添加的课程信息
     */
    @ApiOperation("返回上一步，回显功能实现")
    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourse(@ApiParam("课程id") @PathVariable String courseId) {
        CourseInfoVo courseInfoVo = eduCourseService.getCourseInfo(courseId);
        return R.ok().data("CourseInfoVo", courseInfoVo);
    }

    /**
     * 修改课程信息
     *
     * @param courseId     获取url上的id
     * @param courseInfoVo 获取回显的信息
     * @return 返回信息修改是否成功
     */
    @ApiOperation("修改课程信息")
    @PutMapping("/updateCourseInfo/{courseId}")
    public R updateCourse(@ApiParam("课程id") @PathVariable String courseId, @ApiParam("课程的属性") CourseInfoVo courseInfoVo) {
        courseInfoVo.setId(courseId);
        eduCourseService.updateCourseInfo(courseInfoVo);
        // 这里无需返回R.error？因为具体实现方法里面有throw异常
        return R.ok();
    }

}

