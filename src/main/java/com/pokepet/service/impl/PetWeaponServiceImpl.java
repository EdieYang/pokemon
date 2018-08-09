package com.pokepet.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokepet.dao.PetMapper;
import com.pokepet.dao.PetSupplyConcatMapper;
import com.pokepet.dao.PetSupplyMapper;
import com.pokepet.dao.PetWeaponConcatMapper;
import com.pokepet.dao.PetWeaponMapper;
import com.pokepet.dao.UserMapper;
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


	@Override
	public List<Map<String, Object>> getWeaponByUserId(String userId) {
		return PetWeaponConcatMapper.getWeaponByUserId(userId);
	}

	@Override
	public List<PetWeapon> getWeaponList() {
		return petWeaponMapper.getWeaponList();
	}

}
