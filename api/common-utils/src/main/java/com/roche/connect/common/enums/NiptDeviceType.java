package com.roche.connect.common.enums;

public enum NiptDeviceType {

	MP24("MP24"), LP24("LP24"), HTP("HTP"), FORTE("FORTE");

	private final String text;

	NiptDeviceType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
