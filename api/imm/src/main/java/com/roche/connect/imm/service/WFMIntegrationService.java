package com.roche.connect.imm.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.forte.ForteStatusMessage;
import com.roche.connect.common.mp96.WFMQueryMessage;
import com.roche.connect.imm.utils.RestClient;
import com.roche.connect.imm.utils.UrlConstants;

@Service
public class WFMIntegrationService {
    
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    
	@Value("${connect.wfm_host_url}")
	private String wfmHostUrl;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    public boolean workOrderRequest(WFMQueryMessage workOrderRequest, String token) throws IOException {
        
        try {
            
            logger
                .info("Calling WFM to post workOrderRequest, accessioningId: " + workOrderRequest.getAccessioningId());
            
            String url = wfmHostUrl + UrlConstants.MP96_WFM_WORK_ORDER_REQ_URL;
            Response response = RestClient.post(url, workOrderRequest, token, null);
            
			logger.info("WFM workOrderRequest, response code:" + response.getStatus());
			return response.getStatus() == HttpStatus.SC_OK;
            
        } catch (UnsupportedEncodingException exp) {
            logger.error("Error occurred while calling at WFM workOrderRequest Api" + exp.getMessage(), exp);
            throw exp;
        }
        
    }
    
    public boolean updateForteStatus(ForteStatusMessage forteStatusMessage, String token) throws IOException {
        
        try {
            logger.info("Calling WFM to update forte, accessioningId: " + forteStatusMessage.getAccessioningId());
            String url = wfmHostUrl + UrlConstants.FORTE_WFM_STATUS_UPDATE;
            RestClient.post(url, forteStatusMessage, token, null);
            return true;
        } catch (IOException exp) {
            logger.error("Error occurred while calling at WFM updateForteStatus Api" + exp.getMessage(), exp);
            throw exp;
        }
        
    }
    
    public boolean genericMessagePoster(Object message, String url, String token) throws IOException {
        
        try {
            logger.info("URL: " + url);
            logger.info("Post message: " + objectMapper.writeValueAsString(message));
            RestClient.post(url, message, token, null);
            return true;
            
        } catch (UnsupportedEncodingException exp) {
            logger.error("Error occurred while calling Api" + exp.getMessage(), exp);
            throw exp;
        }
    }
    
    public void updateRunResultStatusByDeviceId(String deviceId, String deviceType, String token) {
        try {
            String url = null;
            logger.info("Calling WFM to update Run Result Device stats changed to Idle, DeviceId: " + deviceId);
            Map<String, String> requestBody = new HashMap<>();
            if (deviceType.equalsIgnoreCase(DeviceType.MP24)) {
                url = wfmHostUrl + UrlConstants.WFM_MP24_RUN_RESULT_UPDATE_URL;
                requestBody.put("deviceType", DeviceType.MAGNAPURE24);
            } else if (deviceType.equalsIgnoreCase(DeviceType.LP24)) {
                url = wfmHostUrl + UrlConstants.WFM_LP24_RUN_RESULT_UPDATE_URL;
                requestBody.put("deviceType", deviceType);
            }
            
            logger.info("URL: " + url);
            
            requestBody.put("deviceId", deviceId);
            RestClient.post(url, requestBody, token, null);
        } catch (UnsupportedEncodingException e) {
            logger.error("Error occurred while calling Api" + e.getMessage());
        }
    }
    
}
