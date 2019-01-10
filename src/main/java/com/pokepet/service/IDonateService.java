package com.pokepet.service;

import java.util.List;
import java.util.Map;

import com.pokepet.model.UserDonate;

public interface IDonateService {
	
	UserDonate getLastDonate(Map<String, Object> param);
	
	List<Map<String, Object>> getDonateList(Map<String, Object> param);
	
	boolean insertRecord(UserDonate record);

	List<Map<String, Object>> getDonateStatistics(Map<String, Object> param);
}
