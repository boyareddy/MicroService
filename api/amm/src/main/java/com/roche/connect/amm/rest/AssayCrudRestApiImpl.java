/*******************************************************************************
*  * File Name: AssayCrudRestApiImpl.java            
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
package com.roche.connect.amm.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.UniqueConstraint;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.amm.model.AssayInputDataValidations;
import com.roche.connect.amm.model.AssayListData;
import com.roche.connect.amm.model.AssayType;
import com.roche.connect.amm.model.DeviceTestOptions;
import com.roche.connect.amm.model.FlagsData;
import com.roche.connect.amm.model.MolecularIDType;
import com.roche.connect.amm.model.ProcessStepAction;
import com.roche.connect.amm.model.SampleType;
import com.roche.connect.amm.model.TestOptions;
import com.roche.connect.amm.repository.AssayDataValidationReadRepository;
import com.roche.connect.amm.repository.AssayListDataReadRepository;
import com.roche.connect.amm.repository.AssayTypeReadRepository;
import com.roche.connect.amm.repository.DeviceTestOptionsReadRepository;
import com.roche.connect.amm.repository.FlagsDataReadRepository;
import com.roche.connect.amm.repository.MolecularTypeReadRepository;
import com.roche.connect.amm.repository.ProcessStepActionReadRepository;
import com.roche.connect.amm.repository.SampleTypeReadRepository;
import com.roche.connect.amm.repository.TestOptionsReadRepository;
import com.roche.connect.amm.util.FlagDescriptionCache;
import com.roche.connect.amm.writerepository.AssayTypeWriteRepository;
import com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO;
import com.roche.connect.common.amm.dto.AssayListDataDTO;
import com.roche.connect.common.amm.dto.AssayTypeDTO;
import com.roche.connect.common.amm.dto.DeviceTestOptionsDTO;
import com.roche.connect.common.amm.dto.FlagsDataDTO;
import com.roche.connect.common.amm.dto.MolecularIDTypeDTO;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.amm.dto.RunCompleteFilterDTO;
import com.roche.connect.common.amm.dto.SampleTypeDTO;
import com.roche.connect.common.amm.dto.TestOptionsDTO;
import com.roche.connect.common.amm.dto.UnassignedFilterDTO;

/**
 * The Class AssayCrudRestApiImpl.
 */
@Component
public class AssayCrudRestApiImpl implements AssayCrudRestApi {

	/** The assay read repository. */
	@Autowired
	AssayTypeReadRepository assayReadRepository;

	/** The assay write repository. */
	@Autowired
	AssayTypeWriteRepository assayWriteRepository;

	/** The sample type read repository. */
	@Autowired
	SampleTypeReadRepository sampleTypeReadRepository;

	/** The test options read repository. */
	@Autowired
	TestOptionsReadRepository testOptionsReadRepository;

	/** The device test options read repository. */
	@Autowired
	DeviceTestOptionsReadRepository deviceTestOptionsReadRepository;

	/** The process step action read repository. */
	@Autowired
	ProcessStepActionReadRepository processStepActionReadRepository;

	/** The assay data validation read repository. */
	@Autowired
	AssayDataValidationReadRepository assayDataValidationReadRepository;

	/** The assay list data read repository. */
	@Autowired
	AssayListDataReadRepository assayListDataReadRepository;

	/** The molecular type read repository. */
	@Autowired
	MolecularTypeReadRepository molecularTypeReadRepository;
	@Autowired
	FlagsDataReadRepository flagsDataReadRepository;
	FlagDescriptionCache flagDescriptionCache;
	@Value("${connect.urlUIResourceBundle}")
	private String urlforFalgDescription;
	/** The logger. */
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	/**
	 * Gets the assay types data.
	 *
	 * @param assayType
	 *            the assay type
	 * @return the assay types data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Cacheable(value = "entity", key = "{ #root.methodName + #assayType }")
	@Override
	public Response getAssayTypesData(String assayType) throws HMTPException {
		List<AssayType> assays = null;
		List<AssayTypeDTO> assayDTOs = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getAssayTypesData() -> Start of getAssayTypesData Execution");
			assayDTOs = new ArrayList<>();
			assays = assayType != null ? assayReadRepository.findAssayDetails(assayType, domainId)
					: assayReadRepository.findAssays(domainId);
			for (AssayType assay : assays) {
				AssayTypeDTO assayDTO = new AssayTypeDTO();
				assayDTO.setAssayType(assay.getAssayTypeName());
				assayDTO.setAssayVersion(assay.getAssayVersion());
				assayDTO.setWorkflowDefFile(assay.getWorkflowDefFile());
				assayDTO.setWorkflowDefVersion(assay.getWorkflowDefVersion());
				assayDTO.setAssayTypeId(assay.getId());
				assayDTOs.add(assayDTO);
			}
			logger.info("End of getAssayTypesData() execution");
			return Response.ok().entity(assayDTOs).build();
		} catch (Exception exception) {
			logger.error("Error while getting AssayTypes : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	/**
	 * Gets the sample types data.
	 *
	 * @param assayType
	 *            the assay type
	 * @return the sample types data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Cacheable(value = "entity", key = "{ #root.methodName + #assayType }")
	@Override
	public Response getSampleTypesData(String assayType) throws HMTPException {
		List<SampleType> samples = null;
		List<SampleTypeDTO> sampleDTOs = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getSampleTypesData() -> Start of getSampleTypesData Execution");
			samples = sampleTypeReadRepository.find(assayType, domainId);
			sampleDTOs = new ArrayList<>();
			SampleTypeDTO sampleDTO = null;
			for (SampleType sample : samples) {
				sampleDTO = new SampleTypeDTO();
				sampleDTO.setAssayType(sample.getAssayType());
				sampleDTO.setSampleType(sample.getSampleTypeName());
				sampleDTO.setMaxAgeDays(sample.getMaxAgeDays());
				sampleDTOs.add(sampleDTO);
			}
			logger.info("End of getSampleTypesData() execution");
			return Response.ok().entity(sampleDTOs).build();
		} catch (Exception exception) {
			logger.error("Error while getting SampleTypes : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	/**
	 * Gets the test options data.
	 *
	 * @param assayType
	 *            the assay type
	 * @return the test options data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Cacheable(value = "entity", key = "{ #root.methodName + #assayType }")
	@Override
	public Response getTestOptionsData(String assayType) throws HMTPException {
		List<TestOptions> testOptions = null;
		List<TestOptionsDTO> testOptionDTOs = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getTestOptionsData() -> Start of getTestOptionsData Execution");
			testOptionDTOs = new ArrayList<>();
			testOptions = testOptionsReadRepository.findTestOptions(assayType, domainId);
			TestOptionsDTO testOptionDTO = null;
			for (TestOptions testOption : testOptions) {
				testOptionDTO = new TestOptionsDTO();
				testOptionDTO.setAssayType(testOption.getAssayType());
				testOptionDTO.setTestName(testOption.getTestName());
				testOptionDTO.setTestDescription(testOption.getTestDescription());
				testOptionDTO.setSequenceId(testOption.getSequenceId());
				testOptionDTOs.add(testOptionDTO);
			}
			logger.info("End of getTestOptionsData() execution");
			return Response.ok().entity(testOptionDTOs).build();
		} catch (Exception exception) {
			logger.error("error while getting TestOptions : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	/**
	 * Gets the device test options data.
	 *
	 * @param assayType
	 *            the assay type
	 * @param deviceType
	 *            the device type
	 * @return the device test options data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Cacheable(value = "entity", key = "{ #root.methodName + #assayType + #deviceType + #processStepName }")
	@Override
	public Response getDeviceTestOptionsData(String assayType, String deviceType, String processStepName)
			throws HMTPException {
		List<DeviceTestOptions> deviceTestOptions = null;
		List<DeviceTestOptionsDTO> deviceTestOptionsDTOs = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getDeviceTestOptionsData() -> Start of getDeviceTestOptionsData Execution");
			deviceTestOptionsDTOs = new ArrayList<>();
			if (processStepName != null && deviceType != null) {
				deviceTestOptions = deviceTestOptionsReadRepository.findDeviceTestOptions(assayType, deviceType,
						processStepName, domainId);
			} else {
				deviceTestOptions = deviceType != null
						? deviceTestOptionsReadRepository.findDeviceTestOptions(assayType, deviceType, domainId)
						: deviceTestOptionsReadRepository.findDeviceTestOptions(assayType, domainId);
			}
			DeviceTestOptionsDTO deviceTestOptionsDTO = null;
			for (DeviceTestOptions deviceTestOption : deviceTestOptions) {
				deviceTestOptionsDTO = new DeviceTestOptionsDTO();
				deviceTestOptionsDTO.setAssayType(deviceTestOption.getAssayType());
				deviceTestOptionsDTO.setProcessStepName(deviceTestOption.getProcessStepName());
				deviceTestOptionsDTO.setTestName(deviceTestOption.getTestName());
				deviceTestOptionsDTO.setTestOptionId(deviceTestOption.getTestOptionId());
				deviceTestOptionsDTO.setTestProtocol(deviceTestOption.getTestProtocol());
				deviceTestOptionsDTO.setDeviceType(deviceTestOption.getDeviceType());
				deviceTestOptionsDTO.setAnalysisPackageName(deviceTestOption.getAnalysisPackageName());
				deviceTestOptionsDTOs.add(deviceTestOptionsDTO);
			}
			logger.info("End of getDeviceTestOptionsData() execution");
			return Response.ok().entity(deviceTestOptionsDTOs).build();
		} catch (Exception exception) {
			logger.error("error while getting Device TestOptions : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	/**
	 * Gets the WF process data.
	 *
	 * @param assayType
	 *            the assay type
	 * @param deviceType
	 *            the device type
	 * @return the WF process data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Cacheable(value = "entity", key = "{ #root.methodName + #assayType + #deviceType }")
	@Override
	public Response getWFProcessData(String assayType, String deviceType) throws HMTPException {
		List<ProcessStepAction> wfProcessData = null;
		List<ProcessStepActionDTO> processStepDTOs = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getWFProcessData() -> Start of getWFProcessData Execution");
			processStepDTOs = new ArrayList<>();
			wfProcessData = deviceType != null
					? processStepActionReadRepository.findByAssayandProcessStep(assayType, deviceType, domainId)
					: processStepActionReadRepository.findByAssay(assayType, domainId);
			ProcessStepActionDTO processStepDTO = null;
			for (ProcessStepAction procStepAction : wfProcessData) {
				processStepDTO = new ProcessStepActionDTO();
				processStepDTO.setAssayType(procStepAction.getAssayType());
				processStepDTO.setDeviceType(procStepAction.getDeviceType());
				processStepDTO.setManualVerificationFlag(procStepAction.getManualVerificationFlag());
				processStepDTO.setProcessStepSeq(procStepAction.getProcessStepSeq());
				processStepDTO.setProcessStepName(procStepAction.getProcessStepName());
				processStepDTO.setInputContainerType(procStepAction.getInputContainerType());
				processStepDTO.setOutputContainerType(procStepAction.getOutputContainerType());
				processStepDTO.setSampleVolume(procStepAction.getSampleVolume());
				processStepDTO.setEluateVolume(procStepAction.getEluateVolume());
				processStepDTO.setReagent(procStepAction.getReagent());
				processStepDTOs.add(processStepDTO);
			}
			logger.info("End of getWFProcessData() execution");
			return Response.ok().entity(processStepDTOs).build();
		} catch (Exception exception) {
			logger.error("error while fetching workflow process step details : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	/**
	 * Gets the list data.
	 *
	 * @param assayType
	 *            the assay type
	 * @param listType
	 *            the list type
	 * @return the list data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Cacheable(value = "entity", key = "{ #root.methodName + #assayType + #listType }")
	@Override
	public Response getListData(String assayType, String listType) throws HMTPException {
		List<AssayListData> assayListValues = null;
		List<AssayListDataDTO> assayListDataDTOs = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getListData() -> Start of getListData Execution");
			assayListDataDTOs = new ArrayList<>();
			assayListValues = listType != null
					? assayListDataReadRepository.findAssayListValues(assayType, listType, domainId)
					: assayListDataReadRepository.findAssayListValues(assayType, domainId);
			AssayListDataDTO assayListDataDTO = null;
			for (AssayListData assayListData : assayListValues) {
				assayListDataDTO = new AssayListDataDTO();
				assayListDataDTO.setAssayType(assayListData.getAssayType());
				assayListDataDTO.setListType(assayListData.getListType());
				assayListDataDTO.setValue(assayListData.getValue());
				assayListDataDTOs.add(assayListDataDTO);
			}
			logger.info("End of getListData() execution");
			return Response.ok().entity(assayListDataDTOs).build();
		} catch (Exception exception) {
			logger.error("error while fetching assay list values : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	/**
	 * Gets the assay input validation data.
	 *
	 * @param assayType
	 *            the assay type
	 * @param fieldName
	 *            the field name
	 * @return the assay input validation data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Cacheable(value = "entity", key = "{ #root.methodName + #assayType + #fieldName + #groupName }")
	@Override
	public Response getAssayInputValidationData(String assayType, String fieldName, String groupName)
			throws HMTPException {
		List<AssayInputDataValidations> dataInputValidations = null;
		List<AssayInputDataValidationsDTO> inputDataValidationsDTOs = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getAssayInputValidationData() -> Start of getAssayInputValidationData Execution");
			inputDataValidationsDTOs = new ArrayList<>();

			if (fieldName != null && groupName != null) {
				dataInputValidations = assayDataValidationReadRepository.findDataInputValidations(assayType, fieldName,
						domainId, groupName);
			} else if (fieldName != null) {
				dataInputValidations = assayDataValidationReadRepository.findDataInputValidations(assayType, fieldName,
						domainId);
			} else if (groupName != null) {
				dataInputValidations = assayDataValidationReadRepository.findDataInputValidationsByGroupName(assayType,
						domainId, groupName);
			} else {
				dataInputValidations = assayDataValidationReadRepository.findDataInputValidations(assayType, domainId);
			}
			AssayInputDataValidationsDTO inputDataValidationsDTO = null;
			for (AssayInputDataValidations validations : dataInputValidations) {
				inputDataValidationsDTO = new AssayInputDataValidationsDTO();
				inputDataValidationsDTO.setAssayType(validations.getAssayType());
				inputDataValidationsDTO.setFieldName(validations.getFieldName());
				inputDataValidationsDTO.setMinVal(validations.getMinValue());
				inputDataValidationsDTO.setMaxVal(validations.getMaxValue());
				inputDataValidationsDTO.setExpression(validations.getExpression());
				inputDataValidationsDTO.setIsMandatory(validations.getIsMandatory());
				inputDataValidationsDTO.setGroupName(validations.getGroupName());
				inputDataValidationsDTOs.add(inputDataValidationsDTO);
			}
			logger.info("End of getAssayInputValidationData() execution");
			return Response.ok().entity(inputDataValidationsDTOs).build();
		} catch (Exception exception) {
			logger.error("error while fetching data input validations : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	/**
	 * Gets the assay type ID.
	 *
	 * @param assayType
	 *            the assay type
	 * @return the assay type ID
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Cacheable(value = "entity", key = "{ #root.methodName + #assayType }")
	@Override
	public Response getAssayTypeID(String assayType) throws HMTPException {
		Long assayTypeID;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getAssayTypeID() -> Start of getAssayTypeID Execution");
			assayTypeID = assayReadRepository.findAssayTypeID(assayType, domainId);
			logger.info("End of getAssayTypeID() execution");
			return Response.ok().entity(assayTypeID).build();
		} catch (Exception exception) {
			logger.error("error while fetching the Assay type id for given Assay : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	/**
	 * Gets the molecular id data.
	 *
	 * @param assayType
	 *            the assay type
	 * @param plateType
	 *            the plate type
	 * @param plateLocation
	 *            the plate location
	 * @return the molecular id data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Cacheable(value = "entity", key = "{ #root.methodName + #assayType + #plateType + #plateLocation }")
	@Override
	public Response getMolecularIdData(String assayType, String plateType, String plateLocation) throws HMTPException {
		List<MolecularIDType> molecularIdTypes = null;
		List<MolecularIDTypeDTO> molecularDTOs = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getMolecularIdData() -> Start of getMolecularIdData Execution");
			molecularDTOs = new ArrayList<>();

			if (assayType != null && plateType != null && plateLocation != null) {
				molecularIdTypes = molecularTypeReadRepository.findMolecularIds(assayType, plateType, plateLocation,
						domainId);
			} else if (plateType != null && plateLocation != null) {
				molecularIdTypes = molecularTypeReadRepository.findMolecularIDTypeByPlateTypeAndPlateLocation(plateType,
						plateLocation, domainId);
			} else if (assayType != null && plateType != null) {
				molecularIdTypes = molecularTypeReadRepository.findMolecularIDTypeByIdAndPlateType(assayType, plateType,
						domainId);
			} else if (assayType != null && plateLocation != null) {
				molecularIdTypes = molecularTypeReadRepository.findMolecularIDTypeByIdAndPlateLocation(assayType,
						plateLocation, domainId);
			} else if (assayType != null) {
				molecularIdTypes = molecularTypeReadRepository.findByAssayType(assayType, domainId);
			} else {
				molecularIdTypes = molecularTypeReadRepository.findAll();
			}

			MolecularIDTypeDTO molecularsDTO = null;
			for (MolecularIDType molecular : molecularIdTypes) {
				molecularsDTO = new MolecularIDTypeDTO();
				molecularsDTO.setAssayType(molecular.getAssayType());
				molecularsDTO.setMolecularId(molecular.getMolecularId());
				molecularsDTO.setPlateType(molecular.getPlateType());
				molecularsDTO.setPlateLocation(molecular.getPlateLocation());
				molecularsDTO.setAssayTypeId(molecular.getAssay().getId());
				molecularDTOs.add(molecularsDTO);
			}
			logger.info("End of getMolecularIdData() execution");
			return Response.ok().entity(molecularDTOs).build();
		} catch (Exception exception) {
			logger.error("error while fetching Molecular Id : ", exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	@Override
	public Response getFlagDescriptions(String lang, String deviceType, String assayType, String flagCode)
			throws HMTPException {
		List<FlagsData> flagsDatas = new ArrayList<>();
		List<FlagsDataDTO> flagsDataDTOS = new ArrayList<>();
		Locale currentLocale = null;
		JSONObject localeFlagData = null;
		String key;
		long domainId;
		try {
			logger.info("Start of fetchAssayResultsFlagValues Execution");
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			if (deviceType != null) {
				if (deviceType.contains("+"))
					deviceType = deviceType.replace("+", " ");
				flagDescriptionCache = FlagDescriptionCache.getInstance();
				currentLocale = LocaleContextHolder.getLocale();
				if (assayType != null && flagCode != null) {
					flagsDatas = flagsDataReadRepository.findFlagDTOByAssayTypeAndDeviceTypeAndFlageCode(assayType,
							deviceType, flagCode, domainId);
				} else if (assayType != null) {
					flagsDatas = flagsDataReadRepository.findFlagDTOByAssayTypeAndDeviceType(assayType, deviceType,
							domainId);
				} else if (flagCode != null) {
					flagsDatas = flagsDataReadRepository.findFlagDTOByDeviceTypeAndFlageCode(deviceType, flagCode,
							domainId);
				} else {
					flagsDatas = flagsDataReadRepository.findFlagDTOByDeviceType(deviceType, domainId);
				}
			}
			if (deviceType != null && deviceType.equalsIgnoreCase("dPCR Analyzer")) {
				String[] device = deviceType.split(" ");
				deviceType = device[0];
				key = currentLocale + "," + deviceType;
			} else {
				key = currentLocale + "," + deviceType;
			}
			localeFlagData = flagDescriptionCache.get(key, urlforFalgDescription);
			logger.info("::::::::::::::" + localeFlagData);
			if (!flagsDatas.isEmpty() && localeFlagData != null) {
				flagsDataDTOS = mapModelToDTO(flagsDatas, localeFlagData);
			}
			logger.info("End of fetchAssayResultsFlagValues() execution");
			return Response.ok().entity(flagsDataDTOS).build();
		} catch (Exception e) {
			logger.error("error while fetching Flag's value: " + e.getMessage(), e);
			throw new HMTPException(e);
		}
	}

	public List<FlagsDataDTO> mapModelToDTO(List<FlagsData> listOfFlagsData, JSONObject localeFlagData) {
		List<FlagsDataDTO> flagsDataDTOS = new ArrayList<>();
		try {
			if (!listOfFlagsData.isEmpty() && localeFlagData != null) {
				for (FlagsData flagsData : listOfFlagsData) {
					FlagsDataDTO flagsDataDTO = new FlagsDataDTO();
					flagsDataDTO.setAssayType(flagsData.getAssayType());
					flagsDataDTO.setFlagCode(flagsData.getFlagCode());
					String falgDescription = localeFlagData.getString(flagsData.getFlagCode());
					flagsDataDTO.setDescription(falgDescription);
					flagsDataDTO.setSeverity(flagsData.getFlagSeverity());
					flagsDataDTO.setProcessStepName(flagsData.getProcessStepName());
					flagsDataDTO.setSampleOrRunLevel(flagsData.getSampleOrRunLevel());
					flagsDataDTO.setDeviceType(flagsData.getDeviceType());
					flagsDataDTOS.add(flagsDataDTO);
				}
			}
		} catch (Exception e) {
			logger.error("error while mapModelToDTO Flag's value: " + e.getMessage(), e);
		}
		return flagsDataDTOS;
	}

	@Cacheable(value = "entity", key = "{ #root.methodName }")
	@Override
	public Response getLastProcessStepData() throws HMTPException {
		List<ProcessStepAction> wfProcessData = null;
		List<ProcessStepActionDTO> processStepDTOs = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getLastProcessStepData() -> Start of getLastProcessStepData Execution");
			processStepDTOs = new ArrayList<>();
			wfProcessData = processStepActionReadRepository.findLastProcessStepDetails(domainId);
			for (ProcessStepAction procStepAction : wfProcessData) {
				ProcessStepActionDTO processStepDTO = new ProcessStepActionDTO();
				processStepDTO.setAssayType(procStepAction.getAssayType());
				processStepDTO.setDeviceType(procStepAction.getDeviceType());
				processStepDTO.setManualVerificationFlag(procStepAction.getManualVerificationFlag());
				processStepDTO.setProcessStepSeq(procStepAction.getProcessStepSeq());
				processStepDTO.setProcessStepName(procStepAction.getProcessStepName());
				processStepDTO.setInputContainerType(procStepAction.getInputContainerType());
				processStepDTO.setOutputContainerType(procStepAction.getOutputContainerType());
				processStepDTO.setSampleVolume(procStepAction.getSampleVolume());
				processStepDTO.setEluateVolume(procStepAction.getEluateVolume());
				processStepDTO.setReagent(procStepAction.getReagent());
				processStepDTOs.add(processStepDTO);
			}
			logger.info("End of getLastProcessStepData() execution");
			return Response.ok().entity(processStepDTOs).build();
		} catch (Exception exception) {
			logger.error("Error while fetching process step details : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	@Cacheable(value = "entity", key = "{ #root.methodName}")
	@Override
	public Response getUnassignedFilterData() throws HMTPException {
		List<SampleType> samplesTypes = null;
		List<UnassignedFilterDTO> unassignedFilterDTOList = new ArrayList<>();
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getUnassignedFilterData() -> Start of getUnassignedFilterData Execution");
			samplesTypes = sampleTypeReadRepository.findAll(domainId);
			samplesTypes.forEach(s -> {
				UnassignedFilterDTO unassignedFilterDTO = new UnassignedFilterDTO();
				unassignedFilterDTO.setAssayType(s.getAssayType());
				unassignedFilterDTO.setSampleType(s.getSampleTypeName());
				unassignedFilterDTOList.add(unassignedFilterDTO);
			});
			logger.info("End of getUnassignedFilterData() execution");
			return Response.ok().entity(unassignedFilterDTOList).build();
		} catch (Exception exception) {
			logger.error("error while fetching getUnassignedFilterData : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

	
	@Cacheable(value="entity",key="{ #root.methodName}") 
	@Override
	public Response getAssayProcessStepData() throws HMTPException {
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getAssayProcessStepData() -> Start of getAssayProcessStepData Execution");
			List<ProcessStepAction> processStepAction = processStepActionReadRepository.findAll(domainId);
			List<SampleType> sampleTypeList = sampleTypeReadRepository.findAll(domainId);
			Set<RunCompleteFilterDTO> collect2 = processStepAction.stream()
					.flatMap(l1 -> sampleTypeList.stream().map(l2 -> {

						RunCompleteFilterDTO runCompleteFilterDTO = new RunCompleteFilterDTO();
						runCompleteFilterDTO.setAssayType(l1.getAssayType());
						runCompleteFilterDTO.setDeviceType(l1.getDeviceType());
						runCompleteFilterDTO.setWorkflowStep(l1.getProcessStepName());
						runCompleteFilterDTO.setSampleType(l2.getSampleTypeName());
						return runCompleteFilterDTO;

					})).collect(Collectors.toSet());
			logger.info("End of getAssayProcessStepData() execution");
			return Response.ok().entity(collect2).build();
		} catch (Exception exception) {
			logger.error("error while fetching getAssayProcessStepData : " + exception.getMessage());
			throw new HMTPException(exception);
		}
	}

}
