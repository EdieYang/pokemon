package com.pokepet.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.model.PetWeapon;
import com.pokepet.model.PetWeaponConcat;

public interface IPetWeaponService {

	/**
	 * 根据petID获取装备栏装备信息
	 * 
	 * @param petId
	 * @return
	 */
	List<Map<String, Object>> getWeaponByPetId(String petId);

	/**
	 * 设置宠物装备
	 * 
	 * @param concat
	 * @return
	 */
	public boolean setPetWeaponConcat(PetWeaponConcat concat);

	/**
	 * 根据userId获取装备信息
	 * 
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getWeaponByUserId(String userId);

	/**
	 * 获取装备列表
	 * 
	 * @return
	 */
	List<PetWeapon> getWeaponList();

	/**
	 * 购买装备
	 * @param userId
	 * @param weaponId
	 * @param payWay
	 * @param payInfo
	 * @return
	 */
	JSONObject buyWeapon(String userId, String weaponId, String payWay, JSONObject payInfo);
	
	
	/**
	 * 购买补给
	 * @param userId
	 * @param supplyId
	 * @param payWay
	 * @param payInfo
	 * @return
	 */
	JSONObject buySupply(String userId, String supplyId, String payWay, JSONObject payInfo);

}
