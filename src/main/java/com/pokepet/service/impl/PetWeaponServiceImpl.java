package com.pokepet.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.dao.PetMapper;
import com.pokepet.dao.PetSupplyConcatMapper;
import com.pokepet.dao.PetSupplyMapper;
import com.pokepet.dao.PetWeaponConcatMapper;
import com.pokepet.dao.PetWeaponMapper;
import com.pokepet.dao.UserMapper;
import com.pokepet.model.Pet;
import com.pokepet.model.PetSupply;
import com.pokepet.model.PetSupplyConcat;
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

	@Autowired
	PetSupplyMapper petSupplyMapper;

	@Autowired
	PetSupplyConcatMapper petSupplyConcatMapper;

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
		List<PetWeapon> newWeaponList = new ArrayList<PetWeapon>();
		List<PetSupply> newSupplyList = new ArrayList<PetSupply>();
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
				supplyRate = 3;

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
				newWeapon.setUserId("4df926ea-7144-4b35-a290-4cf5cc26bff6");
				PetWeaponConcatMapper.insertSelective(newWeapon);// 入库
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
				newSupply.setUserId("4df926ea-7144-4b35-a290-4cf5cc26bff6");
				petSupplyConcatMapper.insertSelective(newSupply);// 入库
				newSupplyList.add(supply);

			}

			// 扣除活力值
			Pet pet = petMapper.selectByPrimaryKey(petId);
			pet.setEnergyCoin(pet.getEnergyCoin() - discountEnergyCoin);
			petMapper.updateByPrimaryKeySelective(pet);
			result.put("energyCoin", pet.getEnergyCoin());

			// 生成奖励物品，保存入庫
			result.put("chip", chipCount + chipAddCount);
			result.put("weapon", newWeaponList);
			result.put("supply", newSupplyList);
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
