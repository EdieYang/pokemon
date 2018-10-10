package com.pokepet.controller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.OrderPay;
import com.pokepet.service.IOrderService;

@ResponseResult
@RestController
@RequestMapping("/order")
public class OrderController {
	
	/**
	 * 同一商品同一用户购买天数间隔
	 */
	private static final int DAY_RANGE_FOR_COMMODITY_TO_BUY = 7;
	
	/**
	 * 待支付订单有效期（分钟）
	 */
	private static final int EFFECTIVE_MINUTE_FOR_ORDER_TO_PAY = 30;
	
	@Autowired
	IOrderService orderService;
	
	
	@RequestMapping(value = "/crtOrder", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject crtOrder(@RequestBody JSONObject data) {
		OrderPay order = JSONObject.toJavaObject(data, OrderPay.class);
		//判断此人是否能建订单
		JSONObject check = orderService.checkBuyStatusByUserId(order.getUserId(), order.getCommodityId(), DAY_RANGE_FOR_COMMODITY_TO_BUY);
		if(check.getBooleanValue("FLAG")){
			Calendar cal = Calendar.getInstance();
			order.setCreateTime(cal.getTime());//订单创建时间
			cal.add(Calendar.MINUTE, EFFECTIVE_MINUTE_FOR_ORDER_TO_PAY);
			order.setEffectiveTime(cal.getTime());// 订单有效期
			orderService.createOrderPay(order);
		}
		return check;
	}

}
