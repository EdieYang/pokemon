package com.pokepet.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class RedisAspect {


	@Autowired
	private RedisTemplate<String,String> redisTemplate;



	private static final Logger log = LoggerFactory.getLogger(RedisAspect.class);

	// 关注宠物切点
	@Pointcut("execution(public * com.pokepet.controller.RecordController.createUserRecord(..))")
	public void updateCharityTypeRecord() {
	}


	@Around("updateCharityTypeRecord()")
	public Object aroundUpdateCharityTypeRecordPointCut(ProceedingJoinPoint pjp) {
		log.info("{RedisAspect} =updateCharity start.....");

		try {
			Object o = pjp.proceed();
			log.info("方法环绕proceed，结果是 :" + o);
			if (null != o) {
				JSONObject js = JSONObject.parseObject(JSONObject.toJSONString(o));
				String type=js.getString("type");
				boolean operateResult=js.getBooleanValue("operateResult");
				String operateType=js.getString("operateType");
				String recordId=js.getString("recordId");
				String lat=js.getString("lat");
				String lng=js.getString("lng");
				String city=js.getString("city");
				String content=js.getString("content");
				//创建寻宠信息时新增地图信息
				if(type.equals("3") && operateType.equals("insert") && operateResult){
					JSONObject contentObj=JSON.parseObject(content);
					ListOperations<String, String> oper = redisTemplate.opsForList();
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("recordId",recordId);
					jsonObject.put("lat",lat);
					jsonObject.put("lng",lng);
					jsonObject.put("city",city);
					jsonObject.put("emergencyType",contentObj.getString("emergencyType"));
					String popTitle="";
					if(contentObj.getString("emergencyType").equals("1")){
						popTitle=contentObj.getString("petName");
					}else if(contentObj.getString("emergencyType").equals("2")){
						popTitle=contentObj.getString("speciesType");
					}
					jsonObject.put("popTitle",popTitle);
					oper.leftPush("emergencyMap:"+city, JSON.toJSONString(jsonObject));
					//修改寻宠信息时更新地图信息
				}else if(type.equals("3") && operateType.equals("update") && operateResult){
					ListOperations<String, String> oper = redisTemplate.opsForList();
					//获取缓存信息
					List<String> emergencyList=oper.range("emergencyMap:"+city,0,-1);
					for(String emergency:emergencyList){
						JSONObject eoj=JSON.parseObject(emergency);
						String eoRecordId=eoj.getString("recordId");
						if(recordId.equals(eoRecordId)){
							//移除原有缓存
							oper.remove("emergencyMap:"+city,0,emergency);
							break;
						}
					}

					//更新缓存记录
					JSONObject contentObj=JSON.parseObject(content);
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("recordId",recordId);
					jsonObject.put("lat",lat);
					jsonObject.put("lng",lng);
					jsonObject.put("city",city);
					jsonObject.put("emergencyType",contentObj.getString("emergencyType"));
					String popTitle="";
					if(contentObj.getString("emergencyType").equals("1")){
						popTitle=contentObj.getString("petName");
					}else if(contentObj.getString("emergencyType").equals("2")){
						popTitle=contentObj.getString("speciesType");
					}
					jsonObject.put("popTitle",popTitle);
					oper.leftPush("emergencyMap:"+city, JSON.toJSONString(jsonObject));
				}
			}
			return o;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

}