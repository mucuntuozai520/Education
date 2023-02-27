package com.linfeng.eduorder.client;

import com.linfeng.commonutils.ordervo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Yu1
 * @date 2022/10/11 - 18:16
 */
@Component
@FeignClient(name = "service-ucenter")
public interface UcenterClient {

    //根据用户id获取用户信息
    @PostMapping("/educenter/member/getOrderUserInfo/{memberId}")
    public UcenterMemberOrder getOrderUserInfo(@PathVariable("memberId") String memberId);
}
