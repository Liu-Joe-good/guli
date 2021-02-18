package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-18
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Cacheable(value = "teachers",key = "'getAllHotTeacher'")
    @Override
    public List<EduTeacher> getAllHotTeacher() {
        QueryWrapper<EduTeacher> eduTeacherQueryWrapper = new QueryWrapper<>();
        eduTeacherQueryWrapper.orderByDesc("id");
        eduTeacherQueryWrapper.last("limit 4");
        List<EduTeacher> eduTeachers = baseMapper.selectList(eduTeacherQueryWrapper);
        return eduTeachers;
    }
}
