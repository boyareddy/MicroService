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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.roche.swam.labsimulator.common.bl.sim.Simulator;
import com.roche.swam.labsimulator.common.server.EmbeddedJetty;
import com.roche.swam.labsimulator.engine.Engine;
import com.roche.swam.labsimulator.engine.bl.AppConfig;
import com.roche.swam.labsimulator.mpx.Mp96Simulator;



@SuppressWarnings("restriction")
public class MainApp  {

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);
	private Engine engine;
	private EmbeddedJetty server;
	 private static String deviceName;

	public static void main(String[] args) throws Exception {
		MainApp mn = new MainApp();
		
		
		String continueFlag="";
		if(args.length>0 && StringUtils.isNotBlank(args[0])){
			 continueFlag = args[0];
			
		}else {
			log.error(
					"Invalid or no Arguments. \n valid command is <JAR NAME> <true/false>. \n true-for automation Job.\n False- for manual run");
			System.exit(1);
		}
		if (StringUtils.isNotBlank(continueFlag)
				&& ("true".equalsIgnoreCase(continueFlag) || "false".equalsIgnoreCase(continueFlag))) {
			if (args.length > 1)
				mn.start(continueFlag, args[1]);
			else
				mn.start(continueFlag);
		} else {
			log.error(
					"Invalid or no Arguments. \n valid command is <JAR NAME> <true/false>. \n true-for automation Job.\n False- for manual run");
			System.exit(1);
		}
	}
	
	public EmbeddedJetty getServer() {
		return server;
	}



	public void setServer(EmbeddedJetty server) {
		this.server = server;
	}

	public void start(String canContinueFlag,String...containerId) throws Exception {
		List<Simulator> simulators;
		Mp96Simulator mp96Simulator;

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
		
		if(!simulators.isEmpty()) {
		mp96Simulator = (Mp96Simulator) simulators.get(0);
		mp96Simulator.setCanContinue(canContinueFlag);
		if(containerId.length>0)
			mp96Simulator.setContainerId(containerId[0]);
		mp96Simulator.startProcess();
		}
		
		this.stop();

	}

	//@Override
	public void stop() throws IOException {
		this.engine.shutdown();
		this.server.shutdown();
		
		
	}

	public static  String getFilepath(String string) {
		Properties prop = new Properties();
		 try {
			FileReader fr= new FileReader("simulator.properties");
			
			prop.load(fr);
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		
		return prop.getProperty(string);
	}
	
	
	public static String getClasspath() {
		String classPath = "";
		try {
			File file = new File("./");
			File directory = new File(file.getCanonicalPath()+"/"+ MainApp.getFilepath("DeviceType"));
			if (!directory.exists()) {
			 directory.mkdir(); 
			}
			classPath=directory.getCanonicalPath();
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
	
	public static boolean checkSampleDataJson() {
		File sampleFile = new File(MainApp.getFilepath("MP96DataJsonPath"));
		return 	sampleFile.exists();
		
	}
	
}
