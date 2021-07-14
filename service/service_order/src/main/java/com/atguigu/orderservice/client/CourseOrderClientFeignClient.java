package com.atguigu.orderservice.client;

import com.atguigu.oss.commonutils.orderVO.EduCourseOrder;
import org.springframework.stereotype.Component;

/**
 * @author liuzixuan
 * @ClassName CourseOrderClientFeignClient
 * @Date
 */
@Component
public class CourseOrderClientFeignClient implements CourseOrderClient {
    @Override
    public EduCourseOrder getCourseOrder(String courseId) {
        return null;
    }
}
