package com.roche.connect.ttv2analyzer.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.CharEncoding;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.ttv2analyzer.utils.Constants;
import org.json.simple.parser.ParseException;
import java.net.URL;

@Service
public class TTV2AnalyzerServiceImpl {

	@Value("${path.sourcePath}")
	private String sourcePath;

	//@Value("${path.destinationPath}")
	private String destinationPath;

	@Value("${path.rsync}")
	private String rsync;

	@Value("${path.rsyncWithChecksum}")
	private String rsyncWithChecksum;

	@Value("${path.rsyncWithRTA}")
	private String rsyncWithRTA;

	@Value("${path.rsyncDownload}")
	private String rsyncDownload;

	@Value("${analyzer.status_url}")
	private String statusURL;
	
	 @Value("${analyzer.create_job_url}") 
	 private String createJobURL;
	 
	 @Value("${path.create_job_filepath}")
	 private String createJobFilePath;
	 
	 private String jobId;
	    
	 JSONParser parser = new JSONParser();

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	Timer timer;

	Timer timer2;
	
	 public void getAnalyzerUploadDestinationPath(JSONObject jobCreationInfo) throws UnsupportedEncodingException {
	        logger.info("Posting Job details to Analyzer: " + createJobURL);
	        Invocation.Builder createJobBuilder =
	            RestClientUtil.postOrPutBuilder(URLEncoder.encode(createJobURL, CharEncoding.UTF_8), null);
	        Response response = createJobBuilder.post(Entity.entity(jobCreationInfo, MediaType.APPLICATION_JSON));
	        Map<String, String> responseOutput = response.readEntity(Map.class);
	        jobId = responseOutput.get("jobId");
	        destinationPath = responseOutput.get("ingestURI");
	        
	    }
	 
	 public Object readJsonObject(String string) {
	        Object o = null;
	        try {
	            o = parser.parse(new FileReader(new File(string)));
	        } catch (IOException | ParseException e) {
	            System.out.println(e.getMessage());
	        }
	        return o;
	    }

	public void executeTimer(String runId) throws UnsupportedEncodingException {
		logger.info("Entering executeTimer: " + runId);
    	 URL fileUrl = getClass().getResource(createJobFilePath);
    	logger.info("file path is : "+fileUrl.getPath());
		getAnalyzerUploadDestinationPath((JSONObject) readJsonObject(fileUrl.getPath()));
		String src = sourcePath + "/" + runId + "/" + Constants.UPLOAD + "/";
		String desc = destinationPath + "/" + runId + "/" + Constants.UPLOAD + "/";

		ExcuteTimer excuteTimer = new ExcuteTimer(runId, src, desc);
		timer = new Timer(true);
		timer.scheduleAtFixedRate(excuteTimer, 0, 1 * 1000);

	}

	class ExcuteTimer extends TimerTask {

		Process p;

		String runId;

		private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

		public ExcuteTimer(String runId, String source, String desc) {
			this.runId = runId;
		}

		@Override
		public void run() {
			logger.info("Entering Run Method");
			executeShellCommand(runId, sourcePath, destinationPath);

		}

		private Boolean checkRTAComplete(String runId, String src) {
			logger.info("Entering checkRTAComplete");

			File f = new File(getRTACompleteFilePath(runId, src));

			logger.info("File Path: " + f.getAbsolutePath());

			logger.info("Check RTA Complete: " + f.exists());
			return f.exists();
		}

		private String getRTACompleteFilePath(String runId, String src) {
			logger.info("Entering getRTACompleteFilePath");

			logger.info(
					"RUN Complete Path: " + src + runId + "/" + Constants.UPLOAD + "/" + Constants.RTA_COMPLETE_TXT);

			return src + runId + "/" + Constants.UPLOAD + "/" + Constants.RTA_COMPLETE_TXT;
		}

		private String getRTACompleteDescFilePath(String runId, String desc) {
			logger.info("Entering getRTACompleteFilePath");

			logger.info("RUN Complete Path: " + desc + "/" + runId + "/" + Constants.UPLOAD + "/");

			return desc + "/" + runId + "/" + Constants.UPLOAD + "/";
		}

		private void executeShell(String[] command) {

			logger.info("Entering executeShell: " + command.toString());

			try {

				p = Runtime.getRuntime().exec(command);
				p.waitFor();

				logger.info("Finished Process");

				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;

				while ((line = reader.readLine()) != null) {
					logger.info("Input Line : " + line);
				}

			} catch (IOException | InterruptedException e) {
				logger.error("Exception: " + e.getMessage());
			}
		}

		private void executeShellCommand(String runId, String src, String dest) {

			logger.info("Entered into executeShellCommand:");

			logger.info("Source Path : " + src);
			logger.info("Destination Path : " + dest);

			if (checkRTAComplete(runId, src)) {

				logger.info("Entering in to RTA Complete Flow: Path:" + rsync);

				String[] cmd2 = { "sh", rsyncWithChecksum, src, dest };
				executeShell(cmd2);

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				logger.info("Copying RTA Complete Flow: Path:" + rsyncWithRTA);

				String[] cmd3 = { "sh", rsyncWithRTA, src, dest };
				executeShell(cmd3);

				timer.cancel();

				DownloadTimer downloadTimer = new DownloadTimer(runId);

				timer2 = new Timer(true);
				timer2.scheduleAtFixedRate(downloadTimer, 0, 10 * 1000);

			} else {
				logger.info("Entering in to Normal Flow: Path:" + rsync);
				String[] cmd1 = { "sh", rsync, src, dest };
				executeShell(cmd1);

			}

		}
	}

	class DownloadTimer extends TimerTask {

		String runId;

		private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

		public DownloadTimer(String runId) {
			this.runId = runId;
		}

		@Override
		public void run() {
			logger.info("Entering DownlaodTimer run method");
			Response res = null;
			try {
				String jobId = "1234"; // replace with dynamic value
				res = getResponse(jobId);
				logger.info("Response from Analyzer is: " + res.getStatus());
				Map<String, String> map = res.readEntity(Map.class);
				if (map.get("status").equalsIgnoreCase("done")) {

					String downloadSrc = destinationPath + "/" + runId + "/" + Constants.DOWNLOAD + "/";
					String downloadDest = sourcePath + runId + "/" + Constants.DOWNLOAD;

					startDownload(jobId, downloadSrc, downloadDest);
				} else {
					logger.info("File upload process is inprogress,Please wait.......");
				}
			} catch (UnsupportedEncodingException | HMTPException e) {
				logger.error("Error occured while fetching the status from Analyzer", e);
			}
		}

		public void startDownload(String runId, String src, String dest) {

			logger.info("Entered into startDownload:");

			logger.info("Source Path : " + src);
			logger.info("Download Path : " + dest);

			logger.info("Entering in to download flow path:" + rsyncDownload);

			String[] cmd4 = { "sh", rsyncDownload, src, dest };
			executeShell(cmd4);

		}

		private void executeShell(String[] command) {

			logger.info("Entering executeShell: " + Arrays.toString(command));

			try {
				Process p;

				p = Runtime.getRuntime().exec(command);
				p.waitFor();

				logger.info("Finished Process");

				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;

				while ((line = reader.readLine()) != null) {
					logger.info("Input Line : " + line);
				}

				timer2.cancel();
			} catch (IOException | InterruptedException e) {
				logger.error("Exception: " + e.getMessage());
			}
		}
	}

	/*
	 * class UploadTimer extends TimerTask {
	 * 
	 * String runId; String source; String desc;
	 * 
	 * private HMTPLogger logger =
	 * HMTPLoggerFactory.getLogger(this.getClass().getName());
	 * 
	 * public UploadTimer(String runId, String source, String desc) { this.runId =
	 * runId; }
	 * 
	 * @Override public void run() { logger.info("Entering Run Method");
	 * executeShellCommand(runId, source, desc);
	 * 
	 * }
	 * 
	 * private Boolean checkRTAComplete(String runId, String src) {
	 * logger.info("Entering checkRTAComplete");
	 * 
	 * File f = new File(getRTACompleteFilePath(runId, src));
	 * 
	 * return f.exists(); }
	 * 
	 * private String getRTACompleteFilePath(String runId, String src) {
	 * logger.info("Entering getRTACompleteFilePath");
	 * 
	 * logger.info("RUN Complete Path: " + src + "/" + runId + "/" +
	 * Constants.UPLOAD + "/" + Constants.RTA_COMPLETE_TXT);
	 * 
	 * return src + "/" + runId + "/" + Constants.UPLOAD + "/" +
	 * Constants.RTA_COMPLETE_TXT; }
	 * 
	 * private void executeShell(String[] command) {
	 * 
	 * logger.info("Entering executeShell: " + command.toString());
	 * 
	 * try { Process p;
	 * 
	 * p = Runtime.getRuntime().exec(command); p.waitFor();
	 * 
	 * logger.info("Finished Process");
	 * 
	 * BufferedReader reader = new BufferedReader(new
	 * InputStreamReader(p.getInputStream())); String line;
	 * 
	 * while ((line = reader.readLine()) != null) { logger.info("Input Line : " +
	 * line); } } catch (IOException | InterruptedException e) {
	 * logger.error("Exception: " + e.getMessage()); } }
	 * 
	 * private void executeShellCommand(String runId, String src, String dest) {
	 * 
	 * logger.info("Entered into executeShellCommand:");
	 * 
	 * logger.info("Source Path : " + src); logger.info("Destination Path : " +
	 * dest);
	 * 
	 * if (checkRTAComplete(runId, src)) {
	 * 
	 * logger.info("Entering in to RTA Complete Flow: Path:" + rsync);
	 * 
	 * String[] cmd2 = { "sh", rsyncWithChecksum, src, dest }; executeShell(cmd2);
	 * 
	 * logger.info("Copying RTA Complete Flow: Path:" + rsyncWithChecksum);
	 * 
	 * String[] cmd3 = { "sh", rsync, getRTACompleteFilePath(runId, src),
	 * getRTACompleteFilePath(runId, dest) }; executeShell(cmd3);
	 * 
	 * timer.cancel();
	 * 
	 * DownloadTimer downloadTimer = new DownloadTimer(runId);
	 * 
	 * timer2 = new Timer(true); timer2.scheduleAtFixedRate(downloadTimer, 0, 10 *
	 * 1000);
	 * 
	 * } else { logger.info("Entering in to Normal Flow: Path:" + rsync); String[]
	 * cmd3 = { "sh", rsync, src, dest }; executeShell(cmd3);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 */

	public Response getResponse(String jobId) throws HMTPException, UnsupportedEncodingException {

		Response response = null;
		try {
			String url = statusURL + jobId + "/status";

			logger.info("Calling Analyzer for status: " + url);
			Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8),
					null);
			response = orderClient.get();
		} catch (Exception e) {
			logger.info("Exception on Requesting Simulator: " + e.getMessage());
		}

		return response;

	}

}
