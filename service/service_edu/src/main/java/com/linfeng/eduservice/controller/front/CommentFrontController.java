package com.linfeng.eduservice.controller.front;

import com.linfeng.eduservice.entity.EduComment;
import com.linfeng.eduservice.service.EduCommentService;
import com.linfeng.commonutils.JwtUtils;
import com.linfeng.commonutils.R;
import com.linfeng.educenter.entity.UcenterMember;
import com.linfeng.eduservice.client.UcenterClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yu1
 * @date 2022/10/10 - 20:52
 */
@RestController
@RequestMapping("/eduservice/comment")
public class CommentFrontController {

    @Autowired
    private EduCommentService commentService;

    @Autowired
    private UcenterClient ucenterClient;

    //根据课程id查询评论列表
    @GetMapping("{page}/{limit}")
    public R getCommentList(@PathVariable Long page,
                            @PathVariable Long limit,
                            @RequestParam(value = "courseId") String courseId) {
        Page<EduComment> commentPage = new Page<>(page, limit);
        QueryWrapper<EduComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        //返回分页结果的list
        commentService.page(commentPage,queryWrapper);
        List<EduComment> list = commentPage.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("current", commentPage.getCurrent());
        map.put("pages", commentPage.getPages());
        map.put("size", commentPage.getSize());
        map.put("total", commentPage.getTotal());
        map.put("hasNext", commentPage.hasNext());
        map.put("hasPrevious", commentPage.hasPrevious());
        return R.ok().data(map);
    }

    //添加评论
    @PostMapping("save")
    public R save(@RequestBody EduComment comment, HttpServletRequest request) {
        //判断登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        commentService.loginCheck(memberId);
        //向评论的实体类set id
        comment.setMemberId(memberId);
        //通过id获取用户信息
        UcenterMember member = ucenterClient.getInfo(memberId);
        //向评论的实体类set 昵称 头像
        comment.setNickname(member.getNickname());
        comment.setAvatar(member.getAvatar());
        commentService.save(comment);
        return R.ok();
    }

    //删除评论
    @PostMapping("delete/{commentId}")
    public R deleteComment(@PathVariable String commentId, HttpServletRequest request) {
        //判断登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        commentService.loginCheck(memberId);
        commentService.removeById(commentId);
        return R.ok().message("删除评论成功");
    }
}
