package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeacherQuery {
    @ApiModelProperty(value = "讲师姓名")
    private String name;
    @ApiModelProperty(value = "讲师头衔")
    private Integer level;
    @ApiModelProperty(value = "查询开始时间",example = "2020-6-24 0:43:55")
    private String begin; // 使用的String类型，前端传入的数据无需类型转换
    @ApiModelProperty(value = "查询结束时间",example = "2020-6-24 0:43:55")
    private String end;
}
