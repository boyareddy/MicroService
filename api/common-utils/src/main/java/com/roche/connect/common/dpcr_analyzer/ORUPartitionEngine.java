package com.roche.connect.common.dpcr_analyzer;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ORUPartitionEngine implements Serializable {

	private static final long serialVersionUID = 1L;
	private String plateId;
	private String serialNumber;
	private String fluidId;
	private String dateandTime;

	public String getPlateId() {
		return plateId;
	}

	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getFluidId() {
		return fluidId;
	}

	public void setFluidId(String fluidId) {
		this.fluidId = fluidId;
	}

	public String getDateandTime() {
		return dateandTime;
	}

	public void setDateandTime(String dateandTime) {
		this.dateandTime = dateandTime;
	}

}
