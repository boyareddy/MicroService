package com.roche.swam.labsimulator.common.bl.lab;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class SampleIdProvider {

    private Map<String, SampleTrackingRecord> sampleIds;

    public SampleIdProvider() {
        this.sampleIds = new HashMap<>();
    }


    public String initSample(final String sampleId) {
        SampleTrackingRecord record = this.sampleIds.getOrDefault(sampleId, null);
        if (record == null) {
            record = new SampleTrackingRecord(sampleId);
            this.sampleIds.put(sampleId, record);
        }
        record.setProcessState(EnumSampleProcessState.ACCESSIONING);
        return sampleId;
    }

    public String moveSample(final EnumSampleProcessState fromState, final EnumSampleProcessState toState) {
        try {
            SampleTrackingRecord record = this.sampleIds.values().stream()
                    .filter(r -> r.getProcessState() == fromState)
                    .findAny().get();
            record.setProcessState(toState);
            return record.getSampleId();
        } catch (NoSuchElementException ex) {
            return null;
        }
    }


   
}
