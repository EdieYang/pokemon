package com.pokepet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.PetSupply;
import com.pokepet.model.PetWeapon;
import com.pokepet.service.IPetSupplyService;
import com.pokepet.service.IPetWeaponService;

@ResponseResult
@RestController
@RequestMapping("/mall")
public class MallController {
	
	@Autowired
	IPetWeaponService petWeaponService;
	
	@Autowired
	IPetSupplyService petSupplyService;
	
	@RequestMapping(value = "/weaponList", method = RequestMethod.GET)
	public List<PetWeapon> getWeaponList(){
		return petWeaponService.getWeaponList();
	}
	
	@RequestMapping(value = "/supplyList", method = RequestMethod.GET)
	public List<PetSupply> getSupplyList(){
		return petSupplyService.getSupplyList();
	}
	
	
	@RequestMapping(value = "/buyWeapon", method = RequestMethod.POST,consumes="application/json")
	public JSONObject buyWeapon(@RequestBody JSONObject data){
		String userId = data.getString("userId");
		String weaponId = data.getString("weaponId");
		String payWay = data.getString("payWay");
		return petWeaponService.buyWeapon(userId, weaponId, payWay, data);
	}
	
	@RequestMapping(value = "/buySupply", method = RequestMethod.POST,consumes="application/json")
	public JSONObject buySupply(@RequestBody JSONObject data){
		String userId = data.getString("userId");
		String supplyId = data.getString("supplyId");
		String payWay = data.getString("payWay");
		return petWeaponService.buySupply(userId, supplyId, payWay, data);
	}
	

}
