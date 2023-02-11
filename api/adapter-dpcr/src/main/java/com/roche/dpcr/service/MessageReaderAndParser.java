package com.roche.dpcr.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.dpcr_analyzer.AcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.ESUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUAssay;
import com.roche.connect.common.dpcr_analyzer.ORUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUPartitionEngine;
import com.roche.connect.common.dpcr_analyzer.ORUSampleDetails;
import com.roche.connect.common.dpcr_analyzer.QueryMessage;
import com.roche.dpcr.dto.MessageExchange;
import com.roche.dpcr.util.InstanceUtil;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v26.message.ACK;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.segment.EQU;
import ca.uhn.hl7v2.model.v26.segment.MSA;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.OBR;
import ca.uhn.hl7v2.model.v26.segment.QPD;
import ca.uhn.hl7v2.model.v26.segment.SPM;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

@Service
public class MessageReaderAndParser {

	private static final Logger logger = LogManager.getLogger(MessageReaderAndParser.class);

	private static final String UNSUPPORTED_HL7_MSG = "UNSUPPORTED_HL7_MSG_DPCR";

	private static final String INVALID_VER = "INVALID_HL7_VER_dPCR";

	private static final String MISSING_INFO_DEVICE_DPCR = "MISSING_INFO_DEVICE_DPCR";
	private static final String SIMPLE_DATE_FORMAT = "yyyyMMddHHmmss";

	@Autowired
	private MessageHandlerService messageHandlerService;
	HapiContext context = new DefaultHapiContext();

	public void readQueryMessage(String inMsg, MessageExchange messageExchange)
			throws HL7Exception, IOException, HMTPException {
		logger.info("In MessageReaderAndParser Class - readQueryMessage() -->" + inMsg);
		if (validateHl7Vesrion(messageExchange.getMsgVersion())) {
			if (inMsg.contains("|QBP^Z01|")) {
				convertQueryToPayload(inMsg, messageExchange);
			} else if (inMsg.contains("|ESU^U01|")) {
				convertEquipmentStatusUpdateToPayload(inMsg, messageExchange);
			} else if (inMsg.contains("|ACK^O21|")) {
				convertOMLACKToPayload(inMsg, messageExchange);
			} else if (inMsg.contains("|ORU^R01|")) {
				convertORUToPayload(inMsg, messageExchange);
			} else {
				logger.error("readQueryMessage() --> Invalid MessageType, Please Check HL7 Message From Device");
				if (StringUtils.isNotBlank(messageExchange.getSerialNo())) {
					messageHandlerService.sendNotification(messageExchange.getSerialNo(), UNSUPPORTED_HL7_MSG);
				} else {
					messageHandlerService.sendNotification("", UNSUPPORTED_HL7_MSG);
				}
				messageExchange.getOut().close();
			}
		} else {
			logger.error("readQueryMessage() --> Invalid Version, Please Check HL7 Message From Device");
			if (StringUtils.isNotBlank(messageExchange.getSerialNo())) {
				messageHandlerService.sendNotification(messageExchange.getSerialNo(), INVALID_VER);
			} else {
				messageHandlerService.sendNotification("", INVALID_VER);
			}
			messageExchange.getOut().close();
		}

	}

	private void convertORUToPayload(String inMsg, MessageExchange messageExchange) throws HL7Exception, IOException {
		String msgControlId = "";
		try {
			this.context.getExecutorService();
			Parser parser = this.context.getPipeParser();
			this.context.getParserConfiguration().setDefaultObx2Type("ST");
			ORU_R01 oru = (ORU_R01) parser.parse(inMsg.replace('\n', '\r'));
			ORUMessage oruRun = new ORUMessage();
			List<ORUSampleDetails> oruSampleList = new ArrayList<>();
			MSH msh = oru.getMSH();
			String processingId = msh.getProcessingID().getPt1_ProcessingID().getValue();
			if (validateHl7ProcessingId(processingId)) {
				msgControlId = msh.getMsh10_MessageControlID().getValue();
				oruRun.setDateTimeMessageGenerated(msh.getDateTimeOfMessage().getValue());
				oruRun.setDeviceId(msh.getSendingFacility().getHd1_NamespaceID().getValue());
				oruRun.setMessageType(msh.getMsh9_MessageType().getMsg1_MessageCode().getValue());
				oruRun.setMessageControlId(msgControlId);
				oruRun.setReleasedBy(oru.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getORC().getVerifiedBy(0)
						.getXcn1_IDNumber().getValue());
				oruRun.setOperatorName(oru.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getORC().getEnteredBy(0)
						.getXcn1_IDNumber().getValue());
				oruRun.setSentBy(oru.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getORC().getActionBy(0)
						.getXcn1_IDNumber().getValue());
				oruRun.setRunComments(oru.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getORC()
						.getOrc16_OrderControlCodeReason().getCwe1_Identifier().getValue());
				oruRun.setRunId(oru.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getORC().getPlacerGroupNumber()
						.getEi1_EntityIdentifier().getValue());
				setORUPayload(oru, oruSampleList);
				oruRun.setOruSampleDetails(oruSampleList);
				messageExchange.setSendingApp(msh.getReceivingApplication().getHd1_NamespaceID().getValue());
				messageExchange.setRecevingApp(msh.getSendingApplication().getHd1_NamespaceID().getValue());
				messageExchange.setSendingFacility(msh.getReceivingFacility().getHd1_NamespaceID().getValue());
				messageExchange.setRecevingFacility(msh.getSendingFacility().getHd1_NamespaceID().getValue());
				messageExchange.setSerialNo(msh.getSendingFacility().getHd1_NamespaceID().getValue());
				messageExchange.setAckTriggerEvent(msh.getMessageType().getMsg2_TriggerEvent().getValue());
				InstanceUtil.getInstance().getConnectionMap().put(msgControlId, messageExchange);
				messageHandlerService.sendORUToIMM(oruRun);
			} else {

				unsupportedHL7MsgValidation(messageExchange);

			}
		} catch (Exception e) {
			messageExchange.getOut().close();
			InstanceUtil.getInstance().getConnectionMap().remove(msgControlId);
			logger.error("Error while parsing OUL: ", e.getMessage());
		}

	}

	private void setORUPayload(ORU_R01 oru, List<ORUSampleDetails> oruSampleList) throws IOException {
		try {
			int sampleCount = oru.getPATIENT_RESULT(0).getORDER_OBSERVATIONReps();
			for (int i = 0; i < sampleCount; i++) {
				List<ORUPartitionEngine> partitionEngineList = new ArrayList<>();
				List<ORUAssay> assayList = new ArrayList<>();
				ORUSampleDetails oruSample = new ORUSampleDetails();
				ORU_R01_ORDER_OBSERVATION orderObv = oru.getPATIENT_RESULT(0).getORDER_OBSERVATION(i);
				oruSample.setAccessioningId(orderObv.getORC().getPlacerOrderNumber().getEntityIdentifier().getValue());
				setPartitionEngineList(partitionEngineList, orderObv);
				StringJoiner stringJoiner = setFlagCodes(assayList, orderObv);
				oruSample.setFlag(stringJoiner.toString());
				oruSample.setAssayList(assayList);
				oruSample.setPartitionEngineList(partitionEngineList);
				oruSampleList.add(oruSample);
			}
		} catch (Exception e) {
			throw new IOException("Error in setORUPayload method");
		}
	}

	private StringJoiner setFlagCodes(List<ORUAssay> assayList, ORU_R01_ORDER_OBSERVATION orderObv) throws IOException {
		StringJoiner stringJoiner = new StringJoiner(",");
		try {
			int nteCount = setAssayList(assayList, orderObv);

			for (int k = 0; k < nteCount; k++) {
				stringJoiner.add(orderObv.getOBSERVATION(0).getNTE(k).getComment(0).getValue());
			}
		} catch (Exception e) {
			throw new IOException("Error in setFlagCodes method");
		}
		return stringJoiner;
	}

	private int setAssayList(List<ORUAssay> assayList, ORU_R01_ORDER_OBSERVATION orderObv) throws IOException {

		int nteCount = 0;
		try {
			ORUAssay assay = new ORUAssay();
			OBR obr = orderObv.getOBR();
			assay.setKit(obr.getUniversalServiceIdentifier().getAlternateIdentifier().getValue());
			assay.setName(obr.getUniversalServiceIdentifier().getIdentifier().getValue());
			assay.setPackageName(obr.getUniversalServiceIdentifier().getText().getValue());
			assay.setQualitativeResult(orderObv.getOBSERVATION(1).getOBX().getObservationResultStatus().getValue());
			assay.setQualitativeValue(orderObv.getOBSERVATION(1).getOBX().getObservationValue(0).getData().toString());
			String quantitativeValue = orderObv.getOBSERVATION(0).getOBX().getObservationValue(0).getData().toString()
					+ orderObv.getOBSERVATION(0).getOBX().getUnits().getIdentifier().getValue();
			assay.setQuantitativeResult(orderObv.getOBSERVATION(0).getOBX().getObservationResultStatus().getValue());
			assay.setQuantitativeValue(quantitativeValue);
			assay.setVersion(obr.getUniversalServiceIdentifier().getNameOfCodingSystem().getValue());
			nteCount = orderObv.getOBSERVATION(0).getNTEReps();
			assayList.add(assay);
		} catch (Exception e) {
			throw new IOException("Error in setAssayList method: ");
		}
		return nteCount;
	}

	private void setPartitionEngineList(List<ORUPartitionEngine> partitionEngineList,
			ORU_R01_ORDER_OBSERVATION orderObv) throws IOException {
		try {
			int partitionEngineCount = orderObv.getSPECIMENReps();

			for (int j = 0; j < partitionEngineCount; j++) {
				ORUPartitionEngine partitionEngine = new ORUPartitionEngine();
				SPM spm = orderObv.getSPECIMEN(j).getSPM();
				partitionEngine
						.setPlateId(spm.getSpecimenID().getPlacerAssignedIdentifier().getEntityIdentifier().getValue());
				partitionEngine.setFluidId(spm.getSpecimenSourceSiteModifier(0).getIdentifier().getValue());
				partitionEngine.setSerialNumber(spm.getSpecimenSourceSite().getIdentifier().getValue());
				partitionEngine.setDateandTime(spm.getSpecimenCollectionDateTime().getRangeStartDateTime().getValue());
				partitionEngineList.add(partitionEngine);
			}
		} catch (Exception e) {
			throw new IOException("Error in setPartitionEngineList method: ");
		}
	}

	private void convertOMLACKToPayload(String inMsg, MessageExchange messageExchange)
			throws HL7Exception, IOException {
		logger.info("In MessageReaderAndParser Class - convertOMLACKToPayload() -->" + inMsg);
		try {
			this.context.getExecutorService();
			Parser parser = this.context.getPipeParser();
			Message omlAckMessage = parser.parse(inMsg.replace('\n', '\r'));
			AcknowledgementMessage acknowledgementMessage = new AcknowledgementMessage();
			MSH msh = (MSH) omlAckMessage.get("MSH");
			MSA msa = (MSA) omlAckMessage.get("MSA");

			String processingId = msh.getProcessingID().getPt1_ProcessingID().getValue();
			if (validateHl7ProcessingId(processingId)) {
				String msgControlId = msh.getMsh10_MessageControlID().getValue();
				acknowledgementMessage.setDateTimeMessageGenerated(msh.getDateTimeOfMessage().getValue());
				acknowledgementMessage.setMessageControlId(msgControlId);
				acknowledgementMessage.setStatus(msa.getAcknowledgmentCode().getValue());
				acknowledgementMessage.setDeviceSerialNo(msh.getSendingFacility().getHd1_NamespaceID().getValue());
				if ("AR".equalsIgnoreCase(msa.getAcknowledgmentCode().getValue())) {
					Terser terser = new Terser(omlAckMessage);
					acknowledgementMessage.setErrorCode(terser.get("/.ERR-3-1"));
					acknowledgementMessage.setErrorDescription(terser.get("/.ERR-3-2"));
				}
				MessageExchange omlExchange = InstanceUtil.getInstance().getConnectionMap().get(msgControlId);
				logger.info("RunId from OML exchange: " + omlExchange.getRunId());
				acknowledgementMessage.setRunId(omlExchange.getRunId());
				InstanceUtil.getInstance().getConnectionMap().remove(msgControlId);
				messageHandlerService.sendOMLACKToIMM(acknowledgementMessage);
			} else {
				unsupportedHL7MsgValidation(messageExchange);
			}
		} catch (HL7Exception | HMTPException hl7Exception) {
			logger.error("Error while parsing ACK: ", hl7Exception.getMessage());
			messageExchange.getOut().close();

		} finally {
			if (context != null) {
				context.close();
			}
		}

	}

	private void unsupportedHL7MsgValidation(MessageExchange messageExchange) throws HMTPException, HL7Exception {
		if (StringUtils.isNotBlank(messageExchange.getSerialNo())) {
			messageHandlerService.sendNotification(messageExchange.getSerialNo(), UNSUPPORTED_HL7_MSG);
		} else {
			messageHandlerService.sendNotification("", UNSUPPORTED_HL7_MSG);
		}
		throw new HL7Exception(
				"convertOMLACKToPayload() --> Invalid Processing Id, Please Check HL7 Message From Device");
	}

	private void convertEquipmentStatusUpdateToPayload(String inMsg, MessageExchange messageExchange)
			throws HL7Exception, HMTPException, IOException {
		logger.info("In MessageReaderAndParser Class - convertEquipmentStatusUpdate() -->" + inMsg);
		try {
			this.context.getExecutorService();
			Parser parser = this.context.getPipeParser();
			SimpleDateFormat f = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
			Message queryMessage = parser.parse(inMsg.replace('\n', '\r'));
			MSH msh = (MSH) queryMessage.get("MSH");
			EQU equ = (EQU) queryMessage.get("EQU");
			String processingId = msh.getProcessingID().getPt1_ProcessingID().getValue();
			if (validateHl7ProcessingId(processingId)) {
				ESUMessage esuMessage = new ESUMessage();
				String msgControlId = msh.getMsh10_MessageControlID().getValue();
				messageExchange.setSendingApp(msh.getReceivingApplication().getHd1_NamespaceID().getValue());
				messageExchange.setRecevingApp(msh.getSendingApplication().getHd1_NamespaceID().getValue());
				messageExchange.setSendingFacility(msh.getReceivingFacility().getHd1_NamespaceID().getValue());
				messageExchange.setAckTriggerEvent(msh.getMessageType().getMsg2_TriggerEvent().getValue());
				String status = equ.getEqu3_EquipmentState().getCwe1_Identifier().getValue();
				messageExchange.setRecevingFacility("");
				messageExchange.setSerialNo(msh.getSendingFacility().getHd1_NamespaceID().getValue());
				if (status == null || status.isEmpty()) {
					if (StringUtils.isNotBlank(messageExchange.getSerialNo())) {
						messageHandlerService.sendNotification(messageExchange.getSerialNo(), MISSING_INFO_DEVICE_DPCR);
					} else {
						messageHandlerService.sendNotification("", MISSING_INFO_DEVICE_DPCR);
					}
					generateAndSendACK(messageExchange, f, queryMessage, "AR");
				} else if ("OP".equalsIgnoreCase(status)
						&& (equ.getEqu3_EquipmentState().getCwe2_Text().getValue() == null)
						&& (equ.getEqu5_AlertLevel().getCwe2_Text().getValue() == null)) {
					sendESUtoPayload(messageExchange, msh, equ, esuMessage, msgControlId, "InProgress");
				} else if ("SS".equalsIgnoreCase(status)
						&& (equ.getEqu3_EquipmentState().getCwe2_Text().getValue() == null)
						&& (equ.getEqu5_AlertLevel().getCwe2_Text().getValue() == null)) {
					sendESUtoPayload(messageExchange, msh, equ, esuMessage, msgControlId, status);
				} else if ("ID".equalsIgnoreCase(status) && ((StringUtils
						.isNotBlank(equ.getEquipmentInstanceIdentifier().getEi1_EntityIdentifier().getValue()))
						|| (StringUtils
								.isNotBlank(equ.getEquipmentInstanceIdentifier().getEi2_NamespaceID().getValue())))) {
					sendESUtoPayload(messageExchange, msh, equ, esuMessage, msgControlId, status);
				} else if ("ES".equalsIgnoreCase(status)) {
					generateAndSendACK(messageExchange, f, queryMessage, "AA");

				} else if ("OP".equalsIgnoreCase(status)
						&& (equ.getEqu3_EquipmentState().getCwe2_Text().getValue() != null
								&& !equ.getEqu3_EquipmentState().getCwe2_Text().getValue().isEmpty())
						&& (equ.getEqu5_AlertLevel().getCwe2_Text().getValue() != null
								&& !equ.getEqu5_AlertLevel().getCwe2_Text().getValue().isEmpty())
						&& (messageExchange.getSerialNo() != null && !messageExchange.getSerialNo().isEmpty())) {
					generateAndSendACK(messageExchange, f, queryMessage, "AA");

				} else if ("OP".equalsIgnoreCase(status)
						&& !((equ.getEqu3_EquipmentState().getCwe2_Text().getValue() != null
								&& !equ.getEqu3_EquipmentState().getCwe2_Text().getValue().isEmpty())
								&& (equ.getEqu5_AlertLevel().getCwe2_Text().getValue() != null
										&& !equ.getEqu5_AlertLevel().getCwe2_Text().getValue().isEmpty())
								&& (messageExchange.getSerialNo() != null
										&& !messageExchange.getSerialNo().isEmpty()))) {
					generateAndSendACK(messageExchange, f, queryMessage, "AR");
				} else if ("ID".equalsIgnoreCase(status)
						&& (equ.getEquipmentInstanceIdentifier().getEi1_EntityIdentifier().getValue() == null || equ
								.getEquipmentInstanceIdentifier().getEi1_EntityIdentifier().getValue().isEmpty())) {
					generateAndSendACK(messageExchange, f, queryMessage, "AA");
				} else {
					generateAndSendACK(messageExchange, f, queryMessage, "AR");
				}
			} else {
				unsupportedHL7MsgValidation(messageExchange);
			}
		} catch (HL7Exception hl7Exception) {
			logger.error("Error while parsing ESU: ", hl7Exception.getMessage());
			messageExchange.getOut().close();

		} finally {
			if (context != null) {
				context.close();
			}
		}
	}

	private void sendESUtoPayload(MessageExchange messageExchange, MSH msh, EQU equ, ESUMessage esuMessage,
			String msgControlId, String status) throws JsonProcessingException, UnsupportedEncodingException {
		esuMessage.setMessageControlId(msgControlId);
		esuMessage.setStatus(status);
		esuMessage.setRunId(equ.getEquipmentInstanceIdentifier().getEi1_EntityIdentifier().getValue());
		esuMessage.setMessageType(msh.getMsh9_MessageType().getMsg1_MessageCode().getValue());
		esuMessage.setDeviceId(msh.getSendingFacility().getHd1_NamespaceID().getValue());
		esuMessage.setEstimatedTimeRemaining(equ.getEventDateTime().getValue());
		esuMessage.setFilePath(equ.getEquipmentInstanceIdentifier().getEi2_NamespaceID().getValue());
		esuMessage.setDateTimeMessageGenerated(msh.getDateTimeOfMessage().getValue());
		InstanceUtil.getInstance().getConnectionMap().put(msgControlId, messageExchange);
		messageHandlerService.sendESUToIMM(esuMessage);
	}

	private void generateAndSendACK(MessageExchange messageExchange, SimpleDateFormat f, Message queryMessage,
			String status) throws HL7Exception, IOException {
		ACK ack = (ACK) queryMessage.generateACK();
		ack.getMSA().getMsa1_AcknowledgmentCode().setValue(status);
		ack.getMSH().getDateTimeOfMessage().setValue(f.format(new Date()));
		ack.getMSH().getVersionID().getVid1_VersionID().setValue(messageExchange.getMsgVersion());
		ack.getMSH().getMessageControlID().setValue(ack.getMSA().getMsa2_MessageControlID().getValue());
		ack.getMSH().getMessageType().getMsg3_MessageStructure().clear();
		ack.getMSH().getReceivingFacility().clear();
		String output = "\u000b" + ack.toString() + "\u001c" + "\r";
		messageExchange.getOut().write(output.getBytes());
		messageExchange.getOut().close();
	}

	private void convertQueryToPayload(String inMsg, MessageExchange messageExchange)
			throws HL7Exception, IOException, HMTPException {
		logger.info("In MessageReaderAndParser Class - convertQueryToPayload() -->" + inMsg);
		try {
			this.context.getExecutorService();
			Parser parser = this.context.getPipeParser();
			Message queryMessage = parser.parse(inMsg.replace('\n', '\r'));
			QueryMessage query = new QueryMessage();
			MSH msh = (MSH) queryMessage.get("MSH");
			QPD qpd = (QPD) queryMessage.get("QPD");
			String processingId = msh.getProcessingID().getPt1_ProcessingID().getValue();
			if (validateHl7ProcessingId(processingId)) {
				String msgControlId = msh.getMsh10_MessageControlID().getValue();
				query.setMessageControlId(msgControlId);
				query.setDeviceId(msh.getSendingFacility().getHd1_NamespaceID().getValue());
				query.setMessageType(msh.getMsh9_MessageType().getMsg1_MessageCode().getValue());
				query.setDateTimeMessageGenerated(msh.getDateTimeOfMessage().getValue());
				messageExchange.setSendingApp(msh.getReceivingApplication().getHd1_NamespaceID().getValue());
				messageExchange.setRecevingApp(msh.getSendingApplication().getHd1_NamespaceID().getValue());
				messageExchange.setSendingFacility(msh.getReceivingFacility().getHd1_NamespaceID().getValue());
				messageExchange.setRecevingFacility("");
				messageExchange.setSerialNo(msh.getSendingFacility().getHd1_NamespaceID().getValue());
				messageExchange.setAckTriggerEvent(msh.getMessageType().getMsg2_TriggerEvent().getValue());
				List<String> plateIds = new ArrayList<>();
				Terser t = new Terser(queryMessage);
				int n = 3;
				for (int i = 0; i < qpd.numFields() - 2; i++) {
					plateIds.add(t.get("/QPD-" + n));
					n += 1;
				}
				query.setPlateId(plateIds);
				InstanceUtil.getInstance().getConnectionMap().put(msgControlId, messageExchange);
				messageHandlerService.sendQueryToIMM(query);
			} else {
				unsupportedHL7MsgValidation(messageExchange);
			}
		} catch (HL7Exception hl7Exception) {
			logger.error("Error while parsing Query: ", hl7Exception.getMessage());
			messageExchange.getOut().close();

		} finally {
			if (context != null) {
				context.close();
			}
		}

	}

	private boolean validateHl7Vesrion(String version) {

		ResourceBundle mybundle = ResourceBundle.getBundle("application");
		String hl7Versions = mybundle.getString("HL7_VERSIONS");
		return hl7Versions.contains(version);

	}

	private boolean validateHl7ProcessingId(String pId) {

		ResourceBundle mybundle = ResourceBundle.getBundle("application");
		String processingIds = mybundle.getString("PROCESSING_ID");
		return processingIds.contains(pId);

	}

}
