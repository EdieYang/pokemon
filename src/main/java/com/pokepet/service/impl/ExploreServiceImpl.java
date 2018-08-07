package com.pokepet.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.dao.ExplorePointMapper;
import com.pokepet.model.ExplorePoint;
import com.pokepet.model.GPSLocation;
import com.pokepet.service.IExploreService;
import com.pokepet.util.LocationUtils;

@Service
public class ExploreServiceImpl implements IExploreService {

	@Autowired
	ExplorePointMapper explorePointMapper;

	@Override
	public JSONArray getExplorePoint(GPSLocation location, double distance, int pointStar, int pointCount) {
		JSONArray arr = new JSONArray();
		List<ExplorePoint> pointList = explorePointMapper.getPointListWithPointStar(pointStar);

		while (pointCount > 0) {
			if (pointList.size() > 0) {
				int aa = (int) (Math.random() * 100) % pointList.size();
				JSONObject point = (JSONObject) JSONObject.toJSON(pointList.get(aa));
				pointList.remove(aa);
				point.put("location", LocationUtils.GetRandomLocation(location, distance));
				arr.add(point);
			} else {
				pointCount = 0;
			}
		}

		return arr;
	}

}
