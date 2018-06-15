package com.pokepet.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pokepet.annotation.ResponseResult;
import com.pokepet.service.IFtpService;

@ResponseResult
@RestController
public class FileController {

	@Autowired
	IFtpService ftpService;

	@PostMapping
	@RequestMapping(value = "/upload",method = RequestMethod.POST)
	String upload(@RequestParam("file") MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();
		
		String fileType = "";
		if(fileName != null){
			fileType =file.getContentType();
		}
        InputStream inputStream=file.getInputStream();
		return ftpService.uploadFile(fileName, fileType, inputStream);
	}

}
