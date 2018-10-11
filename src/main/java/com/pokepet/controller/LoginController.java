package com.pokepet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.dao.UserMapper;
import com.pokepet.model.User;
import com.pokepet.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


//@ResponseResult
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

    private static final Logger log= LoggerFactory.getLogger(LoginController.class);


    @RequestMapping("/login")
    public Map<String,Object> login(HttpServletRequest request){

        String code=request.getParameter("code");
        log.info(">>>>>>>>>>>>>>>>>获取到的code为:"+code);

        if(StringUtils.isEmpty(code)){
            log.info(">>>>>>>>>>>>>>>>>获取到的code为空");
            return null;
        }
        String openId="";
        //先获取缓存中的code
        Object codeObj=request.getSession().getAttribute(code);

        if(codeObj!=null){
            openId=codeObj.toString();
        }

        log.info(">>>>>>>>>>>>>>>>>获取到用户缓存的openId为:"+openId);
        if(StringUtils.isEmpty(openId)){
            //请求wx获取openId
            log.info("=================通过code获取openId======================");

            String requestCodeUrl=codeTicketUrl+"?appid="+appId+"&secret="+appSecret+"&js_code="+code+"&grant_type=authorization_code";
            String response= HttpClientUtils.httpGet(requestCodeUrl);
            log.info(response);

            JSONObject resJsonObj= JSON.parseObject(response);
            openId=resJsonObj.getString("openid");
            log.info(">>>>>>>>>>>>>>>>>获取到用户的openId为:"+openId);

            if(!StringUtils.isEmpty(openId)){
                request.getSession().setAttribute(code,openId);
            }
        }



        Map<String,Object> map=new HashMap<>();
        log.info(">>>>>>>>>>>>>>>>判断用户是否注册>>>>>>>>>>>>>");


        //用户是否注册
        User user=userMapper.getUserByOpenId(openId);
        if(user==null && !StringUtils.isEmpty(openId)){
            User newUser=new User();
            String userId=UUID.randomUUID().toString();
            newUser.setOpenId(openId);
            newUser.setCreateDatetime(new Date());
            newUser.setUserId(userId);
            userMapper.insertSelective(newUser);
            log.info(">>>>>>>>>>>>>>>>>获取到用户的userId为:"+userId);
            map.put("userId",userId);
        }else if(user!=null &&  !StringUtils.isEmpty(openId)){
            log.info(">>>>>>>>>>>>>>>>>获取到用户的userId为:"+user.getUserId());
            map.put("userId",user.getUserId());
        }else{
            map.put("userId","");
        }

        map.put("openId",openId);
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
