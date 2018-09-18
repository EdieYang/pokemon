package com.pokepet.service;

import com.pokepet.model.User;

import java.util.List;
import java.util.Map;

public interface IUserService {
	
	/**
	 * 生成用户ID，格式为"hu-03+区号+年份（后两位）+00001"
	 * @param areaId
	 * @return
	 */
	String createUserId(String areaId);

	int modifyUser(User user);

	User getUserInfo(String userId);

	List<User> getUsers(Map<String,Object> map);

	int getUsersCount(Map<String,Object> map);

}
