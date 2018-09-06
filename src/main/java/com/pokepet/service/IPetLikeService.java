package com.pokepet.service;

import com.pokepet.model.PetLike;

import java.util.List;

public interface IPetLikeService {
	
	boolean addPetLike(PetLike record);
	
	boolean delPetLike(String petId,String userId);
	
	int getLikeCountByPetId(String petId);

	int getPetLikeCountByUserId(String petId,String userId);


}
