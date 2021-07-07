package com.atguigu.eduservice.entity.Subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
/**
 * 一级分类数据
 */
public class OneSubject {
    private String id;
    private String title;
    private List<TwoSubject> children=new ArrayList<>();
}
