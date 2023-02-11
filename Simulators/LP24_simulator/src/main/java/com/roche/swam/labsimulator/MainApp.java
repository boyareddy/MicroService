/**
 * Copyright 2017 Roche Sequencing Solutions
 *
 * Class name: MainApp.java
 * Brief: Entry point for the application.
 */

package com.roche.swam.labsimulator;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.roche.swam.labsimulator.common.bl.sim.Simulator;
import com.roche.swam.labsimulator.common.server.EmbeddedJetty;
import com.roche.swam.labsimulator.engine.Engine;
import com.roche.swam.labsimulator.engine.bl.AppConfig;
import com.roche.swam.labsimulator.lpx.LpxSimulator;



@SuppressWarnings("restriction")
public class MainApp  {

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);
	private Engine engine;
	private EmbeddedJetty server;
	 private static String configFilePath;
	 private static String deviceName;

	public static void main(String[] args) throws Exception {

		configFilePath = getFilepath("LP24DataJsonPath");
		setDeviceName(getFilepath("LP"));

		log.info("Inside start method: input for the device is located in : " + configFilePath);

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

	public void start(String canContinueFlag) throws Exception {
		List<Simulator> simulators;
		LpxSimulator lpxSimulator;
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
		
			
			log.debug("Inside start method: value of simulator lists: "+simulators.toString());
			log.debug("Inside start method: selected device to run: "+getDeviceName());
			lpxSimulator = (LpxSimulator) simulators.get(0);
			if("true".equalsIgnoreCase(getFilepath("adhocU01")) && "true".equalsIgnoreCase(getFilepath("Esu_U01"))) {
				InstanceUtil.getInstance().setDeviceState(getFilepath("deviceState"));
				lpxSimulator.startRunU01();
				System.exit(1);
			}else {
				
			if("true".equalsIgnoreCase(getFilepath("Esu_U01"))) {
				Timer timer = new Timer();
				Long delay = Long.valueOf(getFilepath("pingDelay"));
				class TaskScheduler extends TimerTask {
					@Override
					public void run() {
						try {
							lpxSimulator.startRunU01();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				timer.schedule(new TaskScheduler(), 0, delay);
			}
			lpxSimulator.setCanContinue(canContinueFlag);
			lpxSimulator.startProcess();
			}
		
		this.stop();

	}

	//@Override
	public void stop() {
		this.engine.shutdown();
		this.server.shutdown();
		//exit();
		
		
	}

	public static  String getFilepath(String string) {
		Properties prop = new Properties();
		 try {
			FileReader fr= new FileReader("simulator.properties");
			
			prop.load(fr);
			
		} catch (Exception e) {
			log.error("Mainapp: error in reading properties file: "+e.getMessage());
		}
		
		
		return prop.getProperty(string);
	}
	
	
	public static String getClasspath() {
		String classPath = "";
		try {
			File file = new File("./");
			File directory = new File(file.getCanonicalPath()+"/"+ MainApp.getDeviceName());
			if (!directory.exists()) {
			 directory.mkdir(); 
			}
			classPath=directory.getCanonicalPath();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Mainapp: error in Finding classpath: "+e.getMessage());
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
