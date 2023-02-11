/*******************************************************************************
 * RouterUrlCofig.java                  
 *  Version:  1.0
 * 
 *  Authors:  gosula.r
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
 *   gosula.r@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.lpcamel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * The Class RouterUrlCofig.
 */
public class RouterUrlCofig {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(MP24ParamConfig.class);

	/** The instance. */
	private static RouterUrlCofig instance;

	/** The url map. */
	private Map<String, String> urlMap;

	/**
	 * Gets the URL map.
	 *
	 * @return the URL map
	 */
	public Map<String, String> getURLMap() {
		return urlMap;
	}

	/**
	 * Sets the URL map.
	 *
	 * @param uRLMap the u RL map
	 */
	public void setURLMap(Map<String, String> uRLMap) {
		this.urlMap = uRLMap;
	}

	/**
	 * Get the instance of RouterUrlCofig.
	 *
	 * @return single instance of RouterUrlCofig
	 */
	public static RouterUrlCofig getInstance() {
		if (instance == null) {
			instance = new RouterUrlCofig();
		}
		instance.fetchUrlConfig();
		return instance;
	}

	/**
	 * Fetch url config.
	 *
	 * @return true, if successful
	 */
	public boolean fetchUrlConfig() {

		if (urlMap != null && urlMap.size() > 0) {
			return true;
		}

		boolean success = false;
		String response = getFile();

		try {
			if (response != null && !response.isEmpty()) {

				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(response);
				Iterator<String> fieldIterator = node.getFieldNames();

				while (fieldIterator.hasNext()) {
					String field = fieldIterator.next();
					if (field.equalsIgnoreCase(AppUrlEntry.findByKey(AppUrlEntry.ROOT_ELEMENT))) {
						Map<String, String> urlMap1 = mapper.readValue(node.get(field),
								new TypeReference<Map<String, String>>() {
								});
						setURLMap(urlMap1);
					}
				}
				success = true;

			}
		} catch (Exception e) {
			logger.info("Exception at router url config");
		}

		return success;

	}

	/**
	 * Gets the url.
	 *
	 * @param url the url
	 * @return the url
	 */
	public String getUrl(AppUrlEntry url) {
		if (urlMap != null)
			return urlMap.get(url.getValue());
		return null;
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	private String getFile() {

		StringBuilder result = new StringBuilder("");
		try {

			Resource resource = new ClassPathResource("urlConfig.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					result.append(line).append("\n");
				}
			} finally {
				reader.close();
			}

		} catch (Exception e) {
			logger.info("Exception at router url confi:::");
		}

		return result.toString();

	}
}
