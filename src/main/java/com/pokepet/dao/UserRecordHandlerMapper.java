package com.pokepet.dao;

/**
 * Created by Fade on 2018/9/18.
 */


import java.util.List;
import java.util.Map;

/**
 * 用户分享软文处理接口
 */
public interface UserRecordHandlerMapper {

    List<Map<String,Object>> selectUserRecordList(String userId);

    List<Map<String,Object>> selectRecommendList();

    List<Map<String,Object>> selectCharityList();

}
