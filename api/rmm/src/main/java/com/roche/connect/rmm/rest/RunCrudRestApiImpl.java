/*******************************************************************************
 * RunCrudRestApiImpl.java Version: 1.0 Authors: prasant.sahoo
 * ================================== Copyright (c) 2018 Roche Sequencing
 * Solutions (RSS) - CONFIDENTIAL All Rights Reserved. NOTICE: All information
 * contained herein is, and remains the property of COMPANY. The intellectual
 * and technical concepts contained herein are proprietary to COMPANY and may be
 * covered by U.S. and Foreign Patents, patents in process, and are protected by
 * trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from COMPANY. Access to the source code contained
 * herein is hereby forbidden to anyone except current COMPANY employees,
 * managers or contractors who have executed Confidentiality and Non-disclosure
 * agreements explicitly covering such access The copyright notice above does
 * not evidence any actual or intended publication or disclosure of this source
 * code, which includes information that is confidential and/or proprietary, and
 * is a trade secret, of COMPANY. ANY REPRODUCTION, MODIFICATION, DISTRIBUTION,
 * PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE
 * WITHOUT THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN
 * VIOLATION OF APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR
 * POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR
 * IMPLY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO
 * MANUFACTURE, USE, OR SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 * ================================== ChangeLog:
 ******************************************************************************/
package com.roche.connect.rmm.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.CommonErrorCodeConstants;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.AssayTypeDTO;
import com.roche.connect.common.amm.dto.FlagsDataDTO;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.dmm.DeviceDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderDetailsDTO;
import com.roche.connect.common.order.dto.WorkflowDTO;
import com.roche.connect.common.rmm.dto.InputStripDetailsDTO;
import com.roche.connect.common.rmm.dto.OutputStripDetailsDTO;
import com.roche.connect.common.rmm.dto.ProcessStepValuesDTO;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleProtocolDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SearchOrderElements;
import com.roche.connect.common.rmm.dto.SearchResult;
import com.roche.connect.common.rmm.dto.SearchRunResultElements;
import com.roche.connect.common.rmm.dto.UniqueReagentsAndConsumables;
import com.roche.connect.rmm.error.CustomErrorCodes;
import com.roche.connect.rmm.error.ErrorResponse;
import com.roche.connect.rmm.jasper.dto.DPCRDTO;
import com.roche.connect.rmm.jasper.dto.DPCRReagentnConsumableDTO;
import com.roche.connect.rmm.jasper.dto.DeviceRunResultsDTO;
import com.roche.connect.rmm.jasper.dto.FlagsDTO;
import com.roche.connect.rmm.jasper.dto.JasperUtil;
import com.roche.connect.rmm.jasper.dto.LibraryPrepDTO;
import com.roche.connect.rmm.jasper.dto.LpplateBasedSamples;
import com.roche.connect.rmm.jasper.dto.MPConsumablesDTO;
import com.roche.connect.rmm.jasper.dto.MPReagentsDTO;
import com.roche.connect.rmm.jasper.dto.NAExtractionDTO;
import com.roche.connect.rmm.jasper.dto.PlateBasisSamplesDTO;
import com.roche.connect.rmm.jasper.dto.ReagentsAndConsumablesDTO;
import com.roche.connect.rmm.jasper.dto.SampleCommentsDTO;
import com.roche.connect.rmm.jasper.dto.SampleDTO;
import com.roche.connect.rmm.jasper.dto.SamplesDataDTO;
import com.roche.connect.rmm.model.RunReagentsAndConsumables;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.RunResultsDetail;
import com.roche.connect.rmm.model.SampleProtocol;
import com.roche.connect.rmm.model.SampleReagentsAndConsumables;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.model.SampleResultsDetail;
import com.roche.connect.rmm.readrepository.RunReagentsAndConsumablesReadRepository;
import com.roche.connect.rmm.readrepository.RunResultsDetailReadRepository;
import com.roche.connect.rmm.readrepository.RunResultsReadRepository;
import com.roche.connect.rmm.readrepository.SampleResultsReadRepository;
import com.roche.connect.rmm.services.AdmIntegrationService;
import com.roche.connect.rmm.services.DeviceIntegrationService;
import com.roche.connect.rmm.services.OrderIntegrationService;
import com.roche.connect.rmm.services.SearchService;
import com.roche.connect.rmm.specifications.RunResultsSpecifications;
import com.roche.connect.rmm.specifications.RunResultsTypeSpecification;
import com.roche.connect.rmm.util.JasperUtils;
import com.roche.connect.rmm.util.MapDTOToEntity;
import com.roche.connect.rmm.util.MapEntityToDTO;
import com.roche.connect.rmm.util.RMMConstant;
import com.roche.connect.rmm.writerepository.RunReagentsAndConsumablesWriteRepository;
import com.roche.connect.rmm.writerepository.RunResultsDetailWriteRepository;
import com.roche.connect.rmm.writerepository.RunResultsWriteRepository;
import com.roche.connect.rmm.writerepository.SampleProtocolWriteRepository;
import com.roche.connect.rmm.writerepository.SampleReagentsAndConsumablesWriteRespository;
import com.roche.connect.rmm.writerepository.SampleResultsDetailWriteRepository;
import com.roche.connect.rmm.writerepository.SampleResultsWriteRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@Component
public class RunCrudRestApiImpl implements RunCrudRestApi {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private RunResultsReadRepository runResultsReadRepository;
	@Autowired
	private RunResultsWriteRepository runResultsWriteRepository;
	@Autowired
	RunReagentsAndConsumablesReadRepository runReagentsAndConsumablesReadRepository;
	@Autowired
	RunReagentsAndConsumablesWriteRepository runReagentsAndConsumablesWriteRepository;
	@Autowired
	RunResultsDetailReadRepository runResultsDetailReadRepository;
	@Autowired
	RunResultsDetailWriteRepository runResultsDetailWriteRepository;
	@Autowired
	private SampleResultsReadRepository sampleResultsReadRepository;
	@Autowired
	private SampleResultsWriteRepository sampleResultsWriteRepository;
	@Autowired
	private SampleResultsDetailWriteRepository sampleResultsDetailWriteRepository;
	@Autowired
	private SampleReagentsAndConsumablesWriteRespository sampleReagentsAndConsumablesWriteRespository;
	@Autowired
	private SampleProtocolWriteRepository sampleProtocolWriteRepository;
	@Autowired
	private DeviceIntegrationService deviceIntegrationService;
	@Autowired
	private SearchService searchService;
	
	@Autowired
	MapDTOToEntity mapDTOToEntity;

	@Autowired
	MapEntityToDTO mapEntityToDTO;
	
	@Autowired
	AdmIntegrationService admIntegrationService;
	
	@Autowired
	OrderIntegrationService orderIntegrationService;
	
	@Value("${pas.reports_password_required}")
	private String passwordRequired;
	
	@Value("${pas.reports_password}")
	private String password;
	
	/**
	 * Gets the run results.
	 * 
	 * @param deviceid
	 * @param processstepname
	 * @param runstatus
	 * @return the run results
	 * @throws HMTPException
	 */
	@Override
	public Response getRunResults(String deviceid, String processstepname, String runstatus) throws HMTPException {

		RunResultsDTO runResultsDTO = null;
		RunResults runResults = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getRunResults() -> Start of getRunResults Execution");

			if (StringUtils.isEmpty(deviceid)) {
				throw new HMTPException(CustomErrorCodes.DEVICE_RUN_ID_NULL,
						RMMConstant.ErrorMessages.DEVICE_RUN_ID_NULL.toString());
			}
			if (StringUtils.isEmpty(processstepname)) {
				throw new HMTPException(CustomErrorCodes.PROCESS_STEP_NAME_NULL,
						RMMConstant.ErrorMessages.PROCESS_STEP_NAME_NULL.toString());
			}
			if (runstatus == null) {

				runResults = runResultsReadRepository.findFirstByDeviceIdAndProcessStepName(deviceid, processstepname,
						domainId);
			} else {

				runResults = runResultsReadRepository.findFirstByDeviceIdAndProcessStepNameAndRunStatus(deviceid,
						processstepname, runstatus, domainId);
			}
			if (runResults != null) {
				runResultsDTO = mapEntityToDTO.getRunResultsDTOFromRunResults(runResults);
				logger.info("End of getRunResults() execution with data");
				return Response.ok(runResultsDTO, MediaType.APPLICATION_JSON).status(200).build();
			} else {
				logger.info("End of getRunResults() execution finished");
				return Response.status(Status.NO_CONTENT).build();
			}
		} catch (HMTPException exp) {
			logger.error("Error while getting the runId : " + exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exp) {
			logger.error("Error while getting the runId : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}

	}

	/**
	 * Adds the run results.
	 * 
	 * @param runResultsDTO
	 * @return the Response
	 * @throws HMTPException
	 */
	@Override
	public Response addRunResults(RunResultsDTO runResultsDTO) throws HMTPException {
		logger.info("addRunResults() -> Start of addRunResults Execution");
		try {
			RunResults runResults = mapDTOToEntity.getRunResultsFromDTO(runResultsDTO);
			List<RunReagentsAndConsumables> runReagentsAndConsumables = mapDTOToEntity
					.getRunReagentsAndConsumablesFromDTO(runResultsDTO);
			List<RunResultsDetail> runResultsDetails = mapDTOToEntity.getRunResultsDetailFromDTO(runResultsDTO);

			// setting the references as per relationship
			runReagentsAndConsumables.stream().forEach(p -> p.setRunResultsMappedToReagents(runResults));
			runResultsDetails.stream().forEach(p -> p.setRunResultsMappedToRunDetails(runResults));

			// saving the run results in db
			RunResults runResultsCreated = runResultsWriteRepository.save(runResults);
			runReagentsAndConsumables.stream().forEach(p -> runReagentsAndConsumablesWriteRepository.save(p));
			runResultsDetails.stream().forEach(p -> runResultsDetailWriteRepository.save(p));
			logger.info("addRunResults() -> end of addRunResults Execution");
			return Response.ok(runResultsCreated.getId()).status(200).build();
		} catch (Exception exp) {
			logger.error("Error while saving the Run result : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
	}

	/**
	 * Post sample results.
	 * 
	 * @param sampleResultsDTO
	 * @return the Response
	 * @throws HMTPException
	 */
	@Override
	public Response postSampleResults(SampleResultsDTO sampleResultsDTO) throws HMTPException {
		List<SampleResults> sampleResultsList;
		logger.info("postSampleResults() -> Start of postSampleResults Execution");
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			if (sampleResultsDTO.getInputContainerPosition() == null) {
				sampleResultsList = sampleResultsReadRepository.findByAccesssioningIdAndInputContainerId(
						sampleResultsDTO.getAccesssioningId(), sampleResultsDTO.getInputContainerId(), domainId);
			} else {
				sampleResultsList = sampleResultsReadRepository
						.findByAccesssioningIdAndInputContainerIdAndInputContainerPosition(
								sampleResultsDTO.getAccesssioningId(), sampleResultsDTO.getInputContainerId(),
								sampleResultsDTO.getInputContainerPosition(), domainId);
			}
			if ((sampleResultsList.isEmpty()) && sampleResultsDTO.getRunResultsId() != null) {
				return addSampleResults(sampleResultsDTO);
			} else {
				SampleResults sampleResult = sampleResultsList.get(0);
				return updateSampleResults(sampleResultsDTO, sampleResult);
			}
		} catch (Exception exp) {
			logger.error("Error while saving or updating the Sample result : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
	}

	/**
	 * Adds the sample results.
	 * 
	 * @param sampleResultsDTO
	 * @return the Response
	 * @throws HMTPException
	 */
	public Response addSampleResults(SampleResultsDTO sampleResultsDTO) throws HMTPException {
		logger.info("addSampleResults() -> Start of addSampleResults Execution");
		RunResults runResult = null;
		try {
			if (sampleResultsDTO.getRunResultsId() == null)
				throw new HMTPException("Run Result Id cannot be null");
			else
				runResult = runResultsReadRepository.findOne(sampleResultsDTO.getRunResultsId());

			SampleResults sampleResults = mapDTOToEntity.getSampleResultsFromDTO(sampleResultsDTO);
			List<SampleReagentsAndConsumables> sampleReagentsAndConsumables = mapDTOToEntity
					.getSampleReagentsAndConsumablesFromDTO(sampleResultsDTO);
			List<SampleResultsDetail> sampleResultsDetail = mapDTOToEntity
					.getSampleResultsDetailFromDTO(sampleResultsDTO);
			List<SampleProtocol> sampleProtocol = mapDTOToEntity.getSampleProtocolFromDTO(sampleResultsDTO);

			// setting the references as per relationship
			sampleResults.setRunResultsId(runResult);
			sampleReagentsAndConsumables.stream().forEach(p -> p.setSampleResultsMappedToReagents(sampleResults));
			sampleResultsDetail.stream().forEach(p -> p.setSampleResultsMappedToSampleDetail(sampleResults));
			sampleProtocol.stream().forEach(p -> p.setSampleResultsMappedToSampleProtocol(sampleResults));

			// saving the sample results in db
			runResultsWriteRepository.save(runResult);
			SampleResults sampleResultsCreated = sampleResultsWriteRepository.save(sampleResults);
			sampleReagentsAndConsumables.stream().forEach(p -> sampleReagentsAndConsumablesWriteRespository.save(p));
			sampleResultsDetail.stream().forEach(p -> sampleResultsDetailWriteRepository.save(p));
			sampleProtocol.stream().forEach(p -> sampleProtocolWriteRepository.save(p));
			logger.info("addSampleResults() -> end of addSampleResults Execution");
			return Response.ok(sampleResultsCreated.getId()).status(200).build();
		} catch (Exception exp) {
			logger.error("Error while saving the Sample result : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
	}

	/**
	 * Update sample results.
	 * 
	 * @param sampleResultsDTO
	 * @param sampleResult
	 * @return the Response
	 * @throws HMTPException
	 */
	public Response updateSampleResults(SampleResultsDTO sampleResultsDTO, SampleResults sampleResult)
			throws HMTPException {
		try {
			logger.info("updateSampleResults() -> Start of updateSampleResults Execution");
			List<SampleReagentsAndConsumables> sampleReagentsAndConsumables = null;
			List<SampleResultsDetail> sampleResultsDetail = null;
			sampleResult.setOutputContainerId(sampleResultsDTO.getOutputContainerId());
			sampleResult.setOutputContainerPosition(sampleResultsDTO.getOutputContainerPosition());
			sampleResult.setStatus(sampleResultsDTO.getStatus());
			sampleResult.setUpdatedBy(sampleResultsDTO.getUpdatedBy());
			sampleResult.setUpdatedDateTime(sampleResultsDTO.getUpdatedDateTime());
			sampleResult.setFlag(sampleResultsDTO.getFlag());
			sampleResult.setResult(sampleResultsDTO.getResult());
			sampleResult.setOutputContainerType(sampleResultsDTO.getOutputContainerType());
			sampleResult.setOutputPlateType(sampleResultsDTO.getOutputPlateType());
			sampleResult.setComments(sampleResultsDTO.getComments());
			sampleResult.setCreatedBy(sampleResultsDTO.getCreatedBy());
			sampleResult.setCreatedDate(sampleResultsDTO.getCreatedDateTime());
			sampleResult.setSampleType(sampleResultsDTO.getSampleType());
			List<SampleProtocol> sampleProtocolFromDB = null;
			if (sampleResultsDTO.getSampleProtocol() != null && !sampleResultsDTO.getSampleProtocol().isEmpty()) {
				sampleProtocolFromDB = new ArrayList<>();
				for (SampleProtocolDTO sampleProtocolDTO : sampleResultsDTO.getSampleProtocol()) {
					SampleProtocol protocol = new SampleProtocol();
					protocol.setId(sampleProtocolDTO.getSampleProtocolId());
					protocol.setProtocolName(sampleProtocolDTO.getProtocolName());
					protocol.setSampleResultsMappedToSampleProtocol(sampleResult);
					protocol.setUpdatedBy(sampleProtocolDTO.getUpdatedBy());
					protocol.setUpdatedDateTime(sampleProtocolDTO.getUpdatedDateTime());
					sampleProtocolFromDB.add(protocol);
				}
			}

			// updating sample reagents and consumables
			List<SampleReagentsAndConsumables> sampleReagentsAndConsumablesFromDB = null;
			List<SampleReagentsAndConsumables> newsampleReagentsAndConsumablesList = new ArrayList<>();
			if (sampleResultsDTO.getSampleReagentsAndConsumables() != null
					&& !sampleResultsDTO.getSampleReagentsAndConsumables().isEmpty()) {
				sampleReagentsAndConsumablesFromDB = (List<SampleReagentsAndConsumables>) sampleResult
						.getSampleReagentsAndConsumables();
				sampleReagentsAndConsumables = (mapDTOToEntity
						.getSampleReagentsAndConsumablesFromDTO(sampleResultsDTO));
				sampleResult.setSampleReagentsAndConsumables(null);

				if (sampleReagentsAndConsumablesFromDB != null)
					sampleReagentsAndConsumablesWriteRespository.delete(sampleReagentsAndConsumablesFromDB);

				for (SampleReagentsAndConsumables sp : sampleReagentsAndConsumables) {
					sp.setSampleResultsMappedToReagents(sampleResult);
					newsampleReagentsAndConsumablesList.add(sp);
				}
			}

			// updating sample details
			List<SampleResultsDetail> sampleResultsDetailFromDB = null;
			List<SampleResultsDetail> newsampleResultsDetailList = new ArrayList<>();
			if (sampleResultsDTO.getSampleResultsDetail() != null
					&& !sampleResultsDTO.getSampleResultsDetail().isEmpty()) {
				sampleResultsDetailFromDB = (List<SampleResultsDetail>) sampleResult.getSampleResultsDetail();
				sampleResultsDetail = (mapDTOToEntity.getSampleResultsDetailFromDTO(sampleResultsDTO));
				for (SampleResultsDetail sp : sampleResultsDetail) {
					Boolean sampleDetailsPresent = false;
					for (SampleResultsDetail spDB : sampleResultsDetailFromDB) {
						if (spDB.getAttributeName().equalsIgnoreCase(sp.getAttributeName())) {
							spDB.setUpdatedBy(sp.getUpdatedBy());
							spDB.setUpdatedDateTime(sp.getUpdatedDateTime());
							spDB.setAttributeValue(sp.getAttributeValue());
							sampleDetailsPresent = true;
							break;
						}
					}
					if (!sampleDetailsPresent) {
						sp.setSampleResultsMappedToSampleDetail(sampleResult);
						newsampleResultsDetailList.add(sp);
					}
				}
			}

			if (sampleResult != null)
				sampleResultsWriteRepository.save(sampleResult);
			if (sampleProtocolFromDB != null)
				sampleProtocolWriteRepository.save(sampleProtocolFromDB);
			if (sampleReagentsAndConsumablesFromDB != null)
				sampleReagentsAndConsumablesWriteRespository.save(newsampleReagentsAndConsumablesList);
			if (sampleResultsDetailFromDB != null)
				sampleResultsDetailWriteRepository.save(newsampleResultsDetailList);

			logger.info("updateSampleResults() -> End of updateSampleResults Execution");
			return Response.ok("Sample Result updated successfully").status(200).build();
		} catch (Exception exp) {
			logger.error("Error while updating sample results : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
	}

	/**
	 * Update run results.
	 * 
	 * @param runResultsDTO
	 * @return the Response
	 * @throws HMTPException
	 */
	@Override
	public Response updateRunResults(RunResultsDTO runResultsDTO) throws HMTPException {
		logger.info("updateRunResults() -> Start of updateRunResults Execution");
		logger.info("runResultsDTO" + runResultsDTO.toString());

		RunResults runResults = null;
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			runResults = runResultsReadRepository.findRunResultByRunResultId(runResultsDTO.getRunResultId(),domainId);
			if (runResults != null) {
				runResults = mapDTOToEntity.getRunResultsFromDTO(runResultsDTO);
				long ownerId = runResultsReadRepository.findCompanyIdByRunResultId(runResultsDTO.getRunResultId());
				if (ownerId != domainId) {
					logger.error("No OwnerId found for this run id");
					throw new HMTPException("No OwnerId found for this run id :- " + runResultsDTO.getRunResultId());
				}
				
				List<RunReagentsAndConsumables> reagentsAndConsumables = mapDTOToEntity
						.getRunReagentsAndConsumablesFromDTO(runResultsDTO);
				reagentsAndConsumables.stream().forEach(
						reagentsAndConsumable -> runReagentsAndConsumablesWriteRepository.save(reagentsAndConsumable));
				List<RunResultsDetail> runResultsDetails = mapDTOToEntity.getRunResultsDetailFromDTO(runResultsDTO);
				runResultsDetails.stream()
						.forEach(runResultsDetail -> runResultsDetailWriteRepository.save(runResultsDetail));
				runResultsWriteRepository.save(runResults, ownerId);
			} else {
				logger.error("RunResult is not available for this RunID");
				return Response.ok("RunId Not available in RMM :" + runResultsDTO.getRunResultId())
						.status(Status.NOT_FOUND).build();
			}
			logger.info("updateRunResults() -> End of updateRunResults Execution");
			return Response.ok("RunResult updated successfully").status(200).build();
		} catch (Exception exp) {
			logger.error("Error while fetching the sample lists : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
	}

	/**
	 * Gets the process step results.
	 * 
	 * @param accessioningId
	 * @return the process step results
	 * @throws HMTPException
	 */
	@Override
	public Response getProcessStepResults(String accessioningId) throws HMTPException {
		SampleResults sampleResults = null;
		List<ProcessStepValuesDTO> processStepActionValues = new ArrayList<>();
		try {
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			logger.info("getProcessStepResults() -> Start of getProcessStepResults Execution");
			if (StringUtils.isNotEmpty(accessioningId)) {
				List<Long> sampleResultids = sampleResultsReadRepository.findIdByAccesssioningId(accessioningId,
						domainId);
				for (Long sampleResultid : sampleResultids) {
					sampleResults = sampleResultsReadRepository.findOne(sampleResultid);
					ProcessStepValuesDTO processStepActionValue = mapEntityToDTO
							.mapsampleResultstoProcessDTO(sampleResults);
					processStepActionValues.add(processStepActionValue);
				}
				logger.info("End of getProcessStepResults() execution");
				return Response.ok(processStepActionValues, MediaType.APPLICATION_JSON).status(200).build();
			} else {
				throw new HMTPException(CustomErrorCodes.ACCESSIONING_ID_NULL,
						RMMConstant.ErrorMessages.ACCESSIONING_ID_NULL.toString());
			}
		} catch (HMTPException exp) {
			logger.error("Error while getting the ProcessStepAction for accessioningId:" + accessioningId);
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception e) {
			logger.error("Error while getting the ProcessStepAction for accessioningId:" + accessioningId);
			throw new HMTPException(e.getMessage());
		}
	}

	/**
	 * Gets the run results by device run id.
	 * 
	 * @param deviceRunId
	 * @return the run results by device run id
	 * @throws HMTPException
	 */
	@Override
	public Response getRunResultsByDeviceRunId(String deviceRunId) throws HMTPException {
		try {
			logger.info("getRunResultsByDeviceRunId() -> Start of getRunResultsByDeviceRunId Execution");
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			if (StringUtils.isNotEmpty(deviceRunId)) {
				RunResults runResults = runResultsReadRepository.findRunResultsByDeviceRunId(deviceRunId, domainId);
				if (runResults != null) {
					ArrayList<SampleResultsDTO> sampleresultslist = new ArrayList<>();
					RunResultsDTO runresultsdto = mapEntityToDTO.getRunResultsDTOFromRunResults(runResults);
					List<RunResultsDetailDTO> runresultdetaildto = mapEntityToDTO
							.convertRunResultsDetailToRunResultsDetailDTODTO(
									(List<RunResultsDetail>) runResults.getRunResultsDetail());
					runresultsdto.setRunResultsDetail(runresultdetaildto);
					List<RunReagentsAndConsumablesDTO> runReagentsAndConsumablesDTO = mapEntityToDTO
							.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO(
									(List<RunReagentsAndConsumables>) runResults.getRunReagentsAndConsumables());
					runresultsdto.setRunReagentsAndConsumables(runReagentsAndConsumablesDTO);
					List<SampleResults> sampleResults = (List<SampleResults>) runResults.getSampleResults();
					for (SampleResults sampleResult : sampleResults) {
						SampleResultsDTO sampleResultsDTO = mapEntityToDTO.getSampleDTOFromSampleResults(sampleResult);
						List<SampleProtocolDTO> sampleProtocolDTo = mapEntityToDTO
								.convertSampleProtocolToSampleProtocolDTO(
										(List<SampleProtocol>) sampleResult.getSampleProtocol());
						sampleResultsDTO.setSampleProtocol(sampleProtocolDTo);
						List<SampleReagentsAndConsumablesDTO> samplereagentdto = mapEntityToDTO
								.convertSampleReagentsAndConsumablesToReagentDTO(
										(List<SampleReagentsAndConsumables>) sampleResult
												.getSampleReagentsAndConsumables());
						sampleResultsDTO.setSampleReagentsAndConsumables(samplereagentdto);
						List<SampleResultsDetailDTO> sampledetaildto = mapEntityToDTO
								.convertSampleResultsDetailToSampleResultsDetailDTO(
										(List<SampleResultsDetail>) sampleResult.getSampleResultsDetail());
						sampleResultsDTO.setSampleResultsDetail(sampledetaildto);
						sampleresultslist.add(sampleResultsDTO);
					}
					runresultsdto.setSampleResults(sampleresultslist);
					logger.info("End of getRunResultsByDeviceRunId() execution");
					return Response.ok(runresultsdto, MediaType.APPLICATION_JSON).status(200).build();

				}
				return Response.status(Status.NO_CONTENT).build();
			} else {
				throw new HMTPException(CustomErrorCodes.DEVICE_RUN_ID_NULL,
						RMMConstant.ErrorMessages.DEVICE_RUN_ID_NULL.toString());
			}

		} catch (HMTPException exp) {
			logger.error("Exception in getting the getRunResultsByDeviceRunId details:" + this.getClass().getMethods(),
					exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exception) {
			logger.error("Exception in getting the getRunResultsByDeviceRunId details:" + this.getClass().getMethods(),
					exception.getMessage());
			throw new HMTPException(exception.getMessage());
		}
	}

	/**
	 * Gets the sampleand run results.
	 * 
	 * @param deviceRunId
	 * @param processStepName
	 * @return the sampleand run results
	 * @throws HMTPException
	 */
	@Override
	public Response getSampleandRunResults(String deviceRunId, String processStepName, String deviceid)
			throws HMTPException {

		RunResultsDTO runresultsdto = null;
		SampleResultsDTO sampleResultsDTO = null;
		ArrayList<SampleResultsDTO> sampleresultslist = new ArrayList<>();
		List<RunResults> runresults = new ArrayList<>();
		try {
			logger.info("getSampleandRunResults() -> Start of getSampleandRunResults Execution");
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			if (StringUtils.isEmpty(deviceRunId)) {
				throw new HMTPException(CustomErrorCodes.DEVICE_RUN_ID_NULL,
						RMMConstant.ErrorMessages.DEVICE_RUN_ID_NULL.toString());
			}
			if (StringUtils.isEmpty(processStepName)) {
				throw new HMTPException(CustomErrorCodes.PROCESS_STEP_NAME_NULL,
						RMMConstant.ErrorMessages.PROCESS_STEP_NAME_NULL.toString());
			}

			if (deviceid != null) {
				runresults = runResultsReadRepository.findByDeviceRunIdAndProcessStepNameAndDeviceId(deviceRunId,
						processStepName, deviceid, domainId);
			} else {
				runresults = runResultsReadRepository.findByDeviceRunIdAndProcessStepName(deviceRunId, processStepName,
						domainId);
			}

			if (!runresults.isEmpty()) {
				for (RunResults runresult : runresults) {
					runresultsdto = mapEntityToDTO.getRunResultsDTOFromRunResults(runresult);
					List<RunResultsDetailDTO> runresultdetaildto = mapEntityToDTO
							.convertRunResultsDetailToRunResultsDetailDTODTO(
									(List<RunResultsDetail>) runresult.getRunResultsDetail());
					runresultsdto.setRunResultsDetail(runresultdetaildto);
					List<RunReagentsAndConsumablesDTO> runReagentsAndConsumablesDTO = mapEntityToDTO
							.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO(
									(List<RunReagentsAndConsumables>) runresult.getRunReagentsAndConsumables());
					runresultsdto.setRunReagentsAndConsumables(runReagentsAndConsumablesDTO);
					List<SampleResults> sampleResults = (List<SampleResults>) runresult.getSampleResults();
					for (SampleResults sampleResult : sampleResults) {
						sampleResultsDTO = mapEntityToDTO.getSampleDTOFromSampleResults(sampleResult);
						List<SampleProtocolDTO> sampleProtocolDTo = mapEntityToDTO
								.convertSampleProtocolToSampleProtocolDTO(
										(List<SampleProtocol>) sampleResult.getSampleProtocol());
						sampleResultsDTO.setSampleProtocol(sampleProtocolDTo);
						List<SampleReagentsAndConsumablesDTO> samplereagentdto = mapEntityToDTO
								.convertSampleReagentsAndConsumablesToReagentDTO(
										(List<SampleReagentsAndConsumables>) sampleResult
												.getSampleReagentsAndConsumables());
						sampleResultsDTO.setSampleReagentsAndConsumables(samplereagentdto);
						List<SampleResultsDetailDTO> sampledetaildto = mapEntityToDTO
								.convertSampleResultsDetailToSampleResultsDetailDTO(
										(List<SampleResultsDetail>) sampleResult.getSampleResultsDetail());
						sampleResultsDTO.setSampleResultsDetail(sampledetaildto);
						sampleresultslist.add(sampleResultsDTO);
					}

					runresultsdto.setSampleResults(sampleresultslist);
				}
				logger.info("End of getSampleandRunResults() execution");
				return Response.ok(runresultsdto, MediaType.APPLICATION_JSON).status(Status.OK).build();
			} else {
				throw new HMTPException(CustomErrorCodes.RUNRESULT_NOT_FOUND,
						RMMConstant.ErrorMessages.RUNRESULT_NOT_FOUND.toString());
			}
		} catch (HMTPException exp) {
			logger.error("Error while getting the getSampleandRunResults for deviceRunId:" + deviceRunId
					+ "processStepName:" + processStepName + "and deviceId:" + deviceid);
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exp) {
			logger.error("Error while getting the getSampleandRunResults for deviceRunId:" + deviceRunId
					+ "processStepName:" + processStepName + "and deviceId:" + deviceid);
			throw new HMTPException(exp.getMessage());
		}

	}

	/**
	 * Gets the run results.
	 * 
	 * @param sampleresultid
	 * @return the run results
	 * @throws HMTPException
	 */
	@Override
	public Response getRunResults(long sampleresultid) throws HMTPException {
		SampleResults sampleResults = null;
		try {

			logger.info("getRunResults() -> Start of getRunResults Execution");
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			sampleResults = sampleResultsReadRepository.findRunResultBySampleResultId(sampleresultid, domainId);
			if (sampleResults != null) {
				RunResultsDTO runResultsDTO = mapEntityToDTO.convertToDTO(sampleResults);
				logger.info("End of getRunResults() execution");
				return Response.ok(runResultsDTO, MediaType.APPLICATION_JSON).status(Status.OK).build();
			} else {
				throw new HMTPException(CustomErrorCodes.SAMPLERESULTS_NOT_FOUND,
						RMMConstant.ErrorMessages.SAMPLERESULTS_NOT_FOUND.toString());
			}
		} catch (HMTPException exp) {
			logger.error(
					"Error while getting the run results : " + "sampleresultid:" + sampleresultid + exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exp) {
			logger.error(
					"Error while getting the run results : " + "sampleresultid:" + sampleresultid + exp.getMessage());
			throw new HMTPException(exp.getMessage());

		}

	}

	/**
	 * Finds the inprogress status by device ID.
	 * 
	 * @param deviceId
	 * @param runStatus
	 *            the run status
	 * @return the Response
	 * @throws HMTPException
	 */
	@Override
	public Response findInprogressStatusByDeviceID(String deviceId, String runStatus) throws HMTPException {
		List<RunResults> listRunResults = null;
		List<SampleResultsDTO> sampleResultsDtos = new ArrayList<>();
		try {
			logger.info("findInprogressStatusByDeviceID() -> Start of findInprogressStatusByDeviceID Execution");
			if (StringUtils.isEmpty(deviceId)) {
				throw new HMTPException(CustomErrorCodes.DEVICE_RUN_ID_NULL,
						RMMConstant.ErrorMessages.DEVICE_RUN_ID_NULL.toString());
			}
			if (StringUtils.isEmpty(runStatus)) {
				throw new HMTPException(CustomErrorCodes.RUNSTATUS_NULL,
						RMMConstant.ErrorMessages.RUNRESULT_STATUS_NULL.toString());
			}
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			listRunResults = runResultsReadRepository.findByDeviceIdAndRunStatus(deviceId, runStatus, domainId);
			if (!listRunResults.isEmpty()) {
				for (RunResults runResults : listRunResults) {
					for (SampleResults sampleResult : runResults.getSampleResults()) {

						sampleResultsDtos.add(mapEntityToDTO.getSampleDTOFromSampleResults(sampleResult));
					}
				}
				logger.info("findInprogressStatusByDeviceID() -> End of findInprogressStatusByDeviceID Execution");
				return Response.ok(sampleResultsDtos, MediaType.APPLICATION_JSON).build();
			} else {
				return Response.status(Status.NO_CONTENT).build();
			}

		} catch (HMTPException exp) {
			logger.error("Error while getting the sample results : " + "deviceId:" + deviceId + "runStatus:" + runStatus
					+ exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		}

		catch (Exception exp) {
			logger.error("Error while getting the sample results : " + "deviceId:" + deviceId + "runStatus:" + runStatus
					+ exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
	}

	/**
	 * Gets the all details by run results id.
	 * 
	 * @param runId
	 *            the run id
	 * @return the all details by run results id
	 * @throws HMTPException
	 */
	
	@Override
	public Response getAllDetailsByRunResultsId(String runId) throws HMTPException {
		RunResults runResults = null;
		ArrayList<SampleResultsDTO> sampleresultslist = new ArrayList<>();
		RunResultsDTO runresultsDTO = null;
		Long runID =null;
		try {
			
			logger.info("getAllDetailsByRunResultsId() -> Start of getAllDetailsByRunResultsId Execution");
			if (runId == null) {
				throw new HMTPException(CustomErrorCodes.RUNRESULT_ID_NULL,
						RMMConstant.ErrorMessages.RUNRESULT_ID_NULL.toString());
			}
			runID = Long.parseLong(runId);
			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			runResults = runResultsReadRepository.findRunResultByRunResultId(runID, domainId);
			if (runResults == null) {
				throw new HMTPException(CustomErrorCodes.RUNRESULT_NOT_FOUND,
						RMMConstant.ErrorMessages.RUNRESULT_NOT_FOUND.toString());
			} else {
				runresultsDTO = mapEntityToDTO.getRunResultsDTOFromRunResults(runResults);
				List<RunResultsDetailDTO> runresultdetaildto = mapEntityToDTO
						.convertRunResultsDetailToRunResultsDetailDTODTO(
								(List<RunResultsDetail>) runResults.getRunResultsDetail());
				runresultsDTO.setRunResultsDetail(runresultdetaildto);
				List<RunReagentsAndConsumablesDTO> runReagentsAndConsumablesDTO = mapEntityToDTO
						.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO(
								(List<RunReagentsAndConsumables>) runResults.getRunReagentsAndConsumables());
				runresultsDTO.setRunReagentsAndConsumables(runReagentsAndConsumablesDTO);
				List<SampleResults> sampleResults = (List<SampleResults>) runResults.getSampleResults();
				for (SampleResults sampleResult : sampleResults) {
					SampleResultsDTO sampleResultsDTO = mapEntityToDTO.getSampleDTOFromSampleResults(sampleResult);
					sampleresultslist.add(sampleResultsDTO);
				}
				List<SampleResultsDTO> sampleResultsListFinal = orderIntegrationService.getMandatoryFieldValidations(sampleresultslist);
				runresultsDTO.setSampleResults(sampleResultsListFinal);

				List<UniqueReagentsAndConsumables> uniqueReagentsAndConsumables = new ArrayList<>();
				for (SampleResultsDTO sampleResultsDTO : sampleresultslist) {
					if (sampleResultsDTO.getSampleReagentsAndConsumables() != null
							&& !sampleResultsDTO.getSampleReagentsAndConsumables().isEmpty()) {

						for (SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO : sampleResultsDTO
								.getSampleReagentsAndConsumables()) {
							UniqueReagentsAndConsumables uniqueReagentsAndConsumable = new UniqueReagentsAndConsumables();
							uniqueReagentsAndConsumable
									.setAttributeName(sampleReagentsAndConsumablesDTO.getAttributeName());
							uniqueReagentsAndConsumable
									.setAttributeValue(sampleReagentsAndConsumablesDTO.getAttributeValue());

							if (uniqueReagentsAndConsumables != null && !uniqueReagentsAndConsumables.isEmpty()) {
								int inc = 0;
								for (UniqueReagentsAndConsumables reagentsAndConsumable : uniqueReagentsAndConsumables) {
									if (reagentsAndConsumable.equals(uniqueReagentsAndConsumable)) {
										break;
									} else {
										inc++;
									}
								}
								if (inc == uniqueReagentsAndConsumables.size()) {
									uniqueReagentsAndConsumables.add(uniqueReagentsAndConsumable);
								}
							} else {
								uniqueReagentsAndConsumables.add(uniqueReagentsAndConsumable);
							}
						}

					}
				}
				
				if (runReagentsAndConsumablesDTO != null && !runReagentsAndConsumablesDTO.isEmpty()) {
					for (RunReagentsAndConsumablesDTO runReagentsAndConsumables : runReagentsAndConsumablesDTO) {
						UniqueReagentsAndConsumables uniqueReagentsAndConsumable = new UniqueReagentsAndConsumables();
						uniqueReagentsAndConsumable.setAttributeName(runReagentsAndConsumables.getAttributeName());
						uniqueReagentsAndConsumable.setAttributeValue(runReagentsAndConsumables.getAttributeValue());
						if (uniqueReagentsAndConsumables != null && !uniqueReagentsAndConsumables.isEmpty()) {
							int inc = 0;
							for (UniqueReagentsAndConsumables reagentsAndConsumable : uniqueReagentsAndConsumables) {
								if (reagentsAndConsumable.equals(uniqueReagentsAndConsumable)) {
									break;
								} else {
									inc++;
								}
							}
							if (inc == uniqueReagentsAndConsumables.size()) {
								uniqueReagentsAndConsumables.add(uniqueReagentsAndConsumable);
							}
						} else {
							uniqueReagentsAndConsumables.add(uniqueReagentsAndConsumable);
						}
					}

				}
				
				runresultsDTO.setUniqueReagentsAndConsumables(uniqueReagentsAndConsumables);
				logger.info("End of getAllDetailsByRunResultsId() execution");
				return Response.ok(runresultsDTO, MediaType.APPLICATION_JSON).status(Status.OK).build();
			}

		} catch (HMTPException exp) {
			logger.error(
					"Error while getting the Runresult by RunResultId :  " + "RunResultId:" + runID + exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		}
		catch (NumberFormatException exp) {
			logger.error(
					"Error while getting the Runresult by RunResultId :  " + "RunResultId:" + runID + exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(CustomErrorCodes.RUNRESULT_NOT_FOUND.getErrorCode());
			errResponse.setErrorMessage(RMMConstant.ErrorMessages.RUNRESULT_NOT_FOUND.toString());
			return Response.status(errResponse.getErrorStatusCode()).entity(errResponse).build();
		}

		catch (Exception exp) {
			logger.error(
					"Error while getting the Runresult by RunResultId :  " + "RunResultId:" + runId + exp.getMessage());
			return Response.status(500).build();
		}
	}

	/**
	 * Gets the run results by process step name.
	 * 
	 * @param processStepName
	 * @return the run results by process step name
	 * @throws HMTPException
	 */
	@Override
	public Response getRunResultsByProcessStepName(String processStepName) throws HMTPException {
		logger.info("getRunResultsByProcessStepName() -> Start of getRunResultsByProcessStepName Execution");
		List<RunResults> runResultsList = null;
		List<RunResultsDTO> runResultsDTOList = new ArrayList<>();
		try {
			if (StringUtils.isNotEmpty(processStepName)) {
				RunResultsTypeSpecification runResultsTypeSpecification;
				runResultsTypeSpecification = new RunResultsTypeSpecification(processStepName);
				runResultsList = runResultsReadRepository.findAll(runResultsTypeSpecification);
			} else {
				throw new HMTPException(CustomErrorCodes.PROCESS_STEP_NAME_NULL,
						RMMConstant.ErrorMessages.PROCESS_STEP_NAME_NULL.toString());
			}

			if (!runResultsList.isEmpty()) {
				for (RunResults runResults : runResultsList) {
					RunResultsDTO runresultsdto = mapEntityToDTO.getRunResultsDTOFromRunResults(runResults);
					runResultsDTOList.add(runresultsdto);
				}
				logger.info("getRunResultsByProcessStepName() -> End of getRunResultsByProcessStepName Execution");
				return Response.ok(runResultsDTOList, MediaType.APPLICATION_JSON).status(Status.OK).build();
			} else {
				return Response.status(Status.NO_CONTENT).build();
			}
		} catch (HMTPException exp) {
			logger.error("Exception in getting the getRunResultsByProcessStepName details:", exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exception) {
			logger.error(
					"Exception in getting the getRunResultsByProcessStepName details:" + this.getClass().getMethods(),
					exception.getMessage());
			throw new HMTPException(exception.getMessage());
		}
	}

	/**
	 * Gets the sample results.
	 * 
	 * @param deviceId
	 * @param processStepName
	 * @param outputContainerId
	 * @param inputContainerId
	 * @param inputContainerPosition
	 * @param outputContainerPosition
	 * @return the sample results
	 * @throws HMTPException
	 */
	@Override
	public Response getSampleResults(String deviceId, String processStepName, String outputContainerId,
			String inputContainerId, String inputContainerPosition, String outputContainerPosition,
			String accessioningId) throws HMTPException {
		logger.info("getSampleResults() -> Start of getSampleResults Execution");
		List<SampleResults> result = new ArrayList<>();
		List<SampleResultsDTO> sampleResultsDTO = new ArrayList<>();
		try {

			if (deviceId != null || processStepName != null || outputContainerId != null
					|| outputContainerPosition != null || inputContainerId != null || inputContainerPosition != null
					|| accessioningId != null) {

				result = sampleResultsReadRepository
						.findAll(RunResultsSpecifications.getSampleResults(deviceId, processStepName, outputContainerId,
								inputContainerId, inputContainerPosition, outputContainerPosition, accessioningId));
				if (!result.isEmpty()) {
					for (SampleResults sampleResult : result) {
						sampleResultsDTO.add(mapEntityToDTO.getSampleDTOFromSampleResults(sampleResult));
					}
				} else {
					return Response.status(Status.NO_CONTENT).build();
				}

			} else {
				throw new HMTPException(CommonErrorCodeConstants.INVALID_ARGUMENTS,
						"deviceId or processStepName or outputContainerId or inputContainerId or inputContainerPosition or outputContainerPosition value is Empty");
			}
		} catch (HMTPException exp) {
			logger.error("Exception in getting the getSampleResults :", exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exp) {
			logger.error("Exception in getting the getSampleResults :", exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
		logger.info("getSampleResults() -> End of getSampleResults Execution");
		return Response.ok(sampleResultsDTO, MediaType.APPLICATION_JSON).status(Status.OK).build();

	}

	/**
	 * Gets the list of active Runresults for ongoing case.
	 * 
	 * @param assayType
	 *            the assay type
	 * @param status
	 *            the status
	 * @param processstep
	 *            the processstep
	 * @return the list of RunResultsDTO
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	// For RunDashbaord active runs to be displayed based on assayType,status or
	// processstep
	@Override
	public Response listActiveRuns(String assayType, String status, String processstep) throws HMTPException {
		logger.info("listActiveRuns() -> Start of listActiveRuns Execution");
		List<RunResults> runResultsList = new ArrayList<>();
		List<RunResultsDTO> runResultsDTOList = new ArrayList<>();
		try {
			if (StringUtils.isNotEmpty(assayType) || StringUtils.isNotEmpty(status)) {
				// for getting AssayTypeDetails
				if (status.equalsIgnoreCase(RMMConstant.RunStatus.INPROGRESS.toString())
						|| status.equalsIgnoreCase(RMMConstant.RunStatus.COMPLETED.toString())
						|| status.equalsIgnoreCase(RMMConstant.RunStatus.COMPLETEDWITHFLAGS.toString())
						|| status.equalsIgnoreCase(RMMConstant.RunStatus.FAILED.toString())
						|| status.equalsIgnoreCase(RMMConstant.RunStatus.ABORTED.toString())) {
					runResultsList = runResultsReadRepository.findAll(new Specification<RunResults>() {
						@Override
						public Predicate toPredicate(Root<RunResults> root, CriteriaQuery<?> query,
								CriteriaBuilder cb) {
							if (processstep == null) {
								return cb.and(cb.equal(root.get(RMMConstant.ASSAYTYPE).as(String.class), assayType),
										cb.equal(root.get(RMMConstant.RUNSTATUS).as(String.class), status));
							} else {
								return cb.and(cb.equal(root.get(RMMConstant.ASSAYTYPE).as(String.class), assayType),
										cb.equal(root.get(RMMConstant.PROCESSSTEPNAME).as(String.class), processstep),
										cb.equal(root.get(RMMConstant.RUNSTATUS).as(String.class), status));
							}
						}
					});

					if (!runResultsList.isEmpty()) {
						for (RunResults runResults : runResultsList) {
							RunResultsDTO runresultsDTO = mapEntityToDTO.getRunResultsDTOFromRunResults(runResults);
							List<SampleResults> sampleResults = (List<SampleResults>) runResults.getSampleResults();
							runresultsDTO.setTotalSamplecount(String.valueOf(sampleResults.size()));
							List<RunReagentsAndConsumablesDTO> runReagentsAndConsumablesDTO=mapEntityToDTO.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO((List<RunReagentsAndConsumables>) runResults.getRunReagentsAndConsumables());
							runresultsDTO.setRunReagentsAndConsumables(runReagentsAndConsumablesDTO);
							List<SampleResultsDTO> sampleresultslist = new ArrayList<>();
							int sampleFlagCount = 0;
							int sampleFailedCount = 0;
							for (SampleResults sampleResult : sampleResults) {
								SampleResultsDTO sampleResultsDTO = mapEntityToDTO
										.getSampleDTOFromSampleResults(sampleResult);
								// added for RC-8707
								if (sampleResult.getFlag() != null && !sampleResult.getFlag().isEmpty()) {
									sampleFlagCount = sampleFlagCount + 1;
								}
								if ((sampleResult.getStatus()
										.equalsIgnoreCase(RMMConstant.RunStatus.ABORTED.toString()))
										|| (sampleResult.getStatus()
												.equalsIgnoreCase(RMMConstant.RunStatus.FAILED.toString()))) {
									sampleFailedCount = sampleFailedCount + 1;
								}
								sampleResultsDTO.setSampleReagentsAndConsumables(null);
								sampleResultsDTO.setSampleResultsDetail(null);
								sampleResultsDTO.setSampleProtocol(null);
								sampleresultslist.add(sampleResultsDTO);

							}
							if (sampleFlagCount > 0) {
								runresultsDTO.setTotalSampleFlagCount(sampleFlagCount);
							}
							if (sampleFailedCount > 0) {
								if (((runresultsDTO.getProcessStepName().equalsIgnoreCase(RMMConstant.ProcessStepValues.NAEXTRACTION.toString()))
										&& (runresultsDTO.getAssayType().equalsIgnoreCase(RMMConstant.ASSAYTYPENIPTDPCR)))||
										((runresultsDTO.getProcessStepName().equalsIgnoreCase(RMMConstant.ProcessStepValues.SEQUENCING.toString()))
										&& (runresultsDTO.getAssayType().equalsIgnoreCase(RMMConstant.ASSAYTYPENIPTHTP))) ||
										((runresultsDTO.getProcessStepName().equalsIgnoreCase(RMMConstant.ProcessStepValues.LIBRARYPREPARATION.toString()))
										&& (runresultsDTO.getAssayType().equalsIgnoreCase(RMMConstant.ASSAYTYPENIPTDPCR))))  {
									if ((sampleFailedCount != Integer.parseInt(runresultsDTO.getTotalSamplecount()))) {
										runresultsDTO.setTotalSampleFailedCount(sampleFailedCount);
									}
								} else if ((runresultsDTO.getAssayType().equalsIgnoreCase(RMMConstant.ASSAYTYPENIPTHTP))
										|| (runresultsDTO.getAssayType()
												.equalsIgnoreCase(RMMConstant.ASSAYTYPENIPTDPCR))
												&& (sampleFailedCount != Integer
														.parseInt(runresultsDTO.getTotalSamplecount()))) {
									runresultsDTO.setTotalSampleFailedCount(sampleFailedCount);
								}
							}

							runresultsDTO.setSampleResults(sampleresultslist);
							Map<String, Integer> inputContainerIdshm = new HashMap<>();
							Map<String, Integer> outputcontainerIdhm = new HashMap<>();
							List<InputStripDetailsDTO> inputcontainerIds = new ArrayList<>();
							List<OutputStripDetailsDTO> outputcontainerIds = new ArrayList<>();
							// for getting strip details and count
							for (SampleResultsDTO sampleResultDTO : sampleresultslist) {
								String inputcontainerId = sampleResultDTO.getInputContainerId();
								String outputcontainerId = sampleResultDTO.getOutputContainerId();
								Integer j = inputContainerIdshm.get(inputcontainerId);
								inputContainerIdshm.put(inputcontainerId, (j == null) ? 1 : j + 1);
								Integer k = outputcontainerIdhm.get(outputcontainerId);
								outputcontainerIdhm.put(outputcontainerId, (k == null) ? 1 : k + 1);
							}

							for (Map.Entry<String, Integer> entry : inputContainerIdshm.entrySet()) {
								InputStripDetailsDTO inputStripDetails = new InputStripDetailsDTO();
								String key = entry.getKey();
								String samplecounts = entry.getValue().toString();
								inputStripDetails.setStripId(key);
								inputStripDetails.setSamplecounts(samplecounts);
								inputcontainerIds.add(inputStripDetails);
							}

							for (Map.Entry<String, Integer> entry : outputcontainerIdhm.entrySet()) {
								OutputStripDetailsDTO outputStripDetails = new OutputStripDetailsDTO();
								String key = entry.getKey();
								String samplecounts = entry.getValue().toString();
								outputStripDetails.setStripId(key);
								outputStripDetails.setSamplecounts(samplecounts);
								outputcontainerIds.add(outputStripDetails);
							}
							runresultsDTO.setInputcontainerIds(inputcontainerIds);
							runresultsDTO.setOutputcontainerIds(outputcontainerIds);

							runResultsDTOList.add(runresultsDTO);
						}
					}
					// For sorting default support based on updated time stamp
					runResultsDTOList.sort(Comparator.comparing(RunResultsDTO::getUpdatedDateTime).reversed());
					// end
					logger.info("listActiveRuns() -> End of listActiveRuns Execution");
					return Response.ok(runResultsDTOList, MediaType.APPLICATION_JSON).status(Status.OK).build();
				}

			} else {

				throw new HMTPException(CustomErrorCodes.ASSAYTYPE_RUNSTATUS_PROCESS_STEP_NAME_NULL,
						RMMConstant.ErrorMessages.ASSAYTYPE_RUNSTATUS_PROCESS_STEP_NAME_NULL.toString());
			}
			return Response.status(Status.NO_CONTENT).build();
		} catch (HMTPException exp) {
			logger.error("Exception in getting the listActiveRuns details:", exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exception) {
			logger.error("Exception in getting the listActiveRuns details:" + this.getClass().getMethods(),
					exception.getMessage());
			throw new HMTPException(exception.getMessage());
		}
	}

	// for workflowIncompletedRuns
	/**
	 * Gets the list of active workflowIncompletedRuns for partial case and
	 * fully completed but next workflow started or pending with in next wfm
	 * step.
	 * 
	 * @param assayType
	 *            the assay type
	 * @param processstep
	 *            the processstep
	 * @return the list of RunResultsDTO
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Override
	public Response listInCompletedWFSActiveRuns(String assayType, String processstep) throws HMTPException {
		logger.info("listInCompletedWFSActiveRuns() -> Start of listInCompletedWFSActiveRuns Execution");
		List<RunResults> runResultsList = new ArrayList<>();
		List<RunResultsDTO> runResultsDTOList = new ArrayList<>();
		try {
			if (StringUtils.isNotEmpty(assayType) || StringUtils.isNotEmpty(processstep)) {
				// for getting AssayTypeDetails
				AssayTypeDTO assayTypeDTO = getAssayTypeList(assayType);
				if (assayTypeDTO == null)
					throw new HMTPException(CustomErrorCodes.ASSAYTYPE_NOT_FOUND,
							RMMConstant.ErrorMessages.ASSAYTYPE_NOT_FOUND.toString());

				List<ProcessStepActionDTO> processStepActionList = getProcessStepActionList(assayType);
				//
				ArrayList<String> wfmProcessStepsList = new ArrayList<>();
				for (ProcessStepActionDTO processStepActionDTO : processStepActionList) {
					String wfmProcVal = processStepActionDTO.getProcessStepName();
					wfmProcessStepsList.add(wfmProcVal);
				}

				if (wfmProcessStepsList.indexOf(processstep) == -1)
					throw new HMTPException(CustomErrorCodes.PROCESS_STEP_NAME_NOT_FOUND,
							RMMConstant.ErrorMessages.PROCESS_STEP_NAME_NOT_FOUND.toString());

				if (assayTypeDTO.getAssayType().equalsIgnoreCase(assayType)) {
					List<RunResults> pendingInNextRunList = new ArrayList<>();
					String statusValue = RMMConstant.RunStatus.COMPLETED.toString();
					runResultsList = runResultsReadRepository.findAll(new Specification<RunResults>() {
						@Override
						public Predicate toPredicate(Root<RunResults> root, CriteriaQuery<?> query,
								CriteriaBuilder cb) {
							return cb.and(cb.equal(root.get(RMMConstant.ASSAYTYPE).as(String.class), assayType),
									cb.equal(root.get(RMMConstant.PROCESSSTEPNAME).as(String.class), processstep),
									cb.like(root.get(RMMConstant.RUNSTATUS).as(String.class), statusValue + "%"));
						}
					});
					if (!runResultsList.isEmpty()) {
						for (RunResults runResults : runResultsList) {

							List<String> accessingIdList = getAccessingIdList(runResults);
							int wfmIndexValue = wfmProcessStepsList.indexOf((processstep));

							if (wfmIndexValue < wfmProcessStepsList.size()) {
								String nextWfmStep = wfmProcessStepsList.get(wfmIndexValue + 1);
								// for nextWFMstep chk
								boolean isExistInNextRun = true;
								List<RunResults> nextrunResultsList = runResultsReadRepository
										.findAll(new Specification<RunResults>() {
											@Override
											public Predicate toPredicate(Root<RunResults> root, CriteriaQuery<?> query,
													CriteriaBuilder cb) {
												return cb.and(
														cb.equal(root.get(RMMConstant.ASSAYTYPE).as(String.class), assayType),
														cb.equal(root.get(RMMConstant.PROCESSSTEPNAME).as(String.class),
																nextWfmStep));
											}
										});

								// for combining all accessingId in nextwfs
								List<String> nextAllaccessingIdList = new ArrayList<>();

								for (RunResults nextrunResults : nextrunResultsList) {
									List<String> nextaccessingIdList = getAccessingIdList(nextrunResults);
									nextAllaccessingIdList.addAll(nextaccessingIdList);
								}

								isExistInNextRun = nextAllaccessingIdList.containsAll(accessingIdList);
								if (isExistInNextRun) {
									pendingInNextRunList.add(runResults);
								}

							}
						}
						runResultsList.removeAll(pendingInNextRunList);
						if (!runResultsList.isEmpty()) {
							for (RunResults runResultsList1 : runResultsList) {
								RunResultsDTO runresultsDTO = mapEntityToDTO
										.getRunResultsDTOFromRunResults(runResultsList1);
								List<SampleResults> sampleResults = (List<SampleResults>) runResultsList1
										.getSampleResults();
								runresultsDTO.setTotalSamplecount(String.valueOf(sampleResults.size()));
								List<RunResultsDetailDTO> runresultdetaildto = mapEntityToDTO
										.convertRunResultsDetailToRunResultsDetailDTODTO(
												(List<RunResultsDetail>) runResultsList1.getRunResultsDetail());
								runresultsDTO.setRunResultsDetail(runresultdetaildto);
								List<RunReagentsAndConsumablesDTO> runReagentsAndConsumablesDTO = mapEntityToDTO
										.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO((List<RunReagentsAndConsumables>) runResultsList1.getRunReagentsAndConsumables());				
								runresultsDTO.setRunReagentsAndConsumables(runReagentsAndConsumablesDTO);
								List<SampleResultsDTO> sampleresultslist = new ArrayList<>();
								int sampleFlagCount = 0;
								int sampleFailedCount = 0;
								for (SampleResults sampleResult : sampleResults) {
									SampleResultsDTO sampleResultsDTO = mapEntityToDTO
											.getSampleDTOFromSampleResults(sampleResult);
									// added for RC-8707
									if (sampleResult.getFlag() != null && !sampleResult.getFlag().isEmpty()) {
										sampleFlagCount = sampleFlagCount + 1;
									}
									if ((sampleResult.getStatus().equalsIgnoreCase("Aborted"))
											|| (sampleResult.getStatus().equalsIgnoreCase("Failed"))) {
										sampleFailedCount = sampleFailedCount + 1;
									}
									sampleResultsDTO.setSampleReagentsAndConsumables(null);
									sampleResultsDTO.setSampleResultsDetail(null);
									sampleResultsDTO.setSampleProtocol(null);
									sampleresultslist.add(sampleResultsDTO);

								}
								if (sampleFlagCount > 0) {
									runresultsDTO.setTotalSampleFlagCount(sampleFlagCount);
								}
								if (sampleFailedCount > 0) {
									if ((runresultsDTO.getProcessStepName()
											.equalsIgnoreCase(RMMConstant.ProcessStepValues.NAEXTRACTION.toString()))
											&& (runresultsDTO.getAssayType()
													.equalsIgnoreCase(RMMConstant.ASSAYTYPENIPTDPCR))) {
										if ((sampleFailedCount != Integer
												.parseInt(runresultsDTO.getTotalSamplecount()))) {
											runresultsDTO.setTotalSampleFailedCount(sampleFailedCount);
										} else if (sampleFailedCount == Integer
												.parseInt(runresultsDTO.getTotalSamplecount())) {
											runresultsDTO.setRunStatus(RMMConstant.RunStatus.FAILED.toString());

										}
									} else if ((runresultsDTO.getAssayType()
											.equalsIgnoreCase(RMMConstant.ASSAYTYPENIPTHTP))
											|| (runresultsDTO.getAssayType()
													.equalsIgnoreCase(RMMConstant.ASSAYTYPENIPTDPCR))) {
										if ((sampleFailedCount != Integer
												.parseInt(runresultsDTO.getTotalSamplecount()))) {
											runresultsDTO.setTotalSampleFailedCount(sampleFailedCount);
										} else if (sampleFailedCount == Integer
												.parseInt(runresultsDTO.getTotalSamplecount())) {
											runresultsDTO.setRunStatus(RMMConstant.RunStatus.ABORTED.toString());
										}
									}
								}

								runresultsDTO.setSampleResults(sampleresultslist);
								runresultsDTO.setWfmsflag(RMMConstant.ONGOING);
								// for strip details chking
								Map<String, Integer> inputContainerIdshm = new HashMap<>();
								Map<String, Integer> outputcontainerIdhm = new HashMap<>();
								List<InputStripDetailsDTO> inputcontainerIds = new ArrayList<>();
								List<OutputStripDetailsDTO> outputcontainerIds = new ArrayList<>();
								// for getting strip details and count
								for (SampleResultsDTO sampleResultDTO : sampleresultslist) {
									String inputcontainerId = sampleResultDTO.getInputContainerId();
									String outputcontainerId = sampleResultDTO.getOutputContainerId();
									Integer j = inputContainerIdshm.get(inputcontainerId);
									inputContainerIdshm.put(inputcontainerId, (j == null) ? 1 : j + 1);
									Integer k = outputcontainerIdhm.get(outputcontainerId);
									outputcontainerIdhm.put(outputcontainerId, (k == null) ? 1 : k + 1);
								}

								for (Map.Entry<String, Integer> entry : inputContainerIdshm.entrySet()) {
									InputStripDetailsDTO inputStripDetails = new InputStripDetailsDTO();
									String key = entry.getKey();
									String samplecounts = entry.getValue().toString();
									inputStripDetails.setStripId(key);
									inputStripDetails.setSamplecounts(samplecounts);
									inputcontainerIds.add(inputStripDetails);
								}

								for (Map.Entry<String, Integer> entry : outputcontainerIdhm.entrySet()) {
									OutputStripDetailsDTO outputStripDetails = new OutputStripDetailsDTO();
									String key = entry.getKey();
									String samplecounts = entry.getValue().toString();
									outputStripDetails.setStripId(key);
									outputStripDetails.setSamplecounts(samplecounts);
									outputcontainerIds.add(outputStripDetails);
								}
								runresultsDTO.setInputcontainerIds(inputcontainerIds);
								runresultsDTO.setOutputcontainerIds(outputcontainerIds);
								runResultsDTOList.add(runresultsDTO);
							}
						}
					}
					// partial movement scenario case
					if (!processstep.equalsIgnoreCase(RMMConstant.ProcessStepValues.LPPREPCR.toString())
							&& !processstep.equalsIgnoreCase(RMMConstant.ProcessStepValues.LPPOSTPCR.toString())) {
						List<RunResults> partialInNextRunList = new ArrayList<>();
						if (!runResultsList.isEmpty()) {
							for (RunResults runResults : runResultsList) {
								List<String> accessingIdList = getAccessingIdList(runResults);
								int wfmIndexValue = wfmProcessStepsList.indexOf((processstep));
								if (wfmIndexValue < wfmProcessStepsList.size()) {
									String nextWfmStep = wfmProcessStepsList.get(wfmIndexValue + 1);
									// for nextWFMstep chk
									boolean fewExistInNextRun = false;
									List<RunResults> nextrunResultsList = runResultsReadRepository
											.findAll(new Specification<RunResults>() {
												@Override
												public Predicate toPredicate(Root<RunResults> root,
														CriteriaQuery<?> query, CriteriaBuilder cb) {
													return cb.and(
															cb.equal(root.get("assayType").as(String.class), assayType),
															cb.equal(root.get("processStepName").as(String.class),
																	nextWfmStep));
												}
											});

									// For combining all accessingId in nextwfs
									List<String> nextAllaccessingIdList = new ArrayList<>();

									for (RunResults nextrunResults : nextrunResultsList) {
										List<String> nextaccessingIdList = getAccessingIdList(nextrunResults);
										nextAllaccessingIdList.addAll(nextaccessingIdList);
									}

									for (RunResults nextrunResults : nextrunResultsList) {

										List<String> commonList = new ArrayList<>(accessingIdList);
										commonList.retainAll(nextAllaccessingIdList);
										// chking partial in nextrunlevel.
										if (!commonList.isEmpty()) {
											if (commonList.size() != accessingIdList.size()) {
												fewExistInNextRun = true;
												break;
											}
										}

									}

									if (fewExistInNextRun) {
										// chcking pending samples case
										List<SampleResults> sampleResults = (List<SampleResults>) runResults
												.getSampleResults();
										List<SampleResults> sampleresultslist = new ArrayList<>();
										for (SampleResults sampleResult : sampleResults) {
											if (!nextAllaccessingIdList.contains(sampleResult.getAccesssioningId())) {
												sampleresultslist.add(sampleResult);
											}

										}
										// end
										runResults.setSampleResults(sampleresultslist);
										partialInNextRunList.add(runResults);

									}

								}

							}
							if (!partialInNextRunList.isEmpty()) {
								for (RunResults runResultsList2 : partialInNextRunList) {
									RunResultsDTO runresultsDTO = mapEntityToDTO
											.getRunResultsDTOFromRunResults(runResultsList2);
									List<SampleResults> sampleResults = (List<SampleResults>) runResultsList2
											.getSampleResults();
									runresultsDTO.setTotalSamplecount(String.valueOf(sampleResults.size()));
									List<RunResultsDetailDTO> runresultdetaildto = mapEntityToDTO
											.convertRunResultsDetailToRunResultsDetailDTODTO(
													(List<RunResultsDetail>) runResultsList2.getRunResultsDetail());
									runresultsDTO.setRunResultsDetail(runresultdetaildto);
									List<RunReagentsAndConsumablesDTO> runReagentsAndConsumablesDTO = mapEntityToDTO
											.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO((List<RunReagentsAndConsumables>) runResultsList2.getRunReagentsAndConsumables());
												
									runresultsDTO.setRunReagentsAndConsumables(runReagentsAndConsumablesDTO);
									List<SampleResultsDTO> sampleresultslist = new ArrayList<>();
									for (SampleResults sampleResult : sampleResults) {
										SampleResultsDTO sampleResultsDTO = mapEntityToDTO
												.getSampleDTOFromSampleResults(sampleResult);
										sampleResultsDTO.setSampleReagentsAndConsumables(null);
										sampleResultsDTO.setSampleResultsDetail(null);
										sampleResultsDTO.setSampleProtocol(null);
										sampleresultslist.add(sampleResultsDTO);

									}
									runresultsDTO.setSampleResults(sampleresultslist);
									Map<String, Integer> inputContainerIdshm = new HashMap<>();
									Map<String, Integer> outputcontainerIdhm = new HashMap<>();
									List<InputStripDetailsDTO> inputcontainerIds = new ArrayList<>();
									List<OutputStripDetailsDTO> outputcontainerIds = new ArrayList<>();
									// for getting strip details and count
									for (SampleResultsDTO sampleResultDTO : sampleresultslist) {
										String inputcontainerId = sampleResultDTO.getInputContainerId();
										String outputcontainerId = sampleResultDTO.getOutputContainerId();
										Integer j = inputContainerIdshm.get(inputcontainerId);
										inputContainerIdshm.put(inputcontainerId, (j == null) ? 1 : j + 1);
										Integer k = outputcontainerIdhm.get(outputcontainerId);
										outputcontainerIdhm.put(outputcontainerId, (k == null) ? 1 : k + 1);
									}

									for (Map.Entry<String, Integer> entry : inputContainerIdshm.entrySet()) {
										InputStripDetailsDTO inputStripDetails = new InputStripDetailsDTO();
										String key = entry.getKey();
										String samplecounts = entry.getValue().toString();
										inputStripDetails.setStripId(key);
										inputStripDetails.setSamplecounts(samplecounts);
										inputcontainerIds.add(inputStripDetails);
									}

									for (Map.Entry<String, Integer> entry : outputcontainerIdhm.entrySet()) {
										OutputStripDetailsDTO outputStripDetails = new OutputStripDetailsDTO();
										String key = entry.getKey();
										String samplecounts = entry.getValue().toString();
										outputStripDetails.setStripId(key);
										outputStripDetails.setSamplecounts(samplecounts);
										outputcontainerIds.add(outputStripDetails);
									}
									runresultsDTO.setInputcontainerIds(inputcontainerIds);
									runresultsDTO.setOutputcontainerIds(outputcontainerIds);
									runresultsDTO.setWfmsflag(RMMConstant.PENDING);
									if (runResultsDTOList.contains(runresultsDTO)) {
										for (RunResultsDTO runresultsDTO1 : runResultsDTOList) {
											if (runresultsDTO1.getRunResultId() == runresultsDTO.getRunResultId()) {
												runresultsDTO1.setWfmsflag(RMMConstant.PARTIALLYMOVED);
												break;
											}

										}

									}
									runResultsDTOList.add(runresultsDTO);
								}
							}

						}
					}
					// For completed tab scenario

					if (!pendingInNextRunList.isEmpty()) {
						for (RunResults runResultsList3 : pendingInNextRunList) {
							RunResultsDTO runresultsDTO = mapEntityToDTO
									.getRunResultsDTOFromRunResults(runResultsList3);
							List<SampleResults> sampleResults = (List<SampleResults>) runResultsList3
									.getSampleResults();
							runresultsDTO.setTotalSamplecount(String.valueOf(sampleResults.size()));
							List<RunResultsDetailDTO> runresultdetaildto = mapEntityToDTO
									.convertRunResultsDetailToRunResultsDetailDTODTO(
											(List<RunResultsDetail>) runResultsList3.getRunResultsDetail());
							runresultsDTO.setRunResultsDetail(runresultdetaildto);
							List<SampleResultsDTO> sampleresultslist = new ArrayList<>();
							for (SampleResults sampleResult : sampleResults) {
								SampleResultsDTO sampleResultsDTO = mapEntityToDTO
										.getSampleDTOFromSampleResults(sampleResult);
								sampleResultsDTO.setSampleReagentsAndConsumables(null);
								sampleResultsDTO.setSampleResultsDetail(null);
								sampleResultsDTO.setSampleProtocol(null);
								sampleresultslist.add(sampleResultsDTO);

							}
							runresultsDTO.setSampleResults(sampleresultslist);
							Map<String, Integer> inputContainerIdshm = new HashMap<>();
							Map<String, Integer> outputcontainerIdhm = new HashMap<>();
							List<InputStripDetailsDTO> inputcontainerIds = new ArrayList<>();
							List<OutputStripDetailsDTO> outputcontainerIds = new ArrayList<>();
							// for getting strip details and count
							for (SampleResultsDTO sampleResultDTO : sampleresultslist) {
								String inputcontainerId = sampleResultDTO.getInputContainerId();
								String outputcontainerId = sampleResultDTO.getOutputContainerId();
								Integer j = inputContainerIdshm.get(inputcontainerId);
								inputContainerIdshm.put(inputcontainerId, (j == null) ? 1 : j + 1);
								Integer k = outputcontainerIdhm.get(outputcontainerId);
								outputcontainerIdhm.put(outputcontainerId, (k == null) ? 1 : k + 1);
							}

							for (Map.Entry<String, Integer> entry : inputContainerIdshm.entrySet()) {
								InputStripDetailsDTO inputStripDetails = new InputStripDetailsDTO();
								String key = entry.getKey();
								String samplecounts = entry.getValue().toString();
								inputStripDetails.setStripId(key);
								inputStripDetails.setSamplecounts(samplecounts);
								inputcontainerIds.add(inputStripDetails);
							}

							for (Map.Entry<String, Integer> entry : outputcontainerIdhm.entrySet()) {
								OutputStripDetailsDTO outputStripDetails = new OutputStripDetailsDTO();
								String key = entry.getKey();
								String samplecounts = entry.getValue().toString();
								outputStripDetails.setStripId(key);
								outputStripDetails.setSamplecounts(samplecounts);
								outputcontainerIds.add(outputStripDetails);
							}
							runresultsDTO.setInputcontainerIds(inputcontainerIds);
							runresultsDTO.setOutputcontainerIds(outputcontainerIds);
							runresultsDTO.setWfmsflag(RMMConstant.ARCHIVED);
							runResultsDTOList.add(runresultsDTO);
						}
					}

					List<String> deviceIds = runResultsDTOList.stream().filter(Objects::nonNull)
							.map(RunResultsDTO::getDeviceId).collect(Collectors.toList());

					List<DeviceDTO> deviceList = new ArrayList<>();
					try {
						deviceList = deviceIntegrationService.getDevice(deviceIds);
					} catch (Exception e) {
						logger.error("Failed to get Device information,", e);
					}

					Map<String, String> deviceMap = deviceList.stream().filter(Objects::nonNull)
							.collect(Collectors.toMap(DeviceDTO::getSerialNo, x -> x.getDeviceType().getName()));

					runResultsDTOList.parallelStream().forEach(r -> r.setDeviceType(deviceMap.get(r.getDeviceId())));

					// For sorting default support based on updated time stamp
					runResultsDTOList.sort(Comparator.comparing(RunResultsDTO::getUpdatedDateTime).reversed());
					// end
					logger.info("listInCompletedWFSActiveRuns() -> End of listInCompletedWFSActiveRuns Execution");
					return Response.ok(runResultsDTOList, MediaType.APPLICATION_JSON).status(Status.OK).build();

				} else {

					throw new HMTPException(CustomErrorCodes.ASSAYTYPE_NOT_FOUND,
							RMMConstant.ErrorMessages.ASSAYTYPE_NOT_FOUND.toString());

				}

			} else {
				throw new HMTPException(CustomErrorCodes.ASSAYTYPE_RUNSTATUS_PROCESS_STEP_NAME_NULL,
						RMMConstant.ErrorMessages.ASSAYTYPE_RUNSTATUS_PROCESS_STEP_NAME_NULL.toString());
			}

		} catch (HMTPException exp) {
			logger.error("Exception in getting the listInCompletedWFSActiveRuns details:", exp.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setErrorStatusCode(exp.getErrorCode().getErrorCode());
			errResponse.setErrorMessage(exp.getMessage());
			return Response.status(exp.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exception) {
			logger.error(
					"Exception in getting the listInCompletedWFSActiveRuns details:" + this.getClass().getMethods(),
					exception.getMessage());
			throw new HMTPException(exception.getMessage());
		}
	}

	public AssayTypeDTO getAssayTypeList(String assayType) throws HMTPException, UnsupportedEncodingException {
		final String url = RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay?assayType=",
				assayType, null);
		Invocation.Builder ammClient = RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null);
		List<AssayTypeDTO> listAssay = ammClient.get(new GenericType<List<AssayTypeDTO>>() {
		});
		return (!listAssay.isEmpty()) ? listAssay.get(0) : null;
	}

	public List<ProcessStepActionDTO> getProcessStepActionList(String assayType)
			throws HMTPException, UnsupportedEncodingException {
		final String url = RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/",
				assayType + "/" + "processstepaction", null);

		Invocation.Builder ammClient = RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null);
		List<ProcessStepActionDTO> processStepActionDTOList = ammClient
				.get(new GenericType<List<ProcessStepActionDTO>>() {
				});

		return processStepActionDTOList;
	}

	public List<String> getAccessingIdList(RunResults runResults) {
		List<String> accessingIdList = new ArrayList<>();
		List<SampleResults> sampleResultsList = (List<SampleResults>) runResults.getSampleResults();

		for (SampleResults sampleResultDTO : sampleResultsList) {
			String accessioningId = sampleResultDTO.getAccesssioningId();
			accessingIdList.add(accessioningId);
		}
		return accessingIdList;
	}

	public Response getInWorkFlowOrders() {
		logger.info("getInWorkFlowOrders() -> Start of getInWorkFlowOrders Execution");
		List<Object> inWorkFlowOrders = sampleResultsReadRepository.getInWorkFlowOrders();
		List<WorkflowDTO> inWorkFlowOrdersResp = new LinkedList<>();
		try {
			for (Object o : inWorkFlowOrders) {
				Object[] obj = (Object[]) o;
				WorkflowDTO wfo = new WorkflowDTO();
				wfo.setAccessioningId((String) obj[0]);
				wfo.setComments((String) obj[1]);
				wfo.setOrderId(Long.parseLong(obj[2].toString()));
				wfo.setWorkflowType((String) obj[3]);
				wfo.setAssaytype((String) obj[4]);
				wfo.setSampletype((String) obj[5]);
				wfo.setWorkflowStatus((String) obj[6]);
				wfo.setFlags((String) obj[7]);
				wfo.setCreateDate((Date) obj[8]);
				inWorkFlowOrdersResp.add(wfo);
			}
			logger.info("getInWorkFlowOrders() -> End of getInWorkFlowOrders Execution");
		} catch (Exception exp) {
			logger.info("Error while fetching inworflow orders : " + exp.getMessage());
		}
		return Response.status(200).entity(inWorkFlowOrdersResp).build();
	}

	@Override
	public Response getInWorkflowOrderCount() throws HMTPException {
		logger.info("getInWorkflowOrderCount() --> Start of getUnassignedOrderCount() execution");
		Long count = null;
		try {
			count = sampleResultsReadRepository.getInWorkFlowOrdersCount();
			logger.info("getInWorkflowOrderCount() --> End of getUnassignedOrderCount() execution");
			return Response.ok(count).build();
		} catch (Exception e) {
			logger.error("Error while getting the count of unassigned orders : " + e.getMessage());
			throw new HMTPException(e);
		}
	}


	@Override
	public Response getJasperPDFReportByLocale(String language, String country, String accessioningId)
			throws JRException, IOException, HMTPException {
		logger.info("getJasperPDFReportByLocale() -> Start of getJasperReport() execution ");
		try {
			List<OrderDTO> orderDTOs = orderIntegrationService.searchByAccessioningId(accessioningId);
			if(orderDTOs.isEmpty()) {
					throw new HMTPException(CustomErrorCodes.INVALID_ACCESSIONING_ID,RMMConstant.ErrorMessages.INVALID_ACCESSIONING_ID_JASPER_REPORTS.toString());
			}
			
			JasperPrint jasperPrint = null;
			JasperUtils jasperUtils = new JasperUtils();
			JasperReport jasperReport = jasperUtils.loadCompiledJasper("HarmonyTestHighAnProbability-2.jasper");
			logger.info("Jasper Report compiled");
			Collection<OrderDetailsDTO> data = invokeOrderService(accessioningId);
			
			HashMap<String, Object> parameters = new HashMap<>();
			String harmonyRocheLogo = null;
			String highProbabilityRisk = null;
			
			jasperUtils.setReportParameters(parameters, language, country, "amm");
			
			harmonyRocheLogo = RestClientUtil.getUrlString("pas.harmony_roche_logo", "", "", "", null);
			highProbabilityRisk = RestClientUtil.getUrlString("pas.high_probability_risk", "", "", "", null);
			parameters.put("rocheLogo", harmonyRocheLogo);
			parameters.put("highProbabilityRiskLogo", highProbabilityRisk);
			logger.info("Start of filling the report data with API response");
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
			logger.info("Datasource populated successfully!!");
			jasperPrint = jasperUtils.fillReport(jasperReport, parameters, dataSource);
			logger.info("Reports filled successfully!!");
			
			SimplePdfExporterConfiguration configuration = "true".equalsIgnoreCase(passwordRequired)?jasperUtils.setReportPassword(password):new SimplePdfExporterConfiguration();
			byte[] bytes = jasperUtils.getReportPDF(configuration, jasperPrint);
			logger.info("JasperReport Generated Successfully: ");
			return Response.ok(bytes).type("application/pdf")
					.header("Content-Disposition", "inline; filename=Harmony test-high and low probability.pdf;")
					.build();
		} catch (HMTPException e) {
			logger.error("Error while fetching the patient risk report" + e.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
            errResponse.setErrorStatusCode(e.getErrorCode().getErrorCode());
            errResponse.setErrorMessage(e.getMessage());
            return Response.ok().type("application/json").status(e.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exp) {
			logger.error("Error while fetching the patient risk report" + exp.getMessage());
			throw new HMTPException(exp);
		}
	}


	public List<OrderDetailsDTO> invokeOrderService(String accessioningId) {
		String url = "";
		List<OrderDetailsDTO> orderList = null;
		String dob=null;
		
		try {
			logger.info("invokeOrderService() -> Start of invokeService() execution");
			if (accessioningId != null) {
				url = RestClientUtil.getUrlString("pas.omm_api_url", "", "/json/rest/api/v1/orders?accessioningID=",
						accessioningId, null);
			} else {
				url = RestClientUtil.getUrlString("pas.omm_api_url", "", "/json/rest/api/v1/ord", "", null);
			}

			Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null);
			orderList = orderClient.get(new GenericType<List<OrderDetailsDTO>>() {
			});

			for (OrderDetailsDTO order : orderList) {
				if (order.getPatient().getPatientFirstName() != null
						&& order.getPatient().getPatientLastName() != null) {
					order.setPatientFirstName(
							order.getPatient().getPatientFirstName() + " " + order.getPatient().getPatientLastName());
					order.setPatientLastName(order.getPatient().getPatientLastName());

				} else if (order.getPatient().getPatientLastName() == null) {
					order.setPatientFirstName(order.getPatient().getPatientFirstName());
					order.setPatientLastName("");
				} else {
					order.setPatientFirstName("");
					order.setPatientLastName("");
				}

				if (StringUtils.isBlank(order.getPatient().getPatientDOB())|| order.getPatient().getPatientDOB().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")||order.getPatient().getPatientDOB().matches("([0-9]{1})/([0-9]{1})/([0-9]{4})")) {
					order.setPatientDOB(order.getPatient().getPatientDOB());
				} else if (null != order.getPatient().getPatientDOB()) {
					Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
							.parse(order.getPatient().getPatientDOB());
					dob = new SimpleDateFormat("MM/dd/yyyy").format(date);
					order.setPatientDOB(dob);
				}
				
				
				order.setGestationalAgeDays(order.getAssay().getGestationalAgeDays());
				order.setGestationalAgeWeeks(order.getAssay().getGestationalAgeWeeks());
				order.setFetusNumber(order.getAssay().getFetusNumber());
				order.setIvfStatus(order.getAssay().getIvfStatus());
				order.setRefClinicianClinicName(order.getPatient().getClinicName());
				order.setOtherClinicianName(order.getPatient().getOtherClinicianName());
				order.setRefClinicianName(order.getPatient().getRefClinicianName());
				order.setCollectionDate(order.getAssay().getCollectionDate());
				order.setReceivedDate(order.getAssay().getReceivedDate());
			}

		} catch (Exception e) {
			logger.error("Error while calling the API: " + e.getMessage());
		}
		logger.info(" End of invokeOrderService() execution");
		return orderList;
	}

	public List<AssayTypeDTO> invokeService() {
		String url = "";
		List<AssayTypeDTO> listAssays = null;

		try {
			logger.info("invokeService() -> Start of invokeService() execution");
			url = RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay", "", null);
			Invocation.Builder assayClient = RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null);
			listAssays = assayClient.get(new GenericType<List<AssayTypeDTO>>() {
			});

		} catch (Exception e) {
			logger.error("Error while calling the API: " + e.getMessage());
		}
		logger.info(" End of invokeService() execution");
		return listAssays;
	}

	@Override
	public Response updateUserComments(ProcessStepValuesDTO processStepValuesDTO) throws HMTPException {
		logger.info("updateUserComments() -> Start of updateUserComments Execution");

		List<SampleResults> sampleresult = new ArrayList<>();
		RunResults runResults = null;

		if (processStepValuesDTO.getRunResultId() != null) {
			try {

				runResults = runResultsReadRepository.findOne(processStepValuesDTO.getRunResultId());

				if (runResults != null) {
					if (processStepValuesDTO.getComments() == null) {
						runResults.setComments(processStepValuesDTO.getComments());
					} else {
						runResults.setComments(processStepValuesDTO.getComments().trim());
					}
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					runResults.setUpdatedDateTime(timestamp);
					runResults.setUpdatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());
					runResults.setRunReagentsAndConsumables(null);
					runResults.setSampleResults(null);
					runResults.setRunResultsDetail(null);
					runResultsWriteRepository.save(runResults);
				}

				return Response.ok("Sample comments updated successfully").status(200).build();
			} catch (Exception exp) {
				logger.error("Error while updating sample results : " + exp.getMessage());
				throw new HMTPException(exp.getMessage());
			}
		} else {
			try {

				sampleresult = sampleResultsReadRepository.findAll(
						RunResultsSpecifications.getSampleResults(null, processStepValuesDTO.getProcessStepName(), null,
								null, null, null, processStepValuesDTO.getAccesssioningId()));

				for (SampleResults sampleResults : sampleresult) {
					if (processStepValuesDTO.getComments() == null) {
						sampleResults.setComments(processStepValuesDTO.getComments());
					} else {
						sampleResults.setComments(processStepValuesDTO.getComments().trim());
					}
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					sampleResults.setUpdatedDateTime(timestamp);
					sampleResults.setUpdatedBy(ThreadSessionManager.currentUserSession().getAccessorUserName());

					sampleResultsWriteRepository.save(sampleResults);
				}

				logger.info("updateUserComments() -> End of updateUserComments Execution");
				return Response.ok("Sample comments updated successfully").status(200).build();
			} catch (Exception exp) {
				logger.error("Error while updating sample results : " + exp.getMessage());
				throw new HMTPException(exp.getMessage());
			}
		}
	}

	public NAExtractionDTO getNAExtractionDTO(Optional<RunResultsDTO> runResultDto) throws HMTPException {

		NAExtractionDTO nAExtractionDTO = null;
		Optional<SampleResultsDTO> sampleResultsDTO=null;
		Optional<SampleProtocolDTO> sampleProtocolDTO=null;
		if (runResultDto.isPresent()) {
			nAExtractionDTO = new NAExtractionDTO();
			nAExtractionDTO.setProcessStepName(runResultDto.get().getProcessStepName());
			nAExtractionDTO.setDeviceId(runResultDto.get().getDeviceId());
			nAExtractionDTO.setRunStatus(runResultDto.get().getRunStatus());
			nAExtractionDTO.setAssayType(runResultDto.get().getAssayType());
			nAExtractionDTO.setComments(runResultDto.get().getComments());
			nAExtractionDTO.setOperatorName(runResultDto.get().getOperatorName());
			nAExtractionDTO.setDeviceRunId(runResultDto.get().getDeviceRunId());
			nAExtractionDTO.setComments(runResultDto.get().getComments());
			nAExtractionDTO.setStartTime(runResultDto.get().getRunStartTime());
			nAExtractionDTO.setCompletionTime(runResultDto.get().getRunCompletionTime());
			nAExtractionDTO.setOperatorId(runResultDto.get().getOperatorName());
			sampleResultsDTO=runResultDto.get().getSampleResults().stream().findFirst();
			if(sampleResultsDTO.isPresent()){
				sampleProtocolDTO=	sampleResultsDTO.get().getSampleProtocol().stream().findFirst();
				if(sampleProtocolDTO.isPresent()){
					nAExtractionDTO.setProtocolName(sampleProtocolDTO.get().getProtocolName());
				}
			}
			
			
			List<ProcessStepActionDTO> processSteps = this.getProcessstepForAssayAndDevice(runResultDto.get().getAssayType(), "MP96");
			if (!processSteps.isEmpty()) {
				nAExtractionDTO.setEluateVolume(processSteps.get(0).getEluateVolume());
				nAExtractionDTO.setSampleVolume(processSteps.get(0).getSampleVolume());
				nAExtractionDTO.setOutputFormat(processSteps.get(0).getOutputContainerType());
			}
			List<SampleDTO> sample = getlistOfSampleDto(runResultDto);
			PlateBasisSamplesDTO plateBasisSamplesDTO = new PlateBasisSamplesDTO();
			plateBasisSamplesDTO.setPlateId(sample.get(0).getPlateId());
			plateBasisSamplesDTO.setSampleDetails(sample.get(0).getListOfSampleDataDTO());
			List<PlateBasisSamplesDTO> plateSampleDTO = new ArrayList<>();
			plateSampleDTO.add(plateBasisSamplesDTO);
			nAExtractionDTO.setMp96samplesData(plateSampleDTO);
			nAExtractionDTO.setMpsampleDetails(sample.get(0).getListOfSampleDataDTO());
			sampleResultsDTO=	runResultDto.get().getSampleResults().stream().findFirst();
			if(sampleResultsDTO.isPresent()){
				nAExtractionDTO.setPlateId(sampleResultsDTO.get().getOutputContainerId());
			}	
		
			nAExtractionDTO
					.setMp96sampleComments(getListOfSampleWithCommentsDTO(runResultDto.get().getSampleResults()));
			nAExtractionDTO.setMp96Flags(getListOfFlagesDTO(runResultDto.get().getSampleResults(),runResultDto.get().getAssayType(),"MP96"));
			nAExtractionDTO
					.setMp96Consumables(getListOfConsumablesDTO(runResultDto.get().getUniqueReagentsAndConsumables()));
			nAExtractionDTO.setMp96reagents(getListOfReagentDTO(runResultDto.get().getUniqueReagentsAndConsumables()));
		} else {
			logger.info("Run results information not present for NA Extraction ");
		}
		return nAExtractionDTO;

	}

	public LibraryPrepDTO getLibraryPrepDTO(Optional<RunResultsDTO> runResultDto) throws HMTPException {
		LibraryPrepDTO libraryPrepDTO = null;
		Optional<SampleResultsDTO> sampleResultsDTO=null;
		Optional<SampleProtocolDTO> sampleProtocolDTO=null;
		if (runResultDto.isPresent()) {
			libraryPrepDTO = new LibraryPrepDTO();
			libraryPrepDTO.setProcessStepName(runResultDto.get().getProcessStepName());
			libraryPrepDTO.setDeviceId(runResultDto.get().getDeviceId());
			libraryPrepDTO.setRunStatus(runResultDto.get().getRunStatus());
			libraryPrepDTO.setAssayType(runResultDto.get().getAssayType());
			libraryPrepDTO.setComments(runResultDto.get().getComments());
			libraryPrepDTO.setOperatorName(runResultDto.get().getOperatorName());
			libraryPrepDTO.setDeviceRunId(runResultDto.get().getDeviceRunId());
			libraryPrepDTO.setComments(runResultDto.get().getComments());
			
			libraryPrepDTO.setStartTime(runResultDto.get().getRunStartTime());
			libraryPrepDTO.setCompletionTime(runResultDto.get().getRunCompletionTime());
			libraryPrepDTO.setOperatorId(runResultDto.get().getOperatorName());
			sampleResultsDTO=runResultDto.get().getSampleResults().stream().findFirst();
			if(sampleResultsDTO.isPresent()){
				sampleProtocolDTO=sampleResultsDTO.get().getSampleProtocol().stream().findFirst();
				if(sampleProtocolDTO.isPresent()){
					libraryPrepDTO.setProtocolName(sampleProtocolDTO.get().getProtocolName());
				}
			}
			
			
			List<ProcessStepActionDTO> processSteps = this.getProcessstepForAssayAndDevice(runResultDto.get().getAssayType(), "LP24");
			
			if (!processSteps.isEmpty()) {
				libraryPrepDTO.setEluateVolume(processSteps.get(0).getEluateVolume());
				libraryPrepDTO.setSampleVolume(processSteps.get(0).getSampleVolume());
				libraryPrepDTO.setOutputFormat(processSteps.get(0).getOutputContainerType());
			}

			List<SampleDTO> samples = getlistOfSampleDto(runResultDto);
			List<LpplateBasedSamples> lpPLatebasedsamples = new ArrayList<>();
			lpPLatebasedsamples.add(JasperUtil.getLpPlateBasedSamples(samples));
			libraryPrepDTO.setLpsamplesData(lpPLatebasedsamples);
			libraryPrepDTO.setLpSampleComments(getListOfSampleWithCommentsDTO(runResultDto.get().getSampleResults()));
			libraryPrepDTO.setLpFlags(getListOfFlagesDTO(runResultDto.get().getSampleResults(),runResultDto.get().getAssayType(),"LP24"));
			libraryPrepDTO.setLpReagentsAndConsumables(
					getlistOfReagents(runResultDto.get().getUniqueReagentsAndConsumables()));
		} else {
			logger.info("Run results data not present for Library Preparation ");
		}
		return libraryPrepDTO;

	}
	
	public DPCRDTO getDPCRResultsData(Optional<RunResultsDTO> runResultDto) throws HMTPException {
		DPCRDTO dPCRDTO = null;
		if(runResultDto.isPresent()) {
			dPCRDTO = new DPCRDTO();
			dPCRDTO.setProcessStepName(runResultDto.get().getProcessStepName());
			dPCRDTO.setAssayType(runResultDto.get().getAssayType());
			dPCRDTO.setComments(runResultDto.get().getComments());
			dPCRDTO.setDeviceRunId(runResultDto.get().getDeviceRunId());
			dPCRDTO.setComments(runResultDto.get().getComments());
			
			dPCRDTO.setStartTime(runResultDto.get().getRunStartTime());
			dPCRDTO.setCompletionTime(runResultDto.get().getRunCompletionTime());
			dPCRDTO.setOperatorId(runResultDto.get().getOperatorName());
			
			List<RunResultsDetailDTO> runResultDetailDTO1 = runResultDto.get().getRunResultsDetail().stream().filter(p -> "DPCR Analyzer FilePath".equalsIgnoreCase(p.getAttributeName()) ).collect(Collectors.toList());
			if (!runResultDetailDTO1.isEmpty()) {
				dPCRDTO.setOutputFilePath(runResultDetailDTO1.get(0).getAttributeValue());
			}
			
			if (!runResultDto.get().getSampleResults().isEmpty()) {
				dPCRDTO.setProtocolName(runResultDto.get().getSampleResults().stream().findFirst().get()
						.getSampleProtocol().stream().findFirst().get().getProtocolName());
			}
			
			List<ProcessStepActionDTO> processSteps = this.getProcessstepForAssayAndDevice(runResultDto.get().getAssayType(), "dPCR Analyzer");
			
			if (!processSteps.isEmpty()) {
				dPCRDTO.setEluateVolume(processSteps.get(0).getEluateVolume());
				dPCRDTO.setSampleVolume(processSteps.get(0).getSampleVolume());
				dPCRDTO.setOutputFormat(processSteps.get(0).getOutputContainerType());
			}

			List<SampleDTO> sample = getlistOfSampleDto(runResultDto);
			PlateBasisSamplesDTO plateBasisSample = new PlateBasisSamplesDTO();
			plateBasisSample.setSampleDetails(sample.get(0).getListOfSampleDataDTO());
			List<PlateBasisSamplesDTO> plateSampleDTO = new ArrayList<>();
			plateSampleDTO.add(plateBasisSample);
			dPCRDTO.setDpcrsamplesData(plateSampleDTO);
			
			try {
				dPCRDTO.setDpcrFlags(this.getListOfFlagesDTO(runResultDto.get().getSampleResults(),runResultDto.get().getAssayType(),"dPCR Analyzer"));
			} catch (HMTPException e) {
				e.getMessage();
			}
			/*** setting reagents and consumables **/
			
			List<DPCRReagentnConsumableDTO> listOfreagents = new CopyOnWriteArrayList<>();
			if (!runResultDto.get().getUniqueReagentsAndConsumables().isEmpty()) {
				runResultDto.get().getUniqueReagentsAndConsumables().stream()
						.filter(p -> p.getAttributeName().equalsIgnoreCase("plateID"))
						.collect(
								Collectors.mapping(UniqueReagentsAndConsumables::getAttributeValue, Collectors.toSet()))
						.forEach(p -> {
							String[] dpcrreagent = p.split(";");
							if (dpcrreagent.length >= 4) {
								DPCRReagentnConsumableDTO consumablesDTO = new DPCRReagentnConsumableDTO();
								consumablesDTO.setPartioningFluidId(dpcrreagent[2]);
								consumablesDTO.setPatitioningSerialNo(dpcrreagent[1]);
								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
								Date parsedDate;
								try {
									parsedDate = dateFormat.parse(dpcrreagent[3]);
									consumablesDTO.setPartioningTime(new Timestamp(parsedDate.getTime()));
								} catch (Exception e) {
									logger.error("Date parsing error"+e.getMessage());
								}
								listOfreagents.add(consumablesDTO);
							}
						});
			}
			
			/** end of setting reagents and consumables **/
			if (listOfreagents.isEmpty()) {
				DPCRReagentnConsumableDTO consumablesDTO = new DPCRReagentnConsumableDTO();
				consumablesDTO.setPartioningFluidId("");
				listOfreagents.add(consumablesDTO);
			}
			dPCRDTO.setDpcrReagents(listOfreagents);
			
			dPCRDTO.setDpcrSampleComments(getListOfSampleWithCommentsDTO(runResultDto.get().getSampleResults()));
		} else {
			logger.info("Run results data not present for dPCR analyzer ");
		}
		return dPCRDTO;
	}

	public List<SampleCommentsDTO> getListOfSampleWithCommentsDTO(Collection<SampleResultsDTO> listOfSampleDTO) {
		List<SampleCommentsDTO> listOfSampleComment = new CopyOnWriteArrayList<>();
		if (listOfSampleDTO == null || listOfSampleDTO.isEmpty()) {
			logger.info("SampleResultsDTO is empty");
		} else {
			listOfSampleDTO.forEach(p -> {
				if (StringUtils.isNotEmpty(p.getComments())) {
					SampleCommentsDTO commentsDTO = new SampleCommentsDTO();
					commentsDTO.setComment(p.getComments());
					commentsDTO.setSampleId(p.getAccesssioningId());
					listOfSampleComment.add(commentsDTO);
				}

			});
		}
		if (listOfSampleComment.isEmpty()) {
			SampleCommentsDTO commentsDTO = new SampleCommentsDTO();
			commentsDTO.setComment("");
			listOfSampleComment.add(commentsDTO);
		}
		return listOfSampleComment;
	}

	public List<FlagsDTO> getListOfFlagesDTO(Collection<SampleResultsDTO> listOfSampleDTO,String assayType, String deviceType) throws HMTPException {
		List<FlagsDTO> listOfFlagesDTO = new CopyOnWriteArrayList<>();
		Set<String> flages = new HashSet<>();
		if (listOfSampleDTO == null || listOfSampleDTO.isEmpty()) {
			logger.info("listOfSampleDTO is empty");
		} else {
			listOfSampleDTO.forEach(p -> {
				if (StringUtils.isNotEmpty(p.getFlag()) && p.getFlag().contains(",")) {
					String[] listOfFalges = p.getFlag().split(",");
					for (String flag : listOfFalges) {
						flages.add(flag);
					}
				} else if (StringUtils.isNotEmpty(p.getFlag())) {
					flages.add(p.getFlag());
				}
			});
			if (flages.isEmpty()) {
				logger.info("set is Empty");
			} else {
			List<FlagsDataDTO> assayFlags = this.getFlagsForAssayAndDevice(assayType, deviceType);
			
			List<FlagsDataDTO>  filteredFlags = assayFlags.stream().filter(p -> flages.contains(p.getFlagCode())).collect(Collectors.toList());
			
			filteredFlags.stream().forEach(p -> {
				FlagsDTO flagsDTO = new FlagsDTO();
				flagsDTO.setFlagId(p.getFlagCode());
				flagsDTO.setSeverity(p.getSeverity());
				flagsDTO.setDescription(p.getDescription());
				listOfFlagesDTO.add(flagsDTO);
			});
			
			}
		}
		if (listOfFlagesDTO.isEmpty()) {
			FlagsDTO flagsDTO = new FlagsDTO();
			flagsDTO.setDescription("");
			listOfFlagesDTO.add(flagsDTO);
		}
		return listOfFlagesDTO;
	}

	public List<SampleDTO> getlistOfSampleDto(Optional<RunResultsDTO> runResultDto) {
		List<SampleDTO> listOfSampleDTO = new CopyOnWriteArrayList<>();
		Optional<SampleResultsDTO> sampleResultsDTO=null;
		if (runResultDto.isPresent()) {
			if (runResultDto.get().getProcessStepName() != null
					&& "NA Extraction".equalsIgnoreCase(runResultDto.get().getProcessStepName())) {
				SampleDTO sampleDTO = new SampleDTO();
				sampleResultsDTO=	runResultDto.get().getSampleResults().stream().findFirst();
				if(sampleResultsDTO.isPresent()){
					sampleDTO.setPlateId(sampleResultsDTO.get().getOutputContainerId());
				}
				
				sampleDTO.setListOfSampleDataDTO(getListOfSamplesDataDTO(runResultDto.get().getSampleResults()));
				listOfSampleDTO.add(sampleDTO);
			} else if (runResultDto.get().getProcessStepName() != null
					&& "Library Preparation".equalsIgnoreCase(runResultDto.get().getProcessStepName())) {
				Map<String, List<SampleResultsDTO>> map = getlistofSampleDTO(runResultDto.get().getSampleResults());

				map.keySet().forEach(p -> {
					SampleDTO sampleDTO = new SampleDTO();
					sampleDTO.setPlateId(p);
					sampleDTO.setListOfSampleDataDTO(getListOfSamplesDataDTO(map.get(p)));
					listOfSampleDTO.add(sampleDTO);

				});
			} else if(runResultDto.get().getProcessStepName() != null
					&& "dPCR".equalsIgnoreCase(runResultDto.get().getProcessStepName())) {
				SampleDTO sampleDTO = new SampleDTO();
				
				List<SamplesDataDTO> listOfSamplesDataDTO = new CopyOnWriteArrayList<>();
				if (runResultDto.get().getSampleResults() == null || runResultDto.get().getSampleResults().isEmpty()) {
					SamplesDataDTO dto = new SamplesDataDTO();
					dto.setAssayType("");
					listOfSamplesDataDTO.add(dto);
				} else {
					runResultDto.get().getSampleResults().stream().forEach(p -> {
						SamplesDataDTO dto = new SamplesDataDTO();
						dto.setAssayType(p.getAssayType());
						dto.setFlags(p.getFlag());
						
						List<SampleResultsDetailDTO> sampleResultsdtl1 = p.getSampleResultsDetail().stream().filter(p1 -> !p1.getAttributeName().isEmpty()
								&& ("quantitativeValue".equalsIgnoreCase(p1.getAttributeName()) || "qualitativeValue".equalsIgnoreCase(p1.getAttributeName()))).collect(Collectors.toList());
						
						 sampleResultsdtl1.stream().forEach(p2 -> {
							 if("qualitativeValue".equalsIgnoreCase(p2.getAttributeName())){
								dto.setQualitativeResult(p2.getAttributeValue());
							 } else if("quantitativeValue".equalsIgnoreCase(p2.getAttributeName())) {
								 dto.setQuantitativeResult(p2.getAttributeValue()); 
							 }
						 });
						dto.setStatus(p.getStatus());
						dto.setSampleId(p.getAccesssioningId());
						listOfSamplesDataDTO.add(dto);
					});
					
				}
				sampleDTO.setListOfSampleDataDTO(listOfSamplesDataDTO);
				listOfSampleDTO.add(sampleDTO);
			}
		}
		return listOfSampleDTO;
	}

	private Map<String, List<SampleResultsDTO>> getlistofSampleDTO(Collection<SampleResultsDTO> listOfSampleDTO) {
		Map<String, List<SampleResultsDTO>> map = new HashMap<>();
		logger.info("start of getlistofSampleDTO() execution");
		for (SampleResultsDTO sampleResultsDTO : listOfSampleDTO) {

			if (sampleResultsDTO != null) {
				if (map.containsKey(sampleResultsDTO.getOutputContainerId())) {
					List<SampleResultsDTO> listofDtos = map.get(sampleResultsDTO.getOutputContainerId());
					listofDtos.add(sampleResultsDTO);
					map.put(sampleResultsDTO.getOutputContainerId(), listofDtos);
				} else {
					List<SampleResultsDTO> listOfSampleResultDto = new CopyOnWriteArrayList<>();
					listOfSampleResultDto.add(sampleResultsDTO);
					map.put(sampleResultsDTO.getOutputContainerId(), listOfSampleResultDto);
				}
			} else {
				logger.info("sampleResultdto null");
			}
		}
		return map;
	}

	public List<ReagentsAndConsumablesDTO> getlistOfReagents(Collection<UniqueReagentsAndConsumables> listOfReagents) {
		final List<ReagentsAndConsumablesDTO> listOfreagents = new CopyOnWriteArrayList<>();
		if (listOfReagents.isEmpty()) {
			logger.info("UniqueReagentsAndConsumables is empty");
		} else {
			try {
				listOfReagents.stream().filter(p -> !(p.getAttributeName() == null || p.getAttributeName().isEmpty()))
						.forEach(p -> {

							String[] list = p.getAttributeValue().split(";");

							if (list.length >= 3) {
								ReagentsAndConsumablesDTO consumablesDTO = new ReagentsAndConsumablesDTO();
								consumablesDTO.setName(list[0]);
								consumablesDTO.setLotNumber(list[1]);
								listOfreagents.add(consumablesDTO);
							}

						});
			} catch (Exception e) {
				logger.info("Error while retrieving the reagents and consumables: " + e.getMessage());
			}
		}
		if (listOfreagents.isEmpty()) {
			ReagentsAndConsumablesDTO consumablesDTO = new ReagentsAndConsumablesDTO();
			consumablesDTO.setName("");
			listOfreagents.add(consumablesDTO);
		}
		return listOfreagents;

	}

	public List<MPReagentsDTO> getListOfReagentDTO(Collection<UniqueReagentsAndConsumables> listOfReagents) {
		final List<MPReagentsDTO> listOfReagentDTO = new CopyOnWriteArrayList<>();
		
		listOfReagents.stream().filter(p -> p.getAttributeName().equalsIgnoreCase("Reagents"))
				.collect(Collectors.mapping(UniqueReagentsAndConsumables::getAttributeValue, Collectors.toSet()))
				.forEach(p -> {
					String[] reagent = p.split(";");
					if(reagent.length>=2) {
						MPReagentsDTO dto = new MPReagentsDTO();
						dto.setName(reagent[0]);
						dto.setVersion(reagent[1]);
						listOfReagentDTO.add(dto);
					}
				});
		if(listOfReagentDTO.isEmpty()) {
			MPReagentsDTO dto = new MPReagentsDTO();
			dto.setName("");
			listOfReagentDTO.add(dto);
		}
		return listOfReagentDTO;
	}

	public List<MPConsumablesDTO> getListOfConsumablesDTO(Collection<UniqueReagentsAndConsumables> listOfReagents) {
		List<MPConsumablesDTO> consumablesDTOs1 = new CopyOnWriteArrayList<>();
		final String SIMPLE_DATE_FORMAT_STR = "yyyy-MM-dd hh:mm:ss.SSS";
		try {
			for (UniqueReagentsAndConsumables consumables : listOfReagents) {
				if("Internal Control".equalsIgnoreCase(consumables.getAttributeName())) {
					String[] consumable = consumables.getAttributeValue().split(";");
					if(consumable.length>=4) {
						MPConsumablesDTO consumablesDTO = new MPConsumablesDTO();
						consumablesDTO.setBarcode(consumable[1]);
						SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_FORMAT_STR);
						Timestamp timeStamp = new Timestamp(formatter.parse(consumable[3]).getTime());
						consumablesDTO.setExpirationDate(timeStamp);
						consumablesDTO.setProductionLot(consumable[0]);
						consumablesDTO.setVolume(consumable[2]);
						consumablesDTOs1.add(consumablesDTO);
					}
				}
			}
			
			if(consumablesDTOs1.isEmpty()) {
				MPConsumablesDTO consumablesDTO = new MPConsumablesDTO();
				consumablesDTO.setBarcode("");
				consumablesDTOs1.add(consumablesDTO);
			}
		} catch (Exception e) {
			logger.info("Error while setting consumables for MP96:" + e.getMessage());
		}

		return consumablesDTOs1;
	}

	public List<SamplesDataDTO> getListOfSamplesDataDTO(Collection<SampleResultsDTO> listOfSampleDTO) {
		logger.info("getListOfSamplesDataDTO() -> start of getListOfSamplesDataDTO() execution");
		List<SamplesDataDTO> listOfSamplesDataDTO = new CopyOnWriteArrayList<>();
		if (listOfSampleDTO == null || listOfSampleDTO.isEmpty()) {
			logger.info("SampleResultsDTO is empty");
		} else {
			for(SampleResultsDTO p :listOfSampleDTO)
			{
				SamplesDataDTO dto = new SamplesDataDTO();
				dto.setAssayType(p.getAssayType());
				dto.setFlags(p.getFlag());
				dto.setPosition(p.getOutputContainerPosition());
				dto.setStatus(p.getStatus());
				dto.setSampleId(p.getAccesssioningId());
				listOfSamplesDataDTO.add(dto);
			}
		}
		if (listOfSamplesDataDTO.isEmpty()) {
			SamplesDataDTO dto = new SamplesDataDTO();
			dto.setAssayType("");
			listOfSamplesDataDTO.add(dto);
		}
		logger.info("getListOfSamplesDataDTO() -> end of getListOfSamplesDataDTO() execution");
		return listOfSamplesDataDTO;

	}

	private Set<Long> getListOfRunResultIdBasedOnDeviceRunID(String devicerunid) throws HMTPException {
		logger.info(
				"getListOfRunResultIdBasedOnDeviceRunID() -> start of getListOfRunResultIdBasedOnDeviceRunID() execution");
		Set<Long> listofrunid = new HashSet<>();
		List<String> accessioningids = null;
		RunResultsDTO runresultdto = null;
		Response response = null;
		
		try {
			if (Optional.ofNullable(devicerunid).isPresent()) {
				response = getRunResultsByDeviceRunId(devicerunid);
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					runresultdto = response.readEntity(RunResultsDTO.class);
					
					
					accessioningids = runresultdto.getSampleResults().stream().filter(p->p.getAccesssioningId()!=null).collect(Collectors.mapping(SampleResultsDTO::getAccesssioningId, Collectors.toList()));
					
				}
				
				for(String accessioningID: accessioningids) {
					if (accessioningID != null) {
						Response response2 = getProcessStepResults(accessioningID);
						if (response2.getStatus() == Response.Status.OK.getStatusCode()) {
							Optional<List<ProcessStepValuesDTO>> list = Optional
									.ofNullable(response2.readEntity(new GenericType<List<ProcessStepValuesDTO>>() {
									}));

							if (list.isPresent()) {

								listofrunid.addAll(list.get().stream().collect(
										Collectors.mapping(ProcessStepValuesDTO::getRunResultId, Collectors.toList())));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("getListOfRunResultIdBasedOnDeviceRunID()" + e.getLocalizedMessage());
		}
		logger.info(
				"getListOfRunResultIdBasedOnDeviceRunID() -> end of getListOfRunResultIdBasedOnDeviceRunID() execution");
		return listofrunid;
	}
	
	public List<FlagsDataDTO> getFlagsForAssayAndDevice(String assayType, String deviceType) throws HMTPException {
		String url = null;
		Invocation.Builder ammClient = null;
		List<FlagsDataDTO> listFlags = null;

		url = RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/flagDescriptions",
				"/"+deviceType+"?assayType="+assayType, null);
		try {
			ammClient = RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null);
			listFlags = ammClient.get(new GenericType<List<FlagsDataDTO>>() {
			});
		} catch (UnsupportedEncodingException e) {
			logger.error("Error while fetching flags data: "+e.getMessage());
		}
		return listFlags;
	}
	
	public List<ProcessStepActionDTO> getProcessstepForAssayAndDevice(String assayType, String deviceType) throws HMTPException {
		String url = null;
		Invocation.Builder ammClient = null;
		List<ProcessStepActionDTO> processSteps = null;

		url = RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay",
				"/"+assayType+ "/processstepaction?deviceType=" +deviceType, null);
		try {
			ammClient = RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null);
			processSteps = ammClient.get(new GenericType<List<ProcessStepActionDTO>>() {
			});
		} catch (UnsupportedEncodingException e) {
			logger.error("Error while fetching flags data: "+e.getMessage());
		}
		return processSteps;
	}

	@Override
	public DeviceRunResultsDTO getRunresultsByDevice(String devicerunid) throws HMTPException {
		logger.info("getrundeatilbyrunid() -> start of getRunresultsByDevice() execution");
		Set<Long> listofrunid = null;
		DeviceRunResultsDTO deviceRunResultsDTO = new DeviceRunResultsDTO();
		deviceRunResultsDTO.setCompletionDate(new Timestamp(System.currentTimeMillis()));
		listofrunid = getListOfRunResultIdBasedOnDeviceRunID(devicerunid);
		if (listofrunid!=null&&listofrunid.isEmpty()) {
			logger.info("list is empty");
		} else {
			List<NAExtractionDTO> dtos = new ArrayList<>();
			List<LibraryPrepDTO> dtos1 = new ArrayList<>();
			List<DPCRDTO> dpcrdtos = new ArrayList<>();
			
			if(listofrunid!=null&&!listofrunid.isEmpty()){
			listofrunid.stream().forEach(p -> {
				Response res = null;
				String s=Long.toString(p);
				try {
					res = getAllDetailsByRunResultsId(s);
				} catch (Exception e) {
					logger.info("Error while fetching run result details: " + e.getMessage());
				}
				if (res.getStatus() == Response.Status.OK.getStatusCode()) {
					RunResultsDTO dto = res.readEntity(RunResultsDTO.class);
					if (dto.getProcessStepName() != null && dto.getProcessStepName().equalsIgnoreCase("NA Extraction")) {
						deviceRunResultsDTO.setAssayType(dto.getAssayType());
						deviceRunResultsDTO.setComments("-");
						try {
							dtos.add(this.getNAExtractionDTO(Optional.ofNullable(dto)));
						} catch (Exception e) {
							logger.info("Error while mapping NA Extraction Data: "+e.getMessage());
						}
						deviceRunResultsDTO.setNaExtractionData(dtos);
					} else if (dto.getProcessStepName() != null
							&& dto.getProcessStepName().equalsIgnoreCase("Library Preparation")) {
						try {
							dtos1.add(this.getLibraryPrepDTO(Optional.ofNullable(dto)));
						} catch (Exception e) {
							logger.info("Error while mapping Library Preparation Data: "+e.getMessage());
						}
						deviceRunResultsDTO.setLibraryPrepData(dtos1);
					} else if (dto.getProcessStepName() != null && dto.getProcessStepName().equalsIgnoreCase("dPCR")) {
						deviceRunResultsDTO.setNumOfSamples((long) dto.getSampleResults().size());
						try {
							dpcrdtos.add(this.getDPCRResultsData(Optional.ofNullable(dto)));
						} catch (Exception e) {
							logger.info("Error while mapping dPCR analyzer Data: "+e.getMessage());
						}	
						deviceRunResultsDTO.setDpcrData(dpcrdtos);
					}
				}
			});

		}
		}
		return deviceRunResultsDTO;
	}

	@Override
	public Response getWorkflowReport(String country, String language, String deviceRunId)
			throws IOException, JRException, HMTPException {
		logger.info("getWorkflowReport() -> Start of getWorkflowReport() execution ");
		long domainId;
		try {
			domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			List<RunResults> runResults = runResultsReadRepository.findByDeviceRunIdAndProcessStepName(deviceRunId,"dPCR",domainId);
			if(runResults.isEmpty()) {
				throw new HMTPException(CustomErrorCodes.INVALID_DEVICE_RUN_ID,RMMConstant.ErrorMessages.INVALID_DEVICE_RUN_ID.toString());
			}
			JasperPrint jasperPrint = null;
			JasperUtils jasperUtils = new JasperUtils();
			
			JasperReport jasperReport = jasperUtils.loadCompiledJasper("WorkflowSubReporttest.jasper");
			JasperReport subReport1 = jasperUtils.loadCompiledJasper("WorkflowSubReport1test.jasper");
			JasperReport subReport2 = jasperUtils.loadCompiledJasper("WorkflowSubReport2test.jasper");
			JasperReport subReport3 = jasperUtils.loadCompiledJasper("WorkflowSubReport3test.jasper");
			JasperReport dpcrSubReport = jasperUtils.loadCompiledJasper("DPCRResultsSubreport.jasper");
			
			logger.info("Jasper Report compiled");

			List<DeviceRunResultsDTO> runData = new ArrayList<>();
			runData.add(getRunresultsByDevice(deviceRunId));
			List<DeviceRunResultsDTO> data = runData;
			HashMap<String, Object> parameters = new HashMap<>();
			String rocheImageURL = null;
			
			jasperUtils.setReportParameters(parameters, language, country, "workflow");

			rocheImageURL = RestClientUtil.getUrlString("pas.roche_image_url", "", "", "", null);
			parameters.put("rocheLogo", rocheImageURL);
			logger.info("Start of filling the report data with API response");
			if (!data.isEmpty()) {
				parameters.put("subReport2DS", data.get(0).getLibraryPrepData());
				parameters.put("SUBREPORT_DIR", "");
				parameters.put("subReport1", subReport1);
				parameters.put("subReport2", subReport2);
				parameters.put("subReport3", subReport3);
				parameters.put("dpcrSubReport", dpcrSubReport);
				if (!data.get(0).getDpcrData().isEmpty()) {
					parameters.put("dpcrResultsData", data.get(0).getDpcrData());
					parameters.put("dpcrSamplesDatasource", new JRBeanCollectionDataSource(
							data.get(0).getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails()));
					parameters.put("dpcrFlagsDataSource",
							new JRBeanCollectionDataSource(data.get(0).getDpcrData().get(0).getDpcrFlags()));
					parameters.put("dpcrCommentsDataSource",
							new JRBeanCollectionDataSource(data.get(0).getDpcrData().get(0).getDpcrSampleComments()));
					parameters.put("dpcrReagentsDatasource",
							new JRBeanCollectionDataSource(data.get(0).getDpcrData().get(0).getDpcrReagents()));
				}
			}
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
			jasperPrint = jasperUtils.fillReport(jasperReport, parameters, dataSource);
			logger.info("End of filling the report data with API response");

			SimplePdfExporterConfiguration configuration = "true".equalsIgnoreCase(passwordRequired)?jasperUtils.setReportPassword(password):new SimplePdfExporterConfiguration();
			byte[] bytes = jasperUtils.getReportPDF(configuration, jasperPrint);
			logger.info("JasperReport Generated Successfully: ");

			return Response.ok(bytes).type("application/pdf")
					.header("Content-Disposition", "inline; filename=WorkflowReport.pdf;").build();
		} catch (HMTPException e) {
			logger.error("Error while fetching workflow report" + e.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
            errResponse.setErrorStatusCode(e.getErrorCode().getErrorCode());
            errResponse.setErrorMessage(e.getMessage());
            return Response.ok().type("application/json").status(e.getErrorCode().getErrorCode()).entity(errResponse).build();
		} catch (Exception exp) {
			logger.error("Error while fetching workflow report" + exp.getMessage());
			throw new HMTPException(exp);
		}
	}

	public Response searchResult(String searchQuery, int offset, int limit, String content) {

		try {
			if (limit == 0)
				limit = 100;

			if (StringUtils.isBlank(searchQuery) || searchQuery.trim().length() < RMMConstant.SEARCH_KEY_MIN_LENGTH)
				return Response.status(Status.BAD_REQUEST).build();

			searchQuery = searchQuery.replaceAll("_", "\\\\_");
			
			SearchResult searchResult = new SearchResult();
			if (StringUtils.isBlank(content) || content.equalsIgnoreCase(RMMConstant.SEARCH_BOTH_CONTENT_QUERY_PARAM)) {

				SearchOrderElements searchOrderElements = searchService.getSearchOrderElements(searchQuery, offset,
						limit);

				int numberOfSampleResultElementSlice = searchOrderElements.getOrders().size();
				int runOffset = 0;
				int runLimit = limit;
				if (numberOfSampleResultElementSlice == 0) {
					long totalSampleResultRecord = searchOrderElements.getTotalElements();

					runOffset = (int) (offset - totalSampleResultRecord);
				} else if (numberOfSampleResultElementSlice < limit) {
					runLimit = limit - numberOfSampleResultElementSlice;
				}

				searchResult.setSearchOrderElements(searchOrderElements);

				SearchRunResultElements searchRunResultElements = searchService.getSearchRunResultElements(searchQuery,
						runOffset, runLimit);
				if (numberOfSampleResultElementSlice < limit) {
					searchResult.setSearchRunResultElements(searchRunResultElements);
				}

				if (searchResult.getSearchRunResultElements() == null) {
					SearchRunResultElements searchRunResultElementsTemp = new SearchRunResultElements();
					searchRunResultElementsTemp.setTotalElements(searchRunResultElements.getTotalElements());
					searchResult.setSearchRunResultElements(searchRunResultElementsTemp);
				}

			} else if (content.equalsIgnoreCase(RMMConstant.SEARCH_ORDER_CONTENT_QUERY_PARAM)) {
				searchResult.setSearchOrderElements(searchService.getSearchOrderElements(searchQuery, offset, limit));
			} else if (content.equalsIgnoreCase(RMMConstant.SEARCH_RUN_CONTENT_QUERY_PARAM)) {
				searchResult.setSearchRunResultElements(
						searchService.getSearchRunResultElements(searchQuery, offset, limit));
			}

			return Response.ok(searchResult).build();

		} catch (Exception e) {
			logger.info("Error in RMM - search API, message: " + e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	@Override
	public List<RunResults> getCurrentRunResult(String fromDate, String toDate) {
		Date fDate = new Date();
		Date tDate = new Date();
		try {
			fDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
			tDate =  new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			logger.error("Error while retrieving runresults based on date range: " + e.getMessage());
		}
		return runResultsReadRepository.findCurrentRunResult(fDate, tDate);
	}

	@Override
	public Response getRunDetailsByAccessioningId(String accessioningId) throws HMTPException {
		logger.info("getRunDetailsByAccessioningId() -> Start of getRunDetailsByAccessioningId Execution");
		List<Object> inWorkFlowOrders = sampleResultsReadRepository.getInWorkFlowOrderDetails(accessioningId);
		List<WorkflowDTO> inWorkFlowOrdersResp = new LinkedList<>();
		try {
			for (Object o : inWorkFlowOrders) {
				Object[] obj = (Object[]) o;
				WorkflowDTO wfo = new WorkflowDTO();
				wfo.setAccessioningId((String) obj[0]);
				wfo.setComments((String) obj[1]);
				wfo.setOrderId(Long.parseLong(obj[2].toString()));
				wfo.setWorkflowType((String) obj[3]);
				wfo.setAssaytype((String) obj[4]);
				wfo.setSampletype((String) obj[5]);
				wfo.setWorkflowStatus((String) obj[6]);
				wfo.setFlags((String) obj[7]);
				wfo.setCreateDate((Date) obj[8]);
				inWorkFlowOrdersResp.add(wfo);
			}
			logger.info("getRunDetailsByAccessioningId() -> End of getRunDetailsByAccessioningId Execution");
		} catch (Exception exp) {
			logger.info("Error while fetching run details for given accessioning id : " + exp.getMessage());
		}
		return Response.status(200).entity(inWorkFlowOrdersResp).build();
	}
	
	
}
