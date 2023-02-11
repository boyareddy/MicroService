package com.roche.connect.imm.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDetailDTO;
import com.roche.connect.imm.utils.RestClient;
import com.roche.connect.imm.utils.UrlConstants;

@Service
public class RmmIntegrationService {

	@Value("${connect.rmm_host_url}")
	private String rmmHostUrl;

	/** The rmm sample result by run result id. */
	@Value("${connect.rmm_run_result_by_id_url}")
	private String rmmRunResultById;

	/** The rmm sample result by OP container. */
	@Value("${connect.rmm_get_sample_result}")
	private String rmmGetSampleResult;

	private ObjectMapper objectMapper = new ObjectMapper();

	/** The logger. */
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());


	public long saveRunResult(RunResultsDTO runResult) throws IOException {
		return saveRunResult(runResult, null);
	}
	

	public long saveRunResult(RunResultsDTO runResult, String token) throws IOException {
		logger.info("Going to save runResult in RMM: --------" + objectMapper.writeValueAsString(runResult));

		String url = rmmHostUrl + UrlConstants.RMM_RUN_RESULT_URL;
		Long runResultId = RestClient.post(url, Entity.entity(runResult, MediaType.APPLICATION_JSON), token, null, Long.class);

		logger.info("Result message for RMM. Run Result record ID:" + runResultId);

		if (runResultId > 0) {
			return runResultId;
		}

		return 0;
	}

	public List<SampleResultsDTO> getSampleResultsFromUrl(String url) throws UnsupportedEncodingException {

		List<SampleResultsDTO> listOfSamples = null;
		if (url == null) {
			logger.error("Url is missing to get Sample Results from RMM.");
			return Collections.emptyList();
		}

		Invocation.Builder responseClient = RestClientUtil
				.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null);
		Response response = responseClient.get();
		logger.info("Response status from RMM while getting sample results: " + response.getStatus());

		if (response.getStatus() == 204) {
			return Collections.emptyList();
		}

		listOfSamples = response.readEntity(new GenericType<List<SampleResultsDTO>>() {
		});

		try {
			logger.info("Response from RMM Sample Results: " + objectMapper.writeValueAsString(listOfSamples));
		} catch (JsonProcessingException e) {
			logger.info("Exception on parsing list of samples: " + e.getMessage());
		}

		return listOfSamples;

	}
	
	public List<SampleResultsDTO> getSampleResults(String deviceId, String processStepName, String outputContainerId,
			String outputContainerPosition, String inputContainerId, String inputContainerPosition,
			String accessioningId) throws IOException {
		
		return getSampleResults(deviceId, processStepName, outputContainerId, outputContainerPosition, inputContainerId,
				inputContainerPosition, accessioningId, null);
	}
	
	public List<SampleResultsDTO> getSampleResults(String deviceId, String processStepName, String outputContainerId,
			String outputContainerPosition, String inputContainerId, String inputContainerPosition,
			String accessioningId, String token) throws IOException {

		try {

			StringBuilder url = new StringBuilder(rmmHostUrl + UrlConstants.RMM_SAMPLE_RESULTS_URL);

			List<String> param = new ArrayList<>();
			if (!StringUtils.isEmpty(outputContainerId)) {
				param.add("outputContainerId=" + outputContainerId);
			}

			if (!StringUtils.isEmpty(outputContainerPosition)) {
				param.add("outputContainerPosition=" + outputContainerPosition);
			}

			if (!StringUtils.isEmpty(inputContainerId)) {
				param.add("inputContainerId=" + inputContainerId);
			}

			if (!StringUtils.isEmpty(inputContainerPosition)) {
				param.add("inputContainerPosition=" + inputContainerPosition);
			}

			if (!StringUtils.isEmpty(deviceId)) {
				param.add("deviceId=" + deviceId);
			}

			if (!StringUtils.isEmpty(processStepName)) {
				param.add("processStepName=" + processStepName);
			}

			if (!StringUtils.isEmpty(accessioningId)) {
				param.add("accessioningId=" + accessioningId);
			}

			if (!param.isEmpty()) {
				url.append("?");
			}

			boolean flag = false;
			for (String p : param) {

				if (flag) {
					url.append("&" + p);
				} else {
					url.append(p);
					flag = true;
				}
			}

			logger.info("MessageProcessorService.getSampleResults: " + url);
			Invocation.Builder responseClient = RestClientUtil
					.getBuilder(URLEncoder.encode(url.toString(), CharEncoding.UTF_8), null);
			
			if (token != null)
				responseClient.header(RestClient.COOKIE_STR, RestClient.COOKIE_KEY + token);
			
			Response response = responseClient.get();
			logger.info("Response status from RMM while getting sample results: " + response.getStatus());

			if (response.getStatus() == 204 || response.getStatus() == 500 || (response.getStatus() + "").startsWith("4"))
				return Collections.emptyList();

			List<SampleResultsDTO> sampleResults = response.readEntity(new GenericType<List<SampleResultsDTO>>() {
			});

			logger.info("Response from RMM Sample Results: " + objectMapper.writeValueAsString(sampleResults));
			return sampleResults;
		} catch (JsonProcessingException e) {
			logger.error("Exception on parsing list of samples: " + e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			logger.error("Failed to get SampleResults, message: " + e.getMessage(), e);
			throw e;
		}

	}

	public RunResultsDTO getRunResults(String deviceId, String processstepname, String runstatus, String token)
			throws IOException {

		if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(processstepname))
			return null;

		String url = rmmHostUrl + UrlConstants.RMM_RUN_RESULT_URL + "?deviceid=" + deviceId + "&processstepname="
				+ processstepname;

		if (StringUtils.isNotBlank(runstatus)) 
			url = url + "&runstatus=" + runstatus;
		
		logger.info("Get Run Results URL: " + url);

		RunResultsDTO runResultsDTO = null;

		try {
			Invocation.Builder builder = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8),
					null);

			if (token != null)
				builder.header(RestClient.COOKIE_STR, RestClient.COOKIE_KEY + token);

			runResultsDTO = builder.get(new GenericType<RunResultsDTO>() {
			});

			logger.info("RunResults message from RMM: " + objectMapper.writeValueAsString(runResultsDTO));
		} catch (IOException e) {
			logger.error("IOException occured while getting run informations: " + e.getMessage(), e);
		}

		return runResultsDTO;
	}

	public RunResultsDTO getRunResultsById(Long id) throws UnsupportedEncodingException {
		return getRunResultsById(id, null);
	}
	
	public RunResultsDTO getRunResultsById(Long id, String token) throws UnsupportedEncodingException {
		
		logger.info("MessageProcessorService.getRunResultsById: " + id);
		String url = rmmRunResultById + "/" + id;
		logger.info("Get Run Results ById, URL: " + url);

		RunResultsDTO runResultsDTO = null;

		try {
			Invocation.Builder responseClient = RestClientUtil
					.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null);
			
			if (token != null)
				responseClient.header(RestClient.COOKIE_STR, RestClient.COOKIE_KEY + token);
			
			runResultsDTO = responseClient.get(new GenericType<RunResultsDTO>() {
			});

			logger.info("RunResults By Id from RMM: " + objectMapper.writeValueAsString(runResultsDTO));
		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException occured while getting RunResults by Id: " + e.getMessage(), e);
		}

		return runResultsDTO;
	}

	public RunResultsDTO getRunResultsByDeviceRunId(String deviceRunId) throws UnsupportedEncodingException {
		return getRunResultsByDeviceRunId(deviceRunId, null);
	}
	
	public RunResultsDTO getRunResultsByDeviceRunId(String deviceRunId, String token) throws UnsupportedEncodingException {
		
		logger.info("MessageProcessorService.getRunResultsByDeviceRunId: " + deviceRunId);
		String url = rmmHostUrl + UrlConstants.RMM_RUN_RESULTS_BY_DEVICE_RUNID_URL;
		RunResultsDTO runResultsDTO = null;

		try {
			if (deviceRunId != null) {
				url = url + "?deviceRunId=" + deviceRunId;
			} else {
				return null;
			}

			logger.info("Get Run Results by DeviceRunId, URL: " + url);

			Invocation.Builder responseClient = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8),
					null);

			if (token != null)
				responseClient.header(RestClient.COOKIE_STR, RestClient.COOKIE_KEY + token);
			
			Response response = responseClient.get();
			if (response.getStatus() == HttpStatus.SC_OK) {
				runResultsDTO = response.readEntity(new GenericType<RunResultsDTO>() {
				});
				logger.info(
						"RunResults by DeviceRunId from RMM: " + objectMapper.writeValueAsString(runResultsDTO));
				return runResultsDTO;
			}

		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException occured while getting RunResults by DeviceRunId: " + e.getMessage());
		}

		return null;
	}
	
	public RunResultsDTO getRunResultsAndSampleResults(String deviceRunId, String processStepName, String deviceId)
			throws UnsupportedEncodingException {

		return getRunResultsAndSampleResults(deviceRunId, processStepName, deviceId, null);
	}

	public RunResultsDTO getRunResultsAndSampleResults(String deviceRunId, String processStepName, String deviceId,
			String token) throws UnsupportedEncodingException {
		
		try {
			logger.info("MessageProcessorService.getSampleandRunResults().  deviceRunId=" + deviceRunId
					+ "\tprocessStepName=" + processStepName + "\tdeviceId=" + deviceId);
			
			String url = rmmHostUrl + UrlConstants.RMM_RUN_RESULTS_AND_SAMPLE_URL;
			RunResultsDTO runResultsDTO = null;
			
			if (StringUtils.isNotBlank(deviceRunId) && StringUtils.isNotBlank(processStepName)
					&& StringUtils.isNotBlank(deviceId)) {
				url = url + "?deviceRunId=" + deviceRunId + "&processStepName=" + processStepName + "&deviceId="
						+ deviceId;
			} else {
				return null;
			}

			logger.info("Get RunResultsAndSampleResults URL: " + url);

			Invocation.Builder responseClient = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8),
					null);
			if (token != null)
				responseClient.header(RestClient.COOKIE_STR, RestClient.COOKIE_KEY + token);
			
			Response response = responseClient.get();
			if (response.getStatus() == 200) {
				runResultsDTO = response.readEntity(new GenericType<RunResultsDTO>() {
				});
				logger.info(
						"RunResultsAndSampleResults message from RMM: " + objectMapper.writeValueAsString(runResultsDTO));
				return runResultsDTO;
			}

		} catch (JsonProcessingException e) {
			logger.info("JsonProcessingException occured while getting RunResultsAndSampleResults: " + e.getMessage(), e);
		}

		return null;
	}

	public boolean updateRunResult(RunResultsDTO runResultDto) {
		try {
			logger.info("RmmIntegrationService.updateRunResult: "
					+ objectMapper.writeValueAsString(runResultDto));

			String url = rmmHostUrl + UrlConstants.RMM_RUN_RESULT_URL;
			RestClientUtil.putMethodCall(url, runResultDto, null);
			return true;
		} catch (JsonProcessingException e) {
			logger.info("Exception occurred while parsing run results: " + e.getMessage());
		} catch (HMTPException e) {
			logger.info("Exception occurred while submitting run results: " + e.getMessage(), e);
		}
		return false;
	}
	
	public boolean updateRunResult(RunResultsDTO runResultDto, String token) {
		try {
			logger.info("MessageProcessorService.getRunResultsByDeviceRunId: "
					+ objectMapper.writeValueAsString(runResultDto));

			String url = rmmHostUrl + UrlConstants.RMM_RUN_RESULT_URL;
			RestClient.put(url, runResultDto, token, null);
			return true;

		} catch (JsonProcessingException | UnsupportedEncodingException e) {
			logger.info("Exception occurred while parsing run results: " + e.getMessage());
		}
		return false;
	}

	public List<SampleResultsDTO> getSampleResultsByProcessStepAndAccessioningId(String processStep,
			String accessioningId) throws UnsupportedEncodingException {

		if (rmmGetSampleResult == null) {
			logger.error(
					"RMM Microservice url to get sample results using process step and input container id is missing.");
			return Collections.emptyList();
		}

		String url = rmmGetSampleResult + "?processStepName=" + processStep + "&accessioningId=" + accessioningId;
		logger.info("URL to Sample Results from RMM: " + url);
		return getSampleResultsFromUrl(url);

	}

	public String getMolecularIdByProcessStepNameAndAccessioningId(String processStepName, String accessioningId) {

		try {
			List<SampleResultsDTO> lpPreSampleRestults = getSampleResultsByProcessStepAndAccessioningId(processStepName,
					accessioningId);

			SampleResultsDTO sampleDTO = !lpPreSampleRestults.isEmpty() ? lpPreSampleRestults.get(0) : null;

			if (sampleDTO != null) {
				
				if (sampleDTO.getSampleResultsDetail().isEmpty())
					return null;

				Optional<SampleResultsDetailDTO> findFirst = sampleDTO.getSampleResultsDetail().parallelStream()
						.filter(e -> e.getAttributeName().equalsIgnoreCase("molecularId")).findFirst();

				return findFirst.isPresent() ? findFirst.get().getAttributeValue() : null;

			}

		} catch (UnsupportedEncodingException e) {
			logger.info("Exception on RmmIntegrationService.getMolecularId: " + e.getMessage());
		}

		return null;
	}
}
