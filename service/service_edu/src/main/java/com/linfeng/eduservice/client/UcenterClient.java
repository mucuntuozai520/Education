package com.linfeng.eduservice.client;

import com.linfeng.educenter.entity.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Yu1
 * @date 2022/10/10 - 20:44
 */
@FeignClient(name = "service-ucenter") //调用的服务名称
@Component
public interface UcenterClient {

    //根据用户id获取用户信息
    @PostMapping("/educenter/member/getInfo/{id}")
    public UcenterMember getInfo(@PathVariable("id") String id);

}
