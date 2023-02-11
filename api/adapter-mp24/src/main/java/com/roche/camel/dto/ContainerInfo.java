/*******************************************************************************
 * ContainerInfo.java
 *  Version:  1.0
 *
 *  Authors:  Arun Paul Jacob
 *
 * *********************
 *  Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 *
 *  The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE,
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.
 *
 * *********************
 *  ChangeLog:
 *
 *   arunpaul.j@hcl.com : Updated copyright headers
 *
 * *********************
 *
 *  Description:
 *
 * *********************
 ******************************************************************************/
package com.roche.camel.dto;

/**
 * The Class ContainerInfo.
 */
public class ContainerInfo {

	/** The container position. */
	String containerPosition;

	/** The container status. */
	String containerStatus;

	/** The carrier type. */
	String carrierType;

	/** The carrier barcode. */
	String carrierBarcode;

	/** The carrier position. */
	String carrierPosition;

	/** The container volume. */
	String containerVolume;

	/** The available specimen volume. */
	String availableSpecimenVolume;

	/** The initial specimen volume. */
	String initialSpecimenVolume;

	/** The specimen event date. */
	String specimenEventDate;

	/** The specimen volume. */
	String specimenVolume;

	/** The unitof volume. */
	String unitofVolume;

	/**
	 * Gets the carrier type.
	 *
	 * @return the carrier type
	 */
	public String getCarrierType() {
		return carrierType;
	}

	/**
	 * Sets the carrier type.
	 *
	 * @param carrierType
	 *            the new carrier type
	 */
	public void setCarrierType(String carrierType) {
		this.carrierType = carrierType;
	}

	/**
	 * Gets the carrier position.
	 *
	 * @return the carrier position
	 */
	public String getCarrierPosition() {
		return carrierPosition;
	}

	/**
	 * Sets the carrier position.
	 *
	 * @param carrierPosition
	 *            the new carrier position
	 */
	public void setCarrierPosition(String carrierPosition) {
		this.carrierPosition = carrierPosition;
	}

	/**
	 * Gets the container volume.
	 *
	 * @return the container volume
	 */
	public String getContainerVolume() {
		return containerVolume;
	}

	/**
	 * Sets the container volume.
	 *
	 * @param containerVolume
	 *            the new container volume
	 */
	public void setContainerVolume(String containerVolume) {
		this.containerVolume = containerVolume;
	}

	/**
	 * Gets the available specimen volume.
	 *
	 * @return the available specimen volume
	 */
	public String getAvailableSpecimenVolume() {
		return availableSpecimenVolume;
	}

	/**
	 * Sets the available specimen volume.
	 *
	 * @param availableSpecimenVolume
	 *            the new available specimen volume
	 */
	public void setAvailableSpecimenVolume(String availableSpecimenVolume) {
		this.availableSpecimenVolume = availableSpecimenVolume;
	}

	/**
	 * Gets the initial specimen volume.
	 *
	 * @return the initial specimen volume
	 */
	public String getInitialSpecimenVolume() {
		return initialSpecimenVolume;
	}

	/**
	 * Sets the initial specimen volume.
	 *
	 * @param initialSpecimenVolume
	 *            the new initial specimen volume
	 */
	public void setInitialSpecimenVolume(String initialSpecimenVolume) {
		this.initialSpecimenVolume = initialSpecimenVolume;
	}

	/**
	 * Gets the container position.
	 *
	 * @return the container position
	 */
	public String getContainerPosition() {
		return containerPosition;
	}

	/**
	 * Sets the container position.
	 *
	 * @param containerPosition
	 *            the new container position
	 */
	public void setContainerPosition(String containerPosition) {
		this.containerPosition = containerPosition;
	}

	/**
	 * Gets the container status.
	 *
	 * @return the container status
	 */
	public String getContainerStatus() {
		return containerStatus;
	}

	/**
	 * Sets the container status.
	 *
	 * @param containerStatus
	 *            the new container status
	 */
	public void setContainerStatus(String containerStatus) {
		this.containerStatus = containerStatus;
	}

	/**
	 * Gets the specimen event date.
	 *
	 * @return the specimen event date
	 */
	public String getSpecimenEventDate() {
		return specimenEventDate;
	}

	/**
	 * Sets the specimen event date.
	 *
	 * @param specimenEventDate
	 *            the new specimen event date
	 */
	public void setSpecimenEventDate(String specimenEventDate) {
		this.specimenEventDate = specimenEventDate;
	}

	/**
	 * Gets the specimen volume.
	 *
	 * @return the specimen volume
	 */
	public String getSpecimenVolume() {
		return specimenVolume;
	}

	/**
	 * Sets the specimen volume.
	 *
	 * @param specimenVolume
	 *            the new specimen volume
	 */
	public void setSpecimenVolume(String specimenVolume) {
		this.specimenVolume = specimenVolume;
	}

	/**
	 * Gets the unitof volume.
	 *
	 * @return the unitof volume
	 */
	public String getUnitofVolume() {
		return unitofVolume;
	}

	/**
	 * Sets the unitof volume.
	 *
	 * @param unitofVolume
	 *            the new unitof volume
	 */
	public void setUnitofVolume(String unitofVolume) {
		this.unitofVolume = unitofVolume;
	}

	/**
	 * Gets the carrier barcode.
	 *
	 * @return the carrier barcode
	 */
	public String getCarrierBarcode() {
		return carrierBarcode;
	}

	/**
	 * Sets the carrier barcode.
	 *
	 * @param carrierBarcode
	 *            the new carrier barcode
	 */
	public void setCarrierBarcode(String carrierBarcode) {
		this.carrierBarcode = carrierBarcode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
    @Override public String toString() {
        return "ContainerInfo [containerPosition=" + containerPosition + ", containerStatus=" + containerStatus
            + ", carrierType=" + carrierType + ", carrierBarcode=" + carrierBarcode + ", carrierPosition="
            + carrierPosition + ", containerVolume=" + containerVolume + ", availableSpecimenVolume="
            + availableSpecimenVolume + ", initialSpecimenVolume=" + initialSpecimenVolume + ", specimenEventDate="
            + specimenEventDate + ", specimenVolume=" + specimenVolume + ", unitofVolume=" + unitofVolume + "]";
    }


}
