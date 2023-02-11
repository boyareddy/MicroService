package com.roche.connect.imm.testng;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mockito.InjectMocks;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.constant.StatusMessage;
import com.roche.connect.common.lp24.ContainerInfo;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.imm.service.LP24MessageService;
import com.roche.connect.imm.utils.JsonFileReaderAsString;

public class LP24MessageValidationTest {

	public static final String LP24_SSU_MESSAGE = "src/test/java/resource/lp24SSUMessage.json";

	@InjectMocks
	private LP24MessageService lp24MessageService = new LP24MessageService();

	private Class<? extends LP24MessageService> lp24MessageServiceClass = null;

	private Method isValidLP24InputContainerIdMethod = null;

	private Method isValidMP96OutputContainerIdMethod = null;

	private Method isValidSpecimenStatusUpdateMessageMethod;
	
	private SpecimenStatusUpdateMessage specimenStatusUpdateMessage = null;


	@BeforeMethod
	public void beforeMethod() throws IOException, NoSuchMethodException, SecurityException {

		lp24MessageServiceClass = lp24MessageService.getClass();

		isValidLP24InputContainerIdMethod = lp24MessageServiceClass.getDeclaredMethod("isValidLP24InputContainerId",
				String.class);
		isValidLP24InputContainerIdMethod.setAccessible(true);

		isValidMP96OutputContainerIdMethod = lp24MessageServiceClass.getDeclaredMethod("isValidMP96OutputContainerId",
				String.class);
		isValidMP96OutputContainerIdMethod.setAccessible(true);
		
		isValidSpecimenStatusUpdateMessageMethod = lp24MessageServiceClass.getDeclaredMethod("isValidSpecimenStatusUpdateMessage",
				SpecimenStatusUpdateMessage.class);
		isValidSpecimenStatusUpdateMessageMethod.setAccessible(true);

		String jsonContent4 = JsonFileReaderAsString.getJsonfromFile(LP24_SSU_MESSAGE);
		specimenStatusUpdateMessage = new ObjectMapper().readValue(jsonContent4, SpecimenStatusUpdateMessage.class);

	}

	@Test
	public void testLP24InputContainerId1()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertFalse((boolean) isValidLP24InputContainerIdMethod.invoke(lp24MessageService, ""));
	}

	@Test
	public void testLP24InputContainerId2()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertFalse((boolean) isValidLP24InputContainerIdMethod.invoke(lp24MessageService, "6e75e9b8"));
	}

	@Test
	public void testLP24InputContainerId3()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertFalse((boolean) isValidLP24InputContainerIdMethod.invoke(lp24MessageService, "6e75e9b9_ "));
	}

	@Test
	public void testLP24InputContainerId4()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertFalse((boolean) isValidLP24InputContainerIdMethod.invoke(lp24MessageService, "_A1"));
	}

	@Test
	public void testLP24InputContainerId5()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertFalse((boolean) isValidLP24InputContainerIdMethod.invoke(lp24MessageService, "_"));
	}

	@Test
	public void testLP24InputContainerId6()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertTrue((boolean) isValidLP24InputContainerIdMethod.invoke(lp24MessageService, "6e75e9b8_A1"));
	}

	@Test
	public void testMP96OutputContainerId1()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertFalse((boolean) isValidMP96OutputContainerIdMethod.invoke(lp24MessageService, ""));
	}

	@Test
	public void testMP96OutputContainerId2()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertFalse((boolean) isValidMP96OutputContainerIdMethod.invoke(lp24MessageService, "ab87d02b_"));
	}

	@Test
	public void testMP96OutputContainerId3()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertFalse((boolean) isValidMP96OutputContainerIdMethod.invoke(lp24MessageService, "ab87d02b_ "));
	}

	@Test
	public void testMP96OutputContainerId4()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertFalse((boolean) isValidMP96OutputContainerIdMethod.invoke(lp24MessageService, "_A1"));
	}

	@Test
	public void testMP96OutputContainerId5()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertFalse((boolean) isValidMP96OutputContainerIdMethod.invoke(lp24MessageService, "_"));
	}

	@Test
	public void testMP96OutputContainerId6()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertFalse((boolean) isValidMP96OutputContainerIdMethod.invoke(lp24MessageService, "ab87d02b_A13"));
	}

	@Test
	public void testMP96OutputContainerId7()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Assert.assertTrue((boolean) isValidMP96OutputContainerIdMethod.invoke(lp24MessageService, "ab87d02b_A1"));
	}

	@Test
	public void testSpecimenStatusUpdateMessage() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		 
		Assert.assertTrue((boolean)isValidSpecimenStatusUpdateMessageMethod.invoke(lp24MessageService, specimenStatusUpdateMessage));
	}
	
	@Test
	public void testSpecimenStatusUpdateMessage2() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		 
		specimenStatusUpdateMessage.getStatusUpdate().setRunResult("");
		Assert.assertFalse((boolean)isValidSpecimenStatusUpdateMessageMethod.invoke(lp24MessageService, specimenStatusUpdateMessage));
	}
	
	@Test
	public void testSpecimenStatusUpdateMessage3() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		 
		specimenStatusUpdateMessage.getStatusUpdate().setOrderName("");
		Assert.assertFalse((boolean)isValidSpecimenStatusUpdateMessageMethod.invoke(lp24MessageService, specimenStatusUpdateMessage));
	}
	
	@Test
	public void testSpecimenStatusUpdateMessage4() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		 
		specimenStatusUpdateMessage.getStatusUpdate().setOrderResult("");
		Assert.assertFalse((boolean)isValidSpecimenStatusUpdateMessageMethod.invoke(lp24MessageService, specimenStatusUpdateMessage));
	}
	
	@Test
	public void testSpecimenStatusUpdateMessage5() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		 
		specimenStatusUpdateMessage.setContainerId("ab87d02b_A13");
		Assert.assertFalse((boolean)isValidSpecimenStatusUpdateMessageMethod.invoke(lp24MessageService, specimenStatusUpdateMessage));
	}

	@Test
	public void testSpecimenStatusUpdateMessage6() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		specimenStatusUpdateMessage.getStatusUpdate().setOrderResult(StatusMessage.LP24_STATUS_MESSAGE_FAILED);
		Assert.assertTrue((boolean)isValidSpecimenStatusUpdateMessageMethod.invoke(lp24MessageService, specimenStatusUpdateMessage));
	}
	
	@Test
	public void testSpecimenStatusUpdateMessage7() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ContainerInfo containerInfo = specimenStatusUpdateMessage.getStatusUpdate().getContainerInfo();
		containerInfo.setCarrierBarcode("");
		Assert.assertFalse((boolean)isValidSpecimenStatusUpdateMessageMethod.invoke(lp24MessageService, specimenStatusUpdateMessage));
	}
	
	@Test
	public void testSpecimenStatusUpdateMessage8() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ContainerInfo containerInfo = specimenStatusUpdateMessage.getStatusUpdate().getContainerInfo();
		containerInfo.setCarrierPosition("");
		Assert.assertFalse((boolean)isValidSpecimenStatusUpdateMessageMethod.invoke(lp24MessageService, specimenStatusUpdateMessage));
	}
	
	@Test
	public void testSpecimenStatusUpdateMessage9() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ContainerInfo containerInfo = specimenStatusUpdateMessage.getStatusUpdate().getContainerInfo();
		containerInfo.setCarrierType("");
		Assert.assertFalse((boolean)isValidSpecimenStatusUpdateMessageMethod.invoke(lp24MessageService, specimenStatusUpdateMessage));
	}
}
