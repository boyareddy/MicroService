package com.roche.connect.dpcr.sim.bl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.connect.dpcr.MainApp;
import com.roche.connect.dpcr.sim.DPCRSimulator;
import com.roche.connect.dpcr.util.DpcrInput;
import com.roche.connect.dpcr.util.ResultBean;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.message.ESU_U01;
import ca.uhn.hl7v2.model.v26.segment.EQU;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;



public class EsuMessageBuilder {

	private static final Logger logger = LoggerFactory.getLogger(QueryMessageBuilder.class);
	private HapiContext context;
	private SimpleDateFormat dateFormatter;

	public EsuMessageBuilder() {
		this.setContext(new DefaultHapiContext());
		 this.dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
	}
	
	
	
	public Message build(String status,int i) throws IOException, HL7Exception {
		// TODO Auto-generated method stub
		ResultBean resultBean =null;
		DpcrInput dpcrInput =null;
		if(status.contains("OP")) {
		 resultBean = getResultBean();
		
		}else {
			dpcrInput = getDpcrInput();
		}
		InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/ESU_U01.txt");
		String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		Parser p = this.context.getPipeParser();
		ESU_U01 esuBuilder = (ESU_U01) p.parse(msg);
		String messageControlId = UUID.randomUUID().toString().substring(0, 20);
		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(new Date()); // sets calendar time/date
		String currentTime = dateFormatter.format(cal.getTime()).toString();
		cal.add(Calendar.MINUTE, Integer.parseInt(MainApp.getValueFromPropFile("NoOfESU_InProgress"))-i); // adds one hour
		String runRemainingTime = dateFormatter.format(cal.getTime()).toString();
		MSH msh = esuBuilder.getMSH();
		msh.getMsh7_DateTimeOfMessage().setValue(currentTime);
		msh.getSendingApplication().getHd1_NamespaceID().setValue(MainApp.getValueFromPropFile("DeviceName"));
		msh.getSendingApplication().getHd2_UniversalID().clear();
		msh.getSendingFacility().getHd1_NamespaceID().setValue(MainApp.getValueFromPropFile("DeviceSerialNumber"));
		msh.getMessageControlID().setValue(messageControlId);
		
		EQU equ = esuBuilder.getEQU();
		if(status.contains("OP")) {
			msh.getReceivingApplication().getHd1_NamespaceID().setValue(resultBean.getRecevingApplication());
			msh.getReceivingFacility().getHd1_NamespaceID().setValue(resultBean.getRecevingFacility());
			msh.getVersionID().getVid1_VersionID().setValue(resultBean.getHl7Version());
			equ.getEquipmentInstanceIdentifier().getEi1_EntityIdentifier().setValue(resultBean.getRunId());
			equ.getEqu2_EventDateTime().setValue(runRemainingTime);
			equ.getEqu3_EquipmentState().getCwe1_Identifier().setValue("OP");
			if("OPN".equalsIgnoreCase(status)) {
			equ.getEqu3_EquipmentState().getCwe2_Text().setValue("NORMAL_OPERATION");
			equ.getEqu5_AlertLevel().getCwe1_Identifier().setValue("N");
			equ.getEqu5_AlertLevel().getCwe2_Text().setValue("NORMAL");
			}
		}else if("ID".equalsIgnoreCase(status)) {
			msh.getReceivingApplication().getHd1_NamespaceID().setValue(dpcrInput.getReceivingApplicationName());
			msh.getVersionID().getVid1_VersionID().setValue(dpcrInput.getVersionId());
			equ.getEqu3_EquipmentState().getCwe1_Identifier().setValue(status);
			equ.getEquipmentInstanceIdentifier().getEi1_EntityIdentifier().setValue(DPCRSimulator.getRunId());
			equ.getEquipmentInstanceIdentifier().getEi2_NamespaceID().setValue(DPCRSimulator.getFilePath());
		}else if("SS".equalsIgnoreCase(status)) {
			msh.getReceivingApplication().getHd1_NamespaceID().setValue(dpcrInput.getReceivingApplicationName());
			msh.getVersionID().getVid1_VersionID().setValue(dpcrInput.getVersionId());
			equ.getEqu3_EquipmentState().getCwe1_Identifier().setValue(status);
			equ.getEquipmentInstanceIdentifier().getEi1_EntityIdentifier().setValue(DPCRSimulator.getRunId());
			equ.getEquipmentInstanceIdentifier().getEi2_NamespaceID().clear();
		}else if("ES".equalsIgnoreCase(status)) {
			msh.getReceivingApplication().getHd1_NamespaceID().setValue(dpcrInput.getReceivingApplicationName());
			msh.getVersionID().getVid1_VersionID().setValue(dpcrInput.getVersionId());
			equ.getEqu3_EquipmentState().getCwe1_Identifier().setValue(status);
		}
		
		try (FileWriter hl7Writer = new FileWriter(MainApp.getClassPath()+"/"+MainApp.getValueFromPropFile("MessageTxt"), true)) {
		  hl7Writer.write("Request Message from "+MainApp.getValueFromPropFile("DeviceSerialNumber")+ " with Message ControlId "+messageControlId +"\n");
	   		hl7Writer.write("Message ControlId: "+messageControlId +"\n");
	   		hl7Writer.write("Date : "+new Date() +"\n");
	   		hl7Writer.write("Message :\n");
	   		hl7Writer.write(esuBuilder.toString()+"\n");
		}
		
		return esuBuilder;
	}



	private DpcrInput getDpcrInput() {
		ObjectMapper mapper = new ObjectMapper();
		DpcrInput dpcrInput = new DpcrInput();
		try {
			dpcrInput = mapper.readValue(new File(MainApp.getValueFromPropFile("DPCRQueryPath")),
					DpcrInput.class);
			}catch(Exception e) {
				
				logger.error("JSON parse Error. \n");
				logger.error("Error may br due to :\n");
				logger.error("* Incorrect File path in simulator.properties file.\n");
				logger.error("* JSON Structure Error");
				System.exit(1);
			}
		return dpcrInput;
	}

	private ResultBean getResultBean() {
		ObjectMapper mapper = new ObjectMapper();
		ResultBean resultBean = new ResultBean();
		try {
			 resultBean = mapper.readValue(new File(MainApp.getValueFromPropFile("DPCRRunDataPath")),
					ResultBean.class);
			}catch(Exception e) {
				
				logger.error("JSON parse Error. \n");
				logger.error("Error may br due to :\n");
				logger.error("* Incorrect File path in simulator.properties file.\n");
				logger.error("* JSON Structure Error");
				System.exit(1);
			}
		return resultBean;
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

	
}
