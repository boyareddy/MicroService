package com.roche.swam.labsimulator.mpx.bl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.common.bl.hl7.HL7Client;
import com.roche.swam.labsimulator.common.bl.hl7.MessageSender;
import com.roche.swam.labsimulator.lis.bl.SampleIdGenerator;
import com.roche.swam.labsimulator.util.JsonPropertyReader;
import com.roche.swam.labsimulator.util.LP24PostJsonPropertyReader;
import com.roche.swam.labsimulator.util.LP24PreJsonPropertyReader;
import com.roche.swam.labsimulator.util.LP24SeqPrepJsonPropertyReader;
import com.roche.swam.labsimulator.util.SamplesListBean;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.QAK;
import ca.uhn.hl7v2.model.v251.segment.SPM;

@Component
public class QueryMessageSender implements MessageSender {
	@Autowired
	private SampleIdGenerator sampleIdGenerator;
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryMessageSender.class);
	@Autowired
	private SampleRepository samples;
	@Autowired
	private ApplicationEventPublisher publisher;
	private HapiContext context;
	private SamplesListBean samplesListBean = new SamplesListBean();

	public void run(HL7Client client, EnumSampleStatus sampleStatus) throws ParseException, LLPException {
		LOGGER.info("inside QBS method");

		List<Sample> sampleList = new ArrayList<>();
		String deviceRunID = "ORD"+SampleIdGenerator.getNext(); 
		try (FileWriter queryWriter = new FileWriter(
				MainApp.getClasspath() + "/" + MainApp.getFilepath("QueryMessageTxt"), true)) {
			try (FileWriter responseWriter = new FileWriter(
					MainApp.getClasspath() + "/" + MainApp.getFilepath("ResponseMesssageTxt"), true)) {
				Collection<Sample> loadedSamples = this.samples.getAllInState(null, sampleStatus);
				QueryMessageBuilder builder = new QueryMessageBuilder();
				Message queryMessage = null;
				Message responseMessage = null;
				int timeDelay = 0;

				if ("LP-PRE-PCR".equalsIgnoreCase(MainApp.getDeviceName())) {
					timeDelay = getLpPreJson();

				} else if ("LP-POST-PCR".equalsIgnoreCase(MainApp.getDeviceName())) {
					timeDelay = getLpPosJson();
				} else if ("LP-SEQ-PP".equalsIgnoreCase(MainApp.getDeviceName())) {
					timeDelay = getLpSeqJson();
				} else {
					timeDelay = getMpJson();
				}

				ORC orc = null;
				OBR obr = null;
				SPM spm = null;
				
				
				String serial = sampleIdGenerator.getNext().substring(0, 3);
				
				for (Sample loadedSample : loadedSamples) {
				
				//String serialNum[] = loadedSample.getInstrumentId().split("\\-");
				//serialNum[1] = serial;
				//String serialnumber = serialNum[0];
				/*for (int i = 1; i < serialNum.length; i++)
					serialnumber += "-" + serialNum[i];*/
					loadedSample.setInstrumentId(loadedSample.getInstrumentId());
					//context = new DefaultHapiContext();
					if (builder.build(loadedSample) != null) {
						queryMessage = builder.build(loadedSample);
						queryWriter.write(queryMessage + "\n\n");

						LOGGER.info(queryMessage.toString());
						try {
							Connection connection = client.getConnection();
							Initiator initiator = connection.getInitiator();
							initiator.setTimeout(Integer.parseInt(MainApp.getFilepath("HAPITimeout")),
									TimeUnit.MILLISECONDS);
							responseMessage = initiator.sendAndReceive(queryMessage);
							responseWriter.write(responseMessage.toString() + "\n\n");

							LOGGER.info(responseMessage.toString());
						} catch (Exception e) {
							LOGGER.error("Closing HL7 connection: " + e.getLocalizedMessage());
							System.exit(1);
						}

						MSH msh = (MSH) queryMessage.get("MSH");
						loadedSample
								.setSendingFacilityName(msh.getMsh4_SendingFacility().getHd1_NamespaceID().getValue());
						loadedSample.setInstrumentId(msh.getMsh3_SendingApplication().getHd2_UniversalID().getValue());
						loadedSample.setReceivingFacilityName(msh.getReceivingFacility().getNamespaceID().getValue());
						loadedSample.setSendingApplicationName(
								msh.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
						loadedSample.setReceivingApplicationName(
								msh.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
						loadedSample.setMessageControlId(msh.getMsh10_MessageControlID().getValue());

						loadedSample.setStatus(EnumSampleStatus.QUERYING);
						System.out.println("before orc:" + responseMessage.toString());
						orc = (ORC) responseMessage.get("ORC");
						QAK qak = (QAK) responseMessage.get("QAK");
						MSA msa = (MSA) responseMessage.get("MSA");
						if ((qak.getQak2_QueryResponseStatus().getValue()).equalsIgnoreCase("OK") && "AA".equalsIgnoreCase(msa.getMsa1_AcknowledgmentCode().getValue())) {
							obr = (OBR) responseMessage.get("OBR");
							loadedSample.setProtocolType(
									obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().getValue());

						}
						spm = (SPM) responseMessage.get("SPM");
						loadedSample.setSampleDescription(spm.getSpecimenDescription(0).getValue());
						loadedSample.setOrderId(orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().getValue());
						if (StringUtils.isNotBlank(spm.getSpecimenType().getCwe1_Identifier().getValue())) {
							loadedSample.setSampleType(spm.getSpecimenType().getCwe1_Identifier() + "^"
									+ spm.getSpecimenType().getCwe2_Text().getValue() + "^"
									+ spm.getSpecimenType().getCwe3_NameOfCodingSystem().getValue());
						}

						this.publisher.publishEvent(new SampleChangedEvent(this, loadedSample.getSampleId()));
						try {
							Thread.sleep(timeDelay * 1000L);
						} catch (InterruptedException interruptedException) {
							LOGGER.error(interruptedException.getMessage());
							Thread.currentThread().interrupt();
						}

						if ((qak.getQak2_QueryResponseStatus().getValue()).equalsIgnoreCase("OK") && "AA".equalsIgnoreCase(msa.getMsa1_AcknowledgmentCode().getValue())) {
							loadedSample.setDeviceRunId(deviceRunID);
							sampleList.add(loadedSample);
						} else {
							loadedSample.setStatus(EnumSampleStatus.ABORTED);
						}
					}
					samplesListBean.setSamples(sampleList);
					ObjectMapper mapper = new ObjectMapper();
					mapper.writeValue(new File(MainApp.getFilepath(MainApp.getDeviceName())), samplesListBean);

					LOGGER.info("end of run method in query message sender........ ");
				}
			} catch (HL7Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void run(HL7Client client, SampleRepository samples, EnumSampleStatus sampleStatus) {

		/** restart purpsoe */

	}

	public int getMpJson() {

		JsonPropertyReader jsonParser = new JsonPropertyReader();

		return jsonParser.getTimeDelay();

	}

	public int getLpPreJson() {

		LP24PreJsonPropertyReader jsonParser = new LP24PreJsonPropertyReader();

		return jsonParser.getTimeDelay();

	}

	public int getLpPosJson() {

		LP24PostJsonPropertyReader jsonParser = new LP24PostJsonPropertyReader();

		return jsonParser.getTimeDelay();

	}

	public int getLpSeqJson() {

		LP24SeqPrepJsonPropertyReader jsonParser = new LP24SeqPrepJsonPropertyReader();

		return jsonParser.getTimeDelay();

	}

	@Override
	public void run(HL7Client client)
			throws ParseException, LLPException, InterruptedException, IOException, HL7Exception {
		// TODO Auto-generated method stub
		/* no code */
	}

}
