package com.atguigu.educms.controller;


import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.atguigu.oss.commonutils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-02-01
 */
//@CrossOrigin
@RestController
@RequestMapping("/educms/crm-bannerAdmin")
@Api(description = "后台轮播图数据管理")
public class BannerAdminController {

    @Autowired
    private CrmBannerService crmBannerService;

    // 查询轮播图
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page,@PathVariable long limit){
        Page<CrmBanner> objectPage = new Page<>(page,limit);
        crmBannerService.page(objectPage,null);
        return R.ok().data("items",objectPage.getRecords()).data("total",objectPage.getTotal());
    }

    // 新增
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        crmBannerService.save(crmBanner);
        return R.ok();
    }

    // 修改
    @PutMapping("updateBanner/{id}")
    public R updateBanner(@PathVariable String id){
        CrmBanner byId = crmBannerService.getById(id);
        crmBannerService.updateById(byId);
        return R.ok();
    }

    // 删除
    @DeleteMapping("deleteBanner/{id}")
    public R deleteBanner(@PathVariable String id){
        crmBannerService.removeById(id);
        return R.ok();
    }
}

