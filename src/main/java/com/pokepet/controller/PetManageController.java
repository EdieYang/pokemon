package com.pokepet.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.pokepet.model.*;
import com.pokepet.service.IPetLikeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.service.IPetManageService;
import com.pokepet.service.IPetSupplyService;
import com.pokepet.service.IPetWeaponService;
import com.pokepet.util.CommonUtil;

@ResponseResult
@RestController
@RequestMapping("/pet")
public class PetManageController {
	
	@Autowired
	IPetManageService petManageService;

//	@Autowired
//	private UserPetMapper userPetMapper;
	
	@Autowired
	IPetWeaponService petWeaponService;
	
	@Autowired
	IPetSupplyService petSupplyService;

	@Autowired
	IPetLikeService petLikeService;


	private final static String PIC_SUFFIX=".png";
	
	/**
	 * 
	 * @Description: 根据petId获取宠物信息
	 * @param @param petId
	 * @param @return   
	 * @return Pet  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月24日
	 */
	@RequestMapping(value = "/{petId}",method = RequestMethod.GET)
    public Pet getPet(@PathVariable String petId){
        Pet pet= petManageService.getPetByPetId(petId);
		 pet.setAge(CommonUtil.getAgeByBirthday(pet.getBirthday()));//设置年龄
		 return pet;
	}


	
	/**
	 * 获取宠物相册
	 * @param petId
	 * @return
	 */
	@RequestMapping(value = "/{petId}/album",method = RequestMethod.GET,consumes="application/json")
    public List<PetAlbum> getPetAlbum(@PathVariable String petId){
        return petManageService.getPetAlbumByPetId(petId);
    }
	
	/**
	 * 
	 * @Description: 新增宠物
	 * @param @param pet
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月24日
	 */
	@PostMapping
	@RequestMapping(value = "",method = RequestMethod.POST)
    public JSONObject addPet(HttpServletRequest request){
		JSONObject jsonObject=new JSONObject();
		String petId=petManageService.createPetId("010");//地区ID没传
		String species=request.getParameter("species");
		String name=request.getParameter("name");
		String sex=request.getParameter("sex");
		String birthday=request.getParameter("birthday");
		String weight=request.getParameter("weight");
		String sterilization=request.getParameter("sterilization");
		String level=request.getParameter("level");
		String exp=request.getParameter("exp");
		String delFlag=request.getParameter("delFlag");
		String userId=request.getParameter("userId");
		String memo=request.getParameter("memo");


		Pet pet=new Pet();
		//转换生日格式
		SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd");
		try {
			Date birthDate=format.parse(birthday);
			pet.setBirthday(birthDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		pet.setPetId(petId);
		pet.setSex(sex);
		pet.setSpecies(species);
		pet.setName(name);
		pet.setWeight(weight);
		pet.setPhotoPath(petId+PIC_SUFFIX); //默认png格式
		pet.setSterilization(sterilization);
		pet.setExp(Integer.valueOf(exp));
		pet.setLevel(Integer.valueOf(level));
		pet.setMemo(memo);
		pet.setDelFlag(delFlag);

		boolean returnFlag=petManageService.addPet(pet);
		if(!returnFlag){
			jsonObject.put("code",returnFlag);
			return jsonObject;
		}

		//绑定用户宠物关系
		petManageService.bindlingPetToUser(petId, userId);

		jsonObject.put("code",returnFlag);
		jsonObject.put("petId",petId);
       return jsonObject;
    }
	
	/**
	 * 
	 * @Description: 修改宠物
	 * @param @param pet
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月24日
	 */
	@PostMapping
	@RequestMapping(value = "/modify",method = RequestMethod.POST,consumes="application/json")
    public boolean uptPet(@RequestBody JSONObject data){
		Pet pet=JSONObject.toJavaObject(data, Pet.class);
		return petManageService.uptPet(pet);
    }
	
	/**
	 * 
	 * @Description: 删除宠物
	 * @param @param petId
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author Bean Zhou
	 * @date 2018年5月24日
	 */
	@PostMapping
	@RequestMapping(value = "/{petId}",method = RequestMethod.DELETE,consumes="application/json")
    public boolean uptPet(@PathVariable String petId){
        return petManageService.delPet(petId);
    }
	
	/**
	 * 创建宠物
	 * @return
	 */
	@RequestMapping(value = "/petId",method = RequestMethod.GET,consumes="application/json")
	public JSONObject createPet(){
		String areaId = "010";
		JSONObject js = new JSONObject();
		js.put("petId", petManageService.createPetId(areaId));
        return js;
	}
	
	/**
	 * 设置宠物装备
	 * @param concat
	 */
	@RequestMapping(value = "/setPetWeapon",method = RequestMethod.POST,consumes="application/json")
	public void setPetWeapon(@RequestBody PetWeaponConcat concat){
		petWeaponService.setPetWeaponConcat(concat);
	}
	
	/**
	 * 使用补给
	 * @param concat
	 */
	@RequestMapping(value = "/useSupply",method = RequestMethod.POST,consumes="application/json")
	public void useSupply(@RequestBody JSONObject data){
		String petId = data.getString("petId");
		String id = data.getString("id");
		petSupplyService.useSupply(petId, id);
	}

	@RequestMapping(value = "/setPetEnergyCoin" ,method = RequestMethod.POST,consumes="application/json")
	public void setPetWeapon(@RequestBody JSONObject data){
		String petId = data.getString("petId");
		String energyCoin=data.getString("energyCoin");
		if(!StringUtils.isEmpty(energyCoin)){
			//获取最新活力值并进行校验
			Pet petRecord=petManageService.getPetByPetId(petId);
			int energyCoinInt=Integer.parseInt(energyCoin);
			Pet pet=new Pet();
			pet.setPetId(petId);
			pet.setEnergyCoin(energyCoinInt);
			if(petRecord!=null){
				int egCoinOld=petRecord.getEnergyCoin();
				if(egCoinOld<energyCoinInt){
					petManageService.uptPet(pet);
				}
			}

		}
	}

	/**
	 * 宠物装备
	 * @param petId
	 * @return
     */
	@RequestMapping(value = "/{petId}/weaponRepo",method = RequestMethod.GET,consumes = "application/json")
	public JSONObject getPetWeaponRepo(@PathVariable String petId){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("weapons",petWeaponService.getWeaponByPetId(petId));
		return jsonObject;
	}


	@RequestMapping(value = "/{petId}/like",method = RequestMethod.POST,consumes = "application/json")
	public JSONObject addPetsLike(@PathVariable String petId,@RequestBody JSONObject data){
		JSONObject jsonObject=new JSONObject();
		String userId=data.getString("userId");
		int petLikeCount=petLikeService.getPetLikeCountByUserId(petId,userId);
		if(petLikeCount==0){
			//增加爱心
			PetLike petLike=new PetLike();
			petLike.setPetId(petId);
			petLike.setUserId(userId);
			petLike.setCreateDatetime(new Date());
			petLikeService.addPetLike(petLike);
			jsonObject.put("success",true);
		}else{
			jsonObject.put("success",false);
		}
		return jsonObject;
	}



}
