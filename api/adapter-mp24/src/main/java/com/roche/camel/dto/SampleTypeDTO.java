/*******************************************************************************
 * SampleTypeDTO.java
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

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * The Class SampleTypeDTO.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleTypeDTO {

	/** The id. */
	String id;

	/** The name. */
	String name;

	/** The value. */
	String value;

	/** The encoding system. */
	String encodingSystem;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the encoding system.
	 *
	 * @return the encoding system
	 */
	public String getEncodingSystem() {
		return encodingSystem;
	}

	/**
	 * Sets the encoding system.
	 *
	 * @param encodingSystem
	 *            the new encoding system
	 */
	public void setEncodingSystem(String encodingSystem) {
		this.encodingSystem = encodingSystem;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}



}
