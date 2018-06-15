package com.pokepet.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.enums.PetLevelEnum;
import com.pokepet.enums.StarSignEnum;
import com.pokepet.model.Pet;
import com.pokepet.service.IPetManageService;

import cpm.pokepet.util.CommonUtil;

@ResponseResult
@RestController
@RequestMapping("/pet")
public class PetManageController {
	
	@Autowired
	IPetManageService petManageService;
	
	/**
	 * 
	 * @Description: 根据petId获取宠物信息
	 * @param @param petId
	 * @param @return   
	 * @return Pet  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月24日
	 */
	@RequestMapping(value = "/{petId}",method = RequestMethod.GET,consumes="application/json")
    public Pet getPet(@PathVariable String petId){
        return petManageService.getPetByPetId(petId);
    }
	
	@RequestMapping(value = "/2/{petId}",method = RequestMethod.GET,consumes="application/json")
    public Pet getPet2(@PathVariable String petId){
        return petManageService.getPetByPetId(petId);
    }
	
	/**
	 * 
	 * @Description: 新增宠物
	 * @param @param pet
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月24日
	 */
	@PostMapping
	@RequestMapping(value = "/",method = RequestMethod.POST,consumes="application/json")
    public boolean addPet(@RequestBody Pet pet){
		pet.setPetId(UUID.randomUUID().toString());
        return petManageService.addPet(pet);
    }
	
	/**
	 * 
	 * @Description: 修改宠物
	 * @param @param pet
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月24日
	 */
	@PostMapping
	@RequestMapping(value = "/",method = RequestMethod.PUT,consumes="application/json")
    public boolean uptPet(@RequestBody Pet pet){
        return petManageService.uptPet(pet);
    }
	
	/**
	 * 
	 * @Description: 删除宠物
	 * @param @param petId
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月24日
	 */
	@PostMapping
	@RequestMapping(value = "/{petId}",method = RequestMethod.DELETE,consumes="application/json")
    public boolean uptPet(@PathVariable String petId){
        return petManageService.delPet(petId);
    }
	
	

}
