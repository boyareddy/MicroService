package com.roche.connect.common.forte;

public class ForteJobStatusUpdateDTO {

	private String jobStatus;
	private ForteJobTypes jobType;
	private String complexId;

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public ForteJobTypes getJobType() {
		return jobType;
	}

	public void setJobType(ForteJobTypes jobType) {
		this.jobType = jobType;
	}

	public String getComplexId() {
		return complexId;
	}

	public void setComplexId(String complexId) {
		this.complexId = complexId;
	}

}
