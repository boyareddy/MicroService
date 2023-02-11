package com.roche.connect.common.enums;

public enum AssayType {

	NIPT("NIPTHTP");

	private final String text;

	AssayType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
