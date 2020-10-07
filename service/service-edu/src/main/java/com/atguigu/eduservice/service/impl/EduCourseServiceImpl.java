package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.oss.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-09
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    EduCourseDescriptionService eduCourseDescriptionService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        // 先将courseInfoVo转换为EduCourse
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        // 把信息放入edu_Course表中
        int i = baseMapper.insert(eduCourse);
        if (i == 0) {
            new GuliException(20001, "添加课程信息失败");
        }
        // 把信息放入edu_Course_description表中
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescription.setId(eduCourse.getId());
        eduCourseDescriptionService.save(eduCourseDescription);
        return eduCourse.getId();
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        // 获取课程信息
        EduCourse eduCourse = baseMapper.selectById(courseId);
        BeanUtils.copyProperties(eduCourse, courseInfoVo);
        // 获取详细课程信息
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(eduCourseDescription.getDescription());
        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        // 修改课程信息
        int a = baseMapper.updateById(eduCourse);
        if (a == 0) {
            throw new GuliException(20001, "修改课程信息失败"); // 若没有throw只是new，则不会显示异常的
            // 当执行update时，swagger测试时关于@putmapping,入参属性没有输入值表示不做更改，不代表把值改为了空
        }

        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(eduCourse.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        // 修改详细课程信息
        boolean b = eduCourseDescriptionService.updateById(eduCourseDescription);
        if (!b) {
            throw new GuliException(20001, "修改详细课程信息失败");
        }
    }
}
