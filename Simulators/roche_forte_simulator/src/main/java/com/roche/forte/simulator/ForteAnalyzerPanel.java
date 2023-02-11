package com.roche.forte.simulator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.api.client.ClientResponse;

public class ForteAnalyzerPanel {

	public static final Logger logger = Logger.getLogger(ForteAnalyzerPanel.class);
	WebServicesClientResponse webService = new WebServicesClientResponse();
	ClientResponse response = null;
	private static final String PATH_DELIMITER = "/";
	private static final String JOBQUEUE = "jobqueue";
	private SimpleDateFormat dateFormater = new SimpleDateFormat("yyMMdd");
	private static final String SECONDARY = "secondary";
	private static final String TERTIARY = "tertiary";
	private static final String TEXT = "This file contains FORTE Secondary Anaysis report.";

	JSONParser parser = new JSONParser();
	String adapterURL = readProperties("hostName") + readProperties("hostUrl");
	String fileChecksum;
	String jobDetails = null;
	String deviceId = null;

	public static void main(String[] args) throws InterruptedException {
		ForteAnalyzerPanel forteAnalyzerPanel = new ForteAnalyzerPanel();
		DOMConfigurator.configure("resources//log4j.xml");

		Timer timer = new Timer("Ping Timer"); // Instantiate Timer Object
		ForteTask st = new ForteTask(); // Instantiate TaskScheduler for Ping
		st.setTaskType(UrlConstants.PING.getText());
		logger.info(st.getTaskType());
		Long delay = Long.valueOf(forteAnalyzerPanel.readProperties("pingDelay"));
		timer.schedule(st, 0, delay);

		forteAnalyzerPanel.hello();

		// Get Jobqueue
		int noOfJobWorkers = Integer.parseInt(forteAnalyzerPanel.readProperties("noOfJobWorkers"));
		
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(noOfJobWorkers);
		ForteTask task = new ForteTask();
		task.setTaskType(UrlConstants.GETJOB.getText());
		executorService.scheduleWithFixedDelay(task, 0,
				Long.valueOf(forteAnalyzerPanel.readProperties("jobPollIntervals")), TimeUnit.MILLISECONDS);
	}

	public void ping() {

		try {
			logger.info("Pinging to CONNECT from FORTE ..");
			response = webService.getResponse(adapterURL + "/ping", MediaType.APPLICATION_JSON,
					UrlConstants.POST.getText(), null);
			this.displayResponseDetails(response);
			if (response.getStatus() == 200) {
				logger.info("Ping completed successfully. FORTE is alive.....");
			} else {
				logger.info("Something went wrong while calling ping");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public void hello() {
		logger.info("FORTE Analyzer started ..\n\n");
		try {
			logger.info("Requesting HELLO from FORTE...");
			Object obj = readJsonObject("instrumentInformationPath");
			JSONObject instrumentInfo = (JSONObject) obj;
			deviceId = (String) instrumentInfo.get("instance_id");
			response = webService.getResponse(adapterURL + "/hello", MediaType.APPLICATION_JSON,
					UrlConstants.POST.getText(), instrumentInfo);
			this.displayResponseDetails(response);
			if (response.getStatus() == 200) {
				logger.info("Requesting HELLO completed Successfully....");
			} else {
				logger.info("Something went wrong while calling HELLO");
				System.exit(0);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void getJobQueue() {
		try {

			logger.info("Get JOBQUEUE details......");
			logger.info("Current Thread: " + Thread.currentThread().getName());
			response = webService.getResponse(adapterURL + PATH_DELIMITER + JOBQUEUE, MediaType.APPLICATION_JSON,
					UrlConstants.GET.getText(), null);
			jobDetails = this.displayResponseDetails(response);
			if (response.getStatus() == 200) {
				logger.info("Querying job details done Successfully :)");
				this.checkAnalysisType(jobDetails);
			} else if (response.getStatus() == 204) {
				logger.info("No jobs available for processing");
			} else {
				logger.info("Something went wrong while getting job details :(");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void checkAnalysisType(String jobDetails) {

		logger.info("jobDetails" + jobDetails);
		JSONObject json = null;
		Boolean isValidChecksum = false;
		try {
			json = (JSONObject) parser.parse(jobDetails);
			String id = (String) json.get("id");
			String filePath = null;
			String checksum = null;
			JSONArray filePathArray = null;
			JSONArray checkSumArray = null;
			String kind = (String) json.get("kind");
			if (kind.equalsIgnoreCase(SECONDARY)) {
				filePath = (String) json.get("secondary_infile");
				checksum = (String) json.get("secondary_checksum");
				logger.info("Secondary Path: " + filePath);
				logger.info("Secondary Checksum: " + checksum);
				isValidChecksum = this.checkSum(filePath, checksum);
				if (!isValidChecksum)
					this.updateJobErrorQueue(id);
				else
					this.updateJobStartQueue(id, kind);
			} else if (kind.equalsIgnoreCase(TERTIARY)) {
				filePathArray = (JSONArray) json.get("tertiary_infiles");
				checkSumArray = (JSONArray) json.get("tertiary_checksums");
				logger.info("Tertiary checksums: " + checkSumArray);
				logger.info("Tertiary filePaths: " + filePathArray);

				Boolean isChecksumValid = true;

				for (int i = 0; i < filePathArray.size(); i++) {
					isChecksumValid = isChecksumValid
							&& this.checkSum(filePathArray.get(i).toString(), checkSumArray.get(i).toString());
				}

				if (!isChecksumValid)
					this.updateJobErrorQueue(id);
				else
					this.updateJobStartQueue(id, kind);
			} else if (kind.equalsIgnoreCase("problem")) {
				logger.info("Problem occured at CONNECT end");
			}

		} catch (ParseException | IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void updateJobStartQueue(String id, String kind) {
		try {
			logger.info("Updating Job Start Details ........");
			Object obj = readJsonObject("jobStartStatusPath");
			JSONObject jobStatusInfo = (JSONObject) obj;
			response = webService.getResponse(adapterURL + PATH_DELIMITER + JOBQUEUE + PATH_DELIMITER + id,
					MediaType.APPLICATION_JSON, UrlConstants.PUT.getText(), jobStatusInfo);
			this.displayResponseDetails(response);
			if (response.getStatus() == 200) {
				logger.info("updating job Start status Successfully :)");
				this.updateJobInprogressQueue(id, kind);
			} else {
				logger.info("Something went wrong while updating Start job details :(");
			}
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
	}

	public void updateJobInprogressQueue(String id, String kind) {
		try {
			logger.info("Updating Job Inprogress Details ........");
			Object obj = readJsonObject("jobInprogressStatusPath");
			JSONObject jobStatusInfo = (JSONObject) obj;
			response = webService.getResponse(adapterURL + PATH_DELIMITER + JOBQUEUE + PATH_DELIMITER + id,
					MediaType.APPLICATION_JSON, UrlConstants.PUT.getText(), jobStatusInfo);
			this.displayResponseDetails(response);
			if (response.getStatus() == 200) {
				logger.info("updating job Inprogess status Successfully :)");
				this.updateJobDoneQueue(id, kind);
			} else {
				logger.info("Something went wrong while updating Inprogress job details :(");
			}
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
	}

	@SuppressWarnings("unchecked") public void updateJobDoneQueue(String id, String kind) {
		try {
			logger.info("Updating Job Done Details ........");
			Object obj = readJsonObject("jobDoneStatusPath");
			JSONObject jobStatusInfo = (JSONObject) obj;
			String primaryPath = readProperties("folderPath");
			String connectPath = readProperties("connectPath");

			String path = null;

			if (kind.equalsIgnoreCase(SECONDARY)) {
				path = readProperties("deviceSerialNumber") + PATH_DELIMITER + "runs" + PATH_DELIMITER + dateFormater.format(new Date()) + "_"
						+ id + PATH_DELIMITER + kind + PATH_DELIMITER + kind + "_" + dateFormater.format(new Date())
						+ ".fastq";

			} else if (kind.equalsIgnoreCase(TERTIARY)) {

				path =  readProperties("deviceSerialNumber") + PATH_DELIMITER + "runs" + PATH_DELIMITER + dateFormater.format(new Date()) + "_"
						+ id + PATH_DELIMITER + kind + PATH_DELIMITER + kind + "_" + dateFormater.format(new Date())
						+ ".xml";
				String tertiaryPath = primaryPath + path;
				logger.info("Tertiary Path: " + tertiaryPath);
			}

			if (path != null) {

				String actualPath = primaryPath + path;
				String adapterPath = connectPath + path;

				logger.info(kind + " Path: " + actualPath);
				this.generateFileByKind(actualPath, kind);
				fileChecksum = this.checkSum(actualPath);
				jobStatusInfo.put("outfile", adapterPath);
				jobStatusInfo.put("outfile_checksum", fileChecksum);

				response = webService.getResponse(adapterURL + PATH_DELIMITER + JOBQUEUE + PATH_DELIMITER + id,
						MediaType.APPLICATION_JSON, UrlConstants.PUT.getText(), jobStatusInfo);
				this.displayResponseDetails(response);
				if (response.getStatus() == 200) {
					logger.info("updating job Done status Successfully :)");
				} else {
					logger.info("Something went wrong while updating Done job details :(");
				}
			} else {
				logger.info("Path is empty");
				System.exit(0);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public String checkSum(String path) throws IOException {
		logger.info("Entered in checkSum method ");
		InputStream in = null;
		byte[] output = null;
		MessageDigest digest = null;
		byte[] block = new byte[1024];
		int length;
		try {
			in = new FileInputStream(new File(path));
			digest = MessageDigest.getInstance("SHA1");
			while ((length = in.read(block)) > 0) {
				if (digest != null) {
					digest.update(block, 0, length);
					output = digest.digest();
					logger.info("Finished checkSum method ");
				} else {
					throw new NullPointerException("Message digest cannnot be null");
				}
			}
		} catch (FileNotFoundException | NoSuchAlgorithmException e) {
			logger.error("Error in checkSum method " + e.getMessage());
			return null;
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return javax.xml.bind.DatatypeConverter.printHexBinary(output);
	}

	@SuppressWarnings("unchecked") public void updateJobErrorQueue(String id) {
		try {
			logger.info("Updating Job Error Details ........");
			Object obj = readJsonObject("jobErrorStatusPath");
			JSONObject jobStatusInfo = (JSONObject) obj;
			jobStatusInfo.put("error_key", "CheckSum MisMatch");
			response = webService.getResponse(adapterURL + PATH_DELIMITER + JOBQUEUE + PATH_DELIMITER + id,
					MediaType.APPLICATION_JSON, UrlConstants.PUT.getText(), jobStatusInfo);
			this.displayResponseDetails(response);
			if (response.getStatus() == 200) {
				logger.info("updating job Error status Successfully :)");
			} else {
				logger.info("Something went wrong while updating job details :(");
			}
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
	}

	public Boolean checkSum(String path, String checkSum) throws IOException {
		InputStream in = null;
		byte[] output = null;
		MessageDigest digest = null;
		byte[] block = new byte[1024];
		int length;
		try {
			in = new FileInputStream(new File(path));
			digest = MessageDigest.getInstance("SHA1");
			while ((length = in.read(block)) > 0) {
				if (digest != null) {
					digest.update(block, 0, length);
					output = digest.digest();
					logger.info("Finished checkSum method ");
				} else {
					throw new NullPointerException("Message digest cannnot be null");
				}
			}
		} catch (FileNotFoundException | NoSuchAlgorithmException e) {
			logger.error("Error in checkSum method " + e.getMessage());
			return false;
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return javax.xml.bind.DatatypeConverter.printHexBinary(output).equalsIgnoreCase(checkSum);
	}

	public void generateFileByKind(String path, String kind) {

		logger.info("Path: " + path);

		if (kind.equals(SECONDARY)) {
			File file = new File(path);
			file.getParentFile().mkdirs();
			logger.info("Secondary Process:");
			Writer writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(TEXT);
			} catch (IOException e) {
				logger.info("Exception in generateFileByKind: " + e.getMessage());
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						logger.info("Exception in finally generateFileByKind: " + e.getMessage());
					}
				}
			}
		} else if (kind.equals(TERTIARY)) {
			logger.info("Tertiary Process:");

			File file = new File(path);
			file.getParentFile().mkdirs();
			logger.info("TERTIARY Process:");
			Writer writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(TEXT);
			} catch (IOException e) {
				logger.info("Error: " + e.getMessage());
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						logger.info("Exception in finally generateFileByKind: " + e.getMessage());
					}
				}
			}
		}

	}

	public void fileStorageDirectory(String path, String kind) {
		File file = new File(path);
		file.getParentFile().mkdirs();
		String s;
		FileReader fr = null;
		BufferedReader br = null;
		FileWriter tertiaryOutputFile = null;
		try (FileWriter outputFile = new FileWriter(file)) {
			logger.info(path);
			if (kind.equals(SECONDARY)) {
				outputFile.write(TEXT);
			} else if (kind.equals(TERTIARY)) {
				logger.info("Tertiary directory path: " + path);
				fr = new FileReader("resources//forteTertiaryOutput.xml");
				br = new BufferedReader(fr);
				tertiaryOutputFile = new FileWriter(file, true);
				while ((s = br.readLine()) != null) {
					tertiaryOutputFile.write(s);
					tertiaryOutputFile.flush();
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
				if (br != null) {
					br.close();
				}
				if (tertiaryOutputFile != null) {
					tertiaryOutputFile.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	public String displayResponseDetails(ClientResponse response) {
		int status = response.getStatus();
		logger.info("Output from Server .... ");
		logger.info("Response code: " + status);
		String output = response.getEntity(String.class);
		logger.info("Response body: " + output);
		return output;
	}

	public String readProperties(String propKey) {
		Properties prop = new Properties();
		String str = null;
		try {
			FileReader fr = new FileReader("resources//simulator.properties");
			prop.load(fr);
			str = prop.getProperty(propKey);
		} catch (IOException e) {
			logger.error(String.format("Exception while getting file path...", e));
		}
		return str;
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
}
