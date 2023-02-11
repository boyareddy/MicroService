package com.roche.connect.imm.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;

import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.DeviceTestOptionsDTO;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.imm.utils.RestClient;

@Service("assayIntegrationService")
public class AssayIntegrationService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	/**
	 * Method to find findProcessStepByAssayTypeByDeviceType from AMM
	 * 
	 * @param
	 * @return List of ProcessStepActionDTO object.
	 * @throws HMTPException
	 * @throws UnsupportedEncodingException
	 */
	/** AMM Process step action Url */
	@Value("${connect.amm_processstep_action_url}")
	private String ammProcessStepActionUrl;
	
    @Value("${connect.amm_device_type_url}")
    private String ammDeviceTypeUrl;
	/** AMM Url */
	@Value("${connect.amm_host_url}")
	private String ammHostUrl;
	
	@Value("${connect.amm_process_step_name_url}")
	private String ammProcessStepNameUrl;

	/** AMM Process step action Url */
	@Value("${connect.amm_device_test_options_url}")
	private String ammDeviceTestOptionsUrl;

	private ObjectMapper objectMapper = new ObjectMapper();
	
	private static final String DEVICE_TYPE_URL_KEY = "deviceType=";
	
	public List<ProcessStepActionDTO> findProcessStepByAssayTypeAndDeviceType(String assayType, String deviceType,
			String token) throws HMTPException, UnsupportedEncodingException {

		List<ProcessStepActionDTO> processSteplist = new ArrayList<>();
		String url = ammHostUrl + "/" + assayType + "/" + ammProcessStepActionUrl;
		try {
			if (deviceType != null)
				url = url + "?" + DEVICE_TYPE_URL_KEY + deviceType;
			// http://localhost:88/amm/json/rest/api/v1/assay/NIPTDPCR/processstepaction?deviceType=MP96
			logger.info("Calling AMM to find processstepaction Details");

			Builder builder = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null);
			builder.header("Cookie", "brownstoneauthcookie=" + token);
			processSteplist = builder.get(new GenericType<List<ProcessStepActionDTO>>() {
			});

		} catch (Exception exp) {
			logger.error("Error occurred while calling at AMM findProcessStepByAssayTypeByDeviceType Api"
					+ exp.getMessage());
			throw new HMTPException(exp);
		}
		return processSteplist;
	}

	/**
	 * This method returns Device test options from AMM using assayType and
	 * deviceType
	 * 
	 * @param assayType
	 * @param deviceType
	 * @return
	 */
	public List<DeviceTestOptionsDTO> getDeviceTestOptionsData(String assayType, String deviceType) {

		logger.info("MessageProcessorService.getDeviceTestOptionsData: " + assayType + "," + deviceType);

		List<DeviceTestOptionsDTO> deviceTestOptions = null;
		String url = ammHostUrl + "/" + assayType + "/" + ammDeviceTestOptionsUrl;

		try {

			if (deviceType != null)
				url = url + "?"+DEVICE_TYPE_URL_KEY + deviceType;

			logger.info("Calling Api to AMM to get test options: " + url);

			Invocation.Builder assayClient = RestClientUtil
					.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null);
			deviceTestOptions = assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {
			});

			logger.info("Response from AMM while calling getDeviceTestOptionsData: "
					+ objectMapper.writeValueAsString(deviceTestOptions));

		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException Error occured while calling AMM getWFProcessData: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("Exception occured while calling AMM getWFProcessData: " + e.getMessage());
		}

		return deviceTestOptions;
	}

	/**
	 * This method returns list of process step actions from AMM using assayType and
	 * deviceType
	 * 
	 * @param assayType
	 * @param deviceType
	 * @return
	 */
	public List<ProcessStepActionDTO> getProcessStepAction(String assayType, String deviceType) {

		logger.info("MessageProcessorService.getProcessStepAction: " + assayType + "," + deviceType);

		List<ProcessStepActionDTO> processStepActions = null;
		String url = ammHostUrl + "/" + assayType + "/" + ammProcessStepActionUrl;

		try {

			if (deviceType != null)
				url = url + "?" + DEVICE_TYPE_URL_KEY + deviceType;

			logger.info("Calling Api to AMM to get process step actions: " + url);

			Invocation.Builder assayClient = RestClientUtil
					.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null);
			processStepActions = assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {
			});

			logger.info("Response from AMM while calling getDeviceTestOptionsData: "
					+ objectMapper.writeValueAsString(processStepActions));

		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException Error occured while calling AMM getWFProcessData: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("Exception occured while calling AMM getWFProcessData: " + e.getMessage());
		}

		return processStepActions;
	}
	public List<DeviceTestOptionsDTO> getTestOptionsByOrderIdandDeviceandProcessStep(String assayType,
        String deviceType, String processStepName) throws IOException {
        logger.info(
            " -> getTestOptionsByAccessioningIDandDevice()::Call AMM to find device test options details for given accessioning id and device type");
        return findDeviceTestOptions(assayType, deviceType, processStepName);
    }
	
	public List<DeviceTestOptionsDTO> findDeviceTestOptions(String assayType, String deviceType, String processStepName)
        throws IOException {
        
        return findDeviceTestOptions(assayType, deviceType, processStepName, null);
    }
	
	public List<DeviceTestOptionsDTO> findDeviceTestOptions(String assayType, String deviceType, String processStepName,
			String token) throws IOException {
	        
	        String url = "";
	        try {
	            logger.info("Calling AMM to find Test Options Details");
	            if (processStepName != null) {
	                url = ammHostUrl+"/" + assayType+"/"+ ammDeviceTestOptionsUrl+ammDeviceTypeUrl + deviceType
	                        + "&" + ammProcessStepNameUrl+ processStepName;
	            } else {
	                url = ammHostUrl+"/" + assayType+"/"+ ammDeviceTestOptionsUrl+ammDeviceTypeUrl + deviceType;
	            }
	            Invocation.Builder assayClient =
	                RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null);
	            
	            if (token != null)
	            	assayClient.header(RestClient.COOKIE_STR, RestClient.COOKIE_KEY + token);
	            
	            List<DeviceTestOptionsDTO> listTestOptions =
	                assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {});
	            
	            if (!listTestOptions.isEmpty()) {
	                return listTestOptions;
	            }
	            
	        } catch (IOException e) {
	            logger.error("Error occurred while calling at findDeviceTestOptions(Long id,String deviceType). URL "+ url
	                + this.getClass().getMethods(), e);
	            throw e;
	            
	        }
	        logger.info("Find Test Options Details Completed..");
	        return Collections.emptyList();
	        
	    }

}
