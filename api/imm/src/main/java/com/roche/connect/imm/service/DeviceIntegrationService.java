package com.roche.connect.imm.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.client.Invocation.Builder;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.imm.utils.UrlConstants;

@Service
public class DeviceIntegrationService {
	@Value("${connect.dm_host_url}")
	private String deviceHostUrl;

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	public List<String> getAssayTypeFromDM(String instrumentId, String token) {

		List<String> assayTypes = new ArrayList<>();
		logger.info("Entering getAssayTypeFromDM instrumentId:" + instrumentId);

		JSONArray responseObj = null;
		String parameters = "((serialNo='" + instrumentId + "') & (state='ACTIVE') | (state='NEW'))";
		String url;
		try {
		url = deviceHostUrl + UrlConstants.DEVICE_MGMT_URL + URLEncoder.encode(parameters,"UTF-8");
		Builder deviceConnection = RestClientUtil.getBuilder(url, null);
		deviceConnection.header("Cookie", "brownstoneauthcookie=" + token);
		String response = deviceConnection.get(String.class);
		
		logger.info("response from DM in getAssayTypeFromDM :" + response);
		
			responseObj = new JSONArray(response);
			JSONArray assayType = responseObj.getJSONObject(0).getJSONObject("deviceType").getJSONObject("attributes")
					.getJSONArray("supportedAssayTypes");
			
			   assayTypes = Stream.of(assayType).map(e -> {
				try {
					return (String) e.get(0);
				} catch (JSONException e1) {
					logger.error("Error in getAssayTypeFromDM ", e1.getMessage());
				}
				return null;
				
			}).collect(Collectors.toList());
		} catch (JSONException | UnsupportedEncodingException e) {
			logger.error("Error in getAssayTypeFromDM while parsing ", e.getMessage());
		}
		return assayTypes;
	}

}
