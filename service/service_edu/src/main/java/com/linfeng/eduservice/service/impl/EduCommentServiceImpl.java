package com.linfeng.eduservice.service.impl;

import com.linfeng.eduservice.entity.EduComment;
import com.linfeng.eduservice.service.EduCommentService;
import com.linfeng.eduservice.mapper.EduCommentMapper;
import com.linfeng.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-10-10
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    //判断登录
    @Override
    public void loginCheck(String memberId) {
        if(StringUtils.isEmpty(memberId)) {
            throw new MyException(20001,"请登录");
        }
    }
}
