package com.roche.connect.adm.dto;

import java.util.List;

public class MessageTemplate {
	
	private long id;
	private String activeFlag;
	private String channel;
	private String title;
	private String description;
	private String severity;
	private Integer priority;
	private String locale;
	private String messageGroup;
	private List<MessageRecepient> messageRecepients;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getMessageGroup() {
		return messageGroup;
	}
	public void setMessageGroup(String messageGroup) {
		this.messageGroup = messageGroup;
	}
	public List<MessageRecepient> getMessageRecepients() {
		return messageRecepients;
	}
	public void setMessageRecepients(List<MessageRecepient> messageRecepients) {
		this.messageRecepients = messageRecepients;
	}
	
	@Override
	public String toString() {
		return "MessageTemplate [id=" + id + ", activeFlag=" + activeFlag + ", channel=" + channel + ", title=" + title
				+ ", description=" + description + ", severity=" + severity + ", priority=" + priority + ", locale="
				+ locale + ", messageGroup=" + messageGroup + ", messageRecepients=" + messageRecepients + "]";
	}
}
