package com.linfeng.eduservice.controller.front;

import com.linfeng.eduservice.entity.EduCourse;
import com.linfeng.eduservice.entity.EduTeacher;
import com.linfeng.eduservice.service.EduCourseService;
import com.linfeng.eduservice.service.EduTeacherService;
import com.linfeng.commonutils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yu1
 * @date 2022/9/24 - 0:42
 */
@RestController
@RequestMapping("/eduservice/frontIndex")

public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    //查询前8个课程，查询前4条讲师
    @GetMapping("courseAndTeacherList")
    public R index(){
        //查询前8个课程
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("id");
        courseQueryWrapper.last("limit 8");
        List<EduCourse> courseList = courseService.list(courseQueryWrapper);
        //查询前4条名师
        QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.orderByDesc("id");
        teacherQueryWrapper.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(teacherQueryWrapper);
        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }
}
