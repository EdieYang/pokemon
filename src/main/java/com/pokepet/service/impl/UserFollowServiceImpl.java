package com.pokepet.service.impl;

import com.github.pagehelper.PageHelper;
import com.pokepet.dao.PetFollowMapper;
import com.pokepet.dao.UserFollowMapper;
import com.pokepet.model.PetFollow;
import com.pokepet.model.UserFollow;
import com.pokepet.service.IUserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Fade on 2018/8/9.
 */

@Service
public class UserFollowServiceImpl implements IUserFollowService {

    @Autowired
    UserFollowMapper userFollowMapper;

    @Autowired
    PetFollowMapper petFollowMapper;




    @Override
    public int getUserFollowAmount(String userId) {
        return userFollowMapper.selectFollowAmount(userId);
    }

    @Override
    public int getUserFollowedAmount(String userId) {
        return userFollowMapper.selectFollowedAmount(userId);
    }

    @Override
    public List<Map<String, String>> getUserFollowList(String userId,int pageNumber,int pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        List<Map<String,String>> list=userFollowMapper.selectFollowers(userId);
        return list;
    }

    @Override
    public List<Map<String, String>> getUserFollowedList(String userId, int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        List<Map<String,String>> list=userFollowMapper.selectFans(userId);
        return list;
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

    @Override
    public boolean crdFollowRelationWithPet(Map<String, String> map) {
        String userId= map.get("userId");
        String petId=map.get("petId");

        PetFollow petFollow=petFollowMapper.selectByUserIdAndPetId(map);
        if(petFollow==null){
            petFollow=new PetFollow();
            petFollow.setFollowId(UUID.randomUUID().toString().replace("-",""));
            petFollow.setUserId(userId);
            petFollow.setPetId(petId);
            petFollow.setCreateTime(new Date());
            petFollow.setDelFlag("0");
            return petFollowMapper.insertSelective(petFollow)>0;
        }else{
            String flag=petFollow.getDelFlag();
            if(flag.equals("0")){
                petFollow.setDelFlag("1");
            }else{
                petFollow.setDelFlag("0");
            }
            return petFollowMapper.updateByPrimaryKey(petFollow)>0;

        }

    }

    @Override
    public boolean crdFollowRelationWithUser(Map<String, String> map) {
        String userId=map.get("userId");
        String followUserId=map.get("followUserId");
        String followState=map.get("followState");
        UserFollow userFollow=new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setFollowUserId(followUserId);
        UserFollow existUserFollow=userFollowMapper.selectExistFollowedUser(userFollow);
        if(existUserFollow==null){
            userFollow.setId(UUID.randomUUID().toString().replace("-",""));
            userFollow.setDelFlag("0");
            userFollow.setCreateTime(new Date());
            return userFollowMapper.insertSelective(userFollow)>0;
        }else{
            String delFlag=existUserFollow.getDelFlag();
            if(delFlag.equals(followState)){
              return true;
            }
            if(delFlag.equals("0")){
                existUserFollow.setDelFlag("1");
            }else{
                existUserFollow.setDelFlag("0");
            }
            return userFollowMapper.updateByPrimaryKeySelective(existUserFollow)>0;
        }
    }


}
