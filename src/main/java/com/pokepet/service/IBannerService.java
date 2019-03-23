package com.pokepet.service;

import com.pokepet.model.CmsBanner;

import java.util.List;

/**
 * @author SteveYang
 * @date 2019/3/12
 */
public interface IBannerService {

    List<CmsBanner> getEffectiveBannerList();

    int createBanner(CmsBanner cmsBanner);

}
