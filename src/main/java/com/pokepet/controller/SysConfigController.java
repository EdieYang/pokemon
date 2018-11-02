package com.pokepet.controller;

import com.pokepet.annotation.ResponseResult;
import com.pokepet.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fade on 2018/11/2.
 */

@ResponseResult
@RestController
@RequestMapping("/system")
public class SysConfigController {

    @Autowired
    IPropertyService propertyService;

    @RequestMapping(value = "/sysConfigSwitchStatus/{appVersion}", method = RequestMethod.GET, consumes = "application/json")
    public Map<String,Object> getExploreSwitchStatus(@PathVariable("appVersion") int appVersion) {
        Map<String,Object> reqMap=new HashMap<>();
        reqMap.put("confType","1");
        Map<String,Object> map=propertyService.getPropertyStatus(reqMap);
        if(Integer.parseInt(String.valueOf(map.get("appVersion")))<appVersion){
            map.put("status","1");
        }else{
            map.put("status",map.get("switchStatus"));
        }
        return map;
    }
}
