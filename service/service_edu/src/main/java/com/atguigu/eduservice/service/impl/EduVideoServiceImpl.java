package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.oss.commonutils.R;
import com.atguigu.oss.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-09
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    VodClient vodClient;

    @Override
    public List<EduVideo> getVideolistWithChapter(String chapterId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("chapter_id",chapterId);
        List<EduVideo> eduvideoList= baseMapper.selectList(queryWrapper);
        return eduvideoList;
    }

    @Override
    public List<VideoVo> getVideolistWithCourse(String courseId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("course_id",courseId);
        List<EduVideo> eduvideoList= baseMapper.selectList(queryWrapper);
        // List<VideoVo> eduvideoList= baseMapper.selectList(queryWrapper);
        List<VideoVo> videoVoList=new ArrayList<>();
        for (EduVideo eduVideo:eduvideoList){
            VideoVo videoVo=new VideoVo();
            BeanUtils.copyProperties(eduVideo,videoVo);
            videoVoList.add(videoVo);
        }
        return videoVoList;
    }

    @Override
    public void deleteVideolistByCourseId(String courseId) {
        // 服务调用删除视频接口
        QueryWrapper<EduVideo> wrapperGetVideoSourceId=new QueryWrapper<>();
        wrapperGetVideoSourceId.eq("course_id",courseId);
        // 只得到video_source_id字段的值,如果取所有值，想只获取里面的id属性需调如下方法：
        // List<String> list=videolistWithChapter.stream().map(EduVideo::getVideoSourceId).collect(Collectors.toList());
        wrapperGetVideoSourceId.select("video_source_id");
        List<EduVideo> list =baseMapper.selectList(wrapperGetVideoSourceId);
        System.out.println(Arrays.toString(list.toArray()));
        // 由于是EduVideo，方法入参泛型为String,会报错，故需转换下
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            EduVideo eduVideo =list.get(i);
            if(eduVideo!=null){
                String videoSourceId = eduVideo.getVideoSourceId();
                // 不能把null值加入进来
                if(StringUtils.isNotEmpty(videoSourceId)){
                    ids.add(videoSourceId);
                }
            }
        }
        System.out.println(Arrays.toString(ids.toArray()));
        // 删除视频方法传入转换后的值
        if(ids!=null&&ids.size()!=0){
            R r = vodClient.deleteVideoByChapterId(ids);
            if(r.getCode()==20001){
                throw new GuliException(20001,"宕机了呀");
            }
        }

        // 然后删除小节
        QueryWrapper<EduVideo> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }

}
