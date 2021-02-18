package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CourseQuery {
    @ApiModelProperty(value = "课程标题")
    private String title;
    @ApiModelProperty(value = "课程状态")
    private String status;
    @ApiModelProperty(value = "查询开始时间",example = "2020-6-24 0:43:55")
    private String begin;
    @ApiModelProperty(value = "查询结束时间",example = "2020-6-24 0:43:55")
    private String finish;
}
