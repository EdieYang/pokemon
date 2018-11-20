package com.pokepet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.dao.UserMapper;
import com.pokepet.model.User;
import com.pokepet.util.wxConfig.SHA1;
import com.pokepet.util.wxConfig.Token;
import com.pokepet.util.wxpay.RandomStringGenerator;
import com.pokepet.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Fade on 2018/11/6.
 */

@ResponseResult
@RestController
@RequestMapping("/wxAuth")
public class WxAuthorizeController {



    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.secret}")
    private String secret;

    @Autowired
    private UserMapper userMapper;


    @RequestMapping(value = "/authUserInfo",method = RequestMethod.GET)
    public Map<String,String> authUserInfo(@RequestParam("code")String code){
        Map<String,String> map=new HashMap<>();
        //获取accessToken & openId
        String reqUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
        String res=HttpClientUtils.httpGet(reqUrl);
        JSONObject resJ= JSON.parseObject(res);
        String accessToken=resJ.getString("access_token");
        String openId=resJ.getString("openid");

        //查询用户是否存在
        User user=userMapper.getUserByOpenId(openId);
        if(user==null && !StringUtils.isEmpty(openId)){
            //获取用户基本信息 & unionId
            String reqUserInfoUrl="https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN";
            String resUserInfo=HttpClientUtils.httpGet(reqUserInfoUrl);
            JSONObject resUserInfoJ=JSON.parseObject(resUserInfo);
            String nickName=resUserInfoJ.getString("nickname");
            String portrait=resUserInfoJ.getString("headimgurl");

            User newUser=new User();
            String userId= UUID.randomUUID().toString();
//            newUser.(openId);
            newUser.setCreateDatetime(new Date());
            newUser.setUserId(userId);
            newUser.setNickName(nickName);
            newUser.setPhotoPath(portrait);
            userMapper.insertSelective(newUser);
            map.put("userId",userId);
        }else if(user!=null &&  !StringUtils.isEmpty(openId)) {
            map.put("userId",user.getUserId());
        }

        return map;

    }



    @RequestMapping("/configWxEnvironment")
    public Map<String,Object> configWxEv(@RequestParam("url") String url){
        Map<String,Object> map=new HashMap<>();

        String ticket = Token.getTicket();
        String noncestr = RandomStringGenerator.getRandomStringByLength(32);
        long timestamp = new Date().getTime()/1000;
        String sign =  "jsapi_ticket="+ticket+"&noncestr="//请勿更换字符组装顺序
                +noncestr+"&timestamp="+timestamp
                +"&url="+url;
        try {
            String signature = new SHA1().getDigestOfString(sign.getBytes("utf-8"));
            map.put("appId",appId);
            map.put("timestamp",timestamp);
            map.put("nonceStr",noncestr);
            map.put("signature",signature);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return map;
    }

}
