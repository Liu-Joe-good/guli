package com.atguigu.eduservice.controller.front;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.oss.commonutils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author liuzixuan
 * @ClassName CourseController
 * @Date 2021/06/24
 */
@Api(description = "前台课程管理")
@CrossOrigin
@RestController
@RequestMapping("/eduservice/frontCourse")
public class CourseController {
    @Autowired
    EduTeacherService eduTeacherService;

    @Autowired
    EduCourseService eduCourseService;

    @Autowired
    EduChapterService eduChapterService;


    @ApiOperation(value = "获取前台课程的分页列表")
    @PostMapping("/findAllCourse/{current}/{limit}")
    public R findAllCourse(@PathVariable Long current, @PathVariable Long limit, @RequestBody(required = false) CourseQueryVo courseQueryVo) {
        Page<EduCourse> page = new Page<>(current,limit);// current 当前页,0默认为第一页 limit当前页显示几条
        Map<String,Object> map= eduCourseService.frontCourselist(current,page,courseQueryVo);
        return R.ok().data("map",map);
    }

    @ApiOperation(value = "获取前台课程详情")
    @GetMapping("/findCourseById/{id}")
    public R findCourseById(@PathVariable String id) {
        // 将课程信息全查出
        CourseWebVo courseWebVo= eduCourseService.getCourseById(id);

        // 课程章节小节信息全取出
        List<ChapterVo> subjectList = eduChapterService.GetChapterandVideoList(id);

        return R.ok().data("courseWebVo",courseWebVo).data("subjectList",subjectList);
    }
}
