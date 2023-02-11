package com.roche.connect.dpcr.sim.bl;


import java.io.File;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.connect.dpcr.MainApp;
import com.roche.connect.dpcr.util.DpcrInput;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.QPD;
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

		
		try (FileWriter hl7Writer = new FileWriter(MainApp.getClassPath()+"/"+MainApp.getValueFromPropFile("MessageTxt"), true)) {
		InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/QBP_Z01.txt");
		String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		Parser p = this.context.getPipeParser();
		Date date= new Date();
		String currentTime = dateFormatter.format(date);
		Message queryBuilder = null;
		ObjectMapper mapper = new ObjectMapper();
		DpcrInput dpcrInput = mapper.readValue(new File(MainApp.getValueFromPropFile("DPCRQueryPath")),
				DpcrInput.class);
		if(msg.contains("QBP^Z01")) {
		 queryBuilder =  p.parse(msg);
		String messageControlId = UUID.randomUUID().toString().substring(0, 20);
		MSH msh = (MSH) queryBuilder.get("MSH");
	
		msh.getMsh7_DateTimeOfMessage().setValue(currentTime);
		msh.getSendingApplication().getHd1_NamespaceID().setValue(MainApp.getValueFromPropFile("DeviceName"));
		msh.getSendingApplication().getHd2_UniversalID().clear();
		msh.getMessageControlID().setValue(messageControlId);
		msh.getSendingFacility().getHd1_NamespaceID().setValue(MainApp.getValueFromPropFile("DeviceSerialNumber"));
		msh.getReceivingApplication().getHd1_NamespaceID().setValue(dpcrInput.getReceivingApplicationName());
		if(StringUtils.isNotBlank(dpcrInput.getQueryMsgType()) && !"QBP".equalsIgnoreCase(dpcrInput.getQueryMsgType())) {
			msh.getMsh9_MessageType().getMsg1_MessageCode().setValue(dpcrInput.getQueryMsgType());
			}
			if(StringUtils.isNotBlank(dpcrInput.getQueryMsgEvent()) && !"Z01".equalsIgnoreCase(dpcrInput.getQueryMsgEvent())) {
			msh.getMsh9_MessageType().getMsg2_TriggerEvent().setValue(dpcrInput.getQueryMsgEvent());
			}
			if(StringUtils.isNotBlank(dpcrInput.getQueryProcessingId()) && (!"P".equalsIgnoreCase(dpcrInput.getQueryProcessingId()))) {
			msh.getMsh11_ProcessingID().getPt1_ProcessingID().setValue(dpcrInput.getQueryProcessingId());
			}
			
			if(StringUtils.isNotBlank(dpcrInput.getVersionId()) && (!"2.8".equalsIgnoreCase(dpcrInput.getVersionId()))) {
				
				msh.getVersionID().getVid1_VersionID().setValue(dpcrInput.getVersionId());
			}
		QPD qpd =  (QPD) queryBuilder.get("QPD");
		 Terser ter = new Terser(queryBuilder) ;
		
		int n = 3;
		for(String string:dpcrInput.getPlateId()) {
			System.out.println("++"+string);
			ter.set("/.QPD-"+n, string);
			n+=1;
			
		}
		ter.set("/.QPD-"+(++n), "");
		hl7Writer.write("Request Message from "+MainApp.getValueFromPropFile("DeviceSerialNumber")+ " with Message ControlId "+messageControlId +"\n");
		hl7Writer.write("Message ControlId: "+messageControlId +"\n");
		hl7Writer.write("Date : "+date +"\n");
		hl7Writer.write("Message :\n");
		hl7Writer.write(queryBuilder.toString()+"\n");
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
