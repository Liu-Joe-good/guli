package com.atguigu.eduservice.controller.front;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.oss.commonutils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuzixuan
 * @ClassName TeacherFrontController
 * @Date 2021.06.22
 */
@Api(description = "前台讲师管理")
//@CrossOrigin
@RestController
@RequestMapping("/eduservice/frontTeacher")
public class TeacherFrontController {
    @Autowired
    EduTeacherService eduTeacherService;

    @Autowired
    EduCourseService eduCourseService;

    @ApiOperation(value = "获取前台讲师的分页列表")
    @GetMapping("/findAllTeacher/{current}/{limit}")
    public Map<String, Object> findAllTeacher(@PathVariable Long current,@PathVariable Long limit) {
        Page<EduTeacher> page = new Page<>(current,limit);// current 当前页,0默认为第一页 limit当前页显示几条
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("sort");
        eduTeacherService.page(page,wrapper);
        List<EduTeacher> records = page.getRecords();// 每条具体数据列表
        // current当前页
        long total = page.getTotal();// 总记录数（一个多少条）
        long size = page.getSize();// 当前页总记录数
        long pages = page.getPages();// 总页数
        boolean hasNext = page.hasNext();// 上页是否有
        boolean hasPrevious = page.hasPrevious();// 下页是否有

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("current",current);
        map.put("items", records);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        // 此处code码得加上，因为前端拦截器reponse有拦截
        map.put("code", 20000);
        return map;
    }

    @ApiOperation(value = "获取前台讲师详情")
    @GetMapping("/findTeacherById/{id}")
    public R findTeacherById(@PathVariable String id){
        // 通过讲师id获取课程基本信息
        EduTeacher byId = eduTeacherService.getById(id);
        // 通过讲师id获取讲师基本信息
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",id);
        //按照最后更新时间倒序排列
        wrapper.orderByDesc("gmt_modified");
        List<EduCourse> courseList = eduCourseService.list(wrapper);

        return R.ok().data("teacher",byId).data("courseList",courseList);
    }
}
