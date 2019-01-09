package com.pokepet.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.dao.ShareRecordMapper;
import com.pokepet.dao.UserMapper;
import com.pokepet.dao.UserTempMapper;
import com.pokepet.model.User;
import com.pokepet.model.UserTemp;
import com.pokepet.service.IPetManageService;
import com.pokepet.service.IRecordService;
import com.pokepet.util.HttpUtil;
import com.pokepet.util.wxConfig.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Configurable
@EnableScheduling
public class ScheduledTasks{

    @Autowired
    private IPetManageService petManageService;

    @Autowired
    private IRecordService recordService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private ShareRecordMapper shareRecordMapper;

    @Autowired
    private UserMapper userMapper;



    private static String OPERATE_FAIL_TEMPLATEID="vCiucbFkqFmu_kXzoo2Io58rtG0eDmWO_B0XAbwIpO4";

    private static String OPERATE_SUCCESS_TEMPLATEID="4nCy9M-PlEf3mZT7Vx2z69Ji58RzQfztcNjXj9EtTSE";

    private static String sendTemplateMessageUrl="https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send";

    private static final Logger log= LoggerFactory.getLogger(ScheduledTasks.class);

    //每日凌晨刷新宠物活力值
    @Scheduled(cron = "0 0 0 * * ? ")
    @Async
    public void reportCurrentByCron(){
        log.info("Scheduling Tasks fill energy for all pets: The time is now " + dateFormat ().format (new Date()));
        int rows=petManageService.countAllPets();
        int totalRows=petManageService.updatePetsEnergyToOneHundredPercent();
        log.info("Update pets number is :"+rows);
        log.info("Updated pets number is :"+totalRows);
    }


    //每日凌晨刷新寻宠定位坐标
    @Scheduled(cron = "0 56 13 * * ? ")
    @Async
    public void refreshEmergencyPoints(){
        log.info("Scheduling Tasks refreshing emergencyPoints for all cities: The time is now " + dateFormat ().format (new Date()));
        //获取寻宠城市列表
        List<Map<String,String>> emergencyPoints= recordService.getCollectEmergencyPointCityList();
        int points=emergencyPoints.size();
        List<String> citylist=recordService.getCollectCityList();
        try {
            //删除寻宠坐标点缓存
            for (String item : citylist) {
                Set<String> keys = redisTemplate.keys("emergencyMap:" + item);
                redisTemplate.delete(keys);
            }

            ListOperations<String, String> oper = redisTemplate.opsForList();
            int i = 0;
            for (Map<String, String> map : emergencyPoints) {
                String content = map.get("content");
                String recordId = map.get("recordId");
                String lat = map.get("lat");
                String lng = map.get("lng");
                String city = map.get("city");
                JSONObject contentObj = JSON.parseObject(content);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("recordId", recordId);
                jsonObject.put("lat", lat);
                jsonObject.put("lng", lng);
                jsonObject.put("city", city);
                jsonObject.put("emergencyType", contentObj.getString("emergencyType"));
                String popTitle = "";
                if (contentObj.getString("emergencyType").equals("1")) {
                    popTitle = contentObj.getString("petName");
                } else if (contentObj.getString("emergencyType").equals("2")) {
                    popTitle = contentObj.getString("speciesType");
                }
                jsonObject.put("popTitle", popTitle);
                oper.leftPush("emergencyMap:" + city, JSON.toJSONString(jsonObject));
                i++;

            }
            log.info("Scheduling Tasks refreshing emergencyPoints for all cities done !!!");
            log.info("refreshing points size:"+points);
            log.info("finish refreshing points size:"+i);
        }catch (Exception e){
            //执行补偿
            e.printStackTrace();
            log.warn(e.getMessage());
        }



    }



    //每日9点发送开宝箱状态模板消息
    @Scheduled(cron = "0 9 0 * * ? ")
    @Async
    public void sendOpenBoxStatusTemplateMessages(){
        log.info("Scheduling Tasks sendTemplateMessages for all  OpenBoxStatus: The time is now " + dateFormat ().format (new Date()));
        List<Map<String,String>> shareRecordlist=shareRecordMapper.selectTargetIdsByUserIdOnDate();
        for(Map<String,String> map:shareRecordlist){
            String userId=map.get("userId");
            User userInfo=userMapper.selectByPrimaryKey(userId);
            String targetUserIds=map.get("targetUserIds");
            List<String> targetUserIdArr= Arrays.asList(targetUserIds);
            Map<String,Object> reqMap=new HashMap<>();
            List<Map<String,String>> targetUserInfos=userMapper.getUserInfoByUserId(targetUserIdArr);
            Calendar calendar=Calendar.getInstance();
            calendar.set(1,calendar.get(1)-1);
            Date openTime=calendar.getTime();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd");
            String openTimeStr=simpleDateFormat.format(openTime);
            for(Map<String,String> targetUser:targetUserInfos){
                String accessToken= Token.getToken();
                reqMap.put("access_token",accessToken);
                reqMap.put("touser",targetUser.get("openId"));
                reqMap.put("page","sharebox/sharebox?userId="+userId+"&effectiveTime="+openTime.toString());
                reqMap.put("form_id","");


                if(targetUserIdArr.size()>=3){
                    reqMap.put("template_id",OPERATE_SUCCESS_TEMPLATEID);
                    String data="{" +
                            "    \"keyword1\": {" +
                            "      \"value\": \""+targetUser.get("userName")+"\"\n" +
                            "    }," +
                            "    \"keyword2\": {" +
                            "      \"value\": \""+targetUser.get("chipCount")+"\"" +
                            "    }," +
                            "    \"keyword3\": {" +
                            "      \"value\": \"3枚\"" +
                            "    }," +
                            "    \"keyword4\": {" +
                            "      \"value\": \"开启宝箱获得金币\"" +
                            "    },"+
                            "    \"keyword5\": {" +
                            "      \"value\": \""+openTimeStr+" 23:59:59"+"\"" +
                            "    }";
                    reqMap.put("data",data);

                }else{
                    reqMap.put("template_id",OPERATE_FAIL_TEMPLATEID);
                    String data="{" +
                            "    \"keyword1\": {" +
                            "      \"value\": \""+targetUser.get("userName")+"\"\n" +
                            "    }," +
                            "    \"keyword2\": {" +
                            "      \"value\": \"分享开宝箱\"" +
                            "    }," +
                            "    \"keyword3\": {" +
                            "      \"value\": \"1小时内没有集齐3位好友帮您打开宝箱，真可惜今日宝箱打开失败，请明天再来~\"" +
                            "    }," +
                            "    \"keyword4\": {" +
                            "      \"value\": \""+openTimeStr+" 23:59:59"+"\"" +
                            "    }";
                    reqMap.put("data",data);

                }

                HttpUtil.doPost(sendTemplateMessageUrl,reqMap);

            }


            if(targetUserIdArr.size()>=3){
                reqMap.put("template_id",OPERATE_SUCCESS_TEMPLATEID);
                String data="{" +
                        "    \"keyword1\": {" +
                        "      \"value\": \""+userInfo.getNickName()+"\"\n" +
                        "    }," +
                        "    \"keyword2\": {" +
                        "      \"value\": \""+userInfo.getChipCount()+"\"" +
                        "    }," +
                        "    \"keyword3\": {" +
                        "      \"value\": \"10枚\"" +
                        "    }," +
                        "    \"keyword4\": {" +
                        "      \"value\": \"开启宝箱获得金币\"" +
                        "    },"+
                        "    \"keyword5\": {" +
                        "      \"value\": \""+openTimeStr+" 23:59:59"+"\"" +
                        "    }";
                reqMap.put("data",data);

            }else{
                reqMap.put("template_id",OPERATE_FAIL_TEMPLATEID);
                String data="{" +
                        "    \"keyword1\": {" +
                        "      \"value\": \""+userInfo.getNickName()+"\"\n" +
                        "    }," +
                        "    \"keyword2\": {" +
                        "      \"value\": \"分享开宝箱\"" +
                        "    }," +
                        "    \"keyword3\": {" +
                        "      \"value\": \"1小时内没有集齐3位好友帮您打开宝箱，真可惜今日宝箱打开失败，请明天再来~\"" +
                        "    }," +
                        "    \"keyword4\": {" +
                        "      \"value\": \""+openTimeStr+" 23:59:59"+"\"" +
                        "    }";
                reqMap.put("data",data);

            }

            HttpUtil.doPost(sendTemplateMessageUrl,reqMap);

        }

    }






    private SimpleDateFormat dateFormat(){
        return new SimpleDateFormat ("yyyy-dd-MM HH:mm:ss");
    }
    
}