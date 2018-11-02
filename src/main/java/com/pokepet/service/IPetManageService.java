package com.pokepet.service;

import java.util.List;

import com.pokepet.model.Pet;
import com.pokepet.model.PetAlbum;
import com.pokepet.model.PetLike;

public interface IPetManageService {
	
	Pet getPetByPetId(String petId);
	
	boolean addPet(Pet record);
	
	boolean uptPet(Pet record);
	
	boolean delPet(String petId);
	
	List<Pet> getPetListByUserId(String userId);

	List<PetAlbum> getPetAlbumByPetId(String petId);
	
	/**
	 * 生成宠物ID，格式为"pt-03+区号+年份（后两位）+00001"
	 * @param areaId
	 * @return
	 */
	String createPetId(String areaId);
	
	boolean bindlingPetToUser(String petId,String userId);

	int updatePetsEnergyToOneHundredPercent();

	int countAllPets();


}
