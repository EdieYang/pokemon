package com.pokepet.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.UserDonate;
import com.pokepet.service.IDonateService;

@ResponseResult
@RestController
@RequestMapping("/donate")
public class DonateController {

	@Autowired
	IDonateService donateService;

	/**
	 * 捐赠
	 * 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/donate", method = RequestMethod.POST)
	public JSONObject donate(@RequestBody UserDonate data) {
		JSONObject result = new JSONObject();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", data.getUserId());
		param.put("donateActivity", data.getDonateActivity());
		param.put("donateType", data.getDonateType());
		UserDonate donate = donateService.getLastDonate(param);
		if (null == donate) {
			// 执行插入
			data.setDonateTime(new Date());
			donateService.insertRecord(data);
			result.put("success", true);
			result.put("msg", "捐赠成功！");
		} else {
			// 返回已捐赠
			result.put("success", false);
			result.put("msg", "您已捐赠，不能重复捐赠。");
		}
		return result;
	}

	/**
	 * 查询捐款记录
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/donateHistory", method = RequestMethod.GET)
	public JSONObject getDonate(@RequestParam("userId") String userId) {
		JSONObject result = new JSONObject();
		// 查询
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		
		result.put("success", true);
		result.put("data", donateService.getDonateList(param));
		return result;
	}
	
	/**
	 * 查询捐款统计
	 * @param donateActivity
	 * @return
	 */
	@RequestMapping(value = "/donateStatistics", method = RequestMethod.GET)
	public JSONObject getDonateStatistics(@RequestParam("donateActivity") String donateActivity) {
		JSONObject result = new JSONObject();
		// 查询
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("donateActivity", donateActivity);
		
		result.put("success", true);
		result.put("data", donateService.getDonateStatistics(param));
		return result;
	}

}
