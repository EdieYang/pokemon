package com.pokepet.service;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.model.UserLongRecord;
import com.pokepet.model.UserRecord;

import java.util.List;
import java.util.Map;

/**
 * Created by Fade on 2018/9/19.
 */
public interface IRecordService {


    int updateLongRecord(UserLongRecord longRecord);

    int insertLongRecord(UserLongRecord longRecord);

    int updateRecord(UserRecord userRecord);

    int insertRecord(UserRecord userRecord);

    int getUserCollectRecordAmount(String userId);


    List<Map<String,Object>> selectUserRecordList(String userId,int pageNum,int pageSize);

    List<Map<String,Object>> selectUserCheckedRecordList(String userId,int pageNum,int pageSize);



    List<Map<String,Object>> getCollectRecordList(int pageNum,int pageSize,String userId);

    List<Map<String,Object>> selectRecommendList(int pageNum,int pageSize,String userId);

    List<Map<String,Object>> selectCharityList(int pageNum,int pageSize,String userId);

    List<Map<String,Object>> getPetRecordList(int pageNum,int pageSize,String petId);

    Map<String,Object> selectLongRecordByRecordId(String recordId,String userId);

    Map<String,Object> selectRecordByRecordId(String recordId);

    boolean updateRecordLike(String userId,String recordId);

    boolean updateRecordCollect(String userId,String recordId);


    /**
     * 根据查询条件获取短文列表（分页）
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    JSONObject getRecordList(Map<String, Object> param, int pageNum, int pageSize);


    /**
     * 根据查询条件获取长文列表（分页）
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    JSONObject getLongRecordList(Map<String, Object> param, int pageNum, int pageSize);
    
    /**
     * 获取长文
     * @param recordId
     * @return
     */
    UserLongRecord getLongRecord(String recordId);
    
    /**
     * 更新长文
     * @param record
     * @return
     */
    boolean uptLongRecord(UserLongRecord record);
    
    /**
     * 获取短文
     * @param recordId
     * @return
     */
    UserRecord getShortRecord(String recordId);
    
    /**
     * 更新短文
     * @param record
     * @return
     */
    boolean uptShortRecord(UserRecord record);
}
