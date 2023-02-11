package com.roche.connect.imm.testng;

import static org.testng.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.Instant;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.common.forte.SecondarySampleAssayDetails;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.imm.service.AssayIntegrationService;
import com.roche.connect.imm.service.ForteDetailImpl;
import com.roche.connect.imm.service.MessageProcessorService;
import com.roche.connect.imm.service.OrderIntegrationService;
import com.roche.connect.imm.service.RmmIntegrationService;


public class ForteMessageProcessTest {

	@InjectMocks
	ForteDetailImpl forteDetails = new ForteDetailImpl();


	@Mock
	private MessageProcessorService messageProcessorService;

	@Mock
	private AssayIntegrationService assayService;

	@Mock
	private OrderIntegrationService orderIntegrationService;

	@Mock
	private RmmIntegrationService rmmService;

	AssayDTO assayDTO = new AssayDTO();
	String accessioningId = "123456";

	@BeforeTest
	public void beforeTest() {
		assayDTO.setCollectionDate(Timestamp.from(Instant.now()));
		assayDTO.setEggDonor("Y");
		assayDTO.setFetusNumber("2");
		assayDTO.setGestationalAgeDays(2);
		assayDTO.setGestationalAgeWeeks(3);
		assayDTO.setIvfStatus("Status");
		assayDTO.setMaternalAge(22);
		assayDTO.setPatientAssayid(12345L);
		assayDTO.setReceivedDate(Timestamp.from(Instant.now()));

	}

	@Test
	private void getSampleAssayDetailsFromAssayDTOTest() {
		SecondarySampleAssayDetails actual = forteDetails.getSampleAssayDetailsFromAssayDto(accessioningId, assayDTO);

		assertEquals(actual.getAccessioningId(), accessioningId);
		assertEquals(actual.getEggDonor(), assayDTO.getEggDonor());
		assertEquals(actual.getFetusNumber(), assayDTO.getFetusNumber());
		assertEquals(actual.getGestationalAgeDays(), assayDTO.getGestationalAgeDays());
		assertEquals(actual.getGestationalAgeWeeks(), assayDTO.getGestationalAgeWeeks());
		assertEquals(actual.getMaternalAge(), assayDTO.getMaternalAge());
	}

}
