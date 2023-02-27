package com.linfeng.educms.controller;


import com.linfeng.commonutils.R;
import com.linfeng.educms.entity.CrmBanner;
import com.linfeng.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-09-23
 */
@RestController
@RequestMapping("/educms/frontBanner")

public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;

    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> list = bannerService.selectAllBanner();
        return R.ok().data("list",list);
    }

}

