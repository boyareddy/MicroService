package com.roche.connect.adm.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class Message {
	
	private long id;
	private String createdUser;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp createdDateTime;
	private String title;
	private String msg;
	private String userId;
	private String viewed;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp viewedDateTime;
	private String severity;
	private String topic;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String message) {
		this.msg = message;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getViewed() {
		return viewed;
	}
	public void setViewed(String viewed) {
		this.viewed = viewed;
	}
	public Timestamp getViewedDateTime() {
		return viewedDateTime;
	}
	public void setViewedDateTime(Timestamp viewedDateTime) {
		this.viewedDateTime = viewedDateTime;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	@Override
	public String toString() {
		return "Message [id=" + id + ", createdUser=" + createdUser + ", createdDateTime=" + createdDateTime + ", title="
				+ title + ", msg=" + msg + ", userId=" + userId + ", viewed=" + viewed + ", viewedDateTime="
				+ viewedDateTime + ", severity=" + severity + ", topic=" + topic + "]";
	}
	
}
