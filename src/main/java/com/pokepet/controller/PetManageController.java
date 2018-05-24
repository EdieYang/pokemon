package com.pokepet.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.Pet;
import com.pokepet.model.User;
import com.pokepet.service.IPetManageService;

@ResponseResult
@RestController
@RequestMapping("/pet")
public class PetManageController {
	
	@Autowired
	IPetManageService petManageService;
	
	@RequestMapping(value = "/{petId}",method = RequestMethod.GET,consumes="application/json")
    public Pet getPet(@PathVariable String petId){
        return petManageService.getPetByPetId(petId);
    }
	
	@PostMapping
	@RequestMapping(value = "/",method = RequestMethod.POST,consumes="application/json")
    public boolean getPet(@RequestBody Pet pet){
//		pet.setPetId(UUID.randomUUID().toString());
        return petManageService.addPet(pet);
    }
	
	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Validated @RequestBody User user) {
        user.setUserId("111");
        user.setCreateDatetime(new Date());
        return user;
    }

}
