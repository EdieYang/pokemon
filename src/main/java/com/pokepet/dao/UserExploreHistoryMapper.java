package com.pokepet.dao;

import java.util.List;

import com.pokepet.model.UserExploreHistory;

public interface UserExploreHistoryMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_explore_history
	 * @mbggenerated  Wed Aug 08 11:36:47 GMT+08:00 2018
	 */
	int deleteByPrimaryKey(Integer historyId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_explore_history
	 * @mbggenerated  Wed Aug 08 11:36:47 GMT+08:00 2018
	 */
	int insert(UserExploreHistory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_explore_history
	 * @mbggenerated  Wed Aug 08 11:36:47 GMT+08:00 2018
	 */
	int insertSelective(UserExploreHistory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_explore_history
	 * @mbggenerated  Wed Aug 08 11:36:47 GMT+08:00 2018
	 */
	UserExploreHistory selectByPrimaryKey(Integer historyId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_explore_history
	 * @mbggenerated  Wed Aug 08 11:36:47 GMT+08:00 2018
	 */
	int updateByPrimaryKeySelective(UserExploreHistory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_explore_history
	 * @mbggenerated  Wed Aug 08 11:36:47 GMT+08:00 2018
	 */
	int updateByPrimaryKeyWithBLOBs(UserExploreHistory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_explore_history
	 * @mbggenerated  Wed Aug 08 11:36:47 GMT+08:00 2018
	 */
	int updateByPrimaryKey(UserExploreHistory record);
	
	List<UserExploreHistory> getExploreHistory(String userId);
	
	int getExploreCountForUserToday(String userId);
}