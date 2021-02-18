package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.oss.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.val;

import java.util.Map;

public class subjectListener extends AnalysisEventListener<SubjectData> {
    // 此类因为不能交由spring管理，需要自己手动new，不能注入对象
    public EduSubjectService eduSubjectService;
    public subjectListener() {
    }
    public subjectListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService=eduSubjectService;
    }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        // System.out.println("111");
        if(subjectData==null){
            new GuliException(20001,"文件数据为空");
        }
        // 添加一级分类
        EduSubject oneEduSubject=this.existOneSubject(eduSubjectService,subjectData.getOneSubjectName());
        if(oneEduSubject==null){
            oneEduSubject=new EduSubject();
            oneEduSubject.setTitle(subjectData.getOneSubjectName());
            oneEduSubject.setParentId("0");
            eduSubjectService.save(oneEduSubject);
        }
        // System.out.println("222");
        String pid=oneEduSubject.getId();
        // System.out.println("333");
        // 添加二级分类
        EduSubject twoEduSubject=this.existTwoSubject(eduSubjectService,subjectData.getTwoSubjectName(),pid);
        // System.out.println("444");
        if(twoEduSubject==null){
            twoEduSubject=new EduSubject();
            twoEduSubject.setTitle(subjectData.getTwoSubjectName());
            twoEduSubject.setParentId(pid);
            eduSubjectService.save(twoEduSubject);
        }
        System.out.println("555");
    }
    // 因为是一行一行读数据的，第一个值为一级分类，第二个值为二级分类，第一分类可能重复，故需判断
    public EduSubject existOneSubject(EduSubjectService subjectService,String name){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject one = subjectService.getOne(wrapper);
        return one;
    }
    // 第二分类可能重复，故需判断
    public EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","pid");
        EduSubject one = subjectService.getOne(wrapper);
        return one;
    }

    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
