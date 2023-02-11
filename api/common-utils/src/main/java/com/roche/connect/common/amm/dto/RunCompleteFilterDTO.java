package com.roche.connect.common.amm.dto;

public class RunCompleteFilterDTO {

	private String assayType;
	private String sampleType;
	private String deviceType;
	private String workflowStep;

	public String getWorkflowStep() {
		return workflowStep;
	}

	public void setWorkflowStep(String workflowStep) {
		this.workflowStep = workflowStep;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assayType == null) ? 0 : assayType.hashCode());
		result = prime * result + ((deviceType == null) ? 0 : deviceType.hashCode());
		result = prime * result + ((sampleType == null) ? 0 : sampleType.hashCode());
		result = prime * result + ((workflowStep == null) ? 0 : workflowStep.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RunCompleteFilterDTO other = (RunCompleteFilterDTO) obj;
		if (assayType == null) {
			if (other.assayType != null)
				return false;
		} else if (!assayType.equals(other.assayType))
			return false;
		if (deviceType == null) {
			if (other.deviceType != null)
				return false;
		} else if (!deviceType.equals(other.deviceType))
			return false;
		if (sampleType == null) {
			if (other.sampleType != null)
				return false;
		} else if (!sampleType.equals(other.sampleType))
			return false;
		if (workflowStep == null) {
			if (other.workflowStep != null)
				return false;
		} else if (!workflowStep.equals(other.workflowStep))
			return false;
		return true;
	}

	
}
