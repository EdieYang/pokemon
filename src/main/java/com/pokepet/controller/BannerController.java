package com.pokepet.controller;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.CmsBanner;
import com.pokepet.service.IBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SteveYang
 * @date 2019/3/12
 */


@Api(tags = "banner接口", description = "")
@ResponseResult
@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private IBannerService iBannerService;

    @ApiOperation(value = "获取banner列表")
    @GetMapping("")
    public List<CmsBanner> listEffectiveBanner() {
        List<CmsBanner> cmsBanners = iBannerService.getEffectiveBannerList();
        return cmsBanners;
    }


    @ApiOperation(value = "新增")
    @PostMapping("/")
    public boolean createBanner(@ApiParam("banner")
                                @RequestBody CmsBanner banner) {
        int effectRows = iBannerService.createBanner(banner);
        if (effectRows > 0) {
            return true;
        } else {
            return false;
        }

    }


}
