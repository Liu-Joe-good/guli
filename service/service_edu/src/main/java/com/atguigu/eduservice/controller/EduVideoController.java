package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.oss.commonutils.R;
import com.atguigu.oss.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-09
 */
//@CrossOrigin
@RestController
@RequestMapping("/eduservice/edu-video")
@Api(description = "课程小节管理")
public class EduVideoController {
    @Autowired
    VodClient vodClient;
    @Autowired
    EduVideoService eduVideoService;

    @ApiOperation("通过课程id查询所有小节")
    @GetMapping("/getVideoList/{courseId}")
    public R getVideoList(@ApiParam("章节ID") @PathVariable String courseId){
        List<VideoVo> VideoVoList = eduVideoService.getVideolistWithCourse(courseId);
        return R.ok().data("VideoVoList",VideoVoList);
    }

    @ApiOperation("增加小节")
    @PostMapping("/addVideo")
    public R addVideo(@ApiParam("小节属性") @RequestBody EduVideo eduVideo){
        // 不用考虑加章节id和课程id（必要属性），前端可以传,故此时传的时EduVideo
        boolean flag = eduVideoService.save(eduVideo);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation("通过小节id回显当前小节")
    @GetMapping("/getVideo/{videoId}")
    public R getVideo(@ApiParam("小节ID") @PathVariable String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return R.ok().data("eduVideo",eduVideo);
    }

    @ApiOperation("修改小节")
    @PutMapping("/updateVideo/{videoId}")
    public R updateVideo(@ApiParam("小节ID") @PathVariable String videoId,@ApiParam("小节属性") @RequestBody EduVideo eduVideo){
        eduVideo.setId(videoId);
        // 此处swagger出现错误，重新写updateById（）就好了，可能是先前实现类写了这个方法，又删了，这个也要跟重新声明下
        boolean flag = eduVideoService.updateById(eduVideo);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }
    // TODO 这个方法后面需要完善，删除小节，视频也得删除（已完成）
    @ApiOperation("删除小节")
    @DeleteMapping("/deleteVideo/{videoId}")
    public R deleteVideo(@ApiParam("小节ID") @PathVariable String videoId){
        // 此处通过videoId来获取对象，从而得到videoSourceId
        // QueryWrapper<EduVideo> queryWrapper=new QueryWrapper();
        // queryWrapper.eq("id",videoId);
        // 等待证明
        EduVideo one = eduVideoService.getById(videoId);
        String videoSourceId = one.getVideoSourceId();
        // 通过注册中心调用生产者服务端的接口

        R r = vodClient.deleteVideo(videoSourceId);
        if(r.getCode()==20001){
            throw new GuliException(20001,"宕机了呀");
        }
        boolean flag = eduVideoService.removeById(videoId);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

