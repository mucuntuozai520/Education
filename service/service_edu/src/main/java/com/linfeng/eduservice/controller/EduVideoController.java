package com.linfeng.eduservice.controller;


import com.linfeng.eduservice.entity.EduVideo;
import com.linfeng.eduservice.service.EduVideoService;
import com.linfeng.commonutils.R;
import com.linfeng.eduservice.client.VodClient;
import com.linfeng.servicebase.exceptionhandler.MyException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-27
 */
@RestController
@RequestMapping("/eduservice/video")

public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("saveVideo")
    public R addVideo(@RequestBody EduVideo video){
        //判断是否重复小节名
        videoService.videoTitleCheck(video);
        videoService.save(video);
        return R.ok();
    }
    //删除小节
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId){
        //根据小节id获取视频id，调用方法进行删除
        EduVideo video = videoService.getById(videoId);
        String videoSourceId = video.getVideoSourceId();
        //调用阿里云提供的接口，删除该视频
        if(StringUtils.isNotEmpty(videoSourceId)){
            R result = vodClient.removeAlyVideo(videoSourceId);
            if(result.getCode() == 20001){
                throw new MyException(20001,"删除视频失败");
            }
        }
        videoService.removeById(videoId);
        return R.ok();
    }
    //修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo video){
        if (videoService.getById(video.getId()).getTitle().equals(video.getTitle())){
            return R.ok();
        }
        //判断是否重复小节名
        videoService.videoTitleCheck(video);
        videoService.updateById(video);
        return R.ok();
    }
    //根据小节id查询
    @GetMapping("getVideoInfo/{videoId}")
    public R getChapterInfo(@PathVariable String videoId){
        EduVideo video = videoService.getById(videoId);
        return R.ok().data("video",video);
    }



}

