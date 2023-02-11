package com.roche.connect.imm.testng;

import java.io.IOException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.htp.status.HtpStatus;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.imm.model.MessageStore;
import com.roche.connect.imm.service.MessageService;
import com.roche.connect.imm.writerepository.MessageStoreWriteRepository;

@PrepareForTest({ RestClientUtil.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class MessageServiceTest {

	@InjectMocks
	private MessageService messageService = new MessageService();

	@Mock
	private MessageStoreWriteRepository messageStoreWriteRepository = Mockito.mock(MessageStoreWriteRepository.class);

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@BeforeMethod
	public void beforeMethod() throws IOException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);

	}

	@Test
	public void saveMessage() throws IOException {
		MessageStore msg = new MessageStore();
		Mockito.when(messageStoreWriteRepository.save(msg)).thenReturn(msg);
		messageService.saveMessage(getResponseMessage());
	}

	@Test
	public void runResultDTO() throws IOException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		MessageStore msg = new MessageStore();
		Mockito.when(messageStoreWriteRepository.save(msg)).thenReturn(msg);
		messageService.saveMessage(getRunResultsDTO());
	}

	@Test
	public void htpStatus() throws IOException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		MessageStore msg = new MessageStore();
		Mockito.when(messageStoreWriteRepository.save(msg)).thenReturn(msg);
		messageService.saveMessage(getHTPStatus());
	}

	@Test
	public void messageStore() throws IOException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		MessageStore msg = new MessageStore();
		Mockito.when(messageStoreWriteRepository.save(msg)).thenReturn(msg);
		messageService.saveMessage(getResponseMessage(), "ABCDF", "123ADFD");
	}

	public ResponseMessage getResponseMessage() {

		ResponseMessage responseMessage = new ResponseMessage();

		responseMessage.setMessageControlId("ABCSDD");
		responseMessage.setDeviceSerialNumber("1234");
		responseMessage.setMessageType(EnumMessageType.ResponseMessage);

		return responseMessage;

	}

	public RunResultsDTO getRunResultsDTO() {

		RunResultsDTO runResultDTO = new RunResultsDTO();

		runResultDTO.setDeviceId("ABC");

		return runResultDTO;

	}

	public HtpStatus getHTPStatus() {

		HtpStatus htpStatus = new HtpStatus();

		htpStatus.setDeviceId("ABC");

		return htpStatus;

	}

}
