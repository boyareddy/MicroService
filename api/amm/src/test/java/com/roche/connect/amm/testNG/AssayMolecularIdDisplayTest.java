 /*******************************************************************************
 *  * File Name: AssayMolecularIdDisplayTest.java            
 *  * Version:  1.0
 *  * 
 *  * Authors: Dasari Ravindra
 *  * 
 *  * =========================================
 *  * 
 *  * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  * All Rights Reserved.
 *  * 
 *  * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
 *  * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  * executed Confidentiality and Non-disclosure agreements explicitly covering such access
 *  * 
 *  * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 *  * 
 *  * =========================================
 *  * 
 *  * ChangeLog:
 *  ******************************************************************************/
package com.roche.connect.amm.testNG;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.amm.model.AssayType;
import com.roche.connect.amm.model.MolecularIDType;
import com.roche.connect.amm.repository.MolecularTypeReadRepository;
import com.roche.connect.amm.rest.AssayCrudRestApiImpl;

/**
 * The Class AssayMolecularIdDisplayTest.
 */
@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class AssayMolecularIdDisplayTest {

	/** The assay impl. */
	@InjectMocks
	AssayCrudRestApiImpl assayImpl = new AssayCrudRestApiImpl();

	/** The molecular type read repository. */
	@Mock
	MolecularTypeReadRepository molecularTypeReadRepository = org.mockito.Mockito
			.mock(MolecularTypeReadRepository.class);
	
	@Mock HMTPLoggerImpl hmtpLoggerImpl;

	/** The id. */
	String assayType ;
	
	/** The plate type. */
	String plateType = null;
	
	/** The plate location. */
	String plateLocation = null;
	
	/** The expected correct code. */
	int expectedCorrectCode = 0;
	
	/** The expected incorrect code. */
	int expectedIncorrectCode = 0;

	/**
	 * Before test.
	 */
	@BeforeTest
	public void beforeTest() {
		assayType = "NIPTHTP";
		plateType = "A";
		plateLocation = "A1";
		expectedCorrectCode = 200;
		expectedIncorrectCode = 400;

		List<MolecularIDType> molecularIdTypes = new ArrayList<MolecularIDType>();
		MolecularIDType molType = new MolecularIDType();
		molType.setId(1);
		molType.setActiveFlag("Y");
		molType.setMolecularId("MID1");
		molType.setPlateLocation("A1");
		molType.setPlateType("A");
		molType.setAssayType("NIPTHTP");
		AssayType assay = new AssayType();
		assay.setId(1);
		molType.setAssay(assay);
		molecularIdTypes.add(molType);
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		Mockito.when(molecularTypeReadRepository.findMolecularIds(Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(molecularIdTypes);
		Mockito.when(molecularTypeReadRepository.findMolecularIDTypeByPlateTypeAndPlateLocation(Mockito.any(String.class),Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(molecularIdTypes);
		Mockito.when(molecularTypeReadRepository.findMolecularIDTypeByIdAndPlateType(Mockito.any(String.class), Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(molecularIdTypes);
		Mockito.when(molecularTypeReadRepository.findMolecularIDTypeByIdAndPlateLocation(Mockito.any(String.class), Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(molecularIdTypes);

	}

	/**
	 * Gets the molecualr id display pos test.
	 *
	 * @return the molecualr id display pos test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getMolecualrIdDisplayPosTest() throws HMTPException {
		Response response = assayImpl.getMolecularIdData(assayType, plateType, plateLocation);
		assertEquals(response.getStatus(), expectedCorrectCode);
	}

	/**
	 * Gets the molecualr id by plate type and plate location pos test.
	 *
	 * @return the molecualr id by plate type and plate location pos test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getMolecualrIdByPlateTypeAndPlateLocationPosTest() throws HMTPException {
		Response response = assayImpl.getMolecularIdData(null, plateType, plateLocation);
		assertEquals(response.getStatus(), expectedCorrectCode);
	}

	/**
	 * Gets the molecualr id by id and plate type pos test.
	 *
	 * @return the molecualr id by id and plate type pos test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getMolecualrIdByIdAndPlateTypePosTest() throws HMTPException {
		Response response = assayImpl.getMolecularIdData(assayType, plateType, null);
		assertEquals(response.getStatus(), expectedCorrectCode);
	}

	/**
	 * Gets the molecualr id by id and plate location pos test.
	 *
	 * @return the molecualr id by id and plate location pos test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getMolecualrIdByIdAndPlateLocationPosTest() throws HMTPException {
		Response response = assayImpl.getMolecularIdData(assayType, null, plateLocation);
		assertNotEquals(response.getStatus(), expectedIncorrectCode);
	}

	/**
	 * Gets the molecualr id display neg test.
	 *
	 * @return the molecualr id display neg test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getMolecualrIdDisplayNegTest() throws HMTPException {
		Response response = assayImpl.getMolecularIdData(assayType, plateType, plateLocation);
		assertNotEquals(response.getStatus(), expectedIncorrectCode);
	}
	
	/**
     * Get MolecualrId By AssayType id pos test.
     *
     * @return the MolecularIDType
     * @throws HMTPException
     *             the HMTP exception
     */
    @Test
    public void getMolecualrIdByAssayTypePosTest() throws HMTPException {
        Response response = assayImpl.getMolecularIdData(assayType, null, null);
        assertEquals(response.getStatus(), expectedCorrectCode);
    }
    
    
    /**
     * Get MolecualrId By FindAll test.
     *
     * @return the molecularIdTypes
     * @throws HMTPException
     *             the HMTP exception
     */
    @Test
    public void getMolecualrIdByFindAllTest() throws HMTPException {
        Response response = assayImpl.getMolecularIdData(null, null, null);
        assertEquals(response.getStatus(), expectedCorrectCode);
    }

	/**
	 * After test.
	 */
	@AfterTest
	public void afterTest() {
		assayType = null;
		plateType = null;
		expectedCorrectCode = 0;
		expectedIncorrectCode = 0;
	}

}
