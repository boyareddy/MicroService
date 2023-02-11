package com.roche.connect.common.forte;

import java.util.List;
import java.util.Map;

public class SecondaryJobDetailsDTO extends ForteJobDetailsDTO {

	private String secondaryInfile;
	private String secondaryChecksum;
	private List<Map<String, String>> secondarySampleDetails;

	public List<Map<String, String>> getSecondarySampleDetails() {
		return secondarySampleDetails;
	}

	public void setSecondarySampleDetails(List<Map<String, String>> secondarySampleDetails) {
		this.secondarySampleDetails = secondarySampleDetails;
	}

	public String getSecondaryInfile() {
		return secondaryInfile;
	}

	public void setSecondaryInfile(String secondaryInfile) {
		this.secondaryInfile = secondaryInfile;
	}

	public String getSecondaryChecksum() {
		return secondaryChecksum;
	}

	public void setSecondaryChecksum(String secondaryChecksum) {
		this.secondaryChecksum = secondaryChecksum;
	}

}
