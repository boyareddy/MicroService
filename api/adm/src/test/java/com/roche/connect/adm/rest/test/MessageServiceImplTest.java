package com.roche.connect.adm.rest.test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.owasp.esapi.waf.actions.DoNothingAction;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.security.client.beans.RoleBean;
import com.roche.connect.adm.config.SocketHandler;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.adm.dto.ToasterNotificationDTO;
import com.roche.connect.adm.model.Message;
import com.roche.connect.adm.model.MessageRecipient;
import com.roche.connect.adm.model.MessageTemplate;
import com.roche.connect.adm.service.MessageServiceImpl;
import com.roche.connect.adm.service.SecurityServicesImpl;
import com.roche.connect.adm.util.UtilityService;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, ThreadSessionManager.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class MessageServiceImplTest {
	@InjectMocks
	MessageServiceImpl messageServiceImpl;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	HttpServletRequest httpServletRequest;
	@Mock
	UtilityService utilityService;

	@Mock
	private SimpMessagingTemplate template;
	
	@Mock
	SecurityServicesImpl securityServices;
	
	@Mock
	SocketHandler socketHandler;

	Cookie[] cookie = { new Cookie("brownstoneauthcookie", "cookie") };
	
	@Mock Cookie cookies;

	List<MessageRecipient> messageRecepientsList = new ArrayList<>();

	Message message = null;
	Set<Integer> rolesIds =new HashSet();
	Set<String> roleName = new HashSet();

	@BeforeTest
	public void setUp() {
		messageRecepientsList.add(getMessageRecepient());
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(httpServletRequest.getCookies()).thenReturn(cookie);
		Mockito.when(utilityService.getConstructedMessage(getMessageDto().getContents(),
				getMessageTemplateChannelEmail().getTitle())).thenReturn("email subject");
		ReflectionTestUtils.setField(messageServiceImpl, "senderName", "Connect Admin");
		ReflectionTestUtils.setField(messageServiceImpl, "senderEmail", "mailer@rconnect.com");
		ReflectionTestUtils.setField(messageServiceImpl, "replyTo", "mailer@rconnect.com");
		rolesIds.add(1);
		roleName.add("admin");
		message = new Message();
		message.setDescription("Description");
		message.setSeverity("Error");
		message.setTitle("Title");
		message.setModule("Order");
		message.setCreatedDateTime(Timestamp.from(Instant.now()));
		message.setId(1L);

/*		Mockito.doNothing().when(template).convertAndSend(Mockito.anyString(),
				Mockito.any(ToasterNotificationDTO.class));*/
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

	
	@Mock Invocation.Builder roleBuilder;
	@Mock RoleBean roleBean;
	
	@Test
	public void constructMessageTest() throws HMTPException {
		try{
		Mockito.when(utilityService.getConstructedMessage(Mockito.anyList(),Mockito.anyString())).thenReturn("title");
		Mockito.when(httpServletRequest.getCookies()).thenReturn(cookie);
		Mockito.when(cookies.getName()).thenReturn("brownstoneauthcookie");
		PowerMockito.mockStatic(RestClientUtil.class);
		ReflectionTestUtils.setField(messageServiceImpl, "securityAPI", "http://localhost");
		String securityAPI ="http://localhost";
		Mockito.when(RestClientUtil
						.getBuilder(securityAPI  + "/json/roles/roleId/" + 1, null)).thenReturn(roleBuilder);
		
		Mockito.when(roleBuilder.header("Cookie", "brownstoneauthcookie" + "value")).thenReturn(roleBuilder);
		Mockito.when(roleBuilder.get(new GenericType<RoleBean>() {
				})).thenReturn(roleBean);
		messageServiceImpl.constructMessage(getMessageDto(), getMessageTemplateChannelEmail());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}

	@Test
	public void constructEmailTest() throws HMTPException {
		try{
			messageServiceImpl.constructEmail(getMessageDto(), getMessageTemplateChannelEmail());
		}catch(Exception e){
			e.getMessage();
		}
		
	}

	public MessageTemplate getMessageTemplateChannelEmail() {
		MessageTemplate messageTemplate = new MessageTemplate();
		messageTemplate.setChannel("EMAIL");
		messageTemplate.setMessageRecepients(messageRecepientsList);
		messageTemplate.setTitle("email");
		messageTemplate.setPriority(1);
		return messageTemplate;
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

	MessageRecipient getMessageRecepient() {
		MessageRecipient messageRecipient = new MessageRecipient();
		messageRecipient.setCreatedBy("admin");
		messageRecipient.setId(100L);
		messageRecipient.setUpdatedBy("admin");
		return messageRecipient;

	}

	@Test
	public void sendNotificationTest() {
		Mockito.doNothing().when(socketHandler).sendMessage(Mockito.anyString(), Mockito.anySet());
		try {
			Mockito.when(securityServices.getRoles(rolesIds)).thenReturn(roleName);
			messageServiceImpl.sendNotificationToaster(message, rolesIds);
		} catch (HMTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
