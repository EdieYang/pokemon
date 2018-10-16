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
import com.pokepet.dao.OrderMallMapper;
import com.pokepet.model.Commodity;
import com.pokepet.model.OrderMall;
import com.pokepet.model.User;
import com.pokepet.service.IOrderService;
import com.pokepet.service.IUserService;

/**
 * Created by Fade on 2018/8/22.
 */

@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private OrderMallMapper orderMallMapper;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private CommodityMapper commodityMapper;

	@Override
	public List<OrderMall> getOrderListByUserId(String userId) {
		return null;
	}

	@Override
	public JSONObject selectOrderListByUserId(String userId, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, String>> list = orderMallMapper.selectOrderList(userId);
		PageInfo<Map<String, String>> page = new PageInfo<>(list);
		result.put("page", page.getPageNum());
		result.put("records", page.getTotal());
		result.put("rows", list);
		return result;
	}

	@Override
	public JSONObject selectVirtualOrderListByUserId(String userId, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, String>> list = orderMallMapper.selectVirtualOrderList(userId);
		PageInfo<Map<String, String>> page = new PageInfo<>(list);
		result.put("page", page.getPageNum());
		result.put("records", page.getTotal());
		result.put("rows", list);
		return result;
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
	public boolean updateOrder(OrderMall orderPay) {
		return orderMallMapper.updateByPrimaryKeySelective(orderPay) > 0;
	}

	@Transactional
	@Override
	public boolean settleAccounts(OrderMall orderMall, String outTradeNo) {
		boolean FLAG = false;
		try {
			FLAG = updateOrder(orderMall);
			if (FLAG) {
				OrderMall orderOrigin = getOrder(outTradeNo);

				// 扣除金币(未判断金币数量是否足够扣除,前端已做过校验)
				if (orderOrigin.getBuyType().equals("1")) {
					String userId = orderOrigin.getUserId();
					User user = userService.getUserInfo(userId);
					int leftCoin = user.getChipCount() - orderOrigin.getCoin();
					user.setUserId(userId);
					user.setChipCount(leftCoin);
					userService.modifyUser(user);
				}
				
				//扣除库存
				Commodity commodity = commodityMapper.selectByPrimaryKey(orderOrigin.getCommodityId());
				commodity.setInventory(commodity.getInventory()-1);
				commodityMapper.updateByPrimaryKeySelective(commodity);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return FLAG;
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
	public Map<String, String> getOrderDetail(String orderId) {
		return orderMallMapper.getOrderDetail(orderId);
	}

	@Override
	public JSONObject checkBuyStatusByUserId(String userId, String commodityId, int dayRange) {
		JSONObject result = new JSONObject();
		// 获取最近一条有效的未完成订单
		OrderMall unfilledOrder = orderMallMapper.selectLastUnfilledOrderByUserId(userId);
		if (null != unfilledOrder) {
			result.put("FLAG", false);
			result.put("msg", "您有未完成订单，请完成后继续兑换。");
			return result;
		}

		// 获取一周内该用户对该商品的有效订单
		OrderMall commodityOrder = orderMallMapper.selectLastCommodityOrderByUserId(userId, commodityId, dayRange);
		if (null != commodityOrder) {
			result.put("FLAG", false);
			result.put("msg", "该物品" + dayRange + "天只能兑换一次哦，请过段时间后继续兑换。");
			return result;
		}

		result.put("FLAG", true);
		return result;
	}
}
