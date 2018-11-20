package com.pokepet.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.model.User;
import com.pokepet.model.UserTemp;

public interface IUserService {

	/**
	 * 生成用户ID，格式为"hu-03+区号+年份（后两位）+00001"
	 * 
	 * @param areaId
	 * @return
	 */
	String createUserId(String areaId);

	int modifyUser(User user);

	User getUserInfo(String userId);

	/**
	 * 根据查询条件获取用户列表（分页）
	 * @param param
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	JSONObject getUserList(Map<String, Object> param, int pageNum, int pageSize);

	UserTemp getTempUserByOpenId(String openId);

	boolean insertUserTemp(UserTemp userTemp);

	boolean insertUser(User user);

}
