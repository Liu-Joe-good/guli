package com.atguigu.orderservice.client;

import com.atguigu.oss.commonutils.orderVO.EduCourseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-edu",fallback = CourseOrderClientFeignClient.class)
public interface CourseOrderClient {
    /**
     * 通过id获取课程信息
     * @param courseId
     * @return
     */
    @GetMapping("/eduservice/edu-course/getCourseOrder/{courseId}")
    public EduCourseOrder getCourseOrder(@PathVariable(value = "courseId") String courseId);
}
