package com.pokepet.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pokepet.model.UserShoppingAddress;

@RestController
@RequestMapping("/shoppingAddresss")
public class ShoppingAddressController {
	
	@RequestMapping(value = "/{userId}",method = RequestMethod.GET,consumes="application/json")
    public List<UserShoppingAddress> getAddressList(@PathVariable String userId){
        return null;
    }
	
	@RequestMapping(value = "/",method = RequestMethod.POST,consumes="application/json")
    public UserShoppingAddress addAddress(@PathVariable String userId){
        return null;
    }

}
