package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.oss.commonutils.R;
import com.atguigu.oss.commonutils.orderVO.EduCourseOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 课程 以及课程简介 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-09
 */
@RestController
//@CrossOrigin
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
    public R updateCourse(@ApiParam("课程id") @PathVariable String courseId, @ApiParam("课程的属性") @RequestBody CourseInfoVo courseInfoVo) {
        courseInfoVo.setId(courseId);
        eduCourseService.updateCourseInfo(courseInfoVo);
        // 这里无需返回R.error？因为具体实现方法里面有throw异常
        return R.ok();
    }

    @ApiOperation("课程的最终体现")
    @GetMapping("/getCoursePublish/{courseId}")
    public R getCoursePublish(@ApiParam("课程id") @PathVariable String courseId) {
        CoursePublishVo coursePublish = eduCourseService.getCoursePublish(courseId);
        return R.ok().data("coursePublish", coursePublish);
    }

    @ApiOperation("课程的发布成功")
    @PutMapping("/CourseRelease/{courseId}")
    public R CourseRelease(@ApiParam("课程id") @PathVariable String courseId) {
        eduCourseService.CourseRelease(courseId);
        // 为何不用boolean判断？
        return R.ok();
    }

    @ApiOperation(value = "通过联合条件查询分页列表")
    @PostMapping("pageCourseCondition/{current}/{limit}")
    // 入参类型为何为long？current当前页为第几页，limit每页最多显示几条数据,total为总数据条数 sql中的各个属性？
    public R pageCourseCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) CourseQuery courseQuery) { // required = false表允许为空
        Map map = eduCourseService.pageCondition(current, limit, courseQuery);
        return R.ok().data(map);
    }

    @ApiOperation(value = "删除课程")
    @DeleteMapping("deleteCourse/{courseId}")
    public R deleteCourse(@PathVariable String courseId) {
        // 不能这样删，删除课程要把与之相关的其他信息一并删除
        // eduCourseService.removeById(courseId);
        eduCourseService.removeCourse(courseId);
        return R.ok();
    }

    @ApiOperation(value = "通过id取课程信息")
    @GetMapping("getCourseOrder/{courseId}")
    public EduCourseOrder getCourseOrder(@PathVariable String courseId){
        // 这里在公共模块上创建了实体类，然后通过copyProperties把获取的对象信息直接复制过来
        CourseWebVo byId = eduCourseService.getCourseById(courseId);
        EduCourseOrder eduCourseOrder = new EduCourseOrder();
        BeanUtils.copyProperties(byId,eduCourseOrder);
        return eduCourseOrder;
    }
}

