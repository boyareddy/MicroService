/*******************************************************************************
 * File Name: OrderServiceImpl.java            
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO;
import com.roche.connect.common.amm.dto.AssayListDataDTO;
import com.roche.connect.common.amm.dto.AssayTypeDTO;
import com.roche.connect.common.amm.dto.SampleTypeDTO;
import com.roche.connect.common.amm.dto.TestOptionsDTO;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.common.order.dto.ProcessStepValuesDTO;
import com.roche.connect.common.order.dto.WorkflowDTO;
import com.roche.connect.common.util.DateUtil;
import com.roche.connect.common.util.DateUtilImpl;
import com.roche.connect.omm.error.CustomErrorCodes;
import com.roche.connect.omm.model.ContainerSamples;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.model.TestOptions;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.util.AES;
import com.roche.connect.omm.util.MandatoryFlagValidationUtil;
import com.roche.connect.omm.util.OMMConstant;
import com.roche.connect.omm.util.ResourceBundleUtil;

@Component
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderReadRepository orderReadRepository;
	@Autowired
	ContainerSamplesReadRepository containerSamplesReadRepository;
	@Autowired
	ResourceBundleUtil resourceBundleUtil;

	@Value("${connect.phi_encrypt}")
	Boolean phiEncrypt;

	DateUtil dateUtil = new DateUtilImpl();
	ResourceBundle resourceBundle = null;
	String utf = "UTF-8";
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	private static Lock lockObjInner = new ReentrantLock();

	/**
	 * Creates the patient object.
	 *
	 * @param orderParent
	 *            the order parent
	 * @return Patient
	 * @throws ParseException
	 *             the parse exception
	 * @throws HMTPException 
	 */
	@Override
	public Patient createPatientObject(OrderParentDTO orderParent) throws ParseException, HMTPException {
		PatientDTO patientMapper = orderParent.getOrder().getPatient();
		Patient patient = null;
		try{			
		if (phiEncrypt) {
			patient = (Patient) AES.getEncryptedObject(Patient.class.getName(), patientMapper);
		} else {
			patient = new Patient();
			patient.setPatientFirstName(patientMapper.getPatientFirstName());
			patient.setPatientLastName(patientMapper.getPatientLastName());
			patient.setPatientDOB(patientMapper.getPatientDOB());
			patient.setPatientMedicalRecNo(patientMapper.getPatientMedicalRecNo());
			/**
			 * patient.setPatientGender(patientMapper.getPatientGender());
			 * patient.setPatientContactNo(patientMapper.getPatientContactNo());
			 * patient.setTreatingDoctorName(patientMapper.getTreatingDoctorName
			 * ()); patient.setTreatingDoctorContactNo(patientMapper.
			 * getTreatingDoctorContactNo());
			 * patient.setRefClinicianFaxNo(patientMapper.getRefClinicianFaxNo()
			 * ); patient.setOtherClinicianFaxNo(patientMapper.
			 * getOtherClinicianFaxNo());
			 */
			patient.setRefClinicianName(patientMapper.getRefClinicianName());
			patient.setClinicName(patientMapper.getClinicName());
			patient.setOtherClinicianName(patientMapper.getOtherClinicianName());
			patient.setAccountNumber(patientMapper.getAccountNumber());
			patient.setLabId(patientMapper.getLabId());
			patient.setReasonForReferral(patientMapper.getReasonForReferral());

		}

		patient.setActiveFlag(OMMConstant.ACTIVE_FLAG_YES.toString());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		patient.setCreatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
		patient.setCreatedDateTime(timestamp);
		}
		catch(Exception e){
			throw new HMTPException(CustomErrorCodes.ERROR_ENCRIPTION,
					OMMConstant.ErrorMessages.ERROR_WHILE_ENCRYPTION.toString());
		}
		return patient;
	}

	/**
	 * Creates the patient assay object.
	 *
	 * @param orderParent
	 *            the order parent
	 * @return PatientAssay
	 * @throws ParseException
	 *             the parse exception
	 */
	@Override
	public PatientAssay createPatientAssayObject(OrderParentDTO orderParent) throws ParseException {
		AssayDTO assayMapper = orderParent.getOrder().getAssay();
		PatientAssay patientAssay = new PatientAssay();

		patientAssay.setMaternalAge(assayMapper.getMaternalAge());
		patientAssay.setGestationalAgeWeeks(assayMapper.getGestationalAgeWeeks());
		patientAssay.setGestationalAgeDays(assayMapper.getGestationalAgeDays());
		patientAssay.setEggDonor(assayMapper.getEggDonor());
		patientAssay.setEggDonorAge(assayMapper.getEggDonorAge());
		patientAssay.setIvfStatus(assayMapper.getIvfStatus());
		patientAssay.setFetusNumber(assayMapper.getFetusNumber());
		patientAssay.setCollectionDate(assayMapper.getCollectionDate());
		patientAssay.setRecievedDate(assayMapper.getReceivedDate());
		patientAssay.setActiveFlag(OMMConstant.ACTIVE_FLAG_YES.toString());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		patientAssay.setCreatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
		patientAssay.setCreatedDateTime(timestamp);

		return patientAssay;
	}

	/**
	 * Creates the patient samples object.
	 *
	 * @param orderParent
	 *            the order parent
	 * @return List
	 */
	@Override
	public List<PatientSamples> createPatientSamplesObject(OrderParentDTO orderParent) {
		OrderDTO orderMapper = orderParent.getOrder();
		List<PatientSamples> patientSamples = new ArrayList<>();
		String user = ThreadSessionManager.currentUserSession().getAccessorUserName();
		PatientSamples patientSampleOne = new PatientSamples();
		patientSampleOne.setSampleId(orderMapper.getAccessioningId());
		patientSampleOne.setSampleType(orderMapper.getSampleType());
		Timestamp timestampOne = new Timestamp(System.currentTimeMillis());
		patientSampleOne.setCreatedBy(user);
		patientSampleOne.setCreatedDateTime(timestampOne);

		PatientSamples patientSampleTwo = new PatientSamples();
		patientSampleTwo.setSampleId(orderMapper.getAccessioningId());
		patientSampleTwo.setSampleType(orderMapper.getSampleType());
		Timestamp timestampTwo = new Timestamp(System.currentTimeMillis());
		patientSampleTwo.setCreatedBy(user);
		patientSampleTwo.setCreatedDateTime(timestampTwo);

		if (orderMapper.isRetestSample()) {
			patientSampleOne.setActiveFlag(OMMConstant.ACTIVE_FLAG_YES.toString());
			patientSampleTwo.setActiveFlag(OMMConstant.ACTIVE_FLAG_YES.toString());
		} else {
			patientSampleOne.setActiveFlag(OMMConstant.ACTIVE_FLAG_YES.toString());
			patientSampleTwo.setActiveFlag(OMMConstant.ACTIVE_FLAG_NO.toString());
		}

		patientSamples.add(patientSampleOne);
		patientSamples.add(patientSampleTwo);

		return patientSamples;
	}

	/**
	 * Creates the order object.
	 *
	 * @param orderParent
	 *            the order parent
	 * @return Order
	 */
	@Override
	public Order createOrderObject(OrderParentDTO orderParent) {
		OrderDTO orderMapper = orderParent.getOrder();
		Order order = new Order();

		order.setAccessioningId(orderMapper.getAccessioningId());
		order.setAssayType(orderMapper.getAssayType());
		order.setOrderStatus(OMMConstant.ORDER_STATUS_UNASSIGNED.toString());
		order.setOrderComments(orderMapper.getOrderComments());
		order.setActiveFlag(OMMConstant.ACTIVE_FLAG_YES.toString());
		order.setPriority(orderMapper.getPriority());
		order.setCreatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
		order.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
		if(OMMConstant.HIGH_PRIORITY.toString().equalsIgnoreCase(orderMapper.getPriority())) {
			order.setPriorityUpdatedTime(order.getCreatedDateTime());
		}
		order.setUpdatedDateTime(order.getCreatedDateTime());
		return order;
	}

	/**
	 * Creates the test options object.
	 *
	 * @param orderParent
	 *            the order parent
	 * @return List
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Override
	public List<TestOptions> createTestOptionsObject(OrderParentDTO orderParent) throws HMTPException {
		Map<String, Boolean> testOptions = orderParent.getOrder().getAssay().getTestOptions();
		List<TestOptions> listOfTestOptions = new ArrayList<>();
		try {
			for (Entry<String, Boolean> test : testOptions.entrySet()) {
				TestOptions testOption = new TestOptions();
				testOption.setTestId(test.getKey());
				if (testOptions.get(test.getKey()).equals(OMMConstant.TRUE_FLAG.isBoolVal())) {
					testOption.setActiveFlag(OMMConstant.ACTIVE_FLAG_YES.toString());
					listOfTestOptions.add(testOption);
				} else {
					testOption.setActiveFlag(OMMConstant.ACTIVE_FLAG_NO.toString());
					listOfTestOptions.add(testOption);
				}
			}
		} catch (Exception exp) {
			throw new HMTPException(exp);
		}

		return listOfTestOptions;
	}

	/**
	 * Patient assay mapper.
	 *
	 * @param patientAssay
	 *            the patient assay
	 * @param assayDTO
	 *            the assay DTO
	 * @return PatientAssay
	 */
	public PatientAssay patientAssayMapper(PatientAssay patientAssay, AssayDTO assayDTO) {

		patientAssay.setMaternalAge(assayDTO.getMaternalAge());
		patientAssay.setGestationalAgeWeeks(assayDTO.getGestationalAgeWeeks());
		patientAssay.setGestationalAgeDays(assayDTO.getGestationalAgeDays());
		patientAssay.setEggDonor(assayDTO.getEggDonor());
		patientAssay.setEggDonorAge(assayDTO.getEggDonorAge());
		patientAssay.setIvfStatus(assayDTO.getIvfStatus());
		patientAssay.setFetusNumber(assayDTO.getFetusNumber());
		patientAssay.setCollectionDate(assayDTO.getCollectionDate());
		patientAssay.setRecievedDate(assayDTO.getReceivedDate());
		patientAssay.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
		return patientAssay;
	}

	/**
	 * Patient update mapper.
	 *
	 * @param patient
	 *            the patient
	 * @param patientDTO
	 *            the patient DTO
	 * @return Patient
	 */
	public Patient patientUpdateMapper(Patient patient, PatientDTO patientDTO) {
		if (phiEncrypt) {
			patient = (Patient) AES.getEncryptedObject(Patient.class.getName(), patientDTO);
		} else {
			patient.setPatientFirstName(patientDTO.getPatientFirstName());
			patient.setPatientLastName(patientDTO.getPatientLastName());
			patient.setPatientMedicalRecNo(patientDTO.getPatientMedicalRecNo());
			patient.setPatientDOB(patientDTO.getPatientDOB());
			/**
			 * patient.setPatientGender(patientDTO.getPatientGender());
			 * patient.setPatientContactNo(patientDTO.getPatientContactNo());
			 * patient.setTreatingDoctorName(patientDTO.getTreatingDoctorName())
			 * ; patient.setTreatingDoctorContactNo(patientDTO.
			 * getTreatingDoctorContactNo());
			 * patient.setRefClinicianFaxNo(patientDTO.getRefClinicianFaxNo());
			 * patient.setOtherClinicianFaxNo(patientDTO.getOtherClinicianFaxNo(
			 * ));
			 */
			patient.setRefClinicianName(patientDTO.getRefClinicianName());
			patient.setOtherClinicianName(patientDTO.getOtherClinicianName());
			patient.setClinicName(patientDTO.getClinicName());
			patient.setAccountNumber(patientDTO.getAccountNumber());
			patient.setLabId(patientDTO.getLabId());
			patient.setReasonForReferral(patientDTO.getReasonForReferral());
		}
		patient.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
		return patient;
	}

	/**
	 * Order update mapper.
	 *
	 * @param orderdto
	 *            the orderdto
	 * @param orderToUpdate
	 *            the order to update
	 * @return Order
	 */
	public Order orderUpdateMapper(OrderDTO orderdto, Order orderToUpdate) {
		orderToUpdate.setAccessioningId(orderdto.getAccessioningId());
		orderToUpdate.setAssayType(orderdto.getAssayType());
		orderToUpdate.setOrderComments(orderdto.getOrderComments());
		if(!orderdto.getPriority().equalsIgnoreCase(orderToUpdate.getPriority())) {
			orderToUpdate.setPriorityUpdatedTime(new Timestamp(System.currentTimeMillis()));
		}
		orderToUpdate.setPriority(orderdto.getPriority());
		orderToUpdate.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
		return orderToUpdate;
	}

	/**
	 * Creates the order parent DTO.
	 *
	 * @param order
	 *            the order
	 * @return OrderParentDTO
	 */
	@Override
	public OrderParentDTO createOrderParentDTO(Order order) {
		OrderParentDTO orderParent = new OrderParentDTO();
		orderParent.setOrder(createOrderDTO(order));
		orderParent.getOrder().setAssay(createAssayDTO(order));
		orderParent.getOrder().setPatient(createPatientDTO(order));
		return orderParent;
	}

	/**
	 * Creates the order DTO.
	 *
	 * @param order
	 *            the order
	 * @return OrderDTO
	 */
	public OrderDTO createOrderDTO(Order order) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(order.getId());
		orderDTO.setPatientId(order.getPatient().getId());
		orderDTO.setPatientSampleId(order.getPatientSampleId());
		orderDTO.setAccessioningId(order.getAccessioningId());
		orderDTO.setAssayType(order.getAssayType());
		orderDTO.setReqFieldMissingFlag(order.isReqFieldMissingFlag());
		orderDTO.setPriority(order.getPriority());
		Collection<PatientSamples> patientSamples = order.getPatient().getPatientSamples();
		String sampleType = "";
		for (PatientSamples ps : patientSamples) {
			if (ps.getActiveFlag().equalsIgnoreCase(OMMConstant.ACTIVE_FLAG_YES.toString())) {
				sampleType = ps.getSampleType();
				break;
			}
		}
		orderDTO.setSampleType(sampleType);
		orderDTO.setOrderStatus(order.getOrderStatus());
		boolean retestSample = OMMConstant.RETEST_SAMPLE_FALSE.isBoolVal();
		int sampleCount = 0;
		for (PatientSamples ps : patientSamples) {
			if (ps.getActiveFlag().equalsIgnoreCase(OMMConstant.ACTIVE_FLAG_YES.toString())) {
				sampleCount++;
			}
		}
		if (sampleCount > 1)
			retestSample = OMMConstant.RETEST_SAMPLE_TRUE.isBoolVal();
		orderDTO.setRetestSample(retestSample);
		orderDTO.setOrderComments(order.getOrderComments());
		orderDTO.setActiveFlag(order.getActiveFlag());
		orderDTO.setCreatedBy(order.getCreatedBy());
		orderDTO.setCreatedDateTime(order.getCreatedDateTime());
		orderDTO.setUpdatedBy(order.getUpdatedBy());
		orderDTO.setUpdatedDateTime(order.getUpdatedDateTime());
		return orderDTO;
	}

	/**
	 * Creates the assay DTO.
	 *
	 * @param order
	 *            the order
	 * @return AssayDTO
	 */
	public AssayDTO createAssayDTO(Order order) {
		AssayDTO assayDTO = new AssayDTO();
		PatientAssay patientAssay = order.getPatient().getPetientAssay();
		assayDTO.setPatientAssayid(patientAssay.getId());
		assayDTO.setMaternalAge(patientAssay.getMaternalAge());
		assayDTO.setGestationalAgeWeeks(patientAssay.getGestationalAgeWeeks());
		assayDTO.setGestationalAgeDays(patientAssay.getGestationalAgeDays());
		assayDTO.setIvfStatus(patientAssay.getIvfStatus());
		assayDTO.setEggDonor(patientAssay.getEggDonor());
		assayDTO.setEggDonorAge(patientAssay.getEggDonorAge());
		assayDTO.setFetusNumber(patientAssay.getFetusNumber());
		assayDTO.setCollectionDate(patientAssay.getCollectionDate());
		assayDTO.setReceivedDate(patientAssay.getRecievedDate());
		assayDTO.setTestOptions(createTestOptionsDTO(order));
		return assayDTO;
	}

	/**
	 * Creates the test options DTO.
	 *
	 * @param order
	 *            the order
	 * @return Map
	 */
	public Map<String, Boolean> createTestOptionsDTO(Order order) {
		Collection<TestOptions> testOptions = order.getListOfTestOptions();
		Map<String, Boolean> testOptionsDTO = new HashMap<>();
		for (TestOptions testOption : testOptions) {
			if (testOption.getActiveFlag().equalsIgnoreCase(OMMConstant.ACTIVE_FLAG_YES.toString()))
				testOptionsDTO.put(testOption.getTestId(), OMMConstant.TRUE_FLAG.isBoolVal());
			else
				testOptionsDTO.put(testOption.getTestId(), OMMConstant.FALSE_FLAG.isBoolVal());
		}
		return testOptionsDTO;
	}

	/**
	 * Creates the patient DTO.
	 *
	 * @param order
	 *            the order
	 * @return PatientDTO
	 */
	public PatientDTO createPatientDTO(Order order) {
		PatientDTO patientDTO = null;
		Patient patient = order.getPatient();
		if (phiEncrypt) {
			patientDTO = (PatientDTO) AES.getDecryptedObject(PatientDTO.class.getName(), patient);
		} else {
			patientDTO = new PatientDTO();
			patientDTO.setPatientFirstName(patient.getPatientFirstName());
			patientDTO.setPatientLastName(patient.getPatientLastName());
			patientDTO.setPatientMedicalRecNo(patient.getPatientMedicalRecNo());
			patientDTO.setPatientDOB(patient.getPatientDOB());
			/**
			 * patientDTO.setPatientGender(patient.getPatientGender());
			 * patientDTO.setPatientContactNo(patient.getPatientContactNo());
			 * patientDTO.setTreatingDoctorName(patient.getTreatingDoctorName())
			 * ; patientDTO.setTreatingDoctorContactNo(patient.
			 * getTreatingDoctorContactNo());
			 * patientDTO.setRefClinicianFaxNo(patient.getRefClinicianFaxNo());
			 * patientDTO.setOtherClinicianFaxNo(patient.getOtherClinicianFaxNo(
			 * ));
			 */
			patientDTO.setRefClinicianName(patient.getRefClinicianName());
			patientDTO.setOtherClinicianName(patient.getOtherClinicianName());
			patientDTO.setClinicName(patient.getClinicName());
			patientDTO.setAccountNumber(patient.getAccountNumber());
			patientDTO.setLabId(patient.getLabId());
			patientDTO.setReasonForReferral(patient.getReasonForReferral());

		}

		patientDTO.setPatientId(patient.getId());

		return patientDTO;
	}

	/**
	 * Convert map to orders.
	 *
	 * @param ordersMap
	 *            the orders map
	 * @return List
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List<OrderDTO> convertMapToOrders(List<Map> ordersMap) {

		List<OrderDTO> orders = new ArrayList<>();

		ordersMap.forEach(e -> {
			OrderDTO order = new OrderDTO();

			order.setOrderId((Long) e.get("orderId"));
			order.setPatientId((Long) e.get("patientId"));
			order.setPatientSampleId((Long) e.get("patientSampleId"));
			order.setAccessioningId((String) e.get("accessioningId"));
			order.setOrderStatus((String) e.get("orderStatus"));
			order.setAssayType((String) e.get("assayType"));
			order.setOrderComments((String) e.get("orderComments"));
			order.setCreatedDateTime((Timestamp) e.get("createdDateTime"));
			order.setUpdatedDateTime((Timestamp) e.get("updatedDateTime"));
			order.setActiveFlag((String) e.get("activeFlag"));
			order.setCreatedBy((String) e.get("createdBy"));
			order.setSampleType((String) e.get("sampleType"));
			order.setReqFieldMissingFlag((Boolean)e.get("reqFieldMissingFlag"));

			orders.add(order);
		});

		return orders;

	}

	/**
	 * Creates the wrokflow order object.
	 *
	 * @param processStepValuesDTO
	 *            the process step values DTO
	 * @param orderDTO
	 *            the order DTO
	 * @return WorkflowDTO
	 */
	@Override
	public WorkflowDTO createWrokflowOrderObject(ProcessStepValuesDTO processStepValuesDTO, OrderDTO orderDTO) {
		WorkflowDTO workflowdto = new WorkflowDTO();
		workflowdto.setAccessioningId(orderDTO.getAccessioningId());
		workflowdto.setComments(processStepValuesDTO.getComments());
		workflowdto.setOrderId(orderDTO.getOrderId());
		workflowdto.setWorkflowType(processStepValuesDTO.getProcessStepName());
		workflowdto.setAssaytype(orderDTO.getAssayType());
		workflowdto.setSampletype(orderDTO.getSampleType());
		workflowdto.setWorkflowStatus(processStepValuesDTO.getRunStatus());
		workflowdto.setFlags(processStepValuesDTO.getRunFlag());
		return workflowdto;
	}

	@Override
	public Map<String, Object> assayValidations(OrderParentDTO orderParent) throws HMTPException {

		List<AssayTypeDTO> assayTypelist = new ArrayList<>();
		List<SampleTypeDTO> sampleTypelist = new ArrayList<>();
		List<AssayListDataDTO> assayListData = new ArrayList<>();
		List<TestOptionsDTO> testOptionlist = new ArrayList<>();
		List<AssayInputDataValidationsDTO> inputDataValidationslist = new ArrayList<>();
		String apiPath = "/json/rest/api/v1/assay/";
		String api = "pas.amm_api_url";		
		Map<String, Object> totalMap = new HashMap<>();
		try {
			logger.info("Calling AMM to find assay types");
			String urlassayType = RestClientUtil.getUrlString(api, "",
					apiPath + "?assayType=" + orderParent.getOrder().getAssayType(), "", null);
			Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(urlassayType, utf), null);
			assayTypelist = orderClient.get(new GenericType<List<AssayTypeDTO>>() {
			});
			totalMap.put("assayTypeList", assayTypelist);

			logger.info("Calling AMM to SampleType");
			String urlSampleType = RestClientUtil.getUrlString(api, "",
					apiPath + orderParent.getOrder().getAssayType() + "/sampletype", "", null);
			Invocation.Builder orderClientSample = RestClientUtil.getBuilder(URLEncoder.encode(urlSampleType, utf),
					null);
			sampleTypelist = orderClientSample.get(new GenericType<List<SampleTypeDTO>>() {
			});
			totalMap.put("sampleTypeList", sampleTypelist);

			logger.info("Calling AMM to assayListData");
			String urlassayList = RestClientUtil.getUrlString(api, "",
					apiPath + orderParent.getOrder().getAssayType() + "/listdata", "", null);
			Invocation.Builder orderClientAssayList = RestClientUtil.getBuilder(URLEncoder.encode(urlassayList, utf),
					null);
			assayListData = orderClientAssayList.get(new GenericType<List<AssayListDataDTO>>() {
			});
			totalMap.put("assayListData", assayListData);

			logger.info("Calling AMM to testOptionlist");
			String urltestOptions = RestClientUtil.getUrlString(api, "",
					apiPath + orderParent.getOrder().getAssayType() + "/testoptions", "", null);
			Invocation.Builder orderClienttestOptions = RestClientUtil
					.getBuilder(URLEncoder.encode(urltestOptions, utf), null);
			testOptionlist = orderClienttestOptions.get(new GenericType<List<TestOptionsDTO>>() {
			});

			totalMap.put("testOptionlist", testOptionlist);

			logger.info("Calling AMM to inputDataValidationslist");
			String urlinputDataValidations = RestClientUtil.getUrlString(api, "",
					apiPath + orderParent.getOrder().getAssayType() + "/inputdatavalidations", "", null);
			Invocation.Builder orderClientinputDataValidations = RestClientUtil
					.getBuilder(URLEncoder.encode(urlinputDataValidations, utf), null);
			inputDataValidationslist = orderClientinputDataValidations
					.get(new GenericType<List<AssayInputDataValidationsDTO>>() {
					});
			totalMap.put("inputDataValidationslist", inputDataValidationslist);
		} catch (InternalServerErrorException internalExp) {
			logger.error("Error occurred while calling at AMM Api's" + internalExp.getMessage());
			throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
					"AssayType " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
		} catch (Exception exp) {
			logger.error("Error occurred while calling at AMM Api's" + exp.getMessage());
			throw new HMTPException(exp);
		}
		return totalMap;
	}

	@Override
	public boolean isAccessioningDuplicate(OrderParentDTO orderParentDTO) {
		long domainId = 0;
		boolean duplicateAccessiongId = false;
		domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		String accessioningId = orderParentDTO.getOrder().getAccessioningId();
		List<Order> orderaccessioningId = orderReadRepository.findAllByAccessioningId(accessioningId, domainId);
		if (!orderaccessioningId.isEmpty()) {
			duplicateAccessiongId = true;
		}
		return duplicateAccessiongId;

	}

	@Override
	public Map<String, List<String>> validateCSV(List<String> parsedCSV) throws HMTPException {
		resourceBundle = resourceBundleUtil.getResourceBundle();
		String[] row = null;
		String[] header = null;
		int rowCount = 2;
		List<String> headerErrors = new ArrayList<>();
		List<String> containerReMappingErrors = new ArrayList<>();
		List<String> containerReMappingErrors1 = new ArrayList<>();
		Map<String, List<String>> rowErrors = new LinkedHashMap<>();
		Map<Integer, String> accessioningIdProcessed = new LinkedHashMap<>();
		Map<Integer, String> containerIdPositionProcessed = new LinkedHashMap<>();
		List<String> pCSV = parsedCSV;
		Iterator<String> it = parsedCSV.iterator();
		try {
			if (pCSV.isEmpty()) {
				headerErrors.add(resourceBundleUtil.getMessages(resourceBundle, "NO_DATA_PRESENT"));
				rowErrors.put("CSV file: ", headerErrors);
			}
			if (it.hasNext()) {
				header = it.next().split(OMMConstant.CSV.DELIMETER.toString());
				headerErrors = checkCSVHeader(header);
				if (headerErrors.isEmpty()) {
					if (parsedCSV.size() == 1) {
						headerErrors.add(resourceBundleUtil.getMessages(resourceBundle, "NO_DATA_PRESENT"));
						rowErrors.put("CSV file: ", headerErrors);
					}
					containerReMappingErrors = checkMappingForContainers(pCSV);
					if (!containerReMappingErrors.isEmpty()) {
						String containerIDs = "";
						for (String st : containerReMappingErrors) {
							containerIDs += st + ", ";
						}
						containerIDs = (containerIDs == null || containerIDs.length() == 0) ? null
								: containerIDs.substring(0, containerIDs.length() - 2);
						containerReMappingErrors1.add(resourceBundleUtil.getMessages(resourceBundle,
								"DELETE_EXISTING_CONTAINER", containerIDs));
						rowErrors.put("", containerReMappingErrors1);
						return rowErrors;
					}
					while (it.hasNext()) {
						row = it.next().split(OMMConstant.CSV.DELIMETER.toString());
						List<String> errors = checkFieldsForCSVStream(rowCount, row, accessioningIdProcessed,
								containerIdPositionProcessed);
						if (!errors.isEmpty())
							rowErrors.put("Row " + rowCount + ":", errors);
						rowCount++;
					}
				} else {
					rowErrors.put("", headerErrors);
				}
			}
		} catch (Exception exp) {
			List<String> errorMessage = new ArrayList<>();
			errorMessage.add(exp.getMessage());
			rowErrors.put("Error in validatingCSV:", errorMessage);
		}
		return rowErrors;
	}

	public List<String> checkCSVHeader(String[] header) {
		List<String> errors = new ArrayList<>();
		try {
			if (header != null && (header.length != OMMConstant.CSV.HEADER_SIZE.toInteger()
					|| !header[OMMConstant.CSV.POSITION_INDEX.toInteger()]
							.equalsIgnoreCase(OMMConstant.CSV.POSITION.toString())
					|| !header[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()]
							.equalsIgnoreCase(OMMConstant.CSV.ACCESSIONING_ID.toString())
					|| !header[OMMConstant.CSV.CONTAINER_ID_INDEX.toInteger()]
							.equalsIgnoreCase(OMMConstant.CSV.CONTAINER_ID.toString())
					|| !header[OMMConstant.CSV.CONTAINER_TYPE_INDEX.toInteger()]
							.equalsIgnoreCase(OMMConstant.CSV.CONTAINER_TYPE.toString()))) {
				errors.add(resourceBundleUtil.getMessages(resourceBundle, "INCORRECT_HEADER"));
			}
		} catch (Exception exp) {
			errors.add(resourceBundleUtil.getMessages(resourceBundle, "CSV_HEADER_VALIDATION_FAILURE"));
		}
		return errors;
	}

	public List<String> checkFieldsForCSVStream(Integer rownum, String[] row,
			Map<Integer, String> accessioningIdProcessed, Map<Integer, String> containerIdPositionProcessed) {
		List<String> errors = new ArrayList<>();
		boolean containerPositionValidCheck = false;
		try {
			ContainerSamples containerSamples = new ContainerSamples();
			String errorMsg = "";
			if (row.length == OMMConstant.CSV.HEADER_SIZE.toInteger()) {
				if (row[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()] == null
						|| row[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()].isEmpty()) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "ACCESSIONING_ID_EMPTY"));
				} else if (!StringUtils.isEmpty((errorMsg = isAccessioningPatternValid(
						row[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()])))) {
					errors.add(errorMsg);
				} else if (accessioningIdProcessed.values()
						.contains(row[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()].toLowerCase())) {
					accessioningIdProcessed.forEach((k, v) -> {
						if (v.equalsIgnoreCase(row[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()]))
							errors.add(resourceBundleUtil.getMessages(resourceBundle,
									"ACCESSIONING_ID_DUPLICATE_IN_CSV", k));
					});
				} else if (isAccessioningDuplicate(row[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()],
						containerSamples)) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "ACCESSIONING_ID_DUPLICATE_IN_DB",
							containerSamples.getContainerID(), containerSamples.getPosition()));

				} else if (!isOrderPresent(row[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()])) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "ORDER_NOT_PRESENT",
							row[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()]));
				} else if (!isAssayTypeValid(row[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()])) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "ASSAY_TYPE_INVALID"));
				} else {
					accessioningIdProcessed.put(rownum,
							row[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()].toLowerCase());
				}

				String containerIdAndPosition = "";
				if (row[OMMConstant.CSV.CONTAINER_ID_INDEX.toInteger()] == null
						|| row[OMMConstant.CSV.CONTAINER_ID_INDEX.toInteger()].isEmpty()) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINER_ID_EMPTY"));
				} else if (!Pattern.matches(OMMConstant.CSV.REGEX_FOR_CONTAINER_ID.toString(),
						row[OMMConstant.CSV.CONTAINER_ID_INDEX.toInteger()])) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINER_ID_FORMAT_INCORRECT_ERROR"));
				} else {
					containerIdAndPosition = containerIdAndPosition
							+ row[OMMConstant.CSV.CONTAINER_ID_INDEX.toInteger()];
				}

				if (row[OMMConstant.CSV.POSITION_INDEX.toInteger()] == null
						|| row[OMMConstant.CSV.POSITION_INDEX.toInteger()].isEmpty()) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "LOCATION_EMPTY"));
				} else if (!Pattern.matches(OMMConstant.CSV.REGEX_FOR_LOCATION_1.toString(),
						row[OMMConstant.CSV.POSITION_INDEX.toInteger()])
						&& !Pattern.matches(OMMConstant.CSV.REGEX_FOR_LOCATION_2.toString(),
								row[OMMConstant.CSV.POSITION_INDEX.toInteger()])) {
					containerPositionValidCheck = true;
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINER_LOCATION_FORMAT_INCORRECT"));
				} else {
					containerIdAndPosition = containerIdAndPosition + row[OMMConstant.CSV.POSITION_INDEX.toInteger()];
				}

				final String finalContainerIdAndPosition = containerIdAndPosition;
				if (row[OMMConstant.CSV.CONTAINER_ID_INDEX.toInteger()] != null
						&& !row[OMMConstant.CSV.CONTAINER_ID_INDEX.toInteger()].isEmpty()
						&& row[OMMConstant.CSV.POSITION_INDEX.toInteger()] != null
						&& !row[OMMConstant.CSV.POSITION_INDEX.toInteger()].isEmpty()) {

					if (containerIdPositionProcessed.values().contains(containerIdAndPosition)) {
						containerIdPositionProcessed.forEach((k, v) -> {
							if (v.equalsIgnoreCase(finalContainerIdAndPosition))
								errors.add(resourceBundleUtil.getMessages(resourceBundle,
										"CONTAINERID_POSITION_DUPLICATE_CSV", k));
						});
					} else if (isContaineridAndPositionDuplicate(row[OMMConstant.CSV.CONTAINER_ID_INDEX.toInteger()],
							row[OMMConstant.CSV.POSITION_INDEX.toInteger()], containerSamples)) {
						errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINERID_POSITION_DUPLICATE_DB",
								containerSamples.getAccessioningID()));
					} else {
						if (containerPositionValidCheck) {
							containerPositionValidCheck = false;
						} else {
							containerIdPositionProcessed.put(rownum, containerIdAndPosition);
						}
					}
				}

				if (row[OMMConstant.CSV.CONTAINER_TYPE_INDEX.toInteger()] == null
						|| row[OMMConstant.CSV.CONTAINER_TYPE_INDEX.toInteger()].isEmpty()) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINER_TYPE_EMPTY"));
				} else if (!row[OMMConstant.CSV.CONTAINER_TYPE_INDEX.toInteger()]
						.equalsIgnoreCase(OMMConstant.CSV.CONTAINER_TYPE_NAME.toString())) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINER_TYPE_DATA_INCORRECT"));
				}
			} else {
				if (row.length == 3)
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "ACCESSIONING_ID_EMPTY"));
				else if (row.length == 2) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "LOCATION_EMPTY"));
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "ACCESSIONING_ID_EMPTY"));
				} else if (row.length == 1) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINER_ID_EMPTY"));
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "LOCATION_EMPTY"));
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "ACCESSIONING_ID_EMPTY"));
				} else {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "INCORRECT_DATA_FORMAT"));
				}
			}
		} catch (Exception exp) {
			errors.add(resourceBundleUtil.getMessages(resourceBundle, "VALIDATION_ERROR"));
		}
		return errors;
	}

	public boolean isCSVJsonValid(List<ContainerSamplesDTO> containerSamplesDTOList) {
		boolean csvValid = false;
		List<String> errors = new ArrayList<>();
		Iterator<ContainerSamplesDTO> it = containerSamplesDTOList.iterator();
		Map<Integer, String> accessioningIdProcessed = new LinkedHashMap<>();
		Map<Integer, String> containerIdPositionProcessed = new LinkedHashMap<>();
		int rownum = 1;
		boolean containerPositionValidCheck = false;
		lockObjInner.lock();
		try {
			Thread.sleep(2000);
			while (it.hasNext()) {
				ContainerSamples containerSamples = new ContainerSamples();
				rownum++;
				ContainerSamplesDTO containerSamplesDTO = it.next();
				String errorMsg = "";
				if (containerSamplesDTO.getAccessioningID() == null
						|| containerSamplesDTO.getAccessioningID().isEmpty()) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "ACCESSIONING_ID_EMPTY"));
				} else if (!StringUtils
						.isEmpty(errorMsg = isAccessioningPatternValid(containerSamplesDTO.getAccessioningID()))) {
					errors.add(errorMsg);
				} else if (accessioningIdProcessed.values()
						.contains(containerSamplesDTO.getAccessioningID().toLowerCase())) {
					accessioningIdProcessed.forEach((k, v) -> {
						if (v.equalsIgnoreCase(containerSamplesDTO.getAccessioningID()))
							errors.add(resourceBundleUtil.getMessages(resourceBundle,
									"ACCESSIONING_ID_DUPLICATE_IN_CSV", k));
					});
				} else if (isAccessioningDuplicate(containerSamplesDTO.getAccessioningID(), containerSamples)) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "ACCESSIONING_ID_DUPLICATE_IN_DB",
							containerSamples.getContainerID(), containerSamples.getPosition()));
				} else if (!isOrderPresent(containerSamplesDTO.getAccessioningID())) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "ORDER_NOT_PRESENT",
							containerSamplesDTO.getAccessioningID()));
				} else if (!isAssayTypeValid(containerSamplesDTO.getAccessioningID())) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "ASSAY_TYPE_INVALID"));
				} else {
					accessioningIdProcessed.put(rownum, containerSamplesDTO.getAccessioningID().toLowerCase());
				}

				String containerIdAndPosition = "";
				if (containerSamplesDTO.getContainerID() == null || containerSamplesDTO.getContainerID().isEmpty()) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINER_ID_EMPTY"));
				} else if (!Pattern.matches(OMMConstant.CSV.REGEX_FOR_CONTAINER_ID.toString(),
						containerSamplesDTO.getContainerID())) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINER_ID_FORMAT_INCORRECT"));
				} else {
					containerIdAndPosition = containerIdAndPosition + containerSamplesDTO.getContainerID();
				}

				if (containerSamplesDTO.getPosition() == null || containerSamplesDTO.getPosition().isEmpty()) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "LOCATION_EMPTY"));
				} else if (!Pattern.matches(OMMConstant.CSV.REGEX_FOR_LOCATION_1.toString(),
						containerSamplesDTO.getPosition())
						&& !Pattern.matches(OMMConstant.CSV.REGEX_FOR_LOCATION_2.toString(),
								containerSamplesDTO.getPosition())) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINER_LOCATION_FORMAT_INCORRECT"));
				} else {
					containerPositionValidCheck = true;
					containerIdAndPosition = containerIdAndPosition + containerSamplesDTO.getPosition();
				}

				final String finalContainerIdAndPosition = containerIdAndPosition;
				if (containerSamplesDTO.getContainerID() != null && !containerSamplesDTO.getContainerID().isEmpty()
						&& containerSamplesDTO.getPosition() != null && !containerSamplesDTO.getPosition().isEmpty()) {

					if (containerIdPositionProcessed.values().contains(containerIdAndPosition)) {
						containerIdPositionProcessed.forEach((k, v) -> {
							if (v.equalsIgnoreCase(finalContainerIdAndPosition))
								errors.add(resourceBundleUtil.getMessages(resourceBundle,
										"CONTAINERID_POSITION_DUPLICATE_CSV", k));
						});
					} else if (isContaineridAndPositionDuplicate(containerSamplesDTO.getContainerID(),
							containerSamplesDTO.getPosition(), containerSamples)) {
						errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINERID_POSITION_DUPLICATE_DB",
								containerSamples.getAccessioningID()));
					} else {
						if (containerPositionValidCheck) {
							containerPositionValidCheck = false;
						} else {
							containerIdPositionProcessed.put(rownum, containerIdAndPosition);
						}
					}
				}

				if (containerSamplesDTO.getContainerType() == null
						|| containerSamplesDTO.getContainerType().isEmpty()) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINER_TYPE_EMPTY"));
				} else if (!containerSamplesDTO.getContainerType()
						.equalsIgnoreCase(OMMConstant.CSV.CONTAINER_TYPE_NAME.toString())) {
					errors.add(resourceBundleUtil.getMessages(resourceBundle, "CONTAINER_TYPE_DATA_INCORRECT"));
				}
			}
		} catch (Exception exp) {
			errors.add(resourceBundleUtil.getMessages(resourceBundle, "VALIDATION_ERROR"));
		} finally {
			lockObjInner.unlock();
		}

		if (errors.isEmpty())
			csvValid = true;
		else
			csvValid = false;

		return csvValid;
	}

	public List<String> checkMappingForContainers(List<String> parsedCSV) {
		List<String> errors = new ArrayList<>();
		Set<String> containers = new HashSet<>();
		long domainId;

		logger.info("checkMappingForContainers() -> start of checkMappingForContainers() execution");

		domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		for (String entry : parsedCSV) {

			String[] str = entry.split(OMMConstant.CSV.DELIMETER.toString());
			if (str.length == 4) {
				containers.add(str[OMMConstant.CSV.CONTAINER_ID_INDEX.toInteger()]);
			}
		}
		for (String contr : containers) {
			if (!(containerSamplesReadRepository.findByContainerID(contr, domainId).isEmpty()))
				errors.add(contr);
		}

		logger.info("End of checkMappingForContainers() execution");
		return errors;
	}

	public String isAccessioningPatternValid(String accessioningId) {
		String validateAccessioningIdPos = "^[a-zA-Z0-9-_]+$";
		String validateAccessioningIdNeg = "^[-_]+$";
		String errorMsg = "";

		if (accessioningId.length() > 20) {
			errorMsg = resourceBundleUtil.getMessages(resourceBundle, "ACCESSIONING_ID_ERROR_MSG1");
		} else if (Pattern.matches(validateAccessioningIdNeg, accessioningId)) {
			errorMsg = resourceBundleUtil.getMessages(resourceBundle, "ACCESSIONING_ID_ERROR_MSG2");
		} else if (!Pattern.matches(validateAccessioningIdPos, accessioningId)) {
			errorMsg = resourceBundleUtil.getMessages(resourceBundle, "ACCESSIONING_ID_ERROR_MSG3");
		}
		return errorMsg;
	}

	public boolean isAccessioningDuplicate(String accessioningId, ContainerSamples containerSamples) {
		long domainId = 0;
		boolean duplicateAccessiongId = false;
		domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		List<Map<Object, Object>> containerSample = containerSamplesReadRepository
				.findSampleByAccessioningId(accessioningId, domainId);
		if (!containerSample.isEmpty()) {
			duplicateAccessiongId = true;
			for (Map<Object, Object> obj : containerSample) {
				containerSamples.setContainerID((String) obj.get("containerId"));
				containerSamples.setPosition((String) obj.get("position"));
			}
		}
		return duplicateAccessiongId;
	}

	public boolean isContaineridAndPositionDuplicate(String containerID, String position,
			ContainerSamples containerSamples) {
		long domainId = 0;
		boolean duplicateContaineridAndPosition = false;
		domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		List<Map<Object, Object>> containerSample = containerSamplesReadRepository
				.findSampleByContaineridAndPosition(containerID, position, domainId);
		if (!containerSample.isEmpty()) {
			duplicateContaineridAndPosition = true;
			for (Map<Object, Object> obj : containerSample) {
				containerSamples.setAccessioningID((String) obj.get("accessioningID"));
			}
		}
		return duplicateContaineridAndPosition;
	}

	public boolean isOrderPresent(String accessioningId) {
		long domainId = 0;
		boolean orderPresent = false;
		domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		List<Order> orderaccessioningId = orderReadRepository.findAllByAccessioningId(accessioningId, domainId);
		if (!orderaccessioningId.isEmpty()) {
			orderPresent = true;
		}
		return orderPresent;
	}

	public boolean isAssayTypeValid(String accessioningId) {
		long domainId = 0;
		boolean assayTypeValid = false;
		domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		List<Order> orderList = orderReadRepository.findAllByAccessioningId(accessioningId, domainId);
		if (!orderList.isEmpty() && orderList.get(0).getAssayType() != null
				&& orderList.get(0).getAssayType().equalsIgnoreCase(OMMConstant.CSV.NIPTDPCR.toString())) {
			assayTypeValid = true;
		}
		return assayTypeValid;
	}

	public List<ContainerSamplesDTO> mapInputCSVToDTO(List<String> parsedCSV) {
		List<ContainerSamplesDTO> containerSamplesDTOList = new LinkedList<>();
		for (int i = 0; i < parsedCSV.size(); i++) {
			if (i == OMMConstant.CSV.FIRST_HEADER_ROW.toInteger())
				continue;
			String[] row = parsedCSV.get(i).split(OMMConstant.CSV.DELIMETER.toString());
			ContainerSamplesDTO containerSamplesDTO = new ContainerSamplesDTO();
			containerSamplesDTO.setPosition(row[OMMConstant.CSV.POSITION_INDEX.toInteger()]);
			containerSamplesDTO.setAccessioningID(row[OMMConstant.CSV.ACCESSIONING_ID_INDEX.toInteger()]);
			containerSamplesDTO.setContainerID(row[OMMConstant.CSV.CONTAINER_ID_INDEX.toInteger()]);
			containerSamplesDTO.setContainerType(row[OMMConstant.CSV.CONTAINER_TYPE_INDEX.toInteger()]);
			containerSamplesDTOList.add(containerSamplesDTO);
		}
		return containerSamplesDTOList;
	}

	@Override
	public List<ContainerSamples> containerSamplesDTOToObjMapper(List<ContainerSamplesDTO> containerSampleDTOList) {
		List<ContainerSamples> containerSamplesList = new ArrayList<>();
		ContainerSamples containerSample = null;

		for (ContainerSamplesDTO cSample : containerSampleDTOList) {
			containerSample = new ContainerSamples();
			containerSample.setId(cSample.getContainerSampleId());
			containerSample.setPosition(cSample.getPosition());
			containerSample.setAccessioningID(cSample.getAccessioningID());
			containerSample.setContainerID(cSample.getContainerID());
			containerSample.setContainerType(cSample.getContainerType());
			containerSample.setDeviceID(cSample.getDeviceID());
			containerSample.setDeviceRunID(cSample.getDeviceRunID());
			containerSample.setActiveFlag(cSample.getActiveFlag());
			containerSample.setAssayType(cSample.getAssayType());
			containerSample.setUpdatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			containerSample.setUpdatedDateTime((timestamp));
			containerSample.setLoadID(cSample.getLoadID());
			containerSample.setStatus(cSample.getStatus());
			containerSample.setCreatedBy(cSample.getCreatedBy());
			containerSample.setCreatedDateTime(cSample.getCreatedDateTime());
			containerSample.setActiveFlag(cSample.getActiveFlag());
			containerSamplesList.add(containerSample);
		}

		return containerSamplesList;
	}


	@Override
	public List<ContainerSamples> containerSamplesMapperForStatusUpdate(List<ContainerSamples> containerSampleList) {
		List<ContainerSamples> containerSamplesList = new ArrayList<>();
		ContainerSamples cSample = null;

		for (ContainerSamples containerSamples : containerSampleList) {
			cSample = new ContainerSamples();
			cSample.setId(containerSamples.getId());
			cSample.setAccessioningID(containerSamples.getAccessioningID());
			cSample.setActiveFlag(containerSamples.getActiveFlag());
			cSample.setAssayType(containerSamples.getAssayType());
			cSample.setContainerID(containerSamples.getContainerID());
			cSample.setContainerType(containerSamples.getContainerType());
			cSample.setCreatedBy(containerSamples.getCreatedBy());
			cSample.setCreatedDateTime(containerSamples.getCreatedDateTime());
			cSample.setDeviceID(containerSamples.getDeviceID());
			cSample.setDeviceRunID(containerSamples.getDeviceRunID());
			cSample.setLoadID(containerSamples.getLoadID());
			cSample.setPosition(containerSamples.getPosition());
			cSample.setStatus(OMMConstant.CSV.STATUS_SENT_TO_DEVICE.toString());
			cSample.setUpdatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			cSample.setUpdatedDateTime(timestamp);
			containerSamplesList.add(cSample);
		}

		return containerSamplesList;
	}

	@Override
	public List<ContainerSamplesDTO> containerSamplesMapperForObjTODTO(List<Object> containerSamples)
			throws ParseException {
		List<ContainerSamplesDTO> containerSampleDTOs = new ArrayList<>();

		for (Object obj : containerSamples) {
			Object[] sampleArray = (Object[]) obj;
			ContainerSamplesDTO containerSampleDTO = new ContainerSamplesDTO();
			containerSampleDTO.setAccessioningID(sampleArray[0].toString());
			containerSampleDTO.setAssayType(sampleArray[1].toString());
			if (sampleArray[2] != null) {
				containerSampleDTO.setDeviceID(sampleArray[2].toString());
			}
			containerSampleDTO.setContainerID(sampleArray[3].toString());
			containerSampleDTO.setLoadID(Long.parseLong(sampleArray[4].toString()));
			containerSampleDTO.setStatus(sampleArray[5].toString());
			containerSampleDTO.setPosition(sampleArray[6].toString());
			if (sampleArray[7] != null) {
				containerSampleDTO.setDeviceRunID(sampleArray[7].toString());
			}
			containerSampleDTO.setOrderID(Long.parseLong(sampleArray[8].toString()));
			containerSampleDTO.setOrderComments(sampleArray[9].toString());
			containerSampleDTO.setContainerSampleId(Long.parseLong(sampleArray[10].toString()));
			containerSampleDTO.setActiveFlag(sampleArray[11].toString());
			containerSampleDTO.setContainerType(sampleArray[12].toString());
			containerSampleDTO.setCreatedBy(sampleArray[13] != null ? sampleArray[13].toString() : "");
			containerSampleDTO.setCreatedDateTime(convertStringtoDate(sampleArray[14].toString()));
			if (sampleArray[15] != null) {
				containerSampleDTO.setUpdatedBy(sampleArray[15].toString());
			}
			if (sampleArray[16] != null) {
				containerSampleDTO.setUpdatedDateTime(convertStringtoDate(sampleArray[16].toString()));
			}
			containerSampleDTOs.add(containerSampleDTO);
		}

		return containerSampleDTOs;
	}

	public Timestamp convertStringtoDate(String time) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		Date parsedDate = dateFormat.parse(time);

		return new Timestamp(parsedDate.getTime());
	}

	@Override
	public Map<String, List<String>> fileSizevalidation(InputStream in) throws HMTPException, IOException {
		List<String> sizeErrors = new ArrayList<>();
		Map<String, List<String>> rowErrors = new LinkedHashMap<>();
		resourceBundle = resourceBundleUtil.getResourceBundle();
		List<String> rows = null;
		String line = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
		String dateAndTime = sdf.format(date);
		File file = null;
		Path path = null;
		String errorMsg = "Error :";
		file = new File("file" + dateAndTime + ".csv");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			logger.info("fileSizevalidation() -> Start of fileSizevalidation() execution.");
			rows = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				rows.add(line);
			}
			int j = 1;
			int i = rows.size();
			for (String str : rows) {
				j++;
				if (str.trim().contains("Content-Disposition") || str.trim().contains("Content-Type")
						|| str.trim().contains("WebKitFormBoundary")) {
				} else {
					bw.write(str);
					if (j < i) {
						bw.newLine();
					}
				}
			}
			bw.flush();
			long size = file.length();
			path = file.toPath();
			if (size > OMMConstant.CSV.CSV_FILE_SIZE.toInteger()) {
				sizeErrors.add(resourceBundleUtil.getMessages(resourceBundle, "FILE_SIZE_EXCEEDED"));
				rowErrors.put(errorMsg, sizeErrors);
			}
		} catch (Exception exp) {
			List<String> errorMessage = new ArrayList<>();
			errorMessage.add(exp.getMessage());
			rowErrors.put(errorMsg, errorMessage);
		} finally {
			Files.delete(path);
		}
		return rowErrors;
	}

	@Override
	public List<AssayInputDataValidationsDTO> mandatoryFieldValidationByAssay(String assayType)
			throws HMTPException, UnsupportedEncodingException {
		try {
			final String url = RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay",
					"/" + assayType + "/inputdatavalidations?groupName=" + OMMConstant.RequiredFieldValidation.MANDATORY_FLAG.toString(), null);
			Invocation.Builder ammClient = RestClientUtil.getBuilder(URLEncoder.encode(url, utf), null);
			return ammClient.get(new GenericType<List<AssayInputDataValidationsDTO>>() {
			});
		} catch (HMTPException e) {
			logger.error("Error while checking mandatory fields for accessioning id", e.getMessage());
			throw new HMTPException(e);
		}
	}

	@Override
	public List<WorkflowDTO> getInWorkflowOrders() throws HMTPException, UnsupportedEncodingException {
		try {
			final String url = RestClientUtil.getUrlString("pas.rmm_api_url", "",
					"/json/rest/api/v1/runresults/inworkflow", "", null);
			Invocation.Builder rmmClient = RestClientUtil.getBuilder(URLEncoder.encode(url, utf), null);
			return rmmClient.get(new GenericType<List<WorkflowDTO>>() {
			});
		} catch (HMTPException e) {
			logger.error("Error while checking mandatory fields for accessioning id", e.getMessage());
			throw new HMTPException(e);
		}
	}

	@Override
	public boolean getRequiredFieldMissingFlag(OrderParentDTO order) throws HMTPException {
		boolean flag = false;
		List<AssayInputDataValidationsDTO> assayMandatoryFields;

			MandatoryFlagValidationUtil validations = MandatoryFlagValidationUtil.getInstance();
			assayMandatoryFields = validations.get(order.getOrder().getAssayType());
			
			for(AssayInputDataValidationsDTO assayValidation: assayMandatoryFields) {
				if((order.getOrder().getAssay().getMaternalAge()==null && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.MATERNAL_AGE.toString()))
						|| ((order.getOrder().getAssay().getGestationalAgeWeeks()==null || order.getOrder().getAssay().getGestationalAgeDays()==null)&& assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.GESTATIONAL_AGE.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getAssay().getIvfStatus()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.IVF_STATUS.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getAssay().getFetusNumber()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.NUMBER_OF_FETUS.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getPatient().getPatientFirstName()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.FIRST_NAME.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getPatient().getPatientLastName()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.LAST_NAME.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getPatient().getPatientMedicalRecNo()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.MEDICAL_RECORD_NUMBER.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getPatient().getPatientDOB()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.DOB.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getPatient().getRefClinicianName()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.REFERRING_CLINICIAN.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getPatient().getLabId()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.LABORATORY_ID.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getOrderComments()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.COMMENTS.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getPatient().getOtherClinicianName()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.OTHER_CLINICIAN.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getPatient().getClinicName()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.CLINIC_NAME.toString()))		
						|| (StringUtils.isEmpty(order.getOrder().getPatient().getReasonForReferral()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.REASON_FOR_REFERRAL.toString()))
						|| (StringUtils.isEmpty(order.getOrder().getPatient().getAccountNumber()) && assayValidation.getFieldName().equalsIgnoreCase(OMMConstant.RequiredFieldValidation.ACCOUNT_NUMBER.toString())))
							flag = true;
				
				if(flag) {
					break;
				}
			}
		return flag;
	}

}
