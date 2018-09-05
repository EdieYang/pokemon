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
			if (null != address.getDelFlag() && "1".equals(address.getDelFlag())) {// 若此地址为默认地址，则将其他地址的默认地址属性重置为0
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

}
