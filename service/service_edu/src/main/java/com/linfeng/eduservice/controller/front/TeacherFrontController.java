package com.linfeng.eduservice.controller.front;

import com.linfeng.eduservice.entity.EduCourse;
import com.linfeng.eduservice.entity.EduTeacher;
import com.linfeng.eduservice.service.EduCourseService;
import com.linfeng.eduservice.service.EduTeacherService;
import com.linfeng.commonutils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Yu1
 * @date 2022/9/29 - 15:27
 */
@RestController

@RequestMapping("/eduservice/teacherFront")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    //讲师列表
    @PostMapping("getFrontTeacherList/{page}/{limit}")
    public R getFrontTeacherList(@PathVariable long page,@PathVariable long limit){
        Page<EduTeacher> teacherPage = new Page<>(page,limit);
        Map<String,Object> map = teacherService.getFrontTeacherList(teacherPage);
        return R.ok().data(map);
    }
    //讲师详情
    @GetMapping("getFrontTeacherInfo/{teacherId}")
    public R getFrontTeacherInfo(@PathVariable String teacherId){
        EduTeacher teacher = teacherService.getById(teacherId);
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",teacherId);
        List<EduCourse> list = courseService.list(queryWrapper);
        return R.ok().data("teacher",teacher).data("courseList",list);
    }

}
