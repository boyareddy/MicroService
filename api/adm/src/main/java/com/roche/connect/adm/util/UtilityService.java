package com.roche.connect.adm.util;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.adm.dto.SystemSettingsDto;
import com.roche.connect.adm.model.LabDetails;
import com.roche.connect.adm.model.Message;
import com.roche.connect.adm.model.MessageRecipient;
import com.roche.connect.adm.model.MessageTemplate;

@Service
public class UtilityService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	
	private static String validatePhoneNumber = "^[a-zA-Z 0-9()+-]*$";
	private static final String IMAGE_PATTERN = ".+\\.(?i:jpg|png|jpeg)";
    private static final long IMAGE_SIZE =1048576L;
    
	public String getConstructedMessage(List<String> stringConent, String template) {
		String message = template;
		if (stringConent != null && !stringConent.isEmpty()) {
			List<Object> objects = new ArrayList<>();
			for (String messageContent : stringConent) {
				objects.add(messageContent);
			}
			MessageFormat messageFormat = new MessageFormat(template);
			message = messageFormat.format(objects.toArray());
		}
		return message;
	}

	public List<com.roche.connect.adm.dto.MessageTemplate> mapTemplateToDto(List<MessageTemplate> messageTemplates) {
		List<com.roche.connect.adm.dto.MessageTemplate> messageTemplateDtos = null;
		if (messageTemplates != null && !messageTemplates.isEmpty()) {
			messageTemplateDtos = new ArrayList<>();
			for (MessageTemplate messageTemplate : messageTemplates) {
				com.roche.connect.adm.dto.MessageTemplate messageTemplateDto = new com.roche.connect.adm.dto.MessageTemplate();
				messageTemplateDto.setId(messageTemplate.getId());
				messageTemplateDto.setActiveFlag(messageTemplate.getActiveFlag());
				messageTemplateDto.setChannel(messageTemplate.getChannel());
				messageTemplateDto.setMessageGroup(messageTemplate.getMessageGroup());
				messageTemplateDto.setTitle(messageTemplate.getTitle());
				messageTemplateDto.setDescription(messageTemplate.getDescription());
				messageTemplateDto.setLocale(messageTemplate.getLocale());
				messageTemplateDto.setSeverity(messageTemplate.getSeverity());
				messageTemplateDto.setPriority(messageTemplate.getPriority());

				List<com.roche.connect.adm.dto.MessageRecepient> emailRecepientBeans = null;
				if (messageTemplate.getMessageRecepients() != null
						&& !messageTemplate.getMessageRecepients().isEmpty()) {
					emailRecepientBeans = new ArrayList<>();
					for (MessageRecipient messageRecipient : messageTemplate.getMessageRecepients()) {
						com.roche.connect.adm.dto.MessageRecepient emailRecepientBean = new com.roche.connect.adm.dto.MessageRecepient();
						emailRecepientBean.setId(messageRecipient.getId());
						emailRecepientBean.setRoleId(messageRecipient.getRoleId());
						emailRecepientBean.setType(messageRecipient.getType());
						emailRecepientBeans.add(emailRecepientBean);
					}
				}
				messageTemplateDto.setMessageRecepients(emailRecepientBeans);
				messageTemplateDtos.add(messageTemplateDto);
			}
		}
		return messageTemplateDtos;
	}

	public List<com.roche.connect.adm.dto.Message> mapMessageToDto(List<Message> messages) {
		List<com.roche.connect.adm.dto.Message> messageDtos = null;
		if(messages != null && !messages.isEmpty()) {
			messageDtos = new ArrayList<>();
			for (Message message : messages) {
				com.roche.connect.adm.dto.Message messageDto = new com.roche.connect.adm.dto.Message();
				messageDto.setId(message.getId());
				messageDto.setMsg(message.getDescription());
				messageDto.setTitle(message.getTitle());
				messageDto.setSeverity(message.getSeverity());
				messageDto.setTopic(message.getModule());
				messageDto.setViewed(message.getViewed());
				messageDto.setCreatedDateTime(message.getCreatedDateTime());
				messageDto.setViewedDateTime(message.getViewedDateTime());
				messageDtos.add(messageDto);
			}
		}
		return messageDtos;
	}
	
	// Date duration Calculator between two dates - RC#12381
		public int dateDurationCalculator(String dateStart, String dateStop) throws ParseException {
			long duration ;
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = format.parse(dateStart);
			Date d2 = format.parse(dateStop);
			// in milliseconds
			long diff = d2.getTime() - d1.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			duration = diffDays;
			logger.info(diffDays + " days, ");
			return (int)duration;
		}
	
	public static boolean validatePhoneNumber(String phone) {
		boolean valid = false;

		valid = Pattern.matches(validatePhoneNumber , phone);
		return valid;
	}
	
	public static boolean validateLogoSizeAndFormat(MultipartFile file) {   
        boolean valid = false;
        String filename =file.getOriginalFilename();
        long  size =file.getSize();
        if(!file.isEmpty()&&Pattern.matches(IMAGE_PATTERN ,filename)&&size<=IMAGE_SIZE) {
            valid =true;
        } 
        return valid;
      }
	
	public static boolean validateLabDetails(List<SystemSettingsDto> systemSettingsAttributesList){
		boolean isLabDetailsValid = true;
		for(SystemSettingsDto systemSettingsDto:systemSettingsAttributesList){			
			if((systemSettingsDto.getAttributeName().equalsIgnoreCase(ADMConstants.SystemSettings.LAB_ADDRESS_FIELD1.toString())
				|| systemSettingsDto.getAttributeName().equalsIgnoreCase(ADMConstants.SystemSettings.LAB_ADDRESS_FIELD2.toString())
				|| systemSettingsDto.getAttributeName().equalsIgnoreCase(ADMConstants.SystemSettings.LAB_ADDRESS_FIELD3.toString()))
			    && systemSettingsDto.getAttributeValue().length()>ADMConstants.VALID_ADDRESS_FIELD_LENGTH.toInteger()){
				isLabDetailsValid = false;
				break;
			}
		}
		return isLabDetailsValid;
	}
	
	public static boolean validateLabDetails(LabDetails labDetails){
		boolean isLabDetailsValid = false;
		if(labDetails.getLabAddress1().length()<=ADMConstants.VALID_ADDRESS_FIELD_LENGTH.toInteger()
				&& labDetails.getLabAddress2().length()<=ADMConstants.VALID_ADDRESS_FIELD_LENGTH.toInteger()
				&& labDetails.getLabAddress3().length()<=ADMConstants.VALID_ADDRESS_FIELD_LENGTH.toInteger()){
			isLabDetailsValid = true;
		}
		return isLabDetailsValid;
	}

}
