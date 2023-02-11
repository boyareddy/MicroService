/*******************************************************************************
 * WebServicesImpl.java                  
 *  Version:  1.0
 * 
 *  Authors:  umashankar d
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
 *  umashankar-d@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.forte.adapter.services;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.server.rest.RestClientUtil;

/**
 * The Class WebServicesImpl.
 */
@Service
public class WebServicesImpl implements WebServices {

	/** The login url. */
	@Value("${pas.authenticate_url}")
	private String loginUrl;

	/** The login entity. */
	@Value("${pas.login_entity}")
	private String loginEntity;

	/** The logger. */
	private static Logger logger = LogManager.getLogger(WebServicesImpl.class);

	/* (non-Javadoc)
	 * @see com.roche.connect.htp.adapter.services.WebServices#postRequest(java.lang.String, java.lang.String)
	 */
	@Override
	public Response postRequest(String url, String body) {
		logger.info("Entered WebServicesImpl.postRequest method with URL : " + url + " Body:" + body);

		try {
			Builder builder = RestClientUtil.getBuilder(url, null);
			builder.header("Cookie", "brownstoneauthcookie =" + getToken());
			Response resp = builder.post(Entity.entity(body, MediaType.APPLICATION_JSON));
			logger.info("Response status from WFM : " + resp.getStatus());
			return resp;
		} catch (Exception e) {
			logger.info("Something went wrong, Request from Adapter to IMM : " + e);
			return null;
		}
		
	}
		public Response getRequest(String url) {
			logger.info("Entered WebServicesImpl.postRequest method with URL: " + url);
			Response resp = null;
			try {
				Builder builder = RestClientUtil.getBuilder(url, null);
				builder.header("Cookie", "brownstoneauthcookie=" + getToken());
				resp = builder.get();
				logger.info("Response status from WFM: " + resp.getStatus());
			} catch (Exception e) {
				logger.info("Something went wrong, Request from Adapter to IMM: " + e);
			}
			return resp;

	}
		@Override
		public Response putRequest(String url, String body) {
			logger.info("Entered WebServicesImpl.postRequest method with URL: " + url + " Body:" + body);
			Response resp = null;
			try {
				Builder builder = RestClientUtil.getBuilder(url, null);
				builder.header("Cookie", "brownstoneauthcookie=" + getToken());
				resp = builder.put(Entity.entity(body, MediaType.APPLICATION_JSON));
				logger.info("Response status from WFM: " + resp.getStatus());
			} catch (Exception e) {
				logger.info("Something went wrong, Request from Adapter to IMM: " + e);
			}
			return resp;
			
		}
		@Override
		public String getToken() {
					Builder pasBuilder = RestClientUtil.getBuilder(loginUrl, null);
					return pasBuilder.post(Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		}

}
