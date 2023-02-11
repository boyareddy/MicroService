package com.roche.swam.labsimulator.mpx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.swam.labsimulator.InstanceUtil;
import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.SampleLoader;
import com.roche.swam.labsimulator.common.bl.lab.EnumSampleProcessState;
import com.roche.swam.labsimulator.common.bl.lab.SampleIdProvider;
import com.roche.swam.labsimulator.common.bl.sim.BasicHL7Simulator;
import com.roche.swam.labsimulator.common.bl.sim.EnumSimulatorStatus;
import com.roche.swam.labsimulator.lis.bl.SampleIdGenerator;
import com.roche.swam.labsimulator.mpx.bl.EnumSampleStatus;
import com.roche.swam.labsimulator.mpx.bl.OrderDownloadMessageHandler;
import com.roche.swam.labsimulator.mpx.bl.QueryMessageSender;
import com.roche.swam.labsimulator.mpx.bl.ResultMessabeSerderU01;
import com.roche.swam.labsimulator.mpx.bl.ResultMessageSender;
import com.roche.swam.labsimulator.mpx.bl.Run;
import com.roche.swam.labsimulator.mpx.bl.RunRepository;
import com.roche.swam.labsimulator.mpx.bl.Sample;
import com.roche.swam.labsimulator.mpx.bl.SampleRepository;
import com.roche.swam.labsimulator.util.InputListBean;
import com.roche.swam.labsimulator.util.JsonPropertyReader;
import com.roche.swam.labsimulator.util.LpInputSample;

import ca.uhn.hl7v2.model.v251.message.RSP_K11;

@Component
@Scope("prototype")
public class MpxSimulator extends BasicHL7Simulator {

	private static final Logger logger = LoggerFactory.getLogger(MpxSimulator.class);
	@Autowired
	private SampleIdGenerator sampleIdGenerator;
	@Autowired
	private QueryMessageSender querySender;
	@Autowired
	private ResultMessageSender resultSender;
	@Autowired
	private OrderDownloadMessageHandler orderDownloadHandler;
	@Autowired
	private SampleRepository samples;
	@Autowired
	private RunRepository runs;
	@Autowired
	private SampleIdProvider sampleIdProvider;
	
	@Autowired(required=true)
	private ResultMessabeSerderU01 resultSenderU01;
	
	private SampleLoader sampleLoader;
	private boolean isRunning;
	
	String canContinue;

	public String getCanContinue() {
		return canContinue;
	}

	public void setCanContinue(String canContinue) {
		this.canContinue = canContinue;
	}

	public MpxSimulator() {
		super();
	}

	@Override
	public void configure(final int id, final String type, final String name, final String serialNumber) {
		super.configure(id, type, name, serialNumber);
		this.connection.addSender(this.querySender);
		this.connection.addSender(this.resultSender);
		
		this.connection.addSender(this.resultSenderU01);
		
		this.connection.addHandler(this.orderDownloadHandler, RSP_K11.class);
	}

	public Collection<Sample> getSamples() {
		return this.samples.getAll(this.getSerial());
	}

	public void startProcess() throws InterruptedException, IOException {
	
		if (logger.isDebugEnabled()) {
			logger.debug("Entered  startProcess()");
		}

		File sampleFile = new File(MainApp.getFilepath(MainApp.getDeviceName()));
		boolean exists = sampleFile.exists();
		if (!exists) {
			this.startExtraction();
		} else {
			if(canContinue.equalsIgnoreCase("false"))
				logger.info("Your last extraction was Stopped abruptly, do you wants to restart the process?(Y/N)");

			try {
				String flag = "Y";
				if(canContinue.equalsIgnoreCase("false")) {
					Scanner scanner = new Scanner(System.in);
					flag = scanner.nextLine();
				}
				if ("Y".equalsIgnoreCase(flag)) {
					InstanceUtil.getInstance().setDeviceState("OP");
					sampleLoader = new SampleLoader();
					this.samples = sampleLoader.jsonToSamples();
					Collection<Sample> samples1 = samples.getAllInState(null, EnumSampleStatus.QUERYING);
					logger.info(MainApp.getDeviceName() + "Loaded all the required samples");
					if (logger.isDebugEnabled()) {
						logger.debug("startProcess method: samples collection recieved" + samples1);
					}
					this.startRun();
				} else {

					Path path = Paths.get(MainApp.getFilepath(MainApp.getDeviceName()));
					if (logger.isDebugEnabled()) {
						logger.debug("startProcess method: cleaning up the temp file path" + path);
					}
					cleanUp(path);
					this.startExtraction();
				}
			}finally {
				
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Finished  startProcess()");
		}
	
	}

	public void startExtraction() throws InterruptedException, IOException {

		if (logger.isDebugEnabled()) {
			logger.debug("Entered  startExtraction()");
		}

		this.enable();
		this.createOrders();
		InstanceUtil.getInstance().setDeviceState("OP");
		this.connection.run(this.querySender, EnumSampleStatus.LOADED);
		
		if(this.connection.isConnected()!= true) {
			logger.info("shutting down the " + MainApp.getDeviceName() + " simulator!!"+"Could not connect HL7");
			System.exit(2);
		}
			
		logger.info("MP24 - Loaded all the required samples");
		Thread.sleep(5000);
		this.startRun();

		if (logger.isDebugEnabled()) {
			logger.debug("Finished  startExtraction()");
		}
	}

	public void createOrders() {
		if (logger.isDebugEnabled()) {
			logger.debug("Entered  createOrders()");
		}

		JsonPropertyReader jpr = new JsonPropertyReader();
		List<String> sampleIdArray = jpr.getSampleIdList();
		int numberOfSamples = 0;
		logger.info(MainApp.getDeviceName() + " Loading the samples initiated");
		if (sampleIdArray.size() == 0) {

			numberOfSamples = jpr.getNumberOfSamples();

			logger.info(MainApp.getDeviceName()
					+ " Sample is empty. Generating samples automatically using no. of samples " + numberOfSamples);
			for (int i = 0; i < numberOfSamples; i++) {
				String sampleId = this.sampleIdGenerator.getNext();

				if (logger.isDebugEnabled()) {
					logger.debug("Loading the Sample:" + sampleId);
				}
				this.sampleIdProvider.initSample(sampleId);
			}
		} else {
			logger.info("Loading the samples...");
			for (String sampleId : sampleIdArray) {
				if (logger.isDebugEnabled()) {
					logger.debug("Loading the Sample:" + sampleId);
				}
				this.sampleIdProvider.initSample(sampleId);
				numberOfSamples += 1;
			}

		}
		if (numberOfSamples > 24) {
			logger.info("Number of samples exceeds maximum limit of 24, terminating the process");
			System.exit(1);
		} else {
			loadSamples(numberOfSamples);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Finished  createOrders()");
		}
	}

	public void loadSamples(final int number) {

		if (logger.isDebugEnabled()) {
			logger.debug("Entered  loadSamples() method with no. of samples " + number);
		}

		String stripflag = this.sampleIdGenerator.getNext();
		for (int i = 1; i <= number; i++) {
			String sampleId = this.sampleIdProvider.moveSample(EnumSampleProcessState.ACCESSIONING,
					EnumSampleProcessState.NUCLEIC_ACID_EXTRACTION);

			/**
			 * Since the user can specify the number they may specify more
			 * samples than are actually available for NA. Hence check to see if
			 * a valid sample was found and moved and if not ignore it.
			 */
			if (sampleId != null) {
				JsonPropertyReader jpr = new JsonPropertyReader();
				Sample sample = jpr.readJsonData(sampleId);
				sample.setInstrumentId(this.getSerial());
				sample.setOutputContainerId(sample.getInputContainerId());
				sample.setOutputPosition(sample.getInputPosition());
				int positionflag = i % 8;
				if (positionflag != 0) {
					if(StringUtils.isBlank(sample.getOutputContainerId())) {
					sample.setOutputContainerId(stripflag);
					}
					if(StringUtils.isBlank(sample.getOutputPosition())) {
					sample.setOutputPosition(String.valueOf(positionflag));
					}
				} else {
					if(StringUtils.isBlank(sample.getOutputContainerId())) {
					sample.setOutputContainerId(stripflag);
					}
					if(StringUtils.isBlank(sample.getOutputPosition())) {
					sample.setOutputPosition(String.valueOf(8));
					}
					stripflag = this.sampleIdGenerator.getNext();
				}
				sample.setSampleId(sampleId);
				sample.setStatus(EnumSampleStatus.LOADED);
				this.samples.add(sample);
				if (logger.isDebugEnabled()) {
					logger.debug("loaded samples :" + sample.toString());
				}
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("Finished  loadSamples() method with no. of samples ");
		}
	}

	public void startRun() throws IOException {

		
		if (logger.isDebugEnabled()) {
			logger.debug("Entered  startRun() method  ");
		}
		if(canContinue.equalsIgnoreCase("false"))
			logger.info("Samples have been loaded successfully, do you want to start the process (Y/N) ?:");
		try {
			InstanceUtil.getInstance().setDeviceState("ID");
			String flag = "Y";
			if(canContinue.equalsIgnoreCase("false")) {
				Scanner scanner = new Scanner(System.in);
				flag = scanner.nextLine();
			}
			if ("Y".equalsIgnoreCase(flag)) {
				InstanceUtil.getInstance().setDeviceState("OP");
				this.isRunning = true;
				Run newRun = this.runs.create();
				for (Sample sample : this.samples.getAllInState(null, EnumSampleStatus.QUERYING)) {

					sample.setRun(newRun.getId());
					sample.setStatus(EnumSampleStatus.RUNNING);

				}
				this.updateState();

				this.finishRun();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
					Thread.currentThread().interrupt();
				}
				logger.info("Sending the Status to connect");

				this.connection.run(this.resultSender, this.samples, EnumSampleStatus.RESULTS_AVAILABLE);
				
				Path path = Paths.get(MainApp.getFilepath(MainApp.getDeviceName()));
				cleanUp(path);
				logger.info("shutting down the " + MainApp.getDeviceName() + " simulator!!");
				InstanceUtil.getInstance().setDeviceState("SD");
				if("true".equalsIgnoreCase(MainApp.getFilepath("Esu_U01"))) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						logger.error("thread interrupted : "+e.getMessage());
					}
				}
				logger.info("Run Completed");
				//System.exit(1);

			} else {
				System.exit(1);
			}
		}finally {
			
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Finished  finishRun() method ");
		}

	}

	public void cleanUp(Path path) {
		try {
			Files.delete(path);
		} catch (IOException ioException) {
			logger.error(ioException.getMessage());
		}

	}

	public void finishRun() throws IOException {

		if (logger.isDebugEnabled()) {
			logger.debug("Entered  finishRun() method ");
		}

		this.isRunning = false;
		List<LpInputSample> sampleList = new ArrayList<>();
		InputListBean inputListBean = new InputListBean();
		for (Sample sample : this.samples.getAllInState(null, EnumSampleStatus.RUNNING)) {
			sample.setStatus(EnumSampleStatus.RESULTS_AVAILABLE);
			if (!("ABORTED".equalsIgnoreCase(sample.getResult()))) {
				LpInputSample lpInputSample = new LpInputSample();
				lpInputSample.setRun(sample.getRun());
				lpInputSample.setDateTime(sample.getDateTime());
				lpInputSample.setSampleType(sample.getSampleType());
				lpInputSample.setOutputContainerId("");
				lpInputSample.setOutputPosition("");
				lpInputSample.setInputContainerId(sample.getOutputContainerId());
				lpInputSample.setInputPosition(sample.getOutputPosition());
				lpInputSample.setSendingFacilityName(sample.getSendingFacilityName());
				lpInputSample.setReceivingFacilityName(sample.getReceivingFacilityName());
				System.out.println("sendingApplicationName before:: "+sample.getSendingApplicationName());
				lpInputSample.setSendingApplicationName(MainApp.getFilepath("LP"));
				System.out.println("sendingApplicationName After:: "+lpInputSample.getSendingApplicationName());
				lpInputSample.setReceivingApplicationName(sample.getReceivingApplicationName());
				lpInputSample.setProcessingId(sample.getProcessingId());
				lpInputSample.setCharacterSet(sample.getCharacterSet());
				lpInputSample.setVersionId(sample.getVersionId());
				lpInputSample.setMessageQueryName(sample.getMessageQueryName());
				lpInputSample.setMessageTyep(sample.getMessageTyep());
				lpInputSample.setResult(sample.getResult());
				lpInputSample.setInternalControls(sample.getInternalControls());
				lpInputSample.setProcessingCartridge(sample.getProcessingCartridge());
				lpInputSample.setTipRack(sample.getTipRack());
				lpInputSample.setStatus(sample.getStatus());
				lpInputSample.setSampleType(sample.getSampleType());
				lpInputSample.setRun(sample.getRun());
				lpInputSample.setSampleId(sample.getSampleId());
				lpInputSample.setOrderId(sample.getOrderId());
				lpInputSample.setSampleDescription(sample.getSampleDescription());
				lpInputSample.setConsumables(sample.getConsumables());
				lpInputSample.setSampleComments(sample.getSampleComments());
				sampleList.add(lpInputSample);
			}
		}
		this.updateState();
		inputListBean.setSamples(sampleList);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(MainApp.getFilepath("LP24PreDataJsonPath")), inputListBean);

		if (logger.isDebugEnabled()) {
			logger.debug(" finishRun() method feeding sample data to LPx Pre process simulator in the path   "
					+ MainApp.getFilepath("LP24PreDataJsonPath"));
			logger.debug("Finished  finishRun() method ");
		}
	}

	@Override
	public EnumSimulatorStatus calculateState() {
		EnumSimulatorStatus state = super.calculateState();
		if (state == EnumSimulatorStatus.CONNECTED && this.isRunning) {
			state = EnumSimulatorStatus.RUNNING;
		}
		return state;
	}

	/*
	 * ESU_U01 related code
	*/
	public void startRunU01() throws IOException {
		try {
			this.connection.run(this.resultSenderU01);
		} finally {
			// do't remove
		}
	}
}
