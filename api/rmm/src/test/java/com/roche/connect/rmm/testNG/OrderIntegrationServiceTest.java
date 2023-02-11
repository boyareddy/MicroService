package com.roche.connect.rmm.testNG;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.RestClient;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.rmm.services.OrderIntegrationService;
import com.roche.connect.rmm.test.util.JsonFileReaderAsString;
import com.roche.connect.rmm.util.RMMConstant;

@PrepareForTest({ RestClientUtil.class, RestClient.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class OrderIntegrationServiceTest {

	private static final String containerSamplesJson = "src/test/java/resource/ContainerSampleCSVUpdate.json";
	private ObjectMapper objectMapper = new ObjectMapper();
	private List<ContainerSamplesDTO> containerSamples = null;

	@Mock
	private HMTPLoggerImpl hmtpLoggerImpl;

	@InjectMocks
	private OrderIntegrationService orderIntegrationService = new OrderIntegrationService();

	@Mock
	private Invocation.Builder builder;

	@Mock
	ObjectMapper objectMappers;

	@Mock
	private Response response;
	@Mock Invocation.Builder ommClient;
	List<OrderDTO> ordeDTOlist = new ArrayList<>();
	List<SampleResultsDTO> sampleResultsDTO = new ArrayList<>();

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

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(containerSamplesJson);
		TypeReference<List<ContainerSamplesDTO>> class1 = new TypeReference<List<ContainerSamplesDTO>>() {
		};
		containerSamples = objectMapper.readValue(jsonContent, class1);

		ReflectionTestUtils.setField(orderIntegrationService, "ommHostUrl", "http://localhost:96/omm");
	}

	@Test
	public void getDPCRContainerSamplesWithDeviceRunId() throws IOException, Exception {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get(new GenericType<List<ContainerSamplesDTO>>() {
		})).thenReturn(containerSamples);

		Assert.assertEquals(containerSamples, orderIntegrationService.getDPCRContainerSamples("RND12345"));
	}

	@Test
	public void getDPCRContainerSamplesWithDeviceRunId2() {

		Assert.assertEquals(Collections.emptyList(), orderIntegrationService.getDPCRContainerSamples(null));
	}

	@Test
	public void getDPCRContainerSamplesWithDeviceRunId3() {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenThrow(Exception.class);

		Assert.assertEquals(Collections.emptyList(), orderIntegrationService.getDPCRContainerSamples("RND12345"));
	}
	
	
	@Test
	public void searchByAccessioningIdTest() throws UnsupportedEncodingException, HMTPException {
	    String accessioningId ="8001";
        String url ="";
        Mockito.when(RestClientUtil.getUrlString("pas.omm_api_url", "", "/json/rest/api/v1/orders?accessioningID=",
                    accessioningId , null)).thenReturn(url );
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null)).thenReturn(ommClient);
        Mockito.when(ommClient.get(new GenericType<List<OrderDTO>>() {
            })).thenReturn(ordeDTOlist );
	    orderIntegrationService.searchByAccessioningId(accessioningId);
	}
	
	@Test
	public void getMandatoryFieldValidationsTest() throws HMTPException {
	    String url ="";
	    Mockito.when(RestClientUtil.getUrlString("pas.omm_api_url", "", "/json/rest/api/v1/order/runsamplemandateflags",
                    "", null)).thenReturn(url);
	    Mockito.when(RestClientUtil.postOrPutBuilder(url, null)).thenReturn(ommClient);
	    Mockito.when(RestClientUtil.postOrPutBuilder(url, null).post(Entity.entity(sampleResultsDTO, MediaType.APPLICATION_JSON))).thenReturn(response);
	    Mockito.when(response.readEntity(new GenericType<List<SampleResultsDTO>>() {
            })).thenReturn(sampleResultsDTO);
        orderIntegrationService.getMandatoryFieldValidations(sampleResultsDTO );
	}
	
	public OrderDTO getOrderDTO() {
	    OrderDTO order = new OrderDTO();
	    order.setAccessioningId("8001");
        return order;
	}
	
	public SampleResultsDTO getSampleResultsDTO() {
	    SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
	    sampleResultsDTO.setAccesssioningId("8001");
        return sampleResultsDTO;
	}

}
