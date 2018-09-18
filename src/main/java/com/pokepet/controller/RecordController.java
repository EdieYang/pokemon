package com.pokepet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.dao.PetRecordMapper;
import com.pokepet.model.PetRecord;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Fade on 2018/9/11.
 */

@RestController
@RequestMapping("/record")
@ResponseResult
public class RecordController {

    @Autowired
    private PetRecordMapper recordMapper;

    @RequestMapping(value = "/{petId}",method = RequestMethod.POST)
    public boolean  createRecord(HttpServletRequest request, @PathVariable("petId")String petId){
        String recordId=request.getParameter("recordId");
        String userId=request.getParameter("userId");
        String title=request.getParameter("title");
        String content=request.getParameter("content");
        String abbreImgArr=request.getParameter("abbreImgArr");

        //处理缩略图片
        String abbreImg="";
        JSONArray abbreList=JSONArray.parseArray(abbreImgArr);
        if(abbreList.size()>3){
            List<Object> abbreSubList=abbreList.subList(0,3);
            abbreImg=JSONArray.toJSONString(abbreSubList);
        }else{
            abbreImg=JSONArray.toJSONString(abbreList);
        }
        PetRecord record=new PetRecord();
        if(recordId!=null){ //编辑已有故事
            record.setRecordId(recordId);
            record.setTitle(title);
            record.setContent(content);
            record.setAbbreImg(abbreImg);
            record.setUserId(userId);
            record.setPetId(petId);
            record.setCreateTime(new Date());
            record.setCheckState("0");
            record.setDelState("0");
            recordMapper.updateByPrimaryKeySelective(record);
        }else{
            record.setRecordId(UUID.randomUUID().toString());
            record.setTitle(title);
            record.setContent(content);
            record.setAbbreImg(abbreImg);
            record.setUserId(userId);
            record.setPetId(petId);
            record.setCreateTime(new Date());
            record.setCheckState("0");
            record.setDelState("0");
            recordMapper.insertSelective(record);
        }

        return true;

    }


    @RequestMapping(value = "/user/{userId}",method = RequestMethod.GET)
    public List<PetRecord> getUserRecord(@PathVariable("userId")String userId){
        List<PetRecord> records=recordMapper.getUserRecordsByUserId(userId);
        for(PetRecord petRecord:records){
            JSONArray contentItem= JSON.parseArray(petRecord.getContent());
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
            petRecord.setContent(abbrContent);
            SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
            petRecord.setShowTime(format.format(petRecord.getCreateTime()));
        }

        return records;

    }





}
