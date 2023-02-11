package com.roche.swam.labsimulator.mpx.bl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.common.bl.hl7.HL7Client;
import com.roche.swam.labsimulator.common.bl.hl7.MessageSender;
import com.roche.swam.labsimulator.util.SimulatorVariables;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;

@Component
public class ResultMessageSender implements MessageSender {

	@Autowired
	private SampleRepository samples;
	@Autowired
	private RunRepository runs;
	@Autowired
	private ApplicationEventPublisher publisher;

	private static final Logger LOGGER = LoggerFactory.getLogger(ResultMessageSender.class);
	

	public void run(HL7Client client, EnumSampleStatus sampleStatus) throws NumberFormatException, InterruptedException {
		
		LOGGER.info("inside QBS method of result message sender...");
		try (FileWriter resultWriter = new FileWriter(MainApp.getClasspath()+"/"+MainApp.getFilepath("ResultMessageTxt"), true)) {	
			try (FileWriter acknowledgementWriter = new FileWriter(MainApp.getClasspath()+"/"+MainApp.getFilepath("AcknowledgeTxt"), true)) {
//			String deviceRunID = "ORD"+SampleIdGenerator.getNext();
				ResultMessageBuilder builder = new ResultMessageBuilder();
				Message ssuMessage = null;
				Run run = null;
				String runResult = setRunResultbySampleResults(sampleStatus);
				int ssuCount = Integer.parseInt(MainApp.getFilepath("NoOfSSU"));
				for(int i=1;i<=ssuCount;i++) {
					//Collection<Sample> samplesList = 
				for (Sample sample : this.samples.getAllInState(null, sampleStatus)) {
					
					
					System.out.println("SAMPLE:"+ sample.getSampleId()+" i:"+i+" status:"+sample.getResult());
					 run = this.runs.getRun(sample.getRun());
					run.finish();
					LOGGER.info(sample.getSampleId());
					LOGGER.info(sample.getSampleId()+""+sample.getInstrumentId()+""+sample.getSampleType()+""+sample.getProtocolType()+""+sample.getOutputContainerId()+""+sample.getOutputPosition());
					
					 ssuMessage = builder.build(sample,run,i, runResult);
					resultWriter.write(ssuMessage.toString() + "\n\n");
					LOGGER.info(ssuMessage.toString());
					try {
						Connection connection = client.getConnection();
						 Initiator initiator = connection.getInitiator();
						 initiator.setTimeout(Integer.parseInt(MainApp.getFilepath("HAPITimeout")), TimeUnit.MILLISECONDS);
						Message ackMessage = initiator.sendAndReceive(ssuMessage);
						LOGGER.info(ackMessage.toString());
						acknowledgementWriter.write(ackMessage.toString() + "\n\n");
					} catch (LLPException e) {
						LOGGER.error("Closing HL7 connection :" +e.getLocalizedMessage());
						System.exit(1);
					}
					if(i==ssuCount) {
					sample.setStatus(EnumSampleStatus.FINISHED);
					this.publisher.publishEvent(new SampleChangedEvent(this, sample.getSampleId()));
					}
					
					Thread.sleep(Long.parseLong(MainApp.getFilepath("ResultMessageTimeInterval")));
				}
				
				}
				LOGGER.info("after run method in result message sender...");
			} catch (IOException ioException) {
				LOGGER.error(ioException.getLocalizedMessage());
			} catch (HL7Exception hl7Exception) {
				LOGGER.error(hl7Exception.getLocalizedMessage());
			}
		} catch (IOException e1) {
			LOGGER.error(e1.getLocalizedMessage());
		}
	}

	@Override
	public void run(HL7Client client, SampleRepository samples,EnumSampleStatus sampleStatus) throws NumberFormatException, InterruptedException {
		this.samples = samples;
		this.run(client,sampleStatus);

	}
	
	private String setRunResultbySampleResults(EnumSampleStatus sampleStatus) {
		// TODO Auto-generated method stub
		String result = "";
		StringBuilder sb = new StringBuilder();
		for (Sample sample : this.samples.getAllInState(null, sampleStatus)) {
			sb.append(sample.getResult()+"^");
		}
		
		if (sb.toString().contains("Flagged") || sb.toString().contains("flagged")) {
			result = SimulatorVariables.valueOf("PASSED_WITH_FLAG").toString();
		} else if (sb.toString().contains("Passed") ||sb.toString().contains("passed")) {
			result = SimulatorVariables.valueOf("PASSED").toString();
		} else if (sb.toString().contains("Aborted") ||sb.toString().contains("aborted")) {
			result = SimulatorVariables.valueOf("ABORTED").toString();
			
		}else {
			result = SimulatorVariables.valueOf("PASSED").toString();	
		}
		return result;
	}

	@Override
	public void run(HL7Client client)
			throws ParseException, LLPException, InterruptedException, IOException, HL7Exception {
		// TODO Auto-generated method stub
		
	}
}
