package com.roche.swam.labsimulator.common.bl.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

public interface HL7MessageHandler
{
    void handleMessage(Message message) throws HL7Exception;
}
