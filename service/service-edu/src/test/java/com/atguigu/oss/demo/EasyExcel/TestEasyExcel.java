package com.atguigu.oss.demo.EasyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        String filename = "D:\\1.xlsx";
//        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());
        EasyExcel.read(filename, DemoData.class, new ExcelListener()).sheet().doRead();
    }
    public static List<DemoData> getData(){
        List<DemoData> list=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData demoData=new DemoData();
            demoData.setSno(i);
            demoData.setSname("学生"+i);
            list.add(demoData);
        }
        return list;
    }
}
