package com.pokepet.service;

import com.pokepet.model.OrderPay;

import java.util.List;
import java.util.Map;

/**
 * 兑换订单接口(商品兑换统一下单流程)
 */
public interface IOrderService {

    List<OrderPay> getOrderListByUserId(String userId);

    List<OrderPay> getOrderListByParameter(Map<String,Object> param);

    void createOrderPay(OrderPay orderPay);

    void updateOrderPay(OrderPay orderPay);


}
