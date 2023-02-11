package com.roche.swam.labsimulator.mpx.bl;

import org.springframework.context.ApplicationEvent;

public class SampleChangedEvent extends ApplicationEvent {

    private  String sampleId;

    public SampleChangedEvent(Object sender, String sampleId) {
        super(sender);
        this.sampleId = sampleId;
    }

    public String getSampleId() {
        return sampleId;
    }
}
