package com.roche.connect.common.order.dto;

import java.util.List;

public class TranslationDTO {
	
	private String messageGroup;
	private String messageContent;
	private List<String> values;
	
	
	public String getMessageGroup() {
		return messageGroup;
	}
	public void setMessageGroup(String messageGroup) {
		this.messageGroup = messageGroup;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}

	
	
}
