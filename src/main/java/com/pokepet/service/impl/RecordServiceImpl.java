package com.pokepet.service.impl;

import com.github.pagehelper.PageHelper;
import com.pokepet.dao.*;
import com.pokepet.model.RecordCollect;
import com.pokepet.model.RecordLike;
import com.pokepet.model.UserLongRecord;
import com.pokepet.model.UserRecord;
import com.pokepet.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Fade on 2018/9/19.
 */

@Service
public class RecordServiceImpl implements IRecordService {

    @Autowired
    private UserLongRecordMapper userLongRecordMapper;

    @Autowired
    private UserRecordMapper userRecordMapper;

    @Autowired
    private UserRecordHandlerMapper userRecordHandlerMapper;

    @Autowired
    private RecordLikeMapper recordLikeMapper;


    @Autowired
    private RecordCollectMapper recordCollectMapper;


    @Override
    public int updateLongRecord(UserLongRecord longRecord) {
        return userLongRecordMapper.updateByPrimaryKeySelective(longRecord);

    }

    @Override
    public int insertLongRecord(UserLongRecord longRecord) {
        return userLongRecordMapper.insertSelective(longRecord);
    }

    @Override
    public int updateRecord(UserRecord userRecord) {
        return userRecordMapper.updateByPrimaryKeySelective(userRecord);
    }

    @Override
    public int insertRecord(UserRecord userRecord) {
        return userRecordMapper.insertSelective(userRecord);
    }


    @Override
    public List<Map<String, Object>> selectUserRecordList(String userId,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.selectUserRecordList(userId);
    }

    @Override
    public List<Map<String, Object>> selectRecommendList(int pageNum, int pageSize,String userId) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.selectRecommendList(userId);
    }

    @Override
    public List<Map<String, Object>> selectCharityList(int pageNum, int pageSize,String userId) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.selectCharityList(userId);
    }

    @Override
    public Map<String, Object> selectLongRecordByRecordId(String recordId,String userId) {
        return userRecordHandlerMapper.selectLongRecordByRecordId(recordId,userId);
    }

    @Override
    public Map<String, Object> selectRecordByRecordId(String recordId) {
        return userRecordHandlerMapper.selectRecordByRecordId(recordId);
    }

    @Override
    public boolean updateRecordLike(String userId, String recordId) {
        RecordLike recordLike=recordLikeMapper.selectByUserIdAndRecordId(userId,recordId);
        if(recordLike!=null){
            String delFlag=recordLike.getDelFlag();
            if(delFlag.equals("1")){
                recordLike.setDelFlag("0");
            }else{
                recordLike.setDelFlag("1");

            }
        }else{
            recordLike=new RecordLike();
            recordLike.setUserId(userId);
            recordLike.setRecordId(recordId);
            recordLike.setDelFlag("0");
            recordLike.setId(UUID.randomUUID().toString().replace("-",""));
            recordLike.setCreateTime(new Date());
            return recordLikeMapper.insertSelective(recordLike)>0;
        }
        return recordLikeMapper.updateByPrimaryKey(recordLike)>0;
    }

    @Override
    public boolean updateRecordCollect(String userId, String recordId) {
        RecordCollect recordCollect=recordCollectMapper.selectByUserIdAndRecordId(userId,recordId);
        if(recordCollect!=null){
            String delFlag=recordCollect.getDelFlag();
            if(delFlag.equals("1")){
                recordCollect.setDelFlag("0");
            }else{
                recordCollect.setDelFlag("1");

            }
        }else{
            recordCollect=new RecordCollect();
            recordCollect.setUserId(userId);
            recordCollect.setRecordId(recordId);
            recordCollect.setDelFlag("0");
            recordCollect.setId(UUID.randomUUID().toString().replace("-",""));
            recordCollect.setCreateTime(new Date());
            return recordCollectMapper.insertSelective(recordCollect)>0;
        }
        return recordCollectMapper.updateByPrimaryKey(recordCollect)>0;
    }
}
