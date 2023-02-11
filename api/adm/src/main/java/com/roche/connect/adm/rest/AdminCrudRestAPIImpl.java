/*******************************************************************************
 * AdminCrudRestAPIImpl.java                  
 *  Version:  1.0
 * 
 * Authors: dineshj
 * 
 * ==================================
 * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
 *  ==================================
 * ChangeLog:
 ******************************************************************************/

package com.roche.connect.adm.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.emailservice.client.beans.EmailMessageBean;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.adm.dto.SystemSettingsDto;
import com.roche.connect.adm.error.CustomErrorCodes;
import com.roche.connect.adm.model.Message;
import com.roche.connect.adm.model.MessageTemplate;
import com.roche.connect.adm.model.ProblemReport;
import com.roche.connect.adm.model.Recipient;
import com.roche.connect.adm.model.SystemSettings;
import com.roche.connect.adm.repository.MessageReadRepository;
import com.roche.connect.adm.repository.MessageRecipientReadRepository;
import com.roche.connect.adm.repository.MessageTemplateReadRepository;
import com.roche.connect.adm.repository.SystemSettingsReadRepository;
import com.roche.connect.adm.service.IMessageService;
import com.roche.connect.adm.service.IVersionDetailsService;
import com.roche.connect.adm.service.ProblemReportService;
import com.roche.connect.adm.service.ProblemReportServiceImp;
import com.roche.connect.adm.util.ADMConstants;
import com.roche.connect.adm.util.UtilityService;
import com.roche.connect.adm.writerepository.MessageWriteRepository;
import com.roche.connect.adm.writerepository.RecipientWriteRepository;
import com.roche.connect.adm.writerepository.ReportWriteRepository;
import com.roche.connect.adm.writerepository.SystemSettingsWriteRepository;
import com.roche.connect.common.constant.NotificationGroupType;

@Component
public class AdminCrudRestAPIImpl implements AdminCrudRestAPI {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private MessageWriteRepository messageWriteRepository;

	@Autowired
	private MessageTemplateReadRepository messageTemplateReadRepository;

	@Autowired
	private MessageReadRepository messageReadRepository;

	@Autowired
	private ReportWriteRepository reportWriteRepository;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private IVersionDetailsService versionDetails;


	@Value("${pas.email_remote_url}")
	private String emailAPI;

	@Value("${manifest_file_path}")
	private String manifestFilePath;

	@Value("${pas.audittrail_remote_url}")
	private String auditURL;

	@Value("${auditLog.requiredColumns}")
	private String columnsToMap;

	@Value("${auditLog.folderPath}")
	private String folderPath;

	@Value("${logging.path}")
	private String loggerPath;

	@Value("${problemReport.problemReportPath}")
	private String problemReportPath;

	@Value("${problemReport.monthLimit}")
	private int monthLimit;

	@Value("${problemReport.dateRange}")
	private int dateRange;

	@Autowired
	private IMessageService iMessageService;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private RecipientWriteRepository recipientWriteRepository;

	@Autowired
	private MessageRecipientReadRepository messageRecipientReadRepository;

	@Autowired
	private SystemSettingsReadRepository systemSettingsReadRepository;

	@Autowired
	private SystemSettingsWriteRepository systemSettingsWriteRepository;

	@Autowired
	ProblemReportService problemReportService;

	@Autowired
	ProblemReportServiceImp problemReportServiceImp;


	private static final String BUILDNUMBER = "Build_Number";

	@Override
	public Response addNotification(MessageDto messageDto) throws HMTPException {
		try {
			logger.info("Inside addNotification method -> " + messageDto.toString());
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("Domain id :" + domainId + " username :"
					+ ThreadSessionManager.currentUserSession().getAccessorUserName());
			List<MessageTemplate> messageTemplates = messageTemplateReadRepository
					.findMessageTemplate(messageDto.getMessageGroup(), messageDto.getLocale(), domainId);
			if (messageTemplates != null && !messageTemplates.isEmpty()) {
				processNotification(messageTemplates, messageDto);

			} else {
				throw new HMTPException();
			}
			return Response.ok("Success").status(200).build();
		} catch (Exception e) {
			logger.error(ADMConstants.NOTIFICATION_ADD_FAILED.toString(), e);
			throw new HMTPException(CustomErrorCodes.NOTIFICATION_ADD_FAILED,
					ADMConstants.NOTIFICATION_ADD_FAILED.toString());
		}
	}

	private void processNotification(List<MessageTemplate> messageTemplates, MessageDto messageDto)
			throws HMTPException {
		logger.info("Inside processNotification method UI process -> " + messageDto.toString());
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		for (MessageTemplate messageTemplate : messageTemplates) {
			processUIChannel(messageTemplate, messageDto);
		}
		Set<String> channels = messageTemplates.stream().map(m -> m.getChannel()).collect(Collectors.toSet());
		if (channels.contains("EMAIL")) {
			SystemSettings systemSettings = systemSettingsReadRepository
					.findByAttributeName(ADMConstants.SYSTEMLANGUAGE.toString());
			if (systemSettings != null) {
				messageDto.setLocale(systemSettings.getAttributeValue());
				List<MessageTemplate> emailMessageTemplates = messageTemplateReadRepository
						.findMessageTemplate(messageDto.getMessageGroup(), messageDto.getLocale(), domainId);
				if (emailMessageTemplates != null && !emailMessageTemplates.isEmpty()) {
					logger.info("Inside processNotification method Email process -> " + messageDto.toString());
					for (MessageTemplate emailMessageTemplate : emailMessageTemplates) {
						processEmailChannel(emailMessageTemplate, messageDto);
					}
				} else {
					throw new HMTPException(CustomErrorCodes.NOTIFICATION_ADD_FAILED,
							ADMConstants.EMAIL_TEMPLATE_NOTAVAILABLE.toString() + messageDto.getLocale());
				}
			} else {
				throw new HMTPException(CustomErrorCodes.NOTIFICATION_ADD_FAILED,
						ADMConstants.SYS_SETTINGS_NOTAVAILABLE.toString());
			}
		}
	}
	
	private void processEmailChannel(MessageTemplate messageTemplate, MessageDto messageDto) throws HMTPException {
		if (messageTemplate.getChannel().equals("EMAIL")) {
			EmailMessageBean emailMessageBean = iMessageService.constructEmail(messageDto, messageTemplate);
			Builder builder = null;
					builder = RestClientUtil.getBuilder(this.emailAPI + "/json/emails", null);
					RestClientUtil.setPasHeaders(builder);
					Response response = builder.post(Entity.entity(emailMessageBean, MediaType.APPLICATION_JSON));
					logger.info("Response code from eMail : " + response.getStatus());
		}
	}

	private void processUIChannel(MessageTemplate messageTemplate, MessageDto messageDto) throws HMTPException {

		messageTemplate.getMessageRecepients().get(0).getRoleId();
		if (messageTemplate.getChannel().equals("UI")) {
			List<Message> messages = iMessageService.constructMessage(messageDto, messageTemplate);
			List<Message> messageTemp = messageWriteRepository.save(messages);

			Set<Integer> rolesIds = messages.get(0).getMessageTemplate().getMessageRecepients().stream()
					.map(e -> e.getRoleId()).collect(Collectors.toSet());

			if (!messages.isEmpty()) {
				for (Message message : messages) {
					for (Recipient recipient : message.getRecipient()) {
						recipient.setMessage(messageTemp.get(0));
					}
					recipientWriteRepository.save(message.getRecipient());
					iMessageService.sendNotificationToaster(messageTemp.get(0), rolesIds);
				}
			}
		}
	}

	@Override
	public Response getNotificationTemplates() throws HMTPException {
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			return Response
					.ok(utilityService.mapTemplateToDto(messageTemplateReadRepository.findMessageTemplates(domainId)))
					.status(200).build();

		} catch (Exception e) {
			logger.error(ADMConstants.NOTIFICATION_TEMPLATE_NOT_FOUND.toString());
			throw new HMTPException(CustomErrorCodes.NOTIFICATION_TEMPLATE_NOT_FOUND,
					ADMConstants.NOTIFICATION_TEMPLATE_NOT_FOUND.toString());
		}
	}

	@Override
	public Response updateNotification(List<com.roche.connect.adm.dto.Message> messages) throws HMTPException {

		try {
			logger.info("Inside updateNotification method -> " + messages.toString());
			Message messageTemp = null;
			List<Message> messagesList = null;
			if (!messages.isEmpty()) {
				messagesList = new ArrayList<>();
				for (com.roche.connect.adm.dto.Message message : messages) {
					messageTemp = messageReadRepository.findOne(message.getId());
					if (messageTemp == null) {
						logger.error(ADMConstants.NOTIFICATION_NOT_AVAILABLE.toString());
						throw new HMTPException(CustomErrorCodes.NOTIFICATION_NOT_AVAILABLE,
								ADMConstants.NOTIFICATION_NOT_AVAILABLE.toString());
					}
					messageTemp.setId(messageTemp.getId());
					messageTemp.setViewedDateTime(Timestamp.from(Instant.now()));
					messageTemp.setViewed("Y");
					messagesList.add(messageTemp);
				}
				messageWriteRepository.save(messagesList);
			}
			return Response.ok("Success").status(200).build();
		} catch (Exception e) {
			logger.error(ADMConstants.NOTIFICATION_UPDATE_FAILED.toString(), e);
			throw new HMTPException(CustomErrorCodes.NOTIFICATION_UPDATE_FAILED,
					ADMConstants.NOTIFICATION_UPDATE_FAILED.toString());
		}
	}

	@Override
	public Response getNotifications() throws HMTPException {
		try {
			logger.info("Inside getNotifications method");
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			List<com.roche.connect.adm.dto.Message> messages = utilityService.mapMessageToDto(messageReadRepository
					.findMessages(ThreadSessionManager.currentUserSession().getAccessorUserName(), domainId));
			return Response.ok(messages).status(200).build();
		} catch (Exception e) {
			logger.error(ADMConstants.NOTIFICATION_FETCH_FAILED.toString(), e);
			throw new HMTPException(CustomErrorCodes.NOTIFICATION_FETCH_FAILED,
					ADMConstants.NOTIFICATION_FETCH_FAILED.toString());
		}
	}

	@Override
	public Response getVersionInfo() throws HMTPException {
		logger.info(" -> getVersionInfo()");
		JSONObject versionInfo = new JSONObject();
		JSONObject connectVersionInfo = new JSONObject();
		JSONObject careVersionInfo = new JSONObject();
		JSONObject externalVersionInfo = new JSONObject();
		String care = "care_";
		String connect = "connect_";
		String external = "external_";
		String buildNumber = BUILDNUMBER;
		String line = null;
		try {
			logger.info(
					"Read version info file " + AdminCrudRestAPIImpl.class.getClassLoader().getResource("").toString());
			InputStream inputStream = AdminCrudRestAPIImpl.class.getClassLoader().getResourceAsStream(manifestFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = br.readLine()) != null) {
				if (line.contains("_version") || line.contains(BUILDNUMBER)) {
					String[] versionJson = line.split("=");
					if (versionJson != null) {
						if (versionJson[0].contains(buildNumber)) {
							versionInfo.put(BUILDNUMBER, versionJson[1]);
						} else if (versionJson[0].contains(connect)) {
							connectVersionInfo.put(
									versionJson[0].substring(versionJson[0].indexOf(connect) + connect.length()),
									versionJson[1]);
						} else if (versionJson[0].contains(care)) {
							careVersionInfo.put(versionJson[0].substring(versionJson[0].indexOf(care) + care.length()),
									versionJson[1]);
						} else if (versionJson[0].contains(external)) {
							externalVersionInfo.put(
									versionJson[0].substring(versionJson[0].indexOf(external) + external.length()),
									versionJson[1]);
						}
					} else {
						throw new HMTPException(CustomErrorCodes.VERSION_FILE_NOT_AVAILABLE,
								ADMConstants.VERSION_FETCH_FAILED.toString());
					}
				}
			}
			connectVersionInfo.toString();
			versionInfo.put("Contact", "Roche GCS");
			versionInfo.put("Connect", connectVersionInfo);
			versionInfo.put("CARE", careVersionInfo);
			versionInfo.put("External", externalVersionInfo);

			JSONObject assayInfo = versionDetails.getAssaysWithVersion();
			if (assayInfo != null) {
				versionInfo.put("SupportedAssays", assayInfo);
			}

			JSONObject supportedDevices = versionDetails.getSupportedDeviceWithVersion();
			if (supportedDevices != null) {
				versionInfo.put("SupportedDevices", supportedDevices);
			}

			logger.info("<- getVersionInfo()");
		} catch (IOException e) {
			logger.error("Error occured while fetching version details" + e.getMessage());
			throw new HMTPException(CustomErrorCodes.VERSION_FILE_NOT_AVAILABLE,
					ADMConstants.VERSION_FETCH_FAILED.toString());
		}
		return Response.ok(versionInfo.toString()).status(200).build();
	}

	@Override
	public Response getAuditDetails(String fromDate, String toDate, String companydomainname) {
		File file = null;
		try {
			file = problemReportService.getAuditDetailsFile(fromDate, toDate, companydomainname, folderPath);
			SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_HHmmss.sss");
			String strDate = formatter.format(new Date());
			if(file.length()>0) 
				return Response.ok((Object) file).header("Content-Disposition", "attachment; filename=AuditDetails_" + strDate + ".zip").build();
			
		} catch (Exception e) {
			logger.error("Error occured while getting audit log details" + e.getMessage());
		}
		return Response.status(500).build();
	}

	@Override
	public Response removeAuditDetails(String fileName) {
		String filepath = folderPath + fileName.substring(fileName.indexOf('_') + 1);
		File file = new File(filepath);
		if (file.exists()) {
			try {
				Files.delete(file.toPath());
				logger.info("Successfully delete audit details file " + fileName);
			} catch (IOException e) {
				logger.info("Error while delete audit details file " + fileName);
			}
		} else {
			logger.info("File does not exists " + fileName);
		}
		return Response.ok(200).build();
	}

	@Override
	public Response getModules() throws HMTPException {
		try {
			logger.info("Inside getModules method");
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			List<String> messages = messageTemplateReadRepository.findModules(domainId);
			messages.add(0, "All topics");
			return Response.ok(messages).status(200).build();
		} catch (Exception e) {
			logger.error(ADMConstants.NOTIFICATION_FETCH_FAILED.toString(), e);
			throw new HMTPException(CustomErrorCodes.NOTIFICATION_FETCH_FAILED,
					ADMConstants.NOTIFICATION_FETCH_FAILED.toString());
		}
	}

	// Creating Report for given two date periods - RC#12381
	@Override
	public Response createReport(String fromDate, String toDate) throws HMTPException {
		logger.info("Entered into CreateReport EndPoint");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -monthLimit);
		Date result = cal.getTime();
		int dateDuration;
		MessageDto messagedto = new MessageDto();
		List<String> content = new ArrayList<>();
		content.add(fromDate);
		content.add(toDate);
		messagedto.setContents(content);
		messagedto.setLocale(NotificationGroupType.LOCALE.getGroupType());
		String desc = "Report Created Successfully";
		int statuscode = HttpStatus.CREATED_201;
		try {
			if (result.compareTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromDate)) > 0) {
				return Response.ok("The selected date range is beyond the threshold").status(HttpStatus.BAD_REQUEST_400)
						.build();
			}
		} catch (Exception e) {
			logger.error("Exception occured while parsing String to Date");
			return Response.ok("Exception occured while parsing String to Date")
					.status(HttpStatus.INTERNAL_SERVER_ERROR_500).build();
		}
		try {
			// Date duration calculation
			dateDuration = utilityService.dateDurationCalculator(fromDate, toDate);
		} catch (ParseException e) {
			logger.error("Exception raised while parsing String to Date");
			return Response.ok("Error getting while parsing String to Date")
					.status(HttpStatus.INTERNAL_SERVER_ERROR_500).build();
		}
		try {
			if (dateDuration > dateRange)
				return Response.ok("Date duration is more than three weeks").status(HttpStatus.BAD_REQUEST_400).build();
			else {
				problemReportService.generateProblemReport(fromDate, toDate);
				messagedto.setMessageGroup(NotificationGroupType.PRBLM_RPRT_GEN.getGroupType());
			}

			ProblemReport problemReport = problemReportServiceImp.getProblemReport(fromDate, toDate, problemReportPath);
			reportWriteRepository.save(problemReport);
			logger.info("Report created Successfully");
		} catch (Exception e) {
				logger.error("Error while creating problem repoprt::" + e.getMessage());
				messagedto.setMessageGroup(NotificationGroupType.PRBLM_RPRT_NOT_GEN.getGroupType());
				desc = "Error occurred. Please try generating the report again.";
				statuscode = HttpStatus.ACCEPTED_202;	
		}
		addNotification(messagedto);	
		return Response.ok(desc).status(statuscode).build();
	}

	@Override
	public Response saveSystemSettings(String data) throws HMTPException {
		long domainId;
		try {
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			List<SystemSettings> systemSettings = new ArrayList<>();
			boolean validPhone = false;
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<SystemSettingsDto>> ref = new TypeReference<List<SystemSettingsDto>>() {
			};
			List<SystemSettingsDto> systemSettingsDTO = mapper.readValue(data, ref);

			systemSettingsWriteRepository.deleteByDomainId(domainId);

			List<SystemSettingsDto> sysSettingsForValidation = systemSettingsDTO.stream()
					.filter(p -> (ADMConstants.SystemSettings.PHONE_NUM_ATTR_NAME.toString())
							.equalsIgnoreCase(p.getAttributeName()))
					.collect(Collectors.toList());

			if (!sysSettingsForValidation.isEmpty()) {
				String phone = sysSettingsForValidation.get(0).getAttributeValue();
				if (phone.length() > 20) {
					throw new HMTPException(ADMConstants.ErrorMessages.INVALID_PHONE_NUM_LENGTH.toString());
				}
				validPhone = UtilityService.validatePhoneNumber(phone);
				if (!validPhone) {
					throw new HMTPException(ADMConstants.ErrorMessages.INVALID_PHONE_NUM_FORMAT.toString());
				}
			}

			if (!UtilityService.validateLabDetails(systemSettingsDTO)) {
				throw new HMTPException(ADMConstants.ErrorMessages.ADDRESS_LENGTH_INVALID.toString());
			}

			for (SystemSettingsDto settingDTO : systemSettingsDTO) {
				SystemSettings systemSetting = new SystemSettings();
				systemSetting.setActiveFlag(ADMConstants.SystemSettings.ACTIVE_FLAG.toString());
				systemSetting.setAttributeName(settingDTO.getAttributeName());
				systemSetting.setAttributeValue(settingDTO.getAttributeValue());
				systemSetting.setAttributeType(settingDTO.getAttributeType());
				systemSetting.setCreatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
				systemSetting.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
				systemSetting.setUpdatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
				systemSetting.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
				systemSettings.add(systemSetting);
			}
			systemSettingsWriteRepository.save(systemSettings);

		} catch (Exception e) {
			logger.error("Error while saving system settings", e.getMessage());
			throw new HMTPException(e);
		}
		return Response.ok().entity("System settings saved successfully.").build();
	}

	@Override
	public Response getSystemSettings() throws HMTPException {
		long domainId;
		List<SystemSettings> reportSysSettings = new ArrayList<>();
		List<SystemSettingsDto> systemSettingsDto = new ArrayList<>();
		try {
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			reportSysSettings = systemSettingsReadRepository.findAllSystemSettingsByType(domainId);

			for (SystemSettings ss : reportSysSettings) {
				SystemSettingsDto systemSettingDto = new SystemSettingsDto();
				systemSettingDto.setActiveFlag(ss.getActiveFlag());
				systemSettingDto.setAttributeName(ss.getAttributeName());
				systemSettingDto.setAttributeType(ss.getAttributeType());
				systemSettingDto.setAttributeValue(ss.getAttributeValue());
				systemSettingDto.setCreatedBy(ss.getCreatedBy());
				systemSettingDto.setCreatedDateTime(ss.getCreatedDateTime());
				systemSettingDto.setId(ss.getId());
				systemSettingDto.setImage(null);
				systemSettingDto.setUpdatedBy(ss.getUpdatedBy());
				systemSettingDto.setUpdatedDateTime(ss.getUpdatedDateTime());
				systemSettingsDto.add(systemSettingDto);
			}

		} catch (Exception e) {
			logger.error("Error while fetching system settings", e.getMessage());
			throw new HMTPException();
		}
		return Response.ok().entity(systemSettingsDto).build();
	}

	@Override
	public byte[] getLabLogo() throws HMTPException {
		long domainId;
		byte[] image = null;
		try {
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			image = systemSettingsReadRepository.getLabLogo(domainId);
		} catch (Exception e) {
			logger.error("Error while fetching lab logo", e.getMessage());
			throw new HMTPException();
		}
		return image;
	}


}
