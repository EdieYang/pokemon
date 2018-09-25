package com.pokepet.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.model.Commodity;

public interface ICommodityService {

	/**
	 * 商品列表（分页）
	 * @param param
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public JSONObject getCommodityList(Map<String, Object> param, int pageNum, int pageSize);
	
	/**
	 * 获取商品
	 * @param commodityId
	 * @return
	 */
	public Commodity getCommodity(String commodityId);

	/**
	 * 保存/更新商品信息
	 * @param commodity
	 * @return
	 */
	boolean saveCommodity(Commodity commodity);

}
