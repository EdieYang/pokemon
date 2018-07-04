package com.pokepet.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.dao.UserPetMapper;
import com.pokepet.model.PetAlbum;
import com.pokepet.model.UserPet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.Pet;
import com.pokepet.service.IPetManageService;

import javax.servlet.http.HttpServletRequest;

@ResponseResult
@RestController
@RequestMapping("/pet")
public class PetManageController {
	
	@Autowired
	IPetManageService petManageService;

//	@Autowired
//	private UserPetMapper userPetMapper;


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
	@RequestMapping(value = "/{petId}",method = RequestMethod.GET,consumes="application/json")
    public Pet getPet(@PathVariable String petId){
        return petManageService.getPetByPetId(petId);
    }
	
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
		String petId=petManageService.createPetId("010");//地区ID没传 @杨浩杰
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
	@RequestMapping(value = "/",method = RequestMethod.PUT,consumes="application/json")
    public boolean uptPet(@RequestBody Pet pet){
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
	
	@RequestMapping(value = "/petId",method = RequestMethod.GET,consumes="application/json")
	public JSONObject createPet(){
		String areaId = "010";
		JSONObject js = new JSONObject();
		js.put("petId", petManageService.createPetId(areaId));
        return js;
	}

}
