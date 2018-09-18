package com.pokepet.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokepet.dao.CommodityMapper;
import com.pokepet.model.Commodity;
import com.pokepet.service.ICommodityService;

@Service
public class CommodityServiceImpl implements ICommodityService{
	
	@Autowired
	CommodityMapper commodityMapper;

	@Override
	public List<Commodity> getCommodityList(String search, List<String> typeList, List<String> brandList, int pageNo,
			int pageSize) {
		
		return null;
	}

}
