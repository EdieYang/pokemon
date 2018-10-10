package com.pokepet.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.model.OrderPay;

/**
 * 兑换订单接口(商品兑换统一下单流程)
 */
public interface IOrderService {

	List<OrderPay> getOrderListByUserId(String userId);

	List<OrderPay> getOrderListByParameter(Map<String, Object> param);

	void createOrderPay(OrderPay orderPay);

	void updateOrderPay(OrderPay orderPay);

	/**
	 * 获取订单列表（分页）
	 * 
	 * @param param
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	JSONObject getOrderList(Map<String, Object> param, int pageNum, int pageSize);

	OrderPay getOrder(String orderId);

	/**
	 * 校验用户是否满足购买条件(无待支付订单；每个物品每周每人只能兑换一次）
	 * @param userId
	 * @param commodityId
	 * @param dayRange
	 * @return
	 */
	JSONObject checkBuyStatusByUserId(String userId, String commodityId, int dayRange);

}
