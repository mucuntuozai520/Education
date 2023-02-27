package com.linfeng.eduorder.controller;


import com.linfeng.commonutils.JwtUtils;
import com.linfeng.commonutils.R;
import com.linfeng.eduorder.entity.Order;
import com.linfeng.eduorder.service.OrderService;
import com.linfeng.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-10-11
 */
@RestController
@RequestMapping("/eduorder/order")

public class OrderController {

    @Autowired
    private OrderService orderService;

    //生成订单
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request) {
        //查询是否登录，从token获取id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            throw new MyException(20001,"请登录");
        }else {
            String orderId = orderService.createOrder(courseId,memberId);
            return R.ok().data("orderId",orderId);
        }
    }
    //根据订单id查询订单信息
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderId);
        Order orderInfo = orderService.getOne(queryWrapper);
        return R.ok().data("orderInfo",orderInfo);
    }
    //根据课程id和用户id查询订单表中的订单状态
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memberId){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("status",1);
        int count = orderService.count(queryWrapper);
        if(count>0) {
            return true;
        } else {
            return false;
        }
    }



}