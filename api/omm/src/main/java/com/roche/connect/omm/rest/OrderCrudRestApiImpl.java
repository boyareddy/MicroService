/*******************************************************************************
 * File Name: OrderCrudRestApiImpl.java            
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
package com.roche.connect.omm.rest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.BulkOrdersDTO;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderElements;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.common.order.dto.TranslationDTO;
import com.roche.connect.common.order.dto.WorkflowDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.spring.data.OffsetBasedPageRequest;
import com.roche.connect.common.util.DateUtilImpl;
import com.roche.connect.omm.error.CustomErrorCodes;
import com.roche.connect.omm.error.ErrorResponse;
import com.roche.connect.omm.model.ContainerSamples;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.model.TestOptions;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.readrepository.PatientAssayReadRepository;
import com.roche.connect.omm.readrepository.PatientReadRepository;
import com.roche.connect.omm.readrepository.PatientSampleReadRepository;
import com.roche.connect.omm.readrepository.TestOptionsReadRepository;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.specifications.OrderSpecifications;
import com.roche.connect.omm.util.BulkOrderValidationUtil;
import com.roche.connect.omm.util.CSVParserUtil;
import com.roche.connect.omm.util.OMMConstant;
import com.roche.connect.omm.util.OMMConstant.CSV;
import com.roche.connect.omm.util.ResourceBundleUtil;
import com.roche.connect.omm.util.ValidationUtil;
import com.roche.connect.omm.writerepository.ContainerSamplesWriteRepository;
import com.roche.connect.omm.writerepository.OrderWriteRepository;
import com.roche.connect.omm.writerepository.PatientAssayWriteRepository;
import com.roche.connect.omm.writerepository.PatientSampleWriteRepository;
import com.roche.connect.omm.writerepository.PatientWriteRepository;
import com.roche.connect.omm.writerepository.TestOptionsWriteRepository;


@Component
public class OrderCrudRestApiImpl implements OrderCrudRestApi {
	@Autowired
	OrderWriteRepository orderWriteRepository;
	@Autowired
	PatientReadRepository patientReadRepository;
	@Autowired
	PatientWriteRepository patientWriteRepository;
	@Autowired
	PatientSampleWriteRepository patientSampleWriteRepository;
	@Autowired
	PatientAssayReadRepository patientAssayReadRepository;
	@Autowired
	PatientAssayWriteRepository patientAssayWriteRepository;
	@Autowired
	PatientSampleReadRepository patientSampleReadRepository;
	@Autowired
	OrderReadRepository orderReadRepository;
	@Autowired
	TestOptionsWriteRepository testOptionsWriteRepository;
	@Autowired
	TestOptionsReadRepository testOptionsReadRepository;
	@Autowired
	ContainerSamplesReadRepository containerSamplesReadRepository;
	@Autowired
	ContainerSamplesWriteRepository containerSamplesWriteRepository;
	@Autowired
	OrderService orderService;
	@Autowired
	private BulkOrderValidationUtil bulkOrderValidationUtil;
	@Autowired
	ResourceBundleUtil resourceBundleUtil;
	ResourceBundle resourceBundle = null;
	DateUtilImpl dateUtil = new DateUtilImpl();
	ValidationUtil validateUtil = new ValidationUtil();
	CSVParserUtil csvParserUtil = new CSVParserUtil();
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	@Value("${connect.ordersThresholdSize}")
	private long ordersThresholdSize;
	@Value("${connect.uploadedBulkOrderLocation}")
	private String uploadedBulkOrderLocation;
	@Value("${connect.fileMemorySize}")
	private long fileMemorySize;
	@Value("${connect.phi_encrypt}")
	Boolean phiEncrypt;
	private Map<String, Object> totalMap = null;
	private static Lock lockObj = new ReentrantLock();

	/**
	 * Creates the order.
	 *
	 * @param requestBody
	 *            the request body
	 * @return Response
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Override
	public Response createOrder(@RequestBody String requestBody) throws HMTPException {
		try {
			logger.info("createOrder() -> Start of createOrder Execution");
			ObjectMapper mapper = new ObjectMapper();
			OrderParentDTO orderParent = mapper.readValue(requestBody, OrderParentDTO.class);
			boolean isAccessioningIdDuplicate = orderService.isAccessioningDuplicate(orderParent);
			if (isAccessioningIdDuplicate) {
				throw new HMTPException(CustomErrorCodes.ACCESSIONING_ID_DUPLICATE,
						OMMConstant.ErrorMessages.ACCESSIONING_ID_DUPLICATE.toString());
			}
			validateUtil.patientNameValiadtion(orderParent);
			/*if (!validateUtil.isValidDate(orderParent.getOrder().getAssay().getCollectionDate(),
					orderParent.getOrder().getAssay().getReceivedDate())) {
				throw new HMTPException(CustomErrorCodes.INVALID_COLLECTION_DATE,
						OMMConstant.ErrorMessages.INVALID_COLLECTION_DATE.toString());
			}
			if (!validateUtil.isValidName(orderParent.getOrder().getPatient().getPatientFirstName())) {
				throw new HMTPException(CustomErrorCodes.INVALID_FIRST_NAME_FORMAT,
						OMMConstant.ErrorMessages.INVALID_FIRST_NAME_FORMAT.toString());
			}
			if (!validateUtil.isValidName(orderParent.getOrder().getPatient().getPatientLastName())) {
				throw new HMTPException(CustomErrorCodes.INVALID_LAST_NAME_FORMAT,
						OMMConstant.ErrorMessages.INVALID_LAST_NAME_FORMAT.toString());
			}

			if (!validateUtil.isValidName(orderParent.getOrder().getPatient().getOtherClinicianName())) {
				throw new HMTPException(CustomErrorCodes.INVALID_OTHER_CLINICIAN_NAME_FORMAT,
						OMMConstant.ErrorMessages.INVALID_OTHER_CLINICIAN_NAME_FORMAT.toString());
			}
			if (!validateUtil.isValidName(orderParent.getOrder().getPatient().getClinicName())) {
				throw new HMTPException(CustomErrorCodes.INVALID_REF_CLINICIAN_CLINIC_NAME_FORMAT,
						OMMConstant.ErrorMessages.INVALID_REF_CLINICIAN_CLINIC_NAME_FORMAT.toString());
			}
			if (!validateUtil.isValidName(orderParent.getOrder().getPatient().getRefClinicianName())) {
				throw new HMTPException(CustomErrorCodes.INVALID_REF_CLINICIAN_NAME_FORMAT,
						OMMConstant.ErrorMessages.INVALID_REF_CLINICIAN_NAME_FORMAT.toString());
			}*/
			/**if (!validateUtil.isValidName(orderParent.getOrder().getPatient().getTreatingDoctorName())) {
				throw new HMTPException(CustomErrorCodes.INVALID_TREATING_DOCTOR_NAME_FORMAT,
						OMMConstant.ErrorMessages.INVALID_TREATING_DOCTOR_NAME_FORMAT.toString());
			}*/
			boolean validateOrderParent = validateUtil.validateOrderParentDetails(orderParent);
			if (!validateOrderParent) {
				throw new HMTPException("Validation failed");
			}
			Patient patient = orderService.createPatientObject(orderParent);
			PatientAssay patientAssayObj = orderService.createPatientAssayObject(orderParent);
			List<PatientSamples> patientSamplesList = orderService.createPatientSamplesObject(orderParent);
			Order order = orderService.createOrderObject(orderParent);
			List<TestOptions> listOfTestOptions = orderService.createTestOptionsObject(orderParent);

			// passing the objects as per their relationship
			patientAssayObj.setPatient(patient);
			if (patientSamplesList.size() > 1) {
				patientSamplesList.get(OMMConstant.FIRST_SAMPLE.toInteger()).setPatient(patient);
				patientSamplesList.get(OMMConstant.SECOND_SAMPLE.toInteger()).setPatient(patient);
			} else {
				patientSamplesList.get(OMMConstant.FIRST_SAMPLE.toInteger()).setPatient(patient);
			}
			order.setPatient(patient);
			for (int i = 0; i < listOfTestOptions.size(); i++) {
				listOfTestOptions.get(i).setOrder(order);
			}

			// saving the above objects in DB
			PatientSamples patientSampleOne;
			patientWriteRepository.save(patient);
			patientAssayWriteRepository.save(patientAssayObj);
			if (patientSamplesList.size() > 1) {
				patientSampleOne = patientSampleWriteRepository
						.save(patientSamplesList.get(OMMConstant.FIRST_SAMPLE.toInteger()));
				patientSampleWriteRepository.save(patientSamplesList.get(OMMConstant.SECOND_SAMPLE.toInteger()));
			} else {
				patientSampleOne = patientSampleWriteRepository
						.save(patientSamplesList.get(OMMConstant.FIRST_SAMPLE.toInteger()));
			}
			if (patientSamplesList.isEmpty() == Boolean.FALSE) {
				order.setPatientSampleId(patientSampleOne.getId());
			}
			order.setReqFieldMissingFlag(orderService.getRequiredFieldMissingFlag(orderParent));
			Order orderInstanceReturned = orderWriteRepository.save(order);
			for (int i = 0; i < listOfTestOptions.size(); i++) {
				testOptionsWriteRepository.save(listOfTestOptions.get(i));
			}
			logger.info("End of createOrder() execution");
			return Response.status(200).entity(orderInstanceReturned.getId()).build();

		} catch (HMTPException exp) {
			logger.error("Error while creating the order : " + exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();

		} catch (Exception exp) {
			String[] string;
			logger.error("Error while creating the order : " + exp.getMessage());
			string = exp.getMessage().split("at");
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setErrorStatusCode(CustomErrorCodes.INVALID_JSON_FORMAT.getErrorCode());
			errorResponse.setErrorMessage(string[0]);
			return Response.status(errorResponse.getErrorStatusCode()).entity(errorResponse).build();

		}
	}

	/**
	 * Gets the order list.
	 *
	 * @return Response
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Response getOrderList() throws HMTPException {
		List<Map> ordersMap = null;
		long domainId = 0;
		try {
			logger.info("getOrderList() -> Start of getOrderList Execution");
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			// changes for multitenancy
			ordersMap = orderReadRepository.findOrders(domainId);
			if (!ordersMap.isEmpty()) {
				logger.info("End of getOrderList() execution");
				return Response.ok(200).entity(orderService.convertMapToOrders(ordersMap)).build();
			} else {
				throw new HMTPException(CustomErrorCodes.ORDER_NOT_FOUND,
						OMMConstant.ErrorMessages.UNASSIGNED_ORDERS_NOT_FOUND.toString());
			}

		} catch (HMTPException exp) {
			logger.error("Error while getting the order : ", exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();

		} catch (Exception exp) {
			logger.error("Error while getting the order : " + exp.getMessage());
			throw new HMTPException(exp);
		}

	}


	/**
	 * Gets the unassigned order list as Per Pagination.
	 *
	 * @return Response
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Override
	public Response getOrderLists(int offset, int limit, String assayType,String sampleType, String createdDateTime,
			String updatedDateTime,String orderComments, String reqFieldMissingFlag ) throws HMTPException {
		OrderElements orderElements=new OrderElements();
		List<OrderDTO> orderDtolist=new ArrayList<>();

		try {
			logger.info("getOrderLists Unassigned() -> Start of getOrderList Execution");
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			int pageNumber = 1;
			if (limit != 0) {
				pageNumber = (offset / limit) + 1;
			}
			pageNumber = pageNumber < 1 ? 1 : pageNumber;
			Pageable pageable = new PageRequest(pageNumber - 1, limit, Direction.DESC, "updatedDateTime");
			Specification<Order> orderResultSpecifications = OrderSpecifications.getOrderResults("unassigned",assayType,
					createdDateTime, updatedDateTime, orderComments, reqFieldMissingFlag,domainId);
			//Page<Order> page = orderReadRepository.findAll(orderResultSpecifications, pageable);
			Page<Order> page =orderReadRepository.findAll(orderResultSpecifications, pageable);
			if (!page.getContent().isEmpty()) {
				page.getContent().forEach(e -> {
					OrderDTO orderDTO = new OrderDTO();
					orderDTO = orderService.createOrderDTO(e);
					orderDtolist.add(orderDTO);
				});
				orderElements.setTotalElements(page.getTotalElements());
				orderElements.setOrders(orderDtolist);
			} else {
				throw new HMTPException(CustomErrorCodes.ORDER_NOT_FOUND,
						OMMConstant.ErrorMessages.UNASSIGNED_ORDERS_NOT_FOUND.toString());
			}
			logger.info("End of getOrderLists Unassigned()");
			return Response.ok(200).entity(orderElements).build();

		} catch (HMTPException exp) {
			logger.error("Error while getting the getOrderLists : ", exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();

		} catch (Exception exp) {
			logger.error("Error while getting the getOrderLists : " + exp.getMessage());
			throw new HMTPException(exp);
		}


	}


	/**
	 * Gets the order by sample id.
	 *
	 * @param accessioningID
	 *            the accessioning ID
	 * @return the order by sample id
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Override
	public Response getOrderBySampleId(String accessioningID) throws HMTPException {
		List<Order> ord = null;
		List<PatientSamples> patientsamples = null;
		logger.info("getOrderBySampleId() -> Start of getOrderBySampleId Execution");
		List<OrderDTO> orderDTOs = new ArrayList<>();
		try {
			if (accessioningID != null) {
				ord = orderReadRepository.findOrderDetailsByAccessioningId(accessioningID);
				patientsamples = patientSampleReadRepository.findPatientDetailsBySampleId(accessioningID);
				if (!ord.isEmpty() && !patientsamples.isEmpty()) {
					for (Order order : ord) {
						logger.info("OrderID::" + order.getId());
						OrderDTO orderDTO = orderService.createOrderDTO(order);
						orderDTO.setAssay(orderService.createAssayDTO(order));
						orderDTO.getAssay().setTestOptions(orderService.createTestOptionsDTO(order));
						orderDTO.setPatient(orderService.createPatientDTO(order));
						orderDTOs.add(orderDTO);
					}
				}
			} else {
				throw new HMTPException(CustomErrorCodes.ACCESSIONING_ID_NULL,
						OMMConstant.ErrorMessages.ACCESSIONING_ID_NULL.toString());
			}
			if (orderDTOs.isEmpty()) {
				throw new HMTPException(CustomErrorCodes.ORDER_NOT_FOUND,
						OMMConstant.ErrorMessages.ORDER_NOT_FOUND.toString());
			}

			logger.info("End of getOrderBySampleId() execution");
			return Response.ok(200).entity(orderDTOs).build();

		} catch (HMTPException exp) {
			logger.error("Error While getting the order details : " + exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exp) {
			logger.error("Error While getting the order details : " + exp.getMessage());
			throw new HMTPException(exp);

		}
	}

	/**
	 * Gets the order by order id.
	 *
	 * @param orderId
	 *            the order id
	 * @return the order by order id
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Override
	public Response getOrderByOrderId(Long orderId) throws HMTPException {
		try {
			logger.info("getOrderByOrderId() -> Start of getOrderByOrderId Execution");
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			// changes for multitenancy
			Order order = orderReadRepository.findOrderByOrderId(orderId, domainId);
			if (order != null) {
				OrderParentDTO orderParentDTO = orderService.createOrderParentDTO(order);
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
				String orderString = mapper.writeValueAsString(orderParentDTO);
				logger.info("End of getOrderByOrderId() execution");
				return Response.ok(200).entity(orderString).build();
			} else {
				throw new HMTPException(CustomErrorCodes.ORDER_NOT_FOUND,
						OMMConstant.ErrorMessages.ORDER_NOT_FOUND.toString());
			}

		} catch (HMTPException exp) {
			logger.error("Error While getting the orders : " + orderId, exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exp) {
			logger.error("Error while getting Order details along with parent child relationship: " + exp.getMessage());
			throw new HMTPException(exp);
		}
	}

	/**
	 * Update.
	 *
	 * @param requestBody
	 *            the request body
	 * @return Response
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Override
	public Response update(String requestBody) throws HMTPException {

		try {
			logger.info("update() -> Start of update() Execution");
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			ObjectMapper objectMapper = new ObjectMapper();
			OrderParentDTO orderParentDTO = objectMapper.readValue(requestBody, OrderParentDTO.class);
			OrderDTO orderDTO = orderParentDTO.getOrder();
			AssayDTO assayDTO = orderDTO.getAssay();
			PatientDTO patientDTO = orderDTO.getPatient();

			boolean validateOrderParent = validateUtil.validateOrderParentDetails(orderParentDTO);
			if (!validateOrderParent) {
				throw new HMTPException("Validation failed");
			}
			validateUtil.patientNameValiadtion(orderParentDTO);
			logger.info("Updating the Order Info: " + orderDTO.getOrderId());
			if (orderDTO.getOrderId() == null)
				throw new HMTPException(CustomErrorCodes.ORDER_ID_NULL,
						OMMConstant.ErrorMessages.ORDER_ID_NULL.toString());

			/*if (!validateUtil.isValidDate(orderParentDTO.getOrder().getAssay().getCollectionDate(),
					orderParentDTO.getOrder().getAssay().getReceivedDate())) {
				throw new HMTPException(CustomErrorCodes.INVALID_COLLECTION_DATE,
						OMMConstant.ErrorMessages.INVALID_COLLECTION_DATE.toString());
			}*/

			Order order = orderReadRepository.findOne(orderDTO.getOrderId());
			boolean assayTypeCheck = false;
			if (order.getAssayType().equals(orderDTO.getAssayType())) {
				assayTypeCheck = true;
			}
			long ownerId = order.getCompany().getId();
			if (domainId == ownerId) {
				if (order != null) {
					order = orderService.orderUpdateMapper(orderDTO, order);
					List<PatientSamples> patientSamples = (List<PatientSamples>) order.getPatient().getPatientSamples();
					if (orderDTO.isRetestSample()) {
						for (PatientSamples ptsample : patientSamples) {
							ptsample.setActiveFlag(OMMConstant.ACTIVE_FLAG_YES.toString());
							ptsample.setSampleType(orderDTO.getSampleType());
							ptsample.setSampleId(orderDTO.getAccessioningId());
							ptsample.setUpdatedDateTime(dateUtil.getCurrentUTCTimeStamp());
							// changes for multitenancy
							patientSampleWriteRepository.save(ptsample, ownerId);
						}
					} else {
						if (patientSamples.size() > 1) {
							patientSamples.get(OMMConstant.FIRST_SAMPLE.toInteger())
							.setActiveFlag(OMMConstant.ACTIVE_FLAG_YES.toString());
							patientSamples.get(OMMConstant.FIRST_SAMPLE.toInteger())
							.setSampleType(orderDTO.getSampleType());
							patientSamples.get(OMMConstant.FIRST_SAMPLE.toInteger())
							.setSampleId(orderDTO.getAccessioningId());
							patientSamples.get(OMMConstant.FIRST_SAMPLE.toInteger())
							.setUpdatedDateTime(dateUtil.getCurrentUTCTimeStamp());

							patientSamples.get(OMMConstant.SECOND_SAMPLE.toInteger())
							.setActiveFlag(OMMConstant.ACTIVE_FLAG_NO.toString());
							patientSamples.get(OMMConstant.SECOND_SAMPLE.toInteger())
							.setSampleType(orderDTO.getSampleType());
							patientSamples.get(OMMConstant.SECOND_SAMPLE.toInteger())
							.setSampleId(orderDTO.getAccessioningId());
							patientSamples.get(OMMConstant.SECOND_SAMPLE.toInteger())
							.setUpdatedDateTime(dateUtil.getCurrentUTCTimeStamp());
						}

					}
					// changes for multitenancy
					order.setReqFieldMissingFlag(orderService.getRequiredFieldMissingFlag(orderParentDTO));
					orderWriteRepository.save(order, ownerId);

					logger.info("Updating the PatientAssayDetails Info: " + orderDTO.getOrderId());
					Long patientId = orderReadRepository.findPatientIdByOrderId(orderDTO.getOrderId());
					PatientAssay assayToUpdate = patientAssayReadRepository.findByPatient(patientId);

					// to check whether the patient assay details has been fetched properly based on patient id
					assayToUpdate = orderService.patientAssayMapper(assayToUpdate, assayDTO);
					// changes for multitenancy
					patientAssayWriteRepository.save(assayToUpdate, ownerId);
					Map<String, Boolean> testOptionsDTO = assayDTO.getTestOptions();

					List<TestOptions> testDBValues = testOptionsReadRepository
							.findTestOptionDetailsByOrderId(orderDTO.getOrderId());

					if (assayTypeCheck) {
						for (TestOptions testDBValue : testDBValues) {
							if (testOptionsDTO.keySet().contains(testDBValue.getTestId())) {
								if (testOptionsDTO.get(testDBValue.getTestId()) == OMMConstant.TRUE_FLAG.isBoolVal())
									testDBValue.setActiveFlag(OMMConstant.ACTIVE_FLAG_YES.toString());
								else
									testDBValue.setActiveFlag(OMMConstant.ACTIVE_FLAG_NO.toString());
								testDBValue.setUpdatedDateTime(dateUtil.getCurrentUTCTimeStamp());
								testDBValue.setOrder(order);
								// changes for multitenancy
								testOptionsWriteRepository.save(testDBValue, ownerId);
							}
						}
					} else {
						testOptionsWriteRepository.deleteByOrderId(orderDTO.getOrderId(), ownerId);
						Map<String, Boolean> testOptions = orderParentDTO.getOrder().getAssay().getTestOptions();
						List<TestOptions> listOfTestOptions = new ArrayList<>();
						for (Entry<String, Boolean> test : testOptions.entrySet()) {
							TestOptions testOption = new TestOptions();
							testOption.setTestId(test.getKey());
							testOption.setOrder(order);
							if (testOptions.get(test.getKey()).equals(OMMConstant.TRUE_FLAG.isBoolVal())) {
								testOption.setActiveFlag(OMMConstant.ACTIVE_FLAG_YES.toString());
								listOfTestOptions.add(testOption);
							} else {
								testOption.setActiveFlag(OMMConstant.ACTIVE_FLAG_NO.toString());
								listOfTestOptions.add(testOption);
							}
						}
						for (int i = 0; i < listOfTestOptions.size(); i++) {
							testOptionsWriteRepository.save(listOfTestOptions.get(i));
						}
					}

					Patient updatePatient = order.getPatient();

					logger.info("Updating the PatientDetails Info-patientId:   " + patientId);
					updatePatient = orderService.patientUpdateMapper(updatePatient, patientDTO);
					updatePatient.setId(patientId);

					patientWriteRepository.save(updatePatient, ownerId);
					logger.info("Assay,Order & Patient Update success:patientID" + patientId + "Orderid"
							+ orderDTO.getOrderId());
					String successMessage = "Order " + orderDTO.getOrderId() + " updated successfully..";
					logger.info("End of update() execution");
					return Response.status(200).entity(successMessage).build();
				} else {
					throw new HMTPException(CustomErrorCodes.ORDER_NOT_FOUND,
							OMMConstant.ErrorMessages.ORDER_NOT_FOUND.toString());
				}
			} else {
				throw new HMTPException(CustomErrorCodes.OWNER_NOT_VALID,
						OMMConstant.ErrorMessages.OWNER_NOT_VALID.toString());
			}
		} catch (HMTPException exp) {
			logger.error("Error While Updating the order Information : ", exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch(JsonMappingException jsonMappingException){
			logger.error("JsonMappingError While Updating the Order Information: ", jsonMappingException.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE.getErrorCode());
			errResponse.setErrorMessage(OMMConstant.ErrorMessages.VALIDATION_IN_RETEST_ERR_MESSAGE.toString());
			return Response.status(errResponse.getErrorStatusCode()).entity(errResponse).build();
		}catch(DataIntegrityViolationException exp) {
			logger.error("DataIntegrityError While Updating the Order Information: ", exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE.getErrorCode());
			errResponse.setErrorMessage(OMMConstant.ErrorMessages.ACCESSIONING_ID_DUPLICATE.toString());
			return Response.status(errResponse.getErrorStatusCode()).entity(errResponse).build();           
		}catch (Exception exp) {
			logger.error("Error While Updating the Order Information: ", exp.getMessage());
			throw new HMTPException(exp);
		}

	}

	/**
	 * Update order status.
	 *
	 * @param orderId
	 *            the order id
	 * @param orderStatus
	 *            the order status
	 * @return Response
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Override
	public Response updateOrderStatus(Long orderId, String orderStatus) throws HMTPException {
		try {
			logger.info("updateOrderStatus() -> Start of updateOrderStatus Execution");
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			if (orderId == null) {
				throw new HMTPException(CustomErrorCodes.ORDER_ID_NULL,
						OMMConstant.ErrorMessages.ORDER_ID_NULL.toString());
			}
			if (orderStatus == null || orderStatus.isEmpty()) {
				throw new HMTPException(CustomErrorCodes.ORDER_STATUS_NULL,
						OMMConstant.ErrorMessages.ORDER_STATUS_NULL.toString());
			}
			// changes for multitenancy
			// Order order = orderReadRepository.findOne(orderId);
			Order order = orderReadRepository.findOrderByOrderId(orderId, domainId);
			if (order != null) {
				order.setOrderStatus(orderStatus);
				order.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
				orderWriteRepository.save(order);
				logger.info("End of updateOrderStatus() execution");
				return Response.status(200).entity("Order Status updated successfully for Order Id :- " + orderId)
						.build();
			} else {
				throw new HMTPException(CustomErrorCodes.ORDER_NOT_FOUND,
						OMMConstant.ErrorMessages.ORDER_NOT_FOUND.toString());
			}

		} catch (HMTPException exp) {
			logger.error("Error While Updating the Order Status: " + exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exp) {
			logger.error("Error While Updating the Order Status: " + exp.getMessage());
			throw new HMTPException(exp);
		}
	}

	/**
	 * Gets the workflow status details.
	 *
	 * @param accessioningId
	 *            the accessioning id
	 * @return the workflow status details
	 * @throws HMTPException
	 *             the HMTP exception
	 */

	/**
	 * Gets the order work flow list.
	 *
	 * @return Response
	 * @throws HMTPException
	 *             the HMTP exception
	 */

	@Override
	public Response getContainerSamples(String deviceRunId, String containerID) throws HMTPException {
		List<ContainerSamplesDTO> containerSamplesDTO = null;
		List<Map<String, Object>> containers = null;
		long domainId;
		List<Object> conSamples = null;

		try {
			logger.info("getContainerSamples() -> start of getContainerSamples() execution");
			containerSamplesDTO = new ArrayList<>();
			conSamples = new ArrayList<>();
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			if (deviceRunId != null && containerID == null) {
				conSamples = containerSamplesReadRepository.findAllContainerSamplesByDeviceRunID(deviceRunId, domainId);
			} else if (containerID != null && deviceRunId == null) {
				conSamples = containerSamplesReadRepository.findconatinersetsamplebyconatinerid(containerID, domainId);
			} else {
				containers = containerSamplesReadRepository.findContainerIdByStatus(domainId);
				if (!containers.isEmpty()) {
					Map<String, Object> containerMap = containers.get(0);
					String containerId = (String) containerMap.get("containerID");
					conSamples = containerSamplesReadRepository.findAllContainerSamples(domainId, containerId);
				}
			}
			if (conSamples != null) {
				containerSamplesDTO = orderService.containerSamplesMapperForObjTODTO(conSamples);
			}
			logger.info("End of getContainerSamples() execution");
			return Response.ok(200).entity(containerSamplesDTO).build();
		} catch (Exception exp) {
			logger.error("Error while fetching the container samples : " + exp.getMessage());
			throw new HMTPException(exp);
		}
	}


	@Override
	public Response updateContainerSamples(String requestBody, String deviceRunId, String accessioningID, String status)
			throws HMTPException {
		List<ContainerSamples> containerSampleList = null;
		ObjectMapper mapper = null;
		List<ContainerSamples> containerSampleListToUpdateStatus = new LinkedList<>();
		long domainId;
		try {
			logger.info("updateContainerSamples() -> Start of updateContainerSamples() execution.");
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			if (accessioningID != null && !accessioningID.isEmpty() && status != null && !status.isEmpty()) {
				containerSampleListToUpdateStatus = containerSamplesReadRepository
						.findContainerSamplesByAccessioningId(accessioningID);
				if (!containerSampleListToUpdateStatus.isEmpty()) {
					long ownerId = containerSampleListToUpdateStatus.get(0).getCompany().getId();
					if (domainId == ownerId) {
						containerSampleListToUpdateStatus.stream().forEach(p -> {
							if (status.equalsIgnoreCase(OMMConstant.CSV.STATUS_PROCESSED.toString()))
								p.setStatus(status);
							containerSamplesWriteRepository.save(p, ownerId);
						});
					}
				}
			} else if (requestBody.isEmpty() && deviceRunId != null) {

				containerSampleList = containerSamplesReadRepository.findAllContainerSamplesByDeviceRun(deviceRunId,
						domainId);
				if (!containerSampleList.isEmpty()) {
					long ownerId = containerSampleList.get(0).getCompany().getId();
					if (domainId == ownerId) {
						containerSampleList = orderService.containerSamplesMapperForStatusUpdate(containerSampleList);
						containerSamplesWriteRepository.save(containerSampleList, ownerId);
					} else {
						throw new HMTPException(CustomErrorCodes.OWNER_NOT_VALID,
								OMMConstant.ErrorMessages.OWNER_NOT_VALID.toString());
					}
				}
			} else if (deviceRunId == null) {
				mapper = new ObjectMapper();
				TypeReference<List<ContainerSamplesDTO>> ref = new TypeReference<List<ContainerSamplesDTO>>() {
				};
				List<ContainerSamplesDTO> containerSamplesDTO = mapper.readValue(requestBody, ref);
				if (containerSamplesDTO != null) {
					ContainerSamples contSample = containerSamplesReadRepository
							.findOne(containerSamplesDTO.get(0).getContainerSampleId());
					long ownerId = contSample.getCompany().getId();

					if (domainId == ownerId) {
						containerSampleList = orderService.containerSamplesDTOToObjMapper(containerSamplesDTO);
						containerSamplesWriteRepository.save(containerSampleList, ownerId);
					} else {
						throw new HMTPException(CustomErrorCodes.OWNER_NOT_VALID,
								OMMConstant.ErrorMessages.OWNER_NOT_VALID.toString());
					}
				} else {
					throw new HMTPException("Incorrect input for updating the container samples");
				}

			}

		} catch (Exception exception) {
			logger.error("Error while updating the container samples : " + exception.getMessage());
			throw new HMTPException(exception);
		}

		logger.info("End of updateContainerSamples() execution.");
		return Response.ok(200).entity("container samples updated successfully..").build();
	}

	@Override
	public Response validateContainerSamples(InputStream in) throws HMTPException, IOException {
		Map<String, List<String>> errors = null;
		List<String> parsedCSV;
		List<ContainerSamplesDTO> containerSamplesDTO;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			logger.info("validateContainerSamples() -> start of validateContainerSamples() execution");

			org.apache.commons.io.IOUtils.copy(in, baos);
			byte[] bytes = baos.toByteArray();
			ByteArrayInputStream baissize = new ByteArrayInputStream(bytes);
			ByteArrayInputStream baisparsecsv = new ByteArrayInputStream(bytes);
			errors = orderService.fileSizevalidation(baissize);
			if (!errors.isEmpty()) {
				return Response.status(400).entity(errors).build();
			}
			parsedCSV = csvParserUtil.parseCSV(baisparsecsv);
			errors = orderService.validateCSV(parsedCSV);
			if (!errors.isEmpty())
				return Response.status(400).entity(errors).build();
			else {
				containerSamplesDTO = orderService.mapInputCSVToDTO(parsedCSV);
				return Response.ok(200).entity(containerSamplesDTO).build();
			}
		} catch (Exception exception) {
			logger.error("Error while validating the container samples : " + exception.getMessage());
			throw new HMTPException(exception);
		} finally {
			in.close();
		}
	}

	@Override
	public Response storeContainerSamples(@RequestBody String requestBody) throws HMTPException {
		ContainerSamples containerSample = null;
		List<ContainerSamples> containerSampleList = null;
		List<ContainerSamples> returnValueAfterSave = null;
		ObjectMapper mapper = null;
		long domainId;
		List<Map<String, Object>> loadIds = null;
		long loadId = 1L;
		String containerID = null;

		try {
			logger.info("storeContainerSamples() -> Start of storeContainerSamples() execution.");
			mapper = new ObjectMapper();
			TypeReference<List<ContainerSamplesDTO>> ref = new TypeReference<List<ContainerSamplesDTO>>() {
			};
			List<ContainerSamplesDTO> containerSamplesDTO = mapper.readValue(requestBody, ref);
			containerID = containerSamplesDTO.get(0).getContainerID();
			containerSampleList = new ArrayList<>();
			lockObj.lock();
			Thread.sleep(2000);
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			loadIds = containerSamplesReadRepository.findHighestLoadIdByContainer(domainId);
			if (orderService.isCSVJsonValid(containerSamplesDTO)) {
				for (ContainerSamplesDTO cSample : containerSamplesDTO) {
					containerSample = new ContainerSamples();
					containerSample.setPosition(cSample.getPosition());
					containerSample.setAccessioningID(cSample.getAccessioningID());
					containerSample.setContainerID(cSample.getContainerID());
					containerSample.setContainerType(cSample.getContainerType());
					containerSample.setDeviceID(cSample.getDeviceID());
					containerSample.setActiveFlag(OMMConstant.CSV.ACTIVE_FLAG.toString());
					containerSample.setAssayType(OMMConstant.CSV.NIPTDPCR.toString());
					containerSample.setCreatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					containerSample.setCreatedDateTime(timestamp);
					for (Map<String, Object> loadIdMap : loadIds) {
						if (loadIdMap.get("containerID").toString().equalsIgnoreCase(cSample.getContainerID())) {
							loadId = Long.parseLong(loadIdMap.get("loadID").toString());
							loadId = loadId + 1;
							break;
						}
					}
					containerSample.setLoadID(loadId);
					containerSample.setStatus(OMMConstant.CSV.STATUS_OPEN.toString());
					containerSampleList.add(containerSample);
					loadId = 1L;
				}
				returnValueAfterSave = containerSamplesWriteRepository.save(containerSampleList);
				if(returnValueAfterSave==null)
					logger.error("Error while saving the container samples");
			} else {
				throw new HMTPException("CSV is not Valid");
			}
		} catch (Exception exception) {
			logger.error("Error while saving the container samples : " + exception.getMessage());
			throw new HMTPException(exception);
		} finally {
			lockObj.unlock();
		}

		logger.info("End of storeContainerSamples() execution.");
		List<ContainerSamples> cs = containerSamplesReadRepository.findByContainerIDandActiveFlag(containerID,
				OMMConstant.CSV.INACTIVE_FLAG.toString(), domainId);
		if (!cs.isEmpty()) {
			resourceBundle = resourceBundleUtil.getResourceBundle();
			String message = resourceBundleUtil.getMessages(resourceBundle,"NEW_MAPPING_CREATED_MSG", containerID);
			return Response.ok(204).entity(message).build();
		}
		return Response.ok(200).entity("CSV file successfully saved").build();
	}

	@Override
	public Response getListOfContainerIdWithOpenStatus(String status) throws HMTPException {
		List<Map<Object, Object>> list = null;
		logger.info("getListContainerIdWithOpenStatus() -> start of getListContainerIdWithOpenStatus() execution");
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			if (StringUtils.isEmpty(status)) {
				throw new HMTPException(CustomErrorCodes.ORDER_STATUS_NULL,
						OMMConstant.ErrorMessages.STATUS_NULL.toString());
			}
			String[] statusList = status.split(",");
			list = containerSamplesReadRepository.findAllContainerIdWithOpenStatus(statusList, domainId);
			logger.info("End of getListContainerIdWithOpenStatus() execution");
			return Response.ok(list).build();

		} catch (HMTPException exp) {
			logger.error("Error While get listofContainerId with open Status: " + exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception e) {
			logger.error("Error while validating the container samples : " + e.getMessage());
			throw new HMTPException(e);
		}
	}

	@Override
	public Response duplicate(String accessioningID) throws HMTPException {
		OrderDTO orderdto = new OrderDTO();
		orderdto.setAccessioningId(accessioningID);
		OrderParentDTO orderParent = new OrderParentDTO();
		orderParent.setOrder(orderdto);
		boolean isAccessioningIdDuplicate = orderService.isAccessioningDuplicate(orderParent);
		return Response.ok(isAccessioningIdDuplicate).build();
	}

	@Override
	public Response getUnassignedOrderCount() throws HMTPException {
		logger.info("getUnassignedOrderCount() --> Start of getUnassignedOrderCount() execution");
		Long count = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			count = orderReadRepository.findUnassignedOrdersCount(domainId);
			logger.info("getUnassignedOrderCount() --> End of getUnassignedOrderCount() execution");
			return Response.ok(count).build();
		} catch (Exception e) {
			logger.error("Error while getting the count of unassigned orders : " + e.getMessage());
			throw new HMTPException(e);
		}
	}

	@Override
	public Response containerInfoForAccessioningId(String accessioningId) throws HMTPException {
		boolean isAccessioningIdPresent = false;
		long domainId = 0;
		try {
			logger.info("containerInfoForAccessioningId() -> start of containerInfoForAccessioningId() execution");
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			List<Map<Object, Object>> containerSamples = containerSamplesReadRepository
					.findSampleByAccessioningId(accessioningId, domainId);
			if (!containerSamples.isEmpty()) {
				isAccessioningIdPresent = true;
			}
			logger.info("End of containerInfoForAccessioningId() execution");
			return Response.ok(isAccessioningIdPresent).build();
		} catch (Exception e) {
			logger.info("Error while fetching the container samples for the given accessioning id");
			throw new HMTPException(e);
		}
	}

	@Override
	public Response removeContainerSamples(String containerId) throws HMTPException {
		long domainId = 0;
		try {
			if (containerId != null) {
				domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
				List<ContainerSamples> containers = containerSamplesReadRepository.findByIdAndContainerIDAndStatus(
						containerId, OMMConstant.CSV.STATUS_PROCESSED.toString(), domainId);
				if (!containers.isEmpty()) {
					throw new HMTPException(CustomErrorCodes.INVALID_CONTAINER_ID,
							OMMConstant.ErrorMessages.INVALID_DELETION_OF_CONTAINER_SAMPLES_MAPPING.toString());
				}
				containerSamplesWriteRepository.deleteByContainerID(containerId);
			}
		} catch (Exception e) {
			logger.info("Error while removing container samples for given container id");
			throw new HMTPException(e);

		}
		return Response.ok().build();
	}

	@Override
	public Response validateBulkOrders(InputStream inputStream, String timeZone) throws HMTPException {
		OrderParentDTO orderParentDTO = null;
		Iterable<CSVRecord> records = null;
		Iterable<CSVRecord> recordSize = null;
		Reader reader = null;
		BufferedReader br = null;
		BufferedReader brTemp = null;
		FileOutputStream fileOutputStream = null;
		List<OrderParentDTO> orderParentDTOs = null;
		List<String> errors = new ArrayList<>();
		InputStream in = null;
		InputStream in2 = null;
		BulkOrdersDTO bulkOrdersDTO = null;
		Reader targetReader = null;
		Reader targetReader1 = null;
		File file = null;
		String fileFolder = "Bulkupload-orderentry-NIPT-dPCR";
		String fileType = ".csv";

		String fileNameAppender = String.valueOf(Timestamp.from(Instant.now()).getTime());
		try {
			checkFileEmpty(inputStream);
			fileOutputStream = new FileOutputStream(
					uploadedBulkOrderLocation + fileFolder + fileNameAppender + fileType);
			byte[] content = null;
			content = IOUtils.toByteArray(inputStream);
			in = new ByteArrayInputStream(content);
			in2 = new ByteArrayInputStream(content);
			brTemp = new BufferedReader(new InputStreamReader(in));
			long size = brTemp.lines().count();
			br = new BufferedReader(new InputStreamReader(in2));
			long i = 1;
			writeFile(br, fileOutputStream, i, size);
			file = new File(uploadedBulkOrderLocation + fileFolder + fileNameAppender + fileType);
			checkFileSize(file);
			reader = new FileReader(uploadedBulkOrderLocation + fileFolder + fileNameAppender + fileType);
			byte[] tempByte1;
			tempByte1 = IOUtils.toByteArray(reader);
			targetReader = new StringReader(new String(tempByte1));
			records = readFromCSV(targetReader);
			targetReader1 = new StringReader(new String(tempByte1));
			recordSize = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(targetReader1);
			isRowExceeded(recordSize);
			try {
				int row = 2;
				boolean isVisited = false;
				String accessioningId = null;
				String assayType = null;
				String sampleType = null;
				String retestSample = null;
				String orderComments = null;
				String patientFirstName = null;
				String patientLastName = null;
				String patientMedicalRecNo = null;
				String patientDOB = null;
				String refClinicianName = null;
				String refClinicianClinicName = null;
				String otherClinicianName = null;
				String maternalAge = null;
				String gestationalAgeWeeks = null;
				String gestationalAgeDays = null;
				String ivfStatus = null;
				String eggDonor = null;
				String eggDonorAge = null;
				String testOptions = null;
				String testOptionsFetalSex = null;
				String fetusNumber = null;
				String collectionDate = null;
				String receivedDate = null;
				String labId = null;
				String accountNo = null;
				String reasonForReferral = null;

				if (records != null) {
					orderParentDTOs = new ArrayList<>();
					List<String> accessioningIdList = new ArrayList<>();
					for (CSVRecord csvRecord : records) {
						try {
							accessioningId = csvRecord.get(CSV.ACCESSIONINGID.toString());
							assayType = csvRecord.get(CSV.ASSAY_TYPE.toString());
							sampleType = csvRecord.get(CSV.SAMPLE_TYPE.toString());
							retestSample = csvRecord.get(CSV.RETEST_SAMPLE.toString());
							orderComments = csvRecord.get(CSV.COMMENTS.toString());
							//Assay
							maternalAge = csvRecord.get(CSV.MATERNAL_AGE.toString());
							gestationalAgeWeeks = csvRecord.get(CSV.GESTATIONAL_AGE.toString());
							gestationalAgeDays = csvRecord.get(CSV.GESTATIONAL_AGE_DAYS.toString());
							ivfStatus = csvRecord.get(CSV.IVF_STATUS.toString());
							eggDonor = csvRecord.get(CSV.EGG_DONAR.toString());
							eggDonorAge = csvRecord.get(CSV.EGG_DONAR_AGE.toString());
							testOptions = csvRecord.get(CSV.TEST_OPTIONS.toString());
							testOptionsFetalSex = csvRecord.get(CSV.TEST_OPTION_FETAL_SEX.toString());
							fetusNumber = csvRecord.get(CSV.NO_OF_FETUS.toString());
							collectionDate = csvRecord.get(CSV.COLLECTION_DATE.toString());
							receivedDate = csvRecord.get(CSV.RECEIVED_DATE.toString());

							patientFirstName = csvRecord.get(CSV.FIRST_NAME.toString());
							patientLastName = csvRecord.get(CSV.LAST_NAME.toString());
							patientDOB = csvRecord.get(CSV.DOB.toString());
							patientMedicalRecNo = csvRecord.get(CSV.MEDICAL_REORD_NUMBER.toString());
							labId = csvRecord.get(CSV.LAB_ID.toString());
							accountNo = csvRecord.get(CSV.ACCOUNT.toString());
							refClinicianName = csvRecord.get(CSV.REF_CLINICIAN.toString());
							refClinicianClinicName = csvRecord.get(CSV.CLINIC_NAME.toString());
							otherClinicianName = csvRecord.get(CSV.OTHER_CLINICIAN.toString());
							reasonForReferral = csvRecord.get(CSV.REASON_FOR_REFERRAL.toString());

						} catch (Exception e) {
							throw new HMTPException(OMMConstant.ErrorMessages.INVALID_CSV_TEMPLATE.toString());
						} finally {
							if (fileOutputStream != null) {
								fileOutputStream.close();
							}
						}

						orderParentDTO = new OrderParentDTO();
						OrderDTO orderDTO = new OrderDTO();
						PatientDTO patientDTO = new PatientDTO();
						orderDTO.setPatient(patientDTO);
						AssayDTO assayDTO = new AssayDTO();
						orderDTO.setAssay(assayDTO);
						orderParentDTO.setOrder(orderDTO);

						bulkOrderValidationUtil.validateAccessioningId(accessioningId, row, orderParentDTO, errors,
								accessioningIdList);
						assayType = setAssayType(assayType);

						if (!isVisited && assayType != null && !assayType.isEmpty()
								&& assayType.equals(CSV.NIPTDPCR.toString())) {
							bulkOrderValidationUtil.assayValidations(this, assayType);
							isVisited = true;
						} else if (assayType == null || assayType.isEmpty()
								|| !assayType.equals(CSV.NIPTDPCR.toString())) {
							errors.add(CSV.ROW.toString() + row
									+ " Please enter valid Assay Type to proceed with order creation.");
						}

						if (assayType != null && !assayType.isEmpty() && assayType.equals(CSV.NIPTDPCR.toString())) {
							bulkOrderValidationUtil.validateAssayType(this, assayType, row, orderParentDTO, errors);
						}

						assayType = CSV.NIPTDPCR.toString();

						bulkOrderValidationUtil.validateSampleType(this, sampleType, row, orderParentDTO, errors);

						bulkOrderValidationUtil.validateRetestsample(retestSample, row, orderParentDTO, errors);

						bulkOrderValidationUtil.validateOrderComments(orderComments, row, orderParentDTO, errors);

						bulkOrderValidationUtil.validateMaternalAgeSampleType(this, maternalAge, row, orderParentDTO,
								errors);

						bulkOrderValidationUtil.validateGestationalAge(this, gestationalAgeWeeks, row, orderParentDTO,
								errors);

						bulkOrderValidationUtil.validateGestationalAgeDays(this, gestationalAgeDays, row,
								orderParentDTO, errors);

						bulkOrderValidationUtil.validateEggDonor(ivfStatus, eggDonor, eggDonorAge, maternalAge, row, orderParentDTO,
								errors);

						bulkOrderValidationUtil.validateTestOptions(testOptions, testOptionsFetalSex, row,
								orderParentDTO, errors);

						bulkOrderValidationUtil.validateFetusNumber(this, fetusNumber, row, orderParentDTO, errors);

						bulkOrderValidationUtil.validateCollectionDate(collectionDate, row, orderParentDTO, errors,
								timeZone);

						bulkOrderValidationUtil.validateReceivedDate(receivedDate, row, orderParentDTO, errors,
								timeZone);

						bulkOrderValidationUtil.validatePatientFirstName(patientFirstName, row, orderParentDTO, errors);

						bulkOrderValidationUtil.validatePatientLastName(patientLastName, row, orderParentDTO, errors);

						bulkOrderValidationUtil.validatePatientMedicalRecNo(patientMedicalRecNo, row, orderParentDTO,
								errors);

						bulkOrderValidationUtil.validatePatientDOB(patientDOB, row, orderParentDTO, errors, timeZone);

						/**	bulkOrderValidationUtil.validatePatientGender(patientGender, row, orderParentDTO, errors);

						bulkOrderValidationUtil.validatePatientContactNo(patientContactNo, row, orderParentDTO, errors);

						bulkOrderValidationUtil.validateTreatingDoctorName(treatingDoctorName, row, orderParentDTO,
								errors);

						bulkOrderValidationUtil.validateTreatingDoctorContactNo(treatingDoctorContactNo, row,
								orderParentDTO, errors);

						bulkOrderValidationUtil.validateRefClinicianFaxNo(refClinicianFaxNo, row, orderParentDTO,
								errors);

						bulkOrderValidationUtil.validateOtherClinicianFaxNo(otherClinicianFaxNo, row, orderParentDTO,
								errors);*/

						bulkOrderValidationUtil.validateRefClinicianName(refClinicianName, row, orderParentDTO, errors);



						bulkOrderValidationUtil.validateRefClinicianClinicName(refClinicianClinicName, row,
								orderParentDTO, errors);



						bulkOrderValidationUtil.validateOtherClinicianName(otherClinicianName, row, orderParentDTO,
								errors);

						bulkOrderValidationUtil.validateLabId(labId, row, orderParentDTO,
								errors);

						bulkOrderValidationUtil.validateAccountNumber(accountNo, row, orderParentDTO,
								errors);

						bulkOrderValidationUtil.validateReasonForReferral(reasonForReferral, row, orderParentDTO,
								errors);

						orderParentDTOs.add(orderParentDTO);
						row++;
					}
				}
			} catch (Exception e) {
				throw new HMTPException(e.getMessage());
			}
			bulkOrdersDTO = new BulkOrdersDTO();
			bulkOrdersDTO.setOrderParentDTO(orderParentDTOs);
			checkRowEmpty(bulkOrdersDTO);
			return returnResponse(errors, bulkOrdersDTO);
		} catch (Exception e) {
			errors.add(e.getMessage());
			return Response.ok(errors).status(400).build();
		} finally {
			try {

				closeResources(br, brTemp, in, in2, inputStream, reader, targetReader, targetReader1, fileOutputStream);
				Files.deleteIfExists(Paths.get(uploadedBulkOrderLocation + fileFolder + fileNameAppender + fileType));
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
	}

	private Iterable<CSVRecord> readFromCSV(Reader targetReader) throws HMTPException {
		try {

			return CSVFormat.RFC4180.withHeader(CSV.ACCESSIONINGID.toString(),
					CSV.ASSAY_TYPE.toString(),
					CSV.SAMPLE_TYPE.toString(),
					CSV.RETEST_SAMPLE.toString(),
					CSV.COMMENTS.toString(),
					CSV.MATERNAL_AGE.toString(),
					CSV.GESTATIONAL_AGE.toString(),
					CSV.GESTATIONAL_AGE_DAYS.toString(),
					CSV.IVF_STATUS.toString(),
					CSV.EGG_DONAR.toString(),
					CSV.EGG_DONAR_AGE.toString(),
					CSV.TEST_OPTIONS.toString(),
					CSV.TEST_OPTION_FETAL_SEX.toString(),
					CSV.NO_OF_FETUS.toString(),
					CSV.COLLECTION_DATE.toString(),
					CSV.RECEIVED_DATE.toString(),
					CSV.FIRST_NAME.toString(),
					CSV.LAST_NAME.toString(),
					CSV.DOB.toString(),
					CSV.MEDICAL_REORD_NUMBER.toString(),
					CSV.LAB_ID.toString(),
					CSV.ACCOUNT.toString(),
					CSV.REF_CLINICIAN.toString(),
					CSV.CLINIC_NAME.toString(),
					CSV.OTHER_CLINICIAN.toString(),
					CSV.REASON_FOR_REFERRAL.toString()).withFirstRecordAsHeader().parse(targetReader);

		} catch (Exception e) {
			throw new HMTPException(OMMConstant.ErrorMessages.INVALID_CSV_TEMPLATE.toString());
		}

	}

	private String setAssayType(String assayType) {
		if (assayType.equals(CSV.NIPT_DPCR.toString())) {
			return CSV.NIPTDPCR.toString();
		}
		return null;
	}

	private void closeResources(BufferedReader br, BufferedReader brTemp, InputStream in, InputStream in2,
			InputStream inputStream, Reader reader, Reader targetReader, Reader targetReader1,
			FileOutputStream fileOutputStream) throws IOException {
		if (br != null) {
			br.close();
		}
		if (brTemp != null) {
			brTemp.close();
		}
		if (in != null) {
			in.close();
		}
		if (in2 != null) {
			in2.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
		if (reader != null) {
			reader.close();
		}
		if (targetReader != null) {
			targetReader.close();
		}
		if (targetReader1 != null) {
			targetReader1.close();
		}
		if (fileOutputStream != null) {
			fileOutputStream.close();
		}
	}

	private Response returnResponse(List<String> errors, BulkOrdersDTO bulkOrdersDTO) {
		if (errors != null && !errors.isEmpty()) {
			return Response.ok(errors).status(400).build();
		} else {
			return Response.ok(bulkOrdersDTO).build();
		}
	}

	private void checkRowEmpty(BulkOrdersDTO bulkOrdersDTO) throws HMTPException {
		if (bulkOrdersDTO.getOrderParentDTO().isEmpty()) {
			throw new HMTPException(OMMConstant.ErrorMessages.FILE_EMPTY.toString());
		}

	}

	private void isRowExceeded(Iterable<CSVRecord> recordSize) throws HMTPException {
		if (StreamSupport.stream(recordSize.spliterator(), false).count() > ordersThresholdSize) {
			throw new HMTPException(OMMConstant.ErrorMessages.FILE_ROWS_EXCEEDED.toString() + ordersThresholdSize);
		}
	}

	private void checkFileSize(File file) throws HMTPException {
		if (((double) file.length() / (1024 * 1024)) > fileMemorySize) {
			throw new HMTPException(OMMConstant.ErrorMessages.BULK_ORDER_VALIDATION_FAILED.toString());
		}

	}

	private void writeFile(BufferedReader br, FileOutputStream fileOutputStream, long i, long size) throws IOException {
		String c = null;
		while ((c = br.readLine()) != null) {
			if (i >= 5 && i <= size - 2) {
				fileOutputStream.write(c.getBytes());
			}
			if (i >= 5 && i <= size - 3) {
				fileOutputStream.write("\n".getBytes());
			}
			i++;
		}

	}

	private void checkFileEmpty(InputStream inputStream) throws HMTPException {
		if (inputStream == null) {
			throw new HMTPException(OMMConstant.ErrorMessages.FILE_EMPTY.toString());
		}

	}

	@Override
	@Transactional(rollbackFor = { HMTPException.class, Exception.class })
	public Response createBulkOrders(BulkOrdersDTO bulkOrdersDTO) throws HMTPException {
		try {
			List<PatientAssay> patientAssays = new ArrayList<>();
			List<TestOptions> testOptions = null;
			for (OrderParentDTO orderParentDTO : bulkOrdersDTO.getOrderParentDTO()) {

				// Patients
				Patient patientTemp = patientWriteRepository.save(orderService.createPatientObject(orderParentDTO));

				// PatientAssays
				PatientAssay patientAssay = orderService.createPatientAssayObject(orderParentDTO);
				patientAssay.setPatient(patientTemp);
				patientAssays.add(patientAssay);

				// PatientSamples
				List<PatientSamples> patientSamples = orderService.createPatientSamplesObject(orderParentDTO);
				for (PatientSamples patientSample : patientSamples) {
					patientSample.setPatient(patientTemp);
				}
				List<PatientSamples> patientSamplesTemp = patientSampleWriteRepository.save(patientSamples);

				// Orders
				Order order = orderService.createOrderObject(orderParentDTO);
				order.setPatient(patientTemp);
				order.setPatientSampleId(patientSamplesTemp.get(0).getId());
				order.setReqFieldMissingFlag(orderService.getRequiredFieldMissingFlag(orderParentDTO));
				Order orderTemp = orderWriteRepository.save(order);

				// Test Options
				testOptions = orderService.createTestOptionsObject(orderParentDTO);
				for (TestOptions testOption : testOptions) {
					testOption.setOrder(orderTemp);
				}
				testOptionsWriteRepository.save(testOptions);
			}

			patientAssayWriteRepository.save(patientAssays);
			return Response.ok().build();
		} catch (Exception exp) {
			throw new HMTPException(OMMConstant.ErrorMessages.BULK_ORDER_ERROR_OCCURED.toString());
		}

	}

	@Override
	public Response updateContainerSamplesFlag(String containerId) throws HMTPException {
		long domainId = 0;
		String errMsg = "";
		String successMsg = "";
		List<ContainerSamples> containers = null;
		logger.info("updateContainerSamplesFlag() -> start of updateContainerSamplesFlag() execution");
		try {
			if (containerId != null) {
				domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
				containers = containerSamplesReadRepository.findByIdAndContainerIDAndStatus(containerId,
						OMMConstant.CSV.STATUS_PROCESSED.toString(), domainId);
				if (!containers.isEmpty()) {
					errMsg = "Delete is not possible as the MP96 run results for the " + containerId
							+ " have already been sent";
					return Response.status(CustomErrorCodes.INVALID_CONTAINER_ID.getErrorCode()).entity(errMsg).build();
				}
				List<ContainerSamples> containerSample = containerSamplesReadRepository.findByContainerID(containerId,
						domainId);
				if (!containerSample.isEmpty()) {
					List<ContainerSamples> cSamples = new ArrayList<>();
					for (ContainerSamples cs : containerSample) {
						cs.setActiveFlag(OMMConstant.CSV.INACTIVE_FLAG.toString());
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						cs.setUpdatedDateTime(timestamp);
						cs.setUpdatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
						cSamples.add(cs);
					}
					containerSamplesWriteRepository.save(cSamples);
				}
			}
			logger.info("End of updateContainerSamplesFlag() execution");

			successMsg = "Mapping for the container ID {0} is deleted successfully from the Connect application.";
			TranslationDTO translationDTO=new TranslationDTO();
			List<String> values=new ArrayList<>();
			values.add(containerId);
			translationDTO.setMessageGroup(OMMConstant.WARNING_CONTAINER_ID.toString());
			translationDTO.setMessageContent(successMsg);
			translationDTO.setValues(values);
			return Response.ok(200).entity(translationDTO).build();
		} catch (Exception e) {
			logger.info("Error while removing container samples for given container id");
			throw new HMTPException(e);
		}
	}

	@Override
	public Response validateContainerSamplesStatus(String deviceRunId) throws HMTPException {
		List<ContainerSamples> containerSampleList = null;
		long domainId;
		try {
			logger.info("validateContainerSamplesStatus() -> Start of validateContainerSamplesStatus() execution.");
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			if (deviceRunId != null) {
				containerSampleList = containerSamplesReadRepository.findAllContainerSamplesByDeviceRun(deviceRunId,
						domainId);
				if (!containerSampleList.isEmpty()) {
					Optional<ContainerSamples> first = containerSampleList.stream()
							.filter(c -> (StringUtils.isNotEmpty(c.getStatus()) && c.getStatus()
									.equalsIgnoreCase(OMMConstant.CSV.STATUS_SENT_TO_DEVICE.toString())))
							.findFirst();
					if (first.isPresent()) {
						logger.info("End of validateContainerSamplesStatus() execution.");
						return Response.status(HttpStatus.SC_OK).entity("container samples validated successfully..")
								.build();
					} else {
						logger.info("End of validateContainerSamplesStatus() execution-No content");
						return Response.status(HttpStatus.SC_NO_CONTENT)
								.entity("container samples validated successfully..").build();
					}
				} else {
					logger.info("End of validateContainerSamplesStatus() execution-No content");
					return Response.status(HttpStatus.SC_NO_CONTENT)
							.entity("container samples validated successfully No content").build();
				}

			} else {
				throw new HMTPException("Incorrect deviceRunId  for validating the container samples");
			}
		} catch (Exception exception) {
			logger.error(
					"Error while validating ContainerSamplesStatus the container samples : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	@Override
	public Response getAllOrder() throws HMTPException {
		List<Order> ord = null;
		List<PatientSamples> patientsamples = null;
		logger.info("getOrderBySampleId() -> Start of getOrderBySampleId Execution");
		List<OrderDTO> orderDTOs = new ArrayList<>();
		long domainId = 0;
		try {
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			ord = orderReadRepository.findAllOrders(domainId);
			if (!ord.isEmpty()) {
				for (Order order : ord) {
					logger.info("OrderID::" + order.getId());

					String accessioningID = order.getAccessioningId();
					patientsamples = patientSampleReadRepository.findAllBySampleId(accessioningID, domainId);
					OrderDTO orderDTO = orderService.createOrderDTO(order);
					orderDTO.setAssay(orderService.createAssayDTO(order));
					orderDTO.getAssay().setTestOptions(orderService.createTestOptionsDTO(order));
					orderDTO.setPatient(orderService.createPatientDTO(order));
					orderDTOs.add(orderDTO);
				}
			}

			if (orderDTOs.isEmpty()) {
				throw new HMTPException(CustomErrorCodes.ORDER_NOT_FOUND,
						OMMConstant.ErrorMessages.ORDER_NOT_FOUND.toString());
			}

			logger.info("End of getOrderBySampleId() execution");
			return Response.ok(200).entity(orderDTOs).build();

		} catch (HMTPException exp) {
			logger.error("Error While getting the order details : " + exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exp) {
			logger.error("Error While getting the order details : " + exp.getMessage());
			throw new HMTPException(exp);

		}
	}

	public Map<String, Object> getTotalMap() {
		return totalMap;
	}

	public void setTotalMap(Map<String, Object> totalMap) {
		this.totalMap = totalMap;
	}

	@Override
	public Response searchOrder(String searchQuery, String orderStatus, int offset, int limit) {

		try {

			if (StringUtils.isBlank(searchQuery) || limit == 0)
				return Response.status(Status.BAD_REQUEST).build();

			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();

			Pageable pageable = new OffsetBasedPageRequest(offset, limit);

			Page<Order> searchOrderPage = orderReadRepository
					.findByAccessioningIdContainingIgnoreCaseAndOrderStatusAndCompanyId(searchQuery, orderStatus,
							domainId, pageable);

			OrderElements orderElements = new OrderElements();
			orderElements.setTotalElements(searchOrderPage.getTotalElements());
			orderElements.setOrders(searchOrderPage.getContent().stream().map(orderService::createOrderDTO)
					.collect(Collectors.toList()));

			return Response.ok(orderElements).build();
		} catch (Exception e) {
			logger.info("Error in OMM - search API, message: " + e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response getInworkflowOrders() throws UnsupportedEncodingException, HMTPException {
		List<WorkflowDTO> workflowOrders = new ArrayList<>();
		try {
			workflowOrders = orderService.getInWorkflowOrders();

			workflowOrders.parallelStream().forEach(p -> p.setMandatoryFieldMissing(getMandatoryFlagByAccessioningId(p.getAssaytype(), p.getAccessioningId())));

		} catch(HMTPException e) {
			logger.error("Error while fetching inworkflow orders",e.getMessage());
			throw new HMTPException(e);
		}
		return Response.ok().entity(workflowOrders).build();
	}

	public boolean getMandatoryFlagByAccessioningId(String assayType, String accessioningId) {
		boolean mandatoryFieldMissing = false;

		try {
			Response response = getOrderBySampleId(accessioningId);
			List<OrderDTO> orderDTOs = response.readEntity(new GenericType<List<OrderDTO>>() {
			});
			OrderDTO orderDTO = orderDTOs.get(0);
			mandatoryFieldMissing = orderDTO.isReqFieldMissingFlag();

		} catch(HMTPException e) {
			logger.error("Error while fetching mandatory flag for accessioning id",e.getMessage());
		}

		return mandatoryFieldMissing;
	}

	@Override
	public Response getMandatoryFlagByRun(List<SampleResultsDTO> sampleResults) {
		sampleResults.parallelStream().forEach(p -> p.setMandatoryFieldMissing(getMandatoryFlagByAccessioningId(p.getAssayType(),p.getAccesssioningId())));
		return Response.ok().entity(sampleResults).build();
	}


}
