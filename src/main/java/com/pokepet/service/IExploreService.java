package com.pokepet.service;

import com.alibaba.fastjson.JSONArray;
import com.pokepet.model.GPSLocation;

public interface IExploreService {
	
	/**
	 * 获取探索点信息
	 * @param location
	 * @param distance
	 * @return
	 */
	public JSONArray getExplorePoint(GPSLocation location, double distance, int pointStar, int pointCount);

}
