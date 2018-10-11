package com.pokepet.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pokepet.dao.OrderMallMapper;
import com.pokepet.model.OrderMall;
import com.pokepet.service.IOrderService;

/**
 * Created by Fade on 2018/8/22.
 */

@Service
public class OrderServiceImpl implements IOrderService {


    @Autowired
    private OrderMallMapper orderMallMapper;

    @Override
    public List<OrderMall> getOrderListByUserId(String userId) {
        return null;
    }

    @Override
    public List<OrderMall> getOrderListByParameter(Map<String, Object> param) {
        return null;
    }

    @Override
    public void createOrder(OrderMall orderPay) {
    	orderMallMapper.insertSelective(orderPay);
    }

    @Override
    public void updateOrder(OrderMall orderPay) {

    }

	@Override
	public JSONObject getOrderList(Map<String, Object> param, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = orderMallMapper.selectCommodityList(param);
		PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
		result.put("page", page.getPageNum());
		result.put("records", page.getTotal());
		result.put("rows", list);
		return result;
	}

	@Override
	public OrderMall getOrder(String orderId) {
		return orderMallMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public JSONObject checkBuyStatusByUserId(String userId, String commodityId, int dayRange) {
		JSONObject result = new JSONObject();
		//获取最近一条有效的未完成订单
		OrderMall unfilledOrder = orderMallMapper.selectLastUnfilledOrderByUserId(userId);
		if(null != unfilledOrder){
			result.put("FLAG", false);
			result.put("msg", "您有未完成订单，请完成后继续兑换。");
			return result;
		}
		
		//获取一周内该用户对该商品的有效订单
		OrderMall commodityOrder = orderMallMapper.selectLastCommodityOrderByUserId(userId, commodityId, dayRange);
		if(null != commodityOrder){
			result.put("FLAG", false);
			result.put("msg", "该物品"+dayRange+"天只能兑换一次哦，请过段时间后继续兑换。");
			return result;
		}
		
		result.put("FLAG", true);
		return result;
	}
}
