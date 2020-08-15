package com.atguigu.oss.servicebase.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // 有参构造方法
@NoArgsConstructor  // 无参构造方法
public class GuliException extends RuntimeException {
    private Integer code;
    private String msg;

}
