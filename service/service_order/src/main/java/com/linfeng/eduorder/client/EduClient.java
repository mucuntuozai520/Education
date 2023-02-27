package com.linfeng.eduorder.client;

import com.linfeng.commonutils.ordervo.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Yu1
 * @date 2022/10/11 - 18:16
 */
@Component
@FeignClient(name = "service-edu")
public interface EduClient {

    //根据课程id查询课程信息
    @PostMapping("/eduservice/frontCourse/getOrderCourseInfo/{courseId}")
    public CourseWebVoOrder getOrderCourseInfo(@PathVariable("courseId") String courseId);
}
