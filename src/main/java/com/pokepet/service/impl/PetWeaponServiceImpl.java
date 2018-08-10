package com.pokepet.service.impl;

import java.util.Date;
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
import com.pokepet.dao.UserPaymentLogMapper;
import com.pokepet.model.PetSupply;
import com.pokepet.model.PetSupplyConcat;
import com.pokepet.model.PetWeapon;
import com.pokepet.model.PetWeaponConcat;
import com.pokepet.model.User;
import com.pokepet.model.UserPaymentLog;
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
	
	@Autowired
	UserPaymentLogMapper serPaymentLogMapper;

	@Override
	public List<Map<String, Object>> getWeaponByPetId(String petId) {
		return PetWeaponConcatMapper.getWeaponByPetId(petId);
	}

	@Override
	public boolean setPetWeaponConcat(PetWeaponConcat concat) {
		return PetWeaponConcatMapper.updateByPrimaryKeySelective(concat) == 1;
	}

	@Override
	public List<Map<String, Object>> getWeaponByUserId(String userId) {
		return PetWeaponConcatMapper.getWeaponByUserId(userId);
	}

	@Override
	public List<PetWeapon> getWeaponList() {
		return petWeaponMapper.getWeaponList();
	}

	@Transactional
	@Override
	public JSONObject buyWeapon(String userId, String weaponId, String payWay, JSONObject payInfo) {
		JSONObject result = new JSONObject();
		try {
			UserPaymentLog log = new UserPaymentLog();
			switch (payWay) {
			case "money":// 现金购买
				// 记录购买信息
				log.setUserId(userId);
				log.setPayWay(payWay);
				log.setPayDatetime(new Date());
				log.setAmount(payInfo.getInteger("amount"));
				log.setPayInfo(payInfo.toJSONString());
				//记录支付/兑换流水
				serPaymentLogMapper.insertSelective(log);

				break;
			case "chip":// 碎片兑换
				User user = userMapper.selectByPrimaryKey(userId);
				PetWeapon weapon = petWeaponMapper.selectByPrimaryKey(weaponId);
				if (weapon.getBuyChip() < 0) {
					// 装备无法兑换
					result.put("flag", false);
					result.put("msg", "装备无法兑换");
					return result;
				} else if (user.getChipCount() < weapon.getBuyChip()) {
					// 碎片不足
					result.put("flag", false);
					result.put("msg", "碎片不足");
					return result;

				} else {
					user.setChipCount(user.getChipCount() - weapon.getBuyChip());
					userMapper.updateByPrimaryKeySelective(user);
					
					log.setUserId(userId);
					log.setPayWay(payWay);
					log.setPayDatetime(new Date());
					log.setAmount(weapon.getBuyChip());
					//记录支付/兑换流水
					serPaymentLogMapper.insertSelective(log);
				}

				break;

			default:
				break;
			}
			

			// 绑定装备
			PetWeaponConcat concat = new PetWeaponConcat();
			concat.setId(UUID.randomUUID().toString());
			concat.setUserId(userId);
			concat.setWeaponId(weaponId);
			PetWeaponConcatMapper.insertSelective(concat);
			result.put("flag", true);
			result.put("msg", "购买/兑换成功");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	@Transactional
	@Override
	public JSONObject buySupply(String userId, String supplyId, String payWay, JSONObject payInfo) {
		JSONObject result = new JSONObject();
		try {
			UserPaymentLog log = new UserPaymentLog();
			switch (payWay) {
			case "money":// 现金购买
				// 记录购买信息
				log.setUserId(userId);
				log.setPayWay(payWay);
				log.setPayDatetime(new Date());
				log.setAmount(payInfo.getInteger("amount"));
				log.setPayInfo(payInfo.toJSONString());
				//记录支付/兑换流水
				serPaymentLogMapper.insertSelective(log);

				break;
			case "chip":// 碎片兑换
				User user = userMapper.selectByPrimaryKey(userId);
				PetSupply supply = petSupplyMapper.selectByPrimaryKey(supplyId);
				if (supply.getBuyChip() < 0) {
					// 补给无法兑换
					result.put("flag", false);
					result.put("msg", "补给无法兑换");
					return result;
				} else if (user.getChipCount() < supply.getBuyChip()) {
					// 碎片不足
					result.put("flag", false);
					result.put("msg", "碎片不足");
					return result;

				} else {
					user.setChipCount(user.getChipCount() - supply.getBuyChip());
					userMapper.updateByPrimaryKeySelective(user);
					
					log.setUserId(userId);
					log.setPayWay(payWay);
					log.setPayDatetime(new Date());
					log.setAmount(supply.getBuyChip());
					//记录支付/兑换流水
					serPaymentLogMapper.insertSelective(log);
				}

				break;

			default:
				break;
			}
			

			// 绑定补给
			PetSupplyConcat concat = new PetSupplyConcat();
			concat.setId(UUID.randomUUID().toString());
			concat.setUserId(userId);
			concat.setSupplyId(supplyId);
			petSupplyConcatMapper.insertSelective(concat);
			result.put("flag", true);
			result.put("msg", "购买/兑换成功");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

}
