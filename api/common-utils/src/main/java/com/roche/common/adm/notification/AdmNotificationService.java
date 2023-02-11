package com.roche.common.adm.notification;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.common.constant.NotificationGroupType;

public class AdmNotificationService {
	
	private static HMTPLogger logger = HMTPLoggerFactory.getLogger(AdmNotificationService.class.getName());
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	private AdmNotificationService() {
		
	}
	
	public static void sendNotification(NotificationGroupType messageGroup, List<String> contents, String token, String url) throws HMTPException {
		try {
			MessageDto messagedto = new MessageDto();
			messagedto.setContents(contents);
			messagedto.setMessageGroup(messageGroup.getGroupType());
			messagedto.setLocale(NotificationGroupType.LOCALE.getGroupType());
			logger.info(url +" :sending notification to adm for :",objectMapper.writeValueAsString(messagedto));
			RestClient.post(url , messagedto, token, null);
			logger.info("Notification sent successfully for"+messageGroup.getGroupType());
		} catch (Exception e) {
			throw new HMTPException(e);
		}

	}
	
	public static void sendNotification(NotificationGroupType messageGroup, List<String> contents,String severity, String token, String url) throws HMTPException {
        try {
            MessageDto messagedto = new MessageDto();
            messagedto.setContents(contents);
            messagedto.setMessageGroup(messageGroup.getGroupType());
            messagedto.setLocale(NotificationGroupType.LOCALE.getGroupType());
            messagedto.setSeverity(severity);
            logger.info(url +" :sending notification to adm for :",objectMapper.writeValueAsString(messagedto));
            RestClient.post(url , messagedto, token, null);
            logger.info("Notification sent successfully for"+messageGroup.getGroupType());
        } catch (Exception e) {
            throw new HMTPException(e);
        }

    }

}
