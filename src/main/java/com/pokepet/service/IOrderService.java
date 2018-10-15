package com.pokepet.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.model.OrderMall;

/**
 * 兑换订单接口(商品兑换统一下单流程)
 */
public interface IOrderService {

	List<OrderMall> getOrderListByUserId(String userId);

	JSONObject selectOrderListByUserId(String userId,int pageNum, int pageSize);

	JSONObject selectVirtualOrderListByUserId(String userId,int pageNum, int pageSize);

	List<OrderMall> getOrderListByParameter(Map<String, Object> param);

	void createOrder(OrderMall OrderMall);

	boolean updateOrder(OrderMall OrderMall);


	/**
	 * 用于微信支付回调结算订单
	 * @param orderMall
	 * @return
     */
	boolean settleAccounts(OrderMall orderMall,String outTradeNo);

	/**
	 * 获取订单列表（分页）
	 * 
	 * @param param
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	JSONObject getOrderList(Map<String, Object> param, int pageNum, int pageSize);

	OrderMall getOrder(String orderId);

	Map<String,String> getOrderDetail(String orderId);

	/**
	 * 校验用户是否满足购买条件(无待支付订单；每个物品每周每人只能兑换一次）
	 * @param userId
	 * @param commodityId
	 * @param dayRange
	 * @return
	 */
	JSONObject checkBuyStatusByUserId(String userId, String commodityId, int dayRange);

}
