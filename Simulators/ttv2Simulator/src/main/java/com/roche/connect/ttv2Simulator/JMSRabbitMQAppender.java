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
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

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

@Plugin(name = "JMSRabbitMQ", elementType = "appender", category = "Core", printObject = true)
public class JMSRabbitMQAppender extends AbstractAppender   {

	final private String EXCHANGETYPE_DIRECT = "direct";
	final private String EXCHANGETYPE_TOPIC = "topic";
	final private String EXCHANGETYPE_FANOUT = "fanout";
	final private String EXCHANGETYPE_HEADERS = "headers";
		
	private String exchangeName = null;
	private String regFilter = null;
	private String userName = null;
	private String password = null;
	private String host =  null;
	private int port = -1;
	private String exchangeType = null;
	private String routingKey = null;
	private boolean durable = true;
	
	ConnectionFactory connectionFactory = null;
	Connection connection =  null;
	Channel channel=null;
	boolean initialized=false;
		
	JMSRabbitMQAppender(final String name,final Filter filter,final Layout<? extends Serializable> layout,
            final boolean ignoreExceptions, final String exchangeName, final String host, final int port, final String userName, final String password,final String regFilter,String exchangeType,String routingKey,final boolean durable){
		
		super(name,filter,layout,ignoreExceptions);
				
		this.host = host;
		this.regFilter = regFilter;
		this.userName = userName;
		this.password = password;
		this.port = port;
		this.exchangeName = exchangeName;
		this.exchangeType = exchangeType; 
		this.routingKey = routingKey;
		this.durable = durable;
		try{
			connectionFactory = new ConnectionFactory();
			connectionFactory.setUsername(userName);
			connectionFactory.setPassword(password);
			connectionFactory.setVirtualHost("/");
			connectionFactory.setHost(host);
			connectionFactory.setPort(port);
			connection = connectionFactory.newConnection();
	        channel = connection.createChannel();
	        
	        if(exchangeName == null || exchangeName.isEmpty()){
	        	throw new Exception("Exchange name needs to be specified");
	        }
	        	        
	        if(exchangeType == null || exchangeType.isEmpty()){
	        	throw new Exception("Exchange type needs to be specified");
	        }else if(!(exchangeType.equals(EXCHANGETYPE_TOPIC) || exchangeType.equals(EXCHANGETYPE_DIRECT)
	        		|| exchangeType.equals(EXCHANGETYPE_FANOUT) || exchangeType.equals(EXCHANGETYPE_HEADERS))){
	        	throw new Exception("Invalid Exchange type:"+exchangeType+" Exchange type should be one of the following: "
	        			+EXCHANGETYPE_DIRECT+","+EXCHANGETYPE_TOPIC+","+EXCHANGETYPE_FANOUT+","+EXCHANGETYPE_HEADERS);
	        }
	        
	        channel.exchangeDeclare(exchangeName, exchangeType,durable);
	        initialized=true;
	        System.out.println("Central Messaging Queue: JMSRabbitMQAppender: Started sending logs to central messaging queue.");
		}catch(IOException ioex){
			System.out.println("Central Messaging Queue: JMSRabbitMQAppender: FAILED to start."+ioex.getMessage());
		}catch(Exception ex){
			System.out.println("Central Messaging Queue: JMSRabbitMQAppender: FAILED to start."+ex.getMessage());
		}
	}
		
	@PluginFactory
    public static JMSRabbitMQAppender createAppender(
    		@PluginAttribute(value = "name") String name,
    		@PluginElement("Filter") Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) Boolean ignoreExceptions,
            @PluginAttribute("exchangeName") final String exchangeName,
            @PluginAttribute("host") final String host,
            @PluginAttribute(value = "port", defaultInt = -1) final int port,
            @PluginAttribute("userName") final String userName,
            @PluginAttribute("password") final String password,
            @PluginAttribute("regFilter") final String regFilter,
            @PluginAttribute("conversionPattern") final String conversionPattern,
            @PluginAttribute("exchangeType") final String exchangeType,
            @PluginAttribute("routingKey") final String routingKey,
            @PluginAttribute(value = "durable", defaultBoolean = true) final Boolean durable
            ) {
	
            return new JMSRabbitMQAppender(name,filter,layout,ignoreExceptions,exchangeName, host , port, userName, password,regFilter,exchangeType,routingKey,durable);
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
	        	
	        	if((routingKey != null) && (!routingKey.isEmpty())){
	        		codedRoutingKey = routingKey+"."+"AccessLog";
	        	}
	        	
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
	        		        	
	        	if((routingKey != null) && (!routingKey.isEmpty())){
	        		codedRoutingKey = routingKey+"."+"Log";
	        	}
	        	
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
			
			channel.basicPublish(exchangeName, codedRoutingKey, null, auditTrailMessage.getBytes());
	        
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void close() {
        try {
            connection.close();
            channel.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean requiresLayout() {
        return true;
    }
 }