/*package com.roche.swam.labsimulator.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.annotation.DirtiesContext;

import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.mpx.bl.QueryMessageBuilder;
import com.roche.swam.labsimulator.mpx.bl.Sample;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v251.datatype.HD;
import ca.uhn.hl7v2.model.v251.message.QBP_Q11;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.QPD;
import ca.uhn.hl7v2.model.v251.segment.RCP;

@RunWith(MockitoJUnitRunner.class)
public class QueryMessageBuilderTest {

	public static String configFilePath="C:\\Users\\gosula.r\\Documents\\rocheconnectdevelopment\\sourcecode\\simulator\\src\\main\\resources\\data.json";
	@InjectMocks QueryMessageBuilder queryMsgBuilder;
	//@InjectMocks JsonPropertyReader jsonParser = new JsonPropertyReader();

	@Before
	public void setUp() {
		 try {
			MainApp.main(new String[] { "C:\\Users\\gosula.r\\Documents\\rocheconnectdevelopment\\sourcecode\\simulator\\src\\main\\resources\\data.json" });
		Assert.assertEquals("new String[] { \"C:\\Users\\gosula.r\\Documents\\rocheconnectdevelopment\\sourcecode\\simulator\\src\\main\\resources\\data.json\" ", "C:\\Users\\gosula.r\\Documents\\rocheconnectdevelopment\\sourcecode\\simulator\\src\\main\\resources\\data.json\\");
		 } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void buildTest()  {
		try
		{
			Sample queryBuilderBean = parseJsonToSample();
			QBP_Q11 qbpMsg = queryMsgBuilder.build(queryBuilderBean); 
			//validate MSH segment
			MSH mshSegment = qbpMsg.getMSH();
			HD hd = mshSegment.getSendingFacility();
			Assert.assertEquals(mshSegment.getSendingFacility().getNamespaceID().toString(), queryBuilderBean.getSendingFacilityName());
			Assert.assertEquals(mshSegment.getSendingApplication().getNamespaceID().toString(), queryBuilderBean.getSendingApplicationName());
			Assert.assertEquals(mshSegment.getReceivingApplication().getNamespaceID().toString(), queryBuilderBean.getReceivingApplicationName());
			Assert.assertEquals(mshSegment.getMessageControlID().getValue(), queryBuilderBean.getMessageControlId());
			Assert.assertEquals(mshSegment.getReceivingFacility().getNamespaceID().getValue(), queryBuilderBean.getReceivingFacilityName());
			Assert.assertEquals(mshSegment.getProcessingID().getPt1_ProcessingID().getValue(), queryBuilderBean.getProcessingId());
			Assert.assertEquals(mshSegment.getVersionID().getVid1_VersionID().getValue(), queryBuilderBean.getVersionId());
			//validate QPD segment
			QPD qpdSegment = qbpMsg.getQPD();
			Assert.assertEquals(qpdSegment.getQpd1_MessageQueryName().getCe1_Identifier().getValue(), queryBuilderBean.getMessageQueryName());
			Assert.assertEquals(qpdSegment.getQpd1_MessageQueryName().getCe3_NameOfCodingSystem().getValue(), "IHE_LABTF");
			Assert.assertEquals(qpdSegment.getQpd1_MessageQueryName().getCe2_Text().getValue(), "Work Order Step");
			Assert.assertEquals(qpdSegment.getQpd3_UserParametersInsuccessivefields().getData().toString(), queryBuilderBean.getSampleId());
			Assert.assertEquals(qpdSegment.getQpd2_QueryTag().getValue(), queryBuilderBean.getMessageControlId());
			//validate RCP Segment
			RCP rcpSegment = qbpMsg.getRCP();
			Assert.assertEquals(rcpSegment.getQueryPriority().getValue(), "I");
			Assert.assertEquals(rcpSegment.getRcp3_ResponseModality().getCe1_Identifier().getValue(), "R");
		} catch (ParseException e) {
            Assert.fail("ParseException");
        } catch (IOException e) {
        	Assert.fail("IOException");
        } catch (HL7Exception e) {
        	Assert.fail("HL7Exception");
        }
	}
	
	private Sample parseJsonToSample() {
		JSONParser parser = new JSONParser();
		FileReader fileReader = null;
			Object object =null;
			try {
				//fileReader = new FileReader(MainApp.configFilePath);
				fileReader = new FileReader(new File( "C:\\Users\\karnati.u\\Desktop\\MP24Sim_sampleCodes\\roche-swam-labsimulator\\src\\main\\resources\\data.json"));
				object = parser.parse(fileReader);
				fileReader.close();
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
			// convert Object to JSONObject
			JSONObject jsonObject = (JSONObject) object;
			return readJsonData(jsonObject);
		
	}
	
	
		private Sample readJsonData(JSONObject jsonObject) {
				Sample qBean = new Sample();
				//JSONParser parser = new JSONParser();
				try {

					String sendingApplication = (String) jsonObject.get("SendingApplication");
					String sendingFacility = (String) jsonObject.get("SendingFacility");
					String receivingApplication = (String) jsonObject.get("ReceivingApplication");
					String receivingFacility = (String) jsonObject.get("ReceivingFacility");
					String messageType = (String) jsonObject.get("MessageType");
					String messageControlId = (String) jsonObject.get("MessageControlId");
					String processingId = (String) jsonObject.get("ProcessingId");
					String versionId = (String) jsonObject.get("VersionId");
					String characterSet = (String) jsonObject.get("CharacterSet");
					String messageQueryName = (String) jsonObject.get("MessageQueryName");
					// String delayBetweenQuery = (String) jsonObject.get("DelayBetweenQuery");

					qBean.setSendingApplicationName(sendingApplication);
					qBean.setSendingFacilityName(sendingFacility);
					qBean.setReceivingApplicationName(receivingApplication);
					qBean.setReceivingFacilityName(receivingFacility);
					qBean.setMessageTyep(messageType);
					qBean.setMessageControlId(messageControlId);
					qBean.setMessageQueryName(messageQueryName);
					qBean.setProcessingId(processingId);
					qBean.setVersionId(versionId);
					qBean.setCharacterSet(characterSet);
					// qBean.setDelayBetweenQuery(delayBetweenQuery);

				} catch (Exception e) {
					e.printStackTrace();
				}
				return qBean;

			} 
		 


}

*/