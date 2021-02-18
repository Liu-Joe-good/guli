package com.atguigu.educms.controller;


import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.atguigu.oss.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
 * @since 2021-02-01
 */
@CrossOrigin
@RestController
@RequestMapping("/educms/crm-banner")
@Api(description = "轮播图数据管理")
public class BannerFrontController {
    @Autowired
    private CrmBannerService crmBannerService;

    // 加redis中的，和后台功能有区别,获取轮播图
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> list= crmBannerService.getBanners();
        // 不需要分页，因为只要轮播图效果就可以了
        return R.ok().data("list",list);
    }
}

