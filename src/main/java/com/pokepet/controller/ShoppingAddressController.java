package com.pokepet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pokepet.model.UserShoppingAddress;
import com.pokepet.service.IShoppingAddressService;

@RestController
@RequestMapping("/shoppingAddresss")
public class ShoppingAddressController {
	
	@Autowired
	IShoppingAddressService shoppingAddressService;
	
	
	/**
	 * 获取用户收货地址列表
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/{userId}",method = RequestMethod.GET,consumes="application/json")
    public List<UserShoppingAddress> getAddressList(@PathVariable String userId){
        return shoppingAddressService.getAddressList(userId);
    }
	
	/**
	 * 保存收货地址
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/",method = RequestMethod.POST,consumes="application/json")
    public boolean addAddress(@RequestBody UserShoppingAddress address){
        return shoppingAddressService.saveAddress(address);
    }

}
