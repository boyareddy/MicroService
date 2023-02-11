package com.roche.connect.common.forte;

public enum ForteJobStatus {

	START("Start"), IN_PROGRESS("InProgress"), DONE("Done"), ERROR("Error");

	private final String text;

	ForteJobStatus(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
