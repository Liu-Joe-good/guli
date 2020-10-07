package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<ChapterVo> GetChapterandVideoList(String id) {
        // 1. 获取数据库的章节信息
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("course_id",id);
        List<EduChapter> eduChapterlist= baseMapper.selectList(wrapper);

        // 2. 获取数据库的小节信息
        List<EduVideo> eduvideolist=eduVideoService.getVideolist(id);
        // 这种方法也行eduVideoService.list(wrapper2);

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
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    videoVos.add(videoVo);
                }
                chapterVo.setList(videoVos);
            }
            chapterVoList.add(chapterVo);
        }

        return chapterVoList;
    }
}
