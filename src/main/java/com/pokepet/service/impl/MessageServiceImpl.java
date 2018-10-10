package com.pokepet.service.impl;

import com.github.pagehelper.PageHelper;
import com.pokepet.dao.MessageQueueMapper;
import com.pokepet.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Fade on 2018/10/7.
 */

@Service
public class MessageServiceImpl implements IMessageService {


    @Autowired
    private MessageQueueMapper messageQueueMapper;

    @Override
    public List<Map<String, String>> getMessageList(int pageNum, int pageSize, String userId) {
        PageHelper.startPage(pageNum,pageSize);
        return messageQueueMapper.getMessageList(userId);
    }
}
