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

package com.roche.connect.ttv2analyzer;

import java.io.IOException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.service.AuditTrailEntity;
import com.hcl.hmtp.common.server.service.AuditTrailSedaProcessor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.regex.Pattern;

@Plugin(name = "HTTPPOSTAppender", elementType = "appender", category = "Core", printObject = true)
public class HTTPPOSTAppender extends AbstractAppender   {

	private String regFilter = null;
	private ProducerTemplate template=null;
	private CamelContext context=null;
	private boolean camelInitialized = false;
	private String fromEndPoint=null;
	private String toEndPoint=null;
	private String postURL=null;
			
	HTTPPOSTAppender(final String name,final Filter filter,final Layout<? extends Serializable> layout,
            final boolean ignoreExceptions, final String fromEndPoint, final String toEndPoint,final String postURL,final String regFilter){
		
		super(name,filter,layout,ignoreExceptions);
		
		this.regFilter = regFilter;
		this.fromEndPoint = fromEndPoint;
		this.toEndPoint = toEndPoint;
		this.postURL = postURL;
				
		try{
				        
	        if(fromEndPoint == null || fromEndPoint.isEmpty()){
	        	throw new Exception("From endpoint needs to be specified");
	        }
	        
	        if(toEndPoint == null || toEndPoint.isEmpty()){
	        	throw new Exception("To endpoint needs to be specified");
	        }
	        			    	  
	        StartCamelRouting();
	        
	        camelInitialized=true;
	        
	        System.out.println("Central Messaging: HTTPPOSTAppender: Started sending logs to central messaging");
		}catch(IOException ioex){
			System.out.println("Central Messaging: HTTPPOSTAppender: FAILED to start."+ioex.getMessage());
		}catch(Exception ex){
			System.out.println("Central Messaging: HTTPPOSTAppender: FAILED to start."+ex.getMessage());
		}
	}
		
	@PluginFactory
    public static HTTPPOSTAppender createAppender(
    		@PluginAttribute(value = "name") String name,
    		@PluginElement("Filter") Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) Boolean ignoreExceptions,
            @PluginAttribute("fromEndPoint") final String fromEndPoint,
            @PluginAttribute("toEndPoint") final String toEndPoint,
            @PluginAttribute("postURL") final String postURL,
            @PluginAttribute("regFilter") final String regFilter
            ) {
            return new HTTPPOSTAppender(name,filter,layout,ignoreExceptions,fromEndPoint,toEndPoint,postURL,regFilter);
    }
			
	@Override
	public ErrorHandler getHandler(){
		return null;
	}
			 
	public void StartCamelRouting() throws Exception
	{
        try {
        	context = new DefaultCamelContext();
        	context.addRoutes(new RouteBuilder() {
	        	@Override
			    public void configure() throws Exception {
	        		from(fromEndPoint).process(new AuditTrailSedaProcessor(postURL)).to(toEndPoint);
	        	}
            });
            context.start();
            template = context.createProducerTemplate();
        }catch(Exception ex){
        	throw ex;
        }
	 }
		
	void  SendAuditTrailToCamel(String centralLogMessage) throws Exception{
		try{
			if(camelInitialized){
				template.sendBody(fromEndPoint,centralLogMessage);
			}
		}catch(Exception e){
			//Do Nothing
		}
	}
	
	
	@Override
	public synchronized void append(LogEvent event) {
	    try {

	    	//Check if writing to RabbitMQ is initialized   
	    	if(!camelInitialized){
	    		return;
	    	}
	    	
	    	String msg = event.toString();
	    	
	    	//Processing for JAXRSResponseFilter and REST interceptor filter
	    	String messageCode="";
	    	String httpOperation = null;
	        String fullURL = null;
	        String params = null;
	        String objectnewvalue=null;
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
					
			SendAuditTrailToCamel(auditTrailMessage);
			
	    } catch (Exception e) {
	    	//Do nothing
	    	System.out.println("CentralLog: Exception occured while sending message to central messaging" + e.getMessage());
	    }
	}
	
    public String getRegFilter() {
		return regFilter;
	}

	public void setRegFilter(String regFilter) {
		this.regFilter = regFilter;
	}

	public void close() {
        try {
            
        }catch (Exception e) {
           
        }
    }

    public boolean requiresLayout() {
        return true;
    }
 }