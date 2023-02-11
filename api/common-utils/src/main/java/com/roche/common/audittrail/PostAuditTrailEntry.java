package com.roche.common.audittrail;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.client.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.common.adm.notification.RestClient;

public class PostAuditTrailEntry {

	PostAuditTrailEntry(){}
	private static  HMTPLogger logger = HMTPLoggerFactory.getLogger(String.valueOf(PostAuditTrailEntry.class));
	private static ObjectMapper objectMapper = new ObjectMapper();
	public static void postDataToAuditTrail(Entity auditTraildto, String token, String auditurl) {
		try {
			logger.info(auditurl +" :posting audit data :"+objectMapper.writeValueAsString(auditTraildto));
			RestClient.post(auditurl , auditTraildto, token, null);
		} catch (UnsupportedEncodingException | JsonProcessingException e) {
			logger.error("Error occured while posting audit Trail data::"+e.getMessage());
		}
	}
	
}
