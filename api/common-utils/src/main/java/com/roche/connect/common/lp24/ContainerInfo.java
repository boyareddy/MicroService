package com.roche.connect.common.lp24;

import java.io.Serializable;

public class ContainerInfo implements Serializable{
    
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String containerIdentifier;
	
	private String containerStatus;
	
	private String carrierBarcode;
	
	private String carrierPosition;
	
	private String containerVolume;
	
	private String availableSpecimenVolume;
	
	private String initialSpecimenVolume;
	
	private String specimenEventDate;
	
	private String specimenVolume;
	
	private String unitofVolume;
	
	private String carrierType;
	
	private String outputPlateType;

	public String getContainerIdentifier() {
		return containerIdentifier;
	}

	public void setContainerIdentifier(String containerIdentifier) {
		this.containerIdentifier = containerIdentifier;
	}

	public String getContainerStatus() {
		return containerStatus;
	}

	public void setContainerStatus(String containerStatus) {
		this.containerStatus = containerStatus;
	}

	public String getCarrierBarcode() {
		return carrierBarcode;
	}

	public void setCarrierBarcode(String carrierBarcode) {
		this.carrierBarcode = carrierBarcode;
	}

	public String getCarrierPosition() {
		return carrierPosition;
	}

	public void setCarrierPosition(String carrierPosition) {
		this.carrierPosition = carrierPosition;
	}

	public String getContainerVolume() {
		return containerVolume;
	}

	public void setContainerVolume(String containerVolume) {
		this.containerVolume = containerVolume;
	}

	public String getAvailableSpecimenVolume() {
		return availableSpecimenVolume;
	}

	public void setAvailableSpecimenVolume(String availableSpecimenVolume) {
		this.availableSpecimenVolume = availableSpecimenVolume;
	}

	public String getInitialSpecimenVolume() {
		return initialSpecimenVolume;
	}

	public void setInitialSpecimenVolume(String initialSpecimenVolume) {
		this.initialSpecimenVolume = initialSpecimenVolume;
	}

	public String getSpecimenEventDate() {
		return specimenEventDate;
	}

	public void setSpecimenEventDate(String specimenEventDate) {
		this.specimenEventDate = specimenEventDate;
	}

	public String getSpecimenVolume() {
		return specimenVolume;
	}

	public void setSpecimenVolume(String specimenVolume) {
		this.specimenVolume = specimenVolume;
	}

	public String getUnitofVolume() {
		return unitofVolume;
	}

	public void setUnitofVolume(String unitofVolume) {
		this.unitofVolume = unitofVolume;
	}

	public String getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(String carrierType) {
		this.carrierType = carrierType;
	}

	public String getOutputPlateType() {
		return outputPlateType;
	}

	public void setOutputPlateType(String outputPlateType) {
		this.outputPlateType = outputPlateType;
	}
	
	

}
