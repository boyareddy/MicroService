package com.roche.swam.labsimulator.mpx.bl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.common.bl.hl7.HL7Client;
import com.roche.swam.labsimulator.common.bl.hl7.MessageSender;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
@Component
public class ResultMessageSenderU01 implements MessageSender{

	private static final Logger LOGGER = LoggerFactory.getLogger(ResultMessageSenderU01.class);
    
	@Override
	public void run(HL7Client client, EnumSampleStatus sampleStatus)
			throws ParseException, LLPException, NumberFormatException, InterruptedException {
		/*
		 * for sonar 
		 * 
		 */
	}

	@Override
	public void run(HL7Client client, SampleRepository samples, EnumSampleStatus sampleStatus)
			throws NumberFormatException, InterruptedException {
		/*
		 * for sonar 
		 * 
		 */
	}

	@Override
	public void run(HL7Client client) throws ParseException, LLPException,InterruptedException,IOException, HL7Exception {
		LOGGER.info("inside ESU_U01 method of result message sender...");
		try (FileWriter resultWriter = new FileWriter(
				MainApp.getClasspath() + "/" + MainApp.getFilepath("ResultMessageTxt"), true)) {
			try (FileWriter acknowledgementWriter = new FileWriter(
					MainApp.getClasspath() + "/" + MainApp.getFilepath("AcknowledgeTxt"), true)) {
				ResultMessageBuilderU01 builder = new ResultMessageBuilderU01();
				Message esuMessage = null;
				int esuCount = 1;
				for (int i = 1; i <= esuCount; i++) {
					esuMessage = builder.build();
					resultWriter.write(esuMessage.toString() + "\n\n");
					LOGGER.info(esuMessage.toString());
					try {
						Connection connection = client.getConnection();
						Initiator initiator = connection.getInitiator();
						initiator.setTimeout(Integer.parseInt(MainApp.getFilepath("HAPITimeout")),TimeUnit.MILLISECONDS);
						Message ackMessage = initiator.sendAndReceive(esuMessage);
						LOGGER.info(ackMessage.toString());
						acknowledgementWriter.write(ackMessage.toString() + "\n\n");
					} catch (LLPException e) {
						LOGGER.error("Closing HL7 connection :" + e.getLocalizedMessage());
					}
				}
			}
		}
	}
					
}
