package com.roche.connect.rmm.services;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.dmm.DeviceDTO;
import com.roche.connect.rmm.util.UrlConstants;

@Service
public class DeviceIntegrationService {

	@Value("${pas.dmm_api_url}")
	private String deviceHostUrl;

	private ObjectMapper objectMapper = new ObjectMapper();

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	public DeviceDTO getDevice(String deviceId) throws IOException {

		List<DeviceDTO> device = getDevice(Arrays.asList(deviceId));
		if (device.isEmpty())
			return null;

		return device.get(0);
	}

	public List<DeviceDTO> getDevice(List<String> deviceIdList) throws IOException {

		try {

			if(deviceIdList.isEmpty())
				return Collections.emptyList();
			
			String prefix = "(serialNo='";
			String suffix = "')";
			String parameters = deviceIdList.stream().map(x -> prefix + x + suffix).collect(Collectors.joining("|"));

			String url = deviceHostUrl + UrlConstants.DEVICE_MGMT_URL + URLEncoder.encode(parameters, "UTF-8");

			Builder builder = RestClientUtil.getBuilder(url, null);
			List<DeviceDTO> deviceList = builder.get(new GenericType<List<DeviceDTO>>() {
			});

			logger.info("response from DM by deviceID list:" + objectMapper.writeValueAsString(deviceList));
			return deviceList;

		} catch (IOException e) {
			logger.error("Error in getDevice method" + e.getMessage(), e);
			throw e;
		}
	}

}
