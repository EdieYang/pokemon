package com.pokepet.service.impl;

import java.util.Calendar;
import java.util.List;

import com.pokepet.dao.PetAlbumMapper;
import com.pokepet.model.PetAlbum;
import com.pokepet.model.UserPet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokepet.dao.PetMapper;
import com.pokepet.dao.UserPetMapper;
import com.pokepet.model.Pet;
import com.pokepet.service.IPetManageService;

@Service
public class PetManageServiceImpl implements IPetManageService{

	private static int petIdGrenc = -1;

	private static int year = -1;

	private final static String PET_ID_TEMP = "pt-03+区号+年份（后两位）+00001";

	
	@Autowired
	PetMapper petMapper;

	@Autowired
	PetAlbumMapper petAlbumMapper;
	
	@Autowired
	UserPetMapper userPetMapper;
	
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

	@Override
	public String createPetId(String areaId) {
		Calendar cal = Calendar.getInstance();

		if (cal.get(Calendar.YEAR) != year) {
			year = cal.get(Calendar.YEAR);
			String maxId = petMapper.getMaxPetNo(PET_ID_TEMP.substring(0, 3), (year+"").substring(2));
			petIdGrenc = null == maxId ? 1 : Integer.parseInt(maxId)+1;
			return createPetId(areaId);
		} else {
			String userIdEnd = "000000" + petIdGrenc++;
			return PET_ID_TEMP.substring(0, 3) + areaId + ("" + year).substring(2, 4)
					+ userIdEnd.substring(userIdEnd.length() - 6, userIdEnd.length());
		}
	}

	@Override
	public boolean bindlingPetToUser(String petId, String userId) {
		UserPet userPet=new UserPet();
		userPet.setUserId(userId);
		userPet.setPetId(petId);
		userPet.setDelFlag("0");
		return userPetMapper.insertSelective(userPet) == 1;
	}

	@Override
	public int updatePetsEnergyToOneHundredPercent() {
		int effectedRows=petMapper.updatePetsEnergyToOneHundredPercent();
		return effectedRows;
	}

	@Override
	public int countAllPets() {
		return petMapper.countAllPets();
	}
}
