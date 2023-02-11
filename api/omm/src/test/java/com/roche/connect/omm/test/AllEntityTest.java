package com.roche.connect.omm.test;

import static org.testng.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.server.entity.Company;
import com.roche.connect.omm.model.ContainerSamples;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.model.TestOptions;

public class AllEntityTest {
	Patient patient = null;
	PatientSamples patientSamples = null;
	TestOptions testOptions = null;
	PatientAssay patientAssay = null;
	Timestamp ts = null;
	Order order = null;
	List<Order> orderList = null;
	ContainerSamples containerSamples = null;
	String testId = null;
	Company company;

	@BeforeTest
	public void setUp() {
		patient = new Patient();
		patientSamples = new PatientSamples();
		patientAssay = new PatientAssay();
		testOptions = new TestOptions();
		order = new Order();
		orderList = new ArrayList<Order>();
		ts = new Timestamp(new Date().getTime());
		containerSamples = new ContainerSamples();
		testId = "343";
		company = new Company();
	}

	@Test
	public void testSetCompany() {
		Company company = new Company();
		company.setDomainName("hcl.com");
		patientSamples.setCompany(company);
		testOptions.setCompany(company);
		order.setCompany(company);

		patient.setCompany(company);
		patient.setId(1);
	
		patient.setOtherClinicianName("dental");
		patient.setClinicName("dental");
		
		patient.setRefClinicianName("ram");
		
		patient.setPatientLastName("sss");

		patient.setCreatedABy("");
		patient.setEditedBy("");
		patient.setCreatedDate(ts);
		patient.setModifiedDate(ts);

		patientAssay.setCompany(company);

		containerSamples.setId(123);
		containerSamples.setDeviceRunID("121");
		containerSamples.setUpdatedDateTime(ts);
		containerSamples.setUpdatedBy("test");
		containerSamples.setContainerID("1");
		containerSamples.setContainerType("96-well plate");
		containerSamples.setPosition("1");
		containerSamples.setAccessioningID("12345");
		containerSamples.setActiveFlag("Y");
		containerSamples.setStatus("open");
		containerSamples.setLoadID(123333l);
		containerSamples.setDeviceID("1245");
		containerSamples.setAssayType("NIPT");
		containerSamples.setCreatedBy("admin");

		containerSamples.setCreatedBy("admin");
		containerSamples.setCreatedABy("");
		containerSamples.setEditedBy("");
		containerSamples.setModifiedDate(ts);
		containerSamples.setCreatedDate(ts);

		containerSamples.setCompany(company);

		assertEquals(containerSamples.getCreatedABy(), containerSamples.getCreatedABy());
		assertEquals(containerSamples.getEditedBy(), containerSamples.getEditedBy());
		assertEquals(containerSamples.getModifiedDate(), containerSamples.getModifiedDate());
		assertEquals(containerSamples.getCreatedBy(), containerSamples.getCreatedBy());
		assertEquals(containerSamples.getCreatedDate(), containerSamples.getCreatedDate());

		assertEquals(containerSamples.getId(), 123);
		assertEquals(containerSamples.getDeviceRunID(), "121");
		assertEquals(containerSamples.getCompany().getDomainName(), "hcl.com");
		assertEquals(containerSamples.getUpdatedDateTime(), containerSamples.getUpdatedDateTime());
		assertEquals(containerSamples.getUpdatedBy(), "test");
		assertEquals(containerSamples.getContainerID(), "1");
		assertEquals(containerSamples.getPosition(), "1");
		assertEquals(containerSamples.getContainerType(), "96-well plate");
		assertEquals(containerSamples.getAccessioningID(), "12345");
		assertEquals(containerSamples.getActiveFlag(), "Y");
		assertEquals(containerSamples.getLoadID(), new Long(123333));
		assertEquals(containerSamples.getAssayType(), "NIPT");
		assertEquals(containerSamples.getCreatedDateTime(), containerSamples.getCreatedDateTime());
		assertEquals(containerSamples.getOwnerPropertyName(), "company");
		assertEquals(patient.getOwnerPropertyName(), "company");
		assertEquals(patientAssay.getOwnerPropertyName(), "company");
		assertEquals(patientSamples.getOwnerPropertyName(), "company");
		assertEquals(order.getOwnerPropertyName(), "company");
		assertEquals(testOptions.getOwnerPropertyName(), "company");
		assertEquals(order.getCompany().getDomainName(), "hcl.com");
		assertEquals(containerSamples.getDeviceID(), "1245");
		assertEquals(containerSamples.getStatus(), "open");
		assertEquals(containerSamples.getCreatedBy(), "admin");

		assertEquals(patientSamples.getCompany().getDomainName(), "hcl.com");
		assertEquals(testOptions.getCompany().getDomainName(), "hcl.com");
		assertEquals(patient.getCompany().getDomainName(), "hcl.com");
		assertEquals(patientAssay.getCompany().getDomainName(), "hcl.com");
		assertEquals(patient.getId(), 1);
		
		assertEquals(patient.getOtherClinicianName(), "dental");
		assertEquals(patient.getClinicName(), "dental");
		
		assertEquals(patient.getRefClinicianName(), "ram");
		
		assertEquals(patient.getPatientLastName(), "sss");

		assertEquals(patient.getCreatedABy(), patient.getCreatedABy());
		assertEquals(patient.getEditedBy(), patient.getEditedBy());
		assertEquals(patient.getCreatedDate(), patient.getCreatedDate());
		assertEquals(patient.getModifiedDate(), patient.getModifiedDate());
	}

	@Test
	public void testSetUpdatedDateTime() {
		patientSamples.setUpdatedDateTime(ts);
		patientAssay.setUpdatedDateTime(ts);
		patient.setUpdatedDateTime(ts);
		patient.setCreatedDateTime(ts);
		patientSamples.setCreatedDateTime(ts);
		patientAssay.setCreatedDateTime(ts);

		patientSamples.setCreatedBy("admin");
		patientSamples.setCreatedABy("");
		patientSamples.setEditedBy("");
		patientSamples.setModifiedDate(ts);
		patientSamples.setCreatedDate(ts);

		patientAssay.setCreatedABy("");
		patientAssay.setEditedBy("");
		patientAssay.setModifiedDate(ts);
		patientAssay.setCreatedBy("admin");
		patient.setCreatedBy("admin");
		patient.setActiveFlag("Y");
		patientAssay.setActiveFlag("Y");
		patientAssay.setPatient(patient);

		assertEquals(patientSamples.getCreatedABy(), patientSamples.getCreatedABy());
		assertEquals(patientSamples.getEditedBy(), patientSamples.getEditedBy());
		assertEquals(patientSamples.getModifiedDate(), patientSamples.getModifiedDate());
		assertEquals(patientSamples.getCreatedDate(), patientSamples.getCreatedDate());

		assertEquals(patientAssay.getCreatedABy(), patientAssay.getCreatedABy());
		assertEquals(patientAssay.getEditedBy(), patientAssay.getEditedBy());
		assertEquals(patientAssay.getModifiedDate(), patientAssay.getModifiedDate());
		assertEquals(patientAssay.getCreatedBy(), patientAssay.getCreatedBy());

		assertEquals(patientSamples.getUpdatedDateTime(), patientSamples.getUpdatedDateTime());
		assertEquals(patient.getUpdatedDateTime(), patient.getUpdatedDateTime());
		assertEquals(patientAssay.getUpdatedDateTime(), patientAssay.getUpdatedDateTime());
		assertEquals(patientSamples.getCreatedDateTime(), patientSamples.getCreatedDateTime());
		assertEquals(patientAssay.getCreatedDateTime(), patientAssay.getCreatedDateTime());
		assertEquals(patient.getCreatedDateTime(), patient.getCreatedDateTime());
		assertEquals(patientSamples.getCreatedBy(), "admin");
		assertEquals(patientAssay.getCreatedBy(), "admin");
		assertEquals(patient.getCreatedBy(), "admin");
		assertEquals(patient.getActiveFlag(), "Y");
		assertEquals(patientAssay.getActiveFlag(), "Y");
		assertEquals(patientAssay.getPatient().getActiveFlag(), "Y");
		assertEquals(patientAssay.getPatient(), patient);
		assertEquals(patientSamples.getPatient(), patient);

	}

	@Test
	public void testSetUpdatedBy() {
		patientSamples.setUpdatedBy("test");
		patientSamples.setCreatedBy("admin");
		patientSamples.setCreatedABy("");
		patientSamples.setEditedBy("");
		patientSamples.setModifiedDate(ts);

		patient.setUpdatedBy("test");
		patient.addPatientSample(patientSamples);
		patient.deletePatientSample(patientSamples);
		patientAssay.setUpdatedBy("test");

		patientAssay.setCreatedABy("");
		patientAssay.setEditedBy("");
		patientAssay.setModifiedDate(ts);
		patientAssay.setCreatedBy("admin");
		patientAssay.setCreatedDate(ts);

		assertEquals(patientAssay.getCreatedABy(), patientAssay.getCreatedABy());
		assertEquals(patientAssay.getEditedBy(), patientAssay.getEditedBy());
		assertEquals(patientAssay.getModifiedDate(), patientAssay.getModifiedDate());
		assertEquals(patientAssay.getCreatedBy(), patientAssay.getCreatedBy());
		assertEquals(patientAssay.getCreatedDate(), patientAssay.getCreatedDate());

		assertEquals(patientSamples.getCreatedABy(), patientSamples.getCreatedABy());
		assertEquals(patientSamples.getEditedBy(), patientSamples.getEditedBy());
		assertEquals(patientSamples.getModifiedDate(), patientSamples.getModifiedDate());
		assertEquals(patientSamples.getCreatedBy(), "admin");
		assertEquals(patientSamples.getUpdatedBy(), "test");
		assertEquals(patient.getUpdatedBy(), "test");
		assertEquals(patientAssay.getUpdatedBy(), "test");

	}

	@Test
	public void testSetSampleId() {
		patientSamples.setSampleId("1");

		patientSamples.setCreatedBy("admin");
		patientSamples.setCreatedABy("");
		patientSamples.setEditedBy("");
		patientSamples.setModifiedDate(ts);

		assertEquals(patientSamples.getCreatedABy(), patientSamples.getCreatedABy());
		assertEquals(patientSamples.getEditedBy(), patientSamples.getEditedBy());
		assertEquals(patientSamples.getModifiedDate(), patientSamples.getModifiedDate());
		assertEquals(patientSamples.getCreatedBy(), "admin");
		assertEquals(patientSamples.getSampleId(), "1");
	}

	@Test
	public void testSetTestOptions() {
		testOptions.setId(1);
		testOptions.setUpdatedDateTime(ts);
		testOptions.setCreatedDateTime(ts);
		testOptions.setUpdatedBy("admin");
		testOptions.setCreatedBy("admin");

		testOptions.setCreatedABy("Penta");
		testOptions.setEditedBy("");
		testOptions.setCreatedDate(ts);
		testOptions.setModifiedDate(ts);
		testOptions.setTestId(testId);
		testOptions.setCompany(company);
		testOptions.setActiveFlag("true");

		order.setAccessioningId("111");

		order.setCreatedABy("");
		order.setEditedBy("");
		order.setCreatedDate(ts);
		order.setModifiedDate(ts);
		order.setPriorityUpdatedTime(ts);
		order.setPriority("high");

		assertEquals(order.getCreatedABy(), order.getCreatedABy());
		assertEquals(order.getEditedBy(), order.getEditedBy());
		assertEquals(order.getCreatedDate(), order.getCreatedDate());
		assertEquals(order.getModifiedDate(), order.getModifiedDate());
		assertEquals(order.getPriorityUpdatedTime(), order.getPriorityUpdatedTime());

		orderList.add(order);
		testOptions.setOrder(order);
		patient.setOrder(orderList);
		assertEquals(testOptions.getId(), 1);
		assertEquals(testOptions.getUpdatedDateTime(), testOptions.getUpdatedDateTime());
		assertEquals(testOptions.getCreatedDateTime(), testOptions.getCreatedDateTime());
		assertEquals(testOptions.getUpdatedBy(), "admin");
		assertEquals(testOptions.getCreatedBy(), "admin");

		assertEquals(testOptions.getCreatedABy(), "Penta");
		assertEquals(testOptions.getEditedBy(), "");
		assertEquals(testOptions.getCreatedDate(), ts);
		assertEquals(testOptions.getModifiedDate(), ts);
		assertEquals(testOptions.getTestId(), "343");
		assertEquals(testOptions.getActiveFlag(), "true");
		assertEquals(testOptions.getCompany(), company);

		assertEquals(testOptions.getOrder().getAccessioningId(), "111");
		assertEquals(patient.getOrder().iterator().next().getAccessioningId(), "111");
	}

	
	 /** @Test public void testSetSampleId() { PatientSamples patientSamples = new
	 * PatientSamples(); patientSamples.setSampleId("1");
	 * assertEquals(patientSamples.getSampleId(), "1"); }
	 * 
	 * @Test public void testSetSampleId() { PatientSamples patientSamples = new
	 * PatientSamples(); patientSamples.setSampleId("1");
	 * assertEquals(patientSamples.getSampleId(), "1"); }
	 */

	@AfterTest
	public void afterTest() {

	}
}