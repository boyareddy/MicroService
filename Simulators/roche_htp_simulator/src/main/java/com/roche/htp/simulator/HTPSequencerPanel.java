package com.roche.htp.simulator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.roche.htp.simulator.utils.UrlConstants;
import com.sun.jersey.api.client.ClientResponse;

public class HTPSequencerPanel {
    
    String adapterURL = readProperties("hostName") + readProperties("hostUrl");
    
    public static final Logger logger = Logger.getLogger(HTPSequencerPanel.class);
    ClientResponse response = null;
    JSONParser parser = new JSONParser();
    Map<String, Object> runResultsMap = new HashMap<>();
    WebServicesClientResponse webService = new WebServicesClientResponse();
    
    private SimpleDateFormat cycleDateFormater = new SimpleDateFormat("yyMMdd");
    private SimpleDateFormat fileFormater = new SimpleDateFormat("yyyyMMdd");
    private static final String DESCRIPTION = "description";
    private static final String ENGLISH_DESCRIPTION = "english_description";
    private static final String ERROR = "error";
    private static final String WARNING = "warning";
    private static final String INFORMATIONAL = "informational";
    private static final String REFERENCE_ID = "reference_id";
    private static final String RUNS = "/runs/";
    private static final String RUN_STATUS = "run_status";
    private static final String RUN_ID = "run_id";
    private static final String PATH_DELIMITER = "/";
    private static final String CYCLE = "/cycle";
    
    int noOfCycles = 0;
    long statusTimeDelay = 0L;
    String tmpFolderPath = null;
    Object configObj = null;
    String complexId = null;
    String deviceId = null;
    static JSONObject runInfo = null;
    static JSONObject updateRunInfo = null;
    static JSONObject notifications = null;
    String[] fileType = null;
    String runId = null;
    static String runIdForUpdate = null;
    String referenceId = null;
    static Boolean flag = false;
    static String optionList = null;
    String runStatus = null;
    static Scanner scanner = null;
    List<String> fileTypeList = new ArrayList<>();
    static String hashingAlgorithm = null;
    
    public void getConfig() {
        logger.info("Loading HTP-simulator configurations");
        try {
            configObj = readJsonObject("configPath");
            runInfo = (JSONObject) readJsonObject("runPath");
            notifications = (JSONObject) readJsonObject("notificationPath");
            updateRunInfo = (JSONObject) readJsonObject("updateRunPath");
            JSONObject configInfo = (JSONObject) configObj;
            noOfCycles = Integer.parseInt(runInfo.get("total_cycles").toString());
            tmpFolderPath = configInfo.get("tmpFolderPath").toString();
            hashingAlgorithm = configInfo.get("hashingAlgorithm").toString();
            
            statusTimeDelay = Integer.parseInt(configInfo.get("statusTimeDelay").toString());
            complexId = runInfo.get("complex_id").toString();
            deviceId = runInfo.get("instrument_id").toString();
            runId = runInfo.get(RUN_ID).toString();
            JSONArray jsonArray = (JSONArray) configInfo.get("fileType");
            for (int i = 0; i < jsonArray.size(); i++) {
                fileTypeList.add((String) jsonArray.get(i));
            }
        } catch (Exception e) {
            logger.error(String.format("Loading configurations failed ", e.getMessage()));
        }
        
    }
    
    public static void main(String[] args) throws InterruptedException {
        HTPSequencerPanel htpSequencerPanel = new HTPSequencerPanel();
        DOMConfigurator.configure("resources//log4j.xml");
        htpSequencerPanel.getConfig();
        Timer time = new Timer(); // Instantiate Timer Object
        TaskScheduler st = new TaskScheduler(); // Instantiate TaskScheduler
        Long delay = Long.valueOf(htpSequencerPanel.readProperties("pingDelay"));
        htpSequencerPanel.hello();
        time.schedule(st, 0, delay);
        logger.info(" \n -1.Ping,\n 1. GET ORDER DETAILS,\n 2. POST RUN INFO ,\n 3. GET RUN DETAILS \n 4. UPDATE RUN INFO \n "
            + "5. UPDATE RUN STARTED STATUS \n 6. UPDATE RUN INPROGRESS STATUS \n 7. UPDATE RUN COMPLETED STATUS \n "
            + "8. UPDATE CYCLE CONTINUE \n 9. COMPLETE CYCLE(Pass the cycle number) \n 10. TRANSFER COMPLETE \n 11. GOOD BYE \n 12. NOTIFICATION \n ");
        logger.info("Please select any value to continue");
        
        switch(args[0]){
        	case "-1" :
        		//pinging to connect
        		break;
            case "1" :
                htpSequencerPanel.getOrderDetailsByComplexId();
                break;
            case "2" :
                htpSequencerPanel.runs(runInfo);
                break;
            case "3" :
                htpSequencerPanel.getRunDetailsByRunId(runInfo.get(RUN_ID).toString());
                break;
            case "4" :
                htpSequencerPanel.runUpdateByRunId(updateRunInfo.get(RUN_ID).toString());
                break;
            case "5" :
                  htpSequencerPanel.updateRunByStarted(runInfo.get(RUN_ID).toString());
                break;
            case "6" :
                htpSequencerPanel.updateRunByInProcess(runInfo.get(RUN_ID).toString());
                break;
            case "7" :
                htpSequencerPanel.updateFinalRun(runInfo.get(RUN_ID).toString());
                break;
            case "8" :
                int cycle = Integer.parseInt(args[1]);
                htpSequencerPanel.iterateCycle(runInfo.get(RUN_ID).toString(), cycle);
                break;
            case "9" :
            	int cycleNo = Integer.parseInt(args[1]);
            	htpSequencerPanel.cycleComplete(runInfo.get(RUN_ID).toString(), cycleNo);
            	break;
            case "10" :
                htpSequencerPanel.transferComplete(runInfo.get(RUN_ID).toString());
                break;
            case "11" :
            	htpSequencerPanel.goodBye();
            	break;
            case "12" :
                htpSequencerPanel.notifications(notifications.get("severity").toString(), notifications.get("description").toString(), notifications.get("code").toString());
                break;
            default :
                htpSequencerPanel.getOrderDetailsByComplexId();
                break;
        }
        
    }
    
    public void hello() {
        logger.info("HTP Sequencer started ..\n\n");
        try {
            logger.info("Requesting Hello ...");
            Object obj = readJsonObject("instrumentInformationPath");
            JSONObject instrumentInfo = (JSONObject) obj;
            
            response = webService.getResponse(adapterURL + "/hello", MediaType.APPLICATION_JSON,
                UrlConstants.POST.getText(), instrumentInfo);
            this.displayResponseDetails(response);
            if (response.getStatus() != 200) {
                logger.info("Something went wrong while calling hello");
                System.exit(0);
            } else {
                logger.info("Requesting Hello completed Successfully....");
            }
            
        } catch (Exception e1) {
            logger.error(e1.getMessage());
        }
    }
    
    public void ping() {
        
        try {
            logger.info("Pinging to CONNECT ..");
            
        
            response = webService.getResponse(adapterURL + "/ping",
                MediaType.APPLICATION_JSON, UrlConstants.POST.getText(), null);
            this.displayResponseDetails(response);
            if (response.getStatus() != 200) {
                logger.info("Something went wrong while calling ping");
            } else {
                logger.info("Ping completed successfully");
            }
        } catch (Exception e) {
            logger.error("Something went wrong while calling ping" + e.getMessage());
        }
        
    }
    
    public void goodBye() {
        
        try {
            logger.info("Saying GoodBye to CONNECT ..");
            
            response = webService.getResponse(adapterURL + "/goodbye/" ,MediaType.APPLICATION_JSON, UrlConstants.POST.getText(), null);
            this.displayResponseDetails(response);
            if (response.getStatus() == 200) {
            	logger.info(response.getStatus()+ ": Saying GoodBye completed successfully");
            	System.exit(0);
            } else {
                logger.info("Something went wrong while calling GoodBye");
                System.exit(0);
            }
        } catch (Exception e) {
            logger.error("Something went wrong while calling GoodBye" + e.getMessage());
        }
        
    }
    
    @SuppressWarnings("unchecked") public void getOrderDetailsByComplexId() {
        JSONObject json = null;
        logger.info("Getting Order Details By Using Complex_Id ...");
        try {
            response =
                webService.getResponse(adapterURL + "/orders/" + complexId, MediaType.APPLICATION_JSON, "get", null);
            
            if (response.getStatus() == 500) {
                logger.error(complexId + " is not valid, Enter correct ComplexId ");
                System.exit(0);
            } else if (response.getStatus() == 404) {
                logger.info(response.getStatus() + ":: No ORDER records found for the given complexId....");
                System.exit(0);
            } else {
                String orderDetails = this.displayResponseDetails(response);
                if (!orderDetails.equals("")) {
                    json = (JSONObject) parser.parse(orderDetails);
                    String status = (String) json.get("status");
                    referenceId = String.valueOf(json.get(REFERENCE_ID));
                    runInfo.put("run_protocol", json.get("run_protocol"));
                    runInfo.put(REFERENCE_ID, json.get(REFERENCE_ID));
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new File("resources//run.json"),
                        runInfo);
                    if (!status.equalsIgnoreCase("ready")) {
                        logger.info(complexId + " is already used in HTP can not proceed further operations.");
                        System.exit(0);
                    } else {
                        logger.info("Getting Order Details By Using" + complexId + "Completed Successfully ");
                        Thread.sleep(statusTimeDelay * 1000);
                        this.runs(runInfo);
                    }
                }
            }
            
        } catch (Exception e) {
            logger.info("Something went wrong while getting orders by using Complex_Id" + e.getMessage());
        }
        
    }
    
    public void runs(JSONObject runInfo) {
        try {
            logger.info("Sending Run object to CONNECT ...");
            response = webService.getResponse(adapterURL + "/runs", MediaType.APPLICATION_JSON,
                UrlConstants.POST.getText(), runInfo);
            this.displayResponseDetails(response);
            if (response.getStatus() != 201) {
                runResultsMap = response.getProperties();
            } else {
                logger.info("POST Run information from Simulator completed Successfully");
                Thread.sleep(statusTimeDelay * 1000);
                this.getRunDetailsByRunId(runInfo.get(RUN_ID).toString());
            }
        } catch (Exception e) {
            logger.error("Something went wrong while calling runs " + e.getMessage());
        }
    }
    
    public void runUpdateByRunId(String runId) {
        try {
            logger.info("Sending Updated Run object to CONNECT ...");
            
            response = webService.getResponse(adapterURL + RUNS + runId, MediaType.APPLICATION_JSON,
                UrlConstants.PUT.getText(), updateRunInfo);
            this.displayResponseDetails(response);
            if (response.getStatus() == 404) {
                logger.info("The given RunId is not available in Connect :" + runId);
            } else if (response.getStatus() != 202) {
                logger.info("Something went wrong while calling runs");
            } else {
                logger.info("Sending Updated Run object to CONNECT Completed Successfully");
                if (!flag) {
                    Thread.sleep(statusTimeDelay * 1000);
                    this.updateRunByInProcess(runId);
                }
            }
        } catch (InterruptedException e) {
            logger.error(e);
            Thread.currentThread().interrupt();
        }
    }
    
    public void getRunDetailsByRunId(String runId) {
        logger.info("Getting Run Details By Using the Run_Id " + runId);
        response = webService.getResponse(adapterURL + RUNS + runId, MediaType.APPLICATION_JSON,
            UrlConstants.GET.getText(), null);
        this.displayResponseDetails(response);
        if (response.getStatus() == 404) {
            logger.info("The given RunId is not available in Connect :" + runId);
        } else if (response.getStatus() != 200) {
            runResultsMap = response.getProperties();
        } else {
            try {
                logger.info("Getting Run Details By Using the Run_Id " + runId + " is Completed Successfully");
                Thread.sleep(statusTimeDelay * 1000);
                this.updateRunByStarted(runId);
            } catch (InterruptedException e) {
                logger.error(e);
                Thread.currentThread().interrupt();
            }
        }
        
    }
    
    public void updateRunByStarted(String runId) {
        
        try {
            logger.info("Updating Run Started status ...");
            Object obj;
            obj = readJsonObject("updateStartedStatusPath");
            JSONObject run = (JSONObject) obj;
            runStatus = run.get(RUN_STATUS).toString();
            response = webService.getResponse(adapterURL + RUNS + runId + PATH_DELIMITER + RUN_STATUS,
                MediaType.APPLICATION_JSON, UrlConstants.PUT.getText(), run);
            this.displayResponseDetails(response);
            if (response.getStatus() != 202) {
                logger.info("Something went wrong while calling Run started");
            } else {
                logger.info("Updating Run Started Status Completed Successfully");
                Thread.sleep(statusTimeDelay * 1000);
                this.updateRunByInProcess(runId);
            }
        } catch (Exception e) {
            logger.info("Something went wrong while calling start%s" + e.getMessage());
        }
    }
    
    public void updateRunByInProcess(String runId) {
        
        try {
            logger.info("Updating Run In-Process status ...");
            Object obj;
            obj = readJsonObject("updateInprocessStatusPath");
            JSONObject run = (JSONObject) obj;
            runStatus = run.get(RUN_STATUS).toString();
            response = webService.getResponse(adapterURL + RUNS + runId + PATH_DELIMITER + RUN_STATUS,
                MediaType.APPLICATION_JSON, UrlConstants.PUT.getText(), run);
            this.displayResponseDetails(response);
            if (response.getStatus() != 202) {
                logger.info("Something went wrong while calling Run In-Process");
            } else {
                logger.info("Updating Run In-Process Status Completed Successfully");
                Thread.sleep(statusTimeDelay * 1000);
                this.iterateCycle(runId);
            }
        } catch (Exception e) {
            logger.info("Something went wrong while calling In-Process " + e.getMessage());
        }
    }
    
    public void updateFinalRun(String runId) {
        
        try {
            logger.info("Updating Run Complete status ...");
            Object obj;
            obj = readJsonObject("updateFinalStatusPath");
            JSONObject run = (JSONObject) obj;
            runStatus = run.get(RUN_STATUS).toString();
            response = webService.getResponse(adapterURL + RUNS + runId + PATH_DELIMITER + RUN_STATUS,
                MediaType.APPLICATION_JSON, UrlConstants.PUT.getText(), run);
            this.displayResponseDetails(response);
            if (response.getStatus() != 202) {
                logger.info("Something went wrong while calling Run Complete");
            } else {
                logger.info("Updating Run " + runStatus + " Status Completed Successfully");
                if (runStatus.equalsIgnoreCase("Failed")){
                    String description = "Sequencing run has failed. Check the instrument and try again. If the error persists, create a problem report and contact your Roche service representative.";
                    this.notifications(ERROR,description,"1.2.0.3");
                    System.exit(0);
                }
                else               
                	Thread.sleep(statusTimeDelay * 1000);
                    this.transferComplete(runId);

            }
        } catch (Exception e) {
            logger.info("Something went wrong while calling " + runStatus + e.getMessage());
        }
        
    }
    
    public void iterateCycle(String runId) {
        
        try {
            
            for (int cycleNo = 0; cycleNo < noOfCycles; cycleNo++) {
                Boolean isLastCycle = false;
                
                if (cycleNo == noOfCycles - 1) {
                    isLastCycle = true;
                }
                
                Boolean isNotSpaceAvailable = true;
                
                while (isNotSpaceAvailable) {
                    Long serverSpace = this.getDirectoryFreeSpace();
                    Long expectedFreeSpace = Long.parseLong(readProperties("expectedFreeSpace"));
                    if (serverSpace <= expectedFreeSpace) {
                        String description = "Insufficient free space detected. Ensure there is enough space in the destination folder.";
                        this.notifications(WARNING,description, "1.2.0.2");
                    } else {
                        isNotSpaceAvailable = false;
                        this.updateCycle(runId, cycleNo, isLastCycle);
                    }
                }
            }
        } catch (Exception e) {
            logger.info("Something went wrong while calling iterate cycle " + e.getMessage());
        }
        
    }
    
    public void iterateCycle(String runId, int cycle) {
        
        try {
            
            for (int cycleNo = cycle; cycleNo < noOfCycles; cycleNo++) {
                Boolean isLastCycle = false;
                
                if (cycleNo == noOfCycles - 1) {
                    isLastCycle = true;
                }
                
                Boolean isNotSpaceAvailable = true;
                
                while (isNotSpaceAvailable) {
                    Long serverSpace = this.getDirectoryFreeSpace();
                    Long expectedFreeSpace = Long.parseLong(readProperties("expectedFreeSpace"));
                    if (serverSpace <= expectedFreeSpace) {
                        String description = "Insufficient free space detected. Ensure there is enough space in the destination folder.";
                        this.notifications(WARNING,description,"1.2.0.2");
                    } else {
                        isNotSpaceAvailable = false;
                        this.updateCycle(runId, cycleNo, isLastCycle);
                    }
                }
            }
        } catch (Exception e) {
            logger.info("Something went wrong while calling iterate cycle%s" + e.getMessage());
        }
        
    }
    
    public void updateCycle(String runId, Integer cycleNo, Boolean isLastCycle) throws InterruptedException {
        for (String type: fileTypeList) {
            logger.info("Updating Cycle status ...");
            String chipId = runInfo.get("consumable_device_part_number").toString();
            String laneNo = runInfo.get("lane_number").toString();
            String fileChecksum = null;
            DecimalFormat decimalFormater = new DecimalFormat("00");
            String localFilePath = null;
            String folderPath = RUNS + cycleDateFormater.format(new Date()) + "_" + chipId + "_L0" + laneNo + CYCLE
                + decimalFormater.format(cycleNo);
            String filePath = fileFormater.format(new Date()) + "_" + chipId + "_L0" + laneNo + "_cycle"
                + decimalFormater.format(cycleNo) + "_" + type;
            localFilePath = "ips" + folderPath + PATH_DELIMITER + filePath;
            String path = tmpFolderPath + deviceId + folderPath + PATH_DELIMITER + filePath;
            if (!"true".equalsIgnoreCase(readProperties("useRemoteFS"))) {
                fileChecksum = this.getFileChecksumWindows(path);
            } else {
                fileChecksum = getFileChecksumIPS(folderPath, filePath);
            }
            if(fileChecksum == null){
                logger.error("CheckSum value should not be null ");
                System.exit(0);
            }    
            JSONObject run = new JSONObject();
            run.put("path", localFilePath);
            run.put("checksum", fileChecksum);
            run.put("cycle", cycleNo);
            run.put("type", type);
            
            response =
                webService.getResponse(adapterURL + RUNS + runId + CYCLE, MediaType.APPLICATION_JSON, "put", run);
            this.displayResponseDetails(response);
            
            if (response.getStatus() == 202) {
                logger.info("Update Cycle: " + cycleNo + "\t Type: " + type + " completed ...");
            } else if (response.getStatus() == 409) {
                logger.info("File is corrupted, Checksum is not matching  ...");
            } else {
                logger.info("Something went wrong while updating cycle...");
            }
            
        }
        
        this.cycleComplete(runId, cycleNo);
        if (isLastCycle) {
            Thread.sleep(statusTimeDelay * 1000);
            this.updateFinalRun(runId);
        }
    }
    
    public String getFileChecksumWindows(String path) {
    	
        String fileChecksum = null;
        String inputFilePath = readProperties("cycleInput");
        File file = new File(path);
        file.getParentFile().mkdirs();
        logger.info("Path :" + path);
        try (FileWriter outputFile = new FileWriter(file);) {
            Files.lines(Paths.get(inputFilePath)).forEach(s -> {
                try {
                    outputFile.write(s);
                } catch (IOException e) {
                    logger.error("Error while writting the file");
                }
            });
            
            outputFile.close();
            
            // getting the checksum
            fileChecksum = this.getFileChecksum(file);
        } catch (Exception e) {
            logger.info(String.format("Error while updating cycle info " + e.getMessage()));
        }
        
        return fileChecksum;
    }
    
    public String getFileChecksumIPS(String folderPath, String filePath) {
        String fileChecksum = null;
        String remoteIp = readProperties("mountIp");
        int remotePort = Integer.parseInt(readProperties("mountPort"));
        String username = readProperties("mountUsername");
        String ecryptedPassword = readProperties("mountPasswordEnc");
        String decryptionKey = readProperties("encriptionPswd");
        String password = null;
        String tempServerPath = deviceId + folderPath + PATH_DELIMITER;
        try {
            Key shaKey = new SecretKeySpec(decryptionKey.getBytes("UTF8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, shaKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(ecryptedPassword));
            password = new String(decrypted);
            fileChecksum =
                createCycleFileAndGetChecksum(remoteIp, username, password, remotePort, tempServerPath, filePath);
        } catch (Exception e) {
            logger.error("Error while decrypt" + e.getMessage());
        }
        return fileChecksum;
    }
    
    /**
     * @param run_id
     * @param cycle
     */
    public void cycleComplete(String runId, int cycleNo) {
        logger.info("Updating cycle Complete .... ");
        JSONObject cycleJson = new JSONObject();
        cycleJson.put("cycle", cycleNo);
        response = webService.getResponse(adapterURL + RUNS + runId + "/cyclecomplete", MediaType.APPLICATION_JSON,
            UrlConstants.PUT.getText(), cycleJson);
        this.displayResponseDetails(response);
        if (response.getStatus() != 200) {
            logger.error("Something went wrong while calling cycleComplete");
        } else {
            logger.info("Cycle Complete done successfully");
        }
    }
    
    public Long getDirectoryFreeSpace() {
        
        JSONObject resultObject = new JSONObject();
        Long freeSpace = 0L;
        response = webService.getResponse(adapterURL + "/freespace", MediaType.APPLICATION_JSON, "get", null);
        String res = this.displayResponseDetails(response);
        if (response.getStatus() != 200) {
            logger.info("Something went wrong while checking freespace");
        } else {
            JSONParser parserobj = new JSONParser();
            try {
                resultObject = (JSONObject) parserobj.parse(res);
            } catch (ParseException e) {
                logger.info(String.format("Error while checking free space...", e));
            }
            freeSpace = Long.parseLong(resultObject.get("freespace").toString());
            logger.info("HTP Sequencer process completed");
        }
        return freeSpace;
    }
    
    /**
     * @param run_id
     */
    public void transferComplete(String runId) {
        try {
            logger.info("Updating transfer Complete .... ");
            response = webService.getResponse(adapterURL + RUNS + runId + "/transferscomplete",
                MediaType.APPLICATION_JSON, UrlConstants.PUT.getText(), null);
            this.displayResponseDetails(response);
            if (response.getStatus() != 200) {
                logger.error("Transfer Completed Encountered Error.");
                String description = "Data transfer interrupted due to connectivity issues. Check the connection and resume the run. If the error persists, create a problem report and contact your Roche service representative.";
                this.notifications(ERROR,description,"1.2.0.1");
            } else {
                logger.info("Transfer Completed done successfully");
                goodBye();
            }
        } catch (Exception e) {
            logger.error("Something went wrong while calling transfer Complete" + e.getMessage());
        }
    }
    
    /**
     * @param url
     * @param type
     * @param methodType
     * @param input
     * @return
     */
    
    public String readProperties(String propKey) {
        Properties prop = new Properties();
        try {
            FileReader fr = new FileReader("resources//simulator.properties");
            prop.load(fr);
        } catch (IOException e) {
            logger.error(String.format("Exception while getting file path...", e));
        }
        return prop.getProperty(propKey);
    }
    
    public Object readJsonObject(String string) {
        Object o = null;
        try {
            o = parser.parse(new FileReader(readProperties(string)));
        } catch (IOException | ParseException e) {
            logger.error(e);
        }
        return o;
    }
    
    public String displayResponseDetails(ClientResponse response) {
        try {
            String output = response.getEntity(String.class);
            int status = response.getStatus();
            logger.info("Output from Server .... ");
            logger.info("Response code: " + status);
            logger.info("Response body: " + output);
            return output;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
    
    public String getRunId() {
        long timeSeed = System.nanoTime();
        double randSeed = Math.random() * 1000;
        long midSeed = (long) (timeSeed * randSeed);
        String s = midSeed + "";
        return s.substring(0, 9);
    }
    
//    public String getChecksum(File input) {
//        InputStream in = null;
//        MessageDigest digest = null;
//        byte[] block = new byte[1024];
//        int length;
//        byte[] output = new byte[1024];
//        try {
//            in = new FileInputStream(input);
//            digest = MessageDigest.getInstance("SHA1");
//            while ((length = in.read(block)) > 0) {
//                if (digest != null) {
//                    digest.update(block, 0, length);
//                    output = digest.digest();
//                } else {
//                    throw new NullPointerException("Message digest cannnot be null");
//                }
//            }
//        } catch (NoSuchAlgorithmException | IOException e) {
//            logger.error("Error in checkSum method :" + e.getMessage());
//        } finally {
//            try {
//                if (in != null)
//                    in.close();
//            } catch (IOException e) {
//                logger.error(String.format("IO exception while closing resource...", e));
//            }
//        }
//        return javax.xml.bind.DatatypeConverter.printHexBinary(output);
//        
//    }
    
    public void notifications(String severity,String description, String code) {
        try {
            logger.info("Sending Notification to CONNECT ...");
            notifications = (JSONObject) readJsonObject("notificationPath");
            notifications.put("severity", severity);
            notifications.put("code", code);
            notifications.put("datetime", Timestamp.from(Instant.now()).toString());
            if (severity.equalsIgnoreCase(ERROR)) {
                notifications.put(DESCRIPTION, description);
                notifications.put(ENGLISH_DESCRIPTION, description);
            }
            if (severity.equalsIgnoreCase(WARNING)) {
                notifications.put(DESCRIPTION, description);
                notifications.put(ENGLISH_DESCRIPTION, description);
            }
            if (severity.equalsIgnoreCase(INFORMATIONAL)) {
                notifications.put(DESCRIPTION, description);
                notifications.put(ENGLISH_DESCRIPTION, description);
            }
            webService.getResponse(adapterURL + "/notification", MediaType.APPLICATION_JSON,
                UrlConstants.POST.getText(), notifications);
            this.displayResponseDetails(response);
            
        } catch (Exception e) {
            logger.error("Something went wrong while calling runs " + e.getMessage());
        }
    }
    
    public String getChecksum(InputStream input) {
        InputStream in = null;
        MessageDigest digest = null;
        byte[] block = new byte[1024];
        int length;
        byte[] output = new byte[1024];
        try {
            in = input;
            digest = MessageDigest.getInstance("SHA1");
            while ((length = in.read(block)) > 0) {
                if (digest != null) {
                    digest.update(block, 0, length);
                    output = digest.digest();
                } else {
                    throw new NullPointerException("Message digest cannnot be null");
                }
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            logger.error("Error in checkSum method :" + e.getMessage());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                logger.error(String.format("IO exception while closing resource... ", e));
            }
        }
        return javax.xml.bind.DatatypeConverter.printHexBinary(output);
        
    }
    
    public String createCycleFileAndGetChecksum(String ip, String username, String password, int port,
        String tempServerPath, String fileName) {
        StringBuilder temp = new StringBuilder("");
        String checksum = null;
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        InputStream isStream = null;
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(username, ip, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            logger.info("Server Configuration done");
            session.connect();
            logger.info("successfully connected with server session");
            channel = session.openChannel("sftp");
            channel.connect();
            logger.info("successfully connected to server");
            channelSftp = (ChannelSftp) channel;
            String folderPath[] = tempServerPath.split("/");
            
            channelSftp.cd(tmpFolderPath);
            for (String path: folderPath) {
                try {
                    channelSftp.cd(path);
                } catch (SftpException e) {
                    channelSftp.mkdir(path);
                    channelSftp.cd(path);
                }
            }
            Files.lines(Paths.get(readProperties("cycleInput"))).forEach(s -> temp.append(s));
            
            isStream = new ByteArrayInputStream(temp.toString().getBytes());
            logger.error("File path from createCycleFileAndGetChecksum() :"+tmpFolderPath + tempServerPath + "/" + fileName);
            channelSftp.put(isStream, tmpFolderPath + tempServerPath + "/" + fileName);
            checksum = getFileChecksum(channelSftp.get(tmpFolderPath + tempServerPath + "/" + fileName));
        } catch (Exception e) {
            logger.error("Error while creating file in mount :" + e.getMessage());
        } finally {
            if ((channel != null) && (session == null))
                channel.disconnect();
            session.disconnect();
        }
        return checksum;
        
    }
    
	private String getFileChecksum(File file) throws IOException, NoSuchAlgorithmException {

		FileInputStream fis = new FileInputStream(file);
		return getFileChecksum(fis);
	}
    
	private String getFileChecksum(InputStream fis) throws IOException, NoSuchAlgorithmException {

		logger.info("Generating checksum...");
		
		MessageDigest digest = MessageDigest.getInstance(hashingAlgorithm);

		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}

		fis.close();

		byte[] bytes = digest.digest();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}
}