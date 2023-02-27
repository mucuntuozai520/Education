package com.linfeng.eduservice.controller;


import com.linfeng.eduservice.service.EduChapterService;
import com.linfeng.commonutils.R;
import com.linfeng.eduservice.entity.EduChapter;
import com.linfeng.eduservice.entity.chapter.ChapterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/eduservice/chapter")

public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    //添加章节
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter chapter){
        //查询数据库看是否有重复的章节名
        chapterService.chapterTitleCheck(chapter);
        chapterService.save(chapter);
        return R.ok();
    }
    //删除章节
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){
        chapterService.deleteChapter(chapterId);
        return R.ok();
    }
    //根据章节id查询
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){
        EduChapter chapter = chapterService.getById(chapterId);
        return R.ok().data("chapter",chapter);
    }
    //根据课程id进行查询课程大纲页面的章节小节
    @GetMapping("getAllChapterVideo/{courseId}")
    public R getAllChapterVideo(@PathVariable String courseId) {
        List<ChapterVo> list = chapterService.getAllChapterVideoByCourseId(courseId);
        return R.ok().data("allChapterVideo",list);
    }
    //修改章节
    @Transactional
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter chapter){
        chapterService.deleteChapter(chapter.getId());
        //查询数据库看是否有重复的章节名
        chapterService.chapterTitleCheck(chapter);
        chapterService.save(chapter);
        return R.ok();
    }

}

