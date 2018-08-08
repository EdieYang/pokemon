package com.pokepet.service;

import java.util.List;
import java.util.Map;

public interface IPetSupplyService {
	
	/**
	 * 根据userId获取补给信息
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getSupplyByUserId(String userId);

}
