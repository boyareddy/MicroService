package com.roche.swam.labsimulator.mpx.bl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.common.bl.hl7.MessageSender;
import com.roche.swam.labsimulator.util.Mp96RunData;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ACK;
import ca.uhn.hl7v2.model.v24.segment.ERR;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.OBR;
import ca.uhn.hl7v2.model.v24.segment.ORC;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

@Component
public class AckMessageSender implements MessageSender{ 
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AckMessageSender.class);
	private HapiContext context;
	   private SimpleDateFormat dateFormatter;

	
public AckMessageSender() {
    	
        this.context = new DefaultHapiContext();
        this.dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    }

@Override
	public void run(Mp96RunData mp96RunData, String ackCheck) throws HL7Exception, IOException {
		// TODO Auto-generated method stub
		QueryMessageBuilder builder = new QueryMessageBuilder();
		Message ackMessage = null;
		int timeDelay = 60000;
		ORC orc = null;
		OBR obr = null;
		ackMessage = buildAckMessage(mp96RunData,ackCheck);
		LOGGER.info("ACK from Device: "+ackMessage.toString());
			
			//Socket socket = null;
			
			
		try (FileWriter ackWriter = new FileWriter(MainApp.getClasspath()+"/"+MainApp.getFilepath("DAcknowledgeTxt"), true)) {
				
				
				try(Socket socket =new Socket(MainApp.getFilepath("HostName"), Integer.parseInt(MainApp.getFilepath("HostPort")));){
				System.out.println("Connection established: " + socket.isConnected());

	            InputStream in = socket.getInputStream();
	            OutputStream out = socket.getOutputStream();
	            String ackMessageWrapper = "\u000b" + ackMessage.toString().trim() + "\u001c" + "\r";

	            // Send the MLLP-wrapped HL7 message to the server
	            out.write(ackMessageWrapper.getBytes());
	            
		           out.close();
		           in.close();
		           ackWriter.write(ackMessage.toString());

			}}catch(Exception e){
				LOGGER.error("Closing Connection :"+ e.getLocalizedMessage());
			}
			
	}


	public Message buildAckMessage(Mp96RunData mp96RunData, String ackCheck) throws IOException, HL7Exception {
		// TODO Auto-generated method stub
		
		InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/ACK.txt");
		String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		Parser p = this.context.getPipeParser();
		ACK ackBuilder = (ACK) p.parse(msg);
		String messageControlId = UUID.randomUUID().toString().substring(0, 5);
		System.out.println("Message control ID :"+messageControlId);
		MSA msa = ackBuilder.getMSA();
		Terser ter = new Terser(ackBuilder) ;
		System.out.println("err code:"+ackCheck);
		if(StringUtils.isNotBlank(ackCheck)) {
		if (ackCheck.contains("200")) {
			msa.getMsa1_AcknowledgementCode().setValue(SimulatorVariables.valueOf("AR").toString());
			ERR err = ackBuilder.getERR();
			
	        ter.set("/.ERR-3-1", SimulatorVariables.valueOf("UMT").toString());
	        ter.set("/.ERR-3-2",  SimulatorVariables.valueOf("UMTD").toString());
	        ter.set("/.ERR-8-1",  SimulatorVariables.valueOf("UMTD").toString());
		} else if (ackCheck.contains("201")) {
			msa.getMsa1_AcknowledgementCode().setValue(SimulatorVariables.valueOf("AR").toString());
			ERR err = ackBuilder.getERR();
			ter.set("/.ERR-3-1", SimulatorVariables.valueOf("UEC").toString());
	        ter.set("/.ERR-3-2",  SimulatorVariables.valueOf("UECD").toString());
	        ter.set("/.ERR-8-1",  SimulatorVariables.valueOf("UECD").toString());
		} else if (ackCheck.contains("202")) {
			msa.getMsa1_AcknowledgementCode().setValue(SimulatorVariables.valueOf("AR").toString());
			ERR err = ackBuilder.getERR();
			ter.set("/.ERR-3-1", SimulatorVariables.valueOf("UPI").toString());
	        ter.set("/.ERR-3-2",  SimulatorVariables.valueOf("UPID").toString());
	        ter.set("/.ERR-8-1",  SimulatorVariables.valueOf("UPID").toString());
		} else if (ackCheck.contains("101")) {
			msa.getMsa1_AcknowledgementCode().setValue(SimulatorVariables.valueOf("AR").toString());
			ERR err = ackBuilder.getERR();
			ter.set("/.ERR-3-1", SimulatorVariables.valueOf("RFM").toString());
	        ter.set("/.ERR-3-2",  SimulatorVariables.valueOf("RFMD").toString());
	        ter.set("/.ERR-8-1",  SimulatorVariables.valueOf("RFMD").toString());
		}
		else {
			ERR err = ackBuilder.getERR();
			err.clear();
		}
		}else {
			ERR err = ackBuilder.getERR();
			err.clear();
		}
		msa.getMsa2_MessageControlID().setValue(mp96RunData.getRunId());
		MSH msh = ackBuilder.getMSH();
		msh.getMsh10_MessageControlID().setValue(mp96RunData.getDeviceId());
		String currentTime = dateFormatter.format(new Date()).toString();
		msh.getMsh7_DateTimeOfMessage().getTs1_TimeOfAnEvent().setValue(currentTime);
		
		
		return ackBuilder;
	}


	@Override
	public void run() throws ParseException, LLPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(Mp96RunData mp96RunData) throws HL7Exception, IOException {
		// TODO Auto-generated method stub
		
	}


}




