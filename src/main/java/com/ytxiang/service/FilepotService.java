package com.ytxiang.service;

import java.io.IOException;
import javax.xml.bind.ValidationException;

import org.springframework.web.multipart.MultipartFile;

import com.ytxiang.bean.UserBean;
import com.ytxiang.dto.S3FileDTO;
import com.ytxiang.dto.S3FilePotDTO;
import com.ytxiang.dto.UserDTO;

public interface FilepotService {
	UserDTO getUser(String userName);
	public void signUpUser(UserBean userbean) throws ValidationException;
	public S3FilePotDTO getFileList(String userName);
	public S3FileDTO upload(MultipartFile mfile, String notes, String userName)
			throws IllegalStateException, IOException, ValidationException;
	public S3FileDTO upload(MultipartFile mfile, Integer fid, String userName, String notes)
			throws IllegalStateException, IOException, ValidationException;
	public void deleteFile(Integer fileId, String userName);
}
