package com.roche.swam.labsimulator.mpx.bl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.common.bl.hl7.MessageSender;
import com.roche.swam.labsimulator.util.Mp96RunData;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;

@Component
public class ResultMessageSender implements MessageSender {

	@Autowired
	private SampleRepository samples;

	private static final Logger LOGGER = LoggerFactory.getLogger(ResultMessageSender.class);
	

	public void run(Mp96RunData mp96RunData) {
		//Socket socket = null;
		LOGGER.info("inside QBS method of result message sender...");
		try (FileWriter resultWriter = new FileWriter(MainApp.getClasspath()+"/"+MainApp.getFilepath("ResultMessageTxt"), true)) {	
			try (FileWriter acknowledgementWriter = new FileWriter(MainApp.getClasspath()+"/"+MainApp.getFilepath("CAcknowledgeTxt"), true)) {
				ResultMessageBuilder builder = new ResultMessageBuilder();
				Message ssuMessage = null;
				int timeDelay = Integer.parseInt(MainApp.getFilepath("TimeOut"));
					ssuMessage = builder.build(mp96RunData);
					resultWriter.write(ssuMessage.toString() + "\n\n");
					System.out.println("OUL :"+ssuMessage.toString());
					try(Socket socket =new Socket(MainApp.getFilepath("HostName"), Integer.parseInt(MainApp.getFilepath("HostPort")));){  //sonar qube code coverages
					System.out.println("Connection established: " + socket.isConnected());
					socket.setSoTimeout(timeDelay);
					 InputStream in = socket.getInputStream();
			            OutputStream out = socket.getOutputStream();
			            String ssuMessageWrapper = "\u000b" + ssuMessage.toString() + "\u001c" + "\r";
			            // Send the MLLP-wrapped HL7 message to the server
			            out.write(ssuMessageWrapper.getBytes());

						DataInputStream input = new DataInputStream(in);
						BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));

						StringBuilder sb = new StringBuilder();
						String ackData;
						while (((ackData = reader.readLine()) != null) && (!ackData.trim().equals(""))) {
							sb.append("\n" + ackData);
						}

						String ackMessage = sb.toString().trim();
			           out.close();
			           in.close();

			            LOGGER.info("Response from Connect: "+ackMessage.trim()); 
			            acknowledgementWriter.write("\n" + ackMessage.trim()+ "\n");
					LOGGER.info("after run method in result message sender...");
			} }catch (IOException ioException) {
				LOGGER.error(ioException.getLocalizedMessage());
			} catch (HL7Exception hl7Exception) {
				LOGGER.error(hl7Exception.getLocalizedMessage());
			}
		} catch (IOException e1) {
			LOGGER.error(e1.getLocalizedMessage());
		}
	}


	@Override
	public void run() throws ParseException, LLPException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void run(Mp96RunData mp96RunData, String ackCheck) throws HL7Exception, IOException {
		// TODO Auto-generated method stub
		
	}




}
