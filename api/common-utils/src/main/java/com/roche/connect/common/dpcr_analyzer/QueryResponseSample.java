package com.roche.connect.common.dpcr_analyzer;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResponseSample implements Serializable {

	private static final long serialVersionUID = 1L;

	private String accessioningId;
	private String containerId;
	private String position;
	private String status;
	private String plateIntegator;
	private List<Assay> assay;

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPlateIntegator() {
		return plateIntegator;
	}

	public void setPlateIntegator(String plateIntegator) {
		this.plateIntegator = plateIntegator;
	}

	public List<Assay> getAssay() {
		return assay;
	}

	public void setAssay(List<Assay> assay) {
		this.assay = assay;
	}

}
