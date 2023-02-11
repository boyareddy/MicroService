package com.roche.connect.common.mp24.message;

import java.io.Serializable;
import java.util.List;

public class AdaptorResponseMessage implements Serializable{
    
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String status;
	private List<String> errors;
	private Response response;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getError() {
		return errors;
	}

	public void setError(List<String> error) {
		this.errors = error;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

}
