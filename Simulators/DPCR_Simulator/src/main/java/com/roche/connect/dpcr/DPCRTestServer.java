/*package com.roche.connect.dpcr;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.connect.dpcr.lis.bl.OrderIdGenerator;
import com.roche.connect.dpcr.mpx.bl.Sample;
import com.roche.connect.dpcr.util.Mp96RunData;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionListener;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.message.ORM_O01;
import ca.uhn.hl7v2.model.v26.message.QBP_Q11;
import ca.uhn.hl7v2.model.v26.segment.MSA;
import ca.uhn.hl7v2.model.v26.segment.MSH;
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

public class DPCRTestServer {

	public class QueryHandler implements ReceivingApplication {
		private final Logger log = LoggerFactory.getLogger(QueryHandler.class);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		  private static final char CARRIAGE_RETURN = 13;

		private List<String> samples;
		private List<Sample> sampleList;
		private HapiContext context;
		private Message message = null;
		private Mp96RunData mp96TestData;

		public Message getMessage() {
			return message;
		}

		public QueryHandler( HapiContext context) {
			
			this.context = context;
		}

		public boolean canProcess(Message theIn) {
			return true;
		}

		private String getRandomTestType() throws IOException {
			Random random;
			String type;

			ArrayList<String> testTypeList = new ArrayList<>();
			try (BufferedReader reader = new BufferedReader(new FileReader(MainApp.getFilepath("TestTypes")))) {

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
			System.out.println("QueryString :"+queryString);
			log.info(queryString, "Query message:\n", "\n\n");
			QBP_Q11 queryMessage = (QBP_Q11) theMessage;

			try {

				ObjectMapper mapper = new ObjectMapper();
				//mp96TestData= mapper.readValue(new File("MP96TestData"), Mp96TestData.class);
				String currentTime = dateFormat.format(new Date()).toString();
				 InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/ORM_001.txt");
			        String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
			        Parser p = this.context.getPipeParser();
			        ORM_O01 queryMessage1 = (ORM_O01) p.parse(msg);
			        String universalIdType="M";
			        String messageControlId = UUID.randomUUID().toString().substring(0, 20);
			        String obx5ObservationValue="01";
			        String obxPassedValue="Passed";
			        MSH msh = queryMessage1.getMSH();
			        System.out.println("ORM before :" +queryMessage1.toString() );
					
					 queryMessage1.getMSH().getMsh7_DateTimeOfMessage().getTs1_TimeOfAnEvent().setValue(currentTime);
					 queryMessage1.getMSH().getMsh10_MessageControlID().setValue(msh.getMsh10_MessageControlID().getValue());

					 System.out.println("ORM after :" +queryMessage1.toString() );
				 
				return queryMessage1;
			} catch (IOException e) {
				throw new HL7Exception(e);
			}

		}
		
		private String convertHl7toString(String message) throws IOException {
	    	
	    	 InputStream messageResource = new ByteArrayInputStream(message.getBytes());
	    	 
		     String msg = CharStreams.toString(new InputStreamReader(messageResource, Charsets.UTF_8)).replace('\n', '\r');
	    	
			return msg;
	    	
	    	
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

		@Override
		public Message processMessage(Message arg0, Map arg1) throws ReceivingApplicationException, HL7Exception {
			// TODO Auto-generated method stub
			return null;
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
			System.out.println(theMessage);
			
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

	public class AckHandler implements ReceivingApplication {
		private final Logger logg = LoggerFactory.getLogger(AckHandler.class);
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
	private AckHandler ackHandler;

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
	
	public AckHandler getAckHandler() {
		return ackHandler;
	}

	public static void main(String[] args) throws Exception {
		DPCRTestServer server = new DPCRTestServer();
		server.start();
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(DPCRTestServer.class);
	private HapiContext context;
	private List<String> samples;
	private List<Sample> sampleList;

	public DPCRTestServer() {
		this.orderIdGenerator = new OrderIdGenerator();
		this.samples = new ArrayList<>();
		this.sampleList = new ArrayList<>();
		this.context = new DefaultHapiContext();
		this.resultHandler = new ResultHandler();
		this.queryHandler = new QueryHandler(context);
		
	}

	public void start() throws InterruptedException, HL7Exception {
		int port = 4444;

		server = context.newServer(port, false);
		queryHandler = new QueryHandler(this.context);
		resultHandler = new ResultHandler();
		server.registerApplication("QBP", "Q11", queryHandler);
		server.registerApplication("OUL", "R21", resultHandler);
		server.registerApplication("ACK", "", ackHandler);
		server.registerConnectionListener(new MyConnectionListener());
		server.setExceptionHandler(new MyExceptionHandler());
		server.startAndWait();
		LOGGER.debug("shutdown");

	}

}
*/