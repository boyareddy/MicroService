/*******************************************************************************
*
* AdminMiscRestApiImpl.java                  
* Version:  1.0
*
* Authors:  somesh_r
*
*********************
*
* Copyright (c) 2019  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
* All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
* herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
* from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
* executed Confidentiality and Non-disclosure agreements explicitly covering such access
*
* The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
* information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
* OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
* LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
* TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.      
*          
**********************
* ChangeLog:
*
*   somesh_r@hcl.com Feb 17, 2019: Updated copyright headers
*
**********************
*
*
**********************
*
* Description: write me
*
**********************
* 
* 
******************************************************************************/

package com.roche.connect.adm.rest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.CharEncoding;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.dto.UserDetailsDTO;
import com.roche.connect.adm.error.CustomErrorCodes;
import com.roche.connect.adm.model.BackupHistory;
import com.roche.connect.adm.model.SystemSettings;
import com.roche.connect.adm.repository.BackupHistoryReadRepository;
import com.roche.connect.adm.repository.SystemSettingsReadRepository;
import com.roche.connect.adm.service.BackupAndRestoreService;
import com.roche.connect.adm.util.ADMConstants;
import com.roche.connect.adm.util.BackupConstants;

@Component
public class AdminMiscRestApiImpl implements AdminMiscRestAPI {

	@Value("${pas.admin_api_url}")
	private String adminAPI;

	@Value("${backup.requiredSpace}")
	private int requiredSpace;

	@Autowired
	BackupAndRestoreService backupAndRestoreService;
	@Autowired
	BackupHistoryReadRepository backupHistoryReadRepository;

	@Autowired
	SystemSettingsReadRepository systemSettingsReadRepository;

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	public static final String ADDUSERURL = "/json/users?ownerId=1&roles=Device";

	@Override
	public Response addDeviceUser(UserDetailsDTO deviceUser) throws HMTPException {
		Response response = null;
		try {
			logger.info("-> addDeviceUser()");
			if (deviceUser == null) {
				throw new HMTPException("-> addDeviceUser():: invalid user entity");
			}
			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
			Builder builder = RestClientUtil.getBuilder(URLEncoder.encode(adminAPI + ADDUSERURL, CharEncoding.UTF_8),
					null);
			RestClientUtil.setPasHeaders(builder);
			logger.info("-> addDeviceUser()::Creating user");
			response = builder.post(Entity.entity(deviceUser, MediaType.APPLICATION_JSON));
			if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
				logger.info("-> addDeviceUser():: User created");
				return Response.ok("success").status(200).build();
			} else {
				return response;
			}
		} catch (HMTPException | UnsupportedEncodingException exp) {
			logger.error("-> addDeviceUser()::Error occured," + exp.getMessage());
			throw new HMTPException(CustomErrorCodes.NOTIFICATION_UPDATE_FAILED, "Device user creation failed");
		}
	}

	@Override
	public Response validateOutputFilePath(String outputPath,String deviceType) throws HMTPException {
		logger.info("validateOutputFilePath : outputPath:" + outputPath);
		if (StringUtils.isNotBlank(outputPath)) {
			Path path = Paths.get(outputPath.trim());
			if (path.toFile().exists()) {

				if (Files.isReadable(path)) {
					logger.info(
							"-> validateOutputFilePath():: Read permission available for the configured output directory");
					return Response.status(Status.OK).entity("Path available").build();
				} else {
					logger.error(
							"-> validateOutputFilePath():: Read permission denied for the configured output directory");
					return Response.status(Status.CONFLICT)
							.entity("Read permission denied for the configured output directory").build();
				}

			} else {
				return Response.status(Status.SERVICE_UNAVAILABLE)
						.entity("The configured "+ deviceType +" output directory is not available in the server")
						.build();
			}
		} else {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity("Output path/Device type should not be blank")
					.build();
		}

	}

	@Override
	public Response doManualBackup(Map<String, String> request) throws HMTPException {
		logger.info("Entered AdminMiscRestApiImpl.doManualBackup");
		double expectedSpace = 0;
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		String username = ThreadSessionManager.currentUserSession().getAccessorUserName();
		logger.info("domainId :" + domainId + "\n username:" + username);
		List<BackupHistory> backupHistory = backupHistoryReadRepository.findByStatus(BackupConstants.INPROGRESS);
		SystemSettings backupLocationSettings = systemSettingsReadRepository
				.findByAttributeName(ADMConstants.BACKUPLOCATION.toString());
		JSONObject responseEntity = new JSONObject();
		responseEntity.put(ADMConstants.DATEANDTIME.toString(), Timestamp.from(Instant.now()));
		if (backupLocationSettings != null) {
			if (!backupHistory.isEmpty()) {
				responseEntity.put(ADMConstants.RSPMESSAGE.toString(), ADMConstants.BACKUP_IN_PROGRESS.toString());
				return Response.status(HttpStatus.CONFLICT_409).entity(responseEntity.toString()).build();
			}

			File destinationFolder = new File(backupLocationSettings.getAttributeValue());
			if (!destinationFolder.exists()) {
				responseEntity.put(ADMConstants.RSPMESSAGE.toString(),
						ADMConstants.DESTINATION_DIRECTORY_IS_NOT_AVILABLE.toString());
				return Response.status(HttpStatus.BAD_REQUEST_400).entity(responseEntity.toString()).build();
			}
			expectedSpace = Math.pow(ADMConstants.BASIC_SIZE_MEASURE.toInteger(), 3) * requiredSpace;
			if (destinationFolder.getUsableSpace() < expectedSpace) {
				responseEntity.put(ADMConstants.RSPMESSAGE.toString(), ADMConstants.NOT_ENOUGH_STORAGE.toString());
				return Response.status(HttpStatus.PRECONDITION_FAILED_412).entity(responseEntity.toString()).build();
			}
			if (!Files.isWritable(Paths.get(destinationFolder.getAbsolutePath()))) {
				responseEntity.put(ADMConstants.RSPMESSAGE.toString(), ADMConstants.WRITE_ACCESS_RESTRICTED.toString());
				return Response.status(HttpStatus.FORBIDDEN_403).entity(responseEntity.toString()).build();
			}
			backupAndRestoreService.takeBackup(domainId, username, backupLocationSettings.getAttributeValue());
			responseEntity.put(ADMConstants.RSPMESSAGE.toString(), ADMConstants.BACKUP_STARTED.toString());
			return Response.ok().entity(responseEntity.toString()).build();
		} else {
			throw new HMTPException(CustomErrorCodes.SYSTEM_SETTINGS_NOT_AVAILABLE,
					ADMConstants.SYS_SETTINGS_NOTAVAILABLE.toString());
		}
	}

	@Override
	public Response doScheduledBackup(Map<String, String> request) throws HMTPException {
		backupAndRestoreService.saveSchedule(request);
		return Response.ok().build();
	}

	@Override
	public Response getBackup() throws HMTPException {
		logger.info("Entered getBackup() and creating response");
		Map<String, String> responseMap = new HashMap<>();
		SystemSettings backupIntervalSettings = systemSettingsReadRepository
				.findByAttributeName(ADMConstants.BACKUPINTERVAL.toString());
		SystemSettings backupLocationSettings = systemSettingsReadRepository
				.findByAttributeName(ADMConstants.BACKUPLOCATION.toString());
		if (backupIntervalSettings != null && backupLocationSettings != null) {
			BackupHistory backupHistory = backupHistoryReadRepository.findFirstByOrderBycreatedDateTimeDesc();
			if (backupHistory != null) {
				logger.info("backupHistory null check passed");
				if (backupHistory.getUpdatedDateTime() != null)
					responseMap.put(BackupConstants.LASTBACKUPDATE, backupHistory.getUpdatedDateTime().toString());
				else
					responseMap.put(BackupConstants.LASTBACKUPDATE, "");
				responseMap.put(BackupConstants.STATUS, backupHistory.getStatus());
			}
			responseMap.put(BackupConstants.NEXTBACKUPDATE,
					(backupAndRestoreService.getNextSchedule(backupIntervalSettings.getAttributeValue()).toString()));
			responseMap.put(ADMConstants.BACKUPINTERVAL.toString(), backupIntervalSettings.getAttributeValue());
			responseMap.put(ADMConstants.BACKUPLOCATION.toString(), backupLocationSettings.getAttributeValue());
			return Response.ok().entity(responseMap).build();
		} else {
			throw new HMTPException(CustomErrorCodes.SYSTEM_SETTINGS_NOT_AVAILABLE,
					ADMConstants.SYSTEM_SETTINGS_NOT_AVAILABLE_FOR_BACKUP.toString());
		}
	}
}