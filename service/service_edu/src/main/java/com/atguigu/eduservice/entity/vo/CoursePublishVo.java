package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CoursePublishVo对象", description="最终课程确认")
public class CoursePublishVo {
//    private String id;
//    private String Name; // 课程名称
//    private BigDecimal Price; // 课程价格
//    private String SubjectparentId; // 章节ID
//    private String SubjectId; // 小节ID
//    private String Cover; // 封面
//    private String Description; // 简介
//    private String teacherId; //讲师ID
    private String id;
    private String title; // 课程名称
    private String cover; // 封面
    private Integer lessonNum; // 课时数
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price; // 只用于显示
    // private String description; // 少了cover
}
