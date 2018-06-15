package com.pokepet.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokepet.dao.UserWalkHistoryMapper;
import com.pokepet.dao.UserWalkLocationMapper;
import com.pokepet.model.UserWalkHistory;
import com.pokepet.model.UserWalkLocation;
import com.pokepet.service.IWalkService;

@Service
public class WalkServiceImpl implements IWalkService{

	@Autowired
	UserWalkHistoryMapper userWalkHistoryMapper;
	
	@Autowired
	UserWalkLocationMapper userWalkLocationMapper;
	
	@Override
	public boolean addHistory(UserWalkHistory record) {
		return userWalkHistoryMapper.insertSelective(record) == 1;
	}

	@Override
	public boolean addLocation(UserWalkLocation record) {
		return userWalkLocationMapper.insertSelective(record) == 1;
	}

	@Override
	public List<UserWalkHistory> getHistoryListByUserId(String userId) {
		return userWalkHistoryMapper.getHistoryListByUserId(userId);
	}

	@Override
	public List<UserWalkLocation> getLocationListByHistoryId(String historyId) {
		return userWalkLocationMapper.getLocationListByHistoryId(historyId);
	}

	@Override
	public UserWalkHistory getHistoryByHistoryId(String historyId) {
		return userWalkHistoryMapper.selectByPrimaryKey(historyId);
	}

	@Override
	public int getWalkDistanceByUserId(String usersId) {
		return userWalkHistoryMapper.getWalkDistanceByUserId(usersId);
	}

	@Override
	public int getWalkDistanceByPetId(String petId) {
		return userWalkHistoryMapper.getWalkDistanceByPetId(petId);
	}

}
