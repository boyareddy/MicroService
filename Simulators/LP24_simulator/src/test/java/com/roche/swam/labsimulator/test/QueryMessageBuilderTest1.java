package com.roche.swam.labsimulator.test;

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
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.mpx.bl.QueryMessageBuilder;


@RunWith(MockitoJUnitRunner.class)
public class QueryMessageBuilderTest1 {

	@InjectMocks
	QueryMessageBuilder queryBuilder;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		 MainApp.main(new String[] { "data.json" });
	}

	@Test // 2.Write test code to see whether a given JSON and Properties object formed are matching
	public void jsonPropertyTest() throws IOException, ParseException {
			JSONObject jsonObject = getProperties();
			Assert.assertEquals("MagnaPure24",jsonObject.get("sendingApplication").toString());
			Assert.assertEquals("Roche Diagnostics",jsonObject.get("sendingFacility").toString() );
			Assert.assertEquals("Connect", jsonObject.get("receivingApplication").toString());
			Assert.assertEquals("Roche Diagnostics",jsonObject.get("receivingFacility").toString() );
			Assert.assertEquals("QBP^Q11^QBP_Q11",jsonObject.get("messageType").toString() );
			Assert.assertEquals("WOS", jsonObject.get("messageQueryName").toString());
			Assert.assertEquals("P",jsonObject.get("processingId").toString() );
			Assert.assertEquals("2.5.1",jsonObject.get("versionId").toString() );
			Assert.assertEquals("UTF-8", jsonObject.get("characterSet").toString());
		
	}
	
	public JSONObject getProperties() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		FileReader fileReader = null;
			Object object =null;
				fileReader = new FileReader(new File("data.json"));
				object = parser.parse(fileReader);
				fileReader.close();
			// convert Object to JSONObject
			JSONObject jsonObject = (JSONObject) object; 
			return jsonObject;
	}
	
	
	/*@Test // 3.Inject Mock Properties with some values and see whether Query object is matching the same
	public void buildTest() throws HL7Exception, IOException, ParseException {
		Message queryMsg = queryBuilder.build(null);
		MSH mshSegment = ((QBP_Q11) queryMsg).getMSH();
		QPD qpdSegment = ((QBP_Q11) queryMsg).getQPD();
		Assert.assertEquals("MagnaPure24",mshSegment.getSendingApplication().getNamespaceID().getValue());
		Assert.assertEquals("WOS", qpdSegment.getMessageQueryName().getCe1_Identifier().getValue());
		
	}*/
	
}
