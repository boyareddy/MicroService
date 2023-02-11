package com.roche.connect.dpcr.sim;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Timer;

import org.apache.commons.lang.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.dpcr.MainApp;
import com.roche.connect.dpcr.common.bl.sim.BasicHL7Simulator;
import com.roche.connect.dpcr.lis.bl.FileGenerator;
import com.roche.connect.dpcr.sim.bl.AckMessageSender;
import com.roche.connect.dpcr.sim.bl.EsuMessageSender;
import com.roche.connect.dpcr.sim.bl.Mp96SampleRepository;
import com.roche.connect.dpcr.sim.bl.QueryMessageSender;
import com.roche.connect.dpcr.sim.bl.ResultMessageSender;
import com.roche.connect.dpcr.util.ResultBean;
import com.roche.connect.dpcr.util.TaskScheduler;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;


@Component
@Scope("prototype")
public class DPCRSimulator extends BasicHL7Simulator {

	private static final Logger logger = LoggerFactory.getLogger(DPCRSimulator.class);
	@Autowired
	private QueryMessageSender querySender;
	@Autowired
	private Mp96SampleRepository samples;
	
	@Autowired
	private AckMessageSender ackSender;
	
	@Autowired
	private EsuMessageSender esuSender;
	
	@Autowired
	private ResultMessageSender resultSender;
	
	private boolean isRunning;
	
	public static String canContinue;
	
	public static String ackValidator;
	
	public static String runId;
	
	public static String filePath;
	
	public static String HL7Version;

	
	public DPCRSimulator() {
		super();
	}



	public void startProcess() throws  IOException,  HL7Exception, InterruptedException, NumberFormatException, ParseException, LLPException {
		this.checkForExistingRun();		
		this.sendESU("ID");		
		this.sendQuery();		
		this.sendAckAfterOML();		
		this.sendESU("OP");	
		this.sendSampleDetails();
		this.sendFinalESU();
		this.sendESU("OPN");	
		this.sendESU("ES");	
		pingMessage();
       
	}




	private void sendQuery() {
		this.querySender.run();
		
	}



	private void sendFinalESU() throws NumberFormatException, HL7Exception, IOException, InterruptedException {
		ResultBean resultBean = getResultBean();

		DPCRSimulator.setRunId(resultBean.getRunId());
		DPCRSimulator.setFilePath(generateAndSaveFile(resultBean.getRunId(),resultBean.getRunId()));
		if (StringUtils.isNotBlank(resultBean.getRunResults())
				&& "Passed".equalsIgnoreCase(resultBean.getRunResults())) {
			sendESU("ID");
		} else if (StringUtils.isNotBlank(resultBean.getRunResults())
				&& "Aborted".equalsIgnoreCase(resultBean.getRunResults())) {
			sendESU("SS");
		} else {
			sendESU("ES");
		}

	}


	private String generateAndSaveFile(String fileName, String message) {
		FileGenerator fc = new FileGenerator();
		String path="";
		String remoteFileShare=MainApp.getValueFromPropFile("useRemoteFS").trim();
		String fPath =MainApp.getValueFromPropFile("FilePath");
		String mountPath= MainApp.getValueFromPropFile("MountPath");
		String file= fileName+MainApp.getValueFromPropFile("fileType");
		if( StringUtils.isNotBlank(remoteFileShare) && "true".equalsIgnoreCase(remoteFileShare)) {
			String userName=MainApp.getValueFromPropFile("FileShareUserName").trim();
			if(StringUtils.isNotBlank(userName)) {
		path=fc.createCycleFileAndGetChecksum(MainApp.getValueFromPropFile("FileHost").trim(),userName , MainApp.getValueFromPropFile("FileSharePassword").trim(), 22,fPath,mountPath,
				file,message);
			}
			else {
				logger.error("FileShare User name in Property file is empty. File cannot be placed in remote server. \n Please make useRemoteFS=false if simulator and adapter running in same host. Else please enter username.");	
			}
		}
		else {
			path=fc.getFileChecksumWindows(fPath+mountPath+file,fileName);
		}
		return path;
	}



	private void sendESU(String status) throws HL7Exception, IOException, NumberFormatException, InterruptedException {
		
		if (canContinue.equalsIgnoreCase("false"))
			logger.info("Send ESU message with "+status+" status?(Y/N)");

		String flag = "Y";
		if (canContinue.equalsIgnoreCase("false")) {
			Scanner scanner = new Scanner(System.in);
			flag = scanner.nextLine();
		}
		if ("Y".equalsIgnoreCase(flag)) {
			this.esuSender.run(status);
		}
		
	}



	private void sendAckAfterOML()
			throws JsonParseException, JsonMappingException, IOException, HL7Exception, InterruptedException {
		File sampleFile = new File(MainApp.getValueFromPropFile("DPCRRunDataPath"));
		boolean exists = sampleFile.exists();
		
		if (exists) {
			Thread.sleep(Long.parseLong(MainApp.getValueFromPropFile("ResultMessageTimeInterval")));
		if (canContinue.equalsIgnoreCase("false"))
			logger.info("Response Message recieved. Do you want to send ACK?(Y/N)");

		String flag = "Y";
		if (canContinue.equalsIgnoreCase("false")) {
			Scanner scanner = new Scanner(System.in);
			flag = scanner.nextLine();
		}
		if ("Y".equalsIgnoreCase(flag)) {
			
		this.sendOmlAck();
		Thread.sleep(Long.parseLong(MainApp.getValueFromPropFile("ResultMessageTimeInterval")));
		}
		else {
			System.exit(1);
		}
		}else {
			logger.info("No data available to send ACK");
			System.exit(1);
		}
	}

	private void sendOmlAck() throws JsonParseException, JsonMappingException, IOException, HL7Exception {
		ObjectMapper mapper = new ObjectMapper();
		try {
		ResultBean resultBean = mapper.readValue(new File(MainApp.getValueFromPropFile("DPCRRunDataPath")),
				ResultBean.class);
		if("true".equalsIgnoreCase(MainApp.getValueFromPropFile("OMLNegAck").trim())) {
			setAckValidator("200");
			this.ackSender.run(resultBean, ackValidator);
			System.exit(1);
		}else {
			this.ackSender.run(resultBean, ackValidator);
		}
		}catch(Exception e) {
			logger.error("JSON parse Error. \n Error may br due to :\n * Incorrect File path in simulator.properties file. \n * JSON Structure Error");
			System.exit(1);
		}
		
	}

	private void checkForExistingRun() throws JsonParseException, JsonMappingException, IOException, NumberFormatException, HL7Exception, InterruptedException, ParseException, LLPException {
		// TODO Auto-generated method stub
		
		File sampleFile = new File(MainApp.getValueFromPropFile("DPCRRunDataPath"));
		boolean exists = sampleFile.exists();
		
		if (exists) {
			if (canContinue.equalsIgnoreCase("false"))
				logger.info("Your last extraction was Stopped abruptly, do you wants to restart the process?(Y/N)");

			String flag = "Y";
			if (canContinue.equalsIgnoreCase("false")) {
				Scanner scanner = new Scanner(System.in);
				flag = scanner.nextLine();
			}
			if ("Y".equalsIgnoreCase(flag)) {
				
				this.sendESU("OP");
				sendSampleDetails();
				Thread.sleep(Long.parseLong(MainApp.getValueFromPropFile("ResultMessageTimeInterval")));
				this.sendFinalESU();
				Thread.sleep(Long.parseLong(MainApp.getValueFromPropFile("ResultMessageTimeInterval")));
				this.sendESU("OPN");
				Thread.sleep(Long.parseLong(MainApp.getValueFromPropFile("ResultMessageTimeInterval")));
				this.sendESU("ES");
				Thread.sleep(Long.parseLong(MainApp.getValueFromPropFile("ResultMessageTimeInterval")));
				pingMessage();
				
			}else {
				Path path = Paths.get(MainApp.getValueFromPropFile("DPCRRunDataPath"));
				cleanUp(path);
			}
		}
		
	}


	
	private void sendSampleDetails() throws NumberFormatException, InterruptedException, ParseException, LLPException {

		Thread.sleep(Long.parseLong(MainApp.getValueFromPropFile("ResultMessageTimeInterval")));
		if (canContinue.equalsIgnoreCase("false"))
			logger.info("Send Sample details (ORU) to connect?(Y/N)");

		String flag = "Y";
		if (canContinue.equalsIgnoreCase("false")) {
			Scanner scanner = new Scanner(System.in);
			flag = scanner.nextLine();
		}
		if ("Y".equalsIgnoreCase(flag)) {
		this.resultSender.run(getResultBean());	
		}
	}
	
	
	
	private void pingMessage() {
		Timer time = new Timer(); // Instantiate Timer Object
		TaskScheduler st = new TaskScheduler(); // Instantiate TaskScheduler
		Long delay = Long.valueOf(MainApp.getValueFromPropFile("pingDelay"));
		time.schedule(st, 0, delay);
	}
	
	private ResultBean getResultBean() {
		ObjectMapper mapper = new ObjectMapper();
		ResultBean resultBean = new ResultBean();
		try {
			resultBean = mapper.readValue(new File(MainApp.getValueFromPropFile("DPCRRunDataPath")), ResultBean.class);
		} catch (Exception e) {

			logger.error("JSON parse Error. \n");
			logger.error("Error may br due to :\n");
			logger.error("* Incorrect File path in simulator.properties file.\n");
			logger.error("* JSON Structure Error");
			System.exit(1);
		}
		return resultBean;
	}
	
	public void cleanUp(Path path) {
		try {
			Files.delete(path);
		} catch (IOException ioException) {
			logger.error(ioException.getMessage());
		}

	}
	/**
	 * @return the ackValidator
	 */
	public static String getAckValidator() {
		return ackValidator;
	}



	/**
	 * @param ackValidator the ackValidator to set
	 */
	public static void setAckValidator(String ackValidator) {
		DPCRSimulator.ackValidator = ackValidator;
	}



	public String getCanContinue() {
		return canContinue;
	}

	public void setCanContinue(String canContinue) {
		this.canContinue = canContinue;
	}



	/**
	 * @return the runId
	 */
	public static String getRunId() {
		return runId;
	}



	/**
	 * @param runId the runId to set
	 */
	public static void setRunId(String runId) {
		DPCRSimulator.runId = runId;
	}



	/**
	 * @return the filePath
	 */
	public static String getFilePath() {
		return filePath;
	}



	/**
	 * @param filePath the filePath to set
	 */
	public static void setFilePath(String filePath) {
		DPCRSimulator.filePath = filePath;
	}



	/**
	 * @return the hL7Version
	 */
	public static String getHL7Version() {
		return HL7Version;
	}



	/**
	 * @param hL7Version the hL7Version to set
	 */
	public static void setHL7Version(String hL7Version) {
		HL7Version = hL7Version;
	}
	
	



}
