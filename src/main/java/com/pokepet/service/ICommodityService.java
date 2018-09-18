package com.pokepet.service;

import java.util.List;

import com.pokepet.model.Commodity;

public interface ICommodityService {
	
	public List<Commodity> getCommodityList(String search, List<String> typeList, List<String> brandList,
			int pageNum, int pageSize);

}
