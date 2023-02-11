package com.roche.connect.adm.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.emailservice.client.beans.EmailMessageBean;
import com.hcl.hmtp.emailservice.client.beans.EmailRecepientBean;
import com.hcl.hmtp.security.client.beans.RoleBean;
import com.hcl.hmtp.security.client.beans.SecurityUserBean;
import com.roche.connect.adm.config.SocketHandler;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.adm.dto.ToasterNotificationDTO;
import com.roche.connect.adm.model.Message;
import com.roche.connect.adm.model.MessageRecipient;
import com.roche.connect.adm.model.MessageTemplate;
import com.roche.connect.adm.model.Recipient;
import com.roche.connect.adm.util.UtilityService;

@Service
public class MessageServiceImpl implements IMessageService {

	@Autowired
	private UtilityService utilityService;

	@Value("${pas.senderName}")
	private String senderName;

	@Value("${pas.senderEmail}")
	private String senderEmail;

	@Value("${pas.replyTo}")
	private String replyTo;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Value("${pas.admin_api_url}")
	private String adminAPI;

	@Value("${pas.security_remote_url}")
	private String securityAPI;

	@Autowired
	@Qualifier("socketHandler")
	SocketHandler socketHandler;

	@Autowired
	SecurityServices securityServices;
	
	private static final String BROWNSTONEAUTHCOOKIE = "brownstoneauthcookie=";

	private static final String BROWNSTONE = "brownstoneauthcookie";

	private static final String COOKIE = "Cookie";

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	
	@Override
	public List<Message> constructMessage(MessageDto messageDto, MessageTemplate messageTemplate) throws HMTPException {
		try {
			List<Message> messages = null;
			if (messageTemplate.getMessageRecepients() != null && !messageTemplate.getMessageRecepients().isEmpty()) {
				messages = new ArrayList<>();
				processMessage(messageTemplate, messages, messageDto);
			}
			return messages;
		} catch (Exception e) {
			throw new HMTPException(e);
		}
	}

	private void processMessage(MessageTemplate messageTemplate, List<Message> messages, MessageDto messageDto) {
		constructMessageForRoles(messageTemplate, messages, messageDto);

	}

	private RoleBean getRoleBean(MessageRecipient messageRecipient) {
		Builder roleBuilder = null;
		for (Cookie cookie : httpServletRequest.getCookies()) {
			if (cookie.getName().equals(BROWNSTONE)) {
				roleBuilder = RestClientUtil
						.getBuilder(this.securityAPI + "/json/roles/roleId/" + messageRecipient.getRoleId(), null);
				roleBuilder.header(COOKIE, BROWNSTONEAUTHCOOKIE + cookie.getValue());
				return roleBuilder.get(new GenericType<RoleBean>() {
				});
			}
		}
		return null;

	}

	private void constructMessageForRoles(MessageTemplate messageTemplate, List<Message> messages,
			MessageDto messageDto) {
		Message message = new Message();
		message.setTitle(utilityService.getConstructedMessage(messageDto.getContents(), messageTemplate.getTitle()));
		message.setDescription(
				utilityService.getConstructedMessage(messageDto.getContents(), messageTemplate.getDescription()));
		message.setMessageTemplate(messageTemplate);
		message.setCreatedUser(ThreadSessionManager.currentUserSession().getAccessorUserName());
		message.setCreatedDateTime(Timestamp.from(Instant.now()));
		message.setViewed("N");
		if ((messageDto.getSeverity() == null) || (messageDto.getSeverity().isEmpty()))
			message.setSeverity(messageTemplate.getSeverity());
		else
			message.setSeverity(messageDto.getSeverity());
		message.setModule(messageTemplate.getModule());
		List<Recipient> recipients = new ArrayList<>();
		for (MessageRecipient messageRecipient : messageTemplate.getMessageRecepients()) {
			// Roles
			RoleBean roleBean = null;
			roleBean = getRoleBean(messageRecipient);
			if (roleBean != null && roleBean.getUsers() != null && !roleBean.getUsers().isEmpty()) {
				addMessageToList(roleBean.getUsers(), recipients);
			}
		}
		message.setRecipient(recipients);
		messages.add(message);

	}

	private void addMessageToList(Set<SecurityUserBean> securityUserBeans, List<Recipient> recipients) {
		if (securityUserBeans != null && !securityUserBeans.isEmpty()) {
			for (SecurityUserBean securityUserBean : securityUserBeans) {
				if (!securityUserBean.isRetired()) {
					Recipient recipient = new Recipient();
					recipient.setCreatedUser(ThreadSessionManager.currentUserSession().getAccessorUserName());
					recipient.setCreatedDateTime(Timestamp.from(Instant.now()));
					recipient.setLoginName(securityUserBean.getLoginName());
					recipients.add(recipient);
				}
			}
		}

	}

	@Override
	public EmailMessageBean constructEmail(MessageDto messageDto, MessageTemplate messageTemplate)
			throws HMTPException {
		try {
			EmailMessageBean emailMessageBean = null;
			if (messageTemplate.getMessageRecepients() != null && !messageTemplate.getMessageRecepients().isEmpty()) {
				emailMessageBean = new EmailMessageBean();
				emailMessageBean.setSubject(
						utilityService.getConstructedMessage(messageDto.getContents(), messageTemplate.getTitle()));
				emailMessageBean.setPriority(messageTemplate.getPriority());
				emailMessageBean.setStatus(EmailMessageBean.NEW);
				emailMessageBean.setSenderName(senderName);
				emailMessageBean.setSenderEmail(senderEmail);
				emailMessageBean.setReplyTo(replyTo);

				// Email Recipients
				Set<EmailRecepientBean> emailRecepientBeans = new HashSet<>();
				for (MessageRecipient messageRecipient : messageTemplate.getMessageRecepients()) {
					// Roles
					RoleBean roleBean = null;
					roleBean = getRoleBean(messageRecipient);
					constructEmailForRoles(roleBean, messageRecipient, messageDto, emailMessageBean, messageTemplate,
							emailRecepientBeans);

				}
				emailMessageBean.setEmailRecepients(emailRecepientBeans);
			}
			return emailMessageBean;
		} catch (Exception e) {
			throw new HMTPException(e);
		}
	}

	private void constructEmailForRoles(RoleBean roleBean, MessageRecipient messageRecipient, MessageDto messageDto,
			EmailMessageBean emailMessageBean, MessageTemplate messageTemplate,
			Set<EmailRecepientBean> emailRecepientBeans) {
		if (roleBean != null && roleBean.getUsers() != null && !roleBean.getUsers().isEmpty()) {
			for (SecurityUserBean securityUserBean : roleBean.getUsers()) {
				EmailRecepientBean emailRecepientBean = new EmailRecepientBean();

				emailRecepientBean.setType(messageRecipient.getType());
				addEmailToList(securityUserBean, messageRecipient, messageDto, emailRecepientBean, emailRecepientBeans,
						emailMessageBean, messageTemplate);
			}
		}

	}

	private void addEmailToList(SecurityUserBean securityUserBean, MessageRecipient messageRecipient,
			MessageDto messageDto, EmailRecepientBean emailRecepientBean, Set<EmailRecepientBean> emailRecepientBeans,
			EmailMessageBean emailMessageBean, MessageTemplate messageTemplate) {
		if (securityUserBean != null) {
			if (messageRecipient.getType() == 1) {
				messageDto.getContents().add("User");
				emailMessageBean.setHtmlBody(utilityService.getConstructedMessage(messageDto.getContents(),
						messageTemplate.getDescription()));
			}
			emailRecepientBean.setName(securityUserBean.getLoginName());
			emailRecepientBean.setEmail(securityUserBean.getEmail());
			if (!securityUserBean.isRetired()) {
				emailRecepientBeans.add(emailRecepientBean);
			}
		}

	}

	public void sendNotificationToaster(Message message, Set<Integer> roleIds) {

		ToasterNotificationDTO toasterNotificationDTO = new ToasterNotificationDTO();

		toasterNotificationDTO.setMsg(message.getDescription());
		toasterNotificationDTO.setSeverity(message.getSeverity());
		toasterNotificationDTO.setTitle(message.getTitle());
		toasterNotificationDTO.setTopic(message.getModule());
		toasterNotificationDTO.setCreatedDateTime(message.getCreatedDateTime());
		toasterNotificationDTO.setId(message.getId());
		
		Set<String> roleName;
		try {
			roleName = securityServices.getRoles(roleIds);
			ObjectMapper mapper = new ObjectMapper();
			socketHandler.sendMessage(mapper.writeValueAsString(toasterNotificationDTO), roleName);

		} catch (HMTPException | JsonProcessingException e) {
			logger.info("Exception on sendNotificationToaster: " + e.getMessage());
		}

	}

	@Override
	public void sendNotificationToaster(ToasterNotificationDTO toasterNotificationDTO, Set<Integer> roleIds) {
		
		Set<String> roleName;
		try {
			roleName = securityServices.getRoles(roleIds);
			ObjectMapper mapper = new ObjectMapper();
			socketHandler.sendMessage(mapper.writeValueAsString(toasterNotificationDTO), roleName);

		} catch (HMTPException | JsonProcessingException e) {
			logger.info("Exception on sendNotificationToaster: " + e.getMessage());
		}
	}

}
