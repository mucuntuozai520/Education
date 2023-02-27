package com.linfeng.eduservice.service;

import com.linfeng.eduservice.entity.vo.CourseInfoVo;
import com.linfeng.eduservice.entity.EduCourse;
import com.linfeng.eduservice.entity.frontvo.CourseFrontVo;
import com.linfeng.eduservice.entity.frontvo.CourseWebVo;
import com.linfeng.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-08-27
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publicCourseInfo(String id);

    void removeCourse(String courseId);

    Map<String,Object> getFrontCourseList(Page<EduCourse> eduCoursePage, CourseFrontVo courseFrontVo);

    CourseWebVo getBaseCourseInfo(String courseId);

}
