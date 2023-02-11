package com.roche.connect.rmm.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.lowagie.text.pdf.PdfWriter;
import com.roche.connect.adm.dto.SystemSettingsDto;
import com.roche.connect.rmm.jasper.dto.Imagepojo;
import com.roche.connect.rmm.services.AdmIntegrationService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class JasperUtils {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	private static String reportFolder = "/jasper";
	private static String pathSeperator = "/";
	
	public JasperReport loadCompiledJasper(String reportName) throws JRException, IOException {
		InputStream inputStream = null;
		JasperReport jasperReport = null;
		try {
				inputStream = this.getClass().getResourceAsStream(reportFolder + pathSeperator + reportName);
				jasperReport = (JasperReport)JRLoader.loadObject(inputStream);
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
		return jasperReport;
	}

	public SimplePdfExporterConfiguration setReportPassword(String password) {
		logger.info("Start of filling the configuration for password protection");
		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		configuration.setEncrypted(true);
		configuration.set128BitKey(true);
		configuration.setUserPassword(password);
		configuration.setOwnerPassword(password);
		configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
		logger.info("End of filling the configuration for password protection");
		return configuration;
	}

	public byte[] getReportPDF(SimplePdfExporterConfiguration configuration, JasperPrint jasperPrint)
			throws JRException {
		logger.info("exporting the data into pdf");
		JRPdfExporter pdfExporter = new JRPdfExporter();
		pdfExporter.setConfiguration(configuration);

		pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
		pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
		pdfExporter.exportReport();
		return pdfReportStream.toByteArray();

	}

	public void setReportParameters(Map<String, Object> parameters, String language, String country,String module) throws HMTPException, IOException {
		logger.info("Start of setting report logo and address details");
		Locale locale = new Locale(language, country);
		ResourceBundle resourceBundle = ResourceBundle.getBundle(module, locale);
		
		AdmIntegrationService admIntegrationService = new AdmIntegrationService();
		List<SystemSettingsDto> systemSettings = admIntegrationService.getSystemSettings();
		String text = admIntegrationService.getLabDetails(systemSettings);
		Imagepojo pojo = admIntegrationService.getLabLogo();
		BufferedImage bufImage = ImageIO.read(pojo.getStream());
		
		parameters.put("REPORT_LOCALE", locale);
		parameters.put("REPORT_RESOURCE_BUNDLE", resourceBundle);
		parameters.put("headerImage", bufImage);
		parameters.put("textDescription", text);
		
		logger.info("End of setting report logo and address details");
	}
	
	public JasperPrint fillReport(JasperReport jasperReport,Map<String,Object> parameters,JRBeanCollectionDataSource dataSource) throws JRException {
		return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
	}
}
