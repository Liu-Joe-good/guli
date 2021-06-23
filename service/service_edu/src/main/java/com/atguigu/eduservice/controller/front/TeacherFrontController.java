package com.atguigu.eduservice.controller.front;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduTeacherService;
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
@CrossOrigin
@RestController
@RequestMapping("/eduservice/frontTeacher")
public class TeacherFrontController {
    @Autowired
    EduTeacherService eduTeacherService;

    @ApiOperation(value = "获取前台讲师的分页列表")
    @GetMapping("/findAllTeacher/{current}/{limit}")
    public Map<String, Object> findAllTeacher(@PathVariable long current,@PathVariable long limit) {
        Page<EduTeacher> page = new Page<>(current,limit);// current 当前页,0默认为第一页 limit当前页显示几条
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("sort");
        eduTeacherService.page(page,wrapper);
        List<EduTeacher> records = page.getRecords();// 每条具体数据列表
        long total = page.getTotal();// 总记录数（一个多少条）
        long size = page.getSize();// 总页数
        long pages = page.getCurrent();// current当前页
        boolean hasNext = page.hasNext();// 上页是否有
        boolean hasPrevious = page.hasPrevious();// 下页是否有

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }
}
