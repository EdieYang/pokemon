package com.pokepet.service;

import java.util.List;

import com.pokepet.model.UserShoppingAddress;

public interface IShoppingAddressService {
	
	/**
	 * 根据用户id获取收货地址列表
	 * @param userId
	 * @return
	 */
	public List<UserShoppingAddress> getAddressList(String userId);
	
	/**
	 * 保存收货地址
	 * @param address
	 * @return
	 */
	public boolean saveAddress(UserShoppingAddress address);

	/**
	 * 获取用户收货地址详情
	 * @param id
	 * @return
     */
	UserShoppingAddress getAddressDetail(int id);
	
}
