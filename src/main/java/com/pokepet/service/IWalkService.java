package com.pokepet.service;

import java.util.List;

import com.pokepet.model.UserWalkHistory;
import com.pokepet.model.UserWalkLocation;

public interface IWalkService {
	
	/**
	 * 
	 * @Description: 创建散步记录
	 * @param @param record
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月28日
	 */
	boolean addHistory(UserWalkHistory record);
	
	/**
	 * 
	 * @Description: 新增定位信息
	 * @param @param record
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月28日
	 */
	boolean addLocation(UserWalkLocation record);
	
	/**
	 * 
	 * @Description: 根据用户id获取散步历史记录
	 * @param @param userId
	 * @param @return   
	 * @return List<UserWalkHistory>  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月28日
	 */
	List<UserWalkHistory> getHistoryListByUserId(String userId);
	
	/**
	 * 
	 * @Description: 根据historyId获取散步记录
	 * @param @param historyId
	 * @param @return   
	 * @return UserWalkHistory  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月28日
	 */
	UserWalkHistory getHistoryByHistoryId(String historyId);
	
	/**
	 * 
	 * @Description: 根据历史记录id获取轨迹坐标
	 * @param @param historyId
	 * @param @return   
	 * @return List<UserWalkLocation>  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月28日
	 */
	List<UserWalkLocation> getLocationListByHistoryId(String historyId);
	
	/**
	 * 
	 * @Description: 获取用户累计walk距离
	 * @param @param usersId
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年6月5日
	 */
	int getWalkDistanceByUserId(String usersId);
	
	/**
	 * 
	 * @Description: 获取pet累计walk距离
	 * @param @param petId
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年6月5日
	 */
	int getWalkDistanceByPetId(String petId);
	

}
