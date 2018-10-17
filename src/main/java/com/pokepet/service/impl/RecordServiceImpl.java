package com.pokepet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.pokepet.dao.*;
import com.pokepet.model.RecordCollect;
import com.pokepet.model.RecordLike;
import com.github.pagehelper.PageInfo;
import com.pokepet.dao.UserLongRecordMapper;
import com.pokepet.dao.UserRecordHandlerMapper;
import com.pokepet.dao.UserRecordMapper;
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
    public int getUserCollectRecordAmount(String userId) {
        return recordCollectMapper.getUserCollectRecordAmount(userId);
    }


    @Override
    public List<Map<String, Object>> selectUserRecordList(String userId,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.selectUserRecordList(userId);
    }

    @Override
    public List<Map<String, Object>> selectUserCheckedRecordList(String userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.selectUserCheckedRecordList(userId);
    }

    @Override
    public List<Map<String, Object>> getCollectRecordList( int pageNum, int pageSize,String userId) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.selectUserCollectRecordList(userId);
    }

    @Override
    public List<Map<String, Object>> selectRecommendList(int pageNum, int pageSize,String userId) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.selectRecommendList(userId);
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



    @Override
    public List<Map<String, Object>> selectCharityList(int pageNum, int pageSize,String userId) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.selectCharityList(userId);
    }

    @Override
    public List<Map<String, Object>> getPetRecordList(int pageNum, int pageSize, String petId) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.getPetRecordList(petId);
    }


    @Override
	public JSONObject getRecordList(Map<String, Object> param, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		if(-1 != pageSize){
			//分页
			PageHelper.startPage(pageNum, pageSize);
			List<Map<String, Object>> list = userRecordMapper.selectRecordList(param);
			PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
			result.put("page", page.getPageNum());
			result.put("records", page.getTotal());
			result.put("rows", list);
		}else{
			List<Map<String, Object>> list = userRecordMapper.selectRecordList(param);
			result.put("rows", list);
		}
		return result;
	}

	@Override
	public JSONObject getLongRecordList(Map<String, Object> param, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		if(-1 != pageSize){
			PageHelper.startPage(pageNum, pageSize);
			List<Map<String, Object>> list = userLongRecordMapper.selectLongRecordList(param);
			PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
			result.put("page", page.getPageNum());
			result.put("records", page.getTotal());
			result.put("rows", list);
		}else{
			List<Map<String, Object>> list = userLongRecordMapper.selectLongRecordList(param);
			result.put("rows", list);
		}
		return result;
	}

	@Override
	public UserLongRecord getLongRecord(String recordId) {
		return userLongRecordMapper.selectByPrimaryKey(recordId);
	}

	@Override
	public boolean uptLongRecord(UserLongRecord record) {
		return userLongRecordMapper.updateByPrimaryKeySelective(record) > 0;
	}

	@Override
	public UserRecord getShortRecord(String recordId) {
		return userRecordMapper.selectByPrimaryKey(recordId);
	}

	@Override
	public boolean uptShortRecord(UserRecord record) {
		return userRecordMapper.updateByPrimaryKeySelective(record) > 0;
	}
}
