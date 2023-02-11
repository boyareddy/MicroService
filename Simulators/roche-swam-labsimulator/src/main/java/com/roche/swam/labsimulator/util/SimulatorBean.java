package com.roche.swam.labsimulator.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class SimulatorBean {
	private int numberOfSamples;
	private int delayBetweenQuery;
	private List<String> sampleId;
	private List<ResultSampleBean> samples;
	private String sendingApplication;
	
	public int getNumberOfSamples() {
		return numberOfSamples;
	}
	public void setNumberOfSamples(int numberOfSamples) {
		this.numberOfSamples = numberOfSamples;
	}
	public int getDelayBetweenQuery() {
		return delayBetweenQuery;
	}
	public void setDelayBetweenQuery(int delayBetweenQuery) {
		this.delayBetweenQuery = delayBetweenQuery;
	}
	public List<String> getSampleId() {
		return sampleId;
	}
	public void setSampleId(List<String> sampleId) {
		this.sampleId = sampleId;
	}
	public String getSendingApplication() {
		return sendingApplication;
	}
	public void setSendingApplication(String sendingApplication) {
		this.sendingApplication = sendingApplication;
	}
	public String getSendingFacility() {
		return sendingFacility;
	}
	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}
	public String getReceivingApplication() {
		return receivingApplication;
	}
	public void setReceivingApplication(String receivingApplication) {
		this.receivingApplication = receivingApplication;
	}
	public String getReceivingFacility() {
		return receivingFacility;
	}
	public void setReceivingFacility(String receivingFacility) {
		this.receivingFacility = receivingFacility;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageControlId() {
		return messageControlId;
	}
	public void setMessageControlId(String messageControlId) {
		this.messageControlId = messageControlId;
	}
	public String getProcessingId() {
		return processingId;
	}
	public void setProcessingId(String processingId) {
		this.processingId = processingId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getCharacterSet() {
		return characterSet;
	}
	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}
	public String getMessageQueryName() {
		return messageQueryName;
	}
	public void setMessageQueryName(String messageQueryName) {
		this.messageQueryName = messageQueryName;
	}
	public List<ResultSampleBean> getSamples() {
		return samples;
	}
	public void setSamples(List<ResultSampleBean> resultSampleBean) {
		this.samples = resultSampleBean;
	}
	private String sendingFacility;
	private String receivingApplication;
	private String receivingFacility;
	private String messageType;
	private String messageControlId;
	private String processingId;
	private String  versionId;
	private String characterSet;
	private String messageQueryName;

	@Override
	public String toString() {
		return "SimulatorBean [numberOfSamples=" + numberOfSamples + ", delayBetweenQuery=" + delayBetweenQuery
				+ ", sampleId=" + sampleId + ", samples=" + samples + ", sendingApplication=" + sendingApplication
				+ ", sendingFacility=" + sendingFacility + ", receivingApplication=" + receivingApplication
				+ ", receivingFacility=" + receivingFacility + ", messageType=" + messageType + ", messageControlId="
				+ messageControlId + ", processingId=" + processingId + ", versionId=" + versionId + ", characterSet="
				+ characterSet + ", messageQueryName=" + messageQueryName + ", getNumberOfSamples()="
				+ getNumberOfSamples() + ", getDelayBetweenQuery()=" + getDelayBetweenQuery() + ", getSampleId()="
				+ getSampleId() + ", getSendingApplication()=" + getSendingApplication() + ", getSendingFacility()="
				+ getSendingFacility() + ", getReceivingApplication()=" + getReceivingApplication()
				+ ", getReceivingFacility()=" + getReceivingFacility() + ", getMessageType()=" + getMessageType()
				+ ", getMessageControlId()=" + getMessageControlId() + ", getProcessingId()=" + getProcessingId()
				+ ", getVersionId()=" + getVersionId() + ", getCharacterSet()=" + getCharacterSet()
				+ ", getMessageQueryName()=" + getMessageQueryName() + ", getSamples()=" + getSamples()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	
	
}

