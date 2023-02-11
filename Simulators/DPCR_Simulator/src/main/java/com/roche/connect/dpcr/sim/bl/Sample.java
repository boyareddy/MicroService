package com.roche.connect.dpcr.sim.bl;

import java.util.List;

public class Sample {
	private String sampleId;
    private String test;
    private String protocolType;
	private String orderId;
	

    private EnumSampleStatus status;
	
    private String run;
	private String dateTime;
    private String sampleType;
    private String instrumentId;
    private String runId;
    private String inputContainerId;
    private String inputPosition;
    private String outputContainerId;
    private String outputPosition;
    private String sendingFacilityName;
    private String receivingFacilityName;
    private String messageControlId;
    private String sendingApplicationName;
    private String receivingApplicationName;
	private String queryProcessingId;
	private String resultProcessingId;
	private String versionId;
	private String characterSet;
	private String messageQueryName;
	private String messageType;
	private String result;
	private String internalControls;
    private String processingCartridge;
    private String tipRack;
    private String plateType;
    private String sampleComments;
    private String outputContainerIdEmpty;
    private String outputPositionEmpty;
    private List<Consumables> consumables;
	private String sampleDescription;
	private String queryMsgType;
	private String queryMsgEvent;
	private String resultMsgType;
	private String resultMsgEvent;
	
	
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


	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getMessageQueryName() {
		return messageQueryName;
	}

	public void setMessageQueryName(String messageQueryName) {
		this.messageQueryName = messageQueryName;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getCharacterSet() {
		return characterSet;
	}

	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
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
	
	

	/**
	 * @return the outputContainerIdEmpty
	 */
	public String getOutputContainerIdEmpty() {
		return outputContainerIdEmpty;
	}

	/**
	 * @param outputContainerIdEmpty the outputContainerIdEmpty to set
	 */
	public void setOutputContainerIdEmpty(String outputContainerIdEmpty) {
		this.outputContainerIdEmpty = outputContainerIdEmpty;
	}

	/**
	 * @return the outputPositionEmpty
	 */
	public String getOutputPositionEmpty() {
		return outputPositionEmpty;
	}

	/**
	 * @param outputPositionEmpty the outputPositionEmpty to set
	 */
	public void setOutputPositionEmpty(String outputPositionEmpty) {
		this.outputPositionEmpty = outputPositionEmpty;
	}
	
	

	/**
	 * @return the queryMsgType
	 */
	public String getQueryMsgType() {
		return queryMsgType;
	}

	/**
	 * @param queryMsgType the queryMsgType to set
	 */
	public void setQueryMsgType(String queryMsgType) {
		this.queryMsgType = queryMsgType;
	}

	/**
	 * @return the queryMsgEvent
	 */
	public String getQueryMsgEvent() {
		return queryMsgEvent;
	}

	/**
	 * @param queryMsgEvent the queryMsgEvent to set
	 */
	public void setQueryMsgEvent(String queryMsgEvent) {
		this.queryMsgEvent = queryMsgEvent;
	}

	/**
	 * @return the resultMsgType
	 */
	public String getResultMsgType() {
		return resultMsgType;
	}

	/**
	 * @param resultMsgType the resultMsgType to set
	 */
	public void setResultMsgType(String resultMsgType) {
		this.resultMsgType = resultMsgType;
	}

	/**
	 * @return the resultMsgEvent
	 */
	public String getResultMsgEvent() {
		return resultMsgEvent;
	}

	/**
	 * @param resultMsgEvent the resultMsgEvent to set
	 */
	public void setResultMsgEvent(String resultMsgEvent) {
		this.resultMsgEvent = resultMsgEvent;
	}

	/**
	 * @return the queryProcessingId
	 */
	public String getQueryProcessingId() {
		return queryProcessingId;
	}

	/**
	 * @param queryProcessingId the queryProcessingId to set
	 */
	public void setQueryProcessingId(String queryProcessingId) {
		this.queryProcessingId = queryProcessingId;
	}

	/**
	 * @return the resultProcessingId
	 */
	public String getResultProcessingId() {
		return resultProcessingId;
	}

	/**
	 * @param resultProcessingId the resultProcessingId to set
	 */
	public void setResultProcessingId(String resultProcessingId) {
		this.resultProcessingId = resultProcessingId;
	}
	
	

	/**
	 * @return the runId
	 */
	public String getRunId() {
		return runId;
	}

	/**
	 * @param runId the runId to set
	 */
	public void setRunId(String runId) {
		this.runId = runId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sample [sampleId=" + sampleId + ", test=" + test + ", protocolType=" + protocolType + ", orderId="
				+ orderId + ", status=" + status + ", run=" + run + ", dateTime=" + dateTime + ", sampleType="
				+ sampleType + ", instrumentId=" + instrumentId + ", runId=" + runId + ", inputContainerId="
				+ inputContainerId + ", inputPosition=" + inputPosition + ", outputContainerId=" + outputContainerId
				+ ", outputPosition=" + outputPosition + ", sendingFacilityName=" + sendingFacilityName
				+ ", receivingFacilityName=" + receivingFacilityName + ", messageControlId=" + messageControlId
				+ ", sendingApplicationName=" + sendingApplicationName + ", receivingApplicationName="
				+ receivingApplicationName + ", queryProcessingId=" + queryProcessingId + ", resultProcessingId="
				+ resultProcessingId + ", versionId=" + versionId + ", characterSet=" + characterSet
				+ ", messageQueryName=" + messageQueryName + ", messageType=" + messageType + ", result=" + result
				+ ", internalControls=" + internalControls + ", processingCartridge=" + processingCartridge
				+ ", tipRack=" + tipRack + ", plateType=" + plateType + ", sampleComments=" + sampleComments
				+ ", outputContainerIdEmpty=" + outputContainerIdEmpty + ", outputPositionEmpty=" + outputPositionEmpty
				+ ", consumables=" + consumables + ", sampleDescription=" + sampleDescription + ", queryMsgType="
				+ queryMsgType + ", queryMsgEvent=" + queryMsgEvent + ", resultMsgType=" + resultMsgType
				+ ", resultMsgEvent=" + resultMsgEvent + ", flag=" + flag + "]";
	}

	
	
    
}

