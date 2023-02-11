/**
 * Copyright 2017 Roche Sequencing Solutions
 *
 * Class name: MainApp.java
 * Brief: Entry point for the application.
 */

package com.roche.swam.labsimulator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.roche.swam.labsimulator.common.bl.sim.Simulator;
import com.roche.swam.labsimulator.common.server.EmbeddedJetty;
import com.roche.swam.labsimulator.engine.Engine;
import com.roche.swam.labsimulator.engine.bl.AppConfig;
import com.roche.swam.labsimulator.lpx.LpxPostProcessSimulator;
import com.roche.swam.labsimulator.lpx.LpxPreProcessSimulator;
import com.roche.swam.labsimulator.lpx.LpxSeqPrepSimulator;
import com.roche.swam.labsimulator.mpx.MpxSimulator;
import com.sun.jersey.api.client.ClientResponse;

@SuppressWarnings("restriction")
public class MainApp {

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);
	private Engine engine;
	private EmbeddedJetty server;
	private static String configFilePath;
	private static String deviceName;
	// private static ResourceBundle filepath =
	// ResourceBundle.getBundle("simulator");
	private String adapterURL = getFilepath("hostName") + getFilepath("hostUrl");
	private ClientResponse clientResponse = null;
	private static MainApp mn = null;
	JSONParser parser = new JSONParser();
	private static WebServicesClientResponse webClientResponse=null ;

	public static void main(String[] args) throws Exception {
		mn = new MainApp();
		String deviceName="";
		String continueFlag="";
		webClientResponse = new WebServicesClientResponse();
		if(args.length>0) {
		 deviceName=args[0];
		}
		if(args.length>1) {
			continueFlag=args[1];
		}
		System.out.println(deviceName);
		
		if (getFilepath("LPPre").equalsIgnoreCase(deviceName)) {
			mn.checkAdaptorStatus();
			configFilePath = getFilepath("LP24PreDataJsonPath");
			 setDeviceName(getFilepath("LPPre"));
		} else if (getFilepath("LPPost").equalsIgnoreCase(deviceName)) {
			mn.checkAdaptorStatus();
			configFilePath = getFilepath("LP24PostDataJsonPath");
			setDeviceName(getFilepath("LPPost"));
		} else if (getFilepath("LPSeq").equalsIgnoreCase(deviceName)) {
			mn.checkAdaptorStatus();
			configFilePath = getFilepath("LP24SeqDataJsonPath");
			setDeviceName(getFilepath("LPSeq"));
		} else if (getFilepath("MP").equalsIgnoreCase(deviceName)) {
			configFilePath = getFilepath("MP24DataJsonPath");
			setDeviceName(getFilepath("MP"));
		}else {
			log.error(
					"Invalid or no Arguments. \n valid command is <JAR NAME> <device name> <true/false>. \n valid device names are MagnaPure24,LP-PRE-PCR,LP-POST-PCR,LP-SEQ-PP \n \"Invalid or no Arguments. \\n valid command is <JAR NAME> <true/false>. \n true-for automation Job.\n False- for manual run\");");
			System.exit(1);
		}
		log.info("Inside start method: input for the device is located in : " + configFilePath);

		// launch(args);
		if (StringUtils.isNotBlank(continueFlag)
				&& ("true".equalsIgnoreCase(continueFlag) || "false".equalsIgnoreCase(continueFlag))) {
			mn.start(continueFlag);
		} else {
			log.error(
					"Invalid or no Arguments. \n valid command is <JAR NAME> <device name> <true/false>. \n valid device names are MagnaPure24,LP-PRE-PCR,LP-POST-PCR,LP-SEQ-PP \n \"Invalid or no Arguments. \\n valid command is <JAR NAME> <true/false>. \n true-for automation Job.\n False- for manual run\");");
			System.exit(1);
		}
			
	}

	public void checkAdaptorStatus() {
		Timer timer = new Timer();
		Long delay = Long.valueOf(getFilepath("pingDelay"));
		class TaskScheduler extends TimerTask {
			@Override
			public void run() {
				mn.ping();
			}
		}
		timer.schedule(new TaskScheduler(), 0, delay);
//		 mn.hello();
	}

	/*public void hello() {
		log.info("Simulator started ..\n\n");
		try {
			log.info("Requesting Hello ...");
			Object readJsonObject = readJsonObject(getFilepath("instrumentInformationPath"));
			JSONObject instructmentInfo = (JSONObject) readJsonObject;
			clientResponse = webClientResponse.getResponse(adapterURL + "/hello", MediaType.APPLICATION_JSON, "post",
					instructmentInfo);
			this.displayResponseDetails(clientResponse);
			if (clientResponse.getStatus() != 200) {
				log.info("Something went wrong while calling hello");
			} else {
				log.info("Requesting Hello completed Successfully....");
			}
		} catch (Exception e1) {
			log.error(e1.getMessage());
		}
	}*/

	private Object readJsonObject(String filepath) {
		Object o = null;
		try {
			o = parser.parse(new FileReader(filepath));
		} catch (IOException | ParseException e) {
			log.error("ERR:- MainApp :: readJsonObject() " + e);
		}
		return o;
	}

	public void ping() {
		log.info("Pinging to CONNECT ..");
		try {
			clientResponse = webClientResponse.getResponse(adapterURL + "/ping", MediaType.APPLICATION_JSON, "post", null);
			this.displayResponseDetails(clientResponse);
			if (clientResponse.getStatus() != 200) {
				log.info("Something went wrong while calling ping");
			} else {
				log.info("Ping completed successfully");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	public void displayResponseDetails(ClientResponse response) {
		log.info("Output from Server .... ");
		log.info("Response code: " + response.getStatus());
		log.info("Response body: " + response.getEntity(String.class));
	}

	public EmbeddedJetty getServer() {
		return server;
	}

	public void setServer(EmbeddedJetty server) {
		this.server = server;
	}

	public void start(String canContinueFlag) throws Exception {
		List<Simulator> simulators;
		
		MpxSimulator mpxSimulator;
		LpxPreProcessSimulator lpxPreProcessSimulator;
		LpxPostProcessSimulator lpxPostProcessSimulator;
		LpxSeqPrepSimulator lpxSeqPrepSimulator;
		// Platform.setImplicitExit(true);

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

		/**
		 * The server has to be initialized but not running prior to engine being
		 * created so the server is available to be configured by any of the simulators
		 * that need it. Once the server has been initialized it can be started.
		 */
		this.server = context.getBean(EmbeddedJetty.class);
		this.server.startup();

		this.engine = context.getBean(Engine.class);
		simulators = this.engine.startup();
		this.server.start();
		InstanceUtil.getInstance().setEngine(this.engine);

		if (log.isDebugEnabled()) {
			log.debug("Inside start method: value of simulator lists: " + simulators.toString());
			log.debug("Inside start method: selected device to run: " + getDeviceName());
		}
		
		mpxSimulator = (MpxSimulator) simulators.get(0);
		if("true".equalsIgnoreCase(getFilepath("adhocU01")) && "true".equalsIgnoreCase(getFilepath("Esu_U01"))) {
			InstanceUtil.getInstance().setDeviceState(getFilepath("deviceState"));
			mpxSimulator.startRunU01();
			System.exit(1);
		}else {
			
		if("true".equalsIgnoreCase(getFilepath("Esu_U01"))) {
			Timer timer = new Timer();
			Long delay = Long.valueOf(getFilepath("pingDelay"));
			class TaskScheduler extends TimerTask {
				@Override
				public void run() {
					try {
						mpxSimulator.startRunU01();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			timer.schedule(new TaskScheduler(), 0, delay);
		}
		
		if ("MagnaPure24".equalsIgnoreCase(getDeviceName())) {
			
//			mpxSimulator = (MpxSimulator) simulators.get(0);
			mpxSimulator.setCanContinue(canContinueFlag);
			mpxSimulator.startProcess();
			
		} else if ("LP-PRE-PCR".equalsIgnoreCase(getDeviceName())) {

			lpxPreProcessSimulator = (LpxPreProcessSimulator) simulators.get(1);
			lpxPreProcessSimulator.setCanContinue(canContinueFlag);
			lpxPreProcessSimulator.startProcess();

		} else if ("LP-POST-PCR".equalsIgnoreCase(getDeviceName())) {
			lpxPostProcessSimulator = (LpxPostProcessSimulator) simulators.get(2);
			lpxPostProcessSimulator.setCanContinue(canContinueFlag);
			lpxPostProcessSimulator.startProcess();

		} else if ("LP-SEQ-PP".equalsIgnoreCase(getDeviceName())) {
			lpxSeqPrepSimulator = (LpxSeqPrepSimulator) simulators.get(3);
			lpxSeqPrepSimulator.setCanContinue(canContinueFlag);
			lpxSeqPrepSimulator.startProcess();
			
		}
		}
		this.stop();

	}
	
	// @Override
	public void stop() {
		this.engine.shutdown();
		this.server.shutdown();
		// exit();

	}

	public static String getFilepath(String string) {
		Properties prop = new Properties();
		try {
			FileReader fr = new FileReader("simulator.properties");
			prop.load(fr);

		} catch (Exception e) {
			log.error("ERR:- MainApp :: getFilepath() ");
		}

		return prop.getProperty(string);
	}

	public static String getClasspath() {
		String classPath = "";
		try {
			File file = new File("./");
			File directory = new File(file.getCanonicalPath() + "/" + MainApp.getDeviceName());
			if (!directory.exists()) {
				directory.mkdir();
			}
			classPath = directory.getCanonicalPath();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return classPath;
	}

	public static String getDeviceName() {
		return deviceName;
	}

	public static void setDeviceName(String deviceName) {
		MainApp.deviceName = deviceName;
	}
}
