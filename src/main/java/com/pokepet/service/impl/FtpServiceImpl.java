//package com.pokepet.service.impl;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.pokepet.service.IFtpService;
//
//import cpm.pokepet.util.FtpUtil;
//
//@Service
//public class FtpServiceImpl implements IFtpService {
//
//	// ftp服务器ip地址
//	@Value("${ftpAddress}")
//	private String ftpAddress;
//	// 端口号
//	@Value("${ftpPort}")
//	private int ftpPort;
//	// 用户名
//	@Value("${ftpUserName}")
//	private String ftpUserName;
//	// 密码
//	@Value("${ftpPassWord}")
//	private String ftpPassWord;
//	// 文件路径
//	@Value("${ftpBasePath}")
//	private String ftpBasePath;
//
//	@Override
//	public void upLoadImg(String path) {
//		// TODO Auto-generated method stub
//		InputStream inputStream = null;
//		try {
//			inputStream = new FileInputStream(path);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		Boolean flag = FtpUtil.uploadFile("xiaocao.txt", inputStream, ftpAddress, ftpPort, ftpUserName, ftpPassWord,
//				ftpBasePath);
//		if (flag == true) {
//			System.out.println("ftp上传成功！");
//		}
//
//	}
//
//	@Override
//	public String uploadFile(String fileName, String fileType, InputStream inputStream) {
//		switch (fileType) {
//		case "jpg":
//			ftpBasePath = "image";
//			break;
//
//		case "mp4":
//			ftpBasePath = "vedio";
//			break;
//
//		case "doc":
//			ftpBasePath = "doc";
//			break;
//
//		case "mp3":
//			ftpBasePath = "img";
//			break;
//
//		default:
//			ftpBasePath = "other";
//			break;
//		}
//
//		if (FtpUtil.uploadFile(fileName, inputStream, ftpAddress, ftpPort, ftpUserName, ftpPassWord, ftpBasePath)) {
//			return "ftp：//" + ftpAddress + ":" + ftpPort + "/" + ftpBasePath + "/" + fileName;
//		} else {
//			return null;
//		}
//
//	}
//
//}
