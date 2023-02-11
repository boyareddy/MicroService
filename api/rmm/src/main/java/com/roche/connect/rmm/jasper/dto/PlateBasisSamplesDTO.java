package com.roche.connect.rmm.jasper.dto;

import java.util.List;

public class PlateBasisSamplesDTO {
	private String plateId;
	private List<SamplesDataDTO> sampleDetails;
	public String getPlateId() {
		return plateId;
	}
	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}
	public List<SamplesDataDTO> getSampleDetails() {
		return sampleDetails;
	}
	public void setSampleDetails(List<SamplesDataDTO> sampleDetails) {
		this.sampleDetails = sampleDetails;
	}
}
