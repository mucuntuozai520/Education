package com.linfeng.educenter.controller;


import com.linfeng.educenter.entity.vo.RegisterVo;
import com.linfeng.educenter.service.UcenterMemberService;
import com.linfeng.commonutils.JwtUtils;
import com.linfeng.commonutils.R;
import com.linfeng.commonutils.ordervo.UcenterMemberOrder;
import com.linfeng.educenter.entity.UcenterMember;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-09-26
 */
@RestController
@RequestMapping("/educenter/member")

public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member) {
        String token = memberService.login(member);
        return R.ok().data("token",token);
    }
    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok().message("注册成功");
    }
    //根据token获取用户信息
    @GetMapping("getUserInfoByToken")
    public R getUserInfoByToken(HttpServletRequest request){
        //从token获取MemberId，
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //通过id获取用户信息
        UcenterMember userInfo = memberService.getById(memberId);
        //返回用户信息
        return R.ok().data("userInfo",userInfo);
    }
    //根据token字符串获取用户信息
    @PostMapping("getInfo/{id}")
    public UcenterMember getInfo(@PathVariable String id) {
        //根据用户id获取用户信息
        UcenterMember ucenterMember = memberService.getById(id);
        UcenterMember memeber = new UcenterMember();
        BeanUtils.copyProperties(ucenterMember,memeber);
        return memeber;
    }
    //根据用户id获取用户信息
    @PostMapping("getOrderUserInfo/{memberId}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String memberId) {
        //获取用户信息
        UcenterMember userInfo = memberService.getById(memberId);
        UcenterMemberOrder orderUserInfo = new UcenterMemberOrder();
        BeanUtils.copyProperties(userInfo,orderUserInfo);
        return orderUserInfo;
    }
    //查询某一天注册人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
         Integer count = memberService.countRegisterDay(day);
         return R.ok().data("countRegister",count);
    }


}

