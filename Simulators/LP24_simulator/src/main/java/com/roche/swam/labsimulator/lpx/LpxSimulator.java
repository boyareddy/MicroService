package com.roche.swam.labsimulator.lpx;

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
import com.roche.swam.labsimulator.mpx.bl.ResultMessageSender;
import com.roche.swam.labsimulator.mpx.bl.ResultMessageSenderU01;
import com.roche.swam.labsimulator.mpx.bl.Run;
import com.roche.swam.labsimulator.mpx.bl.RunRepository;
import com.roche.swam.labsimulator.mpx.bl.Sample;
import com.roche.swam.labsimulator.mpx.bl.SampleRepository;
import com.roche.swam.labsimulator.util.DpcrConstants;
import com.roche.swam.labsimulator.util.DpcrInput;
import com.roche.swam.labsimulator.util.LP24JsonPropertyReader;

import ca.uhn.hl7v2.model.v251.message.RSP_K11;

@Component
@Scope("prototype")
public class LpxSimulator extends BasicHL7Simulator {

	private static final Logger logger = LoggerFactory.getLogger(LpxSimulator.class);
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
	private ResultMessageSenderU01 resultSenderU01;
	private SampleLoader sampleLoader;
	private boolean isRunning;
	String canContinue;

	public String getCanContinue() {
		return canContinue;
	}

	public void setCanContinue(String canContinue) {
		this.canContinue = canContinue;
	}
	
	public LpxSimulator() {
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
		File sampleFile = new File(MainApp.getFilepath("LPSample"));
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
					final List<String> positionId = SampleIdGenerator.getLp24Chip();
					sampleLoader = new SampleLoader();
					this.samples = sampleLoader.jsonToSamples();
					samples.getAllInState(null, EnumSampleStatus.QUERYING);
					int i=1;
					for (Sample sample : samples.getAllInState(null, EnumSampleStatus.QUERYING)) {
						sample.setInstrumentId(MainApp.getFilepath("DeviceSerialNumber"));
						String[] containerPosition = positionId.get(i - 1).split("_");
						if ("false".equalsIgnoreCase(sample.getOutputContainerIdEmpty())) {
							if (StringUtils.isBlank(sample.getOutputContainerId())) {
								sample.setOutputContainerId(containerPosition[0]);
							}
						} else {
							sample.setOutputContainerId("");
						}

						if ("false".equalsIgnoreCase(sample.getOutputPositionEmpty())) {
							if (StringUtils.isBlank(sample.getOutputPosition())) {
								sample.setOutputPosition(containerPosition[1]);
							}
						} else {
							sample.setOutputPosition("");
						}
						i++;
					}
					logger.info("LP24 Post process - Loaded all the required samples");
					this.startRun();
				} else {
					Path path = Paths.get(MainApp.getFilepath("LPSample"));
					cleanUp(path);
					this.startExtraction();
				}
			}finally {
				
			}
		}

	}

	public void startExtraction() throws InterruptedException, IOException {

		this.enable();
		this.createOrders();
		InstanceUtil.getInstance().setDeviceState("OP");
		this.connection.run(this.querySender, EnumSampleStatus.LOADED);
		logger.info("LP24 Post process - Loaded all the required samples");
		Thread.sleep(5000);
		this.startRun();
	}

	public void createOrders() {
		LP24JsonPropertyReader jpr = new LP24JsonPropertyReader();
		List<String> sampleIdArray = jpr.getSampleIdList();
		logger.info("LP24 Post process - Loading the samples from LP24 Pre process");
		logger.info("Loading the samples using in Custom mode");
		for (String sampleId : sampleIdArray) {
			logger.info("Loading the Sample:" + sampleId);
			this.sampleIdProvider.initSample(sampleId);

			logger.info("create orders" + sampleId);
		}

		loadSamples(sampleIdArray.size());

	}

	public void loadSamples(final int number) {
		
		
		final List<String> positionId = SampleIdGenerator.getLp24Chip();
		for (int i = 1; i <= number; i++) {
			String sampleId = this.sampleIdProvider.moveSample(EnumSampleProcessState.ACCESSIONING,
					EnumSampleProcessState.NUCLEIC_ACID_EXTRACTION);
			
			/**
			 * Since the user can specify the number they may specify more
			 * samples than are actually available for NA. Hence check to see if
			 * a valid sample was found and moved and if not ignore it.
			 */
			if (sampleId != null) {
				LP24JsonPropertyReader jpr = new LP24JsonPropertyReader();
				Sample sample = jpr.readJsonData(sampleId);
				sample.setInstrumentId(MainApp.getFilepath("DeviceSerialNumber"));
				sample.setSendingApplicationName(MainApp.getDeviceName());
				sample.setSampleId(sample.getInputContainerId() + "_" + sample.getInputPosition());
			String[] containerPosition = positionId.get(i-1).split("_");
			
			if("false".equalsIgnoreCase(sample.getOutputContainerIdEmpty())) {
				if(StringUtils.isBlank(sample.getOutputContainerId())) {
					sample.setOutputContainerId(containerPosition[0]);
				}
			}
			else {
				sample.setOutputContainerId("");
			}
				
			if("false".equalsIgnoreCase(sample.getOutputPositionEmpty())) {	
				if(StringUtils.isBlank(sample.getOutputPosition())) {
					sample.setOutputPosition(containerPosition[1]);
				}
			}
			else {
				sample.setOutputPosition("");
			}
				
				sample.setStatus(EnumSampleStatus.LOADED);
				logger.info("Output position "  + i + "\t"  + sample.getOutputPosition());
				logger.info("load SAmples" + sample.toString());
				this.samples.add(sample);

			}
		}
	}

	public void startRun() throws IOException {
		if(canContinue.equalsIgnoreCase("false"))
			logger.info("Samples have been loaded sucessfully, do you want to start the process (Y/N) ?:");

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
				logger.info("Library Preparation is completed success fully, Sending the Status to connect");
				this.connection.run(this.resultSender, this.samples, EnumSampleStatus.RESULTS_AVAILABLE);
				Path path = Paths.get(MainApp.getFilepath("LPSample"));
				cleanUp(path);
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

	}

	public void cleanUp(Path path) {
		try {
			Files.delete(path);
		} catch (IOException ioException) {
			logger.error(ioException.getMessage());
		}

	}

	public void finishRun() throws IOException {

		this.isRunning = false;
		DpcrInput dpcrInput = new DpcrInput();
		List<String> plateId = new ArrayList<>();
		for (Sample sample : this.samples.getAllInState(null, EnumSampleStatus.RUNNING)) {
			sample.setStatus(EnumSampleStatus.RESULTS_AVAILABLE);
			if (!("ABORTED".equalsIgnoreCase(sample.getResult()))) {
				if (!plateId.contains(sample.getOutputContainerId())) {
					plateId.add(sample.getOutputContainerId());
				}
			}

		}
		dpcrInput.setPlateId(plateId);
		dpcrInput.setQueryMsgEvent(DpcrConstants.MESSAGEQUERYEVENT);
		dpcrInput.setQueryMsgType(DpcrConstants.MESSAGEQUERYTYPE);
		dpcrInput.setQueryProcessingId(DpcrConstants.PROCESSINGID);
		dpcrInput.setReceivingApplicationName(DpcrConstants.APPLICATION);
		dpcrInput.setVersionId(DpcrConstants.VERSION);
		
		
		this.updateState();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(MainApp.getFilepath("DpcrJsonPath")), dpcrInput);

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
