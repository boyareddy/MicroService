package com.camel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleTypeDTO {
	String id;
	String name;
	String value;
	String encodingSystem;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getEncodingSystem() {
		return encodingSystem;
	}
	public void setEncodingSystem(String encodingSystem) {
		this.encodingSystem = encodingSystem;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
