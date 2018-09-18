package com.pokepet.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pokepet.dao.CommodityMapper;
import com.pokepet.model.Commodity;
import com.pokepet.service.ICommodityService;

@Service
public class CommodityServiceImpl implements ICommodityService{
	
	@Autowired
	CommodityMapper commodityMapper;

	@Override
	public List<Commodity> getCommodityList(String search, List<String> typeList, List<String> brandList, int pageNum,
			int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Commodity> list = commodityMapper.getCommodityList(search, typeList, brandList);
		PageInfo<Commodity> page = new PageInfo<Commodity>(list);
		System.out.println(page.getTotal());
		System.out.println(page.getPageNum());
		return list;
	}

}
