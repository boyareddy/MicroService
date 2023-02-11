package com.roche.swam.labsimulator.mpx.bl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TimerTask;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.common.bl.hl7.MessageSender;
import com.roche.swam.labsimulator.mpx.Mp96Simulator;
import com.roche.swam.labsimulator.util.ConsumablesList;
import com.roche.swam.labsimulator.util.InputListBean;
import com.roche.swam.labsimulator.util.LpConstants;
import com.roche.swam.labsimulator.util.LpInputSample;
import com.roche.swam.labsimulator.util.Mp96RunData;
import com.roche.swam.labsimulator.util.Mp96SampleData;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

@Component
public class QueryMessageSender extends TimerTask implements MessageSender{
	
	@Autowired
	private ResultMessageSender resultSender;
	@Autowired
	private AckMessageSender ackMessageSender;
	@Autowired
	private Mp96SampleRepository samples;
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryMessageSender.class);
	public void run() {
		LOGGER.info("inside QBS method");
		
		
		
		try {
			QueryMessageBuilder builder = new QueryMessageBuilder();
			Message queryMessage = null;
			int timeDelay = 600000;
			queryMessage = builder.build();

			LOGGER.info("Request from Device: " + queryMessage.toString());

			try  {

				System.out.println("Connection established: " + Mp96Simulator.getSocket().isConnected());
				StringBuffer qbpQueryMessage = new StringBuffer(queryMessage.toString());

				InputStream in = Mp96Simulator.getSocket().getInputStream();
				OutputStream out = Mp96Simulator.getSocket().getOutputStream();
				String qbpQueryMessageWrapper = "\u000b" + qbpQueryMessage.toString() + "\u001c" + "\r";

				// Send the MLLP-wrapped HL7 message to the server
				out.write(qbpQueryMessageWrapper.getBytes());

				DataInputStream input = new DataInputStream(in);
				BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));

				StringBuilder sb = new StringBuilder();
				String ormData;
				while (((ormData = reader.readLine()) != null) && (!ormData.trim().equals(""))) {
					sb.append("\n" + ormData);
				}
				String response = sb.toString().trim();

				if (response.contains("ORM^O01")) {
					LOGGER.info("Response from Connect: " + response.trim());
					
					try(FileWriter responseWriter = new FileWriter(
							MainApp.getClasspath() + "/" + MainApp.getFilepath("ResponseMesssageTxt"), true)){
						
						responseWriter.write("\n" + response.trim()+ "\n");
						responseWriter.close();
						processOrmMessage(response.trim());
					}

				}else if(SimulatorVariables.valueOf("FALSE").toString().equalsIgnoreCase(Mp96Simulator.canContinue)){
					loadPreviousSamples();
				}else if(SimulatorVariables.valueOf("TRUE").toString().equalsIgnoreCase(Mp96Simulator.canContinue)){
					runForAutomation();						
				}

			} catch (Exception e) {
				LOGGER.error("Closing Connection :" + e.getLocalizedMessage());
			}

		} catch (HL7Exception e) {
				e.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		

	}

	
	private void processOrmMessage(String response) {
		// TODO Auto-generated method stub

		InputStream resource = new ByteArrayInputStream(response.getBytes());
		String OrmMsg = "";
		String ackCheck="";
		try {
			OrmMsg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		} catch (IOException e2) {
			// TOrmmsgODO Auto-generated catch block
			e2.printStackTrace();
		}
		String[] messageSplit = OrmMsg.split("\r");
	
		try {
		
		Mp96RunData mp96RunData = new Mp96RunData();
		List<Mp96SampleData> mp96SampleDataList = new ArrayList<>();
		
		if(messageSplit.length>1) {
			
			
			if((messageSplit[0].startsWith("MSH")||messageSplit[0].contains("MSH")) && messageSplit[0].length()>1 ){
				
				String[] mshSplit = messageSplit[0].split("\\|");
					if (mshSplit.length >= 9 && StringUtils.isNotBlank(mshSplit[8])) {
						String[] eventSplit = mshSplit[8].split("\\^");
						if (eventSplit.length < 1 && StringUtils.isBlank(eventSplit[0])
								&& "ORM".equalsIgnoreCase(eventSplit[0])) {

							ackCheck = ackCheck + SimulatorVariables.valueOf("UMT").toString();
						}

						if (eventSplit.length < 2 && StringUtils.isBlank(eventSplit[1])
								&& "O01".equalsIgnoreCase(eventSplit[1])) {

							ackCheck = ackCheck + SimulatorVariables.valueOf("UEC").toString();
						}
					}
				
				
				if(mshSplit.length>=10 && StringUtils.isNotBlank(mshSplit[9])) {
					mp96RunData.setDeviceId(mshSplit[9]);
				}
				
				if(mshSplit.length>=11) {
					if(!"P".equalsIgnoreCase(mshSplit[10].trim()) && !"T".equalsIgnoreCase(mshSplit[10].trim()) ) {
						ackCheck = ackCheck + SimulatorVariables.valueOf("UPI").toString();
					}
				}
				
			}
			if(messageSplit[1].startsWith("ORC") && messageSplit[1].length()>1 ){
				
				String[] mshSplit = messageSplit[1].split("\\|");
			
				if(mshSplit.length>=3 && StringUtils.isNotBlank(mshSplit[2])) {
					mp96RunData.setRunId(mshSplit[2]);
				}else {
					ackCheck=ackCheck+SimulatorVariables.valueOf("RFM").toString();
				}
				if(mshSplit.length>=11 && StringUtils.isNotBlank(mshSplit[10])) {
					mp96RunData.setOperatorName(mshSplit[10]);
				}else {
					mp96RunData.setOperatorName("admin");
				}
				if(mshSplit.length>=4 && StringUtils.isNotBlank(mshSplit[3])) {
					mp96RunData.setRunComments(mshSplit[3]);
				}
			}
			
			for(int i=2; i < messageSplit.length;i++) {
				
				if(messageSplit[i].startsWith("OBR") && messageSplit[i].length()>1 ){
					Mp96SampleData mp96SampleData = new Mp96SampleData();
					String[] obrSplit = messageSplit[i].split("\\|");
					
					if(obrSplit.length>1) {
						if(obrSplit.length>=3 && StringUtils.isNotBlank(obrSplit[2])) {
						
							mp96SampleData.setAccessioningId(obrSplit[2]);
						}else {
							ackCheck=ackCheck+SimulatorVariables.valueOf("RFM").toString();
						}
						if(obrSplit.length>=4 &&StringUtils.isNotBlank(obrSplit[3])) {
							
							String[] inputContainerSplit = obrSplit[3].split("\\^");
							if(inputContainerSplit.length>=1 && StringUtils.isNotBlank(inputContainerSplit[0])) {
							mp96SampleData.setInputContainerId(inputContainerSplit[0]);
							}
							else {
								ackCheck=ackCheck+SimulatorVariables.valueOf("RFM").toString();
							}
							if(inputContainerSplit.length>=2 && StringUtils.isNotBlank(inputContainerSplit[1])) {
							mp96SampleData.setInputPosition(inputContainerSplit[1]);
							}
							else {
								ackCheck=ackCheck+SimulatorVariables.valueOf("RFM").toString();
							}
						}
						if(obrSplit.length>=5 &&StringUtils.isNotBlank(obrSplit[4])) {
							
							String[] reagentSplit = obrSplit[4].split("\\^");
							mp96SampleData.setReagentName(reagentSplit[0]);
							mp96SampleData.setReagentVersion(reagentSplit[1]);
							
						}
						if (obrSplit.length>=16 && StringUtils.isNotBlank(obrSplit[15])) {

							String[] protocolSplit = obrSplit[15].split("\\^");
							mp96SampleData.setProtocolName(protocolSplit[0]);
							mp96SampleData.setProtocolVersion(protocolSplit[1]);

						}
						if (obrSplit.length>=19 && StringUtils.isNotBlank(obrSplit[18])) {

							
							mp96SampleData.setSampleVolume(obrSplit[18]);
						}
						if (obrSplit.length>=20 && StringUtils.isNotBlank(obrSplit[19])) {

							mp96SampleData.setEluateVolume(obrSplit[19]);
						}

					}
					
					if (messageSplit[i+1].startsWith("NTE")) {
						String[] nteSplit = messageSplit[i+1].split("\\|");

						if (nteSplit.length>=4 && StringUtils.isNotBlank(nteSplit[3])) {

							mp96SampleData.setSampleComments(nteSplit[3]);
						}

					}
					mp96SampleData.setOutputContainerIdEmpty(SimulatorVariables.valueOf("FALSE").toString());
					mp96SampleData.setOutputContainerPositionEmpty(SimulatorVariables.valueOf("FALSE").toString());
					mp96SampleData.setInstrumentSerialNo(mp96RunData.getDeviceId());
					mp96SampleDataList.add(mp96SampleData);
				}	
			}
			mp96RunData.setResultMessageEvent("R21");
			mp96RunData.setResultMessageType("OUL");
			mp96RunData.setProcessingId("P");
			mp96RunData.setSamples(mp96SampleDataList);
			
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
				List<Mp96RunData> mp96RunDataList = addToMP96JsonFile(mp96RunData);
				mapper.writerWithDefaultPrettyPrinter().writeValue(new File(MainApp.getFilepath("MP96DataJsonPath")),
						mp96RunDataList);
				try{
					ackMessageSender.run(mp96RunData, ackCheck);
					if (SimulatorVariables.valueOf("FALSE").toString().equalsIgnoreCase(Mp96Simulator.canContinue)){
						loadPreviousSamples();
					}else if(SimulatorVariables.valueOf("TRUE").toString().equalsIgnoreCase(Mp96Simulator.canContinue)){
						runForAutomation();						
					}
				}catch(Exception e){
					LOGGER.error("Error while sending the ACK to Connect" + e.getMessage());
				}				
				//Thread.sleep(Long.parseLong(MainApp.getFilepath("AutomationTimeout")));
				//this.startExtraction(ackCheck);
			
			
		} catch (IOException e) {
			
			LOGGER.error("Error while writing ORM data to JSON" + e.getMessage());
		}

		}catch (Exception e) {
			LOGGER.error("Error while processing ORM data" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}




	@Override
	public void run(Mp96RunData Mp96RunData, String ackCheck) {
		// TODO Auto-generated method stub
	}
	
	public void runForAutomation() throws Exception{
		String containerId = Mp96Simulator.containerId;
		List<String> validContainers = new ArrayList<String>();
		List<Mp96RunData> mp96RunDataLists = loadConatiners();
		if(mp96RunDataLists!=null && !mp96RunDataLists.isEmpty() && containerId!=null){
			LOGGER.info("Available Containers");
			for(int i=0;i<mp96RunDataLists.size();i++){
				String container = mp96RunDataLists.get(i).getSamples().get(0).getInputContainerId();
				LOGGER.info(i + "." + container);
				validContainers.add(container);
			}
			if(validContainers.contains(containerId)){
				List<Mp96RunData> tempMP96RunDataList = loadConatiners();
				for(Mp96RunData mp96RunDataObject : tempMP96RunDataList){
					if(mp96RunDataObject.getSamples().get(0).getInputContainerId().equalsIgnoreCase(containerId)){
						this.startRun(mp96RunDataObject);
					}
				}
				removeContainerFromMP96JsonFile(containerId);
			}else{
				LOGGER.info("Provided container is not available, trying again!!");
			}
		}else if(containerId==null){
			LOGGER.info("Please provide containerId.");
		}
	}
	
	public void loadPreviousSamples() throws Exception {
		if (SimulatorVariables.valueOf("FALSE").toString().equalsIgnoreCase(Mp96Simulator.canContinue)) {
			String containerId = "";
			List<String> validContainers = new ArrayList<String>();
			List<Mp96RunData> mp96RunDataList = loadConatiners();
			if(mp96RunDataList!=null && !mp96RunDataList.isEmpty()){
				LOGGER.info("Please enter the containerId from the below options.Else enter 'N' to reload containers.");
				for(int i=0;i<mp96RunDataList.size();i++){
					String container = mp96RunDataList.get(i).getSamples().get(0).getInputContainerId();
					LOGGER.info(i + "." + container);
					validContainers.add(container);
				}
				Scanner scannerIn = new Scanner(System.in);
				containerId = scannerIn.nextLine();
				if(validContainers.contains(containerId)){
					List<Mp96RunData> tempMP96RunDataList = loadConatiners();
					for(Mp96RunData mp96RunDataObject : tempMP96RunDataList){
						if(mp96RunDataObject.getSamples().get(0).getInputContainerId().equalsIgnoreCase(containerId)){
							this.startRun(mp96RunDataObject);
						}
					}
					removeContainerFromMP96JsonFile(containerId);
				}else if(containerId.equalsIgnoreCase("N")){
					
				}else{
					LOGGER.info("Container is not listed, Please select a valid container");
				}
			}else{
				LOGGER.info("No containers available");
			}
		}
	}
	
	public List<Mp96RunData> loadConatiners() throws JsonParseException, JsonMappingException, IOException{
		List<Mp96RunData> mp96RunDataList = null;
		ObjectMapper mapper = new ObjectMapper();
		if(Files.exists(Paths.get(MainApp.getFilepath("MP96DataJsonPath")))){
			mp96RunDataList = mapper.readValue(new File(MainApp.getFilepath("MP96DataJsonPath")),mapper.getTypeFactory().constructCollectionType(List.class,Mp96RunData.class));		
		}
		return mp96RunDataList;
	}
	
	public void removeContainerFromMP96JsonFile(String containerId) throws JsonParseException, JsonMappingException, IOException, Exception{
		List<Mp96RunData> mp96RunDataList = null;
		ObjectMapper mapper = new ObjectMapper();
		if(Files.exists(Paths.get(MainApp.getFilepath("MP96DataJsonPath")))){
			mp96RunDataList = mapper.readValue(new File(MainApp.getFilepath("MP96DataJsonPath")),mapper.getTypeFactory().constructCollectionType(List.class,Mp96RunData.class));		
			Iterator<Mp96RunData> it = mp96RunDataList.iterator();
			while(it.hasNext()){
				Mp96RunData mp96RunDataObject = it.next();
				if(mp96RunDataObject.getSamples().get(0).getInputContainerId().equalsIgnoreCase(containerId)){
					it.remove();
				}
			}
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(MainApp.getFilepath("MP96DataJsonPath")),mp96RunDataList);
			Thread.sleep(Long.parseLong(MainApp.getFilepath("AutomationTimeout")));
		}		
	}
	
	public List<Mp96RunData> addToMP96JsonFile(Mp96RunData mp96RunData) throws JsonParseException, JsonMappingException, IOException{
		List<Mp96RunData> mp96RunDataList = new ArrayList<>();
		List<Mp96RunData> tempMP96RunDataList = loadConatiners();
		if(tempMP96RunDataList!=null){
			mp96RunDataList = tempMP96RunDataList;
			Iterator<Mp96RunData> it = mp96RunDataList.iterator();
			String containerId = mp96RunData!=null ? mp96RunData.getSamples().get(0).getInputContainerId():"";
			while(it.hasNext()){
				Mp96RunData mp96RunDataObject = it.next();
				if(mp96RunDataObject.getSamples().get(0).getInputContainerId().equalsIgnoreCase(containerId)){
					it.remove();
				}
			}
		}
		mp96RunDataList.add(mp96RunData);
		return mp96RunDataList;
	}
	public void startExtraction(String ackCheck) throws  IOException,  HL7Exception, InterruptedException {
		String flag= null;
		
		if(SimulatorVariables.valueOf("FALSE").toString().equalsIgnoreCase(Mp96Simulator.canContinue)){
		LOGGER.info("Work Order Import recieved!! Do you want to process (Y/N)??");
		
		Scanner scanner = new Scanner(System.in);
		
		 flag =scanner.nextLine();
		}
		else {
			flag="Y";
		}
		if("Y".equalsIgnoreCase(flag)) {
		
		if(MainApp.checkSampleDataJson())
		{
			ObjectMapper mapper = new ObjectMapper();
			Mp96RunData mp96RunData = mapper.readValue(new File(MainApp.getFilepath("MP96DataJsonPath")), Mp96RunData.class);
			System.out.println(mp96RunData.toString());
			ackMessageSender.run(mp96RunData,ackCheck);
			Thread.sleep(Long.parseLong(MainApp.getFilepath("DeviceAckTimeout")));
			mp96RunData = null;
			Mp96RunData mp96OULRunData = mapper.readValue(new File(MainApp.getFilepath("MP96DataJsonPath")), Mp96RunData.class);
			System.out.println(mp96OULRunData.toString());
			startRun(mp96OULRunData);
		}
		}else if("N".equalsIgnoreCase(flag)) {
			System.exit(1);
		}
			
			else {
				startExtraction(ackCheck);
		}
	}
	
	public void startRun(Mp96RunData mp96RunData) throws IOException {
		if(Mp96Simulator.canContinue.equalsIgnoreCase(SimulatorVariables.valueOf("FALSE").toString()))
			LOGGER.info("Samples have been loaded successfully, do you want to start the process (Y/N) ?:");

		try {
			String flag = "Y";
			if(Mp96Simulator.canContinue.equalsIgnoreCase(SimulatorVariables.valueOf("FALSE").toString())) {
				Scanner scanner = new Scanner(System.in);
				flag = scanner.nextLine();
			}
			if ("Y".equalsIgnoreCase(flag)) {
				
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					LOGGER.error(e.getMessage());
					Thread.currentThread().interrupt();
				}
				LOGGER.info("NA Extraction is completed successfully, Sending the Status to connect");
				
				this.resultSender.run(mp96RunData);
				
				LOGGER.info("Extraction Completed, feeding sample data to LP24");
				
				upadateLP24Json(mp96RunData);

				//Path path = Paths.get(MainApp.getFilepath("MP96DataJsonPath"));
				//cleanUp(path);
				LOGGER.info("MP 96 Run complete !!");

			} else {
				System.exit(1);
			}
		}finally {
		}

	}
	private void upadateLP24Json(Mp96RunData mp96RunData) throws JsonGenerationException, JsonMappingException, IOException {
		List<LpInputSample> sampleList = new ArrayList<>();

		InputListBean inputListBean = new InputListBean();
		
		for(Mp96SampleData mp96Data: mp96RunData.getSamples()) {
			if("P".equalsIgnoreCase(mp96Data.getSampleResults())) {
			LpInputSample lpInputSample = new LpInputSample();
			lpInputSample.setOutputContainerId("");
			lpInputSample.setOutputPosition("");
			lpInputSample.setInputContainerId(mp96Data.getOutputContainerId());
			lpInputSample.setInputPosition(mp96Data.getOutputContainerposition());
			lpInputSample.setSendingFacilityName(LpConstants.FACLITY);
			lpInputSample.setReceivingFacilityName(LpConstants.FACLITY);
			lpInputSample.setReceivingApplicationName(LpConstants.APPLICATION);
			lpInputSample.setQueryProcessingId(LpConstants.PROCESSINGID);
			lpInputSample.setResultProcessingId(LpConstants.PROCESSINGID);
			lpInputSample.setCharacterSet(LpConstants.CHARACTERSET);
			lpInputSample.setVersionId(LpConstants.VERSION);
			lpInputSample.setMessageQueryName(LpConstants.MESSAGEQUERYNAME);
			lpInputSample.setResult(LpConstants.SAMPLERESULTS);
			lpInputSample.setInternalControls(LpConstants.INTERNALCONTROLS);
			lpInputSample.setProcessingCartridge(LpConstants.PROCESSINGCATRIDGE);
			lpInputSample.setTipRack(LpConstants.TIPRACK);
			lpInputSample.setSampleId(mp96Data.getAccessioningId());
			lpInputSample.setQueryMsgType("QBP");
			lpInputSample.setQueryMsgEvent("WOS");
			lpInputSample.setResultMsgType("SSU");
			lpInputSample.setResultMsgEvent("U03");
			lpInputSample.setOutputPositionEmpty("false");
			lpInputSample.setOutputContainerIdEmpty("false");
			ObjectMapper mapper = new ObjectMapper();
			InputStream  inputStream =this.getClass().getClassLoader().getResourceAsStream("Simulator/InputData/LP24Consumables.json");
			File file = new File("temp.json");
					OutputStream outputStream = new FileOutputStream(file);
					IOUtils.copy(inputStream, outputStream);
					outputStream.close();
			ConsumablesList consumableList =  mapper.readValue(file, ConsumablesList.class);
			LOGGER.info("List !!"+consumableList);
			lpInputSample.setConsumables(consumableList.getConsumables());
			sampleList.add(lpInputSample);
			}
			
			
			
		}
		inputListBean.setSamples(sampleList);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(MainApp.getFilepath("LP24DataJsonPath")), inputListBean);
		cleanUp(Paths.get("temp.json"));
		
	}



	public void cleanUp(Path path) {
		try {
			Files.delete(path);
		} catch (IOException ioException) {
			LOGGER.error(ioException.getMessage());
		}
	}


	@Override
	public void run(Mp96RunData mp96RunData) throws HL7Exception, IOException {
		// TODO Auto-generated method stub
		
	}
		
}
