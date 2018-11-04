package com.pokepet.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.model.ActActivity;
import com.pokepet.model.ActActivityRegister;

public interface IActivityService {
	
	/**
	 * 获取活动列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	JSONObject getActivityList(int pageNum, int pageSize);
	
	/**
	 * 获取活动详情
	 * @param id
	 * @return
	 */
	ActActivity getActivity(String id);
	
	/**
	 * 保存活动信息
	 * @param act
	 * @return
	 */
	boolean saveActivity(ActActivity act);
	
	/**
	 * 获取活动参与者列表
	 * @param search
	 * @param activityId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	JSONObject getActivityRegisterList(String search, String activityId, int pageNum, int pageSize);
	
	/**
	 * 获取参赛者信息
	 * @param registerId
	 * @return
	 */
	Map<String, Object>  getActivityRegister(String registerId);
	
	/**
	 * 保存参赛者信息
	 * @param register
	 * @return
	 */
	boolean saveActivityRegister(ActActivityRegister register);
	
	/**
	 * 投票
	 * @param voterId
	 * @param registerId
	 * @return
	 */
	boolean voteRegister(String voterId, String registerId);

}
