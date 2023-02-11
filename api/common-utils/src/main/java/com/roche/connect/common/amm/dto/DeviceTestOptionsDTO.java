package com.roche.connect.common.amm.dto;

public class DeviceTestOptionsDTO {
	private String testName;
	private String testProtocol;
	private String deviceType;
	private String assayType;
	private String processStepName;
	private Long testOptionId;
	private String analysisPackageName;
	
	
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	public String getTestName() {
		return testName;
	}
	public String getProcessStepName() {
		return processStepName;
	}
	public void setProcessStepName(String processStepName) {
		this.processStepName = processStepName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public Long getTestOptionId() {
		return testOptionId;
	}
	public void setTestOptionId(Long testOptionId) {
		this.testOptionId = testOptionId;
	}
	public String getTestProtocol() {
		return testProtocol;
	}

	public void setTestProtocol(String testProtocol) {
		this.testProtocol = testProtocol;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getAnalysisPackageName() {
		return analysisPackageName;
	}
	public void setAnalysisPackageName(String analysisPackageName) {
		this.analysisPackageName = analysisPackageName;
	}
	
	
}
