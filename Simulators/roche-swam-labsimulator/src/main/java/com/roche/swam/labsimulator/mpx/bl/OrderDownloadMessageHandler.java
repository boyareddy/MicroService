package com.roche.swam.labsimulator.mpx.bl;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v251.message.RSP_K11;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.SPM;
import com.roche.swam.labsimulator.common.bl.hl7.HL7Client;
import com.roche.swam.labsimulator.common.bl.hl7.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrderDownloadMessageHandler implements MessageHandler<RSP_K11> {


    @Autowired SampleRepository samples;
    @Autowired ApplicationEventPublisher publisher;

    @Override
    public void handle(RSP_K11 message, HL7Client client) throws HL7Exception, IOException {
        SPM spm = (SPM)message.get("SPM");
        ORC orc = (ORC)message.get("ORC");
        OBR obr = (OBR)message.get("OBR");

        String sampleId = spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier().getValue();
        String sampleType = spm.getSpm4_SpecimenType().getIdentifier().getValue();
        String control = orc.getOrc1_OrderControl().getValue();
        String orderId = orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().getValue();
        String testCode = obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().getValue();

        if (control.compareToIgnoreCase("NW") == 0) {
            Sample sample = this.samples.get(sampleId);
            if (sample != null) {
                /**
                 * Only consider samples in the querying state as otherwise these records could affect already
                 * processed records (the simulator for the MPx sample prep manager sends all records even ones
                 * it has already sent for example).
                 */
                if (sample.getStatus() == EnumSampleStatus.QUERYING) {
                    sample.setOrderId(orderId);
                    sample.setTest(testCode);
                    sample.setSampleType(sampleType);
                    sample.setStatus(EnumSampleStatus.READY);
                    this.samples.update(sample);
                    this.publisher.publishEvent(new SampleChangedEvent(this, sample.getSampleId()));
                }
            }
        }
        
    }
}
