package com.pokepet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	

}
