package com.roche.connect.imm.testng;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.imm.ApplicationBootIMM;
import com.roche.connect.imm.service.MP24AsyncMessageService;
import com.roche.connect.imm.service.WFMIntegrationService;

@SpringBootTest(classes = ApplicationBootIMM.class)
public class MP24AsyncMessageServiceTest {

	@InjectMocks
	MP24AsyncMessageService mp24AsyncMessageService = null;

	@Mock
	WFMIntegrationService wfmIntegrationService;

	@BeforeTest
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(wfmIntegrationService).updateRunResultStatusByDeviceId(Mockito.anyString(),
				Mockito.anyString(),Mockito.anyString());
	}

	@Test(priority = 1)
	public void updateRunResultByDeviceIdTest() {
		mp24AsyncMessageService.updateRunResultByDeviceId(Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
	}
}
