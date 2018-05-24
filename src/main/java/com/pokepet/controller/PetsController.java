package com.pokepet.controller;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Fade on 2018/5/23.
 * this is the controller for pets resources
 */

@ResponseResult
@RestController
@RequestMapping("/pets")
public class PetsController {


    /**
     * 根据宠物id获取宠物基础信息
     * @return
     */
    @RequestMapping(value = "/pet/{petId}",method = RequestMethod.GET)
    public String getPetInfoByPetId(@PathVariable String petId, HttpServletRequest request){
        JSONObject resJson=new JSONObject();

        if(StringUtils.isEmpty(petId)){
            resJson.put("success",true);
            resJson.put("data","");
            resJson.put("msg","参数获取为空:petId");
            return resJson.toJSONString();
        }




        return null;
    }




}
