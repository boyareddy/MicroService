package com.roche.swam.labsimulator.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.roche.swam.labsimulator.mpx.bl.Sample;

@JsonIgnoreProperties({"status","test","sampleType","run","protocolType","dateTime","messageTyep","messageControlId","instrumentId","sampleId","orderId","sampleDescription"})
public class LpInputSample extends Sample{
    
	
    
}

