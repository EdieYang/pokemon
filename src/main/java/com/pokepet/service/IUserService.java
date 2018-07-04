package com.pokepet.service;

public interface IUserService {
	
	/**
	 * 生成用户ID，格式为"hu-03+区号+年份（后两位）+00001"
	 * @param areaId
	 * @return
	 */
	String createUserId(String areaId);

}
