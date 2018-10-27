package com.ytxiang.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String fullName;
	private String nick;
	private String userName;

	public UserDTO() {
	}

	public UserDTO(int id, String userName, String fullName) {
		this(id, userName, fullName, "");
	}

	public UserDTO(int id, String userName, String fullName, String nick) {
		super();
		this.id = id;
		this.userName = userName;
		this.fullName = fullName;
		this.nick = (nick == null) ? "" : nick;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}