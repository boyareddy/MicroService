package com.roche.connect.common.htp;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class ComplexIdDetailsDTO {

	private String runProtocol;
	private ComplexIdDetailsStatus status;
	
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp complexCreatedDatetime;

	public enum ComplexIdDetailsStatus {
		READY("ready"), USED("used"), INVALID("invalid");

		private final String text;

		ComplexIdDetailsStatus(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}
	}

	public String getRunProtocol() {
		return runProtocol;
	}

	public void setRunProtocol(String runProtocol) {
		this.runProtocol = runProtocol;
	}

	public ComplexIdDetailsStatus getStatus() {
		return status;
	}

	public void setStatus(ComplexIdDetailsStatus status) {
		this.status = status;
	}

	public Timestamp getComplexCreatedDatetime() {
		return complexCreatedDatetime;
	}

	public void setComplexCreatedDatetime(Timestamp complexCreatedDatetime) {
		this.complexCreatedDatetime = complexCreatedDatetime;
	}

	@Override
	public String toString() {
		return "ComplexIdDetailsDTO [runProtocol=" + runProtocol + ", status=" + status + ", complexCreatedDatetime="
				+ complexCreatedDatetime + "]";
	}

}
