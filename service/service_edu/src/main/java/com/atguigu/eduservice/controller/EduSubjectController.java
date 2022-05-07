package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.Subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.oss.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-05
 */
@Api(description = "课程分类管理")
@RestController
@RequestMapping("/eduservice/edu-subject")
//@CrossOrigin
public class EduSubjectController {
    @Autowired
    EduSubjectService eduSubjectService;
    // 从excal中新增课程
    @PostMapping("AddSubject")
    public R addSubject(MultipartFile file){
        // System.out.println("01");
        eduSubjectService.saveSubjectData(file,eduSubjectService);
        // System.out.println("02");
        return R.ok();
    }

    @GetMapping("getSubjectList")
    public R getSubjectList(){
        // 返回分类列表
        List<OneSubject> list = eduSubjectService.getAllOneTwoData();
        return R.ok().data("list",list);
    }

}

