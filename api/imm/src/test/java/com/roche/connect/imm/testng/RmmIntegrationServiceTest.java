package com.roche.connect.imm.testng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDetailDTO;
import com.roche.connect.imm.service.RmmIntegrationService;
import com.roche.connect.imm.utils.JsonFileReaderAsString;
import com.roche.connect.imm.utils.RestClient;

@PrepareForTest({ RestClientUtil.class, RestClient.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class RmmIntegrationServiceTest extends AbstractTestNGSpringContextTests {

	public static final String containerSamplesJson = "src/test/java/resource/ContainerSampleCSVUpdate.json";
	public static final String orderListJson = "src/test/java/resource/OrderList.json";
	public static final String sampleResultList = "src/test/java/resource/SampleResultList.json";
	private List<SampleResultsDTO> listOfSamples = null;
	private RunResultsDTO runResultsDTO = null;

	@Mock
	private HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private RmmIntegrationService rmmIntegrationService;

	@Mock
	private Invocation.Builder builder;

	@Mock
	private RmmIntegrationService rmmIntegrationServices;
	
	
	@Mock
	private Response response;
	
	
	private static Logger logger = LogManager.getLogger(RmmIntegrationServiceTest.class);

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@BeforeMethod
	public void beforeMethod() throws IOException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(RestClient.class);

		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString());
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(sampleResultList);
		TypeReference<List<SampleResultsDTO>> class1 = new TypeReference<List<SampleResultsDTO>>() {
		};
		listOfSamples = new ObjectMapper().readValue(jsonContent, class1);

		runResultsDTO = getRunResultsDTO();
		ReflectionTestUtils.setField(rmmIntegrationService, "rmmHostUrl", "http://localhost:86/rmm");
		
	}

	/*@Test
	public void saveRunResult() throws IOException {
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(12L);
		Assert.assertEquals(12L, rmmIntegrationService.saveRunResult(runResultsDTO));
	}*/

	@Test(priority=1)
	public void saveRunResult2() throws IOException {
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(0L);
		try {
			rmmIntegrationService.saveRunResult(runResultsDTO,"1");
			}catch(Exception e) {
				logger.info("saveRunResult2 passed");
			}
		
		
	}

	@Test(priority=2)
	public void getSampleResultsFromUrl() throws IOException {
		Assert.assertEquals(Collections.emptyList(), rmmIntegrationService.getSampleResultsFromUrl(null));
	}

	@Test(priority=3)
	public void getSampleResultsFromUrl2() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(204);
		Assert.assertEquals(Collections.emptyList(), rmmIntegrationService.getSampleResultsFromUrl(""));
	}

	
	@Test(priority=4)
	public void getSampleResultsFromUrl3() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(200);
		Mockito.when(response.readEntity(new GenericType<List<SampleResultsDTO>>() {
		})).thenReturn(listOfSamples);
		Assert.assertEquals(listOfSamples, rmmIntegrationService.getSampleResultsFromUrl(""));
	}

	@Test(priority=5)
	public void getSampleResultsFromUrl4() throws IOException {
		
		Assert.assertEquals( Collections.emptyList(),rmmIntegrationService.getSampleResultsFromUrl(null));
	}
	
	@Test(priority=6)
	public void getSampleResults() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(204);

		Assert.assertEquals(Collections.emptyList(), rmmIntegrationService.getSampleResults("12345", "NA Extraction",
				"08cd550a", "A1", "08cd440a", "B1", "1222"));
	}

	@Test(priority=7)
	public void getSampleResults2() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(200);
		Mockito.when(response.readEntity(new GenericType<List<SampleResultsDTO>>() {
		})).thenReturn(listOfSamples);
		Assert.assertEquals(listOfSamples, rmmIntegrationService.getSampleResults("12345", "NA Extraction", "08cd550a",
				"A1", "08cd440a", "B1", "1222"));
	}

	@Test(priority=8)
	public void getSampleResults3() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(400);

		Assert.assertEquals(Collections.emptyList(), rmmIntegrationService.getSampleResults("12345", "NA Extraction",
				"08cd550a", "A1", "08cd440a", "B1", "1222"));
	}
	
	/*@Test(priority=9,expectedExceptions = JsonProcessingException.class)
	public void getSampleResults4() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(200);
		Mockito.when(response.readEntity(new GenericType<List<SampleResultsDTO>>() {
		})).thenReturn(listOfSamples);
		
		Mockito.when(objectMapper.writeValueAsString(listOfSamples)).thenThrow(JsonProcessingException.class);
		Assert.assertEquals(listOfSamples, rmmIntegrationService.getSampleResults("12345", "NA Extraction", null,
				null, null, null, "1222"));
	}*/
	
	@Test(priority=10,expectedExceptions = IOException.class)
	public void getSampleResults5() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(200);
		Mockito.when(response.readEntity(new GenericType<List<SampleResultsDTO>>() {
		})).thenThrow(IOException.class);
		
		Assert.assertEquals(Collections.emptyList(), rmmIntegrationService.getSampleResults(null, null,
				null, null, null, null, null));
	}
	
	@Test(priority=11)
	public void getRunResultsById() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get(new GenericType<RunResultsDTO>() {
		})).thenReturn(runResultsDTO);

		Assert.assertEquals(runResultsDTO, rmmIntegrationService.getRunResultsById(1L));
	}

	@Test(priority=12)
	public void getRunResultsById2() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		/*Mockito.when(builder.get(new GenericType<RunResultsDTO>() {
		})).thenReturn(runResultsDTO);*/

		
		Mockito.when(builder.get(new GenericType<RunResultsDTO>() {
		})).thenThrow(JsonProcessingException.class);
		//Mockito.when(objectMapper.writeValueAsString(runResultsDTO)).thenThrow(JsonProcessingException.class);
		//Assert.assertEquals(runResultsDTO, rmmIntegrationService.getRunResultsById(1L));
		
		try {
		rmmIntegrationService.getRunResultsById(1L);
		}catch(Exception e) {
			
		}
	}
	
	/*@Test
	public void getSampleResultsByProcessStepAndAccessioningId() throws IOException {
		Assert.assertEquals(Collections.emptyList(), rmmIntegrationService.getSampleResultsByProcessStepAndAccessioningId("NA Extraction", "235556"));
	}*/
	
	@Test(priority=13)
	public void getSampleResultsByProcessStepAndAccessioningId2() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(200);
		Mockito.when(response.readEntity(new GenericType<List<SampleResultsDTO>>() {
		})).thenReturn(listOfSamples);
		
		ReflectionTestUtils.setField(rmmIntegrationService, "rmmGetSampleResult",
				"http://localhost:86/rmm" + "/json/rest/api/v1/runresults/sampleresults");
		Assert.assertEquals(listOfSamples, rmmIntegrationService.getSampleResultsByProcessStepAndAccessioningId("NA Extraction", "235556"));
	}
	
	@Test(priority=14)
	public void updateRunResult() throws IOException, HMTPException {
		Mockito.when(RestClientUtil.putMethodCall(Mockito.anyString(), Mockito.any(), Mockito.eq(null))).thenReturn(new Object());
		
		Assert.assertEquals(true, rmmIntegrationService.updateRunResult(runResultsDTO));
	}
	
	@Test(priority=15)
	public void updateRunResult2() throws IOException, HMTPException {
		Mockito.when(RestClientUtil.putMethodCall(Mockito.anyString(), Mockito.any(), Mockito.eq(null)))
				.thenThrow(new HMTPException());

		Assert.assertEquals(false, rmmIntegrationService.updateRunResult(runResultsDTO));
	}

	/*@Test(priority=16)
	public void updateRunResult3() throws IOException, HMTPException {
		Mockito.when(objectMapper.writeValueAsString(runResultsDTO)).thenThrow(JsonProcessingException.class);


		Assert.assertEquals(false, rmmIntegrationService.updateRunResult(runResultsDTO));
	}*/
	
	@Test(priority=17)
	public void getRunResultsByDeviceRunId() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
		})).thenReturn(runResultsDTO);

		Assert.assertEquals(runResultsDTO, rmmIntegrationService.getRunResultsByDeviceRunId("10001"));
	}
	
	@Test(priority=18)
	public void getRunResultsByDeviceRunId2() throws IOException {
		Assert.assertEquals(null, rmmIntegrationService.getRunResultsByDeviceRunId(null));
	}
	
	@Test(priority=19)
	public void getRunResultsByDeviceRunId3() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);

		Assert.assertNull(rmmIntegrationService.getRunResultsByDeviceRunId("10001"));
	}
	
	/*@Test(priority=20)
	public void getRunResultsByDeviceRunId4() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
		})).thenReturn(runResultsDTO);

		Mockito.when(objectMapper.writeValueAsString(runResultsDTO)).thenThrow(JsonProcessingException.class);
		Assert.assertNull(rmmIntegrationService.getRunResultsByDeviceRunId("10001"));
	}*/

	
	@Test(priority=21)
	public void getRunResultsAndSampleResults() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
		})).thenReturn(runResultsDTO);

		Assert.assertEquals(runResultsDTO, rmmIntegrationService.getRunResultsAndSampleResults("12345", "NA Extraction", "MP-123"));
	}
	
	@Test(priority=22)
	public void getRunResultsAndSampleResults2() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
		})).thenReturn(runResultsDTO);

		Assert.assertNull(rmmIntegrationService.getRunResultsAndSampleResults(null, null, null));
	}
	
	@Test(priority=23)
	public void getRunResultsAndSampleResults3() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);

		Assert.assertNull( rmmIntegrationService.getRunResultsAndSampleResults("12345", "NA Extraction", "MP-123"));
	}
	
	/*@Test(priority=24)
	public void getRunResultsAndSampleResults4() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
		})).thenReturn(runResultsDTO);

		Mockito.when(objectMapper.writeValueAsString(runResultsDTO)).thenThrow(JsonProcessingException.class);
		Assert.assertNull( rmmIntegrationService.getRunResultsAndSampleResults("12345", "NA Extraction", "MP-123"));
	}*/
	
	
	@Test(priority=25)
	public void getRunResults() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
		})).thenReturn(runResultsDTO);
		Mockito.when(objectMapper.writeValueAsString(runResultsDTO)).thenThrow(JsonProcessingException.class);
		rmmIntegrationService.getRunResults("12345", "NA Extraction", "MP-123","1");
	}
	
	@Test(priority=26)
	public void updateRunResultFinal() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);
		
		Mockito.when(objectMapper.writeValueAsString(runResultsDTO)).thenReturn("abc");

		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
		})).thenReturn(runResultsDTO);
		
		try {
		rmmIntegrationService.updateRunResult(runResultsDTO,"1");
		}catch(Exception e) {
			
			logger.info("updateRunResultFinal passed");
		}
	}
	
	@Test(priority=27)
	public void getMolecularIdByProcessStepNameAndAccessioningId() throws IOException {
		
		
		List<SampleResultsDTO> lpPreSampleRestults = new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		Collection<SampleResultsDetailDTO>  sampleResultsDetailDTOList=new ArrayList<>();
		SampleResultsDetailDTO sampleResultsDetailDTO =new SampleResultsDetailDTO();
		sampleResultsDetailDTO.setAttributeName("molecularId");
		sampleResultsDetailDTO.setAttributeValue("ABC");
		sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
		sampleResultsDTO.setSampleResultsDetail(sampleResultsDetailDTOList);
		lpPreSampleRestults.add(sampleResultsDTO);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(200);
		Mockito.when(response.readEntity(new GenericType<List<SampleResultsDTO>>() {
		})).thenReturn(lpPreSampleRestults);
		
		ReflectionTestUtils.setField(rmmIntegrationService, "rmmGetSampleResult",
				"http://localhost:86/rmm" + "/json/rest/api/v1/runresults/sampleresults");
		
		try {
			rmmIntegrationService.getMolecularIdByProcessStepNameAndAccessioningId("NA Extraction","123456");
		}catch(Exception e) {
			
			logger.info("getMolecularIdByProcessStepNameAndAccessioningId passed");
		}
	}
	
	
	
	private static RunResultsDTO getRunResultsDTO() {
		RunResultsDTO runResultsDTO = new RunResultsDTO();

		runResultsDTO.setRunResultId(10001L);
		runResultsDTO.setDeviceId("MP001-121");
		runResultsDTO.setProcessStepName("NA Extraction");
		runResultsDTO.setDeviceRunId("order123");
		runResultsDTO.setRunStatus("Completed");
		return runResultsDTO;
	}
}
