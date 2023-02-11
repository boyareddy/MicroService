package com.roche.swam.labsimulator.mpx;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.common.bl.sim.BasicHL7Simulator;
import com.roche.swam.labsimulator.mpx.bl.AckMessageSender;
import com.roche.swam.labsimulator.mpx.bl.Mp96SampleRepository;
import com.roche.swam.labsimulator.mpx.bl.QueryMessageSender;
import com.roche.swam.labsimulator.util.Mp96RunData;

import ca.uhn.hl7v2.HL7Exception;


@Component
@Scope("prototype")
public class Mp96Simulator extends BasicHL7Simulator {

	private static final Logger logger = LoggerFactory.getLogger(Mp96Simulator.class);
	@Autowired
	private QueryMessageSender querySender;
	@Autowired
	private Mp96SampleRepository samples;
	
	@Autowired
	private AckMessageSender ackMessageSender;
	
	private boolean isRunning;
	public static String canContinue;
	
	public static Socket socket;
	
	public static String containerId;

	public static String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public String getCanContinue() {
		return canContinue;
	}

	public void setCanContinue(String canContinue) {
		this.canContinue = canContinue;
	}
	
	public Mp96Simulator() {
		super();
	}



	public void startProcess() throws  IOException,  HL7Exception, InterruptedException {
		
		createConnection();
		//checkOUL();
		processWOR();
		//closeConnection();
		
			

	}

	private void closeConnection() throws IOException {
		// TODO Auto-generated method stub
		this.getSocket().close();
	}

	private void createConnection() throws NumberFormatException, UnknownHostException, IOException {
		Socket socket = new Socket(MainApp.getFilepath("HostName"),
				Integer.parseInt(MainApp.getFilepath("HostPort")));
		socket.setSoTimeout(600000);
		this.setSocket(socket);
	}

	private void checkOUL() throws JsonParseException, JsonMappingException, IOException, HL7Exception, InterruptedException {
		// TODO Auto-generated method stub
		
		File sampleFile = new File(MainApp.getFilepath("MP96DataJsonPath"));
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
				ObjectMapper mapper = new ObjectMapper();
				Mp96RunData mp96OULRunData = mapper.readValue(new File(MainApp.getFilepath("MP96DataJsonPath")),
						Mp96RunData.class);
				
				System.out.println(mp96OULRunData.toString());
				this.querySender.startRun(mp96OULRunData);
			}else {
				this.processWOR();
			}
		}else {
			this.processWOR();
		}
		
	}

	@Async
	private void processWOR() {
		
		int queryDelay = Integer.parseInt(MainApp.getFilepath("Timer"));
		 Timer timer = new Timer();
	        Date executionDate = new Date(); // no params = now
	        logger.info("Work order request Scheduled at "+executionDate);
	        timer.scheduleAtFixedRate(this.querySender, executionDate, queryDelay);
		
	}

	/**
	 * @return the socket
	 */
	public static Socket getSocket() {
		return socket;
	}

	/**
	 * @param socket the socket to set
	 */
	public static void setSocket(Socket socket) {
		Mp96Simulator.socket = socket;
	}


	

	



}
