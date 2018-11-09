package com.ytxiang.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class S3FileDTO implements Serializable {

	private static final long serialVersionUID = 2L;


	private int id;
	private String name;
	private String path;
	private String notes;
	private String fileSize;
	private Timestamp createdTime;
	private Timestamp modifiedTime;
	private String owner;

	public S3FileDTO() {
	}

	public S3FileDTO(int id, String name, String notes, String fileSize, String path, Timestamp createdTime,
			Timestamp modifiedTime, String owner) {
		super();
		this.id = id;
		this.name = name;
		this.notes = notes;
		this.fileSize = fileSize;
		this.path = path;
		this.createdTime = createdTime;
		this.modifiedTime = modifiedTime;
		this.owner = owner;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Timestamp modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "S3FileDTO [id=" + id + ", name=" + name + ", owner=" + owner + ", path=" + path + ", notes=" + notes
			+ ", fileSize=" + fileSize + ", createdTime=" + createdTime + ", modifiedTime=" + modifiedTime + "]";
	}
}
