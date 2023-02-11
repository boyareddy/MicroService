package com.roche.swam.labsimulator.common.bl.hl7;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.roche.swam.labsimulator.util.Mp96RunData;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;

public interface MessageSender {
    void run() throws ParseException, LLPException;
    void run(Mp96RunData mp96RunData, String ackCheck) throws HL7Exception, IOException;
    void run(Mp96RunData mp96RunData) throws HL7Exception, IOException;
}
