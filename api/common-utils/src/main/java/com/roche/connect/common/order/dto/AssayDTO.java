package com.roche.connect.common.order.dto;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class AssayDTO {
	
	private Long patientAssayid;
	private Integer maternalAge;
	private Integer gestationalAgeWeeks;
	private Integer gestationalAgeDays;
	private String ivfStatus;
	private Integer eggDonorAge;
	private String eggDonor;
	private String fetusNumber;	
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp collectionDate;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp receivedDate;
	private Map<String,Boolean> testOptions = new HashMap<>();
	
	
	public void setTestOptions(Map<String, Boolean> testOptions) {
		this.testOptions = testOptions;
	}
	public Map<String, Boolean> getTestOptions() {
		return testOptions;
	}
	public Timestamp getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Timestamp receivedDate) {
		this.receivedDate = receivedDate;
	}
	public Long getPatientAssayid() {
		return patientAssayid;
	}
	public void setPatientAssayid(Long patientAssayid) {
		this.patientAssayid = patientAssayid;
	}
	public Integer getMaternalAge() {
		return maternalAge;
	}
	public void setMaternalAge(Integer maternalAge) {
		this.maternalAge = maternalAge;
	}
	public Integer getGestationalAgeWeeks() {
		return gestationalAgeWeeks;
	}
	public void setGestationalAgeWeeks(Integer gestationalAgeWeeks) {
		this.gestationalAgeWeeks = gestationalAgeWeeks;
	}
	public Integer getGestationalAgeDays() {
		return gestationalAgeDays;
	}
	public void setGestationalAgeDays(Integer gestationalAgeDays) {
		this.gestationalAgeDays = gestationalAgeDays;
	}
	public String getIvfStatus() {
		return ivfStatus;
	}
	public void setIvfStatus(String ivfStatus) {
		this.ivfStatus = ivfStatus;
	}
	public String getEggDonor() {
		return eggDonor;
	}
	public void setEggDonor(String eggDonor) {
		this.eggDonor = eggDonor;
	}
	public String getFetusNumber() {
		return fetusNumber;
	}
	public void setFetusNumber(String fetusNumber) {
		this.fetusNumber = fetusNumber;
	}
	public Timestamp getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(Timestamp collectionDate) {
		this.collectionDate = collectionDate;
	}
	public Integer getEggDonorAge() {
		return eggDonorAge;
	}
	public void setEggDonorAge(Integer eggDonorAge) {
		this.eggDonorAge = eggDonorAge;
	}	
	

}
