package com.roche.swam.labsimulator.test;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.common.bl.hl7.HL7Client;
import com.roche.swam.labsimulator.mpx.bl.EnumSampleStatus;
import com.roche.swam.labsimulator.mpx.bl.QueryMessageSender;
import com.roche.swam.labsimulator.mpx.bl.Run;
import com.roche.swam.labsimulator.mpx.bl.Sample;
import com.roche.swam.labsimulator.mpx.bl.SampleRepository;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;

@RunWith(MockitoJUnitRunner.class)
public class QueryMessageSenderTest {
	
	@InjectMocks 
	QueryMessageSender queryMessageSender;
	
	@Mock
	ApplicationEventPublisher publisher;

	@Mock
	SampleRepository sampleRepository;


	@Spy
	HL7Client client;

	@Mock
	Run run;
	
@Before
	
	public void set() throws HL7Exception{
	try {
			client.connect("localhost",4444);
				
			MainApp.main(new String[] { "data.json" });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

@Test

public void testRun() throws HL7Exception, LLPException, IOException, ParseException {
	Sample sample = new Sample();
	sample.setRun("ID");
	sample.setSampleId("123");
	sample.setInstrumentId("TEST");
	sample.setSampleType("BLOD");
	sample.setOutputContainerId("234");
	Collection<Sample> loadedSamples = new ArrayList<>();
	loadedSamples.add(sample);
		when(sampleRepository.getAllInState(null, EnumSampleStatus.LOADED)).thenReturn(loadedSamples);
		queryMessageSender.run(client, null);
		Assert.assertEquals(EnumSampleStatus.QUERYING,sample.getStatus());
}


}
