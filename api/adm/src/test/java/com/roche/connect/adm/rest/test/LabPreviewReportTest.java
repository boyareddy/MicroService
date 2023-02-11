package com.roche.connect.adm.rest.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.web.multipart.MultipartFile;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.model.LabDetails;
import com.roche.connect.adm.rest.AdminSystemSettingsController;
import com.roche.connect.adm.test.util.JsonFileReaderAsString;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@PrepareForTest({ RestClientUtil.class, JasperCompileManager.class, JasperFillManager.class, JREmptyDataSource.class,
		ThreadSessionManager.class, BufferedInputStream.class, JRPdfExporter.class,ByteArrayOutputStream.class})
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class LabPreviewReportTest {
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	private JasperReport jasperReport;
	@Mock
	JREmptyDataSource dataSource;
	@Mock
	JasperPrint jasperPrint;
	@Mock
	SimpleExporterInput simpleExporterInput;
	@Mock
	SimpleOutputStreamExporterOutput output;
	@Mock
	JRPdfExporter pdfExporter;
	ByteArrayOutputStream pdfReportStream = PowerMockito.mock(ByteArrayOutputStream.class);
	@Mock
	SimplePdfExporterConfiguration configuration;
	@Mock
	InputStream jasperInput;
	@Mock
	OutputStream outputStream;
	@Mock
	LabDetails labDetails;
	@Mock
	MultipartFile image;
	@Mock
	HttpServletResponse httpServletResponse;

	@InjectMocks
	AdminSystemSettingsController adminSystemSettingsController;

	HashMap<String, Object> parameters = new HashMap<>();
	InputStream in = null;
	String text = null;
	String rocheImageURL = null;
	HttpServletResponse response = null;
	String previewJson = null;

	byte[] data = new byte[10];
	public static final String jsonforpreviewreport = "src/test/java/resource/PreviewReportData.json";


	@Test(expectedExceptions=Exception.class)
	public void getLabReportPreviewTest() throws Exception {
		rocheImageURL = "http://localhost";
		parameters.put("headerImage", in);
		parameters.put("textDescription", text);
		parameters.put("rocheLogo", rocheImageURL);

		MockitoAnnotations.initMocks(this);

		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(JasperCompileManager.class);
		PowerMockito.mockStatic(JasperFillManager.class);
		PowerMockito.mockStatic(JRPdfExporter.class);
		

		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		PowerMockito.when(JasperCompileManager.compileReport(Mockito.any(InputStream.class))).thenReturn(jasperReport);
		Mockito.when(RestClientUtil.getUrlString("pas.roche_image_url", "", "", "", null)).thenReturn(rocheImageURL);
		PowerMockito.when(JasperFillManager.fillReport(Mockito.any(JasperReport.class), Mockito.any(HashMap.class),
		Mockito.any(JREmptyDataSource.class))).thenReturn(jasperPrint);
		PowerMockito.whenNew(SimplePdfExporterConfiguration.class).withNoArguments().thenReturn(configuration);
		PowerMockito.whenNew(JRPdfExporter.class).withAnyArguments().thenReturn(pdfExporter);
		PowerMockito.whenNew(ByteArrayOutputStream.class).withNoArguments().thenReturn(pdfReportStream);
		PowerMockito.whenNew(SimpleExporterInput.class).withAnyArguments().thenReturn(simpleExporterInput);
		PowerMockito.whenNew(SimpleOutputStreamExporterOutput.class).withAnyArguments().thenReturn(output);
		PowerMockito.doNothing().when(pdfExporter).exportReport();
		Mockito.when(pdfReportStream.toByteArray()).thenReturn(data);
		Mockito.doNothing().when(httpServletResponse).addHeader(Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(httpServletResponse).setContentType(Mockito.anyString());

		previewJson = JsonFileReaderAsString.getJsonfromFile(jsonforpreviewreport);
		adminSystemSettingsController.getJasperPreviewReport(image, previewJson, httpServletResponse);

	}

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

}
