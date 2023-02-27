package com.linfeng.eduservice.service.impl;

import com.linfeng.eduservice.mapper.EduChapterMapper;
import com.linfeng.eduservice.service.EduChapterService;
import com.linfeng.eduservice.service.EduVideoService;
import com.linfeng.eduservice.entity.EduChapter;
import com.linfeng.eduservice.entity.EduVideo;
import com.linfeng.eduservice.entity.chapter.ChapterVo;
import com.linfeng.eduservice.entity.chapter.VideoVo;
import com.linfeng.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-27
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;//注入小节service

    //根据课程id进行查询章节小节
    @Override
    public List<ChapterVo> getAllChapterVideoByCourseId(String courseId) {
        //根据课程id查询章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);
        //根据课程id查询小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);
        //创建list集合，用于最终封装数据
        List<ChapterVo> finalList = new ArrayList<>();
        //遍历查询章节list集合
        for (int i = 0; i < eduChapterList.size(); i++) {
            //获取每一个章节数据
            EduChapter chapter = eduChapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            //把chapterVo放到最终list集合
            finalList.add(chapterVo);
            //创建集合，用于封装章节的小节
            List<VideoVo> videoList = new ArrayList<>();
            //遍历查询小节list集合，进行封装
            for (int m = 0; m < eduVideoList.size(); m++) {
                //获取每一个小节数据
                EduVideo video = eduVideoList.get(m);
                //判断：小节里面chapterid和章节里面id是否一样
                if(video.getChapterId().equals(chapter.getId())) {
                    //进行封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);
                    //放到小节封装集合
                    videoList.add(videoVo);
                }
            }
            //把封装之后小节list集合，放到章节对象里面
            chapterVo.setChildren(videoList);
        }
        return finalList;
    }

    //删除章节的方法
    @Override
    public void deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("chapter_id",chapterId);
        //根据章节id查是否有小节
        if (videoService.count(QueryWrapper) > 0){
            throw new MyException(20001,"无法删除有小节的章节");
        } else {
            baseMapper.deleteById(chapterId);
        }
    }

    //删除课程时删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> Wrapper = new QueryWrapper<>();
        Wrapper.eq("course_id",courseId);
        baseMapper.delete(Wrapper);
    }

    //判断是否重复章节名
    @Override
    public void chapterTitleCheck(EduChapter chapter) {
        if(StringUtils.isEmpty(chapter.getTitle().trim())){
            throw new MyException(20001,"章节名为空，请重新输入！");
        }
        //查询数据库看是否有重复的章节名
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",chapter.getTitle());
        queryWrapper.eq("course_id",chapter.getCourseId());
        if (baseMapper.selectCount(queryWrapper) > 0){
            throw new MyException(20001,"章节名重复，请重新输入！");
        }
    }
}
