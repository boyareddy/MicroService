package com.roche.swam.labsimulator.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.roche.swam.labsimulator.MpxTestServer;
import com.roche.swam.labsimulator.MpxTestServer.ResultHandler;
import com.roche.swam.labsimulator.lis.bl.OrderIdGenerator;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.message.RSP_K11;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.QPD;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

@RunWith(MockitoJUnitRunner.class)
// @SuiteClasses(ResultHandler.class,QueryHandler.class)
public class MpxTestServerTest {

	/*String expectedMsg = "MSH|^~\\&|LIS|LaboratoryABC|MagNaPure24|LaboratoryABC|20180713133903.299+0530||ACK^U03^ACK|95406|P|2.5.1\r"
			+ "MSA|AA|1b13763f-629c-428d-a6eb-14f269450f09";*/
	String expAcknowledge = "MSH|^~\\&|Connect|Roche Diagnostics|MagnaPure24|Roche Diagnostics|20180718115914.025+0530||ACK^U03^ACK|121801|P|2.5.1||||||UTF-8\r\n" + 
			"MSA|AA|1561be1c-0bff-4cb6-9ecc-077229a73208";
	

	@InjectMocks
	MpxTestServer mpxServer;

	@Mock
	OrderIdGenerator orderIdGenerator;

	Map<String, Object> map;
	Message m  = null;
	
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test // 6. Inject Mock HL7 Ack and check whether internal Ack object is matching
	public void testAcknowledgement() throws NoSuchMethodException, SecurityException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			HL7Exception, ReceivingApplicationException {

		String s = "MSH|^~\\&|MagnaPure24^MP001-12^M|Roche Diagnostics|Connect|Roche Diagnostics|20180716154114||SSU^U03^SSU_U03|1561be1c-0bff-4cb6-9ecc-077229a73208|P|2.5.1||||||UNICODE?UTF-8\r\n"
				+ "EQU|MP001-12|20180716154114|OP\r\n" + "SAC|||Strip1|||||R|9mmStrip|Strip1|2||||||||||12|10||ul\r\n"
				+ "OBX|1|CE|P^^99ROC|1|ctDNA-P1||||||F\r\n" + "OBX|2|CE|PV^^99ROC|1|01||||||F\r\n"
				+ "OBX|3|CE|RES^^99ROC|1|Passed||||||F\r\n"
				+ "OBX|4|DR|RuntimeRange^RunExecutionTimeRange^99ROC|1|20180716154109^20180716154114||||||F\r\n"
				+ "NTE|1||Comment on the sample result\r\n"
				+ "SPM|1|Strip1_2|173964230|WB^Whole Blood^HL70487|||||||P|||Whole Blood\r\n"
				+ "OBX|1|CE|OrderName^^99ROC|1|Order123|||F38|||F|||||jimenj15\r\n"
				+ "OBX|2|CE|OrderResult^^99ROC|1|Passed||||||F\r\n" + "OBX|3|CE|IC^^99ROC|1|74586563||||||F\r\n";
		m = new DefaultHapiContext().getPipeParser().parse(s);
		map = new HashMap<>();
		map.put("/MSH-10", "1561be1c-0bff-4cb6-9ecc-077229a73208");
		map.put("SENDING_PORT", "59842");
		map.put("raw-message",
				"MSH|^~\\&|MagnaPure24^MP001-12^M|Roche Diagnostics|Connect|Roche Diagnostics|20180716154114||SSU^U03^SSU_U03|1561be1c-0bff-4cb6-9ecc-077229a73208|P|2.5.1||||||UNICODE?UTF-8\r\n"
						+ "EQU|MP001-12|20180716154114|OP\r\n"
						+ "SAC|||Strip1|||||R|9mmStrip|Strip1|2||||||||||12|10||ul\r\n"
						+ "OBX|1|CE|P^^99ROC|1|ctDNA-P1||||||F\r\n" + "OBX|2|CE|PV^^99ROC|1|01||||||F\r\n"
						+ "OBX|3|CE|RES^^99ROC|1|Passed||||||F\r\n"
						+ "OBX|4|DR|RuntimeRange^RunExecutionTimeRange^99ROC|1|20180716154109^20180716154114||||||F\r\n"
						+ "NTE|1||Comment on the sample result\r\n"
						+ "SPM|1|Strip1_2|173964230|WB^Whole Blood^HL70487|||||||P|||Whole Blood\r\n"
						+ "OBX|1|CE|OrderName^^99ROC|1|Order123|||F38|||F|||||jimenj15\r\n"
						+ "OBX|2|CE|OrderResult^^99ROC|1|Passed||||||F\r\n"
						+ "OBX|3|CE|IC^^99ROC|1|74586563||||||F\r\n");
		map.put("SENDING_IP", "127.0.0.1");
		// Assert.assertEquals(mpxServer.getResultHandler().processMessage(m, map),
		// expectedMsg);
		m = mpxServer.getResultHandler().processMessage(m, map);
		ACK ack = (ACK) m;
		Assert.assertEquals("Roche Diagnostics", ack.getMSH().getSendingFacility().getNamespaceID().getValue());
		Assert.assertEquals("AA", ack.getMSA().getAcknowledgmentCode().getValue());
	}

	@Test
	public void testStart() throws HL7Exception, InterruptedException, IOException, URISyntaxException, LLPException {
		mpxServer.start();
		Assert.assertEquals(mpxServer.getServer().isRunning(), true);
	}

	@Test // 4.Inject Mock HL7 Response and see whether internal Query Response object and actions are matching the same
	public void testResponse() throws HL7Exception, ReceivingApplicationException {
		String s = "MSH|^~\\&|MagnaPure24^MP001-12^M|Roche Diagnostics|Connect|Roche Diagnostics|20180718102819||QBP^WOS^QBP_Q11|97e898b8-fcac-440a-a265-35b198c8f841|P|2.5.1||||||UNICODE?UTF-8\r\n" + 
				"QPD|WOS^Work Order Step^IHE_LABTF|97e898b8-fcac-440a-a265-35b198c8f841|sampleID1\r\n" + 
				"RCP|I||R\r\n";
		String value = "{/MSH-10=97e898b8-fcac-440a-a265-35b198c8f841, SENDING_PORT=57463, raw-message=MSH|^~\\&|MagnaPure24^MP001-12^M|Roche Diagnostics|Connect|Roche Diagnostics|20180718102819||QBP^WOS^QBP_Q11|97e898b8-fcac-440a-a265-35b198c8f841|P|2.5.1||||||UNICODE?UTF-8\r\n" + 
				"QPD|WOS^Work Order Step^IHE_LABTF|97e898b8-fcac-440a-a265-35b198c8f841|sampleID1\r\n" + 
				"RCP|I||R\r\n" + 
				", SENDING_IP=127.0.0.1}";
		value = value.substring(1, value.length()-1);           //remove curly brackets
		String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
		 map = new HashMap<>();               
		for(String pair : keyValuePairs) {                        //iterate over the pairs
		    String[] entry = pair.split("=");                   //split the pairs to get key and value 
		    map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
		}
		 m = mpxServer.getQueryHandler().processMessage(new DefaultHapiContext().getPipeParser().parse(s), map);
		 RSP_K11 rsp = (RSP_K11) m;
		 MSH mshSegment = (MSH) rsp.get("MSH");
		 QPD qpdSegment =  (QPD) rsp.get("QPD");
		//Assert.assertEquals("2.5.1", mshSegment.getVersionID().getVid1_VersionID().getValue());
		Assert.assertEquals("97e898b8-fcac-440a-a265-35b198c8f841", mshSegment.getMessageControlID().getValue());
		Assert.assertEquals("sampleID1", qpdSegment.getQpd3_UserParametersInsuccessivefields().getData().toString());
	}
}
 