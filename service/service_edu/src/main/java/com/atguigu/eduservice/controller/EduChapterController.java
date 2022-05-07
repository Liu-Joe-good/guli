package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.oss.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
//@CrossOrigin
@RestController
@RequestMapping("/eduservice/chapter")
@Api(description = "课程章节管理")
public class EduChapterController {
    @Autowired
    private EduChapterService eduChapterService; // 加private

    /**
     * 通过课程id获取章节小节列表
     * @param courseId
     * @return
     */
    @ApiOperation("通过课程id获取章节小节列表")
    @GetMapping("/GetChapterandVideoList/{courseId}")
    public R GetChapterandVideoList(@ApiParam("课程id") @PathVariable String courseId){
        List<ChapterVo> chapterVos=eduChapterService.GetChapterandVideoList(courseId);
        return R.ok().data("allChapterandVideoList",chapterVos);
    }

    // 增加章节
    @ApiOperation("增加章节")
    @PostMapping("/addChapter")
    public R addChapter(@ApiParam("章节属性") @RequestBody EduChapter eduChapter){
        // 为啥不需要courseId?()需要的！ EduChapter属性里没有这个，但前端有，所以couseID可以通过前端传进来
        // 这里不用vo了，因为增添章节直接用
        eduChapterService.save(eduChapter);
        return R.ok();
    }

    // 回显章节
    @ApiOperation("回显章节")
    @GetMapping("/getChapter/{ChapterId}")
    public R getChapter(@ApiParam("章节属性") @PathVariable String ChapterId){
        EduChapter eduChapter = eduChapterService.getById(ChapterId);
        return R.ok().data("eduChapter",eduChapter);
    }

    // 修改章节
    @ApiOperation("修改章节")
    @PutMapping("/updateChapter/{ChapterId}")
    public R updateChapter(@ApiParam("章节属性") @PathVariable String ChapterId,@RequestBody EduChapter eduChapter){
        //如果为get请求时，后台接收参数的注解应该为RequestParam，如果为post请求时，则后台接收参数的注解就是为RequestBody。
        eduChapter.setId(ChapterId);
        // save()为增加，updateById()为修改
        boolean b = eduChapterService.updateById(eduChapter);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }

    // 删除章节
    @ApiOperation("删除章节")
    @DeleteMapping("/deleteChapter/{ChapterId}")
    public R deleteChapter(@ApiParam("章节属性") @PathVariable String ChapterId){
        boolean flag=eduChapterService.deleteChapterById(ChapterId);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

