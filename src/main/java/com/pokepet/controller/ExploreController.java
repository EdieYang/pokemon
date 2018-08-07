package com.pokepet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.GPSLocation;
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

	@RequestMapping(value = "/explorePoint", method = RequestMethod.GET)
	public JSONObject explorePoint(@RequestParam("petId") String petId, @RequestParam("pointStar") int pointStar) {
		return petWeaponService.expolrePoint(petId, pointStar);
	}

}
