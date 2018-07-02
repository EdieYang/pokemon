package com.pokepet.service;

import java.util.List;

import com.pokepet.model.Pet;
import com.pokepet.model.PetAlbum;

public interface IPetManageService {
	
	Pet getPetByPetId(String petId);
	
	boolean addPet(Pet record);
	
	boolean uptPet(Pet record);
	
	boolean delPet(String petId);
	
	List<Pet> getPetListByUserId(String userId);

	List<PetAlbum> getPetAlbumByPetId(String petId);

}
