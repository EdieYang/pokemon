package com.pokepet.controller;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.ShareRecord;
import com.pokepet.model.User;
import com.pokepet.service.IShareBusinessService;
import com.pokepet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Fade on 2018/10/19.
 */

@ResponseResult
@RequestMapping("/share")
@RestController
public class ShareBusinessController {

    @Autowired
    IShareBusinessService shareBusinessService;

    @Autowired
    IUserService userService;

    @RequestMapping(value = "/{userId}",method = RequestMethod.POST, consumes = "application/json")
    public JSONObject getShareRecords(@PathVariable("userId")String userId, @RequestBody JSONObject data){
        JSONObject result=new JSONObject();
        String targetUserId=data.getString("targetUserId");
        Date effectiveTime=data.getDate("effectiveTime");
        List<Map<String,Object>> topRecords=shareBusinessService.getTopShareRecords(userId,effectiveTime);



        if(topRecords.size()>=3){
            result.put("data",topRecords.subList(0,3));
            result.put("status",200);
            result.put("reward",false);
            return result;
        }

        if(targetUserId.equals(userId)){
            result.put("data",topRecords);
            result.put("status",200);
            result.put("reward",false);
            return result;
        }

        ShareRecord hasRecord=shareBusinessService.getTargetShareRecord(userId,targetUserId,effectiveTime);

        if(hasRecord!=null){
            result.put("data",topRecords);
            result.put("status",200);
            result.put("reward",false);
            return result;
        }

        List<Map<String,Object>> records=shareBusinessService.getShareRecords(userId,targetUserId,effectiveTime);
        if(records==null || records!=null && records.size()<3){

            //占位
            ShareRecord shareRecord=new ShareRecord();
            shareRecord.setCreateTime(new Date());
            shareRecord.setRecordId(UUID.randomUUID().toString().replace("-",""));
            shareRecord.setUserId(userId);
            shareRecord.setDelFlag("0");
            shareRecord.setType("0");
            shareRecord.setTargetUserId(targetUserId);
            shareBusinessService.addShareRecord(shareRecord);
            List<Map<String,Object>> newTopRecords=shareBusinessService.getTopShareRecords(userId,effectiveTime);
            if(records.size()==2){
                //开启宝箱,计算金币
                //给发起者10个金币
                if(newTopRecords.size()>=3){
                    newTopRecords=newTopRecords.subList(0,3);
                    User source=userService.getUserInfo(userId);
                    source.setChipCount(source.getChipCount()+10);
                    userService.modifyUser(source);

                    for(Map<String,Object> map:newTopRecords){
                        String targetUserId_= String.valueOf(map.get("targetUserId"));
                        User obj=userService.getUserInfo(targetUserId_);
                        obj.setChipCount(obj.getChipCount()+3);
                        userService.modifyUser(obj);
                    }
                    result.put("reward",true);
                }
            }
            result.put("data",newTopRecords);
            result.put("status",200);
            result.put("reward",false);
            return result;

        }


        topRecords=topRecords.subList(0,3);
        result.put("data",topRecords);
        result.put("status",200);
        return result;

    }



}
