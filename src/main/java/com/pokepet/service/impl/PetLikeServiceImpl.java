package com.pokepet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokepet.dao.PetLikeMapper;
import com.pokepet.model.PetLike;
import com.pokepet.service.IPetLikeService;

import java.util.List;

@Service
public class PetLikeServiceImpl implements IPetLikeService{

	@Autowired
	PetLikeMapper petLikeMapper;
	
	@Override
	public boolean addPetLike(PetLike record) {
		return petLikeMapper.insertSelective(record) == 1;
	}

	@Override
	public boolean delPetLike(String petId, String userId) {
		return petLikeMapper.delPetLike(petId, userId) >= 0;
	}

	@Override
	public int getLikeCountByPetId(String petId) {
		return petLikeMapper.getLikeCountByPetId(petId);
	}

	@Override
	public int getPetLikeCountByUserId(String petId, String userId) {
		return petLikeMapper.getPetLikeCountByUserId(petId,userId);

	}


}
