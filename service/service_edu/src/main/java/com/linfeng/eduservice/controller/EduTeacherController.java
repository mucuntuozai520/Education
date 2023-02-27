package com.linfeng.eduservice.controller;


import com.linfeng.commonutils.R;
import com.linfeng.eduservice.entity.EduTeacher;
import com.linfeng.eduservice.entity.vo.TeacherQuery;
import com.linfeng.eduservice.service.EduTeacherService;
import com.linfeng.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-17
 */
@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    //查询所有讲师
    @GetMapping("getTeacherList")
    public R findAllTeacher() {
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("list",list);
    }
    //添加讲师
    @PostMapping("saveTeacher")
    public R addTeacher(@RequestBody EduTeacher teacherInfo) {
        teacherService.saveTeacherInfo(teacherInfo);
        return R.ok();
    }
    //删除讲师
    @DeleteMapping("{teacherId}")
    public R removeByTeacher(@PathVariable String teacherId) {
        teacherService.removeById(teacherId);
        return R.ok();
    }
    //讲师列表条件查询带分页
    @PostMapping("teacherConditionQuery/{current}/{limit}")
    public R teacherConditionQuery(@PathVariable Long current,
                                  @PathVariable Long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {
        Page<EduTeacher> page = new Page<>(current,limit);
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //拼接条件
        if(!StringUtils.isEmpty(name)) {
            queryWrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)) {
            queryWrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create",end);
        }
        queryWrapper.orderByDesc("gmt_create");
        teacherService.page(page, queryWrapper);
        //总记录数
        long total = page.getTotal();
        //讲师list
        List<EduTeacher> list = page.getRecords();
        return  R.ok().data("total", total).data("list", list);
    }
    //根据讲师id进行查询
    @GetMapping("getTeacherInfo/{teacherId}")
    public R getTeacher(@PathVariable String teacherId) {
        EduTeacher teacherInfo = teacherService.getById(teacherId);
        return R.ok().data("teacher",teacherInfo);
    }
    //讲师修改功能
    @PostMapping("updateTeacherInfo")
    public R updateTeacher(@RequestBody EduTeacher teacherInfo) {
        try {
            teacherService.updateById(teacherInfo);
        }catch (Exception e){
            throw new MyException(20001,"讲师名重复，请重新输入");
        }
        return R.ok();
    }




}

