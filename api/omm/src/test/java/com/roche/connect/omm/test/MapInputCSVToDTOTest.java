package com.roche.connect.omm.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.CSVParserUtil;

/**
 * 
 * @author imtiyazm
 *
 */
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class MapInputCSVToDTOTest {

	@InjectMocks
	OrderServiceImpl orderServiceImpl ;

	@InjectMocks
	CSVParserUtil csvParserUtil;
	
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	String expectedCorrectStatus = null;
	String expectedIncorrectStatus = null;

	List<ContainerSamplesDTO> containerSampleDTOList = null;

	String csvFilePath = null;
	List<String> parsedCSV = null;
	InputStream stream = null;
	File csvFile = null; 

	@BeforeTest
	public void setUp() throws HMTPException,Exception {

		expectedCorrectStatus = "96 wellplate";
		expectedIncorrectStatus = "dummyValue";
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        
		containerSampleDTOList = new LinkedList<>();
		csvFilePath = "src/test/java/resource/samplecsv.csv";
		csvFile = new File(csvFilePath);
		stream = new FileInputStream(csvFile);
		
		parsedCSV = csvParserUtil.parseCSV(stream);

		containerSampleDTOList = orderServiceImpl.mapInputCSVToDTO(parsedCSV);
	}

	@Test(priority = 1)
	public void getMapInputCSVToDTOPosTest() throws Exception {
		String statusUpdate = containerSampleDTOList.get(0).getContainerType();
		assertEquals(statusUpdate, expectedCorrectStatus);
	}

	@Test(priority = 2)
	public void getMapInputCSVToDTONegTest() throws HMTPException {
		String statusUpdate = containerSampleDTOList.get(0).getContainerType();
		assertNotEquals(statusUpdate, expectedIncorrectStatus);
	}

	@AfterTest
	public void afterTest() {
		expectedCorrectStatus = null;
		expectedIncorrectStatus = null;
	}

}
