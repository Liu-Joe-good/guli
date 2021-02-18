package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.Subject.OneSubject;
import com.atguigu.eduservice.entity.Subject.TwoSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.listener.subjectListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.oss.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-05
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {


    // 添加课程分类
    @Override
    public void saveSubjectData(MultipartFile file, EduSubjectService eduSubjectService) {
        // System.out.println("03");
        try {
            InputStream in=file.getInputStream();
            EasyExcel.read(in, SubjectData.class,new subjectListener(eduSubjectService)).sheet().doRead();
            // System.out.println("04");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<OneSubject> getAllOneTwoData() {
        // 获取一级分类数据
        QueryWrapper<EduSubject> wrapperOne=new QueryWrapper<>();
        wrapperOne.eq("parent_id", "0");
        List<EduSubject> eduOneSubjectlist = baseMapper.selectList(wrapperOne);
        // 获取二级分类数据
        QueryWrapper<EduSubject> wrapperTwo=new QueryWrapper<>();
        wrapperTwo.ne("parent_id", "0");
        List<EduSubject> eduTwoSubjectlist = baseMapper.selectList(wrapperTwo);

        // 封装一级分类数据
        List<OneSubject> finalSubjectList = new ArrayList<>(); // 此为返回的数据
        for (int i = 0; i < eduOneSubjectlist.size(); i++) {
            EduSubject eduSubject=eduOneSubjectlist.get(i);
            OneSubject subjectone=new OneSubject();
            BeanUtils.copyProperties(eduSubject,subjectone); // 类似与把one的get属性放到了subject的属性中
            // 此时把OneSubject类型的数据封装到了oneSubject中
            finalSubjectList.add(subjectone);
            // 封装二级分类数据
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            for (int j = 0; j < eduTwoSubjectlist.size(); j++) {
                EduSubject two=eduTwoSubjectlist.get(j);
                // 做判断是否为当前分类下二级分类
                if(two.getParentId().equals(subjectone.getId())){
                    TwoSubject twosubject=new TwoSubject();
                    BeanUtils.copyProperties(two,twosubject);
                    // 用于保存此时一级分类下twolist的
                    twoFinalSubjectList.add(twosubject);
                    // subjectone.setChirldren(twosubject);
                }
            }
            // 加入此二级list到一级分类中
            subjectone.setChildren(twoFinalSubjectList);
        }
        return finalSubjectList;
    }


}
