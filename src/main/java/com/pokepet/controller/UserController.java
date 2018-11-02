package com.pokepet.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.pokepet.model.*;
import com.pokepet.service.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.enums.PetLevelEnum;
import com.pokepet.enums.StarSignEnum;
import com.pokepet.util.CommonUtil;

@ResponseResult
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	IWalkService walkService;

	@Autowired
	IPetManageService petManageService;

	@Autowired
	IPetLikeService petLikeService;

	@Autowired
	IUserService userService;

	@Autowired
	IPetWeaponService petWeaponService;

	@Autowired
	IPetSupplyService petSupplyService;

	@Autowired
	IUserFollowService userFollowService;

	@Autowired
	IRecordService recordService;

	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	public JSONObject getUserList(@RequestParam("search") String search, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		return userService.getUserList(param, pageNum, pageSize);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.POST, consumes = "application/json")
	public void modifyUser(@PathVariable String userId, @RequestBody User user) {
		user.setUserId(userId);
		userService.modifyUser(user);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET, consumes = "application/json")
	public JSONObject getUserInfo(@PathVariable String userId) {
		User user = userService.getUserInfo(userId);
		int followAmount = userFollowService.getUserFollowAmount(userId);
		int followedAmount = userFollowService.getUserFollowedAmount(userId);
		int collectAmount= recordService.getUserCollectRecordAmount(userId);
		JSONObject userInfo = new JSONObject();
		userInfo.put("user", user);
		userInfo.put("fanAmount", followedAmount);
		userInfo.put("followAmount", followAmount);
		userInfo.put("collectAmount",collectAmount);

		return userInfo;
	}

	@RequestMapping(value = "/{userId}/follow", method = RequestMethod.GET, consumes = "application/json")
	public JSONObject getFollowUser(@PathVariable String userId, HttpServletRequest request) {
		String followUserId = request.getParameter("followUserId");
		UserFollow userFollowParam = new UserFollow();
		userFollowParam.setFollowUserId(followUserId);
		userFollowParam.setUserId(userId);
		UserFollow follow = userFollowService.selectFollowedUser(userFollowParam);

		JSONObject userInfo = new JSONObject();
		userInfo.put("success", true);
		userInfo.put("data", follow);
		return userInfo;
	}

	@RequestMapping(value = "/{userId}/follow", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject followUser(@PathVariable String userId,@RequestBody JSONObject data) {
		String followUserId = data.getString("followUserId");
		Map<String, String> map = new HashMap<>();
		map.put("userId", userId);
		map.put("followUserId", followUserId);
		boolean result = userFollowService.addFollowRelation(map);

		JSONObject userInfo = new JSONObject();
		userInfo.put("success", result);
		return userInfo;
	}

	@RequestMapping(value = "/{userId}/cancelFollow", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject cancelFollowUser(@PathVariable String userId,@RequestBody JSONObject data) {
		String followUserId = data.getString("followUserId");
		Map<String, String> map = new HashMap<>();
		map.put("userId", userId);
		map.put("followUserId", followUserId);
		boolean result = userFollowService.deleteUserFollow(map);

		JSONObject userInfo = new JSONObject();
		userInfo.put("success", result);
		return userInfo;
	}

	@RequestMapping(value = "/{userId}/pets", method = RequestMethod.GET, consumes = "application/json")
	public JSONArray getPets(@PathVariable String userId) {
		// 获取某用户下的宠物
		List<Pet> list = petManageService.getPetListByUserId(userId);
		JSONArray arr = new JSONArray();
		for (Pet p : list) {
			JSONObject pet = JSONObject.parseObject(JSONObject.toJSONString(p));
			pet.put("nextLevelExp", PetLevelEnum.getValue(p.getLevel() + 1));
			pet.put("starSign", StarSignEnum.getName(p.getBirthday()));// 设置星座
			pet.put("age", CommonUtil.getAgeByBirthday(p.getBirthday()));// 设置年龄
			// 步行里程
			pet.put("walkDistance", walkService.getWalkDistanceByPetId(p.getPetId()));
			pet.put("expCanvas", "");
			// 点赞数
			pet.put("likeCount", petLikeService.getLikeCountByPetId(p.getPetId()));

			// 装备栏装备
			pet.put("weapon", petWeaponService.getWeaponByPetId(p.getPetId()));

			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			pet.put("birthdayFormat", format.format(p.getBirthday()));
			pet.put("petAlbum", petManageService.getPetAlbumByPetId(p.getPetId()));

			arr.add(pet);
		}
		return arr;
	}

	/**
	 *
	 * @Description: 用户walk开始，创建记录
	 * @param @param
	 *            history
	 * @param @return
	 * @return boolean
	 * @throws @author
	 *             Bean Zhou
	 * @date 2018年6月5日
	 */
	@PostMapping
	@RequestMapping(value = "/{userId}/walk/start", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject startWalk(@RequestBody UserWalkHistory history) {
		String historyId = UUID.randomUUID().toString();// 生成historyId
		history.setHistoryId(historyId);// 设置historyId
		history.setStartTime(new Date());// 设置开始walk时间
		walkService.addHistory(history);

		JSONObject result = new JSONObject();
		result.put("historyId", historyId);
		return result;
	}

	/**
	 *
	 * @Description: 用户walk结束，返回经验值、活力值
	 * @param @param
	 *            history
	 * @param @return
	 * @return boolean
	 * @throws @author
	 *             Bean Zhou
	 * @date 2018年6月5日
	 */
	@PostMapping
	@RequestMapping(value = "/{userId}/walk/finish", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject finishWalk(@RequestBody UserWalkHistory history) {

		history.setFinishTime(new Date());

		return walkService.finishWalk(history);
	}

	/**
	 *
	 * @Description: 保存用户walk坐标
	 * @param @param
	 *            location
	 * @param @return
	 * @return boolean
	 * @throws @author
	 *             Bean Zhou
	 * @date 2018年6月5日
	 */
	@PostMapping
	@RequestMapping(value = "/{userId}/walk/location", method = RequestMethod.POST, consumes = "application/json")
	public boolean addLocation(@RequestBody UserWalkLocation location) {
		return walkService.addLocation(location);
	}

	/**
	 *
	 * @Description: 获取用户walk历史记录
	 * @param @param
	 *            userId
	 * @param @return
	 * @return List<UserWalkHistory>
	 * @throws @author
	 *             Bean Zhou
	 * @date 2018年6月5日
	 */
	@RequestMapping(value = "/{userId}/walk/history", method = RequestMethod.GET, consumes = "application/json")
	public List<UserWalkHistory> getWalkHistory(@PathVariable String userId) {
		return walkService.getHistoryListByUserId(userId);
	}

	@RequestMapping(value = "/{userId}/walk/location/{historyId}", method = RequestMethod.GET, consumes = "application/json")
	public List<UserWalkLocation> getWalkLocation(@PathVariable String historyId) {
		return walkService.getLocationListByHistoryId(historyId);
	}

	/**
	 * 获取用户某一次walk记录及轨迹
	 *
	 * @Description: TODO
	 * @param @param
	 *            historyId
	 * @param @return
	 * @return Map<String,Object>
	 * @throws @author
	 *             Bean Zhou
	 * @date 2018年6月5日
	 */
	@RequestMapping(value = "/{userId}/walk/history/{historyId}", method = RequestMethod.GET, consumes = "application/json")
	public Map<String, Object> getCurrentWalk(@PathVariable String historyId) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("hisotory", walkService.getHistoryByHistoryId(historyId));
		result.put("locationList", walkService.getLocationListByHistoryId(historyId));
		return result;
	}

	@RequestMapping(value = "/userId", method = RequestMethod.GET, consumes = "application/json")
	public JSONObject createUser() {
		String areaId = "010";
		JSONObject js = new JSONObject();
		js.put("userId", userService.createUserId(areaId));
		return js;
	}

	/**
	 * 获取用户周围walk的其他用户位置
	 *
	 * @Description: TODO
	 * @param @param
	 *            userId * @param @param longitude * @param @param latitude
	 * @param @return
	 * @return Map<String,Object>
	 * @throws @author
	 *             Bean Zhou
	 * @date 2018年6月5日
	 */
	@RequestMapping(value = "/{userId}/walk/around", method = RequestMethod.GET)
	public List<Map<String, Object>> getWalkAround(@PathVariable String userId,
												   @RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude) {
		return walkService.getWalkAround(userId, longitude, latitude);
	}

	/**
	 * 获取用户装备
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/{userId}/weponList", method = RequestMethod.GET)
	public List<Map<String, Object>> getWeaponList(@PathVariable String userId) {
		return petWeaponService.getWeaponByUserId(userId);
	}

	/**
	 * 获取用户补给
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/{userId}/supplyList", method = RequestMethod.GET)
	public List<Map<String, Object>> getSupplyList(@PathVariable String userId) {
		return petSupplyService.getSupplyByUserId(userId);
	}
}
