package com.roche.connect.rmm.jasper.dto;

import java.sql.Timestamp;
import java.util.List;

public class DeviceRunResultsDTO {
	private String comments;
    private String assayType;
	private Long numOfSamples;
	private String qcResult;
	private String samplesPassedQC;
	private Timestamp completionDate;
	private List<NAExtractionDTO> naExtractionData;
	private List<LibraryPrepDTO> libraryPrepData;
	private List<DPCRDTO> dpcrData;
	private List<QCResultsDTO> qcResultsDTO;
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public Long getNumOfSamples() {
		return numOfSamples;
	}

	public void setNumOfSamples(Long numOfSamples) {
		this.numOfSamples = numOfSamples;
	}

	public String getSamplesPassedQC() {
		return samplesPassedQC;
	}
	
	public void setSamplesPassedQC(String samplesPassedQC) {
		this.samplesPassedQC = samplesPassedQC;
	}
	
	public Timestamp getCompletionDate() {
		return completionDate;
	}
	
	public void setCompletionDate(Timestamp completionDate) {
		this.completionDate = completionDate;
	}

	public String getQcResult() {
		return qcResult;
	}

	public void setQcResult(String qcResult) {
		this.qcResult = qcResult;
	}

	public List<NAExtractionDTO> getNaExtractionData() {
		return naExtractionData;
	}

	public void setNaExtractionData(List<NAExtractionDTO> naExtractionData) {
		this.naExtractionData = naExtractionData;
	}

	public List<LibraryPrepDTO> getLibraryPrepData() {
		return libraryPrepData;
	}

	public void setLibraryPrepData(List<LibraryPrepDTO> libraryPrepData) {
		this.libraryPrepData = libraryPrepData;
	}

	public List<QCResultsDTO> getQcResultsDTO() {
		return qcResultsDTO;
	}

	public void setQcResultsDTO(List<QCResultsDTO> qcResultsDTO) {
		this.qcResultsDTO = qcResultsDTO;
	}

	public List<DPCRDTO> getDpcrData() {
		return dpcrData;
	}

	public void setDpcrData(List<DPCRDTO> dpcrData) {
		this.dpcrData = dpcrData;
	}
	
}
