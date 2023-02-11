package com.roche.swam.labsimulator.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.roche.swam.labsimulator.mpx.bl.Consumables;
import com.roche.swam.labsimulator.mpx.bl.EnumSampleStatus;
import com.roche.swam.labsimulator.mpx.bl.ResultMessageBuilder;
import com.roche.swam.labsimulator.mpx.bl.Run;
import com.roche.swam.labsimulator.mpx.bl.Sample;

import ca.uhn.hl7v2.model.v251.message.SSU_U03;

@PowerMockIgnore({ "org.mockito.*" })
public class ResultMessageBuilderTest {
	@InjectMocks
	ResultMessageBuilder resultMessageBuilder = new ResultMessageBuilder();
	
	private static final Logger logger = LoggerFactory.getLogger(ResultMessageBuilderTest.class);
	
	Sample sample = null;
	Run run = null;
	@BeforeTest
	public void setUp() throws FileNotFoundException {
		MockitoAnnotations.initMocks(this);
		sample = getSample();
		run = getRun();
	}
	  @ObjectFactory
	    public IObjectFactory getObjectFactory() {
	        return new org.powermock.modules.testng.PowerMockObjectFactory();
	    }
	    
/*	@Test(priority=1)
	public void buildSSUTest() {
	    try {
	        SSU_U03 s = resultMessageBuilder.build(sample, run,1,"ORD860867096");
	        System.out.println(s);
        } catch (Exception e) {
           logger.error(e.getMessage());
        }
	}*/
	
	
	public Sample getSample() {
	    Sample s = new Sample();
	    s.setSampleId("0111994");
	    s.setProtocolType("ffPET");
	    s.setOrderId("0111994");
	    s.setStatus(EnumSampleStatus.RESULTS_AVAILABLE);
	    s.setRun("run");
	    s.setSampleType("PLAS^Plasma^HL70487");
	    s.setInstrumentId("MP001-642");
	    s.setOutputContainerId("123874402");
	    s.setOutputPosition("1");
	    s.setSendingFacilityName("Roche Diagnostics");
	    s.setReceivingFacilityName("Roche Diagnostics");
	    s.setMessageControlId("75931023-eee9-4365-8");
	    s.setSendingApplicationName("MagnaPure24");
	    s.setReceivingApplicationName("Connect");
	    s.setProcessingId("P");
	    s.setVersionId("2.5.1");
	    s.setCharacterSet("UTF-8");
	    s.setMessageQueryName("WOS");
	    s.setResult("Passed");
	    s.setProcessingCartridge("tipRack=;;00040");
	    s.setSampleComments("For testing");
	    s.setSampleDescription("Whole Blood");
	    s.setFlag("F1");
	    s.setConsumables(getConsumblesList());
	    return s;
	}
	
	public List<Consumables> getConsumblesList(){
	    List<Consumables> list = new ArrayList<>();
	    Consumables c = new Consumables();
        c.setConsumableName("MRReagentCassette");
        c.setValue("DNA Blood LV;00000000;0040");
        
        Consumables c1 = new Consumables();
        c1.setConsumableName("SR2mlReagentTube");
        c1.setValue("MGP;00000001;44556");
        
        Consumables c2 = new Consumables();
        c2.setConsumableName("SR2mlReagentTube");
        c2.setValue("MGP;00000001;44557");
        
        Consumables c3 = new Consumables();
        c3.setConsumableName("SR25mlReagentTube");
        c3.setValue("Washbuffer;00000002;11111");
        
        Consumables c4 = new Consumables();
        c4.setConsumableName("SR25mlReagentTube");
        c4.setValue("Washbuffer;00000002;21222");
        
        Consumables c5 = new Consumables();
        c5.setConsumableName("SR25mlReagentTube");
        c5.setValue("Specimen Diluent;00000000;23233");
        
        Consumables c6 = new Consumables();
        c6.setConsumableName("SR25mlReagentTube");
        c6.setValue("Specimen Diluent;00000000;31234");
        
        
        list.add(c);
        list.add(c1);
        list.add(c2);
        list.add(c3);
        list.add(c4);
        list.add(c5);
        list.add(c6);
        return list;
	}
	
	
	public Run getRun() {
	    Run r = new Run("1");
	    r.finish();
	    return r;
	}
 }
