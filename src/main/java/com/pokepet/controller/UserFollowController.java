package com.pokepet.controller;

import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.UserFollow;
import com.pokepet.service.IUserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
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
        String followState=request.getParameter("followState");
        Map<String,String> map=new HashMap<>();
        map.put("userId",userId);
        map.put("followUserId",followUserId);
        map.put("followState",followState);
        return userFollowService.crdFollowRelationWithUser(map);
    }


    @RequestMapping(value = "/{userId}/followUsers",method =  RequestMethod.GET)
    public List<Map<String,String>> followUserList(@PathVariable("userId") String userId, HttpServletRequest request){
        int pageNum=request.getParameter("pageNum").equals(null)?0:Integer.parseInt(request.getParameter("pageNum"));
        int pageSize=request.getParameter("pageSize").equals(null)?0:Integer.parseInt(request.getParameter("pageSize"));

        return userFollowService.getUserFollowList(userId,pageNum,pageSize);
    }

    @RequestMapping(value = "/{userId}/followedUsers",method =  RequestMethod.GET)
    public List<Map<String,String>> followedUserList(@PathVariable("userId") String userId, HttpServletRequest request){
        int pageNum=request.getParameter("pageNum").equals(null)?0:Integer.parseInt(request.getParameter("pageNum"));
        int pageSize=request.getParameter("pageSize").equals(null)?0:Integer.parseInt(request.getParameter("pageSize"));

        return userFollowService.getUserFollowedList(userId,pageNum,pageSize);
    }


    @RequestMapping(value = "/{userId}/followOrNot",method =  RequestMethod.GET)
    public boolean followOrNot(@PathVariable("userId") String userId, HttpServletRequest request){
        String followUserId=request.getParameter("followUserId");
        UserFollow userFollow=new UserFollow();
        userFollow.setFollowUserId(followUserId);
        userFollow.setUserId(userId);
        UserFollow userFollowRes=userFollowService.selectFollowedUser(userFollow);
        if(userFollowRes==null){
            return false;
        }
        return true;
    }

}
