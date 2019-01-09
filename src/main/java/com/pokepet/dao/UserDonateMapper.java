package com.pokepet.dao;

import java.util.List;
import java.util.Map;

import com.pokepet.model.UserDonate;

public interface UserDonateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_donate
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_donate
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    int insert(UserDonate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_donate
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    int insertSelective(UserDonate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_donate
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    UserDonate selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_donate
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    int updateByPrimaryKeySelective(UserDonate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_donate
     *
     * @mbg.generated Wed Jan 09 17:48:33 GMT+08:00 2019
     */
    int updateByPrimaryKey(UserDonate record);
    
    UserDonate getLastDonate(Map<String, Object> param);
	
	List<Map<String, Object>> getDonateList(Map<String, Object> param);
}