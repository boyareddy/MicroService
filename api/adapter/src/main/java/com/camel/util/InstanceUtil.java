package com.camel.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.camel.dto.MessageContainerDTO;

import ca.uhn.hl7v2.model.Message;
@Component
public class InstanceUtil {

	static InstanceUtil instanceUtil;
    private Map<String, MessageContainerDTO> connectionMap = new HashMap<>();
    private Map<String, Message> messageMap = new HashMap<>();
    
	
    public synchronized Map<String, MessageContainerDTO> getConnectionMap() {
		return connectionMap;
	}

	public void setConnectionMap(Map<String, MessageContainerDTO> connectionMap) {
		this.connectionMap = connectionMap;
	}
	
	

	public synchronized Map<String, Message> getMessageMap() {
		return messageMap;
	}

	public void setMessageMap(Map<String, Message> messageMap) {
		this.messageMap = messageMap;
	}

	public synchronized static InstanceUtil getInstance() {
        if (InstanceUtil.instanceUtil == null) {
        	InstanceUtil.instanceUtil = new InstanceUtil();
        	synchronized(instanceUtil) {
        		InstanceUtil.instanceUtil.setConnectionMap(instanceUtil.getConnectionMap());
        		InstanceUtil.instanceUtil.setMessageMap(instanceUtil.getMessageMap());
            }
        }
        return InstanceUtil.instanceUtil;
    }
}
