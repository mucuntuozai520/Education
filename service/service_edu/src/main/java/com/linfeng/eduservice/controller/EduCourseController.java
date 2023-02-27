package com.linfeng.eduservice.controller;


import com.linfeng.eduservice.entity.vo.CourseInfoVo;
import com.linfeng.eduservice.service.EduCourseService;
import com.linfeng.commonutils.R;
import com.linfeng.eduservice.entity.EduCourse;
import com.linfeng.eduservice.entity.vo.CoursePublishVo;
import com.linfeng.eduservice.entity.vo.CourseQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-27
 */
@RestController
@RequestMapping("/eduservice/course")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    //课程列表
    @GetMapping
    public R getCourseList() {
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list",list);
    }
    //课程列表条件查询带分页
    @PostMapping("courseConditionQuery/{current}/{limit}")
    public R courseConditionQuery(@PathVariable Long current,
                                  @PathVariable Long limit,
                                  @RequestBody(required = false) CourseQuery courseQuery) {
        Page<EduCourse> page = new Page<>(current,limit);
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        //拼接条件
        if(!StringUtils.isEmpty(title)) {
            queryWrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status)) {
            queryWrapper.eq("status",status);
        }
        queryWrapper.orderByDesc("gmt_create");
        courseService.page(page, queryWrapper);
        //总记录数
        long total = page.getTotal();
        //课程list
        List<EduCourse> list = page.getRecords();
        return  R.ok().data("total", total).data("list", list);
    }
    //添加课程基本信息
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        String courseId = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",courseId);
    }
    //删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }
    //根据课程id查询课程基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfo",courseInfoVo);
    }
    //修改课程基本信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }
    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = courseService.publicCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }
    //发布课程
    @PostMapping("publishCourse/{courseId}")
    public R publishCourse(@PathVariable String courseId){
        EduCourse course = new EduCourse();
        course.setId(courseId);
        course.setStatus("normal");//设置课程发布状态
        courseService.updateById(course);
        return R.ok();
    }
}

