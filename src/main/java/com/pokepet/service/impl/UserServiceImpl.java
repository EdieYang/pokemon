package com.pokepet.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.pokepet.dao.UserTempMapper;
import com.pokepet.model.UserTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pokepet.dao.UserMapper;
import com.pokepet.model.User;
import com.pokepet.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	private static int userIdGrenc = -1;

	private static int year = -1;

	private final static String USER_ID_TEMP = "hu-03+区号+年份（后两位）+00001";

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserTempMapper userTempMapper;

	@Override
	public String createUserId(String areaId) {
		Calendar cal = Calendar.getInstance();

		if (cal.get(Calendar.YEAR) != year) {
			year = cal.get(Calendar.YEAR);
			String maxId = userMapper.getMaxUserNo(USER_ID_TEMP.substring(0, 3), (year + "").substring(2));
			userIdGrenc = null == maxId ? 1 : Integer.parseInt(maxId) + 1;
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
	public JSONObject getUserList(Map<String, Object> param, int pageNum, int pageSize) {
		JSONObject result = new JSONObject();
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = userMapper.selectUserList(param);
		PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
		result.put("page", page.getPageNum());
		result.put("records", page.getTotal());
		result.put("rows", list);
		return result;
	}

	@Override
	public UserTemp getTempUserByOpenId(String openId) {
		return userTempMapper.getTempUserByOpenId(openId);
	}

	@Override
	public boolean insertUserTemp(UserTemp userTemp) {
		return userTempMapper.insertSelective(userTemp)>0;
	}

	@Override
	public boolean insertUser(User user) {
		return  userMapper.insertSelective(user)>0;
	}

}
