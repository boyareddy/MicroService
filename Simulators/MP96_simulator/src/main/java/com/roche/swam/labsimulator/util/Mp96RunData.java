package com.roche.swam.labsimulator.util;

import java.util.List;

public class Mp96RunData {

	private String runId;
	private String runComments;
	private String deviceRunId;
	private String operatorName;
	private String deviceId;
	private String resultMessageType;
	private String resultMessageEvent;
	private String ProcessingId;
	

	
	private List<Mp96SampleData> samples;


	/**
	 * @return the runId
	 */
	public String getRunId() {
		return runId;
	}


	/**
	 * @param runId the runId to set
	 */
	public void setRunId(String runId) {
		this.runId = runId;
	}


	/**
	 * @return the runComments
	 */
	public String getRunComments() {
		return runComments;
	}


	/**
	 * @param runComments the runComments to set
	 */
	public void setRunComments(String runComments) {
		this.runComments = runComments;
	}


	/**
	 * @return the deviceRunId
	 */
	public String getDeviceRunId() {
		return deviceRunId;
	}


	/**
	 * @param deviceRunId the deviceRunId to set
	 */
	public void setDeviceRunId(String deviceRunId) {
		this.deviceRunId = deviceRunId;
	}




	/**
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}


	/**
	 * @param operatorName the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}


	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}


	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}



	/**
	 * @return the resultMessageType
	 */
	public String getResultMessageType() {
		return resultMessageType;
	}


	/**
	 * @param resultMessageType the resultMessageType to set
	 */
	public void setResultMessageType(String resultMessageType) {
		this.resultMessageType = resultMessageType;
	}


	/**
	 * @return the resultMessageEvent
	 */
	public String getResultMessageEvent() {
		return resultMessageEvent;
	}


	/**
	 * @param resultMessageEvent the resultMessageEvent to set
	 */
	public void setResultMessageEvent(String resultMessageEvent) {
		this.resultMessageEvent = resultMessageEvent;
	}


	/**
	 * @return the processingId
	 */
	public String getProcessingId() {
		return ProcessingId;
	}


	/**
	 * @param processingId the processingId to set
	 */
	public void setProcessingId(String processingId) {
		ProcessingId = processingId;
	}


	/**
	 * @return the samples
	 */
	public List<Mp96SampleData> getSamples() {
		return samples;
	}


	/**
	 * @param samples the samples to set
	 */
	public void setSamples(List<Mp96SampleData> samples) {
		this.samples = samples;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Mp96RunData [runId=" + runId + ", runComments=" + runComments + ", deviceRunId=" + deviceRunId
				+ ", operatorName=" + operatorName + ", deviceId=" + deviceId + ", resultMessageType="
				+ resultMessageType + ", resultMessageEvent=" + resultMessageEvent + ", ProcessingId=" + ProcessingId
				+ ", samples=" + samples + "]";
	}








	
	

	
	
	
}
