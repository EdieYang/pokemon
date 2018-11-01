package com.pokepet.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pokepet.dao.UserShoppingAddressMapper;
import com.pokepet.model.UserShoppingAddress;
import com.pokepet.service.IShoppingAddressService;

@Service
public class ShoppingAddressServiceImpl implements IShoppingAddressService {

	@Autowired
	UserShoppingAddressMapper userShoppingAddressMapper;

	@Override
	public List<UserShoppingAddress> getAddressList(String userId) {
		return userShoppingAddressMapper.getAddressList(userId);
	}

	@Transactional
	@Override
	public boolean saveAddress(UserShoppingAddress address) {
		boolean FLAG = false;
		try {
			if("0".equals(address.getDefaultFlag())){ //如果此地址是默认地址,先将其他地址的默认地址属性重置为1
				userShoppingAddressMapper.resetDefultAddress(address.getUserId());
			}

			if (null == address.getId()) {
				// 新增
				userShoppingAddressMapper.insertSelective(address);
			} else {
				// 修改
				userShoppingAddressMapper.updateByPrimaryKeySelective(address);
			}
			FLAG = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return FLAG;
	}

	@Override
	public UserShoppingAddress getAddressDetail(int id) {
		return userShoppingAddressMapper.selectByPrimaryKey(id);
	}

}
