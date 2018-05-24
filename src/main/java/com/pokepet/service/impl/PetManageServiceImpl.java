package com.pokepet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokepet.dao.PetMapper;
import com.pokepet.model.Pet;
import com.pokepet.service.IPetManageService;

@Service
public class PetManageServiceImpl implements IPetManageService{

	@Autowired
	PetMapper petMapper;
	
	@Override
	public Pet getPetByPetId(String petId) {
		return petMapper.selectByPrimaryKey(petId);
	}

	@Override
	public boolean addPet(Pet record) {
		return petMapper.insertSelective(record) == 1;
	}

	@Override
	public boolean uptPet(Pet record) {
		return petMapper.updateByPrimaryKeySelective(record) == 1;
	}

}
