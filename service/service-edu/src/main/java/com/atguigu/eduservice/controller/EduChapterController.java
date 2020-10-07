package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.oss.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-09
 */
@CrossOrigin
@RestController
@RequestMapping("/eduservice/chapter")
@Api(value = "课程章节小节管理")
public class EduChapterController {
    @Autowired
    private EduChapterService eduChapterService; // 加private

    // 通过课程id获取章节小节列表
    @GetMapping("/GetChapterandVideoList/{courseId}")
    public R GetChapterandVideoList(@PathVariable String courseId){
        List<ChapterVo> chapterVos=eduChapterService.GetChapterandVideoList(courseId);
        return R.ok().data("allChapterandVideoList",chapterVos);
    }
}

