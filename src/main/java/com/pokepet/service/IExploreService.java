package com.pokepet.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.model.GPSLocation;
import com.pokepet.model.UserExploreHistory;

public interface IExploreService {

	/**
	 * 获取探索点信息
	 * 
	 * @param location
	 * @param distance
	 * @return
	 */
	public JSONArray getExplorePoint(GPSLocation location, double distance, int pointStar, int pointCount);

	/**
	 * 探索目标点，生成奖励，记录探索历史
	 * @param userId
	 * @param petId
	 * @param pointName
	 * @param pointStar
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	JSONObject expolrePoint(String userId, String petId, String pointName, int pointStar, double longitude, double latitude);
	
	List<UserExploreHistory> getExploreHistory(String userId);
	
	int getExploreCountForUserToday(String userId);

}
