package com.linfeng.eduservice.service.impl;

import com.linfeng.eduservice.entity.EduSubject;
import com.linfeng.eduservice.entity.subject.OneSubject;
import com.linfeng.eduservice.entity.subject.TwoSubject;
import com.linfeng.eduservice.listener.SubjectExcelListener;
import com.linfeng.eduservice.mapper.EduSubjectMapper;
import com.linfeng.eduservice.service.EduSubjectService;
import com.alibaba.excel.EasyExcel;
import com.linfeng.eduservice.entity.excel.SubjectData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-25
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try {
            //文件输入流
            InputStream in = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //课程分类列表
    @Override
    public List<OneSubject> getAllSubject() {
        //查询所有一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
        //查询所有二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);
        //创建最终封装数据finalList
        List<OneSubject> finalList = new ArrayList<>();
        //遍历一级分类list
        for (int i = 0; i < oneSubjectList.size(); i++) {
            EduSubject subjectOne = oneSubjectList.get(i);
            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(subjectOne,oneSubject);
            //遍历所有的二级分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            //遍历二级分类list
            for (int m = 0; m < twoSubjectList.size(); m++) {
                EduSubject subjectTow = twoSubjectList.get(m);
                //判断二级分类parentid和一级分类id是否一样
                if(subjectTow.getParentId().equals(subjectOne.getId())) {
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(subjectTow,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            //二级分类放到一级分类里面
            oneSubject.setChildren(twoFinalSubjectList);
            //每个一级分类放到finalList
            finalList.add(oneSubject);
        }
        return finalList;
    }
}
