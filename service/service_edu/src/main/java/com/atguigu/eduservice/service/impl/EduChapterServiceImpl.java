package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.oss.commonutils.R;
import com.atguigu.oss.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-09
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VodClient vodClient;

    @Override
    public List<ChapterVo> GetChapterandVideoList(String id) {
        // 1. 获取数据库的章节信息
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("course_id",id);
        List<EduChapter> eduChapterlist= baseMapper.selectList(wrapper);
        // 2. 获取数据库的小节信息
        //List<VideoVo> eduvideolist=eduVideoService.getVideolist(id);
        // 这种方法也行eduVideoService.list(wrapper2);
        List<EduVideo> eduvideolist=eduVideoService.list(wrapper);
        // 3.把章节信息和小节信息转换为VO封装起来
        List<ChapterVo> chapterVoList=new ArrayList<>();
        for (int i = 0; i < eduChapterlist.size(); i++) {
            // 此对象必须在里面new
            // 在循环体外，始终都是这一个对象，循环放入的都是最新的值。
            // 在循环体内，创建的是不同的对象，每次放入的对应这不同值的对象。
            ChapterVo chapterVo=new ChapterVo();
            EduChapter eduChapter=eduChapterlist.get(i);
            BeanUtils.copyProperties(eduChapter,chapterVo);

            List<VideoVo> videoVos=new ArrayList<>();
            for (int j = 0; j < eduvideolist.size(); j++) {
                VideoVo videoVo=new VideoVo();
                EduVideo eduVideo=eduvideolist.get(j);
                BeanUtils.copyProperties(eduVideo,videoVo);
                if(eduChapter.getId().equals(eduVideo.getChapterId())){
                    videoVos.add(videoVo);
                }
                chapterVo.setList(videoVos);
            }

            chapterVoList.add(chapterVo);
        }

        return chapterVoList;
    }

    // 通过章节id来删除章节
    @Override
    public boolean deleteChapterById(String id) {
        // 因为章节里面有小节，故有两种删除方法：
        // 1.有小节就做判断，有就不删
        // 2.不管是否有都删除章节
        /**
         * 第二点思路：先删章节下所有视频，再删章节下所有小节，最后删除章节
         */
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",id);
        // 得到小节个数，没有则直接删除章节
        int count = eduVideoService.count(queryWrapper);
        if (count>0){
            // 当第一种方式时，前端页面会有提示信息:修改失败，章节下面有小节
            // throw new GuliException(20001,"修改失败，章节下面有小节");

            // 通过章节id获取List小节属性
            List<EduVideo> videolistWithChapter = eduVideoService.getVideolistWithChapter(id);
            // 得到List视频id
            List<String> list=videolistWithChapter.stream().map(EduVideo::getVideoSourceId).collect(Collectors.toList());
            System.out.println(Arrays.toString(list.toArray()));
            if(list.size()!=0){
                R r = vodClient.deleteVideoByChapterId(list);
                if(r.getCode()==20001){
                    throw new GuliException(20001,"宕机了呀");
                }
                System.out.println("删除视频列表成功");
            }
            // 得到List小节的 ids
            List<String> ids=videolistWithChapter.stream().map(EduVideo::getId).collect(Collectors.toList());
            if(list.size()!=0){
                boolean b = eduVideoService.removeByIds(ids);
                System.out.println("删除小节列表成功");
            }
        }
            int i = baseMapper.deleteById(id);
            // i>0直接进行了判断
            return i>0;
    }


        @Override
    public void deleteChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_id",courseId);
        baseMapper.delete(wrapper2);
    }
}
