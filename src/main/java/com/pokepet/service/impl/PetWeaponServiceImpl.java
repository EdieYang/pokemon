package com.pokepet.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokepet.dao.PetWeaponConcatMapper;
import com.pokepet.service.IPetWeaponService;

@Service
public class PetWeaponServiceImpl implements IPetWeaponService{
	
	@Autowired
	PetWeaponConcatMapper PetWeaponConcatMapper;

	@Override
	public List<Map<String, Object>> getWeaponByPetId(String petId) {
		return PetWeaponConcatMapper.getWeaponByPetId(petId);
	}

}
