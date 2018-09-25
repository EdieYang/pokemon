package com.pokepet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.UserLongRecord;
import com.pokepet.model.UserRecord;
import com.pokepet.service.IRecordService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Fade on 2018/9/11.
 */

@RestController
@RequestMapping("/record")
@ResponseResult
public class RecordController {

    @Autowired
    IRecordService recordService;


    @RequestMapping(value = "/longRecord/{userId}",method = RequestMethod.POST)
    public boolean  createPetRecord(HttpServletRequest request, @PathVariable("userId")String userId){
        String recordId=request.getParameter("recordId");
        String petId=request.getParameter("petId");
        String title=request.getParameter("title");
        String content=request.getParameter("content");
        String abbreImgArr=request.getParameter("abbreImgArr");
        String type=request.getParameter("type");

        //处理缩略图片
        String abbreImg="";
        JSONArray abbreList=JSONArray.parseArray(abbreImgArr);
        if(abbreList.size()>3){
            List<Object> abbreSubList=abbreList.subList(0,3);
            abbreImg=JSONArray.toJSONString(abbreSubList);
        }else{
            abbreImg=JSONArray.toJSONString(abbreList);
        }
        UserLongRecord record=new UserLongRecord();
        if(recordId!=null){ //编辑已有故事
            record.setRecordId(recordId);
            record.setTitle(title);
            record.setContent(content);
            record.setAbbreImg(abbreImg);
            record.setUserId(userId);
            record.setPetId(petId);
            record.setCreateTime(new Date());
            record.setCheckState("0");
            record.setDelFlag("0");
            record.setType(type);
            recordService.updateLongRecord(record);
        }else{
            record.setRecordId(UUID.randomUUID().toString());
            record.setTitle(title);
            record.setContent(content);
            record.setAbbreImg(abbreImg);
            record.setUserId(userId);
            record.setPetId(petId);
            record.setCreateTime(new Date());
            record.setCheckState("0");
            record.setDelFlag("0");
            record.setType(type);
            recordService.insertLongRecord(record);
        }

        return true;

    }

    /**
     * 上传短文
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(value = "/shortRecord/{userId}",method = RequestMethod.POST)
    public boolean  createUserRecord(HttpServletRequest request, @PathVariable("userId")String userId){
        String recordId=request.getParameter("recordId");
        String title=request.getParameter("title");
        String content=request.getParameter("content");
        String images=request.getParameter("images");
        String recommendState=request.getParameter("recommendState");
        String location=request.getParameter("location");
        String detailLocation=request.getParameter("detailLocation");
        String lat=request.getParameter("lat");
        String lng=request.getParameter("lng");
        String type=request.getParameter("type");


        UserRecord record=new UserRecord();
        if(recordId!=null){ //编辑已有故事
            record.setRecordId(recordId);
            record.setTitle(title);
            record.setContent(content);
            record.setImages(images);
            record.setUserId(userId);
            record.setCreateTime(new Date());
            record.setCheckState("0");
            record.setDelFlag("0");
            record.setRecommend(recommendState);
            record.setType(type);
            record.setLocation(location);
            record.setDetailLocation(detailLocation);
            record.setLat(lat);
            record.setLng(lng);
            recordService.updateRecord(record);
        }else{
            record.setRecordId(UUID.randomUUID().toString());
            record.setTitle(title);
            record.setContent(content);
            record.setImages(images);
            record.setUserId(userId);
            record.setCreateTime(new Date());
            record.setCheckState("0");
            record.setDelFlag("0");
            record.setRecommend(recommendState);
            record.setType(type);
            record.setLocation(location);
            record.setDetailLocation(detailLocation);
            record.setLat(lat);
            record.setLng(lng);
            recordService.insertRecord(record);
        }

        return true;

    }


    @RequestMapping(value = "/recommend",method = RequestMethod.GET)
    public List<Map<String,Object>> getRecommendList(HttpServletRequest request){

        int pageNum=request.getParameter("pageNum").equals(null)?0:Integer.parseInt(request.getParameter("pageNum"));
        int pageSize=request.getParameter("pageSize").equals(null)?0:Integer.parseInt(request.getParameter("pageSize"));
        String userId=request.getParameter("userId");
        List<Map<String,Object>> records=recordService.selectRecommendList(pageNum,pageSize,userId);
        for(Map recommendRecord:records){

            String type= (String) recommendRecord.get("type");
            String abbreImg=(String) recommendRecord.get("abbreImg");
            String images=(String) recommendRecord.get("images");
            switch (type){
                case "0": //故事长文截取文字内容

                    recommendRecord.put("abbreImg",JSON.parseArray(abbreImg));
                    recommendRecord.put("content",generateAbbreContent(recommendRecord));
                    break;
                case "1": //科普长文截取文字内容
                    recommendRecord.put("abbreImg",JSON.parseArray(abbreImg));
                    recommendRecord.put("content",generateAbbreContent(recommendRecord));
                    break;
                case "2": //短文截取文字内容
                    //页面显示逻辑
                    String content=recommendRecord.get("content").toString();
                    String abbrContent="";
                    String leftContent="";
                    boolean extend=false;
                    if(content.length()>40){
                        abbrContent= content.substring(0,40);
                        leftContent=content.substring(40);
                        extend=true;

                    }

                    recommendRecord.put("abbrContent",abbrContent);
                    recommendRecord.put("leftContent",leftContent);
                    recommendRecord.put("extend",extend);
                    recommendRecord.put("images",JSON.parseArray(images));
                    break;

            }

            SimpleDateFormat format=new SimpleDateFormat("MM-dd");
            recommendRecord.put("createtime",format.format(recommendRecord.get("createtime")));
        }

        return records;

    }




    @RequestMapping(value = "/charity",method = RequestMethod.GET)
    public List<Map<String,Object>> getCharityList(HttpServletRequest request){

        int pageNum=request.getParameter("pageNum").equals(null)?0:Integer.parseInt(request.getParameter("pageNum"));
        int pageSize=request.getParameter("pageSize").equals(null)?0:Integer.parseInt(request.getParameter("pageSize"));
        String userId=request.getParameter("userId");

        List<Map<String,Object>> records=recordService.selectCharityList(pageNum,pageSize,userId);
        for(Map petRecord:records){

            String type= (String) petRecord.get("type");
            String images=(String) petRecord.get("images");
            switch (type){
                case "3":
                    petRecord.put("images",JSON.parseArray(images));
                    String content2=petRecord.get("content").toString();
                    String abbrContent="";
                    String leftContent="";
                    boolean extend=false;
                    if(content2.length()>40){
                        abbrContent= content2.substring(0,40);
                        leftContent=content2.substring(40);
                        extend=true;

                    }

                    petRecord.put("abbrContent",abbrContent);
                    petRecord.put("leftContent",leftContent);
                    petRecord.put("extend",extend);
                    petRecord.put("images",JSON.parseArray(images));
                    break;
            }

            SimpleDateFormat format=new SimpleDateFormat("MM-dd");
            petRecord.put("createtime",format.format(petRecord.get("createtime")));
        }

        return records;

    }







    @RequestMapping(value = "/{userId}",method = RequestMethod.GET)
    public List<Map<String,Object>> getUserRecord(@PathVariable("userId")String userId,HttpServletRequest request){

        int pageNum=request.getParameter("pageNum").equals(null)?0:Integer.parseInt(request.getParameter("pageNum"));
        int pageSize=request.getParameter("pageSize").equals(null)?0:Integer.parseInt(request.getParameter("pageSize"));

        List<Map<String,Object>> records=recordService.selectUserRecordList(userId,pageNum,pageSize);
        for(Map petRecord:records){

            String type= (String) petRecord.get("type");
            String abbreImg=(String) petRecord.get("abbreImg");
            String images=(String) petRecord.get("images");
            switch (type){
                case "0": //故事长文截取文字内容

                    petRecord.put("abbreImg",JSON.parseArray(abbreImg));
                    petRecord.put("content",generateAbbreContent(petRecord));
                    break;
                case "1": //科普长文截取文字内容
                    petRecord.put("abbreImg",JSON.parseArray(abbreImg));
                    petRecord.put("content",generateAbbreContent(petRecord));
                    break;
                case "2": //短文截取文字内容
                    String content=petRecord.get("content").toString();
                    if(content.length()>40){
                        content= content.substring(0,40)+"...";
                    }
                    petRecord.put("images",JSON.parseArray(images));
                    petRecord.put("content",content);
                    break;
                case "3":
                    petRecord.put("images",JSON.parseArray(images));
                    String content2=petRecord.get("content").toString();
                    if(content2.length()>40){
                        content2= content2.substring(0,40)+"...";
                    }
                    petRecord.put("content",content2);
                    break;
            }

            SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
            petRecord.put("createtime",format.format(petRecord.get("createtime")));
        }

        return records;

    }



    private String generateAbbreContent(Map map){
        JSONArray contentItem= JSON.parseArray(String.valueOf(map.get("content")));
        String abbrContent="";
        for(Object jsonObject:contentItem){
            JSONObject item=JSON.parseObject(jsonObject.toString());
            if(item.getString("type").equals("0")){
                String content= String.valueOf(item.get("data"));
                if(content.length()>40){
                    abbrContent= content.substring(0,40)+"...";
                }else{
                    abbrContent=content;
                }
                break;
            }
        }
        return abbrContent;
    }




    @RequestMapping(value = "/repo/{recordId}",method = RequestMethod.GET)
    public Map<String,Object> getRepoRecordDetail(@PathVariable("recordId")String recordId,HttpServletRequest request){

        String type=request.getParameter("type");
        String userId=request.getParameter("userId");
        Map<String,Object> recordDetail=new HashMap<>();

        switch (type){
            case "0": //故事长文
                recordDetail=recordService.selectLongRecordByRecordId(recordId,userId);
                break;
            case "1": //科普长文
                recordDetail=recordService.selectLongRecordByRecordId(recordId,userId);
                break;
            case "2": //短文截取文字内容
                recordDetail=recordService.selectRecordByRecordId(recordId);
                break;
            case "3":
                recordDetail=recordService.selectRecordByRecordId(recordId);
                break;
        }

        if(recordDetail!=null){
            SimpleDateFormat format=new SimpleDateFormat("yyy-MM-dd hh:mm");
            recordDetail.put("createtime",format.format(recordDetail.get("createtime")));
        }

        return recordDetail;

    }


    @RequestMapping(value = "/like",method = RequestMethod.POST)
    public boolean recordLike(HttpServletRequest request){
        String userId=request.getParameter("userId");
        String recordId=request.getParameter("recordId");
        boolean flag=recordService.updateRecordLike(userId,recordId);
        return flag;

    }


    @RequestMapping(value = "/collect",method = RequestMethod.POST)
    public boolean recordCollect(HttpServletRequest request){
        String userId=request.getParameter("userId");
        String recordId=request.getParameter("recordId");
        boolean flag=recordService.updateRecordCollect(userId,recordId);
        return flag;

    }


}
