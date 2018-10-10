package com.pokepet.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
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
	public JSONObject getCommodityList(Map<String, Object> param, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = commodityMapper.selectCommodityList(param);
		PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
		result.put("page", page.getPageNum());
		result.put("records", page.getTotal());
		result.put("rows", list);
		return result;
	}

	@Override
	public Commodity getCommodity(String commodityId) {
		return commodityMapper.selectByPrimaryKey(commodityId);
	}
	
	@Transactional
	@Override
	public boolean saveCommodity(Commodity commodity) {
		boolean FLAG = false;
		try {
			if (null == commodity.getCommodityId()) {
				// 新增
				commodityMapper.insertSelective(commodity);
			} else {
				// 修改
				commodityMapper.updateByPrimaryKeySelective(commodity);
			}
			FLAG = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return FLAG;
	}

	@Override
	public JSONObject getCommodityListForMall(Map<String, Object> param, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = commodityMapper.selectCommodityListForMall(param);
		PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
		result.put("page", page.getPageNum());
		result.put("records", page.getTotal());
		result.put("rows", list);
		return result;
	}

	@Override
	public Map<String, Object> getCommodityInfo(String commodityId) {
		return commodityMapper.selectCommodityInfoForMall(commodityId);
	}

}
