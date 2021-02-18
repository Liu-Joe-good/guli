package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.oss.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    EduChapterService eduChapterService;

    @Autowired
    EduVideoService eduVideoService;

    @Autowired
    EduCourseService eduCourseService;

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
        if (eduCourseDescription!=null){ // 此处如果表中与courseId相关的表数据为空，则会报错
        // System.out.println(eduCourseDescription.getDescription());
            courseInfoVo.setDescription(eduCourseDescription.getDescription());
        }
        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        // 修改课程信息
        int a = baseMapper.updateById(eduCourse);
        // System.out.println(eduCourse.getId());
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

    @Override
    public CoursePublishVo getCoursePublish(String id) {
        return baseMapper.getCoursePublish(id); // 此处直接用了baseMapper,而没引入注解
    }

    @Override
    public void CourseRelease(String courseId) {
        // 我做的方法，比较冗余:
        /*EduCourse eduCourse = baseMapper.selectById(courseId);
        boolean flag=(("Draft").equals(eduCourse.getStatus()));
        if(flag){
            eduCourse.setStatus("Normal");
            baseMapper.updateById(eduCourse);
        }
        return flag;*/
        // mp可以如此做：只设定改变的值，那么其他的值不会改变：
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        baseMapper.updateById(eduCourse);
    }

    @Override
    public Map pageCondition(long current, long limit, CourseQuery courseQuery) {
        Page<EduCourse> page=new Page<>(current,limit);
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        Map<String, Object> map = new HashMap<>();
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        String begin = courseQuery.getBegin();
        String end = courseQuery.getFinish();
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(begin)){ // 在条件查询中，通过输入开始时间获取大于和等于该时间的值
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_modified",end);
        }
        // wrapper.orderByAsc("gmt_modified",end); // 把end加入会报错，SQLSyntaxErrorException,改为如下：
        wrapper.orderByDesc("gmt_modified");
        eduCourseService.page(page, wrapper);
        long total = page.getTotal();
        List<EduCourse> records = page.getRecords();
        map.put("total",total);
        map.put("rows",records); // 遇到个问题，key值为records时，前端取不到数据
        return map;
    }

    @Override
    public void removeCourse(String courseId) {
        // 明确删除顺序：视频，小节，章节，课程描述，课程
        // 以下注解的代码最好是写在对应的service里面
//        QueryWrapper<EduVideo> wrapper=new QueryWrapper<>();
//        wrapper.eq("course_id",courseId);
//        eduVideoService.remove(wrapper);
//        QueryWrapper<EduChapter> wrapper2 = new QueryWrapper<>();
//        wrapper2.eq("course_id",courseId);
//        eduChapterService.remove(wrapper2);

        // 删除了视频和小节
        eduVideoService.deleteVideolistByCourseId(courseId);
        // 删除章节
        eduChapterService.deleteChapterByCourseId(courseId);
        // 删除课程描述
        eduCourseDescriptionService.removeById(courseId);
        // eduCourseService.removeById(courseId); 与以下的一样
        // 删除课程
        int i = baseMapper.deleteById(courseId);
        if(i==0){
            throw new GuliException(20001,"删除课程失败");
        }
    }

    @Cacheable(value = "courses",key = "'getAllHotCourse'")
    @Override
    public List<EduCourse> getAllHotCourse() {
        QueryWrapper<EduCourse> euCourseWrapper = new QueryWrapper<>();
        euCourseWrapper.orderByDesc("id");
        euCourseWrapper.last("limit 8");
        List<EduCourse> eduCourses = baseMapper.selectList(euCourseWrapper);
        return eduCourses;
    }


}
