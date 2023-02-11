 /*******************************************************************************
 *  * File Name: ExceptionCaseTest.java            
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



import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.amm.rest.AssayCrudRestApiImpl;

/**
 * The Class ExceptionCaseTest.
 */
@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class ExceptionCaseTest {
	
	/** The sample type read repository. */
	/*@Mock
	SampleTypeReadRepository sampleTypeReadRepository = org.mockito.Mockito.mock(SampleTypeReadRepository.class);*/
	
	@Mock HMTPLoggerImpl hmtpLoggerImpl;
	
	/** The assay impl. */
	@InjectMocks
	AssayCrudRestApiImpl assayImpl = new AssayCrudRestApiImpl();
	

	/** The assay id. */
	String assayType ;
	
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
		expectedCorrectCode = 200;
		expectedIncorrectCode = 400;
		MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
	}
	
	
	/**
	 * Sample type catch block test.
	 *
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test(expectedExceptions = HMTPException.class)
	public void sampleTypeCatchBlockTest() throws HMTPException {
		assayImpl.getSampleTypesData(assayType);
			
	}
	
	/**
	 * Assay type catch block test.
	 *
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test(expectedExceptions = HMTPException.class)
	public void assayTypeCatchBlockTest() throws HMTPException {
		assayImpl.getAssayTypesData(assayType);	
		
	}
	
	/**
	 * Test options catch block test.
	 *
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test(expectedExceptions = HMTPException.class)
	public void testOptionsCatchBlockTest() throws HMTPException {
		assayImpl.getTestOptionsData(assayType);
		
	}
	
	/**
	 * Device test options catch block test.
	 *
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test(expectedExceptions = HMTPException.class)
	public void deviceTestOptionsCatchBlockTest() throws HMTPException {
		assayImpl.getDeviceTestOptionsData(assayType,"MP24",null);
		
	}
	
	/**
	 * Workflow process step catch block test.
	 *
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test(expectedExceptions = HMTPException.class)
	public void workflowProcessStepCatchBlockTest() throws HMTPException {
		assayImpl.getWFProcessData(assayType,"MP24");
		
	}
	
	/**
	 * List data catch block test.
	 *
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test(expectedExceptions = HMTPException.class)
	public void listDataCatchBlockTest() throws HMTPException {
		assayImpl.getListData(assayType,"ivf status");
		
	}
	
	/**
	 * Input data validations catch block test.
	 *
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test(expectedExceptions = HMTPException.class)
	public void inputDataValidationsCatchBlockTest() throws HMTPException {
		assayImpl.getAssayInputValidationData(assayType,"ivf status","mandate flag");
		
	}
	
	/**
	 * Gets the assay type ID catch block test.
	 *
	 * @return the assay type ID catch block test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test(expectedExceptions = HMTPException.class)
	public void getAssayTypeIDCatchBlockTest() throws HMTPException {
		assayImpl.getAssayTypeID(assayType);
		
	}
	
	/**
	 * Molecular ID catch block test.
	 *
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test(expectedExceptions = HMTPException.class)
	public void molecularIDCatchBlockTest() throws HMTPException {
		assayImpl.getMolecularIdData(assayType,"96","1");
		
	}
	
	
	
	@Test(expectedExceptions = HMTPException.class)
	public void deviceTestOptionsDataTest() throws HMTPException {
		assayImpl.getDeviceTestOptionsData(assayType, "MP24", "NA Extraction");
		
	}
	
	@Test(expectedExceptions = HMTPException.class)
	public void testFetchFlagValues() throws HMTPException {
		assayImpl.getFlagDescriptions(null,assayType,null,null);
		
	}
	
	
	/**
	 * After test.
	 */
	@AfterTest
	public void afterTest() {
		assayType = null;
		expectedCorrectCode = 0;
		expectedIncorrectCode = 0;
	}
}
