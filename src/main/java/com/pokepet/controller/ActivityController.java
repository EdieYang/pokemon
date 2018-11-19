package com.pokepet.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
import com.pokepet.model.ActActivityVote;
import com.pokepet.service.IActivityService;

@ResponseResult
@RestController
@RequestMapping("/activity")
public class ActivityController {

	static final int VOTE_COUNT_ONE_ACTIVITY_ONE_DAY = 3;

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
	public JSONObject getActivity(@PathVariable String id) {
		Map<String,Object> map=activityService.getActivityStatistics(id);
		ActActivity actActivity=activityService.getActivity(id);
		Date startDate=actActivity.getStartTime();
		Date endDate=actActivity.getEndTime();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime=simpleDateFormat.format(startDate);
		String endTime=simpleDateFormat.format(endDate);

		JSONObject jsonObject=new JSONObject();
		jsonObject.put("activity",actActivity);
		jsonObject.put("startTime",startTime);
		jsonObject.put("endTime",endTime);
		jsonObject.put("activityStatistics",map);
		return jsonObject;
	}
	
	@RequestMapping(value = "/saveActivity", method = RequestMethod.POST)
	public boolean saveActivity(@RequestBody ActActivity act){
		
		return activityService.saveActivity(act);
	}

	@RequestMapping(value = "/{id}/visit", method = RequestMethod.POST)
	public boolean getActivityVisitCount(@PathVariable String id,@RequestBody JSONObject data) {
		String userId=data.getString("userId");
		return activityService.countVisitorForAct(id,userId);
	}



	/**
	 * 活动报名
	 * 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public boolean registActivity(@RequestBody JSONObject data) {
		ActActivityRegister register = JSONObject.toJavaObject(data, ActActivityRegister.class);
		return activityService.saveActivityRegister(register);
	}

	/**
	 * 获取活动报名人列表
	 * 
	 * @param id
	 * @param search
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "{id}/registerList", method = RequestMethod.GET)
	public JSONObject registerList(@PathVariable String id, @RequestParam("userId") String userId,
			@RequestParam("search") String search, @RequestParam("pageNum") int pageNum,
			@RequestParam("pageSize") int pageSize) {
		return activityService.getActivityRegisterList(userId, search, id, pageNum, pageSize);
	}

	/**
	 * 获取参赛人信息，以及当前用户投票状态(true为已投票，false为未投票)
	 * 
	 * @param registerId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/register/{registerId}", method = RequestMethod.GET)
	public Map<String, Object> getRegister(@PathVariable String registerId, @RequestParam("userId") String userId,@RequestParam("activityId")String activityId) {
		Map<String,Object> map= activityService.getActivityRegister(registerId, userId);
		int rankNo=activityService.getRegisterRanking(activityId,registerId);
		map.put("rankNo",rankNo);
		return map;
	}

	/**
	 * 活动投票
	 * 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/vote", method = RequestMethod.POST)
	public boolean saveVote(@RequestBody JSONObject data) {
		ActActivityVote vote = JSONObject.toJavaObject(data, ActActivityVote.class);
		return activityService.saveVote(vote);
	}

	/**
	 * 检验用户是否有参与该活动，有为实体类返回，无为null
	 * 
	 * @param activityId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getUserActivity", method = RequestMethod.GET)
	public JSONObject getRegisterByActivityIdAndUserId(@RequestParam("activityId") String activityId,
			@RequestParam("userId") String userId) {
		ActActivityRegister register = activityService.getRegisterByActivityIdAndUserId(activityId, userId);
		if (null != register) {
			JSONObject js = JSONObject.parseObject(JSONObject.toJSONString(register));
			js.put("rankingNo", activityService.getRegisterRanking(activityId, register.getRegisterId()));
			return js;
		}
		return null;
	}

	/**
	 * 获取该用户今天该次活动剩余票数（默认为每天每人每次活动最多3票）
	 * @param activityId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/voteCount", method = RequestMethod.GET)
	public int getVoteCount(@RequestParam("activityId") String activityId, @RequestParam("userId") String userId) {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		int voteCount = activityService.getVoteCountByActivityIdAndUserIdAndDate(activityId, userId, df.format(now));
		if (VOTE_COUNT_ONE_ACTIVITY_ONE_DAY > voteCount) {
			return VOTE_COUNT_ONE_ACTIVITY_ONE_DAY - voteCount;
		} else {
			return 0;
		}
	}


}
