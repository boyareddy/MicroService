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

public class LP24PreJsonPropertyReader {

	private static final Logger logger = LoggerFactory.getLogger(LP24PreJsonPropertyReader.class);
	private SimulatorBean simulatorBean;
	private SamplesListBean samplesListBean;

	public SimulatorBean getSimulatorBean() {
		return simulatorBean;
	}

	public void setSimulatorBean(SimulatorBean simulatorBean) {
		this.simulatorBean = simulatorBean;
	}

	private List<String> sampleIdList;
	private Map<String, Sample> sampleMap;

	public static void main(String[] args) {
		new LP24PreJsonPropertyReader();

	}

	public LP24PreJsonPropertyReader() {
		ObjectMapper mapper = new ObjectMapper();
		this.samplesListBean = new SamplesListBean();
		try {
			logger.info("LP24PreJsonProperty reading sample data.");
			this.samplesListBean = mapper.readValue(new File(MainApp.getFilepath("LP24PreDataJsonPath")),
					SamplesListBean.class);

		} catch (IOException e) {

		}
		setSampleMap();
	}

	public int getTimeDelay() {
		return 1;
	}

	/**
	 * @param timeDelay
	 *            the timeDelay to set
	 */
	public void setTimeDelay(int timeDelay) {
	}

	public List<String> getSampleIdList() {

		this.sampleIdList = new ArrayList<String>();
		List<Sample> sampleList = new ArrayList<Sample>();
		sampleList = samplesListBean.getSamples();

		for (Sample rsBean : sampleList) {
			this.sampleIdList.add(rsBean.getInputContainerId() + '_' + rsBean.getInputPosition());

		}
		return this.sampleIdList;
	}

	public void setSampleIdList(List<String> sampleIdList) {
		this.sampleIdList = sampleIdList;
	}

	public Map<String, Sample> getSampleMap() {
		return sampleMap;
	}

	public void setSampleMap() {

		this.sampleMap = new HashMap<String, Sample>();
		List<Sample> sampleList = new ArrayList<Sample>();
		sampleList = samplesListBean.getSamples();
		for (Sample rsBean : sampleList) {
			String SampleId = rsBean.getInputContainerId() + '_' + rsBean.getInputPosition();
			sampleMap.put(SampleId, rsBean);
			logger.info("Mapping sample id: " + SampleId);
		}
	}

	public Sample readJsonData(String sampleId) {
		Sample qBean = new Sample();
		qBean = this.sampleMap.get(sampleId);
		return qBean;

	}

}
