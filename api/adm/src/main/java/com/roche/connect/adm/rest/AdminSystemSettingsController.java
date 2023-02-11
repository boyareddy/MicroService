package com.roche.connect.adm.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.model.LabDetails;
import com.roche.connect.adm.model.SystemSettings;
import com.roche.connect.adm.util.ADMConstants;
import com.roche.connect.adm.util.UtilityService;
import com.roche.connect.adm.writerepository.SystemSettingsWriteRepository;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@RestController
public class AdminSystemSettingsController  {
	
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	
	private static final String IMAGE_PATTERN = ".+\\.(?i:jpg|png|jpeg)";
    private static final long IMAGE_SIZE =1048576L;
	
	@Autowired
	SystemSettingsWriteRepository systemSettingsWriteRepository;
	
	
	@PostMapping("/upload")
	@PreAuthorize("validateExpFromProperty('ConnectLabSettings.SystemSettings.preauthorize')")
	public String imageUpload(@RequestParam("file") MultipartFile file) throws HMTPException {
		try {
			logger.info("Start of image upload execution -> uploadImage()");
			if (!file.isEmpty()) {
				String filename =file.getOriginalFilename();
		        long  size =file.getSize();
		        
		        if(!Pattern.matches(IMAGE_PATTERN ,filename)) {
		        	throw new HMTPException(ADMConstants.ErrorMessages.INVALID_LAB_LOGO_FORMAT.toString());
		        } 
		        
		        if(size>IMAGE_SIZE) {
		        	throw new HMTPException(ADMConstants.ErrorMessages.INVALID_LAB_LOGO_SIZE.toString());
		        }

				SystemSettings systemSetting = new SystemSettings();
				systemSetting.setActiveFlag(ADMConstants.SystemSettings.ACTIVE_FLAG.toString());
				systemSetting.setAttributeName(ADMConstants.SystemSettings.LAB_LOGO_ATTR_NAME.toString());
				systemSetting.setAttributeType(ADMConstants.SystemSettings.REP_SETTING_ATTR_TYPE.toString());
				systemSetting.setImage(file.getBytes());
				systemSetting.setCreatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
				systemSetting.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
				systemSetting.setUpdatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
				systemSetting.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));

				systemSettingsWriteRepository.save(systemSetting);
			}
		} catch (Exception e) {
			logger.error("Error while saving Lab logo", e.getMessage());
			throw new HMTPException(e);
		}
		logger.info("End of image upload execution -> uploadImage()");
		return ADMConstants.ErrorMessages.LAB_LOGO_SAVE_SUCCESS.toString();
	}
	
	@PostMapping("/labdetailpreviewreport")
	@PreAuthorize("validateExpFromProperty('ConnectLabSettings.SystemSettings.preauthorize')")
	public void getJasperPreviewReport(@RequestParam("image") MultipartFile image,
										@RequestParam("data") String data,
										HttpServletResponse response) throws HMTPException, IOException {
		logger.info("getJasperPreviewReport -> Start of getJasperPreviewReport() execution ");
		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		HashMap<String, Object> parameters = new HashMap<>();
		String rocheImageURL = null;
		try{			
			final InputStream jasperInput = this.getClass().getResourceAsStream("/jasper/reportpreview.jrxml");	
			jasperReport = JasperCompileManager.compileReport(jasperInput);
						
			rocheImageURL = RestClientUtil.getUrlString("pas.roche_image_url", "", "", "", null);			
			
			ObjectMapper mapper = new ObjectMapper();	
			LabDetails labDetails = mapper.readValue(data, LabDetails.class);
			
			if(UtilityService.validateLabDetails(labDetails)){
				if(!image.isEmpty()){
					InputStream in = image.getInputStream();
					parameters.put("headerImage", in);}
				
				parameters.put("textDescription", getLabDetails(labDetails));			
				
				if(rocheImageURL!=null)
					parameters.put("rocheLogo", rocheImageURL);
				
				
				jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, new JREmptyDataSource());
				JRPdfExporter pdfExporter = new JRPdfExporter();
				pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
				pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
				pdfExporter.exportReport();
				byte[] bytes = pdfReportStream.toByteArray();
		
				logger.info("getJasperPreviewReport -> JasperReport Generated Successfully: ");
				response.addHeader("Content-disposition","attachment;filename=myfilename.pdf");
				response.setContentType("application/pdf");
				IOUtils.copy(new ByteArrayInputStream(bytes),response.getOutputStream());
			}else{
				throw new HMTPException(ADMConstants.ErrorMessages.ADDRESS_LENGTH_INVALID.toString());
			}
		}catch(HMTPException exp){
			response.sendError(400, exp.getMessage());
		}catch(Exception exp){
			logger.info("getJasperPreviewReport -> Error while creating the preview of the report ");
			throw new HMTPException(exp.getMessage());
		}
	}	
	
	public String getLabDetails(LabDetails labDetails){
		StringBuilder sb = new StringBuilder();
		if(!labDetails.getLabName().isEmpty()){
			sb.append(labDetails.getLabName());
			if(!labDetails.getLabAddress1().isEmpty()){
				sb.append(" | ");
			}else{
				sb.append("\n");
			}
		}
		if(!labDetails.getLabAddress1().isEmpty()){
			sb.append(labDetails.getLabAddress1());
			sb.append("\n");
		}
		if(!labDetails.getLabAddress2().isEmpty()){
			sb.append(labDetails.getLabAddress2());
			sb.append("\n");
		}
		if(!labDetails.getLabAddress3().isEmpty()){
			sb.append(labDetails.getLabAddress3());
			sb.append("\n");
		}
		if(!labDetails.getPhoneNumber().isEmpty()){
			sb.append("Tel-");
			sb.append(labDetails.getPhoneNumber());
		}
		return sb.toString();
		
	}
}
