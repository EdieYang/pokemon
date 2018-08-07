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
import com.pokepet.service.IPetWeaponService;
import com.pokepet.util.LocationUtils;

@ResponseResult
@RestController
@RequestMapping("/explore")
public class ExploreController {
	
	@Autowired
	IPetWeaponService petWeaponService;
	
	
	@RequestMapping(value = "/getExplorePoint",method = RequestMethod.POST,consumes="application/json")
	public JSONArray getExplorePoint(@RequestBody GPSLocation location){
		JSONArray arr = new JSONArray();
		arr.add(LocationUtils.GetRandomLocation(location, 300));
		arr.add(LocationUtils.GetRandomLocation(location, 300));
		arr.add(LocationUtils.GetRandomLocation(location, 300));
       return arr;
	}
	
	@RequestMapping(value = "/explorePoint",method = RequestMethod.GET)
	public JSONObject explorePoint(@RequestParam("petId") String petId ,@RequestParam("pointStar") int pointStar){
		return petWeaponService.expolrePoint(petId, pointStar);
	}

}
