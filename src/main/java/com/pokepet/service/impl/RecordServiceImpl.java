package com.pokepet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pokepet.dao.UserLongRecordMapper;
import com.pokepet.dao.UserRecordHandlerMapper;
import com.pokepet.dao.UserRecordMapper;
import com.pokepet.model.UserLongRecord;
import com.pokepet.model.UserRecord;
import com.pokepet.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public List<Map<String, Object>> selectRecommendList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.selectRecommendList();
    }

    @Override
    public List<Map<String, Object>> selectCharityList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return userRecordHandlerMapper.selectCharityList();
    }

	@Override
	public JSONObject getRecordList(Map<String, Object> param, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = userRecordMapper.selectRecordList(param);
		PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
		result.put("page", page.getPageNum());
		result.put("records", page.getTotal());
		result.put("rows", list);
		return result;
	}

	@Override
	public JSONObject getLongRecordList(Map<String, Object> param, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = userLongRecordMapper.selectLongRecordList(param);
		PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
		result.put("page", page.getPageNum());
		result.put("records", page.getTotal());
		result.put("rows", list);
		return result;
	}
}
