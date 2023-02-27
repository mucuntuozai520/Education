package com.linfeng.eduservice.controller.front;

import com.linfeng.eduservice.client.OrdersClient;
import com.linfeng.eduservice.entity.EduCourse;
import com.linfeng.eduservice.entity.frontvo.CourseWebVo;
import com.linfeng.eduservice.service.EduChapterService;
import com.linfeng.eduservice.service.EduCourseService;
import com.linfeng.commonutils.JwtUtils;
import com.linfeng.commonutils.R;
import com.linfeng.commonutils.ordervo.CourseWebVoOrder;
import com.linfeng.eduservice.entity.chapter.ChapterVo;
import com.linfeng.eduservice.entity.frontvo.CourseFrontVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/frontCourse")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrdersClient ordersClient;

    //条件查询带分页查询课程
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page,
                                @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> coursePage = new Page<>(page, limit);
        Map<String,Object> map = courseService.getFrontCourseList(coursePage,courseFrontVo);
        return R.ok().data(map);
    }
    //课程详情
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){ //
        //根据课程id查询课程基本信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);
        //根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getAllChapterVideoByCourseId(courseId);
        //查询是否登录，从token获取id,未登录则返回isBuy=false
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",false);
        }else {//若已登录，查询课程是否支付过了
            boolean buyCourse = ordersClient.isBuyCourse(courseId,memberId);
            return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",buyCourse);
        }
    }

    //根据课程id查询课程信息
    @PostMapping("getOrderCourseInfo/{courseId}")
    public CourseWebVoOrder getOrderCourseInfo(@PathVariable String courseId){
        CourseWebVo courseInfo = courseService.getBaseCourseInfo(courseId);
        CourseWebVoOrder orderCourseInfo = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo,orderCourseInfo);
        return orderCourseInfo;
    }

}












