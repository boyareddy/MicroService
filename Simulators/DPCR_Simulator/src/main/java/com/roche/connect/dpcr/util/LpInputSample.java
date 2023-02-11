package com.roche.connect.dpcr.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.roche.connect.dpcr.sim.bl.Sample;

@JsonIgnoreProperties({"status","test","sampleType","run","protocolType","dateTime","messageTyep","messageControlId","instrumentId","sendingApplicationName","sampleId","orderId","sampleDescription"})
public class LpInputSample extends Sample{
    
	
    
}

