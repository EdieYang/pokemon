package com.pokepet.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokepet.dao.UserDonateMapper;
import com.pokepet.model.UserDonate;
import com.pokepet.service.IDonateService;

@Service
public class DonateServiceImpl implements IDonateService{
	
	@Autowired
	UserDonateMapper userDonateMapper;

	@Override
	public UserDonate getLastDonate(Map<String, Object> param) {
		return userDonateMapper.getLastDonate(param);
	}

	@Override
	public List<Map<String, Object>> getDonateList(Map<String, Object> param) {
		return userDonateMapper.getDonateList(param);
	}

	@Override
	public boolean insertRecord(UserDonate record) {
		return userDonateMapper.insertSelective(record) > 0;
	}

}
