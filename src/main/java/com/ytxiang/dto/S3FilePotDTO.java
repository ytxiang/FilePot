package com.ytxiang.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class S3FilePotDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	ArrayList<S3FileDTO> fileList;

	public S3FilePotDTO() {
	}

	public S3FilePotDTO(ArrayList<S3FileDTO> fileList) {
		super();
		this.fileList = fileList;
	}

	public ArrayList<S3FileDTO> getFileList() {
		return fileList;
	}

	public void setFileList(ArrayList<S3FileDTO> fileList) {
		this.fileList = fileList;
	}

	@Override
	public String toString() {
		return "S3FilePotDTO [fileList=" + fileList + "]";
	}

}
