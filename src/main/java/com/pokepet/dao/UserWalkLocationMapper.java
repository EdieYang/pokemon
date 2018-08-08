package com.pokepet.dao;

import java.util.List;

import com.pokepet.model.UserWalkLocation;

public interface UserWalkLocationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_walk_location
     *
     * @mbggenerated Mon May 28 17:12:56 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_walk_location
     *
     * @mbggenerated Mon May 28 17:12:56 CST 2018
     */
    int insert(UserWalkLocation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_walk_location
     *
     * @mbggenerated Mon May 28 17:12:56 CST 2018
     */
    int insertSelective(UserWalkLocation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_walk_location
     *
     * @mbggenerated Mon May 28 17:12:56 CST 2018
     */
    UserWalkLocation selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_walk_location
     *
     * @mbggenerated Mon May 28 17:12:56 CST 2018
     */
    int updateByPrimaryKeySelective(UserWalkLocation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_walk_location
     *
     * @mbggenerated Mon May 28 17:12:56 CST 2018
     */
    int updateByPrimaryKey(UserWalkLocation record);
    
	List<UserWalkLocation> getLocationListByHistoryId(String historyId);
}