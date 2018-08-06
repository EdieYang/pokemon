package com.pokepet.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.GPSLocation;
import com.pokepet.util.LocationUtils;

@ResponseResult
@RestController
@RequestMapping("/explore")
public class ExploreController {
	
	
	@RequestMapping(value = "/getExplorePoint",method = RequestMethod.POST,consumes="application/json")
	public JSONArray getExplorePoint(@RequestBody GPSLocation location){
		JSONArray arr = new JSONArray();
		arr.add(LocationUtils.GetRandomLocation(location, 300));
		arr.add(LocationUtils.GetRandomLocation(location, 300));
		arr.add(LocationUtils.GetRandomLocation(location, 300));
        return arr;
	}

}
