package com.roche.connect.common.forte;

import java.util.List;

public class TertiaryJobDetailsDTO extends ForteJobDetailsDTO {

	private List<String> tertiary_infiles;

	private List<String> tertiary_checksums;

	private String tertiary_parameters;

	public List<String> getTertiary_infiles() {
		return tertiary_infiles;
	}

	public void setTertiary_infiles(List<String> tertiary_infiles) {
		this.tertiary_infiles = tertiary_infiles;
	}

	public List<String> getTertiary_checksums() {
		return tertiary_checksums;
	}

	public void setTertiary_checksums(List<String> tertiary_checksums) {
		this.tertiary_checksums = tertiary_checksums;
	}

	public String getTertiary_parameters() {
		return tertiary_parameters;
	}

	public void setTertiary_parameters(String tertiary_parameters) {
		this.tertiary_parameters = tertiary_parameters;
	}

}
