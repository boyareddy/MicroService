package com.roche.nipt.dpcr.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.roche.nipt.dpcr.dto.MessageExchangeDTO;

import ca.uhn.hl7v2.model.Message;

@Component
public class InstanceUtil {
	
	static InstanceUtil instanceUtil;
	
	private Map<String, MessageExchangeDTO> connectionMap = new HashMap<>();
	
	private Map<String, Message> messageMap = new HashMap<>();

	public synchronized Map<String, MessageExchangeDTO> getConnectionMap() {
		return connectionMap;
	}

	public void setConnectionMap(Map<String, MessageExchangeDTO> connectionMap) {
		synchronized (this) {
			this.connectionMap = connectionMap;
		}

	}

	public synchronized Map<String, Message> getMessageMap() {
		return messageMap;
	}

	public void setMessageMap(Map<String, Message> messageMap) {
		synchronized (this) {
			this.messageMap = messageMap;
		}
	}

	public static synchronized InstanceUtil getInstance() {
		if (InstanceUtil.instanceUtil == null) {
			InstanceUtil.instanceUtil = new InstanceUtil();
			synchronized (instanceUtil) {
				InstanceUtil.instanceUtil.setConnectionMap(instanceUtil.getConnectionMap());
				InstanceUtil.instanceUtil.setMessageMap(instanceUtil.getMessageMap());
			}
		}
		return InstanceUtil.instanceUtil;
	}

}
