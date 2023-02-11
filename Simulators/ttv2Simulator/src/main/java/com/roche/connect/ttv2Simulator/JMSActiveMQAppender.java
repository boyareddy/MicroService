/**
 * ============================================================================
 * ============ COPYRIGHT NOTICE
 * ================================================
 * ======================================== Copyright (C) 2018, HCL Technologies
 * Limited. All Rights Reserved. Proprietary and confidential. All information
 * contained herein is, and remains the property of HCL Technologies Limited.
 * Copying or reproducing the contents of this file, via any medium is strictly
 * prohibited unless prior written permission is obtained from HCL Technologies
 * Limited.
 */

package com.roche.connect.ttv2Simulator;

import java.io.IOException;


import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.DeliveryMode;

import org.apache.activemq.ActiveMQConnectionFactory;

import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.service.AuditTrailEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.regex.Pattern;

import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

@Plugin(name = "JMSActiveMQ", elementType = "appender", category = "Core", printObject = true)
public class JMSActiveMQAppender extends AbstractAppender   {

	final private String DESTINATIONTYPE_QUEUE = "queue";
	final private String DESTINATIONTYPE_TOPIC = "topic";
		
	private String regFilter = null;
	private String userName = null;
	private String password = null;
	private boolean durable = true;
	private String broker_URL;
    
	
    ConnectionFactory connectionFactory = null;
	MessageProducer messageProducer = null;
	Connection connection = null;
	Session session = null;
	String destinationName=null;
	String destinationType=null;
	boolean initialized=false;
	Queue queue = null;
	Topic topic = null;
				
	JMSActiveMQAppender(final String name,final Filter filter,final Layout<? extends Serializable> layout,
            final boolean ignoreExceptions, final String destinationName,final String destinationType, final String brokerURL, final String userName, final String password,final String regFilter,final boolean durable){
		
		super(name,filter,layout,ignoreExceptions);
						
		this.regFilter = regFilter;
		this.userName = userName;
		this.password = password;
		this.durable = durable;
		this.destinationName = destinationName;
		this.destinationType = destinationType;
		this.broker_URL = brokerURL;
				
		try{
	        if(destinationName == null || destinationName.isEmpty()){
	        	throw new Exception("Exchange name needs to be specified");
	        }
	        	        
	        if(destinationType == null || destinationType.isEmpty()){
	        	throw new Exception("Exchange type needs to be specified");
	        }else if(!(destinationType.equals(DESTINATIONTYPE_QUEUE) || destinationType.equals(DESTINATIONTYPE_TOPIC)
	        		)){
	        	throw new Exception("Invalid Exchange type:"+destinationType+" Exchange type should be one of the following: "
	        			+DESTINATIONTYPE_QUEUE+","+DESTINATIONTYPE_TOPIC);
	        }
	       	     	
	        
        	connectionFactory = new ActiveMQConnectionFactory(broker_URL);
			connection = connectionFactory.createConnection(userName,password);
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
								
			if(destinationType.equalsIgnoreCase(DESTINATIONTYPE_QUEUE)){
				queue =  session.createQueue(destinationName);
				messageProducer = session.createProducer(queue);
			}else{
				topic = session.createTopic(destinationName);
				messageProducer = session.createProducer(topic);
			}
										
			messageProducer.setTimeToLive(0);
						
			if(durable){			
				messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			}
			
	        initialized=true;
	        
	        System.out.println("Central Messaging Queue: JMSActiveMQAppender: Started sending logs to central messaging queue.");
		}catch(IOException ioex){
			System.out.println("Central Messaging Queue: JMSActiveMQAppender: FAILED to start."+ioex.getMessage());
		}catch(Exception ex){
			System.out.println("Central Messaging Queue: JMSActiveMQAppender: FAILED to start."+ex.getMessage());
		}
	}
		
	@PluginFactory
    public static JMSActiveMQAppender createAppender(
    		@PluginAttribute(value = "name") String name,
    		@PluginElement("Filter") Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) Boolean ignoreExceptions,
            @PluginAttribute("destinationName") final String destinationName,
            @PluginAttribute("destinationType") final String destinationType,
            @PluginAttribute("brokerURL") final String brokerURL,
            @PluginAttribute("userName") final String userName,
            @PluginAttribute("password") final String password,
            @PluginAttribute("regFilter") final String regFilter,
            @PluginAttribute(value = "durable", defaultBoolean = true) final Boolean durable
            ) {
	
            return new JMSActiveMQAppender(name,filter,layout,ignoreExceptions,destinationName,destinationType,brokerURL,userName, password,regFilter,durable);
    }
			
	@Override
	public ErrorHandler getHandler(){
		return null;
	}
			
	@Override
	public synchronized void append(LogEvent event) {
	    try {

	    	
	    	//Check if writing to RabbitMQ is initialized   
	    	if(!initialized){
	    		return;
	    	}
	    	
	    	String msg = event.toString();
	    	
	    	//Processing for JAXRSResponseFilter and REST interceptor filter
	    	String messageCode="";
	    	String httpOperation = null;
	        String fullURL = null;
	        String params = null;
	        String objectnewvalue=null;
	        String codedRoutingKey = "";
	        String timeTaken = null;
	        String ipAddress = null;
	        
	        if(msg.contains("JAXRSResponseFilter") || msg.contains("RESTInterceptor")){
	        	
	        	messageCode = "AccessLog";
	        		        	
	        	int index = -1;
	        	String[] httpOperations = new String[]{"GET","PUT","POST","DELETE"}; 
	        	for(String operation : httpOperations){
	        		if(msg.contains(operation)){
		        		index = msg.indexOf(operation);
		        		break;
		        	}
	        	}
	        	String httpInfo = msg.substring(index);
	    		Scanner httpScanner = new Scanner(httpInfo);
	    		httpOperation = httpScanner.next();
	    		fullURL = httpScanner.next();
	    		timeTaken = httpScanner.next();
	    		try{
	    			ipAddress = httpScanner.next();
	    		}catch(Exception ex){
	    			//do nothing
	    		}
	    		params = "{"
	    		+ "\"httpOperation\": \""+httpOperation+"\""+","
				+ "\"fullURL\": \""+fullURL+"\""
		        + "}";
	        }else{
	        	messageCode = "Log";
	        	        	
	        	msg=msg.replace("\\","\\\\");
	        	msg=msg.replace("/","\\/");
	        	msg=msg.replace("\"","\\\"");
	        	msg=msg.replace("'","\\\"");
	        	msg=msg.replace("\b","\\b");
	        	msg=msg.replace("\f","\\f");
	        	msg=msg.replace("\n","\\n");
	        	msg=msg.replace("\r","\\r");
	        	msg=msg.replace("\t","\\t");
	        	params = "{"+ "\"log\": \""+msg+"\""+ "}";
	        }
	    		    		    	
	    	
	        String systemId = null;
	        String userId = null;
	        String domainName = null;
	        
	    	Pattern regFilterPattern = Pattern.compile(regFilter,Pattern.DOTALL);
	        if(regFilter != null ) {
	            if(! regFilterPattern.matcher(msg).matches()) {
	                return;
	            }
	        }
	        
	        systemId = event.getContextMap().getOrDefault("moduleName", "SYSTEM");
	        if(systemId == null){
	        	systemId = "SYSTEM";
	        }
	        
	        userId = event.getContextMap().getOrDefault("userName", "SYSTEM");
	        if(userId == null){
	        	userId = "SYSTEM";
	        }
	        
	        domainName = event.getContextMap().getOrDefault("tenantDomainName", "SYSTEM");
	        if(domainName == null){
	        	domainName = "SYSTEM";
	        }
	        
	        AuditTrailEntity auditTraileEntity = new AuditTrailEntity();
	        ObjectMapper mapperObj = new ObjectMapper();
			String auditTrailMessage=null;
			String timeStampUTC=null;
						
			auditTraileEntity.setSystemid(systemId);
			auditTraileEntity.setSystemmodulename(systemId);
			auditTraileEntity.setMessagecode(messageCode);
			auditTraileEntity.setParams(params);
			auditTraileEntity.setUserid(userId);
			auditTraileEntity.setCompanydomainname(domainName);
			auditTraileEntity.setIpaddress(ipAddress);
			
			Date timeStamp = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			String dateTimeUTC=sdf.format(timeStamp);
			auditTraileEntity.setTimestamp(dateTimeUTC);
			auditTraileEntity.setObjectoldvalue(null);
			auditTraileEntity.setObjectnewvalue(objectnewvalue);
			auditTrailMessage = mapperObj.writeValueAsString(auditTraileEntity);
			
			TextMessage message = session.createTextMessage(auditTrailMessage);
			messageProducer.send(message);
			//session.commit();
			//channel.basicPublish(exchangeName, codedRoutingKey, null, auditTrailMessage.getBytes());
	        
	    } catch (Exception e) {
	    	System.out.println("CentralLog: Exception occured while sending message to centralized message queue: " + e.getMessage());
	    }
	}
	
    public String getRegFilter() {
		return regFilter;
	}

	public void setRegFilter(String regFilter) {
		this.regFilter = regFilter;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void close() {
        try {
            connection.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean requiresLayout() {
        return true;
    }
 }