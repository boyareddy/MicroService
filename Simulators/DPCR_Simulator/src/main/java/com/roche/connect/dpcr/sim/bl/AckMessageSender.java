package com.roche.connect.dpcr.sim.bl;

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
import com.roche.connect.dpcr.MainApp;
import com.roche.connect.dpcr.common.bl.hl7.MessageSender;
import com.roche.connect.dpcr.util.ResultBean;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.message.ACK;
import ca.uhn.hl7v2.model.v26.segment.ERR;
import ca.uhn.hl7v2.model.v26.segment.MSA;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.OBR;
import ca.uhn.hl7v2.model.v26.segment.ORC;
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
	public void run(ResultBean resultBean, String ackCheck) throws HL7Exception, IOException {
		// TODO Auto-generated method stub
		QueryMessageBuilder builder = new QueryMessageBuilder();
		Message ackMessage = null;
		int timeDelay = 60000;
		ORC orc = null;
		OBR obr = null;
		ackMessage = buildAckMessage(resultBean,ackCheck);
		LOGGER.info("ACK from Device: "+ackMessage.toString());
				try(Socket socket =new Socket(MainApp.getValueFromPropFile("HostName"), Integer.parseInt(MainApp.getValueFromPropFile("HostPort")));){
				System.out.println("Connection established: " + socket.isConnected());

	            InputStream in = socket.getInputStream();
	            OutputStream out = socket.getOutputStream();
	            String ackMessageWrapper = "\u000b" + ackMessage.toString() + "\u001c" + "\r";

	            // Send the MLLP-wrapped HL7 message to the server
	            out.write(ackMessageWrapper.getBytes());
	            
		           out.close();
		           in.close();
		           Thread.sleep(Long.parseLong(MainApp.getValueFromPropFile("ResultMessageTimeInterval")));  
			}catch(Exception e){
				LOGGER.error("Closing Connection :"+ e.getLocalizedMessage());
			}
			
	}


	public Message buildAckMessage(ResultBean resultBean, String ackCheck) throws IOException, HL7Exception {
		// TODO Auto-generated method stub
		
		InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/ACK.txt");
		String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		Parser p = this.context.getPipeParser();
		ACK ackBuilder = (ACK) p.parse(msg);
		String messageControlId = UUID.randomUUID().toString().substring(0, 20);
		MSA msa = ackBuilder.getMSA();
		Terser ter = new Terser(ackBuilder) ;
		System.out.println("err code:"+ackCheck);
		if(StringUtils.isNotBlank(ackCheck)) {
		if (ackCheck.contains("200")) {
			msa.getMsa1_AcknowledgmentCode().setValue(SimulatorVariables.valueOf("AR").toString());
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
		msa.getMsa2_MessageControlID().setValue(resultBean.getAckMsgControlId());
		MSH msh = ackBuilder.getMSH();
		msh.getMsh10_MessageControlID().setValue(resultBean.getAckMsgControlId());
		String currentTime = dateFormatter.format(new Date()).toString();
		msh.getMsh7_DateTimeOfMessage().setValue(currentTime);
		msh.getSendingApplication().getHd1_NamespaceID().setValue(MainApp.getValueFromPropFile("DeviceName"));
		msh.getSendingFacility().getHd1_NamespaceID().setValue(MainApp.getValueFromPropFile("DeviceSerialNumber"));
		msh.getReceivingApplication().getHd1_NamespaceID().setValue(resultBean.getRecevingApplication());
		msh.getVersionID().getVid1_VersionID().setValue(resultBean.getHl7Version());
		try (FileWriter hl7Writer = new FileWriter(MainApp.getClassPath()+"/"+MainApp.getValueFromPropFile("MessageTxt"), true)) {
		  hl7Writer.write("Request Message from "+MainApp.getValueFromPropFile("DeviceSerialNumber")+ " with Message ControlId "+messageControlId +"\n");
	   		hl7Writer.write("Message ControlId: "+messageControlId +"\n");
	   		hl7Writer.write("Date : "+new Date() +"\n");
	   		hl7Writer.write("Message :\n");
	   		hl7Writer.write(ackBuilder.toString()+"\n");
		}
		
		return ackBuilder;
	}


	@Override
	public void run() throws ParseException, LLPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(ResultBean resultBean) throws HL7Exception, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(String status) throws HL7Exception, IOException {
		// TODO Auto-generated method stub
		
	}


}




