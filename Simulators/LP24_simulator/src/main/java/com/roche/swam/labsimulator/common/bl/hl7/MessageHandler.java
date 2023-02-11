package com.roche.swam.labsimulator.common.bl.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

import java.io.IOException;


public interface MessageHandler<T extends  Message> {
    void handle(T message, HL7Client client) throws HL7Exception, IOException;
}
