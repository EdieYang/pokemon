package com.pokepet.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pokepet.util.UUIDUtils;
import com.pokepet.util.wxConfig.Token;
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

	private final static String activityPicObjNamePrefix="ac_html/";

	private final static String onlineImagePrefix="https://melody.memorychilli.com/";

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
				break;
			case "record" :
				objName+="/pet/record/"+petId+"/"+UUID.randomUUID();
				break;
			case "recordK":
				objName+="/explore/recordK/"+UUID.randomUUID();
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

			if(type.equals("record") || type.equals("recordK")){
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

	public static void generateImage(String fileObjName,InputStream inputStream){

		// 创建OSSClient实例。
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

		try {

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
		}
	}


	@PostMapping
	@RequestMapping(value = "/uploadFile/image",method = RequestMethod.POST)
	public JSONObject uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) throws IOException {
		JSONObject resJson=new JSONObject();
		String objName="";
		//存储对象实例命名
		switch (type){
			case "activity": objName+=activityPicObjNamePrefix+"acPic/activity/"+ UUIDUtils.randomUUID();
				break;
			case "logo":objName+=activityPicObjNamePrefix+"acPic/logo/"+ UUIDUtils.randomUUID();
				break;
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

			resJson.put("returnUrl",onlineImagePrefix+fileObjName);
			return resJson;
		}

	}






	@RequestMapping("/savePicture")
	public static Map<String,Object> savePicture(@RequestParam("mediaId") String mediaId){
		Map<String,Object> map=new HashMap<>();
		String fileObjName=null;
		String url = "https://api.weixin.qq.com/cgi-bin/media/get";
		String accessToken=Token.getToken();
		String params = "access_token=" + accessToken + "&media_id=" + mediaId;
		InputStream inputStream = null;
		// 创建OSSClient实例。
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		HttpURLConnection http=null;
		try {
			String urlNameString = url + "?" + params;
			System.out.println(urlNameString);
			URL urlGet = new URL(urlNameString);
			http= (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			// 获取文件转化为byte流
			inputStream = http.getInputStream();
			String picId=UUID.randomUUID().toString();
			fileObjName=activityPicObjNamePrefix+"acPic/"+picId+getFileexpandedName(http.getContentType());
			client.putObject(bucketName,fileObjName,inputStream);

		} catch (OSSException oe) {
			System.out.println("Error Message: " + oe.getErrorCode());
		} catch (ClientException ce) {
			System.out.println("Error Message: " + ce.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (client != null) {
				client.shutdown();
			}
			if(http!=null){
				http.disconnect();
			}
			map.put("fileObjName",fileObjName);
			return map;
		}


	}



	/**
	 * 根据内容类型判断文件扩展名
	 *
	 * @param contentType 内容类型
	 * @return
	 */
	public static String getFileexpandedName(String contentType) {
		String fileEndWitsh = "";
		if ("image/jpeg".equals(contentType))
			fileEndWitsh = ".jpg";
		else if ("audio/mpeg".equals(contentType))
			fileEndWitsh = ".mp3";
		else if ("audio/amr".equals(contentType))
			fileEndWitsh = ".amr";
		else if ("video/mp4".equals(contentType))
			fileEndWitsh = ".mp4";
		else if ("video/mpeg4".equals(contentType))
			fileEndWitsh = ".mp4";
		return fileEndWitsh;
	}




}
