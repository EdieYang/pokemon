package com.pokepet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.dao.UserMapper;
import com.pokepet.model.User;
import com.pokepet.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by CharlieX-Man on 2018/6/20.
 * 处理小程序登录
 */


@ResponseResult
@RestController
@RequestMapping("/system")
public class LoginController {

    @Value("${temporary.login.ticket.url}")
    private String codeTicketUrl;

    @Value("${linkPet.appId}")
    private String appId;

    @Value("${linkPet.appSecret}")
    private String appSecret;


    @Autowired
    private UserMapper userMapper;



    @RequestMapping("/login")
    public Map<String,Object> login(HttpServletRequest request){

        String code=request.getParameter("code");

        if(StringUtils.isEmpty(code)){
            return null;
        }

        //请求wx获取openId
        codeTicketUrl=codeTicketUrl.replace("APPID",appId)
                .replace("SECRET",appSecret).replace("JSCODE",code);
        String response= HttpClientUtils.httpGet(codeTicketUrl);
        JSONObject resJsonObj= JSON.parseObject(response);
        String openId=resJsonObj.getString("openid");


        Map<String,Object> map=new HashMap<>();

        //用户是否注册
        User user=userMapper.getUserByOpenId(openId);
        if(user==null && !StringUtils.isEmpty(openId)){
            User newUser=new User();
            String userId=UUID.randomUUID().toString();
            newUser.setOpenId(openId);
            newUser.setCreateDatetime(new Date());
            newUser.setUserId(userId);
            userMapper.insertSelective(newUser);
            map.put("userId",userId);
        }else{
            map.put("userId","");
        }

        return map;

    }



    @RequestMapping("/userId")
    public Map<String,Object> getUserIdByOpenId(HttpServletRequest request){

        String openId=request.getParameter("openId");


        Map<String,Object> map=new HashMap<>();

        //用户是否注册
        User user=userMapper.getUserByOpenId(openId);

        map.put("userId",user.getUserId());

        return map;

    }


}
