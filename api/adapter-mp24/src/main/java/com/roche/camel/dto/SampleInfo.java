/*******************************************************************************
 * SampleInfo.java                  
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
 * The Class SampleInfo.
 */
public class SampleInfo {
	
	/** The sample output id. */
	String sampleOutputId;
	
	/** The sample output position. */
	String sampleOutputPosition;
	
	/** The sample type. */
	String sampleType;
	
	/** The specimen source site. */
	String specimenSourceSite;
	
	/** The specimen collection site. */
	String specimenCollectionSite;
	
	/** The specimen description. */
	String specimenDescription;
	
	/** The date time specimen collected. */
	String dateTimeSpecimenCollected;
	
	/** The date time specimen received. */
	String dateTimeSpecimenReceived;
	
	/** The date time specimen expiration. */
	String dateTimeSpecimenExpiration;
	
	/** The container type. */
	String containerType;
	
	/** The specimen role. */
	String specimenRole;
	

	/**
	 * Gets the sample type.
	 *
	 * @return the sample type
	 */
	public String getSampleType() {
		return sampleType;
	}
	
	/**
	 * Sets the sample type.
	 *
	 * @param sampleType the new sample type
	 */
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	
	/**
	 * Gets the container type.
	 *
	 * @return the container type
	 */
	public String getContainerType() {
		return containerType;
	}
	
	/**
	 * Sets the container type.
	 *
	 * @param containerType the new container type
	 */
	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}
	
	/**
	 * Gets the specimen source site.
	 *
	 * @return the specimen source site
	 */
	public String getSpecimenSourceSite() {
		return specimenSourceSite;
	}
	
	/**
	 * Sets the specimen source site.
	 *
	 * @param specimenSourceSite the new specimen source site
	 */
	public void setSpecimenSourceSite(String specimenSourceSite) {
		this.specimenSourceSite = specimenSourceSite;
	}
	
	/**
	 * Gets the specimen collection site.
	 *
	 * @return the specimen collection site
	 */
	public String getSpecimenCollectionSite() {
		return specimenCollectionSite;
	}
	
	/**
	 * Sets the specimen collection site.
	 *
	 * @param specimenCollectionSite the new specimen collection site
	 */
	public void setSpecimenCollectionSite(String specimenCollectionSite) {
		this.specimenCollectionSite = specimenCollectionSite;
	}
	
	/**
	 * Gets the specimen description.
	 *
	 * @return the specimen description
	 */
	public String getSpecimenDescription() {
		return specimenDescription;
	}
	
	/**
	 * Sets the specimen description.
	 *
	 * @param specimenDescription the new specimen description
	 */
	public void setSpecimenDescription(String specimenDescription) {
		this.specimenDescription = specimenDescription;
	}
	
	/**
	 * Gets the date time specimen collected.
	 *
	 * @return the date time specimen collected
	 */
	public String getDateTimeSpecimenCollected() {
		return dateTimeSpecimenCollected;
	}
	
	/**
	 * Sets the date time specimen collected.
	 *
	 * @param dateTimeSpecimenCollected the new date time specimen collected
	 */
	public void setDateTimeSpecimenCollected(String dateTimeSpecimenCollected) {
		this.dateTimeSpecimenCollected = dateTimeSpecimenCollected;
	}
	
	/**
	 * Gets the date time specimen received.
	 *
	 * @return the date time specimen received
	 */
	public String getDateTimeSpecimenReceived() {
		return dateTimeSpecimenReceived;
	}
	
	/**
	 * Sets the date time specimen received.
	 *
	 * @param dateTimeSpecimenReceived the new date time specimen received
	 */
	public void setDateTimeSpecimenReceived(String dateTimeSpecimenReceived) {
		this.dateTimeSpecimenReceived = dateTimeSpecimenReceived;
	}
	
	/**
	 * Gets the date time specimen expiration.
	 *
	 * @return the date time specimen expiration
	 */
	public String getDateTimeSpecimenExpiration() {
		return dateTimeSpecimenExpiration;
	}
	
	/**
	 * Sets the date time specimen expiration.
	 *
	 * @param dateTimeSpecimenExpiration the new date time specimen expiration
	 */
	public void setDateTimeSpecimenExpiration(String dateTimeSpecimenExpiration) {
		this.dateTimeSpecimenExpiration = dateTimeSpecimenExpiration;
	}
	
	/**
	 * Gets the sample output id.
	 *
	 * @return the sample output id
	 */
	public String getSampleOutputId() {
		return sampleOutputId;
	}
	
	/**
	 * Sets the sample output id.
	 *
	 * @param sampleOutputId the new sample output id
	 */
	public void setSampleOutputId(String sampleOutputId) {
		this.sampleOutputId = sampleOutputId;
	}
	
	/**
	 * Gets the sample output position.
	 *
	 * @return the sample output position
	 */
	public String getSampleOutputPosition() {
		return sampleOutputPosition;
	}
	
	/**
	 * Sets the sample output position.
	 *
	 * @param sampleOutputPosition the new sample output position
	 */
	public void setSampleOutputPosition(String sampleOutputPosition) {
		this.sampleOutputPosition = sampleOutputPosition;
	}
	
	/**
	 * Gets the specimen role.
	 *
	 * @return the specimen role
	 */
	public String getSpecimenRole() {
		return specimenRole;
	}
	
	/**
	 * Sets the specimen role.
	 *
	 * @param specimenRole the new specimen role
	 */
	public void setSpecimenRole(String specimenRole) {
		this.specimenRole = specimenRole;
	}
	
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override public String toString() {
        return "SampleInfo [sampleOutputId=" + sampleOutputId + ", sampleOutputPosition=" + sampleOutputPosition
            + ", sampleType=" + sampleType + ", specimenSourceSite=" + specimenSourceSite + ", specimenCollectionSite="
            + specimenCollectionSite + ", specimenDescription=" + specimenDescription + ", dateTimeSpecimenCollected="
            + dateTimeSpecimenCollected + ", dateTimeSpecimenReceived=" + dateTimeSpecimenReceived
            + ", dateTimeSpecimenExpiration=" + dateTimeSpecimenExpiration + ", containerType=" + containerType
            + ", specimenRole=" + specimenRole + "]";
    }

	
	
}
