package com.roche.swam.labsimulator.mpx.bl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.swam.labsimulator.MainApp;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.QBP_Q11;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

public class QueryMessageBuilder {

	
	private static final Logger log = LoggerFactory.getLogger(QueryMessageBuilder.class);
	private HapiContext context;
	private SimpleDateFormat dateFormatter;

	public QueryMessageBuilder() {
		this.setContext(new DefaultHapiContext());
		 this.dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
	}

	

	public Message build() throws IOException, HL7Exception{

		
		try (FileWriter queryWriter = new FileWriter(MainApp.getClasspath()+"/"+MainApp.getFilepath("QueryMessageTxt"), true)) {
		InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/QBP_Q11.txt");
		String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		Parser p = this.context.getPipeParser();
		String currentTime = dateFormatter.format(new Date());
		Message queryBuilder ;
		if(msg.contains("QBP^Q11")) {
		 queryBuilder = (QBP_Q11) p.parse(msg);
		String messageControlId = UUID.randomUUID().toString().substring(0, 5);
		MSH msh = ((QBP_Q11) queryBuilder).getMSH();
	
		msh.getMsh7_DateTimeOfMessage().getTs1_TimeOfAnEvent().setValue(currentTime);
		msh.getMsh10_MessageControlID().setValue(MainApp.getFilepath("DeviceSerialNumber"));
		msh.getMsh3_SendingApplication().getHd1_NamespaceID().setValue(MainApp.getFilepath("DeviceType"));
		if(StringUtils.isNotBlank(((QBP_Q11) queryBuilder).getRCP().getRcp1_QueryPriority().getValue())) {
			queryWriter.write(queryBuilder.toString());	
		}
		else {
			 String qbpQueryMessageWrapper =  queryBuilder.toString()+"\n"+"RCP|"+"\n";
			 queryWriter.write(qbpQueryMessageWrapper);	
		}}
		else {
			 queryBuilder =  p.parse(msg);
			 Terser ter = new Terser(queryBuilder) ;
			  ter.set("/.MSH-7-1", currentTime);
			  ter.set("/.MSH-10-1", MainApp.getFilepath("DeviceSerialNumber"));
		}
		
		 
		return queryBuilder;
		}
	}

	/**
	 * @return the context
	 */
	public HapiContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(HapiContext context) {
		this.context = context;
	}

	/**
	 * @return the instId
	 */

	
	
}
