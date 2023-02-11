package com.roche.connect.common.mp24.message;

import java.io.Serializable;

public class ContainerInfo implements Serializable{
    
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String containerPosition;
	private String containerStatus;
	private String carrierType;
	private String carrierBarcode;
	private String carrierPosition;
	private String containerVolume;
	private String availableSpecimenVolume;
	private String initialSpecimenVolume;
	private String specimenEventDate;
	private String specimenVolume;
	private String unitofVolume;

	public String getContainerPosition() {
		return containerPosition;
	}

	public void setContainerPosition(String containerPosition) {
		this.containerPosition = containerPosition;
	}

	public String getContainerStatus() {
		return containerStatus;
	}

	public void setContainerStatus(String containerStatus) {
		this.containerStatus = containerStatus;
	}

	public String getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(String carrierType) {
		this.carrierType = carrierType;
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

}
