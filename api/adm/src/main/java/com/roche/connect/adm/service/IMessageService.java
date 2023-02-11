package com.roche.connect.adm.service;

import java.util.List;
import java.util.Set;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.emailservice.client.beans.EmailMessageBean;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.adm.dto.ToasterNotificationDTO;
import com.roche.connect.adm.model.Message;
import com.roche.connect.adm.model.MessageTemplate;

public interface IMessageService {
	
	public List<Message> constructMessage(MessageDto messageDto,MessageTemplate messageTemplate) throws HMTPException; 
	
	public EmailMessageBean constructEmail(MessageDto messageDto,MessageTemplate messageTemplate) throws HMTPException;

	public void sendNotificationToaster(Message message, Set<Integer> rolesIds);

	public void sendNotificationToaster(ToasterNotificationDTO message, Set<Integer> roleIds);
}
