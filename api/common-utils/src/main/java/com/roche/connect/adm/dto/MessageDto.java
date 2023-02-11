package com.roche.connect.adm.dto;

import java.util.List;

public class MessageDto {
	
	private String messageGroup;
	private List<String> contents;
	private String locale;
	private String severity;
	
	public String getMessageGroup() {
		return messageGroup;
	}
	public void setMessageGroup(String group) {
		this.messageGroup = group;
	}
	public List<String> getContents() {
		return contents;
	}
	public void setContents(List<String> content) {
		this.contents = content;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String getSeverity() {
        return severity;
    }
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    @Override
	public String toString() {
		return "MessageDto [messageGroup=" + messageGroup + ", contents=" + contents + ", locale=" + locale +", severity=" + severity +"]";
	}
	
}
