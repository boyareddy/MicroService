package com.roche.swam.labsimulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.swam.labsimulator.lis.bl.OrderIdGenerator;
import com.roche.swam.labsimulator.mpx.bl.Sample;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionListener;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v251.message.QBP_Q11;
import ca.uhn.hl7v2.model.v251.message.RSP_K11;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.QAK;
import ca.uhn.hl7v2.model.v251.segment.QPD;
import ca.uhn.hl7v2.model.v251.segment.SAC;
import ca.uhn.hl7v2.model.v251.segment.SPM;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;
import ca.uhn.hl7v2.protocol.ReceivingApplicationExceptionHandler;

class MyConnectionListener implements ConnectionListener {
	private static final Logger log = LoggerFactory.getLogger(MyConnectionListener.class);

	public void connectionReceived(Connection theC) {
		String recievedConenction = theC.getRemoteAddress().toString();
		log.info(recievedConenction, "New connection received");
	}

	public void connectionDiscarded(Connection theC) {
		String lostConenction = theC.getRemoteAddress().toString();
		log.info(lostConenction, "Lost connection");
	}
}

class MyExceptionHandler implements ReceivingApplicationExceptionHandler {

	public String processException(String theIncomingMessage, Map<String, Object> theIncomingMetadata,
			String theOutgoingMessage, Exception theE) throws HL7Exception {
		return theOutgoingMessage;
	}
}

public class MpxTestServer {

	public class QueryHandler implements ReceivingApplication {
		private final Logger log = LoggerFactory.getLogger(QueryHandler.class);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		private List<String> samples;
		private List<Sample> sampleList;
		private HapiContext context;
		private Message message = null;

		public Message getMessage() {
			return message;
		}

		public QueryHandler(List<String> samples, List<Sample> sampleList, HapiContext context) {
			this.samples = samples;
			this.sampleList = sampleList;
			this.context = context;
		}

		public boolean canProcess(Message theIn) {
			return true;
		}

		private String getRandomTestType() throws IOException {
			Random random;
			String type;

			ArrayList<String> testTypeList = new ArrayList<>();
			try (BufferedReader reader = new BufferedReader(
					new FileReader(MainApp.getFilepath("TestTypes")))) {

				while ((type = reader.readLine()) != null) {
					testTypeList.add(type);
				}

			} catch (FileNotFoundException e) {
				log.error(e.getMessage(), "Exception occured :");
			}
			for (String types : testTypeList) {
				// log.info(types, "the test type is :");
			}
			String[] testType = testTypeList.toArray(new String[0]);
			random = new Random();
			final int N = testType.length;
			return testType[random.nextInt(N)];
		}

		public Message processMessage(Message theMessage, Map<String, Object> theMetadata)
				throws ReceivingApplicationException, HL7Exception {
			String queryString = theMessage.toString().replace('\r', '\n');

			log.info(queryString, "Query message:\n", "\n\n");
			QBP_Q11 queryMessage = (QBP_Q11) theMessage;
			Type sampleIdSeg = queryMessage.getQPD().getQpd3_UserParametersInsuccessivefields().getData();
			String sampleId = sampleIdSeg.encode();
			this.samples.add(sampleId);

			MSH msh = (MSH) queryMessage.get("MSH");
			QPD qpdSegment = (QPD) queryMessage.get("QPD");
			Sample sample = new Sample();
			sample.setSampleId(sampleId);
			sample.setSendingFacilityName(msh.getMsh4_SendingFacility().getHd1_NamespaceID().getValue());
			sample.setInstrumentId(msh.getMsh3_SendingApplication().getHd2_UniversalID().getValue());
			sample.setReceivingFacilityName(msh.getReceivingFacility().getNamespaceID().getValue());
			sample.setSendingApplicationName(msh.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
			sample.setReceivingApplicationName(msh.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
			sample.setMessageControlId(msh.getMsh10_MessageControlID().getValue());
			String queryMessageName = qpdSegment.getQpd1_MessageQueryName().getCe1_Identifier().getValue() + "^"
					+ qpdSegment.getQpd1_MessageQueryName().getCe2_Text() + "^"
					+ qpdSegment.getQpd1_MessageQueryName().getCe3_NameOfCodingSystem().getValue();
			this.sampleList.add(sample);
			try {
				InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/RSP_K11.txt");
				String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
				Parser p = this.context.getPipeParser();
				RSP_K11 queryMessage1 = (RSP_K11) p.parse(msg);

				MSH msh1 = (MSH) queryMessage1.get("MSH");
				MSA msa = (MSA) queryMessage1.get("MSA");
				SPM spm = (SPM) queryMessage1.get("SPM");
				ORC orc = (ORC) queryMessage1.get("ORC");
				OBR obr = (OBR) queryMessage1.get("OBR");
				SAC sac = (SAC) queryMessage1.get("SAC");
				QPD qpd = (QPD) queryMessage1.get("QPD");
				QAK qak = (QAK) queryMessage1.get("QAK");
				String currentTime = dateFormat.format(new Date()).toString();
				qak.getMessageQueryName().parse(queryMessageName);
				msh1.getMsh7_DateTimeOfMessage().getTs1_Time().setValue(currentTime);
				msh1.getMsh3_SendingApplication().getHd1_NamespaceID().setValue(sample.getReceivingApplicationName());
				msh1.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(sample.getSendingFacilityName());
				msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().setValue(sample.getReceivingFacilityName());
				msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().setValue(sample.getSendingApplicationName());
				msh1.getMsh5_ReceivingApplication().getHd2_UniversalID().setValue(sample.getInstrumentId());
				msh1.getMsh5_ReceivingApplication().getHd3_UniversalIDType().setValue("M");
				msh1.getReceivingFacility().getNamespaceID().setValue(sample.getReceivingFacilityName());
				msh1.getMsh10_MessageControlID().setValue(sample.getMessageControlId());
				msa.getMsa2_MessageControlID().setValue(sample.getMessageControlId());
				qpd.getQpd2_QueryTag().parse(sample.getMessageControlId());
				qpd.getQpd3_UserParametersInsuccessivefields().parse(sample.getSampleId());
				sac.getRegistrationDateTime().getTs1_Time().setValue(currentTime);
				sac.getContainerIdentifier().parse(String.valueOf(sample.getSampleId()));
				spm.getSetIDSPM().setValue("1");
				spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier()
						.setValue(sample.getSampleId());
				spm.getSpecimenType().parse(this.getSampleTyep());
				spm.getSpm11_SpecimenRole(0).getCwe1_Identifier().setValue("P");
				spm.getSpecimenDescription(0)
						.setValue(this.getSpecimenDescription(spm.getSpecimenType().getCwe1_Identifier().getValue()));
				orc.getOrc1_OrderControl().setValue("NW");
				orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(sample.getSampleId());
				orc.getOrc5_OrderStatus().setValue("SC");
				orc.getOrc9_DateTimeOfTransaction().getTs1_Time().setValue(currentTime);
				obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(this.getRandomTestType());
				obr.getObr4_UniversalServiceIdentifier().getCe3_NameOfCodingSystem().setValue("99ROC");
				return queryMessage1;
			} catch (IOException e) {
				throw new HL7Exception(e);
			}

		}

		public String getSampleTyep() {
			Random ran = new Random();
			String[] sampleType = { "PLAS^Plasma^HL70487", "WB^Whole Blood^HL70487" };
			final int count = sampleType.length;
			return sampleType[ran.nextInt(count)];

		}

		private String getSpecimenDescription(String sampleType) {
			String specimenDescription = sampleType.contains("Plasma") ? "Plasma" : " Whole Blood";
			return specimenDescription;
		}

	}

	public class ResultHandler implements ReceivingApplication {
		private final Logger logg = LoggerFactory.getLogger(ResultHandler.class);
		private Message message = null;

		public Message getMessage() {
			return message;
		}

		public boolean canProcess(Message theIn) {
			return true;
		}

		public Message processMessage(Message theMessage, Map<String, Object> theMetadata)
				throws ReceivingApplicationException, HL7Exception {
			String reString = theMessage.toString().replace('\r', '\n');
			try {
				Message ackMessage = theMessage.generateACK();
				MSH ackMsh = (MSH) ackMessage.get("MSH");
				MSA ackMsa = (MSA) ackMessage.get("MSA");
				String charSet = "UNICODE UTF-8";
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				String currentTime = dateFormat.format(new Date()).toString();
				String msgContorlId = ackMsa.getMsa2_MessageControlID().getValue();
				ackMsh.getCharacterSet(0).parse(charSet);
				ackMsh.getDateTimeOfMessage().parse(currentTime);
				ackMsh.getMessageControlID().setValue(msgContorlId);

				return ackMessage;
			} catch (IOException e) {
				throw new HL7Exception(e);
			}
		}

	}

	@SuppressWarnings("unused")
	private OrderIdGenerator orderIdGenerator;
	private HL7Service server;
	private ResultHandler resultHandler;
	private QueryHandler queryHandler;

	public HL7Service getServer() {
		return server;
	}

	public void setServer(HL7Service server) {
		this.server = server;
	}

	public ResultHandler getResultHandler() {
		return resultHandler;
	}

	public QueryHandler getQueryHandler() {
		return queryHandler;
	}

	public static void main(String[] args) throws Exception {
		MpxTestServer server = new MpxTestServer();
		server.start();
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(MpxTestServer.class);
	private HapiContext context;
	private List<String> samples;
	private List<Sample> sampleList;

	public MpxTestServer() {
		this.orderIdGenerator = new OrderIdGenerator();
		this.samples = new ArrayList<>();
		this.sampleList = new ArrayList<>();
		this.context = new DefaultHapiContext();
		this.resultHandler = new ResultHandler();
		this.queryHandler = new QueryHandler(samples, sampleList, context);
	}

	public void start() throws InterruptedException, HL7Exception {
		int port = 4444;

		server = context.newServer(port, false);
		queryHandler = new QueryHandler(this.samples, this.sampleList, this.context);
		resultHandler = new ResultHandler();
		server.registerApplication("QBP", "WOS", queryHandler);
		server.registerApplication("SSU", "U03", resultHandler);

		server.registerConnectionListener(new MyConnectionListener());
		server.setExceptionHandler(new MyExceptionHandler());
		server.startAndWait();
		LOGGER.debug("shutdown");

	}

}
