package com.roche.swam.labsimulator.mpx.bl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.swam.labsimulator.InstanceUtil;
import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.util.LP24JsonPropertyReader;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v251.message.ESU_U01;
import ca.uhn.hl7v2.parser.Parser;

public class ResultMessageBuilderU01{
	 
	private HapiContext context;
	    
	    private SimpleDateFormat dateFormatter;
	    private static final Logger LOGGER = LoggerFactory.getLogger(ResultMessageBuilderU01.class);
	    
	    public ResultMessageBuilderU01() {
	        this.context = new DefaultHapiContext();
	        this.dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
	    }
	    
	public ESU_U01 build() throws IOException, HL7Exception {
    	InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/ESU_U01.txt");
        String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
        Parser p = this.context.getPipeParser();
        ESU_U01 resultMessage = (ESU_U01) p.parse(msg);
        String messageControlId = UUID.randomUUID().toString().substring(0, 20);
        LP24JsonPropertyReader jpr = new LP24JsonPropertyReader();
        String sampleId=jpr.getSampleIdList().get(0);
        Sample sample = jpr.readJsonData(sampleId);
        System.out.println("\n\n ");
		resultMessage.getMSH().getMsh3_SendingApplication().getHd2_UniversalID().setValue(MainApp.getFilepath("DeviceSerialNumber"));
		resultMessage.getEQU().getEqu1_EquipmentInstanceIdentifier().getEi1_EntityIdentifier().setValue(MainApp.getFilepath("DeviceSerialNumber"));
        resultMessage.getMSH().getMsh3_SendingApplication().getHd1_NamespaceID().setValue(MainApp.getDeviceName());
        resultMessage.getMSH().getMsh4_SendingFacility().getHd1_NamespaceID().setValue(sample.getSendingFacilityName());
        resultMessage.getMSH().getMsh5_ReceivingApplication().getHd1_NamespaceID().setValue(sample.getReceivingApplicationName());
        resultMessage.getMSH().getMsh6_ReceivingFacility().getHd1_NamespaceID().setValue(sample.getReceivingFacilityName());
        resultMessage.getMSH().getMsh7_DateTimeOfMessage().parse(dateFormatter.format(Calendar.getInstance().getTime()));
        resultMessage.getMSH().getMsh9_MessageType().getName();
        resultMessage.getMSH().getMsh10_MessageControlID().setValue(messageControlId);
        resultMessage.getMSH().getMsh11_ProcessingID().getName();
        resultMessage.getMSH().getMsh12_VersionID().getVid1_VersionID().setValue(sample.getVersionId());
        resultMessage.getEQU().getEqu2_EventDateTime().parse(dateFormatter.format(Calendar.getInstance().getTime()));
        resultMessage.getEQU().getEqu3_EquipmentState().getCe1_Identifier().setValue(InstanceUtil.getInstance().getDeviceState());
        return resultMessage;
	}
	

}
