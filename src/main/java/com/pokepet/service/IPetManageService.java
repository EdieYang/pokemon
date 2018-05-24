package com.pokepet.service;

import com.pokepet.model.Pet;

public interface IPetManageService {
	
	Pet getPetByPetId(String petId);
	
	boolean addPet(Pet record);
	
	boolean uptPet(Pet record);

}
