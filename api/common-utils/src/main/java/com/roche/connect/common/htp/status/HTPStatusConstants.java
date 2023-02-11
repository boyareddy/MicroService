package com.roche.connect.common.htp.status;

public enum HTPStatusConstants {

	CREATED("Created"), STARTED("Started"), IN_PROCESS("InProcess"), COMPLETED("Completed"), FAILED("Failed"), IN_PROGRESS("InProgress"), TRANSFERCOMPLETE("TransferCompleted");

	private final String text;

	HTPStatusConstants(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
