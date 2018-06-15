package com.pokepet.service;

import java.io.InputStream;

public interface IFtpService {
	
	public void upLoadImg(String path);
	
	public String uploadFile(String fileName,String fileType,InputStream inputStream);

}
