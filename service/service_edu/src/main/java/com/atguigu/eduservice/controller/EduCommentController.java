package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import com.atguigu.oss.commonutils.JwtUtils;
import com.atguigu.oss.commonutils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-07
 */
@Api(description = "课程评论管理")
@RestController
@RequestMapping("/eduservice/edu-comment")
public class EduCommentController {
    @Autowired
    EduCommentService eduCommentService;
    /**
     * 获取评论分页列表
     * @param current
     * @param limit
     * @return
     */
    @ApiOperation("评论分页列表")
    @GetMapping("/GetCommentList/{current}/{limit}")
    public R GetChapterandVideoList(@ApiParam("当前页") @PathVariable Long current,@PathVariable Long limit){
        Page<EduComment> page = new Page<EduComment>(current,limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        HashMap<String, Object> map = new HashMap<>();
        wrapper.orderByDesc("gmt_create");
        IPage<EduComment> pageResult = eduCommentService.page(page, wrapper);

        long pages = page.getPages();
        List<EduComment> records = page.getRecords();
        long size = page.getSize();
        long total = page.getTotal();
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();

        map.put("current",current);
        map.put("items", records);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return R.ok().data("map",map);
    }
    @ApiOperation("用户评论")
    @PostMapping("/addComment")
    public R addComment(HttpServletRequest httpServletRequest,@RequestBody EduComment eduComment){
        // 通过获取header中的token，得到用户id
        String id = JwtUtils.getMemberIdByJwtToken(httpServletRequest);
        // 获取用户的信息，把课程id，讲师id存入用户评论表
        boolean b = eduCommentService.addComment(id, eduComment);
        if (b){
            return R.ok();
        }
        return R.error().message("用户不存在或未知错误");
    }
}

