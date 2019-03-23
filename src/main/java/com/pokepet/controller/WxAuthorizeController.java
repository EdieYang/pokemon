package com.pokepet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.dao.UserMapper;
import com.pokepet.model.User;
import com.pokepet.util.CommonUtil;
import com.pokepet.util.HttpUtil;
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

import javax.servlet.http.HttpServletRequest;
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



    @Value("${wx.service.appId}")
    private String s_appId;

    @Value("${wx.service.secret}")
    private String s_secret;

    @Value("${wx.subscribe.appId}")
    private String sub_appId;

    @Value("${wx.subscribe.secret}")
    private String sub_secret;


    @Value("${linkPet.appId}")
    private String lappId;

    @Value("${linkPet.appSecret}")
    private String lsecret;


    @Autowired
    private UserMapper userMapper;


    @RequestMapping(value = "/generateWxACode",method = RequestMethod.GET)
    public JSONObject generateWxACode(HttpServletRequest request) {
        Map<String,Object> map=new HashMap<>();
        String reqUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+lappId+"&secret="+lsecret;
        String res=HttpClientUtils.httpGet(reqUrl);
        JSONObject resJ= JSON.parseObject(res);
        String accessToken=resJ.getString("access_token");
        String scene=request.getParameter("scene");
        map.put("path","pages/exploredetail/exploredetail?scene="+scene+"&type=3");
        map.put("is_hyaline",true);
        String imageUrl=HttpUtil.postInputStream("https://api.weixin.qq.com/wxa/getwxacode?access_token="+accessToken,map);

        JSONObject returnObj=new JSONObject();
        returnObj.put("wxImageUrl",imageUrl);
        return returnObj;
    }


    @RequestMapping(value = "/authUserInfo",method = RequestMethod.GET)
    public Map<String,String> authUserInfo(@RequestParam("code")String code,@RequestParam("plateForm")String plateForm){
        Map<String,String> map=new HashMap<>();
        //获取accessToken & openId
        String secret="";
        String appId="";
        switch (plateForm){
            case "serviceAccount":
                appId=s_appId;
                secret=s_secret;
                break;
            case "subscribeAccount":
                appId=sub_appId;
                secret=sub_secret;
                break;
        }
        if(StringUtils.isEmpty(appId) && StringUtils.isEmpty(secret)){
            map.put("userId","");
            return map;
        }
        String reqUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
        String res=HttpClientUtils.httpGet(reqUrl);
        JSONObject resJ= JSON.parseObject(res);
        String accessToken=resJ.getString("access_token");
        String openId=resJ.getString("openid");


        //获取unionId
        String requestUrl="https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN";
        String resUnionId=HttpClientUtils.httpGet(requestUrl);
        JSONObject resUnion= JSON.parseObject(resUnionId);
        String unionId=resUnion.getString("unionid");


        //查询用户是否存在
        User user=userMapper.getUserByUnionId(unionId);
        if(user==null){
            //创建新用户
            String nickName=resUnion.getString("nickname");
            String portrait=resUnion.getString("headimgurl");

            User newUser=new User();
            String userId= UUID.randomUUID().toString();
            newUser.setCreateDatetime(new Date());
            newUser.setUserId(userId);
            newUser.setNickName(nickName);
            newUser.setPhotoPath(portrait);
            newUser.setUnionId(unionId);
            userMapper.insertSelective(newUser);
            map.put("userId",userId);
        }else if(user!=null) {
            map.put("userId",user.getUserId());
        }

        return map;

    }



    @RequestMapping("/configWxEnvironment")
    public Map<String,Object> configWxEv(@RequestParam("url") String url,@RequestParam("plateForm")String plateForm){
        Map<String,Object> map=new HashMap<>();
        String appId="";
        switch (plateForm){
            case "serviceAccount":
                appId=s_appId;
                break;
            case "subscribeAccount":
                appId=sub_appId;
                break;
        }
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


    public static void main(String[] args) {
        Map<String,Object> map=new HashMap<>();
        String reqUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxb716f1f37b7ef914&secret=1808ec52bc34d929810794f7ee0368bb";
        String res=HttpClientUtils.httpGet(reqUrl);
        JSONObject resJ= JSON.parseObject(res);
        String accessToken=resJ.getString("access_token");
        map.put("scene","springFesDinner");
        map.put("page","pages/activity/activityentrance/activityentrance");
        map.put("width",100);
        map.put("is_hyaline",false);
        String imageUrl=HttpUtil.postInputStream("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken,map);
        System.out.println("二维码生成:"+imageUrl);
    }

}
