package com.pokepet.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.dao.ExplorePointMapper;
import com.pokepet.dao.PetMapper;
import com.pokepet.dao.PetSupplyConcatMapper;
import com.pokepet.dao.PetSupplyMapper;
import com.pokepet.dao.PetWeaponConcatMapper;
import com.pokepet.dao.PetWeaponMapper;
import com.pokepet.dao.UserExploreHistoryMapper;
import com.pokepet.dao.UserMapper;
import com.pokepet.enums.PetLevelEnum;
import com.pokepet.model.ExplorePoint;
import com.pokepet.model.GPSLocation;
import com.pokepet.model.Pet;
import com.pokepet.model.PetSupply;
import com.pokepet.model.PetSupplyConcat;
import com.pokepet.model.PetWeapon;
import com.pokepet.model.PetWeaponConcat;
import com.pokepet.model.User;
import com.pokepet.model.UserExploreHistory;
import com.pokepet.service.IExploreService;
import com.pokepet.util.LocationUtils;

@Service
public class ExploreServiceImpl implements IExploreService {

	@Autowired
	ExplorePointMapper explorePointMapper;

	@Autowired
	PetWeaponConcatMapper PetWeaponConcatMapper;

	@Autowired
	PetWeaponMapper petWeaponMapper;

	@Autowired
	PetMapper petMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	PetSupplyMapper petSupplyMapper;

	@Autowired
	PetSupplyConcatMapper petSupplyConcatMapper;

	@Autowired
	UserExploreHistoryMapper userExploreHistoryMapper;

	@Autowired
	private RedisTemplate<String,String> redisTemplate;

	@Override
	public JSONArray getExplorePoint(GPSLocation location, double distance, int pointStar, int pointCount) {
		JSONArray arr = new JSONArray();
		List<ExplorePoint> pointList = explorePointMapper.getPointListWithPointStar(pointStar);

		while (pointCount > 0) {
			if (pointList.size() > 0) {
				int aa = (int) (Math.random() * 100) % pointList.size();
				JSONObject point = (JSONObject) JSONObject.toJSON(pointList.get(aa));
				pointList.remove(aa);
				point.put("location", LocationUtils.GetRandomLocation(location, distance));
				arr.add(point);
				pointCount--;
			} else {
				pointCount = 0;
			}
		}

		return arr;
	}

	@Transactional
	@Override
	public JSONObject expolrePoint(String userId, String petId, String pointName, int pointStar, double longitude,
			double latitude) {
		JSONObject reward = new JSONObject();
		List<PetWeapon> newWeaponList = new ArrayList<PetWeapon>();
		List<PetSupply> newSupplyList = new ArrayList<PetSupply>();
		JSONObject jsLevel = new JSONObject();
		//
		try {
			// 一次探索装备损耗度
			int weaponDiscountRate = 0;
			// 一次探索活力值消耗度
			int discountEnergyCoin = 0;

			// 碎片掉落基础概率
			int chipRate = 1;
			// 碎片掉落基础数量
			int chipCount = 0;
			// 额外获得碎片的数量
			int chipAddCount = 0;

			// 装备爆率
			int weaponRate = 0;

			// 补给爆率
			int supplyRate = 0;

			switch (pointStar) {
			case 1:// 一星探索点
				chipCount = 1;
				weaponDiscountRate = 3;
				discountEnergyCoin = 20;

				weaponRate = 3;
				supplyRate = 6;

				break;

			case 2:// 二星探索点
				chipCount = 3;
				weaponDiscountRate = 7;
				discountEnergyCoin = 25;

				weaponRate = 7;
				supplyRate = 7;

				break;

			case 3:// 三星探索点
				chipCount = 5;
				weaponDiscountRate = 10;
				discountEnergyCoin = 30;

				weaponRate = 15;
				supplyRate = 30;

				break;

			default:
				break;
			}

			// 装备列表
			List<Map<String, Object>> concatList = PetWeaponConcatMapper.getWeaponByPetId(petId);
			// List<PetWeapon> weaponList = new ArrayList<PetWeapon>();
			for (int i = 0; i < concatList.size(); i++) {
				String id = (String) concatList.get(i).get("id");
				PetWeaponConcat concat = PetWeaponConcatMapper.selectByPrimaryKey(id);
				if (concat.getWeaponEndurancePercent() > weaponDiscountRate) {
					concat.setWeaponEndurancePercent(concat.getWeaponEndurancePercent() - weaponDiscountRate);
				} else {
					// 装备报废
					concat.setWeaponEndurancePercent(0);
					concat.setWeaponInstallState("0");// 不装备
					concat.setPetId("");// 清空petId
					concat.setWeaponStatus("1");// 装备不可用
				}
				PetWeaponConcatMapper.updateByPrimaryKeySelective(concat);// 扣除装备耐久、更新装备信息

				PetWeapon weapon = petWeaponMapper.selectByPrimaryKey(concat.getWeaponId());

				if (Math.random() * 100 <= weapon.getChipAddRate()) {
					chipAddCount += weapon.getChipAddCount();
				}

				chipRate = chipRate * weapon.getChipRate() / 100;

				weaponRate = weaponRate * weapon.getWeaponRate() / 100;
				supplyRate = supplyRate * weapon.getSupplyRate() / 100;

			}

			// 根据装备列表、探索地星级计算物品爆率
			if (chipRate >= 1) {
				chipCount += 1;
			}

			// 碎片入库
			User user = userMapper.selectByPrimaryKey(userId);
			user.setChipCount(user.getChipCount() + chipCount + chipAddCount);
			userMapper.updateByPrimaryKeySelective(user);

			double weaponP = Math.random() * 100;
			System.out.println("[随机幸运值]:" + weaponP + "[装备爆率:]" + weaponRate);
			if (weaponP <= weaponRate) {
				// 生成新装备
				System.out.println("--------生成新装备----------");
				List<PetWeapon> weaponList = petWeaponMapper.getWeaponListWithPointStar(pointStar);
				PetWeapon weapon = weaponList.get((int) (Math.random() * 100) % weaponList.size());
				System.out.println("[" + weapon.getWeaponName() + "]");
				PetWeaponConcat newWeapon = new PetWeaponConcat();
				newWeapon.setId(UUID.randomUUID().toString());
				newWeapon.setWeaponId(weapon.getWeaponId());
				newWeapon.setUserId(userId);
				PetWeaponConcatMapper.insertSelective(newWeapon);// 装备入库
				newWeaponList.add(weapon);

			}

			double supplyP = Math.random() * 100;
			System.out.println("[随机幸运值]:" + supplyP + "[补给爆率:]" + supplyRate);
			if (supplyP <= supplyRate) {
				// 生成新补给
				System.out.println("--------生成新补给----------");
				List<PetSupply> supplyList = petSupplyMapper.getSupplyListWithPointStar(pointStar);
				PetSupply supply = supplyList.get((int) (Math.random() * 100) % supplyList.size());
				System.out.println("[" + supply.getSupplyName() + "]");
				PetSupplyConcat newSupply = new PetSupplyConcat();
				newSupply.setId(UUID.randomUUID().toString());
				newSupply.setSupplyId(supply.getSupplyId());
				newSupply.setUserId(userId);
				petSupplyConcatMapper.insertSelective(newSupply);// 补给入库
				newSupplyList.add(supply);

			}

			// 扣除活力值
			Pet pet = petMapper.selectByPrimaryKey(petId);
			pet.setEnergyCoin(pet.getEnergyCoin() - discountEnergyCoin);
			
			// 获得经验
			int walkExp = 100;
			int petLevel = pet.getLevel();
			int petExp = pet.getExp();
			while (walkExp >= 0) {
				
				int maxExp = PetLevelEnum.getValue(petLevel + 1);// 到达下一级的经验线
				if (petExp + walkExp >= maxExp) {

					petLevel++;// level up！
					walkExp = petExp + walkExp - maxExp;// 计算剩余经验值
					petExp = 0;// 下一级初始经验值

				} else {

					jsLevel.put("startLevel", pet.getLevel());
					jsLevel.put("endLevel", petLevel);
					jsLevel.put("exp", 100);
					jsLevel.put("maxExp", maxExp);
					jsLevel.put("startExp", petExp);
					jsLevel.put("endExp", petExp + walkExp);

					petExp = petExp + walkExp;// 最终态经验值
					walkExp = -1;// 跳出循环
				}

			}
			pet.setLevel(petLevel);//设置宠物等级
			pet.setExp(petExp);//设置当前等级经验值
			petMapper.updateByPrimaryKeySelective(pet);//更新pet信息

			// 生成奖励物品，保存入庫
			reward.put("energyCoin",discountEnergyCoin);// 活力值
			reward.put("chip", chipCount + chipAddCount);//金币
			reward.put("weapon", newWeaponList);//装备
			reward.put("supply", newSupplyList);//补给
			reward.put("level", jsLevel);//等级、经验
			reward.put("pet", pet);//宠物信息

			// 保存探索记录
			UserExploreHistory history = new UserExploreHistory();
			history.setExploreDatetime(new Date());
			history.setUserId(userId);
			history.setPetId(petId);
			history.setPointName(pointName);
			history.setPointStar(pointStar);
			history.setLatitude(String.valueOf(latitude));
			history.setLongitude(String.valueOf(longitude));
			history.setReward(reward.toJSONString());
			userExploreHistoryMapper.insertSelective(history);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println(reward.toJSONString());
		return reward;
	}

	@Override
	public List<UserExploreHistory> getExploreHistory(String userId) {
		return userExploreHistoryMapper.getExploreHistory(userId);
	}

	@Override
	public int getExploreCountForUserToday(String userId) {
		return userExploreHistoryMapper.getExploreCountForUserToday(userId);
	}

	@Override
	public List<String> getEmergencyPoints(String city) {
		ListOperations<String,String> oper= redisTemplate.opsForList();
		List<String> resList=oper.range("emergencyMap:"+city,0,-1);
		return resList;
	}

}
