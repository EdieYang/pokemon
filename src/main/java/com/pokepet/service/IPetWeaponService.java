package com.pokepet.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.model.PetWeaponConcat;

public interface IPetWeaponService {
	
	/**
	 * 根据petID获取装备栏装备信息
	 * @param petId
	 * @return
	 */
	List<Map<String, Object>> getWeaponByPetId(String petId);
	
	/**
	 * 设置宠物装备
	 * @param concat
	 * @return
	 */
	public boolean setPetWeaponConcat(PetWeaponConcat concat);
	
	
	/**
	 * 探索目标点，扣除装备耐久、活力度，生成探索奖励
	 * @param petId
	 * @param pointStar
	 * @return
	 */
	public JSONObject expolrePoint(String petId, int pointStar);
	

}
