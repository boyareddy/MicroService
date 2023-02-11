
package com.roche.swam.labsimulator.mpx.bl;

import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
import com.roche.swam.labsimulator.lis.bl.SampleIdGenerator;
import com.roche.swam.labsimulator.util.SimulatorVariables;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;

@Component
public class test implements MessageSender {

	@Autowired
	private SampleRepository samples;
	@Autowired
	private RunRepository runs;
	@Autowired
	private ApplicationEventPublisher publisher;

	private static final Logger LOGGER = LoggerFactory.getLogger(test.class);
	

	public void run(HL7Client client, EnumSampleStatus sampleStatus) throws NumberFormatException, InterruptedException, IOException {
		FileWriter resultWriter = null;
		FileWriter acknowledgementWriter = null;
		LOGGER.info("inside QBS method of result message sender...");
		String u03msgs = "";
		int count = 0;
		try{	
			String deviceRunID = "RND"+SampleIdGenerator.getNext();
				ResultMessageBuilder builder = new ResultMessageBuilder();
				 resultWriter = new FileWriter(MainApp.getClasspath()+"/"+MainApp.getFilepath("ResultMessageTxt"));
				 acknowledgementWriter = new FileWriter(MainApp.getClasspath()+"/"+MainApp.getFilepath("AcknowledgeTxt"));
				Message ssuMessage = null;
				Run run = null;
				int ssuCount = Integer.parseInt(MainApp.getFilepath("NoOfSSU"));
				String runResult = setRunResultbySampleResults(sampleStatus);
				for(int i=1;i<=ssuCount;i++) {
					//Collection<Sample> samplesList = 
				for (Sample sample : this.samples.getAllInState(null, sampleStatus)) {
					
					
					System.out.println("SAMPLE:"+ sample.getSampleId()+" i:"+i+" status:"+sample.getResult());
					 run = this.runs.getRun(sample.getRun());
					run.finish();
					LOGGER.info(sample.getSampleId());
					LOGGER.info(sample.getSampleId()+""+sample.getInstrumentId()+""+sample.getSampleType()+""+sample.getProtocolType()+""+sample.getOutputContainerId()+""+sample.getOutputPosition());
					
					 ssuMessage = builder.build(sample,run,i,deviceRunID,runResult);
					resultWriter.write(ssuMessage.toString() + "\n\n");
					
					StringBuffer u03Message = new StringBuffer(ssuMessage.toString());
					u03msgs = u03msgs + "\u000b" + u03Message.toString() + "\u001c" + "\r";
					
					LOGGER.info("New U03 Message struct :::"+u03msgs);
					
//					try {
//						Connection connection = client.getConnection();
//						 Initiator initiator = connection.getInitiator();
//						 initiator.setTimeout(Integer.parseInt(MainApp.getFilepath("HAPITimeout")), TimeUnit.MILLISECONDS);
//						Message ackMessage = initiator.sendAndReceive(ssuMessage);
//						LOGGER.info(ackMessage.toString());
//						acknowledgementWriter.write(ackMessage.toString() + "\n\n");
//						
//					} catch (LLPException e) {
//						LOGGER.error("Closing HL7 connection :" +e.getLocalizedMessage());
//						System.exit(1);
//					}
					if(i==ssuCount) {
						LOGGER.info("Samples Finished:::::" + count++);
					sample.setStatus(EnumSampleStatus.FINISHED);
					this.publisher.publishEvent(new SampleChangedEvent(this, sample.getSampleId()));
					}
					
					Thread.sleep(Long.parseLong(MainApp.getFilepath("ResultMessageTimeInterval")));
				}
				LOGGER.info("Sample Count:::::"+count);
				LOGGER.info("New Message:::::"+u03msgs);
				try (Socket socket = new Socket(MainApp.getFilepath("AdapterHostName"),101)) {

					System.out.println("Connection established: " + socket.isConnected());
					socket.setSoTimeout(30000);
//					StringBuffer qbpQueryMessage = new StringBuffer(u03msgs);

					InputStream in = socket.getInputStream();
					DataInputStream dataIn = new DataInputStream(in);
					OutputStream out = socket.getOutputStream();
//					String qbpQueryMessageWrapper = "\u000b" + qbpQueryMessage.toString() + "\u001c" + "\r";
//					qbpQueryMessageWrapper = qbpQueryMessageWrapper + qbpQueryMessageWrapper;
					out.write(u03msgs.getBytes());
					int ackCount = 0;
					while(ackCount<=count) {
						System.out.println(u03msgs);
						
						acknowledgementWriter.write(dataIn.readUTF());
//						Thread.sleep(5000);
						ackCount++;
					}
				} catch(Exception e) {
					e.getStackTrace();
				}
				}
				String inputConId = MainApp.getFilepath("InputContainerIdEmpty");
				String inputPos = MainApp.getFilepath("InputContainerPositionEmpty");
				if ("true".equalsIgnoreCase(inputConId) || "true".equalsIgnoreCase(inputPos)) {
					ssuMessage = builder.build(inputConId, inputPos);
					LOGGER.info(ssuMessage.toString());
					resultWriter.write(ssuMessage.toString() + "\n\n");
					Connection connection = client.getConnection();
					 Initiator initiator = connection.getInitiator();
					 initiator.setTimeout(Integer.parseInt(MainApp.getFilepath("HAPITimeout")), TimeUnit.MILLISECONDS);
					Message ackMessage=null;
					try {
						ackMessage = initiator.sendAndReceive(ssuMessage);
					} catch (LLPException e) {
						// TODO Auto-generated catch block
						LOGGER.error(e.getLocalizedMessage());
					}
					LOGGER.info(ackMessage.toString());
					acknowledgementWriter.write(ackMessage.toString() + "\n\n");
				}
				
				
				
				LOGGER.info("after run method in result message sender...");
			} catch (IOException ioException) {
				LOGGER.error(ioException.getLocalizedMessage());
			} catch (HL7Exception hl7Exception) {
				LOGGER.error(hl7Exception.getLocalizedMessage());
			}finally {
				if(resultWriter!=null) {
					resultWriter.close();
				}
				if(acknowledgementWriter!=null) {
					acknowledgementWriter.close();
				}
				
			}
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
	public void run(HL7Client client, SampleRepository samples,EnumSampleStatus sampleStatus) throws NumberFormatException, InterruptedException, IOException {
		this.samples = samples;
		this.run(client,sampleStatus);

	}

	@Override
	public void run(HL7Client client)
			throws ParseException, LLPException, InterruptedException, IOException, HL7Exception {
		
	}
}
