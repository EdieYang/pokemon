package com.pokepet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pokepet.dao.OrderPayMapper;
import com.pokepet.model.OrderPay;
import com.pokepet.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Fade on 2018/8/22.
 */

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderPayMapper orderPayMapper;



    @Override
    public List<OrderPay> getOrderListByUserId(String userId) {
        return null;
    }

    @Override
    public List<OrderPay> getOrderListByParameter(Map<String, Object> param) {
        return null;
    }

    @Override
    public void createOrderPay(OrderPay orderPay) {
        orderPayMapper.insertSelective(orderPay);
    }

    @Override
    public void updateOrderPay(OrderPay orderPay) {

    }

	@Override
	public JSONObject getOrderList(Map<String, Object> param, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = orderPayMapper.selectCommodityList(param);
		PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
		result.put("page", page.getPageNum());
		result.put("records", page.getTotal());
		result.put("rows", list);
		return result;
	}

	@Override
	public OrderPay getOrder(String orderId) {
		return orderPayMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public JSONObject checkBuyStatusByUserId(String userId, String commodityId, int dayRange) {
		JSONObject result = new JSONObject();
		//获取最近一条有效的未完成订单
		OrderPay unfilledOrder = orderPayMapper.selectLastUnfilledOrderByUserId(userId);
		if(null != unfilledOrder){
			result.put("FLAG", false);
			result.put("msg", "您有未完成订单，请完成后继续兑换。");
			return result;
		}
		
		//获取一周内该用户对该商品的有效订单
		OrderPay commodityOrder = orderPayMapper.selectLastCommodityOrderByUserId(userId, commodityId, dayRange);
		if(null != commodityOrder){
			result.put("FLAG", false);
			result.put("msg", "该物品"+dayRange+"天只能兑换一次哦，请过段时间后继续兑换。");
			return result;
		}
		
		result.put("FLAG", true);
		return result;
	}
}
