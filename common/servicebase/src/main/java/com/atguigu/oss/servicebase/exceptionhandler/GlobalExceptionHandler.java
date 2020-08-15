package com.atguigu.oss.servicebase.exceptionhandler;


import com.atguigu.oss.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice // 实现全局异常处理
@Slf4j // 输出异常信息到文件中
public class GlobalExceptionHandler {
    // 出现什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody // 不在controller中，为了返回json数据加这个
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理");
    }

    @ExceptionHandler(ArithmeticException.class) // 特殊异常
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常");
    }

    @ExceptionHandler(GuliException.class) // 自定义异常
    @ResponseBody
    public R error(GuliException e){
        log.error(e.getMsg()); // 程序出现异常，就会把异常信息输出到文件中,logback-spring.xml中有文件位置
        e.printStackTrace();
        return R.error().message(e.getMsg()).code(e.getCode()); // getMsg()此处别与getmessage()混淆
    }
}
