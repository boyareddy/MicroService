/*******************************************************************************
*
* VersionDetailsServiceImpl.java                  
* Version:  1.0
*
* Authors:  somesh_r
*
*********************
*
* Copyright (c) 2019  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
* All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
* herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
* from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
* executed Confidentiality and Non-disclosure agreements explicitly covering such access
*
* The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
* information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
* OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
* LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
* TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.      
*          
**********************
* ChangeLog:
*
*   somesh_r@hcl.com Mar 20, 2019: Updated copyright headers
*
**********************
*
* TODO: 
*
**********************
*
* Description: write me
*
**********************
* 
* 
******************************************************************************/

package com.roche.connect.adm.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.error.CustomErrorCodes;
import com.roche.connect.adm.util.ADMConstants;
import com.roche.connect.common.amm.dto.AssayTypeDTO;
@Service
public class VersionDetailsServiceImpl implements IVersionDetailsService{

	@Value("${pas.dm_host_url}")
	String deviceHostUrl;
	
	@Value("${pas.amm_host_url}")
	String ammHostUrl;
	
	private final String UTF = "UTF-8";

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getSupportedDeviceWithVersion() throws  HMTPException{

		try {
			logger.info("-> getSupportedDeviceWithVersion()");

			String url = deviceHostUrl + ADMConstants.DEVICE_MGMT_URL;
			Builder deviceConnection = RestClientUtil.getBuilder(URLEncoder.encode(url, UTF), null);
			String response = deviceConnection.get(String.class);
			logger.info("-> getSupportedDeviceWithVersion()::Received device types info from DM");
			if (StringUtils.isEmpty(response)) {
				throw new HMTPException("Device types info cannot be empty!");
			}
			JSONObject deviceTypes = new JSONObject();
			ObjectMapper mapper = new ObjectMapper();
			List<Map<String, Object>> map = mapper.readValue(response , new TypeReference<List<Map<String, Object>>>(){});
			
			for (Map<String, Object> mapEntry: map){
				StringBuilder sb = new StringBuilder();
				Map<String, Object> attribMap = (Map<String, Object>) mapEntry.get(ADMConstants.ATTRIBUTES.toString());
				if(attribMap == null){
					logger.info("-> getSupportedDeviceWithVersion()::DeviceType is not having attributes");
					continue;
				}
				List<String> list =  (List<String>) attribMap.get(ADMConstants.SUPPORTED_PROTOCOLS.toString());
				if(list == null){
					logger.info("-> getSupportedDeviceWithVersion()::DeviceType doesn't contain supported protocol.");
					continue;
				}
				for(String protocol: list) {
					if(protocol.equalsIgnoreCase(ADMConstants.HL7.toString())) {
						sb.append(protocol+" ");
						sb.append(String.join(",", (List<String>) ((Map<String, Object>) mapEntry.get(ADMConstants.ATTRIBUTES.toString())).get(ADMConstants.SUPPORTED_PROTOCOL_VERSION.toString())));
					}else if(protocol.equalsIgnoreCase(ADMConstants.REST.toString())) {
						if (sb.length() > 1)
							sb.append(", "+protocol);
						else
							sb.append(protocol);
					}
				}
				if(sb.length() > 1)
					deviceTypes.put(mapEntry.get(ADMConstants.DEVICE_TYPE_NAME.toString()).toString(), sb.toString());
			}
			logger.info("Device type details are "+deviceTypes);
			return deviceTypes;
		} catch (JSONException | IOException | HMTPException e) {
			logger.error("-> getSupportedDeviceWithVersion()::", e.getMessage());
		}
		return null;
	}

	@Override
	public JSONObject getAssaysWithVersion() throws UnsupportedEncodingException, HMTPException {
		logger.info("Enters into get assay version details");
		String url =ammHostUrl+ADMConstants.ASSAY_URL.toString();
		logger.info("Calling assay url "+url);
		try {
			Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, UTF), null);
			List<AssayTypeDTO> assayTypelist = orderClient.get(new GenericType<List<AssayTypeDTO>>() {
			});
			if(assayTypelist == null){
				throw new HMTPException("assay info cannot be empty!");
			}
			JSONObject assayTypeWithVersion = new JSONObject();
			for (AssayTypeDTO assayTypeDTO : assayTypelist) {
				assayTypeWithVersion.put(assayTypeDTO.getAssayType(), assayTypeDTO.getAssayVersion());
			}
			logger.info("Assay type version is "+assayTypeWithVersion);
			return assayTypeWithVersion;
		} catch (JSONException | IOException e) {
			logger.error("-> getAssaysWithVersion()::", e.getMessage());
		}
		return null;
	}
}
