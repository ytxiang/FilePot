package com.ytxiang.bean;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadForm {

    private String notes;

    private MultipartFile file;

    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public MultipartFile getFile() {
	return file;
    }

    public void setFile(MultipartFile file) {
	this.file = file;
    }

}