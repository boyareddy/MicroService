package com.roche.swam.labsimulator.mpx.bl;

import java.util.List;

public class Sample {
	private String sampleId;
    private String test;

	private String protocolType;
	private String orderId;
	private String deviceRunId;

    private EnumSampleStatus status;
	
    private String run;
	private String DateTime;
    private String sampleType;
    private String instrumentId;
    private String inputContainerId;
    private String inputPosition;
    private String outputContainerId;
    private String outputPosition;
    private String sendingFacilityName;
    private String receivingFacilityName;
    private String messageControlId;
    private String sendingApplicationName;
    private String receivingApplicationName;
	private String ProcessingId;
	private String VersionId;
	private String CharacterSet;
	private String messageQueryName;
	private String messageType;
	private String result;
	private String internalControls;
    private String processingCartridge;
    private String tipRack;
    private String plateType;
    private String sampleComments;
    
    private List<Consumables> consumables;
	private String sampleDescription;
	
	public String getDeviceRunId() {
		return deviceRunId;
	}

	public void setDeviceRunId(String deviceRunId) {
		this.deviceRunId = deviceRunId;
	}
	
	public String getResult() {
		return result;
	}

	public String getInternalControls() {
		return internalControls;
	}

	public void setInternalControls(String internalControls) {
		this.internalControls = internalControls;
	}

	public String getProcessingCartridge() {
		return processingCartridge;
	}

	public void setProcessingCartridge(String processingCartridge) {
		this.processingCartridge = processingCartridge;
	}

	public String getTipRack() {
		return tipRack;
	}

	public void setTipRack(String tipRack) {
		this.tipRack = tipRack;
	}


	public void setResult(String result) {
		this.result = result;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	private String flag;
	
	
	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

    public Sample() {
    	
    }
    
    public String getSendingApplicationName() {
		return sendingApplicationName;
	}

	public void setSendingApplicationName(String sendingApplicationName) {
		this.sendingApplicationName = sendingApplicationName;
	}

	public String getReceivingApplicationName() {
		return receivingApplicationName;
	}

	public void setReceivingApplicationName(String receivingApplicationName) {
		this.receivingApplicationName = receivingApplicationName;
	}

	public String getMessageControlId() {
		return messageControlId;
	}

	public void setMessageControlId(String messageControlId) {
		this.messageControlId = messageControlId;
	}

	public Sample(final String sampleId, final String instrumentId) {
        this.status = EnumSampleStatus.INIT;
        this.sampleId = sampleId;
        this.instrumentId = instrumentId;
    }
    
    public String getRun() {
        return run;
    }

    public EnumSampleStatus getStatus() {
        return status;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTest() {
        return test;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setOrderId(final String orderId) {
        this.orderId = orderId;
    }

    public void setTest(final String test) {
        this.test = test;
    }

    public void setStatus(final EnumSampleStatus status) {
        this.status = status;
    }

    public void setRun(final String id) {
        this.run = id;
    }


    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(final String sampleType) {
        this.sampleType = sampleType;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(final String instrumentId) {
        this.instrumentId = instrumentId;                
    }

	public String getSendingFacilityName() {
		return sendingFacilityName;
	}

	public void setSendingFacilityName(String sendingFacilityName) {
		this.sendingFacilityName = sendingFacilityName;
	}

	public String getReceivingFacilityName() {
		return receivingFacilityName;
	}

	public void setReceivingFacilityName(String receivingFacilityName) {
		this.receivingFacilityName = receivingFacilityName;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getProcessingId() {
		return ProcessingId;
	}

	public void setProcessingId(String processingId) {
		ProcessingId = processingId;
	}

	public String getVersionId() {
		return VersionId;
	}

	public void setVersionId(String versionId) {
		VersionId = versionId;
	}

	public String getMessageQueryName() {
		return messageQueryName;
	}

	public void setMessageQueryName(String messageQueryName) {
		this.messageQueryName = messageQueryName;
	}

	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}

	public String getCharacterSet() {
		return CharacterSet;
	}

	public void setCharacterSet(String characterSet) {
		CharacterSet = characterSet;
	}

	public String getMessageTyep() {
		return messageType;
	}

	public void setMessageTyep(String messageTyep) {
		this.messageType = messageTyep;
	}



	public String getSampleDescription() {
		return sampleDescription;
	}

	public void setSampleDescription(String sampleDescription) {
		this.sampleDescription = sampleDescription;
	}

	public List<Consumables> getConsumables() {
		return consumables;
	}

	public void setConsumables(List<Consumables> consumables) {
		this.consumables = consumables;
	}

	public String getInputContainerId() {
		return inputContainerId;
	}

	public void setInputContainerId(String inputContainerId) {
		this.inputContainerId = inputContainerId;
	}

	public String getInputPosition() {
		return inputPosition;
	}

	public void setInputPosition(String inputPosition) {
		this.inputPosition = inputPosition;
	}

	public String getOutputContainerId() {
		return outputContainerId;
	}

	public void setOutputContainerId(String outputContainerId) {
		this.outputContainerId = outputContainerId;
	}

	public String getOutputPosition() {
		return outputPosition;
	}

	public void setOutputPosition(String outputPosition) {
		this.outputPosition = outputPosition;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getPlateType() {
		return plateType;
	}

	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}

	public String getSampleComments() {
		return sampleComments;
	}

	public void setSampleComments(String sampleComments) {
		this.sampleComments = sampleComments;
	}

	@Override
	public String toString() {
		return "Sample [sampleId=" + sampleId + ", test=" + test + ", protocolType=" + protocolType + ", orderId="
				+ orderId + ", status=" + status + ", run=" + run + ", DateTime=" + DateTime + ", sampleType="
				+ sampleType + ", instrumentId=" + instrumentId + ", inputContainerId=" + inputContainerId
				+ ", inputPosition=" + inputPosition + ", outputContainerId=" + outputContainerId + ", outputPosition="
				+ outputPosition + ", sendingFacilityName=" + sendingFacilityName + ", receivingFacilityName="
				+ receivingFacilityName + ", messageControlId=" + messageControlId + ", sendingApplicationName="
				+ sendingApplicationName + ", receivingApplicationName=" + receivingApplicationName + ", ProcessingId="
				+ ProcessingId + ", VersionId=" + VersionId + ", CharacterSet=" + CharacterSet + ", messageQueryName="
				+ messageQueryName + ", messageType=" + messageType + ", result=" + result + ", internalControls="
				+ internalControls + ", processingCartridge=" + processingCartridge + ", tipRack=" + tipRack
				+ ", plateType=" + plateType + ", sampleComments=" + sampleComments + ", consumables=" + consumables
				+ ", sampleDescription=" + sampleDescription + ", flag=" + flag + "]";
	}

	
    
}

