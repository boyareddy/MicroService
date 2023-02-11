package com.roche.connect.adm.rest.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.server.entity.Company;
import com.roche.connect.adm.error.CustomErrorCodes;
import com.roche.connect.adm.model.BackupHistory;
import com.roche.connect.adm.model.LabDetails;
import com.roche.connect.adm.model.Message;
import com.roche.connect.adm.model.MessageRecipient;
import com.roche.connect.adm.model.MessageTemplate;
import com.roche.connect.adm.model.ProblemReport;
import com.roche.connect.adm.model.Recipient;
import com.roche.connect.adm.util.ADMConstants;
import com.roche.connect.adm.util.AuditTrailDetailDTO;
import com.roche.connect.adm.util.AuditTrailResponseDTO;
import com.roche.connect.adm.util.BackupConstants;
import com.roche.connect.adm.util.DeviceSummaryDTO;
import com.roche.connect.adm.util.EntityDTO;
public class DTOAndModelTest {
	
	@Test
	public void auditTrailDetailDTOTest() {
		AuditTrailDetailDTO auditTrailDetailDTO = new AuditTrailDetailDTO();
		auditTrailDetailDTO.setCompanydomainname("");
		auditTrailDetailDTO.setId(123);
		auditTrailDetailDTO.setMessage("");
		auditTrailDetailDTO.setMessagecode("");
		auditTrailDetailDTO.setNewnessflag("");
		auditTrailDetailDTO.setObjectnewvalue(new JSONObject());
		auditTrailDetailDTO.setObjectoldvalue(new JSONObject());
		auditTrailDetailDTO.setParams(new JSONObject());
		auditTrailDetailDTO.setOwnerPropertyName("");
		auditTrailDetailDTO.setSystemid("");
		auditTrailDetailDTO.setSystemmodulename("");
		auditTrailDetailDTO.setTimestamp("");
		auditTrailDetailDTO.setTitle("");
		auditTrailDetailDTO.setUserid("admin");
		auditTrailDetailDTO.setIpaddress("127.0.0.1");

		Assert.assertEquals(auditTrailDetailDTO.getCompanydomainname(), "");
		Assert.assertEquals(auditTrailDetailDTO.getId(), new Integer(123));
		Assert.assertEquals(auditTrailDetailDTO.getMessage(), "");
		Assert.assertEquals(auditTrailDetailDTO.getMessagecode(), "");
		Assert.assertEquals(auditTrailDetailDTO.getNewnessflag(), "");
		assertNotNull(auditTrailDetailDTO.getObjectnewvalue());
		assertNotNull(auditTrailDetailDTO.getObjectoldvalue());
		Assert.assertEquals(auditTrailDetailDTO.getOwnerPropertyName(), "");
		assertNotNull(auditTrailDetailDTO.getParams());
		Assert.assertEquals(auditTrailDetailDTO.getSystemid(), "");
		Assert.assertEquals(auditTrailDetailDTO.getSystemmodulename(), "");
		Assert.assertEquals(auditTrailDetailDTO.getTimestamp(), "");
		Assert.assertEquals(auditTrailDetailDTO.getTitle(), "");
		Assert.assertEquals(auditTrailDetailDTO.getUserid(), "admin");
		Assert.assertEquals(auditTrailDetailDTO.getIpaddress(), "127.0.0.1");
		auditTrailDetailDTO.toString();
	}

	@Test
	public void auditTrailResponseDTOTest() {
		AuditTrailResponseDTO auditTrailResponseDTO = new AuditTrailResponseDTO();
		auditTrailResponseDTO.setEntity(new EntityDTO());
		assertNotNull(auditTrailResponseDTO.getEntity());
		auditTrailResponseDTO.toString();
	}

	@Test
	public void antityDTOTest() {
		EntityDTO entityDTO = new EntityDTO();
		entityDTO.setLstAuditTrail(new ArrayList<AuditTrailDetailDTO>());
		entityDTO.setTotalCount(10);
		assertNotNull(entityDTO.getLstAuditTrail());
		assertEquals(entityDTO.getTotalCount(),10);
		entityDTO.toString();
	}

	@Test
	public void admConstantsTest() {
		ADMConstants.NOTIFICATION_ADD_FAILED.toString();
		ADMConstants.NOTIFICATION_BUILD_FAILED.toString();
		ADMConstants.NOTIFICATION_FETCH_FAILED.toString();
		ADMConstants.NOTIFICATION_GROUP_NOT_VALID.toString();
		ADMConstants.NOTIFICATION_NOT_AVAILABLE.toString();
		ADMConstants.NOTIFICATION_TEMPLATE_NOT_FOUND.toString();
		ADMConstants.NOTIFICATION_UPDATE_FAILED.toString();
		ADMConstants.VERSION_FETCH_FAILED.toString();
		ADMConstants.YES.toString();
	}
	
	@Test
	public void backupConstantsTest() {
		BackupConstants.DAILYSCHEDULE.toString();
		BackupConstants.WEEKLYSCHEDULE.toString();
		BackupConstants.MONTHLYSCHEDYULE.toString();
		BackupConstants.QUARTERLYSCHEDYULE.toString();
		BackupConstants.BACKUP.toString();
		BackupConstants.INPROGRESS.toString();
		BackupConstants.COMPLETED.toString();
		BackupConstants.FAILED.toString();
		BackupConstants.UI.toString();
		BackupConstants.LASTBACKUPDATE.toString();
		BackupConstants.NEXTBACKUPDATE.toString();
		BackupConstants.INTERVEL.toString();
		BackupConstants.DESTINATIONPATH.toString();
		BackupConstants.SCHEDULED.toString();
		BackupConstants.MANUAL.toString();
		BackupConstants.NOLOGFILE.toString();
		BackupConstants.STATUS.toString();
		BackupConstants.SYSTEM.toString();
		BackupConstants.BROWNSTONEAUTHCOOKIE.toString();
	}

	@Test
	public void messageTest() {
		Message message = new Message();
		message.setCompany(new Company());
		message.setCreatedUser("");
		message.setCreatedDateTime(new Timestamp(234879));
		message.setDescription("");
		message.setId(2146);
		message.setMessageTemplate(new MessageTemplate());
		message.setModule("");
		message.setSeverity("");
		message.setTitle("");
		message.setViewed("");
		message.setViewedDateTime(new Timestamp(234879));

		assertNotNull(message.getCompany());
		Assert.assertEquals(message.getCreatedUser(), "");
		assertNotNull(message.getCreatedDateTime());
		Assert.assertEquals(message.getDescription(), "");
		Assert.assertEquals(message.getId(), 2146);
		assertNotNull(message.getMessageTemplate());
		Assert.assertEquals(message.getModule(), "");
		Assert.assertEquals(message.getSeverity(), "");
		Assert.assertEquals(message.getTitle(), "");
		Assert.assertEquals(message.getViewed(), "");
		assertNotNull(message.getViewedDateTime());
		message.getOwnerPropertyName();
		message.toString();
	}

	@Test
	public void messageRecipientTest() {
		MessageRecipient messageRecipient = new MessageRecipient();
		messageRecipient.setCompany(new Company());
		messageRecipient.setCreatedBy("");
		messageRecipient.setCreatedDateTime(new Timestamp(234879));
		messageRecipient.setId(2146);
		messageRecipient.setMessageTemplate(new MessageTemplate());
		messageRecipient.setRoleId(124);
		messageRecipient.setType(5);
		messageRecipient.setUserId(124);
		messageRecipient.setUpdatedBy("");
		messageRecipient.setUpdatedDateTime(new Timestamp(234879));

		assertNotNull(messageRecipient.getCompany());
		Assert.assertEquals(messageRecipient.getCreatedBy(), "");
		assertNotNull(messageRecipient.getCreatedDateTime());
		Assert.assertEquals(messageRecipient.getId(), 2146);
		assertNotNull(messageRecipient.getMessageTemplate());
		Assert.assertEquals(messageRecipient.getRoleId(), new Integer(124));
		Assert.assertEquals(messageRecipient.getType(), 5);
		Assert.assertEquals(messageRecipient.getUpdatedBy(), "");
		assertNotNull(messageRecipient.getUpdatedDateTime());
		messageRecipient.getOwnerPropertyName();
		messageRecipient.toString();
	}

	@Test
	public void messageTemplateTest() {

		MessageTemplate messageTemplate = new MessageTemplate();
		messageTemplate.setCompany(new Company());
		messageTemplate.setCreatedBy(null);
		messageTemplate.setCreatedDateTime(new Timestamp(234879));
		messageTemplate.setDescription("");
		messageTemplate.setId(2146);
		messageTemplate.setActiveFlag("");
		messageTemplate.setChannel("");
		messageTemplate.setModule("");
		messageTemplate.setSeverity("");
		messageTemplate.setTitle("");
		messageTemplate.setLocale("");
		messageTemplate.setMessageGroup("");
		messageTemplate.setPriority(1);
		messageTemplate.setMessageRecepients(new ArrayList<MessageRecipient>());
		messageTemplate.setMessages(new ArrayList<Message>());
		messageTemplate.setUpdatedBy("");
		messageTemplate.setUpdatedDateTime(new Timestamp(234879));

		assertNotNull(messageTemplate.getCompany());
		Assert.assertEquals(messageTemplate.getCreatedBy(), null);
		assertNotNull(messageTemplate.getCreatedDateTime());
		Assert.assertEquals(messageTemplate.getDescription(), "");
		Assert.assertEquals(messageTemplate.getId(), 2146);
		Assert.assertEquals(messageTemplate.getActiveFlag(), "");
		Assert.assertEquals(messageTemplate.getChannel(), "");
		Assert.assertEquals(messageTemplate.getModule(), "");
		Assert.assertEquals(messageTemplate.getSeverity(), "");
		Assert.assertEquals(messageTemplate.getTitle(), "");
		Assert.assertEquals(messageTemplate.getLocale(), "");
		Assert.assertEquals(messageTemplate.getMessageGroup(), "");
		Assert.assertEquals(messageTemplate.getPriority(), new Integer(1));
		assertNotNull(messageTemplate.getMessageRecepients());
		assertNotNull(messageTemplate.getMessages());
		Assert.assertEquals(messageTemplate.getUpdatedBy(), "");
		assertNotNull(messageTemplate.getUpdatedDateTime());
		messageTemplate.getOwnerPropertyName();
		messageTemplate.toString();
	}

	@Test
	public void customErrorCodesTest() {
		Assert.assertEquals(CustomErrorCodes.NOTIFICATION_ADD_FAILED.getErrorCode(), 500);
		Assert.assertEquals(CustomErrorCodes.NOTIFICATION_BUILD_FAILED.getErrorCode(), 500);
		Assert.assertEquals(CustomErrorCodes.NOTIFICATION_FETCH_FAILED.getErrorCode(), 500);
		Assert.assertEquals(CustomErrorCodes.NOTIFICATION_GROUP_NOT_VALID.getErrorCode(), 500);
		Assert.assertEquals(CustomErrorCodes.NOTIFICATION_NOT_AVAILABLE.getErrorCode(), 500);
		Assert.assertEquals(CustomErrorCodes.NOTIFICATION_TEMPLATE_NOT_FOUND.getErrorCode(), 500);
		Assert.assertEquals(CustomErrorCodes.NOTIFICATION_UPDATE_FAILED.getErrorCode(), 500);
		Assert.assertEquals(CustomErrorCodes.VERSION_FILE_NOT_AVAILABLE.getErrorCode(), 404);
	}

	@Test
	public void recipientTest() {
		Recipient recipient = new Recipient();
		recipient.setCompany(new Company());
		recipient.setCreatedBy("");
		recipient.setCreatedDateTime(new Timestamp(234879));
		recipient.setId(2146);
		recipient.setMessage(new Message());
		recipient.setLoginName("");
		recipient.setUpdatedBy("");
		recipient.setUpdatedDateTime(new Timestamp(234879));

		assertNotNull(recipient.getCompany());
		Assert.assertEquals(recipient.getCreatedBy(), "");
		assertNotNull(recipient.getCreatedDateTime());
		Assert.assertEquals(recipient.getId(), 2146);
		assertNotNull(recipient.getMessage());
		Assert.assertEquals(recipient.getLoginName(), "");
		Assert.assertEquals(recipient.getUpdatedBy(), "");
		assertNotNull(recipient.getUpdatedDateTime());
		recipient.getOwnerPropertyName();
		recipient.toString();
	}

	@Test
	public void backupHistoryTest() {
		BackupHistory backupHistory = new BackupHistory();
		backupHistory.setBackupType("daily");
		backupHistory.setCompany(null);
		backupHistory.setCreatedBy("admin");
		backupHistory.setCreatedDateTime(new Timestamp(234879));
		backupHistory.setErrorReason("unable to take backup");
		backupHistory.setId(1l);
		backupHistory.setInterval("two");
		backupHistory.setStatus("pass");
		backupHistory.setUpdatedBy("admin");
		backupHistory.setProcessId("processId");
		backupHistory.setDestinationFolder("destination");
		backupHistory.setUpdatedDateTime(new Timestamp(234879));
		backupHistory.setEditedBy("admin");
		backupHistory.setCreatedDate(new Date(234879));
		backupHistory.setModifiedDate(new Date(234879));
		assertNull(backupHistory.getCompany());
		assertEquals(backupHistory.getBackupType(), "daily");
		assertEquals(backupHistory.getCreatedBy(), "admin");
		assertNotNull(backupHistory.getCreatedDateTime());
		assertNotNull(backupHistory.getUpdatedDateTime());
		assertEquals(backupHistory.getDestinationFolder(), "destination");
		assertEquals(backupHistory.getOwnerPropertyName(), null);
		assertEquals(backupHistory.getProcessId(), "processId");
		assertEquals(backupHistory.getId(), 1l);
		assertEquals(backupHistory.getInterval(), "two");
		assertEquals(backupHistory.getErrorReason(), "unable to take backup");
		assertEquals(backupHistory.getStatus(), "pass");
		assertEquals(backupHistory.getUpdatedBy(), "admin");
		backupHistory.getEditedBy();
		assertNull(backupHistory.getCreatedDate());
		assertNull(backupHistory.getModifiedDate());
		
	}

	@Test
	public void deviceSummaryDTOTest() {

		DeviceSummaryDTO dto = new DeviceSummaryDTO();
		dto.setUpdatedDateTime("24-05-2019");
		dto.setProcessStepName("NA_EXTRACTION");
		dto.setDeviceId("deviceId");
		dto.setDeviceType("assayType");
		dto.setRunStatus("open");
		dto.setAssayType("NIPTHTP");
		dto.setDeviceRunId("12345");
		assertEquals(dto.getDeviceId(), "deviceId");
		assertEquals(dto.getProcessStepName(), "NA_EXTRACTION");
		assertEquals(dto.getUpdatedDateTime(), "24-05-2019");
		assertEquals(dto.getRunStatus(), "open");
		assertEquals(dto.getAssayType(), "NIPTHTP");
		assertEquals(dto.getDeviceType(), "assayType");
		assertEquals(dto.getDeviceRunId(), "12345");
		dto.toString();
	}

	@Test
	public void getMessageRecipient() {
		MessageRecipient messageRecipient = new MessageRecipient();
		Company company = new Company();
		messageRecipient.setCompany(company);
		messageRecipient.setCreatedBy("admin");
		messageRecipient.setCreatedDate(new Date());
		messageRecipient.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
		messageRecipient.setCreatedUser("admin");
		messageRecipient.setEditedBy("admin");
		messageRecipient.setId(1);
		MessageTemplate messageTemplate = new MessageTemplate();
		messageRecipient.setMessageTemplate(messageTemplate);
		messageRecipient.setModifiedDate(new Date());
		messageRecipient.setRoleId(1);
		messageRecipient.setType(2);
		messageRecipient.setUpdatedBy("admin");
		messageRecipient.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
		messageRecipient.setUserId(1);
		Assert.assertEquals(messageRecipient.getCreatedBy(), "admin");
		Assert.assertEquals(messageRecipient.getCreatedUser(), "admin");
		Assert.assertEquals(messageRecipient.getEditedBy(), "admin");
		Assert.assertEquals(messageRecipient.getUpdatedBy(), "admin");
		Assert.assertEquals(messageRecipient.getModifiedDate().getYear(), new Date().getYear());
		Assert.assertEquals(messageRecipient.getCreatedDate().getYear(), new Date().getYear());

	}

	@Test
	public void getMessageTemplateTest() {
		MessageTemplate messageTemplate = new MessageTemplate();
		messageTemplate.setCreatedUser("admin");
		messageTemplate.setEditedBy("admin");
		messageTemplate.setCreatedDate(new Date());
		messageTemplate.setModifiedDate(new Date());
		messageTemplate.setCreatedBy("admin");
		Assert.assertEquals("admin", messageTemplate.getCreatedUser());
		Assert.assertEquals("admin", messageTemplate.getEditedBy());
		Assert.assertEquals("admin", messageTemplate.getCreatedBy());
		Assert.assertEquals(new Date().getYear(), messageTemplate.getCreatedDate().getYear());
		Assert.assertEquals(new Date().getYear(), messageTemplate.getModifiedDate().getYear());
	}

	@Test
	public void getRecipientTest() {
		Recipient recipient = new Recipient();
		recipient.setCreatedUser("admin");
		recipient.setEditedBy("admin");
		recipient.setCreatedBy("admin");
		recipient.setCreatedDate(new Date());
		recipient.setModifiedDate(new Date());

		Assert.assertEquals("admin", recipient.getCreatedUser());
		Assert.assertEquals("admin", recipient.getEditedBy());
		Assert.assertEquals("admin", recipient.getCreatedBy());
		Assert.assertEquals(new Date().getYear(), recipient.getCreatedDate().getYear());
		Assert.assertEquals(new Date().getYear(), recipient.getModifiedDate().getYear());
	}

	@Test
	public void getMessageTest() {
		Message message = new Message();
		message.setCreatedUser("admin");
		message.setEditedBy("admin");
		message.setCreatedBy("admin");
		message.setCreatedDate(new Date());
		message.setModifiedDate(new Date());

		Assert.assertEquals("admin", message.getCreatedUser());
		Assert.assertEquals("admin", message.getEditedBy());
		Assert.assertEquals("admin", message.getCreatedBy());
		Assert.assertEquals(new Date().getYear(), message.getCreatedDate().getYear());
		Assert.assertEquals(new Date().getYear(), message.getModifiedDate().getYear());
	}

	@Test
	public void labDetailsTest() {
		LabDetails labDetails = new LabDetails();
		labDetails.setLabAddress1("labAddress1");
		labDetails.setLabAddress2("labAddress2");
		labDetails.setLabAddress3("labAddress3");
		labDetails.setLabLogo("labLogo");
		labDetails.setLabLogoImageName("labLogoImageName");
		labDetails.setLabName("labName");
		labDetails.setPhoneNumber("phoneNumber");
		assertEquals(labDetails.getLabAddress1(), "labAddress1");
		assertEquals(labDetails.getLabAddress2(), "labAddress2");
		assertEquals(labDetails.getLabAddress3(), "labAddress3");
		assertEquals(labDetails.getLabLogo(), "labLogo");
		assertEquals(labDetails.getLabLogoImageName(), "labLogoImageName");
		assertEquals(labDetails.getPhoneNumber(), "phoneNumber");
		assertEquals(labDetails.getLabName(), "labName");

	}
	
	@Test
	public void problemReportTest() {
		ProblemReport problemReport = new ProblemReport();
		problemReport.setCreatedBy("admin");
		problemReport.setCreatedDate(new Date());
		problemReport.setEditedBy("admin");
		problemReport.setEndDate("end date");
		problemReport.setId(1l);
		problemReport.setModifiedDate(new Date());
		problemReport.setProblemReportPath("problemreport");
		problemReport.setStartDate("start date");
		assertEquals(problemReport.getCreatedBy(), "admin");
		assertNotNull(problemReport.getCreatedDate());
		assertEquals(problemReport.getEditedBy(), "admin");
		assertEquals(problemReport.getEndDate(), "end date");
		assertEquals(problemReport.getId(), 1l);
		assertNotNull(problemReport.getModifiedDate());
		assertNull(problemReport.getOwnerPropertyName());
		problemReport.getProblemReportPath();
		assertEquals(problemReport.getStartDate(), "start date");
		
	}
	
	

}

