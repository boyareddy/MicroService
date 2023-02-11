package com.roche.connect.common.amm.dto;

public class AssayListDataDTO {
	private String listType;
	private String value;
	private String assayType;
	
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	public String getListType() {
		return listType;
	}
	public void setListType(String listType) {
		this.listType = listType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
