/*******************************************************************************
 * File Name: OrderService.java            
 * Version:  1.0
 * 
 * Authors: Ankit Singh
 * 
 * =========================================
 * 
 * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 * executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 * 
 * =========================================
 * 
 * ChangeLog:
 ******************************************************************************/
package com.roche.connect.omm.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.common.order.dto.ProcessStepValuesDTO;
import com.roche.connect.common.order.dto.WorkflowDTO;
import com.roche.connect.omm.model.ContainerSamples;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.model.TestOptions;

public interface OrderService {

	public Patient createPatientObject(OrderParentDTO orderParent) throws ParseException, HMTPException;

	public PatientAssay createPatientAssayObject(OrderParentDTO orderParent) throws ParseException;

	public List<PatientSamples> createPatientSamplesObject(OrderParentDTO orderParent);

	public Order createOrderObject(OrderParentDTO orderParent);

	public List<TestOptions> createTestOptionsObject(OrderParentDTO orderParent) throws HMTPException;

	@SuppressWarnings("rawtypes")
	public List<OrderDTO> convertMapToOrders(List<Map> ordersMap);

	public OrderParentDTO createOrderParentDTO(Order order);
	
	public OrderDTO createOrderDTO(Order order);
	
	public AssayDTO createAssayDTO(Order order);
	
	public PatientDTO createPatientDTO(Order order);
	
	public Map<String,Boolean> createTestOptionsDTO(Order order);
	
	public Order orderUpdateMapper(OrderDTO orderdto, Order orderToUpdate);
	
	public PatientAssay patientAssayMapper(PatientAssay patientAssay,AssayDTO assayDTO);
	
	public  Patient patientUpdateMapper(Patient patient,PatientDTO patientDTO);
	
	public WorkflowDTO createWrokflowOrderObject(ProcessStepValuesDTO processStepValuesDTO,OrderDTO orderDTO);
	
	public Map<String, Object>  assayValidations(OrderParentDTO orderParent) throws HMTPException;
	
	public boolean isAccessioningDuplicate(OrderParentDTO orderParent) throws HMTPException;
	
	public Map<String,List<String>> validateCSV(List<String> parsedCSV)throws HMTPException;
	
	public List<ContainerSamplesDTO> mapInputCSVToDTO(List<String> parsedCSV);
	
	public boolean isCSVJsonValid(List<ContainerSamplesDTO> containerSamplesDTOList);
	
	public List<ContainerSamples> containerSamplesDTOToObjMapper(List<ContainerSamplesDTO> containerSampleDTOList);
	
	public List<ContainerSamples> containerSamplesMapperForStatusUpdate(List<ContainerSamples> containerSampleList);
	
	public List<ContainerSamplesDTO> containerSamplesMapperForObjTODTO(List<Object> containerSamples) throws ParseException; 

	public boolean isOrderPresent(String accessioningId);
	
	public boolean isAssayTypeValid(String accessioningId);
	
    public Map<String,List<String>> fileSizevalidation(InputStream in) throws HMTPException, IOException;
    
    public List<AssayInputDataValidationsDTO> mandatoryFieldValidationByAssay(String assayType) throws HMTPException, UnsupportedEncodingException;
    
    public List<WorkflowDTO> getInWorkflowOrders() throws HMTPException, UnsupportedEncodingException;
    
    public boolean getRequiredFieldMissingFlag(OrderParentDTO order) throws HMTPException; 
    
}
