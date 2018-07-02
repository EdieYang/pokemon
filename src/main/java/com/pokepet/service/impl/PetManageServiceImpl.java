package com.pokepet.service.impl;

import java.util.List;

import com.pokepet.dao.PetAlbumMapper;
import com.pokepet.model.PetAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokepet.dao.PetMapper;
import com.pokepet.model.Pet;
import com.pokepet.service.IPetManageService;

@Service
public class PetManageServiceImpl implements IPetManageService{

	@Autowired
	PetMapper petMapper;

	@Autowired
	PetAlbumMapper petAlbumMapper;
	
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

	@Override
	public boolean delPet(String petId) {
		Pet record = new Pet();
		record.setPetId(petId);
		record.setDelFlag("1");
		return petMapper.updateByPrimaryKeySelective(record) == 1;
	}

	@Override
	public List<Pet> getPetListByUserId(String userId) {
		return petMapper.getPetListByUserId(userId);
	}

	@Override
	public List<PetAlbum> getPetAlbumByPetId(String petId) {
		return petAlbumMapper.selectAlbumByPetId(petId);
	}

}
