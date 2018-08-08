package com.pokepet.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokepet.dao.PetSupplyConcatMapper;
import com.pokepet.service.IPetSupplyService;

@Service
public class PetSupplyServiceImpl implements IPetSupplyService{
	
	@Autowired
	PetSupplyConcatMapper petSupplyConcatMapper;

	@Override
	public List<Map<String, Object>> getSupplyByUserId(String userId) {
		return petSupplyConcatMapper.getSupplyByUserId(userId);
	}

}
