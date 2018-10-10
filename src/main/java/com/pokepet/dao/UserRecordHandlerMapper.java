package com.pokepet.dao;

/**
 * Created by Fade on 2018/9/18.
 */


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户分享软文处理接口
 */
public interface UserRecordHandlerMapper {

    List<Map<String,Object>> selectUserRecordList(String userId);

    List<Map<String,Object>> selectUserCollectRecordList(String userId);

    List<Map<String,Object>> selectUserCheckedRecordList(String userId);

    List<Map<String,Object>> selectRecommendList(String userId);

    List<Map<String,Object>> selectCharityList(String userId);

    List<Map<String,Object>> getPetRecordList(String petId);

    Map<String,Object> selectLongRecordByRecordId(@Param("recordId") String recordId, @Param("userId") String userId);

    Map<String,Object> selectRecordByRecordId(String recordId);


}
