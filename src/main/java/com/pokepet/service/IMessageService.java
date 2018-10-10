package com.pokepet.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Fade on 2018/10/7.
 */
public interface IMessageService {

    List<Map<String,String>> getMessageList(int pageNum,int pageSize,String userId);

}
