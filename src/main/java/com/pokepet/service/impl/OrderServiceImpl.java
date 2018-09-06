package com.pokepet.service.impl;

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
}
