package com.roche.connect.common.enums;

public enum ProcessStepName {

	NA_EXTRACTION("NA Extraction"), SEQUENCING("Sequencing"), LP_SEQ_PP("LP-SEQ-PP");

	private final String text;

	ProcessStepName(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
