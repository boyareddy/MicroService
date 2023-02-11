package com.roche.connect.rmm.testNG;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import org.apache.commons.httpclient.HttpStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.rmm.dto.ProcessStepValuesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleProtocolDTO;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.RunResultsDetail;
import com.roche.connect.rmm.model.SampleProtocol;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.readrepository.SampleResultsReadRepository;
import com.roche.connect.rmm.rest.RunCrudRestApiImpl;
import com.roche.connect.rmm.util.MapEntityToDTO;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class GetProcessStepsResultsTest {

	@Mock
	SampleResultsReadRepository sampleResultsReadRepository = org.mockito.Mockito
			.mock(SampleResultsReadRepository.class);

	@InjectMocks
	RunCrudRestApiImpl runCrudRestApiImpl = new RunCrudRestApiImpl();

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	MapEntityToDTO mapEntityToDTO;

	ProcessStepValuesDTO processStepValuesDTO;

	String accessioningId;
	long domainId = 1L;
	List<Long> sampleResultids;
	Long sampleResultid = 2L;
	SampleResults sampleResults;
	List<ProcessStepValuesDTO> processStepActionValues;
	List<RunResultsDetailDTO> runResultsDetailDTOs;

	RunResults runResults;
	RunResultsDetail runResultsDetail;
	RunResultsDetailDTO runResultsDetailDTO;
	List<RunResultsDetail> runResultsDetails;

	List<SampleProtocolDTO> sampleprotocolDTOlist;
	List<RunResultsDetailDTO> runResultsDetailDTODTOlist;
	List<SampleProtocol> sampleProtocols;
	SampleProtocolDTO sampleProtocolDTO;
	SampleProtocol sampleProtocol;

	@BeforeTest
	public void setUp() {
		
		accessioningId = "acc-001";
          
		sampleResultids = new ArrayList<>();
		sampleResultids.add(15L);
		sampleResultids.add(18L);
		sampleResultids.add(19L);
		
		processStepValuesDTO = new ProcessStepValuesDTO();
		processStepValuesDTO.setAccesssioningId("acc-001");
		
		runResultsDetailDTO = new RunResultsDetailDTO();
		
		runResultsDetailDTO.setRunResultsDetailsId(34L);
		runResultsDetailDTO.setCreatedBy("Pentalya");
		
		runResultsDetailDTODTOlist = new ArrayList<>();
		
		runResultsDetailDTODTOlist.add(runResultsDetailDTO);
		
		processStepValuesDTO.setRunResultsDetailDTO(runResultsDetailDTODTOlist);
		
		runResultsDetailDTO = new RunResultsDetailDTO();
		runResultsDetailDTO.setRunResultsDetailsId(76L);

		runResultsDetail = new RunResultsDetail();
		runResultsDetails = new ArrayList<>();

		runResultsDetail.setEditedBy("df");
		runResultsDetail.setId(34L);
		runResultsDetails.add(runResultsDetail);
		sampleResults = new SampleResults();
		sampleResults.setAccesssioningId("acc-001");
		runResults = new RunResults();

		runResults.setId(56L);
		runResults.setRunResultsDetail(runResultsDetails);
		sampleResults.setRunResultsId(runResults);
		sampleResults.getRunResultsId().getRunResultsDetail();

		sampleProtocol = new SampleProtocol();
		sampleProtocol.setCreatedBy("");
		sampleProtocol.setProtocolName("");

		sampleProtocols = new ArrayList<>();
		sampleProtocols.add(sampleProtocol);

		sampleResults.setSampleProtocol(sampleProtocols);

		processStepActionValues = new ArrayList<>();
		runResultsDetailDTOs = new ArrayList<>();

		mapEntityToDTO = new MapEntityToDTO();
		sampleProtocolDTO = new SampleProtocolDTO();
		sampleProtocolDTO.setProtocolName("");
		sampleProtocolDTO.setCreatedBy("");
		
		sampleprotocolDTOlist = new ArrayList<>();
		sampleprotocolDTOlist.add(sampleProtocolDTO);

		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(sampleResultsReadRepository.findIdByAccesssioningId(any(String.class), any(long.class)))
				.thenReturn(sampleResultids);
	}

	@Test
	public void getProcessStepResultsPosTest() throws HMTPException {
		Mockito.when(sampleResultsReadRepository.findOne(any(long.class))).thenReturn(sampleResults);
		Mockito.when(mapEntityToDTO.mapsampleResultstoProcessDTO(sampleResults)).thenReturn(processStepValuesDTO);
		Mockito.when(mapEntityToDTO.convertSampleProtocolToSampleProtocolDTO(sampleProtocols)).thenReturn(sampleprotocolDTOlist);
		Response response = runCrudRestApiImpl.getProcessStepResults(accessioningId);
		assertEquals(response.getStatus(), HttpStatus.SC_OK);

	}  

	@Test
	public void getProcessStepResultsNegTest() throws HMTPException {
		Mockito.when(sampleResultsReadRepository.findOne(any(long.class))).thenReturn(sampleResults);
		Mockito.when(mapEntityToDTO.mapsampleResultstoProcessDTO(sampleResults)).thenReturn(processStepValuesDTO);
		Mockito.when(mapEntityToDTO.convertSampleProtocolToSampleProtocolDTO(sampleProtocols)).thenReturn(sampleprotocolDTOlist);
		Response response = runCrudRestApiImpl.getProcessStepResults(null);
		assertEquals(response.getStatus(), HttpStatus.SC_BAD_REQUEST);
	}
	
	@Test(expectedExceptions=HMTPException.class)
	public  void negativeTest() throws HMTPException {
		Mockito.when(sampleResultsReadRepository.findOne(any(long.class))).thenThrow(SQLException.class);
		Mockito.when(mapEntityToDTO.mapsampleResultstoProcessDTO(sampleResults)).thenReturn(processStepValuesDTO);
		Mockito.when(mapEntityToDTO.convertSampleProtocolToSampleProtocolDTO(sampleProtocols)).thenReturn(sampleprotocolDTOlist);
		runCrudRestApiImpl.getProcessStepResults(accessioningId);
		
	}

	

}
