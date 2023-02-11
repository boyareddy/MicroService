package com.roche.connect.common.mp24.message;

import java.io.Serializable;

public class SampleInfo implements Serializable{
    
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String sampleOutputId;
	private String sampleOutputPosition;
	private String sampleType;
	private String specimenSourceSite;
	private String specimenCollectionSite;
	private String specimenDescription;
	private String dateTimeSpecimenCollected;
	private String dateTimeSpecimenReceived;
	private String dateTimeSpecimenExpiration;
	private String containerType;
	private String specimenRole;

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public String getSpecimenSourceSite() {
		return specimenSourceSite;
	}

	public void setSpecimenSourceSite(String specimenSourceSite) {
		this.specimenSourceSite = specimenSourceSite;
	}

	public String getSpecimenCollectionSite() {
		return specimenCollectionSite;
	}

	public void setSpecimenCollectionSite(String specimenCollectionSite) {
		this.specimenCollectionSite = specimenCollectionSite;
	}

	public String getSpecimenDescription() {
		return specimenDescription;
	}

	public void setSpecimenDescription(String specimenDescription) {
		this.specimenDescription = specimenDescription;
	}

	public String getDateTimeSpecimenCollected() {
		return dateTimeSpecimenCollected;
	}

	public void setDateTimeSpecimenCollected(String dateTimeSpecimenCollected) {
		this.dateTimeSpecimenCollected = dateTimeSpecimenCollected;
	}

	public String getDateTimeSpecimenReceived() {
		return dateTimeSpecimenReceived;
	}

	public void setDateTimeSpecimenReceived(String dateTimeSpecimenReceived) {
		this.dateTimeSpecimenReceived = dateTimeSpecimenReceived;
	}

	public String getDateTimeSpecimenExpiration() {
		return dateTimeSpecimenExpiration;
	}

	public void setDateTimeSpecimenExpiration(String dateTimeSpecimenExpiration) {
		this.dateTimeSpecimenExpiration = dateTimeSpecimenExpiration;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	public String getSampleOutputId() {
		return sampleOutputId;
	}

	public void setSampleOutputId(String sampleOutputId) {
		this.sampleOutputId = sampleOutputId;
	}

	public String getSampleOutputPosition() {
		return sampleOutputPosition;
	}

	public void setSampleOutputPosition(String sampleOutputPosition) {
		this.sampleOutputPosition = sampleOutputPosition;
	}

	public String getSpecimenRole() {
		return specimenRole;
	}

	public void setSpecimenRole(String specimenRole) {
		this.specimenRole = specimenRole;
	}

}
