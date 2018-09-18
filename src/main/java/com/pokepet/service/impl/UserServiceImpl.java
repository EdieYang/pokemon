package com.pokepet.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.pokepet.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokepet.dao.UserMapper;
import com.pokepet.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	private static int userIdGrenc = -1;

	private static int year = -1;

	private final static String USER_ID_TEMP = "hu-03+区号+年份（后两位）+00001";

	@Autowired
	private UserMapper userMapper;

	@Override
	public String createUserId(String areaId) {
		Calendar cal = Calendar.getInstance();

		if (cal.get(Calendar.YEAR) != year) {
			year = cal.get(Calendar.YEAR);
			String maxId = userMapper.getMaxUserNo(USER_ID_TEMP.substring(0, 3), (year+"").substring(2));
			userIdGrenc = null == maxId ? 1 : Integer.parseInt(maxId)+1;
			return createUserId(areaId);
		} else {
			String userIdEnd = "000000" + userIdGrenc++;
			return USER_ID_TEMP.substring(0, 3) + areaId + ("" + year).substring(2, 4)
					+ userIdEnd.substring(userIdEnd.length() - 6, userIdEnd.length());
		}

	}

	@Override
	public int modifyUser(User user) {
		return userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public User getUserInfo(String userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public List<User> getUsers(Map<String,Object> map) {
		int pageNum=Integer.parseInt(String.valueOf(map.get("pageNumber")));
		int pageSize=Integer.parseInt(String.valueOf(map.get("pageSize")));
		PageHelper.startPage(pageNum,pageSize);
		List<User> users= userMapper.selectAllUsers(map);
		return users;
	}

	@Override
	public int getUsersCount(Map<String, Object> map) {
		return userMapper.selectAllUsersCount(map);
	}

}
