package com.roche.connect.common.forte;

public enum ForteJobTypes {

	SECONDARY("secondary"), TERTIARY("tertiary");

	private final String text;

	ForteJobTypes(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
