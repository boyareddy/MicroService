package com.roche.connect.imm.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.StatusMessage;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.ContainerInfo;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.imm.model.CustomException;
import com.roche.connect.imm.utils.UrlConstants;

@Service
public class LP24MessageService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	private String mp96ContainerIdPositionRegEx = "^[A-H][1-9][0-2]?$|^12$";

	@Value("${connect.wfm_host_url}")
	private String wfmHostUrl;
	
	@Autowired
	private RmmIntegrationService rmmIntegrationService;

	@Autowired
	private AssayIntegrationService assayIntegrationService;

	@Autowired
	private LP24AsyncMessageService lp24AsyncMessageService;

	@Autowired
	private MessageProcessorService messageProcessorService;

	@Autowired
	private AssayIntegrationService assayService;

	public Response processQueryMessage(QueryMessage queryMessage) {

		try {

			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());

			if (!isValidLP24InputContainerId(queryMessage.getContainerId())) {
				lp24AsyncMessageService.performAsyncLP24LApplicationRejectMessage(queryMessage, token);
				return Response.ok(HttpStatus.SC_OK).build();
			}

			String[] s = queryMessage.getContainerId().split("_");
			String inputContainerId = s[0];
			String inputContainerPosition = s[1];

			List<SampleResultsDTO> sampleResultsList = rmmIntegrationService.getSampleResults(null, null,
					inputContainerId, inputContainerPosition, null, null, null);
			
			if (sampleResultsList == null || sampleResultsList.isEmpty() || sampleResultsList.get(0) == null) {
				lp24AsyncMessageService.performAsyncLP24LApplicationRejectMessage(queryMessage, token);
				return Response.ok(HttpStatus.SC_OK).build();
			}

			SampleResultsDTO sampleResultsDTO = sampleResultsList.get(0);

			long orderId = sampleResultsDTO.getOrderId();
			if (sampleResultsDTO.getAssayType().equals(AssayType.NIPT_DPCR)) {

				if (!isValidMP96OutputContainerId(queryMessage.getContainerId())) {
					lp24AsyncMessageService.performAsyncLP24LApplicationRejectMessage(queryMessage, token);
					return Response.ok(HttpStatus.SC_OK).build();
				}
				queryMessage.setAccessioningId(sampleResultsDTO.getAccesssioningId());
				String url = wfmHostUrl + UrlConstants.WFM_DPCR_LP24_QUERY_URL;
				lp24AsyncMessageService.processAsyncMessage(queryMessage, url, token);
			} else if (sampleResultsDTO.getAssayType().equals(AssayType.NIPT_HTP)) {

				List<ProcessStepActionDTO> processStepList = assayService.getProcessStepAction(AssayType.NIPT_HTP,
						DeviceType.LP24);

				Optional<ProcessStepActionDTO> firstProcessStepName = processStepList.stream()
						.filter(e -> e.getProcessStepSeq().equals(4)).findFirst();

				String lpSeqProcessStepName = firstProcessStepName.isPresent()
						? firstProcessStepName.get().getProcessStepName()
						: null;

				String nextProcessStepName = getCurrentProcessNameFromInputContainerId(queryMessage.getContainerId(),
						queryMessage.getSendingApplicationName(), sampleResultsDTO);

				if (nextProcessStepName != null && lpSeqProcessStepName != null) {
					queryMessage.setProcessStepName(nextProcessStepName);
					if (nextProcessStepName.equalsIgnoreCase(lpSeqProcessStepName))
						messageProcessorService.processLpSeqRequest(queryMessage, orderId, token);
					else
						messageProcessorService.processRequest(queryMessage, token);
				} else {
					throw new HMTPException("Next Process step name not exist in Assay Management after");
				}
			} else {
				return Response.status(HttpStatus.SC_BAD_REQUEST).build();
			}

			return Response.status(Status.OK).build();

		} catch (IOException e) {
			logger.error("Failed to get SampleResults, message: " + e.getMessage(), e);
			return Response.status(HttpStatus.SC_FAILED_DEPENDENCY).build();
		} catch (Exception e) {
			CustomException exception = new CustomException();
			exception.setErrorCode(401);
			exception.setErrorMessage(e.getMessage());
			logger.error("Failed to handle QBP Adaptor request, message: " + e.getMessage(), e);
			return Response.status(Status.CONFLICT).entity(exception).build();
		}

	}

	public Response processQueryResponseMessage(ResponseMessage responseMessage) {

		try {
			logger.info("Inside RSPWFMrequest :" + responseMessage.getDeviceSerialNumber());
			messageProcessorService.submitResponse(responseMessage);

			return Response.status(Status.OK).build();

		} catch (Exception e) {
			logger.error("Failed to handle RSP WFM request, message: " + e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	public Response processStatusUpdateMessages(SpecimenStatusUpdateMessage specimenStatusUpdateMessage) {
		try {

			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());

			if (!isValidLP24InputContainerId(specimenStatusUpdateMessage.getContainerId())) {
				lp24AsyncMessageService.performLP24SSUFailureMessage(specimenStatusUpdateMessage, token);
				return Response.status(Status.OK).build();
			}

			String[] s = specimenStatusUpdateMessage.getContainerId().split("_");
			String inputContainerId = s[0];
			String inputContainerPosition = s[1];

			List<SampleResultsDTO> sampleResultsList = rmmIntegrationService.getSampleResults(null, null,
					inputContainerId, inputContainerPosition, null, null, null);

			if (sampleResultsList == null || sampleResultsList.isEmpty() || sampleResultsList.get(0) == null)
				return Response.status(HttpStatus.SC_NO_CONTENT).build();

			SampleResultsDTO sampleResultsDTO = sampleResultsList.get(0);

			if (sampleResultsDTO.getAssayType().equals(AssayType.NIPT_DPCR)) {

				if (!isValidSpecimenStatusUpdateMessage(specimenStatusUpdateMessage)) {
					lp24AsyncMessageService.performLP24SSUFailureMessage(specimenStatusUpdateMessage, token);
					return Response.status(Status.OK).build();
				}

				specimenStatusUpdateMessage.setAccessioningId(sampleResultsDTO.getAccesssioningId());
				String url = wfmHostUrl + UrlConstants.WFM_DPCR_LP24_SSU_URL;
				lp24AsyncMessageService.processAsyncMessage(specimenStatusUpdateMessage, url, token);

			} else if (sampleResultsDTO.getAssayType().equals(AssayType.NIPT_HTP)) {

				List<ProcessStepActionDTO> processStepList = assayService.getProcessStepAction(AssayType.NIPT_HTP,
						DeviceType.LP24);

				Optional<ProcessStepActionDTO> firstProcessStep = processStepList.stream()
						.filter(e -> e.getProcessStepSeq().equals(4)).findFirst();

				String lpSeqProcessStepName = firstProcessStep.isPresent() ? firstProcessStep.get().getProcessStepName()
						: null;

				String nextProcessStepName = getCurrentProcessNameFromInputContainerId(
						specimenStatusUpdateMessage.getContainerId(),
						specimenStatusUpdateMessage.getSendingApplicationName(), sampleResultsDTO);

				if (nextProcessStepName != null && lpSeqProcessStepName != null) {
					specimenStatusUpdateMessage.setProcessStepName(nextProcessStepName);

					if (nextProcessStepName.equalsIgnoreCase(lpSeqProcessStepName)) {
						messageProcessorService.processLpSeqRequest(specimenStatusUpdateMessage, token);
					} else {
						messageProcessorService.processRequest(specimenStatusUpdateMessage, token);
					}
				} else {
					throw new HMTPException("Next Process step name not exist in Assay Management after");
				}
			} else {
				return Response.status(HttpStatus.SC_BAD_REQUEST).build();
			}

			return Response.status(Status.OK).build();

		} catch (IOException e) {
			logger.error("Failed to get SampleResults, message: " + e.getMessage(), e);
			return Response.status(HttpStatus.SC_FAILED_DEPENDENCY).build();
		} catch (Exception e) {
			logger.error("Failed to handle SSU Adaptor request, message: " + e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	public Response processACKMessages(AcknowledgementMessage acknowledgementMessage) {
		try {

			messageProcessorService.submitResponse(acknowledgementMessage);

			return Response.status(Status.OK).build();

		} catch (Exception e) {
			logger.error("Failed to handle ACK WFM request, message: " + e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	private boolean isValidSpecimenStatusUpdateMessage(SpecimenStatusUpdateMessage ssuMessage) {

		// Specimen Details Check
		if (StringUtils.isBlank(ssuMessage.getStatusUpdate().getRunResult())// run level
				|| StringUtils.isBlank(ssuMessage.getStatusUpdate().getOrderName())// run id
				|| StringUtils.isBlank(ssuMessage.getStatusUpdate().getOrderResult()))// sample result level
			return false;

		// I/P Container Details Check
		if (!isValidMP96OutputContainerId(ssuMessage.getContainerId()))
			return false;

		// O/P Container Details Check
		if(ssuMessage.getStatusUpdate().getOrderResult().equalsIgnoreCase(StatusMessage.LP24_STATUS_MESSAGE_FAILED))
			return true;
		
		ContainerInfo containerInfo = ssuMessage.getStatusUpdate().getContainerInfo();
        return (StringUtils.isBlank(containerInfo.getCarrierBarcode())
				|| StringUtils.isBlank(containerInfo.getCarrierPosition())
                || StringUtils.isBlank(containerInfo.getCarrierType())) ? false : true;

	}

	private boolean isValidMP96OutputContainerId(String containerId) {

		if (StringUtils.isBlank(containerId))
			return false;

		String[] s = containerId.split("_");
        return (s.length < 2 || StringUtils.isBlank(s[0]) || StringUtils.isBlank(s[1])
                || !Pattern.matches(mp96ContainerIdPositionRegEx, s[1])) ? false : true;


	}

	private boolean isValidLP24InputContainerId(String containerId) {

		if (StringUtils.isBlank(containerId))
			return false;
 
		String[] s = containerId.split("_");
        return (s.length < 2 || StringUtils.isBlank(s[0]) || StringUtils.isBlank(s[1])) ? false : true;

	}

	private String getCurrentProcessNameFromInputContainerId(String inputContainer, String sendingApplicationName,
			SampleResultsDTO sampleResultsDTO) throws HMTPException, UnsupportedEncodingException {

		List<ProcessStepActionDTO> processStepActions = assayIntegrationService.getProcessStepAction(AssayType.NIPT_HTP,
				null);

		if (!processStepActions.isEmpty()) {
			RunResultsDTO runResultsDTO = rmmIntegrationService.getRunResultsById(sampleResultsDTO.getRunResultsId());

			if (runResultsDTO != null) {
				try {
					Optional<ProcessStepActionDTO> first = processStepActions.stream()
							.filter(e -> e.getProcessStepName().equalsIgnoreCase(runResultsDTO.getProcessStepName()))
							.findFirst();

					ProcessStepActionDTO currentProcessStepAction = first.isPresent() ? first.get() : null;
					if (currentProcessStepAction != null) {

						Optional<ProcessStepActionDTO> firstProcessStepActions = processStepActions.stream().filter(
								e -> e.getProcessStepSeq() == (currentProcessStepAction.getProcessStepSeq() + 1))
								.findFirst();

						ProcessStepActionDTO nextProcessStepAction = firstProcessStepActions.isPresent()
								? firstProcessStepActions.get()
								: null;

						if (nextProcessStepAction != null) {
							return nextProcessStepAction.getProcessStepName();
						} else {
							throw new HMTPException(
									"Next Process step not exit with Assay Management for process step seq"
											+ (currentProcessStepAction.getProcessStepSeq() + 1));
						}
					} else {
						throw new HMTPException("ProcessStepName in Run result not exit with Assay Management"
								+ runResultsDTO.getProcessStepName());
					}
				} catch (Exception e) {
					logger.info("Exception occured on getCurrentProcessNameFromInputContainerId" + e.getMessage());
					throw new HMTPException(e);
				}

			} else {
				throw new HMTPException(
						"Run result not exist with Result management, Run Id" + sampleResultsDTO.getRunResultsId());
			}

		} else {
			throw new HMTPException(
					sendingApplicationName + " device application name not exist with assay management");
		}
	}

}
