package com.pokepet.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pokepet.dao.ActActivityMapper;
import com.pokepet.dao.ActActivityRegisterMapper;
import com.pokepet.model.ActActivity;
import com.pokepet.model.ActActivityRegister;
import com.pokepet.service.IActivityService;
import com.pokepet.util.CommonUtil;

@Service
public class ActivityServiceImpl implements IActivityService {

	@Autowired
	ActActivityMapper actActivityMapper;

	@Autowired
	ActActivityRegisterMapper actActivityRegisterMapper;

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
		return actActivityMapper.selectByPrimaryKey(id);
	}

	@Override
	public boolean saveActivity(ActActivity act) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JSONObject getActivityRegisterList(String search, String activityId, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		param.put("activityId", activityId);
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = actActivityRegisterMapper.getRegisterListByParam(param);
		PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
		result.put("page", page.getPageNum());
		result.put("records", page.getTotal());
		result.put("rows", list);
		return result;
	}

	@Override
	public Map<String, Object> getActivityRegister(String registerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveActivityRegister(ActActivityRegister register) {
		if (null == register.getRegisterId()) {
			//新建
			int maxNo = actActivityRegisterMapper.selectMaxNoForActivity(register.getActivityId());
			register.setCreateTime(new Date());
			register.setRegisterId(CommonUtil.getUuid());
			register.setRegisterNo(maxNo + 1);
			return actActivityRegisterMapper.insertSelective(register) > 0;
		} else {
			//更新
			return actActivityRegisterMapper.updateByPrimaryKeySelective(register) > 0;
		}
	}

	@Override
	public boolean voteRegister(String voterId, String registerId) {
		// TODO Auto-generated method stub
		return false;
	}

}
