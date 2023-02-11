/*******************************************************************************
 * RunServiceImpl.java Version: 1.0 Authors: umashankar d *********************
 * Copyright (c) 2018 Roche Sequencing Solutions (RSS) - CONFIDENTIAL All Rights
 * Reserved. NOTICE: All information contained herein is, and remains the
 * property of COMPANY. The intellectual and technical concepts contained herein
 * are proprietary to COMPANY and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from COMPANY.
 * Access to the source code contained herein is hereby forbidden to anyone
 * except current COMPANY employees, managers or contractors who have executed
 * Confidentiality and Non-disclosure agreements explicitly covering such access
 * The copyright notice above does not evidence any actual or intended
 * publication or disclosure of this source code, which includes information
 * that is confidential and/or proprietary, and is a trade secret, of COMPANY.
 * ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC
 * DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS WRITTEN
 * CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE
 * LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS SOURCE
 * CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS TO
 * REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR
 * SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 * ********************* ChangeLog: umashankar-d@hcl.com : Updated copyright
 * headers ********************* Description: *********************
 ******************************************************************************/
package com.roche.connect.htp.adapter.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.forte.ForteJobTypes;
import com.roche.connect.common.forte.SecondaryJobDetailsDTO;
import com.roche.connect.common.forte.SecondarySampleAssayDetails;
import com.roche.connect.common.forte.TertiaryJobDetailsDTO;
import com.roche.connect.common.htp.ComplexIdDetailsDTO;
import com.roche.connect.common.htp.status.HTPConstants;
import com.roche.connect.common.htp.status.HTPStatusConstants;
import com.roche.connect.common.htp.status.StatusConstants;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.htp.adapter.model.ComplexIdDetails;
import com.roche.connect.htp.adapter.model.Cycle;
import com.roche.connect.htp.adapter.model.ForteJob;
import com.roche.connect.htp.adapter.readrepository.ComplexIdDetailsReadRepository;
import com.roche.connect.htp.adapter.readrepository.CycleReadRepository;
import com.roche.connect.htp.adapter.readrepository.ForteJobReadRepository;
import com.roche.connect.htp.adapter.util.CycleStatus;
import com.roche.connect.htp.adapter.util.ForteConstants;
import com.roche.connect.htp.adapter.util.HTPAdapterConstants;
import com.roche.connect.htp.adapter.util.PatientCache;
import com.roche.connect.htp.adapter.writerepository.CycleWriteRepository;
import com.roche.connect.htp.adapter.writerepository.ForteJobWriteRepository;

/**
 * The Class RunServiceImpl.
 */
@Service
public class RunServiceImpl implements RunService {

	@Value("${adapter.imm_complex_id_details}")
	private String getComplexIdDetailsUrl;

	/** The update run info url. */
	@Value("${adapter.imm_run_update}")
	private String updateRunInfoUrl;

	@Value("${adapter.htp_regEx}")
	private String regEx;

	/** The HTP mount path. */
	@Value("${adapter.htp_mount}")
	private String htpMountPath;

	/** The HTP mount path. */
	@Value("${adapter.forte_mount}")
	private String forteMountPath;

	@Value("${adapter.adm_notification_url}")
	private String admNotificationURL;
	/** The web services. */

	@Value("${adapter.cycleFileSize}")
	private String cycleFileNameAndSize;

	@Value("${pas.device_remote_url}")
	String deviceURL;

	@Autowired
	WebServices webServicesImpl;

	@Autowired
	ComplexIdDetailsReadRepository complexIdDetailsReadRepository;

	@Autowired
	ForteJobReadRepository forteJobReadRepository;

	@Autowired
	ForteJobWriteRepository forteJobWriteRepository;

	@Autowired
	CycleReadRepository cycleReadRepository;

	@Autowired
	CycleWriteRepository cycleWriteRepository;
	/** The logger. */
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	

	


	@Value("${adapter.imm_getPatientDetails}")
	private String getPatientDetailsUrl;

	@Value("${pas.authenticate_url}")
	private String loginUrl;

	@Value("${pas.login_entity}")
	private String loginEntity;

	@Autowired
	ObjectMapper objectMapper;

	PatientCache cache;

	private static final ThreadLocal<Map<Integer, Cycle>> addCycle = new ThreadLocal<>();

	private static AtomicInteger startIndex = new AtomicInteger(0);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.roche.connect.htp.adapter.services.RunService#convertJsonToRun(java.
	 * util. Map)
	 */

	@Override
	public RunResultsDTO convertJsonToRun(Map<String, Object> runObj) throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat(HTPAdapterConstants.DATE_FORMAT.getText());

		logger.info("Enter RunServiceImpl.convertJsonToRun method ");

		RunResultsDTO runDto = new RunResultsDTO();
		String deviceId = null;
		String runStatus = null;
		if (runObj.containsKey(HTPAdapterConstants.RUN_ID.getText())) {
			runDto.setDeviceRunId(runObj.get(HTPAdapterConstants.RUN_ID.getText()).toString());
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			ComplexIdDetails complexIdDetails = complexIdDetailsReadRepository
					.findByDeviceRunId(runObj.get(HTPAdapterConstants.RUN_ID.getText()).toString(), domainId);
			deviceId = complexIdDetails.getDeviceId();
		}
		if (runObj.containsKey(HTPAdapterConstants.INSTRUMENT_ID.getText())) {
			runDto.setDeviceId(runObj.get(HTPAdapterConstants.INSTRUMENT_ID.getText()).toString());
		} else {
			runDto.setDeviceId(deviceId);
		}
		if (runObj.containsKey(HTPAdapterConstants.RUN_STATUS.getText())) {
			runStatus = runObj.get(HTPAdapterConstants.RUN_STATUS.getText()).toString();

			if (runStatus.equalsIgnoreCase(StatusConstants.CREATED)) {
				runDto.setRunStatus(StatusConstants.CREATED);
			} else if (runStatus.equalsIgnoreCase(StatusConstants.STARTED)) {
				runDto.setRunStatus(StatusConstants.STARTED);
			} else if (runStatus.equalsIgnoreCase(StatusConstants.INPROCESS)) {
				runDto.setRunStatus(StatusConstants.RUN_RESULT_INPROGRESS);
			} else if (runStatus.equalsIgnoreCase(StatusConstants.RUN_RESULT_COMPLETED)) {
				runDto.setRunStatus(StatusConstants.RUN_RESULT_COMPLETED);
			} else {
				runDto.setRunStatus(StatusConstants.RUN_RESULT_FAILED);
			}
		}

		if (runObj.containsKey(HTPAdapterConstants.SEQUENCING_RUN_START_TIME.getText())) {
			runDto.setRunStartTime(
					new Timestamp(format.parse(runObj.get(HTPAdapterConstants.SEQUENCING_RUN_START_TIME.getText()).toString()).getTime()));
		}

		if (runObj.containsKey(HTPAdapterConstants.SEQUENCING_RUN_END_TIME.getText())) {
			runDto.setRunCompletionTime(
					new Timestamp(format.parse(runObj.get(HTPAdapterConstants.SEQUENCING_RUN_END_TIME.getText()).toString()).getTime()));
		}

		if (runObj.containsKey(HTPAdapterConstants.RUN_START_USER_ID.getText())) {
			runDto.setOperatorName(runObj.get(HTPAdapterConstants.RUN_START_USER_ID.getText()).toString());
		}

		runDto.setProcessStepName(HTPConstants.PROCESS_STEP);
		if (runObj.containsKey(HTPAdapterConstants.EST_TIME_REM.getText())) {
			runDto.setRunRemainingTime(Long.parseLong(runObj.get(HTPAdapterConstants.EST_TIME_REM.getText()).toString()));
		}
		runDto.setUpdatedBy(HTPConstants.UPDATED_BY);
		runDto.setUpdatedDateTime(new Timestamp(new Date().getTime()));
		runDto.setComments(HTPConstants.COMMENTS);
		List<RunReagentsAndConsumablesDTO> runReagentsAndConsumablesList = new ArrayList<>();

		// Adding Consumables, Sequencing Run Kit & System fluid details

		if (runObj.containsKey(HTPAdapterConstants.CONSUMABLE_DEVICE_FIRST_USE_DATE.getText())) {
			runReagentsAndConsumablesList.add(getRunReagentsAndConsumablesDTOByAttributeNameValueAndType(
					"consumableDeviceFirstUseDate", runObj.get(HTPAdapterConstants.CONSUMABLE_DEVICE_FIRST_USE_DATE.getText()).toString(), "C"));
		}

		if (runObj.containsKey(HTPAdapterConstants.CONSUMABLE_DEVICE_PART_NUMBER.getText())) {
			runReagentsAndConsumablesList.add(getRunReagentsAndConsumablesDTOByAttributeNameValueAndType(
					"consumableDevicePartNumber", runObj.get(HTPAdapterConstants.CONSUMABLE_DEVICE_PART_NUMBER.getText()).toString(), "C"));
		}

		if (runObj.containsKey(HTPAdapterConstants.CONSUMABLE_DEVICE_EXPIRATION.getText())) {
			runReagentsAndConsumablesList.add(getRunReagentsAndConsumablesDTOByAttributeNameValueAndType(
					"consumableDeviceExpiration", runObj.get(HTPAdapterConstants.CONSUMABLE_DEVICE_EXPIRATION.getText()).toString(), "C"));
		}

		if (runObj.containsKey(HTPAdapterConstants.SEQUENCING_RUN_KIT_PART_NUMBER.getText())) {
			runReagentsAndConsumablesList.add(getRunReagentsAndConsumablesDTOByAttributeNameValueAndType(
					"seqKitPartNumber", runObj.get(HTPAdapterConstants.SEQUENCING_RUN_KIT_PART_NUMBER.getText()).toString(), "R"));
		}

		if (runObj.containsKey(HTPAdapterConstants.SEQUENCING_RUN_KIT_EXPIRATION.getText())) {

			runReagentsAndConsumablesList.add(getRunReagentsAndConsumablesDTOByAttributeNameValueAndType(
					"seqKitExpiration", runObj.get(HTPAdapterConstants.SEQUENCING_RUN_KIT_EXPIRATION.getText()).toString(), "R"));
		}

		if (runObj.containsKey(HTPAdapterConstants.SYSTEM_FLUID_PART_NUMBER.getText())) {
			runReagentsAndConsumablesList.add(getRunReagentsAndConsumablesDTOByAttributeNameValueAndType(
					"systemFluidPartNumber", runObj.get(HTPAdapterConstants.SYSTEM_FLUID_PART_NUMBER.getText()).toString(), "R"));
		}

		if (runObj.containsKey(HTPAdapterConstants.SYSTEM_FLUID_EXPIRATION.getText())) {
			runReagentsAndConsumablesList.add(getRunReagentsAndConsumablesDTOByAttributeNameValueAndType(
					"systemFluidExpiration", runObj.get(HTPAdapterConstants.SYSTEM_FLUID_EXPIRATION.getText()).toString(), "R"));
		}

		runDto.setRunReagentsAndConsumables(runReagentsAndConsumablesList);

		List<RunResultsDetailDTO> runResultsDetailDTOList = new ArrayList<>();

		if (runObj.containsKey(HTPAdapterConstants.COMPLEX_ID.getText())) {
			runResultsDetailDTOList
					.add(getRunResultsDetailDTOByAttributeNameAndValue(HTPAdapterConstants.COMPLEXID.getText(), runObj.get(HTPAdapterConstants.COMPLEX_ID.getText()).toString()));
		}
		if (runObj.containsKey(HTPAdapterConstants.REFERENCE_ID.getText())) {
			runResultsDetailDTOList.add(
					getRunResultsDetailDTOByAttributeNameAndValue("referenceId", runObj.get(HTPAdapterConstants.REFERENCE_ID.getText()).toString()));
		}

		if (runObj.containsKey(HTPAdapterConstants.RUN_DATA_FOLDER_PRIMARY.getText())) {
			runResultsDetailDTOList.add(getRunResultsDetailDTOByAttributeNameAndValue("runDataFolderPrimary",
					runObj.get(HTPAdapterConstants.RUN_DATA_FOLDER_PRIMARY.getText()).toString()));
		}

		if (runObj.containsKey(HTPAdapterConstants.RUN_PROTOCOL.getText())) {
			runResultsDetailDTOList.add(
					getRunResultsDetailDTOByAttributeNameAndValue("protocol", runObj.get(HTPAdapterConstants.RUN_PROTOCOL.getText()).toString()));
		}

		if (runObj.containsKey(HTPAdapterConstants.SOFTWARE_VERSION_NSE.getText())) {
			runResultsDetailDTOList.add(getRunResultsDetailDTOByAttributeNameAndValue("sofVersionNse",
					runObj.get(HTPAdapterConstants.SOFTWARE_VERSION_NSE.getText()).toString()));
		}

		if (runObj.containsKey(HTPAdapterConstants.SOFTWARE_VERSION_NSP.getText())) {
			runResultsDetailDTOList.add(getRunResultsDetailDTOByAttributeNameAndValue("sofVersionNsp",
					runObj.get(HTPAdapterConstants.SOFTWARE_VERSION_NSP.getText()).toString()));
		}

		if (runObj.containsKey(HTPAdapterConstants.SEQUENCING_VERIFICATION_LOAD_TIME.getText())) {
			runResultsDetailDTOList.add(getRunResultsDetailDTOByAttributeNameAndValue(
					"sequencingVerificationLoadDatetime", runObj.get(HTPAdapterConstants.SEQUENCING_VERIFICATION_LOAD_TIME.getText()).toString()));
		}

		if (runObj.containsKey(HTPAdapterConstants.RUN_DATA_FOLDER_TMP.getText())) {
			runResultsDetailDTOList.add(getRunResultsDetailDTOByAttributeNameAndValue("runDataFolderTmp",
					runObj.get(HTPAdapterConstants.RUN_DATA_FOLDER_TMP.getText()).toString()));
		}

		if (runObj.containsKey(HTPAdapterConstants.SOFTWARE_VERSION_NSC.getText())) {
			runResultsDetailDTOList.add(getRunResultsDetailDTOByAttributeNameAndValue("sofVersionNsc",
					runObj.get(HTPAdapterConstants.SOFTWARE_VERSION_NSC.getText()).toString()));
		}
		if (runObj.containsKey(HTPConstants.OUTPUTFOLDER)) {
			runResultsDetailDTOList.add(getRunResultsDetailDTOByAttributeNameAndValue(HTPConstants.OUTPUTFOLDER,
					runObj.get(HTPConstants.OUTPUTFOLDER).toString()));
		}

		if (runObj.containsKey(HTPAdapterConstants.SOFTWARE_VERSION_NSA.getText())) {
			runResultsDetailDTOList.add(getRunResultsDetailDTOByAttributeNameAndValue("sofVersionNsa",
					runObj.get(HTPAdapterConstants.SOFTWARE_VERSION_NSA.getText()).toString()));
		}

		if (runObj.containsKey(HTPAdapterConstants.LANE_NUMBER.getText())) {
			runResultsDetailDTOList
					.add(getRunResultsDetailDTOByAttributeNameAndValue("laneNo", runObj.get(HTPAdapterConstants.LANE_NUMBER.getText()).toString()));
		}

		if (runObj.containsKey(HTPAdapterConstants.RUN_NUMBER.getText())) {
			runResultsDetailDTOList
					.add(getRunResultsDetailDTOByAttributeNameAndValue("runNumber", runObj.get(HTPAdapterConstants.RUN_NUMBER.getText()).toString()));
		}
		if (runObj.containsKey(HTPAdapterConstants.TOTAL_CYCLES.getText())) {
			runResultsDetailDTOList.add(
					getRunResultsDetailDTOByAttributeNameAndValue("totalCycles", runObj.get(HTPAdapterConstants.TOTAL_CYCLES.getText()).toString()));
		}

		runDto.setRunResultsDetail(runResultsDetailDTOList);
		logger.info("Finished RunServiceImpl.convertJsonToRun method with runObj: " + runDto.toString());

		return runDto;
	}

	public RunResultsDetailDTO getRunResultsDetailDTOByAttributeNameAndValue(String name, String value) {
		RunResultsDetailDTO runResultsDetailDTO = new RunResultsDetailDTO();
		runResultsDetailDTO.setAttributeName(name);
		runResultsDetailDTO.setAttributeValue(value);
		runResultsDetailDTO.setUpdatedBy(HTPConstants.UPDATED_BY);
		return runResultsDetailDTO;
	}

	public RunReagentsAndConsumablesDTO getRunReagentsAndConsumablesDTOByAttributeNameValueAndType(String attributeName,
			String attributeValue, String type) {
		RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
		runReagentsAndConsumablesDTO.setAttributeName(attributeName);
		runReagentsAndConsumablesDTO.setAttributeValue(attributeValue);
		runReagentsAndConsumablesDTO.setType(type);
		runReagentsAndConsumablesDTO.setUpdatedBy(HTPConstants.UPDATED_BY);
		return runReagentsAndConsumablesDTO;
	}

	public Map<String, String> convertRunToJson(RunResultsDTO runResultsDTO) {
		Map<String, String> runDetails = new HashMap<>();
		runDetails.put(HTPAdapterConstants.RUN_ID.getText(), runResultsDTO.getDeviceRunId());
		runDetails.put(HTPAdapterConstants.INSTRUMENT_ID.getText(), runResultsDTO.getDeviceId());
		runDetails.put(HTPAdapterConstants.RUN_STATUS.getText(), runResultsDTO.getRunStatus().toLowerCase());
		runDetails.put(HTPAdapterConstants.SEQUENCING_RUN_START_TIME.getText(), runResultsDTO.getRunStartTime().toString());
		runDetails.put(HTPAdapterConstants.SEQUENCING_RUN_END_TIME.getText(), runResultsDTO.getRunCompletionTime().toString());
		runDetails.put(HTPAdapterConstants.RUN_START_USER_ID.getText(), runResultsDTO.getOperatorName());
		runDetails.put(HTPAdapterConstants.EST_TIME_REM.getText(),
				runResultsDTO.getRunRemainingTime() != null ? runResultsDTO.getRunRemainingTime().toString() : null);
		for (RunResultsDetailDTO runResultsDetails : runResultsDTO.getRunResultsDetail()) {
			String attributeName = runResultsDetails.getAttributeName();
			String attributeValue = runResultsDetails.getAttributeValue();

			if (attributeName.equalsIgnoreCase(HTPAdapterConstants.COMPLEXID.getText())) {
				runDetails.put(HTPAdapterConstants.COMPLEX_ID.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("referenceId")) {
				runDetails.put(HTPAdapterConstants.REFERENCE_ID.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("runDataFolderPrimary")) {
				runDetails.put(HTPAdapterConstants.RUN_DATA_FOLDER_PRIMARY.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("protocol")) {
				runDetails.put(HTPAdapterConstants.RUN_PROTOCOL.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("sofVersionNse")) {
				runDetails.put(HTPAdapterConstants.SOFTWARE_VERSION_NSE.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("sofVersionNsp")) {
				runDetails.put(HTPAdapterConstants.SOFTWARE_VERSION_NSP.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("sofVersionNsa")) {
				runDetails.put(HTPAdapterConstants.SOFTWARE_VERSION_NSA.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("sofVersionNsc")) {
				runDetails.put(HTPAdapterConstants.SOFTWARE_VERSION_NSC.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("sequencingVerificationLoadDatetime")) {
				runDetails.put(HTPAdapterConstants.SEQUENCING_VERIFICATION_LOAD_TIME.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("runDataFolderTmp")) {
				runDetails.put(HTPAdapterConstants.RUN_DATA_FOLDER_TMP.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("laneNo")) {
				runDetails.put(HTPAdapterConstants.LANE_NUMBER.getText(), attributeValue);
			} else if (attributeName.equalsIgnoreCase("runNumber")) {
				runDetails.put(HTPAdapterConstants.RUN_NUMBER.getText(), attributeValue);
			} else if (attributeName.equalsIgnoreCase("totalCycles")) {
				runDetails.put(HTPAdapterConstants.TOTAL_CYCLES.getText(), attributeValue);
			}

		}
		for (RunReagentsAndConsumablesDTO consumablesDTO : runResultsDTO.getRunReagentsAndConsumables()) {
			String attributeName = consumablesDTO.getAttributeName();
			String attributeValue = consumablesDTO.getAttributeValue();

			if (attributeName.equalsIgnoreCase("consumableDevicePartNumber")) {
				runDetails.put(HTPAdapterConstants.CONSUMABLE_DEVICE_PART_NUMBER.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("consumableDeviceFirstUseDate")) {
				runDetails.put(HTPAdapterConstants.CONSUMABLE_DEVICE_FIRST_USE_DATE.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("consumableDeviceExpiration")) {
				runDetails.put(HTPAdapterConstants.CONSUMABLE_DEVICE_EXPIRATION.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("systemFluidPartNumber")) {
				runDetails.put(HTPAdapterConstants.SYSTEM_FLUID_PART_NUMBER.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("seqKitPartNumber")) {
				runDetails.put(HTPAdapterConstants.SEQUENCING_RUN_KIT_PART_NUMBER.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("seqKitExpiration")) {
				runDetails.put(HTPAdapterConstants.SEQUENCING_RUN_KIT_EXPIRATION.getText(), attributeValue);
			}

			else if (attributeName.equalsIgnoreCase("systemFluidExpiration")) {
				runDetails.put(HTPAdapterConstants.SYSTEM_FLUID_EXPIRATION.getText(), attributeValue);
			}

		}

		return runDetails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.roche.connect.htp.adapter.services.RunService#createRun(com.roche.
	 * connect .common.rmm.dto.RunResultsDTO)
	 */
	@Override
	public void createRun(RunResultsDTO run) throws HMTPException {

		logger.info("Entered RunServiceImpl.createRun method with runObj: " + run.toString());

		ObjectMapper mapper = new ObjectMapper();

		String json = null;

		try {
			json = mapper.writeValueAsString(run);
			logger.info("json value : " + json);
			logger.info("createRunInfoUrl value : " + updateRunInfoUrl);
			webServicesImpl.postRequest(updateRunInfoUrl, json);
		} catch (Exception e) {
			logger.error("Error while create Run Information : " + e.getMessage());
			throw new HMTPException(e);
		}

		logger.info("Finished RunServiceImpl.createRun method with Response: ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.roche.connect.htp.adapter.services.RunService#updateRun(java.lang.
	 * String, java.util.Map)
	 */
	@Override
	public Response updateRun(String runId, RunResultsDTO run) throws HMTPException, JsonProcessingException {
		logger.info("Entered RunServiceImpl.updateRun method with runObj: " + run.toString());

		ObjectMapper mapper = new ObjectMapper();
		String url = updateRunInfoUrl + "/" + runId;

		String json = null;
		Response response = null;

		try {
			json = mapper.writeValueAsString(run);

			logger.info("json value ::;: " + json);
			logger.info("updateRunInfoUrl value : " + url);
			response = webServicesImpl.putRequest(url, json);
		} catch (Exception e) {
			logger.error("Error while update Run Information : " + e.getMessage());
			throw new HMTPException(e);
		}
		logger.info("Finished RunServiceImpl.updateRun method with Response: ");
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.roche.connect.htp.adapter.services.RunService#checkFreeSpace()
	 */
	@Override
	public JsonNode checkFreeSpace() throws JSONException, IOException {
		logger.info("Entered RunServiceImpl.checkFreeSpace method ");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		File file = new File(htpMountPath);
		json.put("freespace", file.getUsableSpace());

		logger.info("Finished RunServiceImpl.checkFreeSpace method " + json.toString());

		return json;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.roche.connect.htp.adapter.services.RunService#validatePreviousCycle(java.
	 * util.Map, java.util.List)
	 */
	@Override
	public void validatePreviousCycle(String runId, int cycleNumber, long domainId, String deviceId) {

		logger.info(" RunServiceImpl <= :: validatePreviousCycle()");

		Optional<List<Cycle>> cycles = Optional.ofNullable(cycleReadRepository.findByRunId(runId, domainId).stream()
				.sorted(Comparator.comparing(Cycle::getCyclesNumber))
				.filter(e -> e.getType().equalsIgnoreCase("fastq.gz"))
				.filter(e -> e.getStatus().equalsIgnoreCase(CycleStatus.COMPLETE.toString()))
				.collect(Collectors.toList()));
		if (cycles.isPresent()) {
			this.verifyOutOfSeqCycleNumber(runId, cycleNumber, deviceId, cycles);
		}

		logger.info(" RunServiceImpl  ::=>  validatePreviousCycle() ");

	}

	private void verifyOutOfSeqCycleNumber(String runId, int cycleNumber, String deviceId,
			Optional<List<Cycle>> cycles) {

		logger.debug(" RunServiceImpl <= :: verifyOutOfSeqCycleNumber() ");

		List<Integer> missingCycle = findMissingCycleNumber(cycleNumber, cycles);

		if (!missingCycle.isEmpty())
			this.sendNotification(NotificationGroupType.OUT_OF_SEQ_CYCLE_NUMBER_HTP, deviceId, runId,
					missingCycle.stream().map(x -> x + "").collect(Collectors.joining(",")));
	}

	public List<Integer> findMissingCycleNumber(int cycleNumber, Optional<List<Cycle>> cycles) {

		logger.debug(" RunServiceImpl <= :: getMissingCycleNumber() ");

		List<Integer> missingCycles = new ArrayList<>();
		addCycle.set(new ConcurrentHashMap<>());
		if (cycles.isPresent()) {
			cycles.get().forEach(x -> addCycle.get().put(x.getCyclesNumber(), x));
			startIndex.set(
					cycles.get().size() > 1 ? cycles.get().get((cycles.get().size() - 1) - 1).getCyclesNumber() : 0);

			IntStream.range(startIndex.get(), addCycle.get().get(cycleNumber) != null ? cycleNumber : cycleNumber + 1)
					.filter(x -> addCycle.get().get(x) == null
							|| !addCycle.get().get(x).getStatus().equalsIgnoreCase(CycleStatus.COMPLETE.toString()))
					.forEach(x -> missingCycles.add(x));
		}
		return missingCycles;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.roche.connect.htp.adapter.services.RunService#updateCycle(java.util.
	 * Map, java.lang.String)
	 */
	@Override
	public Cycle updateCycle(Map<String, Object> cycle, String id) {
		logger.info("Entered RunServiceImpl.updateCycle method ");

		try {
			Cycle cycleObj = new Cycle();
			cycleObj.setPath(cycle.get(HTPAdapterConstants.PATH.getText()).toString());
			cycleObj.setChecksum(cycle.get(HTPAdapterConstants.CHECKSUM.getText()).toString());
			String cycleNumber = String.valueOf(cycle.get(HTPAdapterConstants.CYCLE.getText()));
			String type = cycle.get(HTPAdapterConstants.TYPE.getText()).toString();
			if (String.valueOf(cycle.get(HTPAdapterConstants.CYCLE.getText())).matches("\\d+")) {
				cycleObj.setCyclesNumber(Integer.parseInt(cycleNumber));
			}
			if (this.isValidType().test(type)) {
				cycleObj.setType(type);
			}
			cycleObj.setRunId(id);
			cycleObj.setSendToSecondaryFlag(ForteConstants.NO);
			logger.info("Finished RunServiceImpl.updateCycle method with cycle Object: " + cycleObj.toString());
			return cycleObj;
		} catch (Exception e) {
			logger.error("ERR:- updateCycle", e.getMessage());
		}
		return null;

	}

	@Override
	public Predicate<String> isValidType() {

		return x -> Pattern.compile("(fastq.gz|PrimaryAnalysisVersionInfo.txt|metrics.txt|ExpState.json|ExpAnno.json)")
				.matcher(x).matches();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.roche.connect.htp.adapter.services.RunService#checkSum(java.lang.
	 * String, java.lang.String)
	 */
/**	@Override
	public Boolean checkSum(String path, String checkSum) throws IOException {
		logger.info("Entered RunServiceImpl.checkSum method ");
		InputStream in = null;
		byte[] output;

		try {
			in = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			logger.error("Error in RunServiceImpl.checkSum method ");
			return false;
		}

		MessageDigest digest = null;

		try {
			digest = MessageDigest.getInstance(HTPConstants.ALGORITHM);

			byte[] block = new byte[1024];
			int length;

			while ((length = in.read(block)) > 0) {
				if (digest != null) {
					digest.update(block, 0, length);
				} else {
					throw new NullPointerException("Message digest cannnot be null");
				}
			}
		}

		catch (NoSuchAlgorithmException | IOException e) {
			logger.error("Error in  RunServiceImpl.checkSum method ");
			return false;
		} finally {
			in.close();
		}
		if (digest != null) {
			output = digest.digest();
		} else {
			throw new NullPointerException("Message digest can't be null");
		}
		logger.info("Finished RunServiceImpl.checkSum method checksum from client: " + checkSum
				+ " Chceksum from server : " + javax.xml.bind.DatatypeConverter.printHexBinary(output));

		return javax.xml.bind.DatatypeConverter.printHexBinary(output).equalsIgnoreCase(checkSum);
	}*/

	/**
	 * Gets the primary file path.
	 *
	 * @param string
	 *            the string
	 * 
	 * @return the primary file path
	 */

	@Override
	public Response getComplexIdDetails(String complexId) {
		logger.info("Entered RunServiceImpl.getComplexIdDetails method ");
		String url = getComplexIdDetailsUrl + "?complexId=" + complexId;
		return webServicesImpl.getRequest(url);

	}

	@Override
	public Map<String, Object> getOrderDetails(long id, ComplexIdDetailsDTO complexIdDetailsDTO) {
		SimpleDateFormat dateFormatterrmm = new SimpleDateFormat(HTPAdapterConstants.SIMPLE_DATE_FORMAT_RMM.getText());
		logger.info("Finished RunServiceImpl.getOrderDetails method ");
		Map<String, Object> orderDetails = new HashMap<>();
		orderDetails.put(HTPAdapterConstants.RUN_PROTOCOL.getText(), complexIdDetailsDTO.getRunProtocol());
		orderDetails.put("status", complexIdDetailsDTO.getStatus().toString());
		orderDetails.put("complex_created_datetime",
				dateFormatterrmm.format(complexIdDetailsDTO.getComplexCreatedDatetime()));
		orderDetails.put(HTPAdapterConstants.REFERENCE_ID.getText(), id);

		return orderDetails;
	}

	@Override
	public Response getRun(String runId) {
		logger.info("Entered RunServiceImpl.getRun method ");
		String url = updateRunInfoUrl + "/" + runId;
		return webServicesImpl.getRequest(url);
	}

	@Override
	public void updateRunStatus(String id, RunResultsDTO runResultDTO) {
		logger.info("Entered RunServiceImpl.updateRun method with runObj: " + runResultDTO.toString());

		ObjectMapper mapper = new ObjectMapper();
		String url = updateRunInfoUrl + "/" + id;

		String json = null;
		Response resp = null;
		try {
			json = mapper.writeValueAsString(runResultDTO);

			logger.info("json value :::: " + json);
			logger.info("updateRunInfoUrl value : " + url);

			resp = webServicesImpl.putRequest(url, json);

			if (resp.getStatus() != 202) {
				logger.error("Error while update Run Status Information : " + resp.getEntity().toString());
			} else {
				logger.info(
						"Finished RunServiceImpl.updateRunStaus method with Response: " + resp.getEntity().toString());
			}
		} catch (Exception e) {
			logger.error("Error while update Run Status Information : " + e.getMessage());
		}

	}

	@Override
	// @Async
	public RunResultsDTO convertRunStatusToRun(Map<String, Object> requestbody) {
		RunResultsDTO runDto = new RunResultsDTO();

		List<RunResultsDetailDTO> runResultsDetailDTOList = new ArrayList<>();
		String runStatus = requestbody.get(HTPAdapterConstants.RUN_STATUS.getText()).toString();
		if (runStatus.equalsIgnoreCase(HTPStatusConstants.CREATED.getText())) {
			runDto.setRunStatus(HTPStatusConstants.CREATED.getText());
		} else if (runStatus.equalsIgnoreCase(HTPStatusConstants.STARTED.getText())) {
			runDto.setRunStatus(HTPStatusConstants.STARTED.getText());
		} else if (runStatus.equalsIgnoreCase(HTPStatusConstants.IN_PROCESS.getText())) {
			runDto.setRunStatus(HTPStatusConstants.IN_PROCESS.getText());
		} else if (runStatus.equalsIgnoreCase(HTPStatusConstants.COMPLETED.getText())) {
			runDto.setRunStatus(HTPStatusConstants.COMPLETED.getText());
		} else if (runStatus.equalsIgnoreCase(HTPStatusConstants.TRANSFERCOMPLETE.getText())) {
			runDto.setRunStatus(HTPStatusConstants.TRANSFERCOMPLETE.getText());
		} else {
			runDto.setRunStatus(HTPStatusConstants.FAILED.getText());
		}
		if (requestbody.get(HTPAdapterConstants.EST_TIME_REM.getText()) != null)
			runDto.setRunRemainingTime(Long.valueOf(requestbody.get(HTPAdapterConstants.EST_TIME_REM.getText()).toString()));
		if (requestbody.containsKey(HTPAdapterConstants.COMPLEX_ID.getText())) {
			RunResultsDetailDTO runResultsDetailDTO = new RunResultsDetailDTO();
			runResultsDetailDTO.setAttributeName(HTPAdapterConstants.COMPLEXID.getText());
			runResultsDetailDTO.setAttributeValue(requestbody.get(HTPAdapterConstants.COMPLEX_ID.getText()).toString());
			runResultsDetailDTOList.add(runResultsDetailDTO);
		}
		if (requestbody.containsKey(HTPConstants.OUTPUTFOLDER)) {
			RunResultsDetailDTO runResultsDetailDTO = new RunResultsDetailDTO();
			runResultsDetailDTO.setAttributeName(HTPConstants.OUTPUTFOLDER);
			runResultsDetailDTO.setAttributeValue(requestbody.get(HTPConstants.OUTPUTFOLDER).toString());

			runResultsDetailDTOList.add(runResultsDetailDTO);
		}

		runDto.setRunResultsDetail(runResultsDetailDTOList);

		return runDto;
	}

	@Override
	public String getMountPath(String deviceId, String path) {
		if (deviceId.equalsIgnoreCase(ForteConstants.FORTE)) {
			return path.replace("/mnt/", forteMountPath);
		} else {
			String delimiter = "/";
			String filePath = htpMountPath + delimiter + deviceId;
			return path.replace("ips", filePath);
		}
	}

	@Override
	public SecondaryJobDetailsDTO getPatientDetails(String htpComplexId, Cycle cycle, String jobId) {
		logger.info("Entering getPatientDetails");
		List<SecondarySampleAssayDetails> secondarySampleDetails = null;
		List<Map<String, String>> sampleDetails = null;
		SecondaryJobDetailsDTO secondaryJobDetailsDTO = new SecondaryJobDetailsDTO();

		cache = PatientCache.getInstance();

		if (cache.containskey(htpComplexId)) {
			secondaryJobDetailsDTO = (SecondaryJobDetailsDTO) cache.get(htpComplexId);
			secondaryJobDetailsDTO.setId(jobId);
			return secondaryJobDetailsDTO;
		} else {
			Response response = getPatientDetailsFromIMM(htpComplexId);

			if (response.getStatus() != 200) {
				logger.info("getPatientDetailsFromIMM : ", secondaryJobDetailsDTO);
				return null;
			} else {

				try {
					secondaryJobDetailsDTO.setKind(ForteConstants.SECONDARY);
					secondaryJobDetailsDTO.setSecondaryChecksum(cycle.getChecksum());
					secondaryJobDetailsDTO.setSecondaryInfile(cycle.getPath());
					secondarySampleDetails = response.readEntity(new GenericType<List<SecondarySampleAssayDetails>>() {
					});
					if (!secondarySampleDetails.isEmpty()) {
						sampleDetails = getSampleDetails(secondarySampleDetails);
					}
					secondaryJobDetailsDTO.setSecondarySampleDetails(sampleDetails);
					cache.put(htpComplexId, secondaryJobDetailsDTO);
					secondaryJobDetailsDTO.setId(jobId);
					return secondaryJobDetailsDTO;

				} catch (Exception e) {
					logger.info("Exception while creating secondary" + e.getMessage());
				}

				return null;
			}

		}
	}

	public List<Map<String, String>> getSampleDetails(List<SecondarySampleAssayDetails> secondarySampleDetails) {

		List<Map<String, String>> sampleList = new ArrayList<>();
		for (SecondarySampleAssayDetails sampleAssay : secondarySampleDetails) {
			Map<String, String> sampleMap = new HashMap<>();
			if (sampleAssay.getMaternalAge() != null) {
				sampleMap.put(ForteConstants.MATERNALAGE, sampleAssay.getMaternalAge().toString());
			}
			if (sampleAssay.getGestationalAgeWeeks() != null) {
				sampleMap.put(ForteConstants.GESTATIONALAGEWEEKS, sampleAssay.getGestationalAgeWeeks().toString());
			}
			if (sampleAssay.getGestationalAgeDays() != null) {
				sampleMap.put(ForteConstants.GESTATIONALAGEDAYS, sampleAssay.getGestationalAgeDays().toString());
			}
			if (sampleAssay.getEggDonor() != null) {
				sampleMap.put(ForteConstants.EGGSOURCE, sampleAssay.getEggDonor());
			}
			if (sampleAssay.getFetusNumber() != null) {
				sampleMap.put(ForteConstants.NUMBERFETUSES, sampleAssay.getFetusNumber());
			}
			if (sampleAssay.getAccessioningId() != null) {
				sampleMap.put(ForteConstants.SAMPLETAG, sampleAssay.getAccessioningId());
			}
			if (sampleAssay.getMolecularId() != null) {
				sampleMap.put(ForteConstants.MID, sampleAssay.getMolecularId());
			}
			sampleList.add(sampleMap);
		}
		return sampleList;
	}

	@Override
	public Response getPatientDetailsFromIMM(String htpComplexId) {
		ObjectMapper mapper = new ObjectMapper();
		String url = getPatientDetailsUrl + "?messagetype=" + ForteConstants.GET;
		Map<String, String> body = new HashMap<>();
		body.put(HTPAdapterConstants.COMPLEXID.getText(), htpComplexId);
		try {
			return webServicesImpl.postRequest(url, mapper.writeValueAsString(body));
		} catch (JsonProcessingException e) {
			logger.error("RunServiceImpl.getPatientDetailsFromIMM : Error while requesting patient details from IMM");
			return Response.serverError().build();
		}
	}

	@Override
	public ForteJob getForteJob(Cycle cycle) {
		ForteJob forteJob = new ForteJob();
		Timestamp currentDateTime = new Timestamp(new Date().getTime());

		forteJob.setCycleId(cycle);
		forteJob.setDeviceRunId(cycle.getRunId());
		forteJob.setJobType(ForteConstants.SECONDARY);
		forteJob.setSentToTertiary(ForteConstants.NO);
		forteJob.setCreatedDateandTime(currentDateTime);

		return forteJob;
	}

	@Override
	public Response updateJobStatusToIMM(String jobStatus, String jobType, String complexId) {
		ObjectMapper mapper = new ObjectMapper();
		String url = getPatientDetailsUrl + "?messagetype=" + ForteConstants.PUT + "&deviceId="
				+ ForteConstants.FORTE_DEVICE_ID;

		Map<String, String> status = new HashMap<>();
		status.put("jobStatus", jobStatus);
		status.put("jobType", jobType);
		status.put(HTPAdapterConstants.COMPLEXID.getText(), complexId);
		try {
			return webServicesImpl.postRequest(url, mapper.writeValueAsString(status));
		} catch (JsonProcessingException e) {
			logger.error("RunServiceImpl.updateJobStatus : Error while parsing and sending the status to IMM");
			return Response.serverError().build();
		}

	}

	@Override
	public List<ForteJob> getForteJobDetailsForTertiary() {
		logger.info("Entering RunService.getForteJobDetailsForTertiary");

		List<ForteJob> forteJob = new ArrayList<>();
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			List<String> forteDeviceRunIds = forteJobReadRepository.findDistinctDeviceRunIdBySentToTertiaryAndJobType(
					ForteConstants.NO, ForteConstants.SECONDARY, ForteConstants.DONE, domainId);

			for (String e : forteDeviceRunIds) {

				List<Cycle> cycle = cycleReadRepository.findCycleForTertiary(e, ForteConstants.YES,
						ForteConstants.COMPLETE, ForteConstants.FILETYPE, domainId);

				if (cycle.isEmpty()) {
					forteJob = forteJobReadRepository
							.findByDeviceRunIdAndSentToTertiaryAndJobTypeAndCompanyOrderByCreatedDateTimeAsc(e,
									ForteConstants.NO, ForteConstants.SECONDARY, domainId);
					return forteJob;
				}
			}
		} catch (Exception e) {
			logger.error("RunServiceImpl.getTertiaryJobDetails : Error while get tertiary details: " + e.getMessage());
		}

		return forteJob;
	}

	@Override
	public TertiaryJobDetailsDTO getTertiaryJobDetails() throws HMTPException {
		logger.info("Entering RunService.getTertiaryJobDetails");

		try {
			List<ForteJob> forteJobs = getForteJobDetailsForTertiary();

			if (!forteJobs.isEmpty()) {

				String deviceId = forteJobs.get(0).getDeviceRunId();

				logger.info("Forte Tertiary job is exist");

				TertiaryJobDetailsDTO tertiaryJobDetails = new TertiaryJobDetailsDTO();
				tertiaryJobDetails.setKind(ForteJobTypes.TERTIARY.getText().toLowerCase());

				List<String> checksums = new ArrayList<>();
				List<String> inFiles = new ArrayList<>();

				forteJobs.forEach(e -> {
					checksums.add(e.getOutFileChecksum());
					inFiles.add(e.getOutFilePath());
					e.setSentToTertiary(ForteConstants.YES);
					forteJobWriteRepository.save(e);
				});

				tertiaryJobDetails.setTertiary_checksums(checksums);
				tertiaryJobDetails.setTertiary_infiles(inFiles);

				ForteJob forteJob = createForteJobFromTertiaryJobDetails(deviceId);
				forteJobWriteRepository.save(forteJob);
				tertiaryJobDetails.setId(String.valueOf(forteJob.getId()));

				return tertiaryJobDetails;
			}
		} catch (Exception e) {
			logger.info("Excetion occured on getTertiaryJobDetails" + e.getMessage());
			throw new HMTPException(e);
		}

		return null;

	}

	@Override
	public SecondaryJobDetailsDTO getSecondaryJobDetails() throws HMTPException {
		logger.info("Entering RunService.getSecondaryJobDetails");
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		Cycle cycle = cycleReadRepository.findTopByStatusAndSendToSecondaryFlagAndTypeOrderByUpdatedDateTimeAsc(
				ForteConstants.HTPCOMPLETE, ForteConstants.NO, ForteConstants.FILETYPE, domainId);

		if (cycle != null) {
			logger.info("Cycle is exist");
			ComplexIdDetails complexIdDetails = complexIdDetailsReadRepository.findByDeviceRunId(cycle.getRunId(),
					domainId);

			if (complexIdDetails != null) {

				ForteJob forteJob = getForteJob(cycle);
				forteJobWriteRepository.save(forteJob);

				logger.info("Forte job details saved");

				String htpComplexId = complexIdDetails.getComplexId();
				cycle.setSendToSecondaryFlag(ForteConstants.YES);
				cycleWriteRepository.save(cycle);
				logger.info("Cycle table updated: " + cycle.getId());
				return getPatientDetails(htpComplexId, cycle, String.valueOf(forteJob.getId()));
			} else {
				throw new HMTPException("Complex details not exist with the device run Id");
			}

		}

		return null;
	}

	@Override
	public ForteJob createForteJobFromTertiaryJobDetails(String deviceId) {
		ForteJob forteJob = new ForteJob();
		forteJob.setDeviceRunId(deviceId);
		forteJob.setJobType(ForteJobTypes.TERTIARY.getText());
		forteJob.setSentToTertiary(ForteConstants.YES);
		return forteJob;
	}

	@Override
	public String getFolder(String path) {
		Pattern patern = Pattern.compile(regEx + "(.*?)" + HTPConstants.SEPATATOR);
		Matcher matcher = patern.matcher(path);
		if (matcher.find()) {
			return "..." + HTPConstants.SEPATATOR + matcher.group(1);
		}
		return null;
	}

	@Override
	@Async
	public void sendNotification(NotificationGroupType messageGroup, String... missingCycle) {

		try {
			logger.info("Thread ::=> " + Thread.currentThread().getName() + " sending notification to ADM ::==> "
					+ messageGroup);
			List<String> contents = Stream.of(missingCycle).collect(Collectors.toList());
			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());

			AdmNotificationService.sendNotification(messageGroup, contents, token, admNotificationURL);

		} catch (Exception e) {
			logger.error("Error while sending a notification! <==" + e.getLocalizedMessage());
		}
	}

	@Override
	public boolean checkFileSize(String path) {
		logger.info("ChekFileSize for :" + path);
		long actualFileSize;
		Optional<String> first = Arrays.asList(cycleFileNameAndSize.split(",")).stream().flatMap(s -> {
			return Arrays.asList(s.split("[^a-zA-Z0-9]")).stream();
		}).filter(c -> path.toLowerCase().contains(c.toLowerCase())).findFirst();
		StringBuilder actualFileType = null;
		StringBuilder tempSubString;
		StringBuilder conditionAndSize;
		StringBuilder condition;
		StringBuilder expectedSize;
		File actualFile = new File(path);
		actualFileSize = actualFile.length();
		if (first.isPresent()) {
			actualFileType = new StringBuilder(first.get());
		}
		logger.info("Actual file size from HTP is :" + actualFileType);
		if (actualFileType != null) {
			tempSubString = new StringBuilder(
					cycleFileNameAndSize.substring(cycleFileNameAndSize.indexOf(actualFileType.toString())));
			if (tempSubString.toString().contains(",")) {
				conditionAndSize = new StringBuilder(tempSubString.substring(0, tempSubString.indexOf(",")));
			} else {
				conditionAndSize = tempSubString;
			}
			Pattern pattern = Pattern.compile("[a-zA-Z0-9]");
			Matcher matcher = pattern.matcher(conditionAndSize);
			condition = new StringBuilder(matcher.replaceAll(""));
			expectedSize = new StringBuilder(conditionAndSize
					.substring(conditionAndSize.indexOf(condition.toString()) + 1, conditionAndSize.length()));
			logger.info("Actual File size in bytes:" + actualFileSize);
			return checkFileSize(condition.toString(), Long.parseLong(expectedSize.toString()), actualFileSize);
		}
		return false;
	}

	public boolean checkFileSize(String condition, long expectedSize, long actualFileSize) {
		boolean isMatching = false;
		switch (condition) {
		case "=":
			if (expectedSize >= actualFileSize)
				isMatching = true;
			break;
		case ">":
			if (expectedSize < actualFileSize)
				isMatching = true;
			break;
		case "<":
			if (expectedSize > actualFileSize)
				isMatching = true;
			break;
		default:
			break;
		}
		logger.info("size :" + expectedSize + "   condition :" + condition + "   actualFileSize :" + actualFileSize);
		return isMatching;
	}

	@Override
	public String getDeviceId(long userId) {
		logger.info("Entered RunServiceImpl.getDeviceId() :" + userId);
		String url = deviceURL + "/json/device/fetch/expr?filterExpression=user.id=" + userId;
		JSONArray responseObj = null;
		Response response = webServicesImpl.getRequest(url);
		String responseStr = response.readEntity(String.class);
		String deviceId = null;

		logger.info("response from DM is "+response);

		try {
			responseObj = new JSONArray(responseStr);
			deviceId =  responseObj.getJSONObject(0).get(HTPAdapterConstants.SERIAL_NO.getText()).toString();
			if(deviceId.isEmpty()) {
				logger.error("DeviceID is empty");
			}
		} catch (JSONException e) {
			logger.info("Error in RunServiceImpl.getDeviceId()"+e.getMessage());
		}
		logger.info("deviceId in RunServiceImpl.getDeviceId() "+deviceId);
		return deviceId;
	}

	public String getExpectedStatus(String runId) {
		logger.info("Entered RunServiceImpl.getExpectedStatus()");
		logger.info("RunID :" + runId);
		Response response = getRun(runId);
		logger.info("Response code from IMM in getExpectedStatus()"+response.getStatus());
		if (response.getStatus() == 200) {
			RunResultsDTO runResultsDTO = response.readEntity(RunResultsDTO.class);
			if (runResultsDTO != null) {
				List<SampleResultsDTO> sampleResults = (List<SampleResultsDTO>) runResultsDTO.getSampleResults();
				if (!sampleResults.isEmpty()) {
					logger.info("expectedStatus from getExpectedStatus():" + sampleResults.get(0).getResult());
					return sampleResults.get(0).getResult();
				} else {
					return null;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	@Override
	public boolean isInValidRunSequence(String runId, String actualRunStatus, long domainId) {
		logger.info("Entered RunServiceImpl.isInValidRunSequence()");
		logger.info("Actual status from RunServiceImpl.isInValidRunSequence() : " + actualRunStatus);
		switch (actualRunStatus.toLowerCase()) {
		case "started":
			Response response = getRun(runId);
			if (response.getStatus() == 200) {
			RunResultsDTO runResultsDTO = response.readEntity(RunResultsDTO.class);
			if (runResultsDTO != null)
				return HTPStatusConstants.CREATED.getText().equalsIgnoreCase(runResultsDTO.getRunStatus());
			return false;
			}return false;
		case "inprocess":
			return HTPStatusConstants.STARTED.getText().equalsIgnoreCase(getExpectedStatus(runId));
		case "completed":
				return HTPStatusConstants.IN_PROCESS.getText().equalsIgnoreCase(getExpectedStatus(runId));
		case "failed":
			return HTPStatusConstants.IN_PROCESS.getText().equalsIgnoreCase(getExpectedStatus(runId));
		case "transfercompleted":
				return HTPStatusConstants.COMPLETED.getText().equalsIgnoreCase(getExpectedStatus(runId));
		default:
			return false;
		}
	}

	@Override
	public void sendNotification(String param1, String param2, NotificationGroupType type) {
		List<String> contents = new ArrayList<>();
		contents.add(param1);
		contents.add(param2);
		String token = (String) ThreadSessionManager.currentUserSession()
				.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
		try {
			AdmNotificationService.sendNotification(type, contents, token, admNotificationURL);
		} catch (HMTPException e) {
			logger.info("Error while sending" + type + " notification :" + e.getMessage());
		}
	}

	/**
	 * @return the addcycle
	 */
	public static ThreadLocal<Map<Integer, Cycle>> getAddcycle() {
		return addCycle;
	}

	
	@Override
	public Boolean validateChecksum(String path, String inputChecksum) {

		logger.info("Entered RunServiceImpl.validateChecksum method: Path" + path + "\t Checksum: " + inputChecksum);

		try {

			MessageDigest digest = MessageDigest.getInstance(HTPConstants.ALGORITHM);

			FileInputStream fis = new FileInputStream(path);

			byte[] byteArray = new byte[1024];
			int bytesCount = 0;

			while ((bytesCount = fis.read(byteArray)) != -1) {
				digest.update(byteArray, 0, bytesCount);
			}

			fis.close();

			byte[] bytes = digest.digest();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			logger.info("Obtained Checksum: " + sb.toString());

			if (inputChecksum.equals(sb.toString()))
				return true;
			else
				return false;

		} catch (NoSuchAlgorithmException | IOException e) {
			logger.info("Exception on RunServiceImpl.validateChecksum: " + e.getMessage());
			return false;
		}
	}

}