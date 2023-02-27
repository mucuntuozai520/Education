package com.linfeng.eduservice.service.impl;

import com.linfeng.eduservice.mapper.EduVideoMapper;
import com.linfeng.eduservice.service.EduVideoService;
import com.linfeng.eduservice.client.VodClient;
import com.linfeng.eduservice.entity.EduVideo;
import com.linfeng.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-27
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //注入vodClient
    @Autowired
    private VodClient vodClient;


    //    1根据课程id删除小节
// TODO: 2022/9/22  删除小节删除视频
    @Override
    public void removeVideoByCourseId(String courseId) {
        //根据课程id查询该课程所有的视频id
        QueryWrapper<EduVideo> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("course_id",courseId);
        QueryWrapper.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(QueryWrapper);

        ArrayList<String> videoIds = new ArrayList<>();
        //遍历每个videoSourceId并add到集合内返回
        for (EduVideo eduVideo : eduVideoList) {
            String videoSourceId = eduVideo.getVideoSourceId();

            if(StringUtils.isNotEmpty(videoSourceId)){
                videoIds.add(videoSourceId);
            }
        }

        //将所有视频id的集合传入 并调用批量删除方法
        //根据多个视频id删除多个视频
        if(videoIds.size()>0) {
            vodClient.deleteBatch(videoIds);
        }

        //删除完该课程的小节和视频后，再删除该课程
        QueryWrapper<EduVideo> Wrapper = new QueryWrapper<>();
        Wrapper.eq("course_id",courseId);
        baseMapper.delete(Wrapper);
    }

    //判断是否重复小节名
    @Override
    public void videoTitleCheck(EduVideo video) {
        if(StringUtils.isEmpty(video.getTitle().trim())){
            throw new MyException(20001,"小节名为空，请重新输入！");
        }
        //查询数据库看是否有重复的小节名
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",video.getTitle());
        queryWrapper.eq("chapter_id",video.getChapterId());
        if (baseMapper.selectCount(queryWrapper) > 0){
            throw new MyException(20001,"小节名重复，请重新输入！");
        }
    }
}
