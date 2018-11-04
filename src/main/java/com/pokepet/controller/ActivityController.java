package com.pokepet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.ActActivity;
import com.pokepet.model.ActActivityRegister;
import com.pokepet.service.IActivityService;

@ResponseResult
@RestController
@RequestMapping("/activity")
public class ActivityController {

	@Autowired
	IActivityService activityService;

	/**
	 * 获取活动列表
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/activityList", method = RequestMethod.GET)
	public JSONObject getActivityList(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
		return activityService.getActivityList(pageNum, pageSize);
	}

	/**
	 * 获取活动详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ActActivity getActivity(@PathVariable String id) {
		return activityService.getActivity(id);
	}

	/**
	 * 活动报名
	 * 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public boolean registActivity(@RequestBody JSONObject data) {
		ActActivityRegister register = JSONObject.toJavaObject(data, ActActivityRegister.class);
		return activityService.saveActivityRegister(register);
	}

	/**
	 * 获取活动报名人列表
	 * @param id
	 * @param search
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "{id}/resigterList", method = RequestMethod.GET)
	public JSONObject resigterList(@PathVariable String id, @RequestParam("search") String search,
			@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
		return activityService.getActivityRegisterList(search, id, pageNum, pageSize);
	}
}
