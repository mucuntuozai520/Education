package com.linfeng.eduservice.service;

import com.linfeng.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-10-10
 */
public interface EduCommentService extends IService<EduComment> {

    void loginCheck(String memberId);
}
