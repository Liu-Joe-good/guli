package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-09
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> GetChapterandVideoList(String id);

    boolean deleteChapterById(String id);

    void deleteChapterByCourseId(String courseId);
}
