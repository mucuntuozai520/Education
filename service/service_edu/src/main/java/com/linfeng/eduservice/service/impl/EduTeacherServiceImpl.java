package com.linfeng.eduservice.service.impl;

import com.linfeng.eduservice.mapper.EduTeacherMapper;
import com.linfeng.eduservice.entity.EduTeacher;
import com.linfeng.eduservice.service.EduTeacherService;
import com.linfeng.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-17
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    //讲师列表分页
    @Override
    public Map<String, Object> getFrontTeacherList(Page<EduTeacher> page) {
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        baseMapper.selectPage(page,queryWrapper);
        //获取每页数据的list集合
        List<EduTeacher> list = page.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        map.put("items", page.getRecords());
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return map;
    }

    //添加讲师
    @Override
    public void saveTeacherInfo(EduTeacher teacherInfo) {
        if(StringUtils.isEmpty(teacherInfo.getName().trim())
                || StringUtils.isEmpty(teacherInfo.getIntro().trim())
                || StringUtils.isEmpty(teacherInfo.getCareer().trim())
                || StringUtils.isEmpty(teacherInfo.getLevel())
                || StringUtils.isEmpty(teacherInfo.getAvatar().trim())
                || StringUtils.isEmpty(teacherInfo.getSort())){
            throw new MyException(20001,"讲师信息不能为空");
        }
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",teacherInfo.getName());
        if (baseMapper.selectCount(queryWrapper) > 0){
            throw new MyException(20001,"讲师名重复，请重新输入！");
        }
        baseMapper.insert(teacherInfo);
    }
}
