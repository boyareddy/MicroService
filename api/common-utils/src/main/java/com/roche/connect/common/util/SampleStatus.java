package com.roche.connect.common.util;

public enum SampleStatus {

	COMPLETED("Completed"), INPROGRESS("InProgress"), ABORTED("Aborted"), PASSEDWITHFLAG("passed with flag"), PASSED(
			"Passed"), FLAGGED("flagged"), COMPLETED_WITH_FLAGS("Completed with flags"),FAILED("Failed"),PENDING("Pending");

	private final String text;

	SampleStatus(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

}
