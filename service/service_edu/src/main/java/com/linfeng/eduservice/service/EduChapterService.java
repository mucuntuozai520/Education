package com.linfeng.eduservice.service;

import com.linfeng.eduservice.entity.EduChapter;
import com.linfeng.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-08-27
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getAllChapterVideoByCourseId(String courseId);

    void deleteChapter(String chapterId);

    void removeChapterByCourseId(String courseId);

    void chapterTitleCheck(EduChapter eduChapter);
}
