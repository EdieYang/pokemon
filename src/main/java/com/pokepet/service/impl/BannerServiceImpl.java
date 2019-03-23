package com.pokepet.service.impl;

import com.pokepet.dao.CmsBannerMapper;
import com.pokepet.model.CmsBanner;
import com.pokepet.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SteveYang
 * @date 2019/3/12
 */

@Service
public class BannerServiceImpl implements IBannerService {


    @Autowired
    private CmsBannerMapper cmsBannerMapper;



    @Override
    public List<CmsBanner> getEffectiveBannerList() {
        return cmsBannerMapper.selectEffectiveBannerList();
    }

    @Override
    public int createBanner(CmsBanner cmsBanner) {
        return cmsBannerMapper.insertSelective(cmsBanner);
    }
}
