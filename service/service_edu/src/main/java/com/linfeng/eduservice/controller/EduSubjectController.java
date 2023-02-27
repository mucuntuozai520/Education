package com.linfeng.eduservice.controller;


import com.linfeng.eduservice.entity.subject.OneSubject;
import com.linfeng.eduservice.service.EduSubjectService;
import com.linfeng.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-25
 */
@RestController
@RequestMapping("/eduservice/subject")

public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file) {
        //上传过来excel文件
        subjectService.saveSubject(file,subjectService);
        return R.ok();
    }
    //课程分类列表
    @GetMapping("getSubjectList")
    public R getAllSubject() {
        List<OneSubject> list = subjectService.getAllSubject();
        return R.ok().data("list",list);
    }
}

