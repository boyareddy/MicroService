package com.roche.connect.omm.test;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.JsonFileReaderAsString;
import com.roche.connect.omm.util.ValidationUtil;

@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class CreateOrderExeptionTest {
	
	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApi;
	@Mock HMTPLoggerImpl hmtpLoggerImpl;
	@Mock OrderServiceImpl orderServiceImpl;
	@Mock ObjectMapper objectmapper;
	
	int incorrectstatusCode=400;
	ObjectMapper objectMapper = new ObjectMapper();
	@Test
	public void createTest() throws IOException, HMTPException{
	MockitoAnnotations.initMocks(this);	
	String	jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudCommon.json");	
	OrderParentDTO 	orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
	orderParentNew.getOrder().getAssay().setCollectionDate(null);
	String json=objectMapper.writeValueAsString(orderParentNew);
	Response response=orderCrudRestApi.createOrder(json);
	Assert.assertEquals(response.getStatus(), incorrectstatusCode);
	}
	@Test
	public void createFirstNameTest() throws IOException, HMTPException{
	MockitoAnnotations.initMocks(this);	
	String	jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudCommon.json");	
	OrderParentDTO 	orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
	PatientDTO patientDto=new PatientDTO();
	patientDto.setPatientFirstName("121212");
	orderParentNew.getOrder().setPatient(patientDto);
	String json=objectMapper.writeValueAsString(orderParentNew);
	
	Response response=orderCrudRestApi.createOrder(json);
	Assert.assertEquals(response.getStatus(), incorrectstatusCode);
	}
	@Test
	public void createLastNameTest() throws IOException, HMTPException{
	MockitoAnnotations.initMocks(this);	
	String	jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudCommon.json");	
	OrderParentDTO 	orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
	PatientDTO patientDto=new PatientDTO();
	patientDto.setPatientFirstName("Choudhary");
	patientDto.setPatientLastName("12121");
	orderParentNew.getOrder().setPatient(patientDto);
	String json=objectMapper.writeValueAsString(orderParentNew);
	Response response=orderCrudRestApi.createOrder(json);
	Assert.assertEquals(response.getStatus(), incorrectstatusCode);
	}
	@Test
	public void createOtherClinicianNameTest() throws IOException, HMTPException{
	MockitoAnnotations.initMocks(this);	
	String	jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudCommon.json");	
	OrderParentDTO 	orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
	PatientDTO patientDto=new PatientDTO();
	patientDto.setPatientFirstName("Choudhary");
	patientDto.setPatientLastName("Sophia");
	patientDto.setOtherClinicianName("123123");
	orderParentNew.getOrder().setPatient(patientDto);
	String json=objectMapper.writeValueAsString(orderParentNew);
	Response response=orderCrudRestApi.createOrder(json);
	Assert.assertEquals(response.getStatus(), incorrectstatusCode);
	}
	
	@Test
	public void createRefClinicianClinicNameTest() throws IOException, HMTPException{
	MockitoAnnotations.initMocks(this);	
	String	jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudCommon.json");	
	OrderParentDTO 	orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
	PatientDTO patientDto=new PatientDTO();
	patientDto.setPatientFirstName("Choudhary");
	patientDto.setPatientLastName("Sophia");
	patientDto.setOtherClinicianName("roche");
	patientDto.setClinicName("121231");
	orderParentNew.getOrder().setPatient(patientDto);
	String json=objectMapper.writeValueAsString(orderParentNew);
	Response response=orderCrudRestApi.createOrder(json);
	Assert.assertEquals(response.getStatus(), incorrectstatusCode);
	}
	@Test
	public void createRefClinicianNameTest() throws IOException, HMTPException{
	MockitoAnnotations.initMocks(this);	
	String	jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudCommon.json");	
	OrderParentDTO 	orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
	PatientDTO patientDto=new PatientDTO();
	patientDto.setPatientFirstName("Choudhary");
	patientDto.setPatientLastName("Sophia");
	patientDto.setOtherClinicianName("roche");
	patientDto.setRefClinicianName("123123");
	orderParentNew.getOrder().setPatient(patientDto);
	String json=objectMapper.writeValueAsString(orderParentNew);
	Response response=orderCrudRestApi.createOrder(json);
	Assert.assertEquals(response.getStatus(), incorrectstatusCode);
	}
	/*@Test
	public void createTreatingDoctorNameTest() throws IOException, HMTPException{
	MockitoAnnotations.initMocks(this);	
	String	jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudCommon.json");	
	OrderParentDTO 	orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
	PatientDTO patientDto=new PatientDTO();
	patientDto.setPatientFirstName("Choudhary");
	patientDto.setPatientLastName("Sophia");
	patientDto.setOtherClinicianName("roche");
	patientDto.setRefClinicianName("roche");
	orderParentNew.getOrder().setPatient(patientDto);
	String json=objectMapper.writeValueAsString(orderParentNew);
	Response response=orderCrudRestApi.createOrder(json);
	
	Assert.assertEquals(response.getStatus(), incorrectstatusCode);
	}*/
	public static void main(String[] args) {
		ValidationUtil util=new ValidationUtil();
	System.out.println(	util.isValidName(""));
	}
	
}
