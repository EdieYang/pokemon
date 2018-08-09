package com.pokepet.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.model.PetSupply;

public interface IPetSupplyService {

	/**
	 * 根据userId获取补给信息
	 * 
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getSupplyByUserId(String userId);

	/**
	 * 获取补给列表
	 * 
	 * @return
	 */
	List<PetSupply> getSupplyList();

	/**
	 * 使用补给
	 * @param petId
	 * @param id
	 * @return
	 */
	JSONObject useSupply(String petId, String id);
}
