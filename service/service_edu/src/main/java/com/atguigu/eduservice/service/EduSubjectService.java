package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.Subject.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-05
 */
public interface EduSubjectService extends IService<EduSubject> {
    void saveSubjectData(MultipartFile file, EduSubjectService eduSubjectService);

    /**
     * 返回课程一二级分类数据
     * @return
     */
    List<OneSubject> getAllOneTwoData();
}
