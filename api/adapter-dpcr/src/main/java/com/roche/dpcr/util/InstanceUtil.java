package com.roche.dpcr.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.roche.dpcr.dto.MessageExchange;

import ca.uhn.hl7v2.model.Message;

@Component
public class InstanceUtil {
	
	static InstanceUtil instanceUtil;
	
	private Map<String, MessageExchange> connectionMap = new HashMap<>();
	
	private Map<String, Message> messageMap = new HashMap<>();

	public synchronized Map<String, MessageExchange> getConnectionMap() {
		return connectionMap;
	}

	public void setConnectionMap(Map<String, MessageExchange> connectionMap) {
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
