package com.roche.connect.dpcr.common.bl.hl7;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.roche.connect.dpcr.util.ResultBean;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;

public interface MessageSender {
    void run() throws ParseException, LLPException, HL7Exception, IOException;
    void run(ResultBean resultBean, String ackCheck) throws HL7Exception, IOException;
    void run(ResultBean resultBean) throws HL7Exception, IOException;
    void run(String status) throws HL7Exception, IOException, NumberFormatException, InterruptedException;
}
