package com.pokepet.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.dao.PetAlbumMapper;
import com.pokepet.dao.PetMapper;
import com.pokepet.model.Pet;
import com.pokepet.model.PetAlbum;

@ResponseResult
@RestController
public class FileController {



	private static final String endpoint="oss-cn-hangzhou.aliyuncs.com";

	private static final String accessKeyId="LTAIKclZeHcK1wEi";

	private static final String accessKeySecret="JptvG66RV6MWa5DPJFecDWxoMDQotb";

	private static final String bucketName="linkpet-image-bucket-1";

//	private static final String petsPortrait="petsPortrait/state.png";

	private final static String portraitObjNamePrefix="images/";


	@Autowired
	private PetMapper petMapper;

	@Autowired
	private PetAlbumMapper petAlbumMapper;



	@PostMapping
	@RequestMapping(value = "/pet/portrait/upload",method = RequestMethod.POST)
	public JSONObject uploadPetPortrait(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
		JSONObject resJson=new JSONObject();
		String 	userId=request.getParameter("userId");
		String petId= request.getParameter("petId");
		String type=request.getParameter("type");
		String objName=portraitObjNamePrefix+userId;
		//存储对象实例命名
		switch (type){
			case "portrait": objName+="/pet/portrait/p_"+petId;
				break;
			case "mapPortrait": objName+="/pet/portrait/m_"+petId;
				break;
			case "album" :
				objName+="/pet/album/"+petId+"/"+ UUID.randomUUID();
			case "record" :
				objName+="/pet/record/"+petId+"/"+UUID.randomUUID();
				break;
			case "explore" :
				objName+="/explore/"+UUID.randomUUID();
			default:break;
		}

		//拼文件格式

		if(file==null || file.isEmpty()){
			resJson.put("returnUrl",null);
			return resJson;
		}

		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		String fileObjName=objName+suffix;

		// 创建OSSClient实例。
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);


		try {

			// 上传文件流。
			InputStream inputStream = file.getInputStream();

			client.putObject(bucketName,fileObjName,inputStream);
		} catch (OSSException oe) {
			System.out.println("Caught an OSSException, which means your request made it to OSS, "
					+ "but was rejected with an error response for some reason.");
			System.out.println("Error Message: " + oe.getErrorCode());
			System.out.println("Error Code:       " + oe.getErrorCode());
			System.out.println("Request ID:      " + oe.getRequestId());
			System.out.println("Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			System.out.println("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ce.getMessage());
		} finally {

			if (client != null) {
				client.shutdown();
			}

			//如果不是png格式图片更新宠物头像路径

			if(type.equals("portrait") && !suffix.equals(".png")){
				Pet petRecord=new Pet();
				petRecord.setPetId(petId);
				petRecord.setPhotoPath(fileObjName);
				petMapper.updateByPrimaryKeySelective(petRecord);
			}

			if(type.equals("album")){
				PetAlbum petAlbum=new PetAlbum();
				petAlbum.setAlbumId(UUID.randomUUID().toString());
				petAlbum.setUserId(userId);
				petAlbum.setPetId(petId);
				petAlbum.setPhotoPath(fileObjName);
				petAlbum.setDelFlag("0");
				petAlbumMapper.insertSelective(petAlbum);
			}

			if(type.equals("record")){
				//返回图片链接
				resJson.put("returnPicUrl",fileObjName);
				return resJson;
			}

			if(type.equals("explore")){
				//返回图片链接
				resJson.put("returnPicUrl",fileObjName);
				return resJson;
			}


			resJson.put("returnUrl",petId);
			return resJson;
		}

	}

}
