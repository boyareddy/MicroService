package com.roche.swam.labsimulator.util;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.mpx.bl.Sample;



public class JsonPropertyReader {
	
	private SimulatorBean simulatorBean;  
	
	public SimulatorBean getSimulatorBean() {
		return simulatorBean;
	}

	public void setSimulatorBean(SimulatorBean simulatorBean) {
		this.simulatorBean = simulatorBean;
	}

	private static final Logger logger = LoggerFactory.getLogger(JsonPropertyReader.class);
	
	private List<String> sampleIdList;
	private Map<String,ResultSampleBean> sampleMap;
	
	public int getTimeDelay() {
		return simulatorBean.getDelayBetweenQuery();
	}
	
	
	public int getNumberOfSamples() {
		return simulatorBean.getNumberOfSamples();
	}
	
	public void setNumberOfSamples(int numberOfSamples) {
	}
	
	
	

	public JsonPropertyReader() {
		ObjectMapper mapper = new ObjectMapper();
		this.simulatorBean = new SimulatorBean();
		try {
			System.out.println(MainApp.getFilepath("MP24DataJsonPath"));
		this.simulatorBean = mapper.readValue(new File(MainApp.getFilepath("MP24DataJsonPath")), SimulatorBean.class);
		
		
		} catch (IOException e) {
			
		}
		setSampleMap();
	}
		

	public Sample readJsonData(String sampleId) {
		Sample qBean = new Sample();
		try {


			qBean.setSendingApplicationName(simulatorBean.getSendingApplication());
			qBean.setSendingFacilityName(simulatorBean.getSendingFacility());
			qBean.setReceivingApplicationName(simulatorBean.getReceivingApplication());
			qBean.setReceivingFacilityName(simulatorBean.getReceivingFacility());
			qBean.setMessageTyep(simulatorBean.getMessageType());
			qBean.setMessageControlId(simulatorBean.getMessageControlId());
			qBean.setMessageQueryName(simulatorBean.getMessageQueryName());
			qBean.setProcessingId(simulatorBean.getProcessingId());
			qBean.setVersionId(simulatorBean.getVersionId());
			qBean.setCharacterSet(simulatorBean.getCharacterSet());
			
			if(this.sampleMap.get(sampleId)!=null) {
			qBean.setFlag(this.sampleMap.get(sampleId).getFlag());
			qBean.setResult(this.sampleMap.get(sampleId).getResult());
			qBean.setInternalControls(this.sampleMap.get(sampleId).getInternalControls());
			qBean.setProcessingCartridge(this.sampleMap.get(sampleId).getProcessingCartridge());
			qBean.setTipRack(this.sampleMap.get(sampleId).getTipRack());
			qBean.setConsumables(this.sampleMap.get(sampleId).getConsumables());
			qBean.setInputContainerId(this.sampleMap.get(sampleId).getInputContainerId());
			qBean.setInputPosition(this.sampleMap.get(sampleId).getInputPosition());
			qBean.setSampleComments(this.sampleMap.get(sampleId).getSampleComments());
			}

		} catch (Exception e1) {
			logger.error(e1.getMessage());
		}
		return qBean;

	}


	/**
	 * @param timeDelay the timeDelay to set
	 */
	public void setTimeDelay(int timeDelay) {
	}


	public List<String> getSampleIdList() {

		
		this.sampleIdList = new ArrayList<String>();
		List<ResultSampleBean> sampleList = new ArrayList<ResultSampleBean>();
		sampleList = simulatorBean.getSamples();
		
		for(ResultSampleBean rsBean:sampleList)
		{
			this.sampleIdList.add(rsBean.getSampleID());
	}
		return this.sampleIdList;
	}


	public void setSampleIdList(List<String> sampleIdList) {
		this.sampleIdList = sampleIdList;
	}

	public Map<String,ResultSampleBean> getSampleMap() {
		return sampleMap;
	}

	public void setSampleMap() {
		
		this.sampleMap = new HashMap<String,ResultSampleBean>();
		List<ResultSampleBean> sampleList = new ArrayList<ResultSampleBean>();
		sampleList = simulatorBean.getSamples();
		for(ResultSampleBean rsBean:sampleList)
		{
			sampleMap.put(rsBean.getSampleID(), rsBean);
		}
	}

	

}
