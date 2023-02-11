package com.roche.connect.adm.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.roche.connect.adm.model.ProblemReport;
import com.roche.connect.adm.util.ADMConstants;
import com.roche.connect.adm.util.AuditTrailDetailDTO;
import com.roche.connect.adm.util.AuditTrailResponseDTO;
import com.roche.connect.adm.util.DeviceSummaryDTO;

@Service
public class ProblemReportServiceImp implements ProblemReportService {
	
	@Value("${auditLog.requiredColumns}")
	private String columnsToMap;
	@Value("${pas.audittrail_remote_url}")
	private String auditURL;
	@Value("${auditLog.limit}")
	private long limit;
	@Value("${pas.rmm_host_url}")
	private String rmmURL;
	@Value("${problemReport.dailyReportPath}")
	private String dailyReportPath;
	@Value("${problemReport.problemReportPath}")
	private String problemReportPath;
	@Value("${problemReport.tempReportPath}")
	private String tempReportPath;
	@Value("${pas.dm_host_url}")
	String deviceEndPoint;
	@Value("${logging.path}")
	private String loggerFilesFolderPath;
	/** The login url. */
	@Value("${pas.authenticate_url}")
	private String loginUrl;

	/** The login entity. */
	@Value("${pas.login_entity}")
	private String loginEntity;
	private String dateFormat="ddMMyyyy_HHmmss.sss";
	private String dateWithoutTime="yyyy-MM-dd";
	private String dateWithTime="yyyy-MM-dd HH:mm:ss";
	private String problemReport="problemReport";
	private String runtimeLogs="runtimeLogs";
	private static final String BROWNSTONEAUTHCOOKIE = "brownstoneauthcookie=";
	private static final String COOKIE = "cookie";
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
	
	public File createCSVFileForAuditLog(AuditTrailResponseDTO auditTrailResponseDTO,File auditCsvFile,File auditZipFile,Writer writer) throws HMTPException {
		
		try{
			ColumnPositionMappingStrategy<AuditTrailDetailDTO> columnMpping = new ColumnPositionMappingStrategy<>();
			columnMpping.setColumnMapping(columnsToMap.split(","));
			columnMpping.setType(AuditTrailDetailDTO.class);
			StatefulBeanToCsvBuilder<AuditTrailDetailDTO> builder= new StatefulBeanToCsvBuilder<>(writer); 
			StatefulBeanToCsv<AuditTrailDetailDTO> beanToCsv = builder.withMappingStrategy(columnMpping).build();
			beanToCsv.write(auditTrailResponseDTO.getEntity().getLstAuditTrail());
			auditZipFile = generateZipFile(auditCsvFile, auditZipFile);
			logger.info("csv file deleted successfully ");
		} catch (Exception e) {
			logger.error("Error occured while creating csv file:: "+e.getMessage());
			throw new HMTPException();
		} 
		return auditZipFile;
	}
	
	public File generateZipFile(File csvFile,File zipFile) throws HMTPException {
		try (FileOutputStream fos = new FileOutputStream(zipFile.getPath());
				ZipOutputStream zipOS = new ZipOutputStream(fos);
				FileInputStream fileInputStream = new FileInputStream(csvFile)) {
			ZipEntry zipEntry = new ZipEntry(csvFile.getName());
			zipOS.putNextEntry(zipEntry);
			byte[] buf = new byte[1024];
			int len;
			while ((len = fileInputStream.read(buf)) > 0) {
				zipOS.write(buf, 0, len);
			}
			zipOS.closeEntry();
		}catch (Exception e) {
			logger.error("Error occured while zipping file:: "+e.getMessage());
			throw new HMTPException();
		} 
		return zipFile;
	}

	@Override
	public File getAuditDetailsFile(String fromDate, String toDate, String companydomainname,String folderPath) throws HMTPException {
		Builder builder = null;
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_HHmmss.sss");
		String strDate = formatter.format(new Date());
		String filepath = folderPath + ADMConstants.AUDIT_FILE_NAME + strDate + ".csv";
		String zipFilePath = folderPath + ADMConstants.AUDIT_FILE_NAME + strDate + ".zip";
		File file = new File(filepath);
		file.getParentFile().mkdirs();
		File zip =new File(zipFilePath);
		long offset=1;
		long total=limit;
		try(Writer writer = new FileWriter(filepath)){
			writer.write(columnsToMap+"\n");
		while(((total/(limit+offset-1))>=1) || offset< total) {	
		String url = auditURL + "/getaudittrail?fromDate=" + UriEncoder.encode(fromDate) + "&toDate=" + UriEncoder.encode(toDate) + "&sortorder=DESC&offset="+offset+"&limit="+limit;
		if (companydomainname != null) {
			url = url + "&companydomainname=" + companydomainname;
		}			
		builder = RestClientUtil.getBuilder(url, null);
		builder.header(COOKIE, BROWNSTONEAUTHCOOKIE + getToken());
		logger.info("audit URL::"+url);
		logger.info("Response from audittrail ::"+builder.get(String.class));
		AuditTrailResponseDTO auditTrailResponseDTO = builder.get(AuditTrailResponseDTO.class);
		total=auditTrailResponseDTO.getEntity().getTotalCount();
		zip = createCSVFileForAuditLog(auditTrailResponseDTO, file, zip, writer);
		offset = offset+limit;
		}
		Files.delete(file.toPath());
		}catch (Exception e) {
			logger.error("Error occured while calling the audit-trail-api:: "+e.getMessage());
			throw new HMTPException();
		}
		return zip;
		
	}

	@Override
	public File getDeviceSummaryLogs(String fromDate, String toDate, String folderPath) throws HMTPException, IOException {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String strDate = formatter.format(new Date());
		String filepath = folderPath + ADMConstants.DEVICE_FILE_NAME + strDate + ".csv";
		String zipFilePath = folderPath + ADMConstants.DEVICE_FILE_NAME + strDate + ".zip";
		File deviceCsvFile = new File(filepath);
		deviceCsvFile.getParentFile().mkdirs();
		File deviceZipFile =new File(zipFilePath);
		try(Writer writer1 = new FileWriter(filepath)){
			String url = rmmURL + "/json/rest/api/v1/DeviceSummaryLog?fromDate=" + UriEncoder.encode(fromDate) + "&toDate=" + UriEncoder.encode(toDate);
			Builder builder = RestClientUtil.getBuilder(url, null);
			builder.header(COOKIE, BROWNSTONEAUTHCOOKIE + getToken());
			List<DeviceSummaryDTO> deviceSummaryDTOList = builder.get(new GenericType<List<DeviceSummaryDTO>>() {});
			getDeviceTypeDetails(deviceSummaryDTOList);
			if(!deviceSummaryDTOList.isEmpty()) {
				StatefulBeanToCsvBuilder<DeviceSummaryDTO> csvBuilder= new StatefulBeanToCsvBuilder<>(writer1); 
				StatefulBeanToCsv<DeviceSummaryDTO> beanToCsv = csvBuilder.build();
				beanToCsv.write(deviceSummaryDTOList);
			}else {
				writer1.write("No run information available for the given date");
			}
		}catch (Exception e) {
			logger.error("Error occured while calling the audit-trail-api:: "+e.getMessage());
			throw new HMTPException();
		}
		deviceZipFile = generateZipFile(deviceCsvFile, deviceZipFile);
		Files.delete(deviceCsvFile.toPath());
		return deviceZipFile;
	}
	
	public List<DeviceSummaryDTO> getDeviceTypeDetails(List<DeviceSummaryDTO> deviceSummaryDTOList) throws HMTPException {
		try {
			for(DeviceSummaryDTO deviceSummaryDTO : deviceSummaryDTOList){
				String parameters = "serialNo='" + deviceSummaryDTO.getDeviceId() + "'";
				String url = deviceEndPoint + "/json/device/fetch/expr?filterExpression=" + URLEncoder.encode(parameters,"UTF-8");
				Builder builder = RestClientUtil.getBuilder(url, null);
				builder.header(COOKIE, BROWNSTONEAUTHCOOKIE + getToken());
				String resp = builder.get(String.class);
				JSONArray respobj = new JSONArray(resp);
				if(respobj.length()>0)
					deviceSummaryDTO.setDeviceType(respobj.getJSONObject(0).getJSONObject("deviceType").getString("name"));
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("Error occured while updating device type:: "+e.getMessage());
			throw new HMTPException();
		}
		return deviceSummaryDTOList;
	}
	public File getRunTimeLogsBetweenDates(Date startdate, Date enddate, String folderPath, String reportType, String sourceFolderPath ) throws HMTPException{
		List<String> listOfAllFilePaths=new ArrayList<>();
		File zipFile=null;
		List<Date> datesInRange=	getDaysBetweenDates(startdate,enddate);
		for(Date date:datesInRange){
			List<String> listOfFilePaths=filterFileNames(date, folderPath, reportType,sourceFolderPath);
			for(String filepath:listOfFilePaths){
				listOfAllFilePaths.add(filepath);
			}		
		}
		if (!listOfAllFilePaths.isEmpty()) {
			zipFile = zipFiles(listOfAllFilePaths, folderPath);
			if (reportType.equalsIgnoreCase(problemReport)) {
				File problemReportZipFile = new File(folderPath + "Connect_Problem_Report_"
						+ new SimpleDateFormat(dateWithoutTime).format(startdate) + "_"
						+ new SimpleDateFormat(dateWithoutTime).format(enddate) + ".zip");
				boolean isRenamed = zipFile.renameTo(problemReportZipFile);
				logger.info("is file renamed? " + isRenamed);
				if (isRenamed)
					return problemReportZipFile;
			}}
			return zipFile;
			
		}
	
	public  List<Date> getDaysBetweenDates(Date startdate, Date enddate)
	{
		String startdateString= simpleDateFormat.format(startdate);
		String enddateString=	simpleDateFormat.format(enddate);
		List<Date> dates = new ArrayList<>();
		if (startdateString.equals(enddateString)) {
			dates.add(startdate);
		} else {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(startdate);
			while (calendar.getTime().before(enddate)) {
				Date result = calendar.getTime();
				dates.add(result);
				calendar.add(Calendar.DATE, 1);
			}
		}
		return dates;
	}

	public List<String> filterFileNames(Date givendate, String folderPath, String reportType, String sourceFolderPath) throws HMTPException {
		String validateAlphabets = "[a-z-_]*.log";
		List<String> listoflogFileNames = new ArrayList<>();
		Date currentDate = new Date();
		String currentDateString = simpleDateFormat.format(currentDate);
		String givenDateString = simpleDateFormat.format(givendate);
		File[] files = null;
		try {
		File directory = new File(sourceFolderPath);
		if(reportType.equalsIgnoreCase(runtimeLogs) && currentDateString.equals(givenDateString)){
			FileFilter filter = new RegexFileFilter(validateAlphabets);
			files = directory.listFiles(filter);
		} else {
			final FilenameFilter filter = (dir,	name) -> name.contains(simpleDateFormat.format(givendate));
			files = directory.listFiles(filter);
		}
			for (File logfile : files) {
				listoflogFileNames.add(logfile.getAbsolutePath());
			}
			if (reportType.equalsIgnoreCase(runtimeLogs) && currentDateString.equals(givenDateString))
				zipFiles(listoflogFileNames, folderPath);
		} catch (Exception exception) {
			logger.error("Error occured while creating the getDailyRunTimeLogFile:: " + exception.getMessage());
			throw new HMTPException();
		}
		return listoflogFileNames;
	}
	
     public   File zipFiles(List<String> files, String folderPath) throws HMTPException{  
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String strDate = formatter.format(new Date());
		String zipFilePath =  folderPath + ADMConstants.RUNTIME_FILE_NAME +strDate + ".zip";
		logger.info("::::"+zipFilePath); 
        File file = new File(zipFilePath);
    	file.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file);
        		ZipOutputStream zipOut = new ZipOutputStream(fos);){	
                for(String filePath:files){
                	addtoZip(filePath, zipOut);
                }
            } catch (IOException e) {
            	logger.error("Error occured while Zip the files:: "+e.getMessage());
            	throw new HMTPException();
            } 
        logger.info("Done... Zipped the files...");
    		return file;
    }

/**
 * @param filePath
 * @param zipOut
 * @throws HMTPException 
 * @throws IOException
 */
private void addtoZip(String filePath, ZipOutputStream zipOut) throws HMTPException {
	File input = new File(filePath);
	try(FileInputStream fis = new FileInputStream(input)){
		ZipEntry ze = new ZipEntry(input.getName());
		logger.info("Zipping the file: "+input.getName());
		zipOut.putNextEntry(ze);
		byte[] tmp = new byte[4*1024];
		int size = 0;
		while((size = fis.read(tmp)) >= 0){
		    zipOut.write(tmp, 0, size);
		}
		zipOut.flush();	
		zipOut.closeEntry();
	}catch (Exception e) {
		logger.error("error while Zipping the file: "+e.getMessage());
		throw new HMTPException();
	}
}
@Scheduled(cron = "${problemReport.cron_time}")
public void runProblemReportsOnDailyBasis() throws HMTPException {
	try {
		File file=new File(dailyReportPath);
		file.getParentFile().mkdirs(); 
		String fromDate=new SimpleDateFormat(dateWithoutTime).format(new Date());
		String	toDate=new SimpleDateFormat(dateWithTime).format(new Date());
		fromDate = fromDate.concat(" 00:00:00");
		getAuditDetailsFile(fromDate, toDate, "*", dailyReportPath);
		getDeviceSummaryLogs(fromDate,toDate, dailyReportPath);
		filterFileNames(new Date(), dailyReportPath, runtimeLogs, loggerFilesFolderPath);
	} catch (IOException e) {
		logger.error("Error while excuting daily problem reports : " + e.getMessage());
	}
}

@Override
public void generateProblemReport(String fromDate, String toDate) throws HMTPException {
	try {
		File dailyfile=new File(dailyReportPath);
		dailyfile.mkdirs();
		File tempFile=new File(tempReportPath);
		tempFile.mkdirs();
		File problmfile=new File(problemReportPath);
		problmfile.mkdirs();
		Date fDate = new SimpleDateFormat(dateWithTime).parse(fromDate);
		Date tDate =  new SimpleDateFormat(dateWithTime).parse(toDate);
		String currentDate = new SimpleDateFormat(dateWithoutTime).format(new Date());
		if(fromDate.contains(currentDate) || toDate.contains(currentDate) ) {
			deleteTempDir(tempReportPath); 
			getDeviceSummaryLogs(fromDate,toDate, tempReportPath);
			getAuditDetailsFile(fromDate, toDate, "*", tempReportPath);
			filterFileNames(new Date(), tempReportPath, runtimeLogs, loggerFilesFolderPath);
			File file=getRunTimeLogsBetweenDates(fDate, tDate, tempReportPath, problemReport, dailyReportPath);
			if(file!=null)
				unzip(file.getAbsolutePath(),tempReportPath);
			getRunTimeLogsBetweenDates(fDate, tDate, problemReportPath, problemReport, tempReportPath);   
		}else{
			getRunTimeLogsBetweenDates(fDate, tDate, problemReportPath, problemReport, dailyReportPath);
		}
	} catch (ParseException | HMTPException | IOException e) {
		logger.error("Error while retriving runresults based on date range: " + e.getMessage());
		throw new HMTPException();
	}
}


/**
 * @throws IOException
 */
	public void deleteTempDir(String path) throws IOException {
		try (Stream<Path> walk = Files.walk(Paths.get(path))) {
			walk.filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
		} catch (IOException e) {
			logger.error("uable to delete the temp  directory" + e.getMessage());
		}
	}

public  void unzip(final String fileZip , final String unZipDestination) throws HMTPException, IOException { 
	logger.info("enter the unzip()::"); 
	final File destDir = new File(unZipDestination);
	if (fileZip!=null&&new File(fileZip).exists()) {
		final byte[] buffer = new byte[1024];
		try (FileInputStream fis = new FileInputStream(fileZip); final ZipInputStream zis = new ZipInputStream(fis);) {
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				final File newFile = newFile(destDir, zipEntry);
				unzipFile(buffer, zis, newFile);
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();

		} catch (Exception e) {
			logger.error("unable to unzip the file", e.getMessage());
			throw new HMTPException();
		}
		Files.delete(Paths.get(fileZip));
	}else{
		logger.error("File is not found in given path");
	} 
}

/**
 * @param buffer
 * @param zis
 * @param newFile
 * @throws HMTPException 
 * 
 * @throws IOException
 */
 private  void unzipFile(final byte[] buffer, final ZipInputStream zis, final File newFile) throws HMTPException{
	 try( FileOutputStream fos = new FileOutputStream(newFile)){
		 int len;
		 while ((len = zis.read(buffer)) > 0) {
			 fos.write(buffer, 0, len);
		 }
	 }catch (Exception e) {
		 logger.error("unable to unzip the file",e.getMessage());
		 throw new HMTPException();
	 }
}

 public  File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
	 File destFile = new File(destinationDir, zipEntry.getName());
	 String destDirPath = destinationDir.getCanonicalPath();
	 String destFilePath = destFile.getCanonicalPath();
	 if (!destFilePath.startsWith(destDirPath + File.separator)) {
		 throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
	 }
	 return destFile;
 }
 
 public ProblemReport getProblemReport(String fromDate, String toDate, String problemReportPath) {
	 
		String userName = ThreadSessionManager.currentUserSession().getAccessorUserName();
		ThreadSessionManager.currentUserSession().getRoleForTenantAccess();
		ProblemReport problemReport = new ProblemReport();
		problemReport.setStartDate(fromDate);
		problemReport.setEndDate(toDate);
		problemReport.setEditedBy(userName);
		problemReport.setModifiedDate(new Date());
		problemReport.setCreatedBy(userName);
		problemReport.setCreatedDate(new Date());
		problemReport.setProblemReportPath(problemReportPath);
		return problemReport;
	}
 
	public String getToken() {
		Builder pasBuilder = RestClientUtil.getBuilder(loginUrl, null);
		return pasBuilder.post(Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED), String.class);
	}
 
}
