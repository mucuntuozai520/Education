package com.linfeng.eduservice.service.impl;

import com.linfeng.eduservice.entity.vo.CourseInfoVo;
import com.linfeng.eduservice.mapper.EduCourseMapper;
import com.linfeng.eduservice.service.EduChapterService;
import com.linfeng.eduservice.service.EduCourseService;
import com.linfeng.eduservice.service.EduVideoService;
import com.linfeng.eduservice.entity.EduCourse;
import com.linfeng.eduservice.entity.EduCourseDescription;
import com.linfeng.eduservice.entity.frontvo.CourseFrontVo;
import com.linfeng.eduservice.entity.frontvo.CourseWebVo;
import com.linfeng.eduservice.entity.vo.CoursePublishVo;
import com.linfeng.eduservice.service.EduCourseDescriptionService;
import com.linfeng.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-27
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService eduChapterService;

    //添加课程信息
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        if(StringUtils.isEmpty(courseInfoVo.getTitle().trim())
                || StringUtils.isEmpty(courseInfoVo.getSubjectId().trim())
                || StringUtils.isEmpty(courseInfoVo.getLessonNum())
                || StringUtils.isEmpty(courseInfoVo.getSubjectParentId().trim())
                || StringUtils.isEmpty(courseInfoVo.getCover().trim())
                || StringUtils.isEmpty(courseInfoVo.getDescription().trim())
                || StringUtils.isEmpty(courseInfoVo.getPrice())){
            throw new MyException(20001,"课程信息不能为空");
        }
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,course);
        //课程信息添加到数据库
        baseMapper.insert(course);
        String courseId = course.getId();
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        //设置描述id为课程id
        courseDescription.setId(courseId);
        //课程描述添加到数据库
        eduCourseDescriptionService.save(courseDescription);
        return courseId;
    }

    //根据课程id查询课程基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        //查询描述表 并封装数据
        EduCourseDescription courseDescription = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        baseMapper.updateById(eduCourse);
        //修改描述表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.updateById(description);
    }

    @Override
    public CoursePublishVo publicCourseInfo(String id) {
        //调用mapper
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);

        return publishCourseInfo;
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
        //删除小节
        eduVideoService.removeVideoByCourseId(courseId);
        //删除章节
        eduChapterService.removeChapterByCourseId(courseId);
        //删除描述
        eduCourseDescriptionService.removeById(courseId);
        //删除课程本身
        if(baseMapper.deleteById(courseId) == 0){
            throw new MyException(20001,"删除失败");
        }
    }

    //分页查询课程
    @Override
    public Map<String, Object> getFrontCourseList(Page<EduCourse> page, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        //判断是否有查询条件
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) {
            queryWrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())) {
            queryWrapper.eq("subject_id", courseFrontVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())) {
            queryWrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) {
            queryWrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) {
            queryWrapper.orderByAsc("price");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getTitle())) {
            queryWrapper.like("title",courseFrontVo.getTitle().trim());
        }
        baseMapper.selectPage(page, queryWrapper);
        List<EduCourse> records = page.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("list", records);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return map;
    }

    //得到课程基本信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}

