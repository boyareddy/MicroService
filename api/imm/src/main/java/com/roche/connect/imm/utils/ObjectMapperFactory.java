package com.roche.connect.imm.utils;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.dpcr_analyzer.AcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.ESUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUMessage;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.common.mp96.AdaptorACKMessage;
import com.roche.connect.common.mp96.OULRunResultMessage;
import com.roche.connect.common.mp96.QueryMessage;

public class ObjectMapperFactory {

	private ObjectMapper objectMapper = new ObjectMapper();

	public static final String DEVICE_STR = "device";

	public static final String CONNECT_STR = "Connect";

	private <T extends Object> Class<T> readObjectClass(String source, String deviceType, String messageType) {

		if (StringUtils.isEmpty(deviceType) || StringUtils.isEmpty(source) || StringUtils.isEmpty(messageType)) {
			return null;
		}

		Class classObj = null;

		switch (deviceType) {

		case DeviceType.MP96:

			if (source.equals(DEVICE_STR) && messageType.equals(MessageType.MP96_QBP))
				classObj = QueryMessage.class;
			else if (source.equals(DEVICE_STR) && messageType.equals(MessageType.MP96_ACK))
				classObj = AdaptorACKMessage.class;
			else if (source.equals(DEVICE_STR) && messageType.equals(MessageType.MP96_OUL))
				classObj = OULRunResultMessage.class;
			break;

		case DeviceType.LP24:

			if (source.equals(DEVICE_STR) && messageType.equals(MessageType.LP24_QBP))
				classObj = com.roche.connect.common.lp24.QueryMessage.class;
			else if (source.equals(CONNECT_STR) && (messageType.equals(MessageType.LP24_RSP) || messageType.equals(EnumMessageType.ResponseMessage.toString())))
				classObj = com.roche.connect.common.lp24.ResponseMessage.class;
			else if (source.equals(DEVICE_STR) && messageType.equals(MessageType.LP24_U03))
				classObj = com.roche.connect.common.lp24.SpecimenStatusUpdateMessage.class;
			else if (source.equals(CONNECT_STR) && (messageType.equals(MessageType.LP24_ACK) || messageType.equals(EnumMessageType.AcknowledgementMessage.toString())))
				classObj = com.roche.connect.common.lp24.AcknowledgementMessage.class;
			break;

		case DeviceType.MP24:
			if (source.equals(DEVICE_STR) && (messageType.equals(MessageType.MP24_NAEXTRACTION)
					|| messageType.equals(MessageType.MP24_STATUS_UPDATE)))
				classObj = AdaptorRequestMessage.class;
			else if (source.equals(CONNECT_STR)
					&& (messageType.equals(MessageType.MP24_RSP) || messageType.equals(MessageType.MP24_ACK)))
				classObj = AdaptorResponseMessage.class;
			break;
			
		case DeviceType.DPCR_ANALYZER:
			if (source.equals(DEVICE_STR) && messageType.equals(MessageType.DPCR_ANALYZER_QBP))
				classObj = com.roche.connect.common.dpcr_analyzer.QueryMessage.class;
			else if (source.equals(DEVICE_STR) && messageType.equals(MessageType.DPCR_ANALYZER_ACK))
				classObj = AcknowledgementMessage.class;
			else if (source.equals(DEVICE_STR) && messageType.equals(MessageType.DPCR_ANALYZER_ESU))
				classObj = ESUMessage.class;
			else if (source.equals(DEVICE_STR) && messageType.equals(MessageType.DPCR_ANALYZER_ORU))
				classObj = ORUMessage.class;
			break;
		default:

		}

		return classObj;

	}

	public Object readObjectValue(String jsonString, String source, String deviceType, String messageType)
			throws IOException {

		if (StringUtils.isEmpty(jsonString))
			return null;

		Class<Object> objectClass = readObjectClass(source, deviceType, messageType);

		if (objectClass == null)
			return null;

		return objectClass.cast(objectMapper.readValue(jsonString, objectClass));

	}

}
