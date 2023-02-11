package com.roche.swam.labsimulator.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import com.roche.swam.labsimulator.common.bl.hl7.HL7Client;
import com.roche.swam.labsimulator.mpx.bl.EnumSampleStatus;
import com.roche.swam.labsimulator.mpx.bl.ResultMessageSender;
import com.roche.swam.labsimulator.mpx.bl.Run;
import com.roche.swam.labsimulator.mpx.bl.RunRepository;
import com.roche.swam.labsimulator.mpx.bl.Sample;
import com.roche.swam.labsimulator.mpx.bl.SampleRepository;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;

@RunWith(MockitoJUnitRunner.class)
public class ResultMessageSenderTest {

	@InjectMocks
	ResultMessageSender resultMessageSender;

	@Mock
	ApplicationEventPublisher publisher;

	@Mock
	SampleRepository sampleRepository;

	@Mock
	Connection connection;

	@Mock
	Initiator initiator;

	@Mock
	RunRepository runs;

	@Spy
	HL7Client client;

	@Mock
	Run run;

	@Mock
	Message message;


	
	/*@Mock
	private Sample sample;
	*/
	
	@Before
	public void setUp() throws HL7Exception{
		client.connect("localhost",4444);
	}
	@Test
	public void run() throws HL7Exception, LLPException, IOException, NumberFormatException, InterruptedException {
		//when(sampleRepository.getAllInState(null, EnumSampleStatus.RESULTS_AVAILABLE)).thenReturn(new ArrayList<>());
		//resultMessageSender.run(client);
		//mockSampleData();
		Sample sample = new Sample(); 
		sample.setRun("ID");
		sample.setSampleId("123");
		sample.setInstrumentId("TEST");
		sample.setSampleType("BLOD");
		sample.setOutputContainerId("234");
		Collection<Sample> loadedSamples = new ArrayList<>();
		loadedSamples.add(sample);
		when(sampleRepository.getAllInState(null, EnumSampleStatus.RESULTS_AVAILABLE)).thenReturn(loadedSamples);
		when(runs.getRun(anyString())).thenReturn(run);
		when(run.getStartTime()).thenReturn(Calendar.getInstance());
		when(run.getEndTime()).thenReturn(Calendar.getInstance());
		//when(client.getConnection()).thenReturn(connection);
		//when(connection.getInitiator()).thenReturn(initiator);
		//when(initiator.sendAndReceive(message)).thenReturn(message);
		
		resultMessageSender.run(client, null);
		Assert.assertEquals(EnumSampleStatus.FINISHED,sample.getStatus()); 
	}

//	private void mockSampleData() throws HL7Exception, LLPException, IOException {
//		Sample sample = new Sample();
//		sample.setRun("ID");
//		sample.setSampleId("123");
//		sample.setInstrumentId("TEST");
//		sample.setSampleType("BLOD");
//		sample.setOutputContainerId("234");
//		Collection<Sample> loadedSamples = new ArrayList<>();
//		loadedSamples.add(sample);
//		when(sampleRepository.getAllInState(null, EnumSampleStatus.RESULTS_AVAILABLE)).thenReturn(loadedSamples);
//		when(runs.getRun(anyString())).thenReturn(run);
//		when(run.getStartTime()).thenReturn(Calendar.getInstance());
//		when(run.getEndTime()).thenReturn(Calendar.getInstance());
//		when(client.getConnection()).thenReturn(connection);
//		when(connection.getInitiator()).thenReturn(initiator);
//		when(initiator.sendAndReceive(message)).thenReturn(message);
//		
//	}

}
