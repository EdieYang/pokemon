package com.pokepet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.GPSLocation;
import com.pokepet.model.UserExploreHistory;
import com.pokepet.service.IExploreService;
import com.pokepet.service.IPetWeaponService;

@ResponseResult
@RestController
@RequestMapping("/explore")
public class ExploreController {

	@Autowired
	IPetWeaponService petWeaponService;

	@Autowired
	IExploreService exploreService;

	@RequestMapping(value = "/getExplorePoint", method = RequestMethod.POST, consumes = "application/json")
	public JSONArray getExplorePoint(@RequestBody JSONObject data) {
		GPSLocation location = new GPSLocation();

		location.setLatitude(data.getDoubleValue("latitude"));
		location.setLongitude(data.getDoubleValue("longitude"));
		double distance = data.getDoubleValue("distance");
		int pointStar = data.getIntValue("pointStar");
		int pointCount = data.getIntValue("pointCount");

		return exploreService.getExplorePoint(location, distance, pointStar, pointCount);
	}

	@RequestMapping(value = "/explorePoint", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject explorePoint(@RequestBody JSONObject data) {
		String userId = data.getString("userId");
		String petId = data.getString("petId");
		String pointName = data.getString("pointName");
		int pointStar = data.getIntValue("pointStar");
		double latitude = data.getDoubleValue("latitude");
		double longitude = data.getDoubleValue("longitude");
		
		return exploreService.expolrePoint(userId, petId, pointName, pointStar, longitude, latitude);
	}
	
	@RequestMapping(value = "/{userId}/exploreHistory", method = RequestMethod.GET)
	public List<UserExploreHistory> getExploreHistory(@PathVariable String userId) {
		return exploreService.getExploreHistory(userId);
	}
	
	@RequestMapping(value = "/{userId}/exploreCountToday", method = RequestMethod.GET)
	public int getExploreCountToday(@PathVariable String userId) {
		return exploreService.getExploreCountForUserToday(userId);
	}

}
