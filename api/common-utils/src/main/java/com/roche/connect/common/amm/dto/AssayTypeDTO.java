package com.roche.connect.common.amm.dto;

public class AssayTypeDTO {
	private Long assayTypeId;
	private String assayType;
	private String assayVersion;
	private String workflowDefFile;
	private String workflowDefVersion;
	
	public Long getAssayTypeId() {
		return assayTypeId;
	}
	public void setAssayTypeId(Long assayTypeId) {
		this.assayTypeId = assayTypeId;
	}
	
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	
	public String getAssayVersion() {
		return assayVersion;
	}
	public void setAssayVersion(String assayVersion) {
		this.assayVersion = assayVersion;
	}
	public String getWorkflowDefFile() {
		return workflowDefFile;
	}
	public void setWorkflowDefFile(String workflowDefFile) {
		this.workflowDefFile = workflowDefFile;
	}
	public String getWorkflowDefVersion() {
		return workflowDefVersion;
	}
	public void setWorkflowDefVersion(String workflowDefVersion) {
		this.workflowDefVersion = workflowDefVersion;
	}
	
}
