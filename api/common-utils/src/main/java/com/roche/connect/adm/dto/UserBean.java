package com.roche.connect.adm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBean {

	private long id;
	private String email;
	private String loginName;
	private String firstName;
	private boolean retired;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public boolean isRetired() {
		return retired;
	}
	public void setRetired(boolean retired) {
		this.retired = retired;
	}
	
	@Override
	public String toString() {
		return "UserBean [id=" + id + ", email=" + email + ", loginName=" + loginName + ", firstName=" + firstName
				+ ", retired=" + retired + "]";
	}
	
}
