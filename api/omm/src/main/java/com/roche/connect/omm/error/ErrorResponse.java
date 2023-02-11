package com.roche.connect.omm.error;

public class ErrorResponse {
	private int errorStatusCode;
	private String errorMessage;

	public int getErrorStatusCode() {
		return errorStatusCode;
	}
	public void setErrorStatusCode(int errorStatusCode) {
		this.errorStatusCode = errorStatusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}



	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


}
