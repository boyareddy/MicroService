
/*******************************************************************************
 * HtpSimulatorRestApiImpl.java Version: 1.0 Authors: umashankar d
 * ********************* Copyright (c) 2018 Roche Sequencing Solutions (RSS) -
 * CONFIDENTIAL All Rights Reserved. NOTICE: All information contained herein
 * is, and remains the property of COMPANY. The intellectual and technical
 * concepts contained herein are proprietary to COMPANY and may be covered by
 * U.S. and Foreign Patents, patents in process, and are protected by trade
 * secret or copyright law. Dissemination of this information or reproduction of
 * this material is strictly forbidden unless prior written permission is
 * obtained from COMPANY. Access to the source code contained herein is hereby
 * forbidden to anyone except current COMPANY employees, managers or contractors
 * who have executed Confidentiality and Non-disclosure agreements explicitly
 * covering such access The copyright notice above does not evidence any actual
 * or intended publication or disclosure of this source code, which includes
 * information that is confidential and/or proprietary, and is a trade secret,
 * of COMPANY. ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE,
 * OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS
 * WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF
 * APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS
 * SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS TO
 * REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR
 * SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 * ********************* ChangeLog: umashankar-d@hcl.com : Updated copyright
 * headers ********************* Description: *********************
 ******************************************************************************/
package com.roche.connect.htp.adapter.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.htp.ComplexIdDetailsDTO;
import com.roche.connect.common.htp.status.HTPConstants;
import com.roche.connect.common.htp.status.HTPStatusConstants;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.htp.adapter.model.ComplexIdDetails;
import com.roche.connect.htp.adapter.model.Cycle;
import com.roche.connect.htp.adapter.model.Notification;
import com.roche.connect.htp.adapter.readrepository.ComplexIdDetailsReadRepository;
import com.roche.connect.htp.adapter.readrepository.CycleReadRepository;
import com.roche.connect.htp.adapter.readrepository.ForteJobReadRepository;
import com.roche.connect.htp.adapter.services.RunService;
import com.roche.connect.htp.adapter.services.RunServiceImpl;
import com.roche.connect.htp.adapter.services.WebServices;
import com.roche.connect.htp.adapter.util.CycleStatus;
import com.roche.connect.htp.adapter.util.HTPAdapterConstants;
import com.roche.connect.htp.adapter.writerepository.ComplexIdDetailsWriteRepository;
import com.roche.connect.htp.adapter.writerepository.CycleWriteRepository;

/**
 * The Class HtpSimulatorRestApiImpl.
 */

@Component
public class HtpAdapterRestApiImpl implements HtpAdapterRestApi {

	/** The logger. */
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	ComplexIdDetailsDTO complexIdDetailsDTO;

	/** The htp simulator service. */
	@Autowired
	RunService htpSimulatorService;

	/** The cycle write repository. */
	@Autowired
	CycleReadRepository cycleReadRepository;

	@Autowired
	CycleWriteRepository cycleWriteRepository;

	@Autowired
	ComplexIdDetailsReadRepository complexIdDetailsReadRepository;

	@Autowired
	ComplexIdDetailsWriteRepository complexIdDetailsWriteRepository;

	@Autowired
	ForteJobReadRepository forteJobReadRepository;

	@Autowired
	WebServices webServices;

	@Value("${adapter.adm_notification_url}")
	private String admNotificationURL;

	/** The login url. */
	@Value("${pas.authenticate_url}")
	private String loginUrl;

	/** The login entity. */
	@Value("${pas.login_entity}")
	private String loginEntity;

	@Value("${pas.device_remote_url}")
	String deviceEndPoint;

	
	private String instrumentId;
	private boolean helloFlag = false;
	private boolean isGoodBye = false;

	public boolean getHelloFlag() {
		return helloFlag;
	}

	public void setHelloFlag(boolean helloFlag) {
		this.helloFlag = helloFlag;
	}

	public boolean getGoodBye() {
		return isGoodBye;
	}

	public void setGoodBye(boolean isGoodBye) {
		this.isGoodBye = isGoodBye;
	}

	public String getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}

	
	String token = (String) ThreadSessionManager.currentUserSession()
			.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.roche.connect.htp.adapter.rest.HtpSimulatorRestApi#hitPing(java.util.
	 * Map)
	 */
	@Override
	public Response hitPing() throws HMTPException {

		logger.info("Enter HtpSimulatorRestApiImpl.hitPing method ");

		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		return Response.ok(Status.OK).build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.roche.connect.htp.adapter.rest.HtpSimulatorRestApi#hitHello(java.util
	 * .Map)
	 */
	@Override
	public Response hitHello(Map<String, Object> requestBody) throws HMTPException {

		logger.info("Enter HtpSimulatorRestApiImpl.hitHello method ");

		logger.info("Finished HtpSimulatorRestApiImpl.Hello method ");
		instrumentId = requestBody.get(HTPAdapterConstants.INSTRUMENT_ID.getText()).toString();
		setHelloFlag(true);
		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		return Response.ok(Status.OK).build();
	}

	/**
	 * @see com.roche.connect.htp.adapter.rest.HtpAdapterRestApi#isGoodBye
	 */
	@Override
	public Response goodBye() {
		logger.info("Enter HtpSimulatorRestApiImpl.goodBye method ");
		setGoodBye(true);
		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		logger.info("Finished HtpSimulatorRestApiImpl.goodBye method ");
		return Response.ok(Status.OK).build();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.roche.connect.htp.adapter.rest.HtpSimulatorRestApi#createRun(java.
	 * util.Map)
	 */
	@Override
	public Response createRun(Map<String, Object> runObject) throws HMTPException, ParseException {

		logger.info("Enter HtpSimulatorRestApiImpl.createRun method hi");
		String runId = runObject.get(HTPAdapterConstants.RUN_ID.getText()).toString();
		String complexId = runObject.get(HTPAdapterConstants.COMPLEX_ID.getText()).toString();
		String protocol = runObject.get(HTPAdapterConstants.RUN_PROTOCOL.getText()).toString();
		String tempFolder = runObject.get(HTPAdapterConstants.RUN_DATA_FOLDER_TMP.getText()).toString();
		String primaryFolderPath = runObject.get(HTPAdapterConstants.RUN_DATA_FOLDER_PRIMARY.getText()).toString();

		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		// if the complex id is present in the db and the post call is coming
		// for the 2nd time we should consider that as out of sequence
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();

		if (!(StringUtils.isBlank(runId) || StringUtils.isBlank(tempFolder) || StringUtils.isBlank(primaryFolderPath)
				|| StringUtils.isBlank(protocol) || StringUtils.isBlank(complexId))) {
			long referenceId = Long.parseLong(String.valueOf((runObject.get(HTPAdapterConstants.REFERENCE_ID.getText()))));

			ComplexIdDetails complexIdDetails = complexIdDetailsReadRepository.findById(referenceId, domainId);
			if (complexIdDetails != null && complexIdDetails.getComplexId().equalsIgnoreCase(complexId)) {
				if (!(StringUtils.isBlank(complexIdDetails.getDeviceRunId()))) {
					logger.info(HTPAdapterConstants.SEQ_MISMATCH.getText());
					htpSimulatorService.sendNotification(
							htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()),
							runId, NotificationGroupType.OUT_OF_SEQ_MSG_HTP);
					return Response.status(400).entity(HTPAdapterConstants.SEQ_MISMATCH.getText()).build();
				}
				complexIdDetails.setDeviceRunId(runId);
				complexIdDetails.setDeviceId(runObject.get(HTPAdapterConstants.INSTRUMENT_ID.getText()).toString());
				Company company = new Company();
				company.setId(ThreadSessionManager.currentUserSession().getAccessorCompanyId());
				complexIdDetails.setCompany(company);
				complexIdDetailsWriteRepository.save(complexIdDetails);
				runObject.put(HTPConstants.OUTPUTFOLDER, "");
				htpSimulatorService.createRun(htpSimulatorService.convertJsonToRun(runObject));

				logger.info("Finished HtpSimulatorRestApiImpl.createRun method ");
				return Response.ok().status(201).entity(HTPAdapterConstants.CREATED.getText()).build();
			} else {
				htpSimulatorService.sendNotification(
						htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()), runId,
						NotificationGroupType.OUT_OF_SEQ_MSG_HTP);
				return Response.status(400).entity(HTPAdapterConstants.SEQ_MISMATCH.getText()).build();
			}
		} else {
			List<String> contents = new ArrayList<>();
			contents.add(runId);
			contents.add(htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()));
			AdmNotificationService.sendNotification(NotificationGroupType.INCORRECT_RUN_DETAILS_HTP, contents,token,
					admNotificationURL);
			return Response.status(400).entity("INCORRECT_RUN_DETAILS_HTP").build();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.roche.connect.htp.adapter.rest.HtpSimulatorRestApi#updateRun(java.
	 * lang.String, java.util.Map)
	 */
	@Override
	public Response updateRun(String runId, Map<String, Object> requestbody)
			throws ParseException, JsonProcessingException{
		logger.info("Enter HtpSimulatorRestApiImpl.UpdateRun method");

		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		// if the complexId's status is used that means the post run has already
		// happened
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		long referenceId = Long.parseLong(String.valueOf((requestbody.get(HTPAdapterConstants.REFERENCE_ID.getText()))));
		
		ComplexIdDetails complexIdDetailsbyid = complexIdDetailsReadRepository.findById(referenceId, domainId);
		if (!(complexIdDetailsbyid.getDeviceRunId()).equals(runId)) {
			try{
			String deviceID = (String) requestbody.get(HTPAdapterConstants.INSTRUMENT_ID.getText());
			List<String> contents = new ArrayList<>();
			contents.add(deviceID);
			contents.add(runId);
			AdmNotificationService.sendNotification(NotificationGroupType.INCORRECT_RUN_ID_HTP, contents,token,
					admNotificationURL);
			logger.error("Sending Notification to ADM while updating Run for Incorrect Run ID");
			return Response.status(Status.NOT_FOUND).entity("RunId is not valid for :" + requestbody.get(HTPAdapterConstants.COMPLEX_ID.getText())).build();
			}catch (Exception e) {
				logger.info("Exception throwed while sending Notififcation");
				return Response.status(Status.NOT_FOUND).entity("Exception throwed in No Content").build();
			}
		}

		ComplexIdDetails complexIdDetails = complexIdDetailsReadRepository.findByDeviceRunId(runId, domainId);
		if (complexIdDetails == null || complexIdDetails.getDeviceRunId() == null) {

			logger.info(HTPAdapterConstants.SEQ_MISMATCH.getText());
			htpSimulatorService.sendNotification(
					htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()), runId,
					NotificationGroupType.OUT_OF_SEQ_MSG_HTP);
			return Response.status(Status.BAD_REQUEST).entity(HTPAdapterConstants.SEQ_MISMATCH.getText()).build();
		}
		RunResultsDTO runResultsDTO = htpSimulatorService.convertJsonToRun(requestbody);
		Response response = null;
		try {
			String deviceID = runResultsDTO.getDeviceId();
			response = htpSimulatorService.updateRun(runId, runResultsDTO);

			// Sending Notification to ADM while updating Run for Incorrect Run
			// ID
			if (response.getStatus() == 200) {
				return Response.ok(200).entity("Run Updated Successfully").build();
			} else if (response.getStatus() == 404) {
				try {
					// Sending Notification while updating run for RunId Not
					// Found
					List<String> contents = new ArrayList<>();
					contents.add(deviceID);
					contents.add(runId);
					AdmNotificationService.sendNotification(NotificationGroupType.INCORRECT_RUN_ID_HTP, contents,token,
							admNotificationURL);
					logger.error("Sending Notification to ADM while updating Run for Incorrect Run ID");
					return Response.status(Status.NOT_FOUND).entity("RunId Not available in Adapter :" + runResultsDTO.getRunResultId()).build();
				} catch (Exception e) {
					logger.info("Exception throwed while sending Notififcation");
					return Response.status(Status.NOT_FOUND).entity("Exception throwed in No Content").build();
				}
			} else {
				logger.info("Run Status other than 404");
				return Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity("Error occurred in while processing the updateRun. Please contact the administrator")
						.build();
			}

		} catch (HMTPException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error occurred in while processing the updateRun. Please contact the administrator")
					.build();
		}
	}

	public Response getRun(String runId) {
		logger.info("Enter HtpSimulatorRestApiImpl.getRun method ");
		Response response = null;

		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		// if the complexId's status is used that means the post run has already
		// happened
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();

		ComplexIdDetails complexIdDetails = complexIdDetailsReadRepository.findByDeviceRunId(runId, domainId);
		if (complexIdDetails == null || complexIdDetails.getDeviceRunId() == null) {
			logger.info(HTPAdapterConstants.SEQ_MISMATCH.getText());
			htpSimulatorService.sendNotification(
					htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()), runId,
					NotificationGroupType.OUT_OF_SEQ_MSG_HTP);
			return Response.status(400).entity(HTPAdapterConstants.SEQ_MISMATCH.getText()).build();

		}
		response = htpSimulatorService.getRun(runId);

		if (response.getStatus() == 200) {
			RunResultsDTO runResultsDTO = response.readEntity(RunResultsDTO.class);
			Map<String, String> runResults = htpSimulatorService.convertRunToJson(runResultsDTO);
			return Response.ok(200).entity(runResults).build();

		} else if (response.getStatus() == 404) {
			try {
				// Sending Notification to ADM while getting Run for Incorrect
				// Run ID

				List<String> contents = new ArrayList<>();
				contents.add("HTP");
				contents.add(runId);
				AdmNotificationService.sendNotification(NotificationGroupType.INCORRECT_RUN_ID_HTP, contents,token,
						admNotificationURL);
				logger.error("Sending Notification to ADM while updating Run for Incorrect Run ID");
				return Response.status(Status.NOT_FOUND).entity("No Content").build();
			} catch (Exception e) {
				logger.info("Exception throwed while sending Notififcation");
				return Response.status(Status.NOT_FOUND).entity("Exception throwed in Notification : Adapter").build();
			}
		} else {
			return response;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.roche.connect.htp.adapter.rest.HtpSimulatorRestApi#updateCycle(java.
	 * lang.String, java.util.Map)
	 */
	@Override
	public Response updateCycle(String id, Map<String, Object> runObject) throws IOException {

		logger.info(" id: " + id);
		logger.info(" runObject Keys: " + runObject.keySet());
		logger.info(" runObject Keys: " + runObject.values());
		logger.info("Enter HtpSimulatorRestApiImpl.updateCycle method ");

		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		// SEQ_MISMATCH
		if (!htpSimulatorService.isInValidRunSequence(id, HTPStatusConstants.COMPLETED.getText(), domainId)) {
			logger.info(HTPAdapterConstants.SEQ_MISMATCH.getText());
			htpSimulatorService.sendNotification(
					htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()), id,
					NotificationGroupType.OUT_OF_SEQ_MSG_HTP);
			return Response.status(400).entity(HTPAdapterConstants.SEQ_MISMATCH.getText()).build();
		}
		ComplexIdDetails complexIdDetails = complexIdDetailsReadRepository.findByDeviceRunId(id, domainId);
		String deviceId = complexIdDetails.getDeviceId();
		Cycle cycle = htpSimulatorService.updateCycle(runObject, id);
		if (!htpSimulatorService.isValidType()
				.test(Optional.ofNullable(String.valueOf(runObject.get(HTPAdapterConstants.TYPE.getText()))).orElse(""))) {

			logger.info(" Type is missing or incorrect.. ");
			try{
				List<String> contents = new ArrayList<>();
				contents.add(deviceId);
				contents.add(id);
				AdmNotificationService.sendNotification(NotificationGroupType.INVALID_DATA_TRANSFER_INFO_HTP, contents,token, admNotificationURL); 	 
				return Response.status(Status.BAD_REQUEST).build();
			} catch (Exception e) {
				logger.info("Exception throwed while sending Notififcation");
				return Response.status(Status.NOT_FOUND).entity("Exception throwed in Notification : Adapter").build();
			}
		}
		if (cycle != null) {
			String filePath = htpSimulatorService.getMountPath(deviceId, cycle.getPath());
			if (htpSimulatorService.checkFileSize(filePath)) {
				logger.info("Cylce object returned : " + cycle.toString());
				cycle.setPath(filePath);
				cycle.setStatus(CycleStatus.STARTED.toString());
				Timestamp currentDateTime = new Timestamp(new Date().getTime());
				cycle.setCreatedDateTime(currentDateTime);

				if (htpSimulatorService.validateChecksum(cycle.getPath(), cycle.getChecksum())
						&& !cycle.getType().equalsIgnoreCase(HTPConstants.TYPE)) {
					Company company = new Company();
					company.setId(ThreadSessionManager.currentUserSession().getAccessorCompanyId());
					cycle.setCompany(company);
					cycleWriteRepository.save(cycle);

					if (!String.valueOf(Optional.ofNullable(runObject.get(HTPAdapterConstants.CYCLE.getText())).orElse("")).matches("\\d+")) {

						logger.info(" CycleNumber is missing.. ");
						htpSimulatorService.sendNotification(NotificationGroupType.MISSING_CYCLE_NUMBER_HTP, deviceId,
								id);
						return Response.status(Status.BAD_REQUEST).build();

					}
					logger.info("Finished HtpSimulatorRestApiImpl.updateCycle method ");
					return Response.ok().status(202).entity(HTPAdapterConstants.ACCEPTED.getText()).build();

				} else if (!htpSimulatorService.validateChecksum(cycle.getPath(), cycle.getChecksum())) {

					logger.info(" Checksum mismatch.. ");
					htpSimulatorService.sendNotification(NotificationGroupType.INVALID_DATA_TRANSFER_INFO_HTP, deviceId,
							id);
					logger.error("Error occured in  HtpSimulatorRestApiImpl.updateCycle method ");
					return Response.status(409).entity(HTPAdapterConstants.CONFLICT.getText()).build();
				}
			} else {
				try {
					List<String> contents = new ArrayList<>();
					contents.add(
							htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()));
					contents.add(id);
					AdmNotificationService.sendNotification(NotificationGroupType.FILE_SIZE_EXCEEDS_LIMIT_HTP, contents,
							token, admNotificationURL);
					logger.error(
							"Sending Notification to ADM, Error occured in  HtpSimulatorRestApiImpl.updateCycle method, file size mismatch");
					return Response.status(400).entity("File size exeeds the maximum limit").build();
				} catch (Exception e) {
					logger.info("Exception occured while sending Notififcation");
					return Response.status(500).entity("Sending notification failed").build();
				}
			}

		}
		logger.error("Error occured in  HtpSimulatorRestApiImpl.updateCycle method ");
		return Response.status(404).entity("Cycle information is not available").build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.roche.connect.htp.adapter.rest.HtpSimulatorRestApi#completeCycle(java
	 * .lang.String)
	 */
	@Override
	public Response completeCycle(String runId, Map<String, Object> runObject) {

		logger.info("Enter HtpSimulatorRestApiImpl.completeCycle method with runID: " + runId);

		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		// SEQ_MISMATCH
		if (!htpSimulatorService.isInValidRunSequence(runId, HTPStatusConstants.COMPLETED.getText(), domainId)) {
			logger.info(HTPAdapterConstants.SEQ_MISMATCH.getText());
			htpSimulatorService.sendNotification(
					htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()), runId,
					NotificationGroupType.OUT_OF_SEQ_MSG_HTP);
			return Response.status(400).entity(HTPAdapterConstants.SEQ_MISMATCH.getText()).build();
		}
		List<Cycle> cycleList = null;
		ComplexIdDetails complexIdDetails = complexIdDetailsReadRepository.findByDeviceRunId(runId, domainId);

		String deviceId = complexIdDetails.getDeviceId();

		if (!String.valueOf(Optional.ofNullable(runObject.get(HTPAdapterConstants.CYCLE.getText())).orElse("")).matches("\\d+")) {

			logger.info(" CycleNumber is missing.. ");
			htpSimulatorService.sendNotification(NotificationGroupType.MISSING_CYCLE_NUMBER_HTP, deviceId, runId);
			return Response.status(Status.BAD_REQUEST).build();
		}

		cycleList = cycleReadRepository.findByRunIdAndCyclesNumber(runId,
				Integer.parseInt(runObject.get(HTPAdapterConstants.CYCLE.getText()).toString()), domainId);

		if (cycleList != null) {

			for (Cycle cycle : cycleList) {

				cycle.setStatus(CycleStatus.COMPLETE.toString());
				Company company = new Company();
				company.setId(ThreadSessionManager.currentUserSession().getAccessorCompanyId());
				cycle.setCompany(company);
				cycleWriteRepository.save(cycle);
			}
			logger.info(" Validating previous Cycles for runId " + runId);
			htpSimulatorService.validatePreviousCycle(runId, Integer.parseInt(runObject.get(HTPAdapterConstants.CYCLE.getText()).toString()),
					domainId, deviceId);
			logger.info("Finished HtpSimulatorRestApiImpl.completeCycle method ");
			return Response.ok().entity("OK").build();

		} else {
			logger.error("Error occured in  HtpSimulatorRestApiImpl.completeCycle method ");
			return Response.status(500)
					.entity("Error occurred in while processing the request. Please contact the administrator").build();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.roche.connect.htp.adapter.rest.HtpSimulatorRestApi#freespace()
	 */
	@Override
	public Response freespace() throws JSONException, IOException {

		logger.info("Enter HtpSimulatorRestApiImpl.freespace ");

		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		JsonNode json = htpSimulatorService.checkFreeSpace();

		if (!json.isNull()) {
			logger.info("Finished HtpSimulatorRestApiImpl.freespace method ");
			return Response.ok(json).build();
		} else {
			logger.error("Error occured in  HtpSimulatorRestApiImpl.freespace method ");
			return Response.status(409).build();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.roche.connect.htp.adapter.rest.HtpSimulatorRestApi#notification(java.
	 * lang.String)
	 */
	@Override
	public Response notification(Notification notificationObj) throws JSONException, IOException, HMTPException {

		logger.info("Enter HtpSimulatorRestApiImpl.notification ");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(notificationObj);
		String userName = ThreadSessionManager.currentUserSession().getPasJwtToken().getUserName();
		String domainName = ThreadSessionManager.currentUserSession().getPasJwtToken().getDomainName();
		String url = deviceEndPoint + "/json/users/userName/" + userName + "/domainName/" + domainName + "/id";
		String token1 = webServices.getToken();
		Builder builder = RestClientUtil.getBuilder(url, null);
		builder.header(HTPAdapterConstants.COOKIE.getText(), HTPAdapterConstants.BROWN_STONE_AUTH_COOKIE.getText() + token1);
		String userId = builder.get(String.class);
		url = deviceEndPoint + "/json/device/fetch/expr?filterExpression=user.id=" + userId;
		builder = RestClientUtil.getBuilder(url, null);
		String response = builder.get(String.class);
		JSONArray deviceArray = new JSONArray(response);
		List<String> content = new ArrayList<>();
		if (deviceArray.length() == 0) {
			content.add(userName);
			AdmNotificationService.sendNotification(NotificationGroupType.UNREGISTERED_DEVICE_HTP, content,token1,
					admNotificationURL);
			throw new HMTPException("Message from unregistered device " + userName);

		} else {
			content.add(deviceArray.getJSONObject(0).getJSONObject("deviceType").getString("name"));
			content.add(deviceArray.getJSONObject(0).getString("name"));
			content.add(deviceArray.getJSONObject(0).getString("serialNo"));
			content.add(notificationObj.getCode());
			content.add(notificationObj.getDescription());
			String severity = notificationObj.getSeverity();
			
			AdmNotificationService.sendNotification(NotificationGroupType.INSTRU_TRIGGER_HTP, content,severity, token1,
                    admNotificationURL);
			logger.info("Notification \t" + json);
		}
		return Response.ok(Status.OK).build();
	}

	@Override
	public Response getOrderDetails(String complexId) throws HMTPException, ParseException {
		logger.info("Getting Order Details by complexid");

		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		ComplexIdDetails complexIdDetails = new ComplexIdDetails();
		complexIdDetails.setComplexId(complexId);

		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		ComplexIdDetails existingComplexIdDetails = complexIdDetailsReadRepository.findByComplexId(complexId, domainId);

		if (existingComplexIdDetails == null) {
			Company company = new Company();
			company.setId(ThreadSessionManager.currentUserSession().getAccessorCompanyId());
			complexIdDetails.setCompany(company);
			complexIdDetailsWriteRepository.save(complexIdDetails);
		} else {
			complexIdDetails = existingComplexIdDetails;
		}
		Response response = htpSimulatorService.getComplexIdDetails(complexId);
		if (response.getStatus() == 200) {
			complexIdDetailsDTO = response.readEntity(ComplexIdDetailsDTO.class);
			Map<String, Object> orderDetailsgetOrderDetails = htpSimulatorService
					.getOrderDetails(complexIdDetails.getId(), complexIdDetailsDTO);
			return Response.ok().entity(orderDetailsgetOrderDetails).build();
		} else if (response.getStatus() == 404) {
			List<String> contents = new ArrayList<>();
			contents.add(complexId);
			contents.add(htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()));
			AdmNotificationService.sendNotification(NotificationGroupType.INVALID_COMPLX_ID_HTP, contents, token,
					admNotificationURL);
			logger.error("Sending Notification to ADM, No order found for complex id");
			return Response.status(400).entity("No order found for complex id.").build();
		} else {
			return Response.status(500)
					.entity("Error occurred while processing the request. Please contact the administrator.").build();
		}

	}

	@Override
	public Response updateRunStatus(String id, Map<String, Object> requestbody)
			throws HMTPException, JsonProcessingException, ParseException {
		logger.info("Enter HtpSimulatorRestApiImpl.updateRunStaus method ");

		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		String runStatus = requestbody.get(HTPAdapterConstants.RUN_STATUS.getText()).toString();
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		if (requestbody.keySet().stream()
		    .noneMatch(x -> requestbody.get(x) == null
		    || String.valueOf(requestbody.get(x)).trim().equalsIgnoreCase(""))
		    && (runStatus.equalsIgnoreCase(HTPStatusConstants.STARTED.getText())
		        || runStatus.equalsIgnoreCase(HTPStatusConstants.IN_PROCESS.getText())
		        || runStatus.equalsIgnoreCase(HTPStatusConstants.FAILED.getText())
		        || runStatus.equalsIgnoreCase(HTPStatusConstants.COMPLETED.getText()))) {
		// Run Sequence checking
		if (!htpSimulatorService.isInValidRunSequence(id, runStatus, domainId)) {
			logger.info(HTPAdapterConstants.SEQ_MISMATCH.getText());
			htpSimulatorService.sendNotification(
					htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()), id,
					NotificationGroupType.OUT_OF_SEQ_MSG_HTP);
			return Response.status(400).entity(HTPAdapterConstants.SEQ_MISMATCH.getText()).build();
		}

			ComplexIdDetails complexIdDetails = complexIdDetailsReadRepository.findByDeviceRunId(id, domainId);
			String complexId = complexIdDetails.getComplexId();
			requestbody.put(HTPAdapterConstants.COMPLEX_ID.getText(), complexId);
			RunResultsDTO runResultsDTO = htpSimulatorService.convertRunStatusToRun(requestbody);
			runResultsDTO.setDeviceId(complexIdDetails.getDeviceId());
			htpSimulatorService.updateRunStatus(id, runResultsDTO);
			return Response.status(202).entity(HTPAdapterConstants.ACCEPTED.getText()).build();
		} else {
			List<String> contents = new ArrayList<>();
			contents.add(runStatus);
			contents.add(htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()));
			AdmNotificationService.sendNotification(NotificationGroupType.INVALID_RUN_STATUS_RECEIVED_FROM_HTP,
					contents, token, admNotificationURL);
			return Response.status(400).build();
		}
	}

	@Override
	public Response updateLogs(String id, Map<String, Object> requestbody) throws IOException {

		logger.info("Enter HtpSimulatorRestApiImpl.updateLogs method ");

		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		if (requestbody != null) {

			if (htpSimulatorService.validateChecksum(requestbody.get(HTPAdapterConstants.PATH.getText()).toString(),
					requestbody.get(HTPAdapterConstants.CHECKSUM.getText()).toString())) {

				logger.info("HtpSimulatorRestApiImpl.updateLogs method is completed successfully ");

				return Response.ok(202).entity(HTPAdapterConstants.ACCEPTED.getText()).build();
			} else {

				logger.error("Error occured in  HtpSimulatorRestApiImpl.updateLogs method ");

				return Response.status(409).entity(HTPAdapterConstants.CONFLICT.getText()).build();
			}
		} else {

			logger.error("Error occured in  HtpSimulatorRestApiImpl.updateLogs method ");

			return Response.status(409).entity(HTPAdapterConstants.CONFLICT.getText()).build();
		}

	}

	@Override
	public Response completeFileTransfer(String runId) {
		if (!webServices.getDetailsFromOauthToken(this)) {
			return Response.status(401).entity(HTPAdapterConstants.DEVICE.getText()).build();
		}
		if (runId != null) {
			// Run Sequence checking
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			if (!htpSimulatorService.isInValidRunSequence(runId, HTPStatusConstants.TRANSFERCOMPLETE.getText(),
					domainId)) {
				logger.info(HTPAdapterConstants.SEQ_MISMATCH.getText());
				htpSimulatorService.sendNotification(
						htpSimulatorService.getDeviceId(ThreadSessionManager.currentUserSession().getUserId()), runId,
						NotificationGroupType.OUT_OF_SEQ_MSG_HTP);
				return Response.status(400).entity(HTPAdapterConstants.SEQ_MISMATCH.getText()).build();
			}
			Map<String, Object> requestbody = new HashMap<>();
			requestbody.put("run_status", HTPStatusConstants.TRANSFERCOMPLETE.getText());
			ComplexIdDetails complexIdDetails = complexIdDetailsReadRepository.findByDeviceRunId(runId, domainId);
			String complexId = complexIdDetails.getComplexId();
			requestbody.put(HTPAdapterConstants.COMPLEX_ID.getText(), complexId);
			String folderPath = "";
			Cycle cycle = cycleReadRepository.findTopByRunId(runId, domainId);
			if (cycle != null) {
				String temp = cycle.getPath().substring(0, cycle.getPath().lastIndexOf('/'));
				folderPath = temp.substring(0, temp.lastIndexOf('/'));
			}
			requestbody.put(HTPConstants.OUTPUTFOLDER, folderPath);
			RunResultsDTO runResultsDTO = htpSimulatorService.convertRunStatusToRun(requestbody);
			runResultsDTO.setDeviceId(complexIdDetails.getDeviceId());
			htpSimulatorService.updateRunStatus(runId, runResultsDTO);
			List<Cycle> cycles = cycleReadRepository.findByRunId(runId, domainId);
			for (Cycle cycle1 : cycles) {
				cycle1.setTransferComplete(CycleStatus.COMPLETE.toString());
				Company company = new Company();
				company.setId(ThreadSessionManager.currentUserSession().getAccessorCompanyId());
				cycle1.setCompany(company);
				cycleWriteRepository.save(cycle1);
			}
			RunServiceImpl.getAddcycle().remove();// -->evict data from
													// ThreadLocal
			return Response.ok().entity("OK").build();
		} else {
			return Response.status(500).build();
		}
	}
}
