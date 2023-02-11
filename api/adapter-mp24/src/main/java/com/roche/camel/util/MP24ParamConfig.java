/*******************************************************************************
 * MP24ParamConfig.java                  
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
package com.roche.camel.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.camel.dto.MP24ConfigDetails;

/**
 * The Class MP24ParamConfig.
 */
public class MP24ParamConfig {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(MP24ParamConfig.class);
	
	/** The instance. */
	private static MP24ParamConfig instance;

	/** The mp 24 config details. */
	private MP24ConfigDetails mp24ConfigDetails;

	/**
	 * Gets the mp 24 config details.
	 *
	 * @return the mp 24 config details
	 */
	public MP24ConfigDetails getMp24ConfigDetails() {
		return this.mp24ConfigDetails;
	}

	/**
	 * Sets the mp 24 config details.
	 *
	 * @param mp24ConfigDetails the new mp 24 config details
	 */
	public void setMp24ConfigDetails(MP24ConfigDetails mp24ConfigDetails) {
		this.mp24ConfigDetails = mp24ConfigDetails;
	}

	/**
	 * Get the instance of RouterUrlCofig.
	 *
	 * @return single instance of MP24ParamConfig
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static MP24ParamConfig getInstance() throws IOException {
		if (instance == null) {
			instance = new MP24ParamConfig();
			instance.fetchSampleTypeConfig();
		}

		return instance;
	}

	/**
	 * Fetch sample type config.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void fetchSampleTypeConfig() throws IOException {

		JsonParser jp = null;
		try {
			InputStream stream = this.getClass().getResourceAsStream("/sampletypeconfig.json");
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory jsonFactory = mapper.getFactory();
			jp = jsonFactory.createParser(stream);
			this.mp24ConfigDetails = jp.readValueAs(MP24ConfigDetails.class);
		} catch (Exception e) {
			logger.error("Exception at fetchSampleTypeConfig:::: ");
		} finally {
			if (jp != null) {
				jp.close();
			}
		}

	}

}
