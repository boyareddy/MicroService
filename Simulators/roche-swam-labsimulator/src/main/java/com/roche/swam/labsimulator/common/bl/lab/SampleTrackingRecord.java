package com.roche.swam.labsimulator.common.bl.lab;

public class SampleTrackingRecord {

    private String sampleId;
    private EnumSampleProcessState processState;

    public SampleTrackingRecord(String sampleId){
        this.sampleId = sampleId;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public EnumSampleProcessState getProcessState() {
        return processState;
    }

    public void setProcessState(EnumSampleProcessState processState) {
        this.processState = processState;
    }
}
