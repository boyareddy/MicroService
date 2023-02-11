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
package com.roche.connect.htp.adapter.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.htp.adapter.rest.HtpAdapterRestApiImpl;
import com.roche.connect.htp.adapter.util.HTPAdapterConstants;


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

	@Value("${pas.device_remote_url}")
	String deviceEndPoint;
	
	@Value("${device.device_validate}")
	private boolean deviceFlag;
	
	@Value("${device.HTPDeviceType}")
	private String deviceTypeId;
	
	@Value("${device.pingDelay}")
	private long pingDelay;
	
	@Value("${device.frequency}")
	private long frequency;
	
	@Value("${adapter.adm_notification_url}")
	private String admNotificationURL;
	
	@Autowired
	HttpServletRequest httpServletRequest;
	
	/** The logger. */
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	private static Map<String, Long> deviceMap = new HashMap<>();
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.roche.connect.htp.adapter.services.WebServices#postRequest(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public Response postRequest(String url, String body) {
		logger.info("Entered WebServicesImpl.postRequest method with URL : " + url + " Body:" + body);

		try {
			Builder builder = RestClientUtil.getBuilder(url, null);
			builder.header(HTPAdapterConstants.COOKIE.getText(), HTPAdapterConstants.BROWN_STONE_AUTH_COOKIE.getText() + getToken());
			Response resp = builder.post(Entity.entity(body, MediaType.APPLICATION_JSON));
			logger.info("Response status from WFM : " + resp.getStatus());
			return resp;
		} catch (Exception e) {
			logger.info("Something went wrong, Request from Adapter to IMM : " + e);
		}
		return null;
	}

	public Response getRequest(String url) {
		logger.info("Entered WebServicesImpl.getRequest method with URL: " + url);
		Response resp = null;
		try {
			Builder builder = RestClientUtil.getBuilder(url, null);
			builder.header(HTPAdapterConstants.COOKIE.getText(), HTPAdapterConstants.BROWN_STONE_AUTH_COOKIE.getText() + getToken());
			resp = builder.get();
			logger.info("Response status from IMM: " + resp.getStatus());
		} catch (Exception e) {
			logger.info("Something went wrong, Request from Adapter to IMM: " + e);
		}
		return resp;

	}

	@Override
	public Response putRequest(String url, String body) {
		logger.info("Entered WebServicesImpl.putRequest method with URL: " + url + " Body:" + body);
		Response resp = null;
		try {
			Builder builder = RestClientUtil.getBuilder(url, null);
			builder.header(HTPAdapterConstants.COOKIE.getText(), HTPAdapterConstants.BROWN_STONE_AUTH_COOKIE.getText() + getToken());
			resp = builder.put(Entity.entity(body, MediaType.APPLICATION_JSON));
			logger.info("Response status from IMM: " + resp.getStatus());
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

	
	public void updateDeviceStatus(String serialNo, String status) {
		logger.info("enters into update device status | serialNo:" + serialNo + " | status:" + status);
			try {
				JSONObject attributes=new JSONObject();
				attributes.put("deviceStatus", status);
				if(status.equals(HTPAdapterConstants.ONLINE_STATUS.getText())) {
					attributes.put("ipAddress", httpServletRequest.getRemoteAddr());
					attributes.put("hostName", httpServletRequest.getRemoteHost());
				}
				attributes.put("state", "ACTIVE");
				String token = getToken();
				String parameters = "((serialNo='" + serialNo + "') & (state='ACTIVE') | (state='NEW'))";
		        String url = deviceEndPoint + "/json/device/fetch/expr?filterExpression=" + URLEncoder.encode(parameters,"UTF-8");
		        Builder builder2 = RestClientUtil.getBuilder(url, null);
	            builder2.header(HTPAdapterConstants.COOKIE.getText(), HTPAdapterConstants.BROWN_STONE_AUTH_COOKIE.getText() + token);
	            String resp = builder2.get(String.class);
	            JSONArray respobj = new JSONArray(resp);
	            if (respobj.length() == 0) {
	            	 throw new IOException("Unregistered device");
	            }
	            String deviceId=respobj.getJSONObject(0).get("deviceId").toString();
				url = deviceEndPoint + "/json/device/update/" + deviceId;
				Builder builder = RestClientUtil.getBuilder(url, null);
				builder.header(HTPAdapterConstants.COOKIE.getText(), HTPAdapterConstants.BROWN_STONE_AUTH_COOKIE.getText() + token);
				builder.post(Entity.entity(attributes.toString(), MediaType.APPLICATION_JSON));
				if(status.equals(HTPAdapterConstants.OFFLINE_STATUS.getText())) {
					List<String> content=new ArrayList<>();
					content.add(serialNo);
					AdmNotificationService.sendNotification(NotificationGroupType.DEVICE_OFFLINE_HTP,content,token,admNotificationURL);
				}
				logger.info("Updated device status");
			} catch (Exception e) {
				logger.error("DeviceManagement | Something went wrong while updating device status: " + e);
			}
	}

	@Override
	@Scheduled(fixedDelayString  =  "${device.frequency}")
	public void updateCounter() {
		try {
			if (deviceMap.size() != 0) {
				for (Map.Entry<String, Long> entry : deviceMap.entrySet()) {
					if (entry.getValue() > pingDelay) {
						updateDeviceStatus(entry.getKey(), HTPAdapterConstants.OFFLINE_STATUS.getText());
						deviceMap.remove(entry.getKey());
					} else {
						entry.setValue(entry.getValue() + frequency);
					}
				}

			}

		} catch (JsonIOException | JsonSyntaxException e) {
			logger.error("error while updating counter"+e);
		}
	}
	
	@Override
	public boolean getDetailsFromOauthToken(HtpAdapterRestApiImpl htpSimulatorRestApiImpl ) {
		boolean helloFlag = htpSimulatorRestApiImpl.getHelloFlag();
		boolean goodBye = htpSimulatorRestApiImpl.getGoodBye();
		htpSimulatorRestApiImpl.setHelloFlag(false);
		htpSimulatorRestApiImpl.setGoodBye(false);
		boolean registeredStatus = true;
		if(deviceFlag) {
		String userName =  ThreadSessionManager.currentUserSession().getPasJwtToken().getUserName();
		String domainName =  ThreadSessionManager.currentUserSession().getPasJwtToken().getDomainName();
		String url = deviceEndPoint + "/json/users/userName/"+userName+"/domainName/"+domainName+"/id";
		String token = getToken();
		Builder builder = RestClientUtil.getBuilder(url, null);
		builder.header(HTPAdapterConstants.COOKIE.getText(), HTPAdapterConstants.BROWN_STONE_AUTH_COOKIE.getText() + token);
		String userId=builder.get(String.class);
		String parameters = "((user.id=" + userId + ") & (state='ACTIVE') | (state='NEW'))";
		try {
		url = deviceEndPoint + "/json/device/fetch/expr?filterExpression=" + URLEncoder.encode(parameters,"UTF-8") ;
		builder = RestClientUtil.getBuilder(url, null);
		String response=builder.get(String.class);
			JSONArray deviceArray = new JSONArray(response);
			if (deviceArray.length() == 0) {
				registeredStatus = false;
				List<String> content=new ArrayList<>();
				content.add(userName);
				AdmNotificationService.sendNotification(NotificationGroupType.UNREGISTERED_DEVICE_HTP,content,token,admNotificationURL);
				throw new HMTPException("Message from unregistered device "+userName);
			}
			String serialNo=deviceArray.getJSONObject(0).get("serialNo").toString();
			if (helloFlag && (htpSimulatorRestApiImpl.getInstrumentId() == null || htpSimulatorRestApiImpl.getInstrumentId().equals("") || !deviceArray.getJSONObject(0).get("serialNo").toString().equals(htpSimulatorRestApiImpl.getInstrumentId()))) {
				registeredStatus = false;
				List<String> content=new ArrayList<>();
				content.add(userName);
				AdmNotificationService.sendNotification(NotificationGroupType.INVALID_DEVICE_ID_HTP,content,token,admNotificationURL);
				throw new HMTPException("Invalid HTP device ID "+serialNo);
			}
			if(goodBye) {
				updateDeviceStatus(serialNo,HTPAdapterConstants.OFFLINE_STATUS.getText());
			}else {
			updateDeviceStatus(serialNo,HTPAdapterConstants.ONLINE_STATUS.getText());
			deviceMap.put(serialNo, 0L);
			}
		} catch (JSONException | HMTPException | UnsupportedEncodingException e) {
			logger.error("error while updating device status"+e);
		}
		}
		return registeredStatus;
	}
	
	
}
