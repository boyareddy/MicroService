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

package com.roche.dpcr;

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
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.service.AuditTrailEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.regex.Pattern;

import javax.naming.directory.InvalidAttributesException;

@Plugin(name = "JMSRabbitMQ", elementType = "appender", category = "Core", printObject = true)
public class JMSRabbitMQAppender extends AbstractAppender   {

	private static final String EXCHANGETYPEDIRECT = "direct";
	private static final String EXCHANGETYPETOPIC = "topic";
	private static final String EXCHANGETYPEFANOUT = "fanout";
	private static final String EXCHANGETYPEHEADERS = "headers";
		
	private String exchangeName = null;
	private String regFilter = null;
	private String userName = null;
	private String password = null;
	private String host =  null;
	private int port = -1;
	private String routingKey = null;
	
	ConnectionFactory connectionFactory = null;
	Connection connection =  null;
	Channel channel=null;
	boolean initialized=false;
	private static final  String SYSTEM = "SYSTEM";
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(JMSRabbitMQAppender.class.getName());
	
	JMSRabbitMQAppender(final String name,final Filter filter,final Layout<? extends Serializable> layout,
            final boolean ignoreExceptions, final String exchangeName, final String host, final int port, final String userName, final String password,final String regFilter,String exchangeType,String routingKey,final boolean durable){
		
		super(name,filter,layout,ignoreExceptions);
				
		this.host = host;
		this.regFilter = regFilter;
		this.userName = userName;
		this.password = password;
		this.port = port;
		this.exchangeName = exchangeName;
		this.routingKey = routingKey;
		try{
			connectionFactory = new ConnectionFactory();
			connectionFactory.setUsername(userName);
			connectionFactory.setPassword(password);
			connectionFactory.setVirtualHost("/");
			connectionFactory.setHost(host);
			connectionFactory.setPort(port);
			connection = connectionFactory.newConnection();
	        channel = connection.createChannel();
	        
	        if(exchangeType == null || exchangeType.isEmpty()){
	        	throw new NullPointerException("Exchange type needs to be specified");
	        }else if(!(exchangeType.equals(EXCHANGETYPETOPIC) || exchangeType.equals(EXCHANGETYPEDIRECT)
	        		|| exchangeType.equals(EXCHANGETYPEFANOUT) || exchangeType.equals(EXCHANGETYPEHEADERS))){
	        	throw new InvalidAttributesException("Invalid Exchange type:"+exchangeType+" Exchange type should be one of the following: "
	        			+EXCHANGETYPEDIRECT+","+EXCHANGETYPETOPIC+","+EXCHANGETYPEFANOUT+","+EXCHANGETYPEHEADERS);
	        }
	        
	        channel.exchangeDeclare(exchangeName, exchangeType,durable);
	        initialized=true;
		}catch(IOException ioex){
			logger.error("Central Messaging Queue: JMSRabbitMQAppender: FAILED to start."+ioex.getMessage());
		}catch(Exception ex){
			logger.error("Central Messaging Queue: JMSRabbitMQAppender: FAILED to start. "+ex.getMessage());
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
	        	try(Scanner httpScanner = new Scanner(httpInfo))
                {
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
                }
		        String systemId = null;
		        String userId = null;
		        String domainName = null;
		        
		    	Pattern regFilterPattern = Pattern.compile(regFilter,Pattern.DOTALL);
		        if(regFilter != null && (! regFilterPattern.matcher(msg).matches())) {
		                return;
		        }
		        systemId = event.getContextData().getValue("moduleName");
		        if(systemId == null){
		        	systemId = SYSTEM;
		        }
		        
		        userId = event.getContextData().getValue("userName");
		        if(userId == null){
		        	userId = SYSTEM;
		        }
		        
		        domainName = event.getContextData().getValue("tenantDomainName");
		        if(domainName == null){
		        	domainName = SYSTEM;
		        }
		        
		        AuditTrailEntity auditTraileEntity = new AuditTrailEntity();
		        ObjectMapper mapperObj = new ObjectMapper();
				String auditTrailMessage=null;
							
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
		        
	        }
	    } catch (Exception e) {
	    	logger.error("CentralLog: Exception occured while sending message to centralized message queue: " + e.getMessage());
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
        	logger.error(e.getMessage());
        }
    }

    public boolean requiresLayout() {
        return true;
    }
 }