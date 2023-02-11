package com.roche.connect.common.mp96;

import java.io.Serializable;
import java.util.List;

public class WFMQueryResponseMessage implements Serializable{
    
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String status;
	private List<String> errors;
	
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
	
}
