package com.pokepet.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.dao.PetMapper;
import com.pokepet.dao.PetWeaponConcatMapper;
import com.pokepet.dao.PetWeaponMapper;
import com.pokepet.dao.UserMapper;
import com.pokepet.model.Pet;
import com.pokepet.model.PetWeapon;
import com.pokepet.model.PetWeaponConcat;
import com.pokepet.service.IPetWeaponService;

@Service
public class PetWeaponServiceImpl implements IPetWeaponService {

	@Autowired
	PetWeaponConcatMapper PetWeaponConcatMapper;

	@Autowired
	PetWeaponMapper petWeaponMapper;

	@Autowired
	PetMapper petMapper;

	@Autowired
	UserMapper userMapper;

	@Override
	public List<Map<String, Object>> getWeaponByPetId(String petId) {
		return PetWeaponConcatMapper.getWeaponByPetId(petId);
	}

	@Override
	public boolean setPetWeaponConcat(PetWeaponConcat concat) {
		return PetWeaponConcatMapper.updateByPrimaryKeySelective(concat) == 1;
	}

	@Transactional
	@Override
	public JSONObject expolrePoint(String petId, int pointStar) {
		JSONObject result = new JSONObject();
		//
		try {
			int weaponDiscountRate = 0;
			int discountEnergyCoin = 0;

			int chipRate = 1;

			int chipCount = 0;

			int chipAddCount = 0;

			switch (pointStar) {
			case 1:
				chipCount = 1;
				weaponDiscountRate = 3;
				discountEnergyCoin = 20;

				break;

			case 2:
				chipCount = 3;
				weaponDiscountRate = 7;
				discountEnergyCoin = 25;

				break;

			case 3:
				chipCount = 5;
				weaponDiscountRate = 10;
				discountEnergyCoin = 30;

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

			}

			// 根据装备列表、探索地星级计算物品爆率
			if (chipRate >= 1) {
				chipCount += 1;
			}

			// 扣除活力值
			Pet pet = petMapper.selectByPrimaryKey(petId);
			pet.setEnergyCoin(pet.getEnergyCoin() - discountEnergyCoin);
			petMapper.updateByPrimaryKeySelective(pet);
			result.put("energyCoin", pet.getEnergyCoin());

			// 生成奖励物品，保存入庫
			result.put("chip", chipCount + chipAddCount);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	@Override
	public List<Map<String, Object>> getWeaponByUserId(String userId) {
		return PetWeaponConcatMapper.getWeaponByUserId(userId);
	}

}
