package com.roche.connect.imm.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.common.error.ErrorCode;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.prop.ErrorCodeService;
import com.roche.connect.imm.utils.IMMConstants;
import com.roche.connect.imm.utils.ObjectMapperFactory;
import com.roche.connect.imm.utils.RestClient;
import com.roche.connect.imm.utils.UrlConstants;


@Component
public class LP24AsyncMessageService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Value("${connect.runID}")
	private String runIDPrefix;

	@Value("${connect.wfm_host_url}")
	private String wfmHostUrl;

	@Value("${connect.mp96_adaptor_host_url}")
	private String mp96AdaptorHostUrl;
	
	@Value("${connect.lp24_adaptor_host_url}")
	private String lp24AdaptorHostUrl;
	
	@Value("${connect.adm_host_url}")
    private String admHostUrl;
	
	@Autowired
	private WFMIntegrationService wfmIntegrationService;
	
	
	@Async
    public void performAsyncLP24LApplicationRejectMessage(com.roche.connect.common.lp24.QueryMessage queryMessage,String token) {
        
		try {

			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setStatus("NOORDER");
			responseMessage.setErrors(new ArrayList<String>());
			responseMessage.setSendingApplicationName("CONNECT"); 
			responseMessage.setReceivingApplication(queryMessage.getSendingApplicationName());
			responseMessage.setMessageType(EnumMessageType.ResponseMessage);
			responseMessage.setContainerId(queryMessage.getContainerId());
			responseMessage.setDeviceSerialNumber(queryMessage.getDeviceSerialNumber());
			responseMessage.setOrderControl("DC");
			responseMessage.setMessageControlId(queryMessage.getMessageControlId());
			SimpleDateFormat formatter = new SimpleDateFormat(IMMConstants.ADAPTOR_SIMPLE_DATE_FORMAT_STR);
			responseMessage.setDateTimeMessageGenerated(formatter.format(new Date()));

			List<String> errorMessages = new LinkedList<>();
			errorMessages.add(queryMessage.getContainerId());
			errorMessages.add(queryMessage.getDeviceSerialNumber());
			sendNoOrderFoundNotification(errorMessages, token);

			RestClient.post(lp24AdaptorHostUrl + UrlConstants.LP24_ADAPTOR_RSP_URL, responseMessage, token, null);
		} catch (Exception e) {
            logger.error("Error while performing LP24LApplicationRejectMessage AR Message in Asynchronous way" + e.getMessage(), e);
        }
        
    }
	
	@Async
	public void performLP24SSUFailureMessage(SpecimenStatusUpdateMessage specimenStatusUpdateMessage, String token) {

		try {
			AcknowledgementMessage acknowledgementMessage = new AcknowledgementMessage();
			acknowledgementMessage.setMessageControlId(specimenStatusUpdateMessage.getMessageControlId());
			acknowledgementMessage.setStatus("AR");
			acknowledgementMessage.setDeviceSerialNumber(specimenStatusUpdateMessage.getDeviceSerialNumber());
			acknowledgementMessage.setSendingApplicationName(ObjectMapperFactory.CONNECT_STR);
			acknowledgementMessage.setReceivingApplication(specimenStatusUpdateMessage.getSendingApplicationName());
			acknowledgementMessage.setDateTimeMessageGenerated(new Date().toString());
			acknowledgementMessage.setMessageType(EnumMessageType.AcknowledgementMessage);
			acknowledgementMessage.setContainerId(specimenStatusUpdateMessage.getContainerId());
			acknowledgementMessage.setErrorCode(ErrorCode.REQUIRED_FIELD_MISSING);
			acknowledgementMessage.setErrorDescription(ErrorCodeService.getInstance().get(ErrorCode.REQUIRED_FIELD_MISSING));

			String url = lp24AdaptorHostUrl + UrlConstants.LP24_ADAPTOR_OUL_ACK_URL;
			wfmIntegrationService.genericMessagePoster(acknowledgementMessage, url, token);
			
			List<String> errorMessages = new LinkedList<>();
			errorMessages.add(specimenStatusUpdateMessage.getContainerId());
			errorMessages.add(specimenStatusUpdateMessage.getDeviceSerialNumber());
			sendNoOrderFoundNotification(errorMessages, token);
			
		} catch (Exception e) {
			logger.error("Error while performing LP24 SSU AR Message in Asynchronous way" + e.getMessage(), e);
		}
	}

	private void sendNoOrderFoundNotification(List<String> errorMessages, String token) {
		try {
			AdmNotificationService.sendNotification(NotificationGroupType.NO_ORDER_FOUND_LP24, errorMessages, token,
					admHostUrl + UrlConstants.ADM_NOTIFICATION_URL);
			logger.info("Notification sent successfully! from IMM side");
		} catch (HMTPException e) {
			logger.error("Failed to send notification" + e.getMessage(), e);
		}

	}
	
	@Async
	public void processAsyncMessage(Object object, String url, String token) {

		try {
			wfmIntegrationService.genericMessagePoster(object, url, token);
		} catch (IOException e) {
			logger.error("Failed to process Generic Message", e);
		}
	}

}
