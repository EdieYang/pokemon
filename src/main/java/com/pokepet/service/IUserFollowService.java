package com.pokepet.service;

import com.pokepet.model.UserFollow;

import java.util.Map;

/**
 * Created by Fade on 2018/8/9.
 */

public interface IUserFollowService {

    /**
     * 获取用户关注数量
     */
    int getUserFollowAmount(String userId);


    /**
     * 获取用户粉丝数量
     * @param userId
     * @return
     */
    int getUserFollowedAmount(String userId);


    /**
     * 增加用户粉丝
     * @param map
     * @return
     */
    boolean addFollowRelation(Map<String,String> map);


    /**
     * 解绑用户粉丝
     * @param map
     * @return
     */
    boolean deleteUserFollow(Map<String,String> map);


    UserFollow selectFollowedUser(UserFollow userFollow);
}
