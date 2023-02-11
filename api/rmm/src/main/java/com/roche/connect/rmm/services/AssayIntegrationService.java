package com.roche.connect.rmm.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.rmm.util.UrlConstants;

@Service
public class AssayIntegrationService {

	@Value("${pas.amm_api_url}")
	private String ammHostURL;

	private List<ProcessStepActionDTO> processStepActions = null;

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	public List<ProcessStepActionDTO> getProcessStepAction(String assayType, String deviceType, String token)
			throws HMTPException {
      boolean flag = false;
		if (processStepActions != null) {
			for (ProcessStepActionDTO processStepActionDTO : processStepActions) {
				if (processStepActionDTO.getAssayType().equalsIgnoreCase(assayType)) {
					flag = true;
					break;
				}
			}

		}

		if (!flag) {
			logger.info("AssayIntegrationService.getProcessStepAction: " + assayType + "," + deviceType);

			String url = ammHostURL + UrlConstants.AMM_ASSAY_URL + assayType + UrlConstants.PROCESS_STEP_ACTION;

			try {

				if (deviceType != null)
					url = url + "?" + UrlConstants.ADM_DEVICE_URL + deviceType;

				logger.info("Calling Api to AMM to get process step actions: " + url);

				Invocation.Builder assayClient = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8),
						null);

				assayClient.header("Cookie", "brownstoneauthcookie=" + token);
				processStepActions = assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {
				});

			} catch (NotAuthorizedException e2) {
				return null;
			} catch (UnsupportedEncodingException e) {
				logger.error("Exception occured while calling AMM getWFProcessData: " + e.getMessage());
			}
		}

		return processStepActions;
	}
}
