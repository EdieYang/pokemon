package com.pokepet.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.pokepet.dao.*;
import com.pokepet.model.*;
import com.github.pagehelper.PageInfo;
import com.pokepet.dao.UserLongRecordMapper;
import com.pokepet.dao.UserRecordHandlerMapper;
import com.pokepet.dao.UserRecordMapper;
import com.pokepet.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RecordShareMapper recordShareMapper;

    @Autowired
    private RecordVisitMapper recordVisitMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


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
    public Map<String, Object> selectRecordByRecordId(String recordId,String userId) {
        return userRecordHandlerMapper.selectRecordByRecordId(recordId,userId);
    }

    @Override
    public boolean updateRecordLike(String userId, String recordId,String recordType) {
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
            recordLike.setRecordType(recordType);
            recordLike.setId(UUID.randomUUID().toString().replace("-",""));
            recordLike.setCreateTime(new Date());
            return recordLikeMapper.insertSelective(recordLike)>0;
        }
        return recordLikeMapper.updateByPrimaryKeySelective(recordLike)>0;
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
    public boolean insertRecordShare(String userId, String recordId,String recordType) {
        RecordShare recordShare=new RecordShare();
        recordShare.setUserId(userId);
        recordShare.setRecordId(recordId);
        recordShare.setRecordType(recordType);
        recordShare.setCreateTime(new Date());
        recordShare.setDelFlag("0");
        recordShare.setId(UUID.randomUUID().toString().replace("-",""));
        return recordShareMapper.insertSelective(recordShare)>0;
    }

    @Override
    public boolean insertRecordVisit(String userId, String recordId, String recordType) {
        RecordVisit recordVisit=new RecordVisit();
        recordVisit.setRecordId(recordId);
        recordVisit.setUserId(userId);
        recordVisit.setRecordType(recordType);
        recordVisit.setCreateTime(new Date());
        recordVisit.setDelFlag("0");
        recordVisit.setId(UUID.randomUUID().toString().replace("-",""));
        return recordVisitMapper.insertSelective(recordVisit)>0;
    }


    @Override
    public List<Map<String, Object>> selectCharityList(int pageNum, int pageSize,String userId,String city,int dayLimit) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.selectCharityList(userId,city,dayLimit);
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

    @Override
    public boolean vanishRedisPoint(String recordId) {
        UserRecord userRecord=userRecordMapper.selectByPrimaryKey(recordId);
        String city=userRecord.getCity();
        ListOperations<String, String> oper =  redisTemplate.opsForList();
        List<String> emergencylist=oper.range("emergencyMap:"+city,0,-1);
        int index=-1;
        for(int i=0;i<emergencylist.size();i++){
            JSONObject itemO= JSON.parseObject(emergencylist.get(i));
            if(recordId.equals(itemO.getString("recordId"))){
                index=i;
                break;
            }
        }
        if (index==0){
            oper.leftPop("emergencyMap:"+city);
        } else if(index==emergencylist.size()-1){
            oper.rightPop("emergencyMap:"+city);
        } else{
            oper.trim("emergencyMap:"+city,0,index-1);
            oper.trim("emergencyMap:"+city,index+1,-1);
        }

        return true;
    }


    public List<Map<String,String>> getCollectEmergencyPointCityList(){
        return userRecordMapper.getCollectEmergencyPointCityList();
    }

    @Override
    public List<String> getCollectCityList() {
        return userRecordMapper.getCollectCityList();
    }


}
