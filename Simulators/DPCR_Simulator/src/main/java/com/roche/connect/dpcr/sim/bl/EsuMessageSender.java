package com.roche.connect.dpcr.sim.bl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.connect.dpcr.MainApp;
import com.roche.connect.dpcr.common.bl.hl7.MessageSender;
import com.roche.connect.dpcr.util.ResultBean;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.message.ACK;
import ca.uhn.hl7v2.parser.Parser;

@Component
public class EsuMessageSender implements MessageSender{ 
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EsuMessageSender.class);
	private HapiContext context;
	   private SimpleDateFormat dateFormatter;

	

@Override
	public void run(String status) throws HL7Exception, IOException, NumberFormatException, InterruptedException {
		Message esuMessage = null;
		EsuMessageBuilder builder = new EsuMessageBuilder();
		if("OP".equalsIgnoreCase(status)) {
			LOGGER.info("No. of Inprogress ESU set: "+MainApp.getValueFromPropFile("NoOfESU_InProgress"));
			int esuCount = Integer.parseInt(MainApp.getValueFromPropFile("NoOfESU_InProgress"));
			if(esuCount>0) {
		for(int i=0;i<Integer.parseInt(MainApp.getValueFromPropFile("NoOfESU_InProgress"));i++) {
		esuMessage = builder.build(status,i);
		sendESUToAdapter(esuMessage.toString());
		Thread.sleep(Long.parseLong(MainApp.getValueFromPropFile("ResultMessageTimeInterval")));
		}
			}else {
				LOGGER.info("No. of Inprogress ESU set: "+MainApp.getValueFromPropFile("NoOfESU_InProgress")+"\r Skipping Inprogress ESU");
			}
		}else if("PING".equalsIgnoreCase(status)){
			sendESUToAdapter("");
		}
		else {
			esuMessage = builder.build(status,0);
			sendESUToAdapter(esuMessage.toString());
			Thread.sleep(Long.parseLong(MainApp.getValueFromPropFile("ResultMessageTimeInterval")));
		}
			
	}


private void sendESUToAdapter(String esuMessage) {
	LOGGER.info("ESU  from Device: "+esuMessage.toString());
	int timeDelay = Integer.parseInt(MainApp.getValueFromPropFile("TimeOut"));
			try(Socket socket =new Socket(MainApp.getValueFromPropFile("HostName"), Integer.parseInt(MainApp.getValueFromPropFile("HostPort")));){
			System.out.println("Connection established: " + socket.isConnected());

	        InputStream in = socket.getInputStream();
	        OutputStream out = socket.getOutputStream();
	        String ackMessageWrapper = "\u000b" + esuMessage + "\u001c" + "\r";
	    	socket.setSoTimeout(timeDelay);
	        // Send the MLLP-wrapped HL7 message to the server
	        out.write(ackMessageWrapper.getBytes());
	        
	        DataInputStream input = new DataInputStream(in);
			new BufferedReader(new InputStreamReader(input, "UTF-8"));
			
			new StringBuilder();
			String response=null;
			byte[] byte1;
			while(input.read() !=-1) {
				byte1=new byte[input.available()];
				input.read(byte1);
				response=new String(byte1);
				if(StringUtils.isNotBlank(response)) {
				LOGGER.info("Response received: \n  " + response);

				if(response.contains("ACK")) {
					if(response.contains("MSH")) {
					validateAck(response.trim());
					}
				}else {
					LOGGER.info("No ACK recieved");
					System.exit(1);
				}
				}else {
					LOGGER.info("No ACK recieved");
					System.exit(1);
				}
			}
			if(StringUtils.isBlank(response)) {
				LOGGER.info("No ACK recieved");
				System.exit(1);
			}
		}catch(Exception e){
			LOGGER.error("Closing Connection :"+ e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(1);
		}
}


private void validateAck(String response) {
	try  (FileWriter hl7Writer = new FileWriter(MainApp.getClassPath()+"/"+MainApp.getValueFromPropFile("MessageTxt"), true)){
		 this.context = new DefaultHapiContext();
		Parser p = this.context.getPipeParser();
		ACK ack = (ACK) p.parse(hl7VersionUpdate(response));
		String ackStatus = ack.getMSA().getMsa1_AcknowledgmentCode().getValue();
		
		
		hl7Writer.write("\n Response ACK Message from Connect \n");
		hl7Writer.write("Message ControlId: "+ack.getMSH().getMessageControlID().getValue() +"\n");
		hl7Writer.write("Date : "+new Date() +"\n");
		hl7Writer.write("Message :\n");
		hl7Writer.write(response.trim()+"\n");
		if(ackStatus.equalsIgnoreCase("AR")) {
			System.out.println("Recieved Negative ACK from Connect for Query with error code: "+ack.getMSA().getTextMessage().getValue());
          	throw new Exception("Error while processing ACK response");
			
		}
	} catch (Exception e) {
		LOGGER.error("Error while processing ACK response"+e.getMessage());
      	System.exit(1);
	}
}


private String hl7VersionUpdate(String inMsg) {
	
	InputStream resource = new ByteArrayInputStream(inMsg.getBytes());
	String msg = "";
	try {
		msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
	} catch (IOException e2) {
		// TOrmmsgODO Auto-generated catch block
		e2.printStackTrace();
	}
	StringBuilder sb = new StringBuilder();
	String[] msgSplit = msg.split("\r");
	
	for(int i=0; i<msgSplit.length;i++) {
		
		if(msgSplit[i].trim().contains("MSH")) {
			sb.append(updateMsh(msgSplit[i])).append('\r');
		}else {
			sb.append(msgSplit[i]).append('\r');
		}
	}
	
	return sb.toString();
	
}



private String updateMsh(String inMsg) {
	// TODO Auto-generated method stub
	
	String[] mshSplit= inMsg.split("\\|");
	
	if(mshSplit.length>11) {
		mshSplit[11]="2.6";
	}
	return String.join("|", mshSplit);
}





	@Override
	public void run(ResultBean resultBean, String ackCheck) throws HL7Exception, IOException {
		
	}

	@Override
	public void run(ResultBean resultBean) throws HL7Exception, IOException {
		
	}

	@Override
	public void run() throws ParseException, LLPException, HL7Exception, IOException {
		
	}


}




