package com.pokepet.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.algorithm.PetAlgorithm;
import com.pokepet.dao.PetMapper;
import com.pokepet.dao.UserWalkHistoryMapper;
import com.pokepet.dao.UserWalkLocationMapper;
import com.pokepet.enums.PetLevelEnum;
import com.pokepet.model.Pet;
import com.pokepet.model.UserWalkHistory;
import com.pokepet.model.UserWalkLocation;
import com.pokepet.service.IWalkService;

@Service
public class WalkServiceImpl implements IWalkService {

	@Autowired
	UserWalkHistoryMapper userWalkHistoryMapper;

	@Autowired
	UserWalkLocationMapper userWalkLocationMapper;

	@Autowired
	PetMapper petMapper;

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

	@Transactional
	@Override
	public JSONObject finishWalk(UserWalkHistory record) {
		JSONObject result = new JSONObject();
		try {
			int distance = record.getDistance();

			Pet pet = petMapper.selectByPrimaryKey(record.getPetId());
			int petLevel = pet.getLevel();
			int petExp = pet.getExp();

			// 计算经验值
			int walkExp = PetAlgorithm.getWalkExp(petLevel, distance);

			result.put("walkExp", walkExp);
			record.setExp(walkExp);

			// 计算活力值
			int walkVitality = distance / 50;
			result.put("walkVitality", walkVitality);
			record.setVitality(walkVitality);

			// 计算等级
			JSONArray jsArr = new JSONArray();
			while (walkExp >= 0) {
				JSONObject jsLevel = new JSONObject();
				jsLevel.put("level", petLevel);
				int maxExp = PetLevelEnum.getValue(petLevel + 1);// 到达下一级的经验线
				if (petExp + walkExp >= maxExp) {
					jsLevel.put("maxExp", maxExp);
					jsLevel.put("startExp", petExp);
					jsLevel.put("endExp", maxExp);

					petLevel++;// level up！
					walkExp = petExp + walkExp - maxExp;// 计算剩余经验值
					petExp = 0;// 下一级初始经验值

				} else {

					jsLevel.put("maxExp", maxExp);
					jsLevel.put("startExp", petExp);
					jsLevel.put("endExp", petExp + walkExp);

					petExp = petExp + walkExp;// 最终态经验值
					walkExp = -1;// 跳出循环
				}
				jsArr.add(jsLevel);

			}

			// 保存当前level、exp
			pet.setLevel(petLevel);
			pet.setExp(petExp);
			petMapper.updateByPrimaryKeySelective(pet);

			// 结束walk
			userWalkHistoryMapper.updateByPrimaryKeySelective(record);

			result.put("level", jsArr);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

}
