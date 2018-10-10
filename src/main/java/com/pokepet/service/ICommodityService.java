package com.pokepet.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.model.Commodity;

public interface ICommodityService {

	/**
	 * 商品列表（分页）--后台
	 * @param param
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public JSONObject getCommodityList(Map<String, Object> param, int pageNum, int pageSize);
	
	/**
	 * 获取商品--后台
	 * @param commodityId
	 * @return
	 */
	public Commodity getCommodity(String commodityId);

	/**
	 * 保存/更新商品信息--后台
	 * @param commodity
	 * @return
	 */
	boolean saveCommodity(Commodity commodity);
	
	/**
	 * 商品列表（分页）--商城
	 * @param param
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public JSONObject getCommodityListForMall(Map<String, Object> param, int pageNum, int pageSize);
	
	/**
	 * 获取商品信息--商城
	 * @param commodityId
	 * @return
	 */
	public Map<String, Object> getCommodityInfo(String commodityId);

}
