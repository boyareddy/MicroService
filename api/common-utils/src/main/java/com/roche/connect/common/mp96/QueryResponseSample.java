package com.roche.connect.common.mp96;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResponseSample {

	private String accessioningId;
	private String containerId;
	private String position;
	private String reagentName;
	private String reagentVersion;
	private String protocolName;
	private String protocolVersion;
	private String sampleVolume;
	private String eluateVolume;
	private String comment;

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

	public String getReagentName() {
		return reagentName;
	}

	public void setReagentName(String reagentName) {
		this.reagentName = reagentName;
	}

	public String getReagentVersion() {
		return reagentVersion;
	}

	public void setReagentVersion(String reagentVersion) {
		this.reagentVersion = reagentVersion;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getSampleVolume() {
		return sampleVolume;
	}

	public void setSampleVolume(String sampleVolume) {
		this.sampleVolume = sampleVolume;
	}

	public String getEluateVolume() {
		return eluateVolume;
	}

	public void setEluateVolume(String eluateVolume) {
		this.eluateVolume = eluateVolume;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "QueryResponseSample [accessioningId=" + accessioningId + ", containerId=" + containerId + ", position="
				+ position + ", reagentName=" + reagentName + ", reagentVersion=" + reagentVersion + ", protocolName="
				+ protocolName + ", protocolVersion=" + protocolVersion + ", sampleVolume=" + sampleVolume
				+ ", eluateVolume=" + eluateVolume + ", comment=" + comment + "]";
	}
}
