package com.roche.connect.dpcr.util;

import java.util.List;

public class ResultBean {
	
	private String runId;
	private String runResults;
	private String runNotes;
	private String sendingFacility;
	private String recevingFacility;
	private String recevingApplication;
	private String hl7Version;

	private List<SampleBean> Sample;
	private String ackMsgControlId;
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
	 * @return the sendingFacility
	 */
	public String getSendingFacility() {
		return sendingFacility;
	}

	/**
	 * @param sendingFacility the sendingFacility to set
	 */
	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}

	/**
	 * @return the recevingFacility
	 */
	public String getRecevingFacility() {
		return recevingFacility;
	}

	/**
	 * @param recevingFacility the recevingFacility to set
	 */
	public void setRecevingFacility(String recevingFacility) {
		this.recevingFacility = recevingFacility;
	}

	/**
	 * @return the recevingApplication
	 */
	public String getRecevingApplication() {
		return recevingApplication;
	}

	/**
	 * @param recevingApplication the recevingApplication to set
	 */
	public void setRecevingApplication(String recevingApplication) {
		this.recevingApplication = recevingApplication;
	}

	/**
	 * @return the hl7Version
	 */
	public String getHl7Version() {
		return hl7Version;
	}

	/**
	 * @param hl7Version the hl7Version to set
	 */
	public void setHl7Version(String hl7Version) {
		this.hl7Version = hl7Version;
	}

	/**
	 * @return the sample
	 */
	public List<SampleBean> getSample() {
		return Sample;
	}

	/**
	 * @param sample the sample to set
	 */
	public void setSample(List<SampleBean> sample) {
		Sample = sample;
	}

	/**
	 * @return the ackMsgControlId
	 */
	public String getAckMsgControlId() {
		return ackMsgControlId;
	}

	/**
	 * @param ackMsgControlId the ackMsgControlId to set
	 */
	public void setAckMsgControlId(String ackMsgControlId) {
		this.ackMsgControlId = ackMsgControlId;
	}

	/**
	 * @return the runResults
	 */
	public String getRunResults() {
		return runResults;
	}

	/**
	 * @param runResults the runResults to set
	 */
	public void setRunResults(String runResults) {
		this.runResults = runResults;
	}

	/**
	 * @return the runNotes
	 */
	public String getRunNotes() {
		return runNotes;
	}

	/**
	 * @param runNotes the runNotes to set
	 */
	public void setRunNotes(String runNotes) {
		this.runNotes = runNotes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResultBean [runId=" + runId + ", runResults=" + runResults + ", runNotes=" + runNotes
				+ ", sendingFacility=" + sendingFacility + ", recevingFacility=" + recevingFacility
				+ ", recevingApplication=" + recevingApplication + ", hl7Version=" + hl7Version + ", Sample=" + Sample
				+ ", ackMsgControlId=" + ackMsgControlId + "]";
	}




	

}
