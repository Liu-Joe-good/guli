package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.oss.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.oss.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-18
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")
@CrossOrigin // 解决跨域
public class EduTeacherController {
    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher() {
        List<EduTeacher> eduTeachers = eduTeacherService.list(null);
        return R.ok().data("items", eduTeachers);
    }

    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("deleteTeacherById/{id}") // 不能直接来测试,浏览器只能测GetMapping
    public R deleteTeacherById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {
        boolean a = eduTeacherService.removeById(id);
        if (a == true) {

            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "获取分页列表")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current, @PathVariable long limit) {
        Page<EduTeacher> pageTeacher = new Page<>(current, limit); // current 当前页
        // mp内部实现分页
        eduTeacherService.page(pageTeacher, null);
        long total = pageTeacher.getTotal(); // 总记录数
        // List<EduTeacher> list=eduTeacherService.list(null); // 此处不会实现分页显示数据
        List<EduTeacher> list = pageTeacher.getRecords(); // 数据list集合
        // return R.ok().data("total",total).data("rows",list); // 第一种return方法 链式编程
        Map map = new HashMap();
        map.put("total", total);
        map.put("list", list);
        return R.ok().data(map); // 第二种return方法
    }

    @ApiOperation(value = "通过联合条件查询分页列表")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(
            @PathVariable long current, @PathVariable long limit,
            @RequestBody(required = false) TeacherQuery teacherQuery) { // (required = false)表示可以为空，且有此注解的话，必须为@postmapping
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        // 以下建议在service层
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (name != "" && name != null) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin); // ge,大于等于“”里为表中的字段名称
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_modified", end);
        }
        // 排序
        wrapper.orderByDesc("gmt_modified");

        eduTeacherService.page(pageTeacher, wrapper);
        long total = pageTeacher.getTotal(); // 数据总行数
        List<EduTeacher> records = pageTeacher.getRecords(); // 数据列表
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "新增老师")
    @PostMapping("addTeacher")
    public R AddTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "根据ID来查询老师信息")
    @GetMapping("getTeacherById/{id}")
    public R getTeacherById(@PathVariable long id) {
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("eduteacher", eduTeacher);
        /*else { 不用判断原因可能是通过前端页面具体数据对应按钮来点击，不存在有为null的情况
            return R.error();
        }*/
    }

    /*@ApiOperation(value = "通过ID来修改老师信息")这是用PostMapping注解来修改老师信息
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean update= eduTeacherService.updateById(eduTeacher);
        if (update == false) {
            return R.error();
        }
        return R.ok();
    }*/
    @ApiOperation(value = "通过ID来修改老师信息")
    @PutMapping("updateTeacher/{id}")
    public R updateTeacher(@ApiParam(value = "老师id", name = "id", required = true) @PathVariable String id,
                           @ApiParam(value = "老师对象", name = "teacher", required = true) @RequestBody EduTeacher eduTeacher) {
        eduTeacher.setId(id);
        boolean update = eduTeacherService.updateById(eduTeacher);
        //int a=10/0; // 测试异常处理
        /*try {
            int a = 10 / 0;
        } catch (Exception e) {
            throw new GuliException(20001, "执行了自定义异常");
        }*/
        if (update == false) {
            return R.error();
        }
        return R.ok();
    }
}
