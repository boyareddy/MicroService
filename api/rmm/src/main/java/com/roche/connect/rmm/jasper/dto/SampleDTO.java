package com.roche.connect.rmm.jasper.dto;

import java.util.List;

import com.roche.connect.rmm.jasper.dto.SamplesDataDTO;

public class SampleDTO {
	private String plateId;
	private List<SamplesDataDTO> listOfSampleDataDTO;

	public String getPlateId() {
		return plateId;
	}

	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}

	public List<SamplesDataDTO> getListOfSampleDataDTO() {
		return listOfSampleDataDTO;
	}

	public void setListOfSampleDataDTO(List<SamplesDataDTO> listOfSampleDataDTO) {
		this.listOfSampleDataDTO = listOfSampleDataDTO;
	}

	
}
