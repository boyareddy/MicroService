package com.roche.connect.adm.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.adm.dto.ToasterNotificationDTO;
import com.roche.connect.adm.error.CustomErrorCodes;
import com.roche.connect.adm.model.BackupHistory;
import com.roche.connect.adm.model.MessageTemplate;
import com.roche.connect.adm.model.SystemSettings;
import com.roche.connect.adm.repository.BackupHistoryReadRepository;
import com.roche.connect.adm.repository.CompanyReadRepository;
import com.roche.connect.adm.repository.MessageTemplateReadRepository;
import com.roche.connect.adm.repository.SystemSettingsReadRepository;
import com.roche.connect.adm.rest.AdminCrudRestAPI;
import com.roche.connect.adm.util.ADMConstants;
import com.roche.connect.adm.util.BackupConstants;
import com.roche.connect.adm.writerepository.BackupHistoryWriteRepository;
import com.roche.connect.common.constant.NotificationGroupType;

@Service
public class BackupAndRestoreServiceImpl implements BackupAndRestoreService {

	@Value("${backup.fileBackupScriptLocation}")
	String fileBackupScriptLocation;

	@Value("${backup.defaultBackupLocation}")
	String defaultBakupLocation;

	@Value("${backup.day_of_week}")
	int dayOfWeek;

	@Value("${backup.day_of_month}")
	int dayOfMonth;

	@Value("${backup.resultFileLocation}")
	String resultFileLocation;

	@Value("${pas.security_remote_url}")
	private String securityAPI;

	@Value("${backup.backupCron}")
	private String backupCron;

	@Value("${pas.org}")
	private String domainName;

	/** The login url. */
	@Value("${pas.authenticate_url}")
	private String loginUrl;

	/** The login entity. */
	@Value("${pas.login_entity}")
	private String loginEntity;

	@Value("${backup.adm_notification_uri}")
	private String notificationUri;

	@Value("${pas.application_url}")
	private String appUrl;

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat notificationDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z");
	@Autowired
	private MessageTemplateReadRepository messageTemplateReadRepository;

	@Autowired
	BackupHistoryReadRepository backupHistoryReadRepository;

	@Autowired
	IMessageService messageService;

	@Autowired
	BackupHistoryWriteRepository backupHistoryWriteRepository;

	@Autowired
	AdminCrudRestAPI adminCrudRestAPI;

	@Autowired
	SystemSettingsReadRepository systemSettingsReadRepository;

	@Autowired
	CompanyReadRepository companyReadRepository;

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	private String backupType = null;

	@Override
	@Async
	public void takeBackup(long domainId, String username, String destinationPath) {
		logger.info("Entered BackupAndRestoreServiceImpl.takeBackup() ");
		if (backupType == null) {
			backupType = BackupConstants.MANUAL;
		}
		logger.info("domainId " + domainId + "\n username:" + username);
		Path resultFilepath = null;
		List<String> lines = null;
		Timestamp now = Timestamp.from(Instant.now());
		logger.info("BackupAndRestoreServiceImpl.takeBackup() destination path :" + destinationPath);
		BackupHistory backupHistory = new BackupHistory();
		backupHistory.setDestinationFolder(destinationPath);
		backupHistory.setStatus(BackupConstants.INPROGRESS);
		backupHistory.setBackupType(backupType);
		backupHistory.setCreatedBy(username);
		backupHistory.setCreatedDateTime(now);
		SystemSettings backupIntervalSettings = systemSettingsReadRepository
				.findByAttributeName(ADMConstants.BACKUPINTERVAL.toString());
		if (backupIntervalSettings != null)
			backupHistory.setInterval(backupIntervalSettings.getAttributeValue());
		else
			logger.error(
					"Unable to set Interval, as no backup settings is available, please add backup settings and try again");
		Company company = new Company();
		company.setId(domainId);
		backupHistory.setCompany(company);
		try {
			lines = Files.readAllLines(Paths.get(resultFileLocation));
			String statusCode = lines.get(0).split("=")[1];
			if (!statusCode.equals("1")) {
				resultFilepath = Paths.get(resultFileLocation);
				Files.delete(resultFilepath);
			}
		} catch (IOException e) {
			logger.error("Result File is missing" + e.getMessage());
		}
		backupHistoryWriteRepository.save(backupHistory);

		ProcessBuilder processBuilder = new ProcessBuilder();
		String[] command = { "sh", fileBackupScriptLocation, destinationPath };
		processBuilder.command(command);
		try {
			logger.info("Starting the backup script:");
			Process process = processBuilder.start();
			sendNotification(backupType, notificationDateFormat.format(new Date()).toString(), NotificationGroupType.BACKUP_TRIGGER_GEN_4);

			try {
				int exitCode = process.waitFor();
				BufferedReader reader = null;
				if (exitCode != 0) {
					logger.error("Error occured in BackupAndRestoreServiceImpl.takeBackup() :" + exitCode);
					reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				} else {
					reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				}

				String line;
				while ((line = reader.readLine()) != null) {
					logger.info(line);
				}
				logger.info(String.valueOf(process.exitValue()));
			} catch (Exception e) {
				logger.error(
						"Error occured in BackupAndRestoreServiceImpl.takeBackup() while displaying script output :"
								+ e.getMessage());
			}
		} catch (IOException e) {
			backupHistory.setStatus(BackupConstants.FAILED);
			backupHistory.setUpdatedDateTime(Timestamp.from(Instant.now()));
			backupHistoryWriteRepository.save(backupHistory);
			sendNotification(backupType, notificationDateFormat.format(new Date()).toString(), NotificationGroupType.BACKUP_TRIGGER_GEN_2);
			messageService.sendNotificationToaster(getToasterMessage(),
					getRolesId(NotificationGroupType.BACKUP_TRIGGER_GEN_2, NotificationGroupType.LOCALE.toString()));
			logger.error(
					"Error occured in BackupAndRestoreServiceImpl.takeBackup() Shell script is not available in the specified location:"
							+ e.getMessage());
		} catch (Exception e) {
			backupHistory.setStatus(BackupConstants.FAILED);
			backupHistory.setUpdatedDateTime(Timestamp.from(Instant.now()));
			backupHistoryWriteRepository.save(backupHistory);
			sendNotification(backupType, notificationDateFormat.format(new Date()).toString(), NotificationGroupType.BACKUP_TRIGGER_GEN_2);
			messageService.sendNotificationToaster(getToasterMessage(),
					getRolesId(NotificationGroupType.BACKUP_TRIGGER_GEN_2, NotificationGroupType.LOCALE.toString()));
			logger.error("Error occured in BackupAndRestoreServiceImpl.takeBackup() :" + e.getMessage());
		}
		logger.info("Exiting BackupAndRestoreServiceImpl.takeBackup() ");

	}

	@Override
	@Scheduled(cron = "${backup.statusCheckCron}")
	public void updateBackupStatus() {
		logger.info("Entered BackupAndRestoreServiceImpl.updateBackupStatus() ");
		List<BackupHistory> backupHistoryList = backupHistoryReadRepository.findByStatus(BackupConstants.INPROGRESS);
		String statusCode ="";
		if (!backupHistoryList.isEmpty()) {
			BackupHistory backupHistory = backupHistoryList.get(0);
			List<String> lines = null;
			try {
				lines = Files.readAllLines(Paths.get(resultFileLocation));
			} catch (IOException e) {
				logger.error("unable to get the result file from server :" + e.getMessage());
			}
			if (lines.get(0).split("=")[1] != null) {
				statusCode = lines.get(0).split("=")[1];
			}
			switch (statusCode) {
			case ("1"):
				backupHistory.setStatus(BackupConstants.INPROGRESS);
				break;
			case ("-1"):
				backupHistory.setStatus(BackupConstants.FAILED);
				try {
					sendNotification(backupHistory.getBackupType(), notificationDateFormat.format(new Date()).toString(),
							NotificationGroupType.BACKUP_TRIGGER_GEN_2);
					messageService.sendNotificationToaster(getToasterMessage(), getRolesId(
							NotificationGroupType.BACKUP_TRIGGER_GEN_2, NotificationGroupType.LOCALE.toString()));
				} catch (Exception e) {
					logger.error(
							"Error occured in BackupAndRestoreServiceImpl.takeBackup() while sending Notification for failed :"
									+ e.getMessage());
				}
				break;
			case ("0"):
				try {
					sendNotification(backupHistory.getBackupType(), notificationDateFormat.format(new Date()).toString(),
							NotificationGroupType.BACKUP_TRIGGER_GEN_3);
					messageService.sendNotificationToaster(getToasterMessage(), getRolesId(
							NotificationGroupType.BACKUP_TRIGGER_GEN_3, NotificationGroupType.LOCALE.toString()));
				} catch (Exception e) {
					logger.error(
							"Error occured in BackupAndRestoreServiceImpl.takeBackup() while sending Notification for completed :"
									+ e.getMessage());
				}
				backupHistory.setStatus(BackupConstants.COMPLETED);
				break;
			default:
				backupHistory.setStatus(BackupConstants.INPROGRESS);
				break;

			}
			backupHistory.setUpdatedDateTime(Timestamp.from(Instant.now()));
			backupHistoryWriteRepository.save(backupHistory);

		} else {
			logger.error("Currently there is no backup history with status InProgress ");
		}
	}

	@Override
	public void saveSchedule(Map<String, String> backupSettings) throws HMTPException {
		String username = ThreadSessionManager.currentUserSession().getAccessorUserName();
		String backupInterval = backupSettings.get(ADMConstants.BACKUPINTERVAL.toString());
		String backupLocation = backupSettings.get(ADMConstants.BACKUPLOCATION.toString());
		logger.info("Entered BackupAndRestoreServiceImpl.saveSchedule() with interval :" + backupInterval
				+ " location :" + backupLocation);
		SystemSettings backupIntervalSettings = systemSettingsReadRepository
				.findByAttributeName(ADMConstants.BACKUPINTERVAL.toString());
		SystemSettings backupLocationSettings = systemSettingsReadRepository
				.findByAttributeName(ADMConstants.BACKUPLOCATION.toString());
		if (backupIntervalSettings != null && backupLocationSettings != null) {
			if (backupInterval.equalsIgnoreCase(backupIntervalSettings.getAttributeValue())) {
				logger.info("No Change in the interval");
			} else {
				backupIntervalSettings.setUpdatedBy(username);
				backupIntervalSettings.setUpdatedDateTime(Timestamp.from(Instant.now()));
				backupIntervalSettings.setAttributeValue(backupInterval);
				systemSettingsReadRepository.save(backupIntervalSettings);
				logger.info("BackupAndRestoreServiceImpl.saveSchedule() is completed successfully");
			}
			if (backupLocation.equalsIgnoreCase(backupLocationSettings.getAttributeValue())) {
				logger.info("No Change in the location");
			} else {
				backupLocationSettings.setUpdatedBy(username);
				backupLocationSettings.setUpdatedDateTime(Timestamp.from(Instant.now()));
				backupLocationSettings.setAttributeValue(backupLocation);
				systemSettingsReadRepository.save(backupLocationSettings);
				logger.info("BackupAndRestoreServiceImpl.saveSchedule() is completed successfully");
			}
		} else {
			throw new HMTPException(CustomErrorCodes.SYSTEM_SETTINGS_NOT_AVAILABLE,
					ADMConstants.SYSTEM_SETTINGS_NOT_AVAILABLE_FOR_BACKUP.toString());
		}
	}

	@Override
	@Scheduled(cron = "${backup.backupCron}")
	public void runScheduledBackup() {
		logger.info("Entered BackupAndRestoreServiceImpl.runScheduledBackup()");
		Date currentDate = new Date();
		Timestamp scheduledDate = null;
		backupType = BackupConstants.SCHEDULED;
		SystemSettings backupIntervalSettings = systemSettingsReadRepository
				.findByAttributeName(ADMConstants.BACKUPINTERVAL.toString());
		if (backupIntervalSettings != null) {
			scheduledDate = getNextSchedule(backupIntervalSettings.getAttributeValue());
			try {
				if (scheduledDate != null) {
					if (scheduledDate
							.equals(new Timestamp(dateFormat.parse((dateFormat.format(currentDate))).getTime())))
						takeBackup(getDomainId(), BackupConstants.SYSTEM, defaultBakupLocation);
				} else {
					sendNotification(backupType, notificationDateFormat.format(currentDate).toString(), NotificationGroupType.BACKUP_TRIGGER_GEN_1);
					messageService.sendNotificationToaster(getToasterMessage(), getRolesId(
							NotificationGroupType.BACKUP_TRIGGER_GEN_1, NotificationGroupType.LOCALE.toString()));
					logger.error(
							"unable to fetch data from SystemSettings BackupAndRestoreServiceImpl.runScheduledBackup()");
				}
			} catch (ParseException e) {
				sendNotification(backupType, notificationDateFormat.format(currentDate).toString(), NotificationGroupType.BACKUP_TRIGGER_GEN_1);
				messageService.sendNotificationToaster(getToasterMessage(), getRolesId(
						NotificationGroupType.BACKUP_TRIGGER_GEN_1, NotificationGroupType.LOCALE.toString()));
				logger.error("unable to parse scheduledDate BackupAndRestoreServiceImpl.runScheduledBackup() ");
			}

		} else {
			logger.error("Error in BackupAndRestoreServiceImpl.runScheduledBackup()"
					+ ADMConstants.SYSTEM_SETTINGS_NOT_AVAILABLE_FOR_BACKUP.toString());
		}

	}

	@Override
	public Timestamp getNextSchedule(String scheduleType) {
		logger.info("Entered BackupAndRestoreServiceImpl.getNextSchedule() :" + scheduleType);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, Integer.parseInt(backupCron.split(" ")[0]));
		calendar.set(Calendar.MINUTE, Integer.parseInt(backupCron.split(" ")[1]));
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(backupCron.split(" ")[2]));
		logger.info("second :" + backupCron.split(" ")[0]);
		logger.info("hour :" + backupCron.split(" ")[2]);
		logger.info("Minutes :" + backupCron.split(" ")[1]);
		logger.info("backupCron :" + backupCron);
		switch (scheduleType) {
		case (BackupConstants.WEEKLYSCHEDULE):
			if (dayOfWeek >= calendar.get(Calendar.DAY_OF_WEEK)) {
				calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			} else {
				calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
				calendar.add(Calendar.WEEK_OF_MONTH, 1);
			}
			break;
		case (BackupConstants.MONTHLYSCHEDYULE):
			if (dayOfMonth >= calendar.get(Calendar.DAY_OF_MONTH)) {
				calendar.set(Calendar.DATE, dayOfMonth);
			} else {
				calendar.add(Calendar.MONTH, 1);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			}
			break;
		case (BackupConstants.DAILYSCHEDULE):
			try {
				return new Timestamp(dateFormat.parse(dateFormat.format(calendar.getTime())).getTime());
			} catch (ParseException e1) {
				logger.error("unable to parse current next date BackupAndRestoreServiceImpl.getNextSchedule() ");
				logger.error(e1.getMessage());
			}
			break;
		default:
			logger.error("no match found for the case BackupAndRestoreServiceImpl.getNextSchedule()");
			calendar.setTime(null);
			break;
		}

		try {
			return new Timestamp(dateFormat.parse(dateFormat.format(calendar.getTime())).getTime());
		} catch (ParseException e) {
			logger.error("unable to parse current next date BackupAndRestoreServiceImpl.getNextSchedule() :"
					+ e.getMessage());
		}
		return null;
	}

	public void sendNotification(String param1, String param2, NotificationGroupType type) {

		logger.info("Entered BackupAndRestoreServiceImpl.sendNotification()");
		List<String> contents = new ArrayList<>();
		if (param1 != null)
			contents.add(param1);
		else
			contents.add(backupType);
		contents.add(param2);
		try {
			AdmNotificationService.sendNotification(type, contents, getToken(), appUrl + notificationUri);
		} catch (HMTPException e) {
			logger.error("Somthing went wrong wile updating notification:" + e.getMessage());
		}

	}

	public ToasterNotificationDTO getToasterMessage() {
		logger.info("Entered BackupAndRestoreServiceImpl.getToasterMessage()");
		ToasterNotificationDTO message = new ToasterNotificationDTO();
		message.setMsg(ADMConstants.MESSAGE.toString());
		message.setSeverity(ADMConstants.SEVERITY.toString());
		message.setTitle(ADMConstants.TITLE.toString());
		message.setTopic(ADMConstants.MODULE.toString());
		message.setCreatedDateTime(Timestamp.from(Instant.now()));
		logger.info("Finished BackupAndRestoreServiceImpl.sendNotification()");
		return message;
	}

	public long getDomainId() {
		long companyId = 0;
		logger.info("Entered BackupAndRestoreServiceImpl.getDomainId()");
		Company company = companyReadRepository.findByDomainName(domainName);
		if (company != null) {
			logger.info("CompanyId from BackupAndRestoreServiceImpl.getDomainId() :" + company.getId());
			companyId = company.getId();
		}
		return companyId;
	}

	public String getToken() {
		Builder builder = RestClientUtil.getBuilder(loginUrl, null);
		return builder.post(Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED), String.class);

	}

	@Override
	public String getFormatedTime(Timestamp time) {
		return dateFormat.format(time.getTime());

	}

	public Set<Integer> getRolesId(NotificationGroupType messageGroup, String locale) {
		long domainId = ThreadSessionManager.currentUserSession().getUserId();
		if (domainId < 0)
			domainId = getDomainId();
		List<MessageTemplate> messageTemplates = messageTemplateReadRepository
				.findMessageTemplate(messageGroup.toString(), locale, domainId);

		return messageTemplates.stream().map(m -> m.getMessageRecepients()).map(s -> s.iterator().next().getRoleId())
				.collect(Collectors.toSet());

	}

}
