package com.pokepet.controller;

import com.pokepet.annotation.ResponseResult;
import com.pokepet.service.IUserFollowService;
import com.pokepet.service.IUserService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fade on 2018/8/9.
 */
@ResponseResult
@RestController
@RequestMapping("/userFollow")
public class UserFollowController {

    @Autowired
    IUserFollowService userFollowService;

    @RequestMapping(value = "/pet/{petId}",method = RequestMethod.POST)
    public boolean followPet(@PathVariable("petId") String petId, HttpServletRequest request){
        String userId=request.getParameter("userId");
        Map<String,String> map=new HashMap<>();
        map.put("userId",userId);
        map.put("petId",petId);
        return userFollowService.crdFollowRelationWithPet(map);
    }

    @RequestMapping(value = "/user/{userId}",method =  RequestMethod.POST)
    public boolean followUser(@PathVariable("userId") String userId, HttpServletRequest request){
        String followUserId=request.getParameter("followUserId");
        Map<String,String> map=new HashMap<>();
        map.put("userId",userId);
        map.put("followUserId",followUserId);
        return userFollowService.crdFollowRelationWithUser(map);
    }
}
