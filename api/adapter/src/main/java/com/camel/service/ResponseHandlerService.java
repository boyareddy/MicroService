package com.camel.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.camel.dto.ContainerInfo;
import com.camel.dto.HL7HeaderSegmentDTO;
import com.camel.dto.MP24ConfigDetails;
import com.camel.dto.MessageContainerDTO;
import com.camel.dto.MessagePayload;
import com.camel.dto.RSPMessageDTO;
import com.camel.dto.ResponseDTO;
import com.camel.dto.ResponseDTO.Status;
import com.camel.dto.SampleInfo;
import com.camel.dto.SampleTypeDTO;
import com.camel.dto.StatusUpdate;
import com.camel.util.AppUrlEntry;
import com.camel.util.InstanceUtil;
import com.camel.util.MP24ParamConfig;
import com.camel.util.RouterUrlCofig;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v251.datatype.NM;
import ca.uhn.hl7v2.model.v251.group.SSU_U03_SPECIMEN_CONTAINER;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.message.RSP_K11;
import ca.uhn.hl7v2.model.v251.message.SSU_U03;
import ca.uhn.hl7v2.model.v251.segment.EQU;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.OBX;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.QAK;
import ca.uhn.hl7v2.model.v251.segment.QPD;
import ca.uhn.hl7v2.model.v251.segment.SAC;
import ca.uhn.hl7v2.model.v251.segment.SPM;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

@RestController
@RequestMapping("/msghandlerservice")
public class ResponseHandlerService {
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	private static Logger logger = LogManager.getLogger(ResponseHandlerService.class);
	
	@RequestMapping(value = "/sendrsptomp24", method=RequestMethod.POST)
    public void sendResponseToMP24(@RequestBody ResponseDTO<MessagePayload> responseDTO) {
		logger.info("Processing on thread:::" + Thread.currentThread().getName());
		
		ObjectMapper mapper = new ObjectMapper();
		
        try {
        	logger.info("RSP_K11 message JSON :::::" + mapper.writeValueAsString(responseDTO));
			Message rspMessage = processRSPMessage(responseDTO);
			MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap().get(responseDTO.getResponse().getAccessioningId());
			InstanceUtil.getInstance().getConnectionMap().remove(responseDTO.getResponse().getAccessioningId());
			Exchange exchange = containerDTO.getExchange();
			exchange.getOut().setBody(rspMessage);
			logger.info(exchange.getOut().getBody(String.class));
			AsyncCallback callback = (AsyncCallback) containerDTO.getCallback();
			callback.done(false);
			InstanceUtil.getInstance().getConnectionMap().remove(responseDTO.getResponse().getAccessioningId());
        } catch (HL7Exception e) {
			logger.error("HLException:::::Error in handling the HL7 message");
		} catch (ReceivingApplicationException e) {
			logger.error("ReceivingApplicationException:::::Error in sending the HL7 message");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
	
	@RequestMapping(value = "/sendacktomp24", method=RequestMethod.POST)
    public void sendACKToMP24(@RequestBody ResponseDTO<MessagePayload> responseDTO) {
        logger.info("Processing ACK_U03 message in thread:::::" + Thread.currentThread().getName());
        try {
			MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap().get(responseDTO.getResponse().getAccessioningId());
			Resource resource = resourceLoader.getResource("classpath:hl7/ACK_U03.txt");
        	InputStream stream = resource.getInputStream();
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
			String msg = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
			Parser p = new DefaultHapiContext().getPipeParser();
			ACK queryMessage1 = (ACK) p.parse(msg);

			MSH msh1 = (MSH) queryMessage1.get("MSH");
			MSA msa = (MSA) queryMessage1.get("MSA");
			HL7HeaderSegmentDTO headerSegmentDTO = containerDTO.getHeaderSegment();
			msh1.getMsh3_SendingApplication().getHd1_NamespaceID().setValue(headerSegmentDTO.getReceivingApplication());
			msh1.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getSendingFacility());
			msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getReceivingFacility());
			msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().setValue(headerSegmentDTO.getSendingApplicationName());
			msh1.getMsh5_ReceivingApplication().getHd2_UniversalID().setValue(headerSegmentDTO.getDeviceSerialNumber());
			msh1.getMsh10_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
			msh1.getMsh7_DateTimeOfMessage().getTime().setValue(f.format(new Date()));
			msh1.getMsh11_ProcessingID().getProcessingID().setValue(headerSegmentDTO.getProcessingId());
			msh1.getMsh12_VersionID().getVersionID().setValue(headerSegmentDTO.getVersionId());
			
			msa.getMsa2_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
			if(responseDTO.IsSuccessful()) {
				
				msa.getMsa1_AcknowledgmentCode().setValue("AA");
			}
			Exchange exchange = containerDTO.getExchange();
			exchange.getOut().setBody(queryMessage1);
			logger.info(exchange.getOut().getBody(String.class));
			AsyncCallback callback = (AsyncCallback) containerDTO.getCallback();
			callback.done(false);
			InstanceUtil.getInstance().getConnectionMap().remove(responseDTO.getResponse().getAccessioningId());
		} catch (HL7Exception e) {
			logger.error("HLException:::::Error in handling the HL7 message");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public void convertQBPToMsgPayload(HL7HeaderSegmentDTO headerSegmentDTO) throws HL7Exception {
		logger.info("Processing QBP_Q11 message in thread:::::" + Thread.currentThread().getName());
		MessagePayload messagePayload = new MessagePayload();
		messagePayload.setDeviceSerialNumber(headerSegmentDTO.getDeviceSerialNumber());
		messagePayload.setSendingApplicationName(headerSegmentDTO.getSendingApplicationName());
		messagePayload.setDateTimeMessageGenerated(headerSegmentDTO.getDateTimeMessageGenerated());
		messagePayload.setMessageType("NA-Extraction");
		messagePayload.setAccessioningId(headerSegmentDTO.getSampleID());
		
		messagePayload.setReceivingApplication(headerSegmentDTO.getReceivingApplication());
		messagePayload.setMessageControlId(headerSegmentDTO.getMessageControlId());
		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.info("QBP_Q11 message converted to JSON :::::" + mapper.writeValueAsString(messagePayload));
		} catch (JsonGenerationException e) {
			logger.error("JsonGenerationException:::::" + e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException:::::" + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException:::::" + e.getMessage());
		}
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<MessagePayload> entity = new HttpEntity<>(messagePayload, headers);
		String url = RouterUrlCofig.getInstance().getUrl(AppUrlEntry.SEND_REQUEST);
		restTemplate.exchange(url,HttpMethod.POST, entity, Void.class);
	}
	
	private Message processRSPMessage(ResponseDTO<MessagePayload> theMessage)
			throws ReceivingApplicationException, HL7Exception {

		logger.info("Processing RSP_K11 message in thread:::::" + Thread.currentThread().getName());
		HapiContext context = new DefaultHapiContext();
		try {
			
			Resource resource = resourceLoader.getResource("classpath:hl7/RSP_K11.txt");
        	InputStream stream = resource.getInputStream();
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
			String msg = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
			context.getParserConfiguration().addForcedEncode("SPM-27");
			context.getParserConfiguration().addForcedEncode("OBR-30");
			Parser p = context.getPipeParser();
			RSP_K11 queryMessage1 = (RSP_K11) p.parse(msg);

			MSH msh1 = (MSH) queryMessage1.get("MSH");
			MSA msa = (MSA) queryMessage1.get("MSA");
			QAK qak = (QAK) queryMessage1.get("QAK");
			QPD qpd = (QPD) queryMessage1.get("QPD");
			SPM spm = (SPM) queryMessage1.get("SPM");
			ORC orc = (ORC) queryMessage1.get("ORC");
			OBR obr = (OBR) queryMessage1.get("OBR");
			SAC sac = (SAC) queryMessage1.get("SAC");
			OBX obx = (OBX) queryMessage1.get("OBX");

			MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap().get(theMessage.getResponse().getAccessioningId());
			if(containerDTO != null) {
				if(theMessage.getStatus() == Status.SUCCESS) {
					HL7HeaderSegmentDTO headerSegmentDTO = containerDTO.getHeaderSegment();
					msh1.getMsh3_SendingApplication().getHd1_NamespaceID().setValue(headerSegmentDTO.getReceivingApplication());
//					msh1.getMsh3_SendingApplication().getHd2_UniversalID().setValue();
					msh1.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getSendingFacility());
					msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getReceivingFacility());
					msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().setValue(headerSegmentDTO.getSendingApplicationName());
					msh1.getMsh5_ReceivingApplication().getHd2_UniversalID().setValue(headerSegmentDTO.getDeviceSerialNumber());
					msh1.getMsh5_ReceivingApplication().getHd3_UniversalIDType().setValue("M");
					msh1.getMsh10_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
					msh1.getMsh7_DateTimeOfMessage().getTime().setValue(f.format(new Date()));
					msh1.getMsh11_ProcessingID().getProcessingID().setValue(headerSegmentDTO.getProcessingId());
					msh1.getMsh12_VersionID().getVersionID().setValue(headerSegmentDTO.getVersionId());
					
					msa.getMsa2_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
					if(theMessage.getStatus() == Status.SUCCESS) {
						msa.getMsa1_AcknowledgmentCode().setValue("AA");
						if(theMessage.getResponse().getRspMessage().getOrderNumber() != null) {
							qak.getQak2_QueryResponseStatus().setValue("OK");
						} else if(theMessage.getResponse().getRspMessage().getOrderNumber() == null) {
							qak.getQak2_QueryResponseStatus().setValue("NF");
						}
					}				
					qpd.getQpd1_MessageQueryName().getCe1_Identifier().setValue(headerSegmentDTO.getQueryDef_Id());
					qpd.getQpd1_MessageQueryName().getCe2_Text().setValue(headerSegmentDTO.getQueryDef_desc());
					qpd.getQpd1_MessageQueryName().getCe3_NameOfCodingSystem().setValue(headerSegmentDTO.getQueryDef_EncodingSystem());
					qpd.getQpd2_QueryTag().setValue(headerSegmentDTO.getMessageControlId());
					
					spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier()
						.setValue(theMessage.getResponse().getAccessioningId());
					RSPMessageDTO rspMessageDTO = theMessage.getResponse().getRspMessage();
					SampleInfo sampleInfo = rspMessageDTO.getSampleInfo();
					ContainerInfo containerInfo = rspMessageDTO.getContainerInfo();
					MP24ConfigDetails configDetails = MP24ParamConfig.getInstance().getMp24ConfigDetails();
					if(sampleInfo.getSampleType() != null) {
						
						for(SampleTypeDTO sampleTypeDTO : configDetails.getConfigType()) {
							if(sampleTypeDTO.getName().equalsIgnoreCase(sampleInfo.getSampleType())) {
								spm.getSpm4_SpecimenType().getCwe1_Identifier().setValue(sampleTypeDTO.getId());
								spm.getSpm4_SpecimenType().getCwe2_Text().setValue(sampleTypeDTO.getValue());
								spm.getSpm4_SpecimenType().getCwe3_NameOfCodingSystem().setValue(sampleTypeDTO.getEncodingSystem());
							}
						}
					}
					
					spm.getSpm14_SpecimenDescription(0).setValue(sampleInfo.getSpecimenDescription());
					if (sampleInfo.getContainerType() != null) {
						for(SampleTypeDTO sampleTypeDTO : configDetails.getConfigType()) {
							if(sampleTypeDTO.getName().equalsIgnoreCase(sampleInfo.getContainerType())) {
								spm.getSpm27_ContainerType().getCwe1_Identifier().setValue(sampleTypeDTO.getId());
								spm.getSpm27_ContainerType().getCwe2_Text().setValue(sampleTypeDTO.getValue());
								spm.getSpm27_ContainerType().getCwe3_NameOfCodingSystem().setValue(sampleTypeDTO.getEncodingSystem());
							}
						}
					}
					
					sac.getRegistrationDateTime().getTs1_Time().setValue(f.format(new Date()));
					
					if(containerInfo.getAvailableSpecimenVolume() != null) {
						sac.getSac22_AvailableSpecimenVolume().setValue(containerInfo.getAvailableSpecimenVolume());
						sac.getSac24_VolumeUnits().getCe1_Identifier().setValue("ul");
					}
					
					orc.getOrc1_OrderControl().setValue(rspMessageDTO.getOrderControl());
					orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(theMessage.getResponse().getAccessioningId());
					if(rspMessageDTO.getOrderStatus().equalsIgnoreCase("unassigned")) {
						orc.getOrc5_OrderStatus().setValue("SC");
					}
					orc.getOrc9_DateTimeOfTransaction().getTs1_Time().setValue(f.format(new Date()));
					
					obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(rspMessageDTO.getProtocolName());
					obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(rspMessageDTO.getProtocolDescription());
					obr.getObr4_UniversalServiceIdentifier().getCe3_NameOfCodingSystem().setValue("99ROC");
					obr.getObr25_ResultStatus().setValue("S");
					
					String observationValue = rspMessageDTO.getEluateVolume();
					if(observationValue != null && observationValue.trim().length() > 1) {
						obx.getObx1_SetIDOBX().setValue("1");
						obx.getObx2_ValueType().setValue("NM");
						obx.getObx3_ObservationIdentifier().getCe1_Identifier().setValue("EluateVol");
						obx.getObx3_ObservationIdentifier().getCe2_Text().setValue("EluateVolume");
						obx.getObx3_ObservationIdentifier().getCe3_NameOfCodingSystem().setValue("99ROC");
						NM nm = new NM(queryMessage1);
						nm.setValue(observationValue);
						Varies varies = obx.getObservationValue(0);
						varies.setData(nm);
						obx.getObx6_Units().getCe1_Identifier().setValue("ul");
					}
				} else {
					msa.getMsa1_AcknowledgmentCode().setValue("AE");
					qak.getQak2_QueryResponseStatus().setValue("AE");
				}
				
				
			}
			return queryMessage1;
		} catch (IOException e) {
			throw new HL7Exception(e);
		}
	}
	
	public void convertSSUToMsgPayload(SSU_U03 statusMessage) throws HL7Exception {
		logger.info("Processing SSU_U03 message in thread:::::" + Thread.currentThread().getName());
		MSH msh1 = (MSH) statusMessage.get("MSH");
		EQU equ = (EQU) statusMessage.get("EQU");
		statusMessage.getSPECIMEN_CONTAINER();
		SSU_U03_SPECIMEN_CONTAINER sacContainer = statusMessage.getSPECIMEN_CONTAINER();
		SAC sac = sacContainer.getSAC();
		SPM spm = sacContainer.getSPECIMEN().getSPM();
		List<OBX> sacObxList = sacContainer.getOBXAll();
		List<OBX> spmObxList = sacContainer.getSPECIMEN().getOBXAll();
		MessagePayload messagePayload = new MessagePayload();
		messagePayload.setDeviceSerialNumber(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue());
		messagePayload.setDateTimeMessageGenerated(msh1.getMsh7_DateTimeOfMessage().getTs1_Time().getValue());
		messagePayload.setMessageType("StatusUpdate");
		
		messagePayload.setSendingApplicationName(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
		messagePayload.setReceivingApplication(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
		StatusUpdate statusUpdateDTO = new StatusUpdate();
		statusUpdateDTO.setEquipmentState(equ.getEqu3_EquipmentState().getCe1_Identifier().getValue());
		statusUpdateDTO.setEventDate(equ.getEqu2_EventDateTime().getTs1_Time().getValue());
		ContainerInfo containerInfo = new ContainerInfo();
		containerInfo.setContainerPosition(sac.getSac3_ContainerIdentifier().getEi1_EntityIdentifier().getValue());
		if(sac.getSac8_ContainerStatus().getCe1_Identifier().getValue().equals("R")) {
			containerInfo.setContainerStatus("Process Completed");	
		} else if(sac.getSac8_ContainerStatus().getCe1_Identifier().getValue().equals("O")){
			containerInfo.setContainerStatus("In Process");
		}
		
		containerInfo.setCarrierType(sac.getSac9_CarrierType().getCe1_Identifier().getValue());
		containerInfo.setCarrierBarcode(sac.getSac10_CarrierIdentifier().getEi1_EntityIdentifier().getValue());
		containerInfo.setCarrierPosition(sac.getSac11_PositionInCarrier().getValue1().getValue());
		List<String> sr2mlReagentTube = new ArrayList<>();
		List<String> sr25mlReagentTube = new ArrayList<>();
		
		for(OBX obx : sacObxList) {
			String observationValue = obx.getObx5_ObservationValue(0).encode();
			if(obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("P")) {
				statusUpdateDTO.setProtocolName(observationValue);
			} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("PV")) {
				statusUpdateDTO.setProtocolVersion(observationValue);
			} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("RES")) {
				statusUpdateDTO.setRunResult(observationValue);
			} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("RuntimeRange")) {
				
				String startEndTime = obx.getObx5_ObservationValue(0).getData().encode();
				statusUpdateDTO.setRunStartTime(startEndTime.substring(0, obx.getObx5_ObservationValue(0).getData().encode().indexOf("^")));
				statusUpdateDTO.setRunEndTime(startEndTime.substring(obx.getObx5_ObservationValue(0).getData().encode().indexOf("^") + 1));
			}
		}
		
		for(OBX obx : spmObxList) {
			String observationValue = obx.getObx5_ObservationValue(0).encode();
			if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("OrderName")) {
				statusUpdateDTO.setOrderName(observationValue);
			} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("OrderResult")) {
				statusUpdateDTO.setOrderResult(observationValue);
			} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("IC")) {
				statusUpdateDTO.setInternalControls(observationValue);
			} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("SCProcessingCartridge")) {
				statusUpdateDTO.setProcessingCartridge(observationValue);
			} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("SCTipRack")) {
				statusUpdateDTO.setTipRack(observationValue);
			} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("MRReagentCassette")) {
				statusUpdateDTO.setReagentCassette(observationValue.replaceAll("·", " "));
			} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("SR2mlReagentTube")) {
				sr2mlReagentTube.add(observationValue);
			} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("SR25mlReagentTube")) {
				sr25mlReagentTube.add(observationValue);
			}    
		}
		statusUpdateDTO.setReagent2mlTube(sr2mlReagentTube);
		statusUpdateDTO.setReagent25mlTube(sr25mlReagentTube);

		SampleInfo sampleInfo = new SampleInfo();
		String sampleOutput = spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier().getValue();
		String [] splitSampleOutput = sampleOutput.split("_");
		sampleInfo.setSampleOutputId(splitSampleOutput[0]);
		if(splitSampleOutput.length > 1) {
			sampleInfo.setSampleOutputPosition(splitSampleOutput[1]);
		}
		String sampleType = spm.getSpm4_SpecimenType().getCwe2_Text().encode();
		sampleInfo.setSampleType(sampleType.replaceAll("·", " "));
		
		if(spm.getSpm11_SpecimenRole(0).getCwe1_Identifier().getValue().equalsIgnoreCase("Q")) {
			sampleInfo.setSpecimenRole("Control");
		} else {
			sampleInfo.setSpecimenRole("Patient");
		}
		messagePayload.setAccessioningId(spm.getSpm3_SpecimenParentIDs(0).encode());
		statusUpdateDTO.setContainerInfo(containerInfo);
		statusUpdateDTO.setSampleInfo(sampleInfo);
		messagePayload.setStatusUpdate(statusUpdateDTO);
		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.info("SSU_U03 message converted to JSON :::::" + mapper.writeValueAsString(messagePayload));
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<MessagePayload> entity = new HttpEntity<>(messagePayload, headers);
			String url = RouterUrlCofig.getInstance().getUrl(AppUrlEntry.SEND_REQUEST);
			restTemplate.exchange(url,HttpMethod.POST, entity, Void.class);
		} catch (JsonGenerationException e) {
			logger.error("JsonGenerationException:::::" + e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException:::::" + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException:::::" + e.getMessage());
		}
		
	}
}