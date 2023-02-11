/*package com.roche.connect.omm.test;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.omm.ApplicationBoot;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.util.AES;
@SpringBootTest(classes = ApplicationBoot.class)
public class AESTest  extends AbstractTestNGSpringContextTests{
	@BeforeTest
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void encrypt() {
		PatientDTO patient = new PatientDTO();
		patient.setPatientLastName("patientLastName");
		patient.setPatientFirstName("patientFirstName");
		String encrypt = AES.encrypt("test");
		String decrypt = AES.decrypt(encrypt);
		Patient oar = (Patient) AES.getEncryptedObject(Patient.class.getName(), patient);
		PatientDTO dto = (PatientDTO) AES.getDecryptedObject(PatientDTO.class.getName(), oar);
		Assert.assertEquals(encrypt, "nYY8zAcnISBbYqWy/BoGgw==");
		Assert.assertEquals(decrypt, "test");
		Assert.assertNotNull(dto);

	}

	@Test
	public void getDecryptedObjectExceptionTest() {
		Order order = new Order();
		order.setAccessioningId("89808");
		AES.getDecryptedObject("test", order);
	}

	@Test
	public void getEncryptedObjectExeptionTest() {
		Order order = new Order();
		order.setAccessioningId("89808");
		AES.getEncryptedObject("test", order);
	}

	@Test(expectedExceptions = { NullPointerException.class })
	public void initializeAndUnproxyExeptionTest() {

		AES.initializeAndUnproxy(null);
	}

	@Test(expectedExceptions = { NullPointerException.class })
	public void getEncryptedObjectTest() {
		Order order = new Order();
		order.setAccessioningId("89808");
		AES.getEncryptedObject(Patient.class.getName(), order);
	}

	@Test(expectedExceptions = { NullPointerException.class })
	public void getDecryptedObjectTest() {
		Order order = new Order();
		order.setAccessioningId("89808");
		AES.getDecryptedObject(PatientDTO.class.getName(), order);
	}

}
*/