package com.pokepet.controller;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.dao.ActivityMapper;
import com.pokepet.model.ActivityStatistic;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by Fade on 2018/6/28.
 */



@RestController
@RequestMapping("/statistics")
public class StatisticsController {


    private static  final String ACID="thdPetSupport20180630";

    @Autowired
    private ActivityMapper activityMapper;

    @RequestMapping("/countSend")
    public String countSend(){
        int count=activityMapper.selectSendCount();
        return String.valueOf(count);
    }


    @RequestMapping("/countSupport")
    public String countSupport(){
        int count=activityMapper.selectSupportCount();
        return String.valueOf(count);
    }


    @RequestMapping("/countUserSupport")
    public String countUserSupport(HttpServletRequest request){
        String userId=request.getParameter("userId");
        int count=activityMapper.countUserSupport(userId);
        return String.valueOf(count);
    }


    @RequestMapping("/insertSupport")
    public JSONObject insertSupport(HttpServletRequest request){
        JSONObject object=new JSONObject();
        String userId=request.getParameter("userId");
        String type=request.getParameter("type");

        if(StringUtils.isEmpty(userId)){
            object.put("result",false);
            return object;
        }
        ActivityStatistic activityStatistic=new ActivityStatistic();
        activityStatistic.setId(UUID.randomUUID().toString());
        activityStatistic.setUserId(userId);
        activityStatistic.setActivityId(ACID);
        activityStatistic.setType(type);
        activityMapper.insertSelective(activityStatistic);
        object.put("result",true);
        return object;
    }


}
