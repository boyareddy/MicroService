package com.roche.connect.adm.controller.test;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.emailservice.client.beans.EmailMessageBean;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.adm.dto.SystemSettingsDto;
import com.roche.connect.adm.model.Message;
import com.roche.connect.adm.model.MessageTemplate;
import com.roche.connect.adm.model.SystemSettings;
import com.roche.connect.adm.repository.MessageReadRepository;
import com.roche.connect.adm.repository.MessageTemplateReadRepository;
import com.roche.connect.adm.repository.SystemSettingsReadRepository;
import com.roche.connect.adm.rest.AdminCrudRestAPIImpl;
import com.roche.connect.adm.service.IMessageService;
import com.roche.connect.adm.service.ProblemReportService;
import com.roche.connect.adm.service.ProblemReportServiceImp;
import com.roche.connect.adm.service.VersionDetailsServiceImpl;
import com.roche.connect.adm.util.AuditTrailDetailDTO;
import com.roche.connect.adm.util.AuditTrailResponseDTO;
import com.roche.connect.adm.util.EntityDTO;
import com.roche.connect.adm.util.UtilityService;
import com.roche.connect.adm.writerepository.MessageWriteRepository;
import com.roche.connect.adm.writerepository.ReportWriteRepository;
import com.roche.connect.adm.writerepository.SystemSettingsWriteRepository;
import com.roche.connect.common.constant.NotificationGroupType;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, ThreadSessionManager.class ,UtilityService.class}) 
@PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" })

public class AdminCrudRestAPIImplTest {

	@InjectMocks
	AdminCrudRestAPIImpl adminCrudRestAPIImplInject;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	UserSession userSession;
	@Mock
	MessageTemplateReadRepository messageTemplateReadRepository;
	@Mock
	IMessageService iMessageService;
	@Mock
	EmailMessageBean emailMessageBean;
	@Mock
	HttpServletRequest httpServletRequest;
	@Mock
	UtilityService utilityService;
	@Mock
	MessageReadRepository messageReadRepository;
	@Mock
	MessageWriteRepository messageWriteRepository;
	@Mock
	Builder builder;
	@Mock
	EntityDTO entityDTO;
	@Mock
	AuditTrailDetailDTO auditTrailDetailDTO;
	@Mock
	SystemSettingsDto systemSettingsDto;
	@Mock
	VersionDetailsServiceImpl versionDetailsServiceImpl;
	@Mock
	SystemSettingsWriteRepository systemSettingsWriteRepository;
	@Mock
	SystemSettingsReadRepository systemSettingsReadRepository;
	@Mock
	ObjectMapper mapper;
	@Mock
	SystemSettings systemSettings;
	@Mock
	ReportWriteRepository reportWriteRepository;
	@Mock
	Response response;
	@Mock
	ProblemReportService problemReportService;
	@Mock
	ProblemReportServiceImp problemReportServiceImp;
	@Mock
	AdminCrudRestAPIImpl adminCrudRestAPIImpl;
	@Mock
	File file;
	@Mock
	List<SystemSettingsDto> sysSettingsForValidation;
	
	Message messageTemp = getMessageTemp();

	Cookie[] cookie = { new Cookie("cookieName", "cookie") };
	List<MessageTemplate> messageTemplates = new ArrayList<>();
	List<com.roche.connect.adm.dto.MessageTemplate> messageTemplateFromDTO = new ArrayList<>();
	List<com.roche.connect.adm.dto.Message> messages = new ArrayList<>();
	List<Message> messagesList = new ArrayList<>();

	@BeforeMethod
	@BeforeTest
	public void setUp() throws Exception {
		long domainId = 1L;
		messageTemplates.add(getMessageTemplateChannelEmail());
		messages.add(getMessage());
		messagesList.add(getMessageTemp());
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(domainId);
		Mockito.when((String) ThreadSessionManager.currentUserSession()
				.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString())).thenReturn("token");
		Mockito.when(messageTemplateReadRepository.findMessageTemplate(getMessageDto().getMessageGroup(),
				getMessageDto().getLocale(), domainId)).thenReturn(messageTemplates);
		Mockito.when(iMessageService.constructEmail(getMessageDto(), getMessageTemplateChannelEmail()))
				.thenReturn(emailMessageBean);
		Mockito.when(httpServletRequest.getCookies()).thenReturn(cookie);
		Mockito.when(utilityService.mapTemplateToDto(messageTemplateReadRepository.findMessageTemplates(domainId)))
				.thenReturn(messageTemplateFromDTO);
		Mockito.when(utilityService.mapMessageToDto(messageReadRepository
				.findMessages(ThreadSessionManager.currentUserSession().getAccessorUserName(), domainId)))
				.thenReturn(messages);
		Mockito.when(messageReadRepository.findOne(getMessage().getId())).thenReturn(messageTemp);
		Mockito.when(messageWriteRepository.save(messagesList)).thenReturn(messagesList);
		String manifestFilePath = "build.properties";
		ReflectionTestUtils.setField(adminCrudRestAPIImplInject, "manifestFilePath", manifestFilePath);
		ReflectionTestUtils.setField(adminCrudRestAPIImplInject, "auditURL", "http://localhost");
		ReflectionTestUtils.setField(adminCrudRestAPIImplInject, "columnsToMap",
				"id,companydomainname,messagecode,newnessflag,objectnewvalue,objectoldvalue,params,systemid,systemmodulename,timestamp,userid,message,title,ownerPropertyName");
		ReflectionTestUtils.setField(adminCrudRestAPIImplInject, "folderPath", "C://auditLog//AuditDetails_");
	}

	/**
	 * We need a special {@link IObjectFactory}.
	 * 
	 * @return {@link PowerMockObjectFactory}.
	 */
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@Test
	public void updateNotificationTest() throws HMTPException {
		Mockito.when(messageReadRepository.findOne(getMessage().getId())).thenReturn(messageTemp);
		adminCrudRestAPIImplInject.updateNotification(messages);
	}
	
	@Test
	public void updateNotificationMessageEmptyTest() throws HMTPException {
		List<com.roche.connect.adm.dto.Message> messages = new ArrayList<>();
		Mockito.when(messageReadRepository.findOne(getMessage().getId())).thenReturn(messageTemp);
		adminCrudRestAPIImplInject.updateNotification(messages);
	}
	
	@Test
	public void updateNotificationMessageTempNullTest() {
		Message messageTemp = null;
		Mockito.when(messageReadRepository.findOne(getMessage().getId())).thenReturn(messageTemp);
		try {
			adminCrudRestAPIImplInject.updateNotification(messages);

		} catch (HMTPException e) {
			System.out.println("Exception got caught");
		}
	}
	
	@Test
	public void getNotificationsTest() throws HMTPException {
		adminCrudRestAPIImplInject.getNotifications();
	}

	@Test
	public void getVersionInfoTest() throws HMTPException {
		adminCrudRestAPIImplInject.getVersionInfo();
	}
	
	/*@Test
	public void getVersionInfoVersionJsonNullTest() throws HMTPException {
		BufferedReader br = null;
		String line = null;
		try {
			Mockito.when(AdminCrudRestAPIImpl.class.getClassLoader().getResourceAsStream(manifestFilePath)).thenReturn(inputStream);
			Mockito.when(new BufferedReader(new InputStreamReader(inputStream))).thenReturn(br);
			Mockito.when(br.readLine()).thenReturn(line);
			Mockito.when(line.split("=")).thenReturn(null);
			adminCrudRestAPIImplInject.getVersionInfo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	@Test
	public void getVersionInfoLineNotContainsTest() throws HMTPException {
		adminCrudRestAPIImplInject.getVersionInfo();
	}
	
	@Test
	public void getAuditDetailsTest() {
		PowerMockito.mockStatic(ThreadSessionManager.class);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when((String) ThreadSessionManager.currentUserSession().getObject(Mockito.anyString()))
				.thenReturn("token");
		String url = "http://localhost/getaudittrail?fromDate=20190220&toDate=20200220&sortorder=DESC&companydomainname=hcl.com";
		Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(builder);
		Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
		AuditTrailResponseDTO auditTrailResponseDTO = getAuditTrailResponseDTO();
		Mockito.when(builder.get(AuditTrailResponseDTO.class)).thenReturn(auditTrailResponseDTO);
		adminCrudRestAPIImplInject.getAuditDetails("20190220", "20200220", "hcl.com");
	}

	@Test
	public void removeAuditDetailsTest() {
		adminCrudRestAPIImplInject.removeAuditDetails("AuditDetails_27032019_091104.004.zip");
	}
	
	@Test
	public void removeAuditDetailsElseBlckTest() {
		Mockito.when(file.exists()).thenReturn(false);
		adminCrudRestAPIImplInject.removeAuditDetails("AuditDetails_27032019_091104.004.zip");
	}

	/*
	 * @Test(expectedExceptions = {Exception.class}) public void
	 * getSystemSettingsNegativeTest() throws HMTPException {
	 * MockitoAnnotations.initMocks(this);
	 * Mockito.when(systemSettingsReadRepository.findAllSystemSettingsByType(1)).
	 * thenThrow(HMTPException.class); adminCrudRestAPIImpl.getSystemSettings();
	 * 
	 * }
	 */
	/*
	 * @Test(expectedExceptions = {HMTPException.class}) public void
	 * getLabLogoNagative() throws HMTPException {
	 * Mockito.when(systemSettingsReadRepository.getLabLogo(1L)).thenThrow(Exception
	 * .class); adminCrudRestAPIImpl.getLabLogo();
	 * 
	 * }
	 */

	@Test
	public void getSystemSettingsTest() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.when(systemSettingsReadRepository.findAllSystemSettingsByType(1)).thenReturn(getSystemSettings());
		Response responce = adminCrudRestAPIImplInject.getSystemSettings();
		AssertJUnit.assertEquals(responce.getStatus(), 200);
	}

	@Test
	public void getModulesTest() throws HMTPException {
		adminCrudRestAPIImplInject.getModules();
	}

	@Test
	public void getNotificationTemplatesTest() throws HMTPException {
		adminCrudRestAPIImplInject.getNotificationTemplates();
	}

	@Test
	public void getLabLogo() throws HMTPException {
		byte[] image = null;
		Mockito.when(systemSettingsReadRepository.getLabLogo(1L)).thenReturn(image);
		adminCrudRestAPIImplInject.getLabLogo();
	}

	public List<SystemSettings> getSystemSettings() {
		List<SystemSettings> reportSysSettings = new ArrayList<>();
		SystemSettings systemSettings = new SystemSettings();
		systemSettings.setAttributeName("jhgvdfkh");
		systemSettings.setActiveFlag("Y");
		systemSettings.setAttributeValue("jhfvjda");
		reportSysSettings.add(systemSettings);
		return reportSysSettings;
	}

	public MessageDto getMessageDto() {
		MessageDto messageDto = new MessageDto();
		String locale = "en-US";
		messageDto.setMessageGroup("group");
		List<String> content = new ArrayList<>();
		content.add("content1");
		messageDto.setContents(content);
		messageDto.setLocale(locale);
		return messageDto;

	}

	public MessageTemplate getMessageTemplateChannelUI() {
		MessageTemplate messageTemplate = new MessageTemplate();
		messageTemplate.setChannel("UI");
		return messageTemplate;
	}

	public MessageTemplate getMessageTemplateChannelEmail() {
		MessageTemplate messageTemplate = new MessageTemplate();
		messageTemplate.setChannel("EMAIL");
		return messageTemplate;
	}

	public com.roche.connect.adm.dto.Message getMessage() {
		com.roche.connect.adm.dto.Message message = new com.roche.connect.adm.dto.Message();
		message.setId(100L);
		return message;
	}

	public Message getMessageTemp() {
		Message messageTemp = new Message();
		messageTemp.setCreatedUser("");
		messageTemp.setViewedDateTime(Timestamp.from(Instant.now()));
		messageTemp.setId(100L);
		messageTemp.setViewed("Y");
		return messageTemp;
	}

	public AuditTrailResponseDTO getAuditTrailResponseDTO() {
		AuditTrailResponseDTO auditTrailResponseDTO = new AuditTrailResponseDTO();
		List<AuditTrailDetailDTO> lstAuditTrail = new ArrayList<>();
		lstAuditTrail.add(auditTrailDetailDTO);
		entityDTO.setLstAuditTrail(lstAuditTrail);
		auditTrailResponseDTO.setEntity(entityDTO);
		return auditTrailResponseDTO;

	}

	@Test
	public void saveSystemSettingsTest() throws Exception {
		PowerMockito.mockStatic(ThreadSessionManager.class);
		//boolean isLabDetailsValid = true;
		PowerMockito.mockStatic(UtilityService.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
		Mockito.doNothing().when(systemSettingsWriteRepository).deleteByDomainId(Mockito.anyLong());
		Mockito.when(systemSettingsWriteRepository.save(Mockito.any(SystemSettings.class))).thenReturn(systemSettings);
		Mockito.when(UtilityService.validatePhoneNumber(Mockito.anyString())).thenReturn(true);		
		Mockito.when(UtilityService.validateLabDetails(Mockito.anyList())).thenReturn(true);
		adminCrudRestAPIImplInject.saveSystemSettings("[{\"id\":11,\"attributeType\":\"type\",\"attributeName\":\"phoneNumber\",\"attributeValue\":\"value\"}]");
	}
	
	/*public List<SystemSettingsDto> systemSettingsDTO(){
		return sysSettingsForValidation;
		
	}*/
	
	/*@Test
	public void saveSystemSettingsValidatnNotEmptyTest() throws HMTPException {
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
		Mockito.doNothing().when(systemSettingsWriteRepository).deleteByDomainId(Mockito.anyLong());
		Mockito.when(systemSettingsWriteRepository.save(Mockito.any(SystemSettings.class))).thenReturn(systemSettings);
		Mockito.when(sysSettingsForValidation.isEmpty()).thenReturn(true);
		PowerMockito.mockStatic(UtilityService.class);
		Mockito.when(UtilityService.validatePhoneNumber(Mockito.anyString())).thenReturn(true);
		adminCrudRestAPIImplInject.saveSystemSettings(
				"[{\"id\":11,\"attributeType\":\"type\",\"attributeName\":\"phoneNumber\",\"attributeValue\":\"value\"}]");
	}*/

	@Test
	public void createReportParseExceptionTest() throws ParseException, Exception {
		String fromDate = "";
		String toDate = "";
		Mockito.when(utilityService.dateDurationCalculator(fromDate, toDate)).thenThrow(new java.text.ParseException(null, 0));
		Response response = adminCrudRestAPIImplInject.createReport(fromDate, toDate);
		AssertJUnit.assertEquals(response.getStatus(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}

	@Test
	public void createReportAbove21Test() throws ParseException, HMTPException {
		String fromDate = "2019-04-05 10:20:02";
		String toDate = "2019-04-27 10:20:02";
		Mockito.when(utilityService.dateDurationCalculator(fromDate, toDate)).thenReturn(22);
		Response response = adminCrudRestAPIImplInject.createReport(fromDate, toDate);
		AssertJUnit.assertEquals(response.getStatus(), HttpStatus.SC_BAD_REQUEST);
	}

	/*
	@Test
	public void createReportCreatedTest() throws Exception {
		String fromDate = "2019-04-10 10:20:02";
		String toDate = "2019-04-15 10:20:02";
		String problemReportPath = "";
		ProblemReport problemReport = new ProblemReport();
		MessageDto messageDto = getMessageDtoCrtRpt(fromDate, toDate);
		Mockito.when(utilityService.dateDurationCalculator(fromDate, toDate)).thenReturn(5);
		Mockito.doNothing().when(problemReportService).generateProblemReport(fromDate, toDate);
		Mockito.when(reportWriteRepository.save(problemReport)).thenReturn(problemReport);
		Mockito.when(problemReportServiceImp.getProblemReport(fromDate, toDate, problemReportPath)).thenReturn(problemReport);
		PowerMockito.mockStatic(ThreadSessionManager.class);
		PowerMockito.mockStatic(AdminCrudRestAPIImpl.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
		Mockito.when(messageTemplateReadRepository.findMessageTemplate(messageDto.getMessageGroup(),messageDto.getLocale(), 1L)).thenReturn(messageTemplates);
		// Mockito.when(iMessageService.constructMessage(messageDto,messageTemplates.get(0))).thenReturn(messages);
		PowerMockito.doNothing().when(adminCrudRestAPIImpl, "processUIChannel", messageTemplates.get(1), messageDto);
		PowerMockito.doNothing().when(adminCrudRestAPIImpl, "processEmailChannel", messageTemplates.get(1), messageDto);
		PowerMockito.doReturn(response).when(adminCrudRestAPIImpl, "processNotification", messageTemplates, messageDto);

		Mockito.when(adminCrudRestAPIImpl.addNotification(messageDto)).thenReturn(response);
		Response response = adminCrudRestAPIImplInject.createReport(fromDate, toDate);
		assertEquals(response.getStatus(), HttpStatus.SC_CREATED);
	}
	  
	@Test
	public void createReportGenReportTest() throws Exception {
		String fromDate = "2019-04-10 10:20:02";
		String toDate = "2019-04-15 10:20:02";
		ProblemReport problemReport = new ProblemReport();
		MessageDto messageDto = new MessageDto();
		Mockito.when(utilityService.dateDurationCalculator(fromDate, toDate)).thenReturn(5);
		Mockito.when(reportWriteRepository.save(problemReport)).thenReturn(problemReport);
		PowerMockito.mockStatic(AdminCrudRestAPIImpl.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class)); //
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession); //
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L); //
		Mockito.when(messageTemplateReadRepository.findMessageTemplate(messageDto.getMessageGroup(),messageDto.getLocale(), 1L)).thenReturn(messageTemplates);
		// PowerMockito.doNothing().when(AdminCrudRestAPIImpl.class,"processNotification",messageTemplates, messageDto);

		Mockito.when(adminCrudRestAPIImpl.addNotification(getMessageDto())).thenReturn(response);
		Response response = adminCrudRestAPIImplInject.createReport(fromDate, toDate);
		assertEquals(response.getStatus(), HttpStatus.SC_CREATED);
	}
	*/
	
	public MessageDto getMessageDtoCrtRpt(String fromDate, String toDate) {
		List<String> content = new ArrayList<>();
		content.add(fromDate);
		content.add(toDate);
		MessageDto messageDto = new MessageDto();
		messageDto.setContents(content);
		messageDto.setLocale(NotificationGroupType.LOCALE.getGroupType());
		messageDto.setMessageGroup(NotificationGroupType.PRBLM_RPRT_GEN.getGroupType());
		return messageDto;

	}
	 

}