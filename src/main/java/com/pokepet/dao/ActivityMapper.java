package com.pokepet.dao;

import com.pokepet.model.ActivityStatistic;

/**
 * Created by Fade on 2018/6/28.
 */
public interface ActivityMapper {

    int selectSendCount();

    int selectSupportCount();

    int countUserSupport(String userId);

    void insertSelective(ActivityStatistic activityStatistic);


}
