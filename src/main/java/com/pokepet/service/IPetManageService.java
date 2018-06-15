package com.pokepet.service;

import java.util.List;

import com.pokepet.model.Pet;

public interface IPetManageService {
	
	Pet getPetByPetId(String petId);
	
	boolean addPet(Pet record);
	
	boolean uptPet(Pet record);
	
	boolean delPet(String petId);
	
	List<Pet> getPetListByUserId(String userId);

}
