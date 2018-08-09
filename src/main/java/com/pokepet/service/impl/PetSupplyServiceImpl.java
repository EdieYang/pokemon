package com.pokepet.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.dao.PetMapper;
import com.pokepet.dao.PetSupplyConcatMapper;
import com.pokepet.dao.PetSupplyMapper;
import com.pokepet.model.Pet;
import com.pokepet.model.PetSupply;
import com.pokepet.model.PetSupplyConcat;
import com.pokepet.service.IPetSupplyService;

@Service
public class PetSupplyServiceImpl implements IPetSupplyService{
	
	@Autowired
	PetSupplyConcatMapper petSupplyConcatMapper;
	
	@Autowired
	PetSupplyMapper petSupplyMapper;
	
	@Autowired
	PetMapper petMapper;

	@Override
	public List<Map<String, Object>> getSupplyByUserId(String userId) {
		return petSupplyConcatMapper.getSupplyByUserId(userId);
	}

	@Override
	public List<PetSupply> getSupplyList() {
		return petSupplyMapper.getSupplyList();
	}

	@Override
	public JSONObject useSupply(String petId, String id) {
		JSONObject result = new JSONObject();
		Pet pet = petMapper.selectByPrimaryKey(petId);
		PetSupplyConcat supplyConcat = petSupplyConcatMapper.selectByPrimaryKey(id);
		if(null!= supplyConcat && "0".equals(supplyConcat.getDelFlag())){
			PetSupply supply = petSupplyMapper.selectByPrimaryKey(supplyConcat.getSupplyId());
			if(null != supply){
				if(pet.getEnergyCoin() + supply.getAddEnergy() >100){
					pet.setEnergyCoin(100);
				}else{
					pet.setEnergyCoin(pet.getEnergyCoin() + supply.getAddEnergy());
				}
				petMapper.updateByPrimaryKeySelective(pet);
				result.put("energyCoin", pet.getEnergyCoin());
			}
			
		}
		
		return result;
	}

}
