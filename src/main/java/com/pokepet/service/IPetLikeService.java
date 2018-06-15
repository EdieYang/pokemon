package com.pokepet.service;

import com.pokepet.model.PetLike;

public interface IPetLikeService {
	
	boolean addPetLike(PetLike record);
	
	boolean delPetLike(String petId,String userId);
	
	int getLikeCountByPetId(String petId);

}
