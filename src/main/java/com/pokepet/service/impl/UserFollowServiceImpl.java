package com.pokepet.service.impl;

import com.pokepet.dao.UserFollowMapper;
import com.pokepet.model.UserFollow;
import com.pokepet.service.IUserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Fade on 2018/8/9.
 */

@Service
public class UserFollowServiceImpl implements IUserFollowService {

    @Autowired
    UserFollowMapper userFollowMapper;


    @Override
    public int getUserFollowAmount(String userId) {

        return 0;
    }

    @Override
    public int getUserFollowedAmount(String userId) {
        return 0;
    }

    @Override
    public boolean addFollowRelation(Map<String, String> map) {
        String userId=map.get("userId");
        String followUserId=map.get("followUserId");
        UserFollow userFollow=new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setFollowUserId(followUserId);
        UserFollow userFollowResult= userFollowMapper.selectFollowedUser(userFollow);
        if(userFollowResult!=null){
            userFollowResult.setDelFlag("0");
            int row=userFollowMapper.updateByPrimaryKeySelective(userFollowResult);
            if(row!=0){
                return true;
            }else {
                return false;
            }
        }

        UserFollow userFollowParam=new UserFollow();
        userFollowParam.setId(UUID.randomUUID().toString());
        userFollowParam.setUserId(map.get("userId"));
        userFollowParam.setFollowUserId(map.get("followUserId"));
        userFollowParam.setDelFlag("0");
        userFollowParam.setCreateTime(new Date());

        int row=userFollowMapper.insertSelective(userFollowParam);
        if(row!=0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean deleteUserFollow(Map<String, String> map) {
        UserFollow userFollow=new UserFollow();
        userFollow.setUserId(map.get("userId"));
        userFollow.setFollowUserId(map.get("followUserId"));
        UserFollow userFollowResult= userFollowMapper.selectFollowedUser(userFollow);

        userFollowResult.setDelFlag("1");

        int row=userFollowMapper.updateByPrimaryKeySelective(userFollow);
        if(row!=0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public UserFollow selectFollowedUser(UserFollow userFollow) {
        return userFollowMapper.selectFollowedUser(userFollow);
    }


}
