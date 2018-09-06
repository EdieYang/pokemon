package com.pokepet.controller;

import com.pokepet.annotation.ResponseResult;
import com.pokepet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Fade on 2018/8/9.
 */
@ResponseResult
@RestController
@RequestMapping("/userFollow")
public class UserFollowController {

    @Autowired
    IUserService userService;

//    @RequestMapping(value = "/{userId}",method = RequestMethod.POST,consumes="application/json")
//    public String
}
