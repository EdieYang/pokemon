package com.pokepet.service;

import java.util.List;
import java.util.Map;

public interface IPetWeaponService {
	
	/**
	 * 根据petID获取装备栏装备信息
	 * @param petId
	 * @return
	 */
	List<Map<String, Object>> getWeaponByPetId(String petId);

}
