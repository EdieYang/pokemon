package com.pokepet.dao;

import com.pokepet.model.UserPet;

public interface UserPetMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_pet
	 * @mbggenerated  Wed Jul 11 15:02:35 GMT+08:00 2018
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_pet
	 * @mbggenerated  Wed Jul 11 15:02:35 GMT+08:00 2018
	 */
	int insert(UserPet record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_pet
	 * @mbggenerated  Wed Jul 11 15:02:35 GMT+08:00 2018
	 */
	int insertSelective(UserPet record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_pet
	 * @mbggenerated  Wed Jul 11 15:02:35 GMT+08:00 2018
	 */
	UserPet selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_pet
	 * @mbggenerated  Wed Jul 11 15:02:35 GMT+08:00 2018
	 */
	int updateByPrimaryKeySelective(UserPet record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user_pet
	 * @mbggenerated  Wed Jul 11 15:02:35 GMT+08:00 2018
	 */
	int updateByPrimaryKey(UserPet record);
}