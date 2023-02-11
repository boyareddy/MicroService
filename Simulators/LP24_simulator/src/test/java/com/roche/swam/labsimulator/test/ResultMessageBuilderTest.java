package com.roche.swam.labsimulator.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import com.roche.swam.labsimulator.mpx.bl.ResultMessageBuilder;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.SSU_U03;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class ResultMessageBuilderTest {

	
	@InjectMocks
	ResultMessageBuilder resultBuilder;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	/*@Test
	public void buildSSUTest() throws HL7Exception, IOException {
		Message msg = resultBuilder.build(null, null);
		SSU_U03 ssuMsg = (SSU_U03) msg;
		MSH mshSegment = ssuMsg.getMSH();
		//Assert.assertEquals("", mshSegment.getMsh3_SendingApplication().getHd2_UniversalID());
	}*/
}
