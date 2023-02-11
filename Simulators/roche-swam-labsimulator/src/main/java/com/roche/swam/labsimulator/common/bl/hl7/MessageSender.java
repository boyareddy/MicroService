package com.roche.swam.labsimulator.common.bl.hl7;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.roche.swam.labsimulator.mpx.bl.EnumSampleStatus;
import com.roche.swam.labsimulator.mpx.bl.SampleRepository;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;

public interface MessageSender{
    void run(HL7Client client,EnumSampleStatus sampleStatus) throws ParseException, LLPException, NumberFormatException, InterruptedException;
    void run(HL7Client client,SampleRepository samples,EnumSampleStatus sampleStatus) throws NumberFormatException, InterruptedException;
    public void run(HL7Client client) throws org.json.simple.parser.ParseException, LLPException,InterruptedException, IOException, HL7Exception;
}
