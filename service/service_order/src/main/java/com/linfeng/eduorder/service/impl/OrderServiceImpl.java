package com.linfeng.eduorder.service.impl;

import com.linfeng.commonutils.ordervo.CourseWebVoOrder;
import com.linfeng.commonutils.ordervo.UcenterMemberOrder;
import com.linfeng.eduorder.client.EduClient;
import com.linfeng.eduorder.client.UcenterClient;
import com.linfeng.eduorder.entity.Order;
import com.linfeng.eduorder.mapper.OrderMapper;
import com.linfeng.eduorder.service.OrderService;
import com.linfeng.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-10-11
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    //生成订单
    @Override
    public String createOrder(String courseId, String memberId) {
        //远程调用根据用户id获取用户信息
        UcenterMemberOrder orderUserInfo = ucenterClient.getOrderUserInfo(memberId);
        //远程调用根据课程id获取课程信息
        CourseWebVoOrder orderCourseInfo = eduClient.getOrderCourseInfo(courseId);
        //向order对象 设置属性
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId); //课程id
        order.setCourseTitle(orderCourseInfo.getTitle());
        order.setCourseCover(orderCourseInfo.getCover());
        order.setTeacherName(orderCourseInfo.getTeacherName());
        order.setTotalFee(orderCourseInfo.getPrice());
        order.setMemberId(memberId);
        order.setMobile(orderUserInfo.getMobile());
        order.setNickname(orderUserInfo.getNickname());
        order.setStatus(0);  //订单状态（0：未支付 1：已支付）
        order.setPayType(1);  //支付类型 ，微信1
        baseMapper.insert(order);
        //返回订单号
        return order.getOrderNo();
    }

}
