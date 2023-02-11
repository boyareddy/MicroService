/*******************************************************************************
 * MessageService.java                  
 *  Version:  1.0
 * 
 *  Authors:  Alexander
 * 
 * *********************
 *  Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  All Rights Reserved.
 * 
 *  NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 *  The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
 *                
 * *********************
 *  ChangeLog:
 * 
 *  Alexanders@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.imm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.htp.status.HtpStatus;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.imm.model.MessageStore;
import com.roche.connect.imm.utils.MessageType;
import com.roche.connect.imm.writerepository.MessageStoreWriteRepository;

/**
 * The Class MessageService.
 *
 * @author gosula.r
 */
@Service
public class MessageService {

	/** The message store write repository. */
	@Autowired
	private MessageStoreWriteRepository messageStoreWriteRepository;

	/** The object mapper. */
	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Save message.
	 *
	 * @param responseMessage the response message
	 * @throws JsonProcessingException the json processing exception
	 */
	public void saveMessage(ResponseMessage responseMessage) throws JsonProcessingException {
		String messageJson = objectMapper.writeValueAsString(responseMessage);

		MessageStore msg = new MessageStore();
		msg.setMessage(messageJson);
		msg.setDeviceID(responseMessage.getDeviceSerialNumber());
		msg.setMessageType(responseMessage.getMessageType().toString());

		messageStoreWriteRepository.save(msg);

	}

	/**
	 * Save message.
	 *
	 * @param runResultDTO the run result DTO
	 * @throws JsonProcessingException the json processing exception
	 */
	public void saveMessage(RunResultsDTO runResultDTO) throws JsonProcessingException {

		MessageStore msg = new MessageStore();
		msg.setMessage(objectMapper.writeValueAsString(runResultDTO));
		msg.setDeviceID(runResultDTO.getDeviceId());
		msg.setMessageType(MessageType.HTP_RUN);

		messageStoreWriteRepository.save(msg);

	}
	
	/**
	 * Save message.
	 *
	 * @param htpStatus the htp status
	 * @throws JsonProcessingException the json processing exception
	 */
	public void saveMessage(HtpStatus htpStatus) throws JsonProcessingException {

		MessageStore msg = new MessageStore();
		msg.setMessage(objectMapper.writeValueAsString(htpStatus));
		msg.setDeviceID(htpStatus.getDeviceId());
		msg.setMessageType(MessageType.HTP_RUN_STATUS);

		messageStoreWriteRepository.save(msg);

	}
	
	public MessageStore saveMessage(Object object, String messageType, String deviceId) throws JsonProcessingException {

		MessageStore msg = new MessageStore();
		msg.setMessage(objectMapper.writeValueAsString(object));
		msg.setDeviceID(deviceId);
		msg.setMessageType(messageType);

		return messageStoreWriteRepository.save(msg);

	}
}
