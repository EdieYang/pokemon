package com.pokepet.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pokepet.dao.ActActivityMapper;
import com.pokepet.model.ActActivity;
import com.pokepet.model.ActActivityRegister;
import com.pokepet.service.IActivityService;

@Service
public class ActivityServiceImpl implements IActivityService{

	@Autowired
	ActActivityMapper actActivityMapper;
	
	@Override
	public JSONObject getActivityList(int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = actActivityMapper.getActivityList();
		PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
		result.put("page", page.getPageNum());
		result.put("records", page.getTotal());
		result.put("rows", list);
		return result;
	}

	@Override
	public ActActivity getActivity(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveActivity(ActActivity act) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JSONObject getActivityRegisterList(String search, String activityId, int pageNum,
			int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getActivityRegister(String registerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveActivityRegister(ActActivityRegister register) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean voteRegister(String voterId, String registerId) {
		// TODO Auto-generated method stub
		return false;
	}

}
