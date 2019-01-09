package com.pokepet.controller;

import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.MessageQueue;
import com.pokepet.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Fade on 2018/10/7.
 */

@ResponseResult
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private IMessageService IMessageService;


    @RequestMapping(value = "/messagelist/{userId}",method = RequestMethod.GET)
    public List<Map<String,String>> getMessageList(@PathVariable("userId")String userId,
                                                   @RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize){

        List<Map<String,String>> messagelist= IMessageService.getMessageList(pageNum,pageSize,userId);
        return messagelist;
    }

    @RequestMapping(value = "/messageCount/{userId}",method = RequestMethod.GET)
    public int getMessageCount(@PathVariable("userId")String userId){
        MessageQueue messageQueue=new MessageQueue();
        messageQueue.setReceiverId(userId);
        messageQueue.setReadState("0");
        int count= IMessageService.getMessageCount(messageQueue);
        return count;
    }



    @RequestMapping(value = "/messageCount/{userId}",method = RequestMethod.POST)
    public int updateMessageCount(@PathVariable("userId")String userId){
        int count= IMessageService.updateMessageToRead(userId);
        return count;
    }


}
