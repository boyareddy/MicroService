package com.roche.swam.labsimulator.mpx.bl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.lis.bl.SampleIdGenerator;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.SSU_U03;
import ca.uhn.hl7v2.model.v251.segment.NTE;
import ca.uhn.hl7v2.model.v251.segment.OBX;
import ca.uhn.hl7v2.parser.Parser;

public class ResultMessageBuilder {

    private HapiContext context;

    private SimpleDateFormat dateFormatter;
    private String flag;
    private String result;
    private String internalControls;
    private String processingCartridge;
    private String tipRack;
    private String obxValues;
    private String obxName;
   

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultMessageBuilder.class);
    public ResultMessageBuilder() {
    	
        this.context = new DefaultHapiContext();
        this.dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    }

   
    
    
    public Message build(Sample sample, Run run, int ssuCount, String deviceRunID, String runResult) throws IOException, HL7Exception {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/SSU_U03.txt");
        String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
        Parser p = this.context.getPipeParser();
        SSU_U03 resultMessage = (SSU_U03) p.parse(msg);
        String universalIdType="M";
        String messageControlId = UUID.randomUUID().toString().substring(0, 20);
        String obx5ObservationValue="1.0";
        String obxPassedValue=EnumSampleStatus.Inprogress.toString();
        OBX obx=null;
        resultMessage.getMSH().getMsh10_MessageControlID().parse(messageControlId);
        resultMessage.getMSH().getMsh3_SendingApplication().getHd3_UniversalIDType().parse(universalIdType);
        resultMessage.getMSH().getMsh3_SendingApplication().getHd2_UniversalID().parse(sample.getInstrumentId());
        resultMessage.getMSH().getSendingFacility().getNamespaceID().setValue(sample.getSendingFacilityName());
        resultMessage.getMSH().getReceivingApplication().getNamespaceID().setValue(sample.getReceivingApplicationName());
        resultMessage.getMSH().getSendingApplication().getNamespaceID().setValue(sample.getSendingApplicationName());
        resultMessage.getMSH().getReceivingFacility().getNamespaceID().setValue(sample.getReceivingFacilityName());
        resultMessage.getMSH().getDateTimeOfMessage().parse(dateFormatter.format(Calendar.getInstance().getTime()));
        resultMessage.getEQU().getEqu1_EquipmentInstanceIdentifier().getEi1_EntityIdentifier().parse(sample.getInstrumentId());
        resultMessage.getEQU().getEqu2_EventDateTime().parse(dateFormatter.format(run.getEndTime().getTime()));
        resultMessage.getSPECIMEN_CONTAINER().getSAC().getContainerIdentifier().parse(sample.getOutputPosition());
        resultMessage.getMSH().getProcessingID().getPt1_ProcessingID().setValue(sample.getResultProcessingId());
        if (!"SSU".equalsIgnoreCase(sample.getResultMsgType())) {
        	 resultMessage.getMSH().getMessageType().getMsg1_MessageCode().setValue(sample.getResultMsgType());
		}
		if (!"U03".equalsIgnoreCase(sample.getResultMsgEvent())) {
			 resultMessage.getMSH().getMessageType().getMsg2_TriggerEvent().setValue(sample.getResultMsgEvent());
		}
        if(StringUtils.isBlank(sample.getPlateType())) {
        resultMessage.getSPECIMEN_CONTAINER().getSAC().getSac9_CarrierType().getCe1_Identifier().setValue(getContainerType(MainApp.getDeviceName()));
        }
        else {
        	resultMessage.getSPECIMEN_CONTAINER().getSAC().getSac9_CarrierType().getCe1_Identifier().setValue(getContainerType(MainApp.getDeviceName())+"_"+sample.getPlateType());
        }
			
       	 resultMessage.getSPECIMEN_CONTAINER().getSAC().getPositionInCarrier().parse(sample.getOutputPosition());
	        resultMessage.getSPECIMEN_CONTAINER().getSAC().getCarrierIdentifier().parse(sample.getOutputContainerId());	
        
        
     NTE nte= (NTE) resultMessage.getSPECIMEN_CONTAINER().get("NTE");
     
     nte.getNte3_Comment(0).setValue(sample.getSampleComments());
        
     int count=0;
     int ssuCnt = Integer.parseInt(MainApp.getFilepath("NoOfSSU"));
     int obxSize=resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getOBXReps();
         obx = resultMessage.getSPECIMEN_CONTAINER().getOBX(0);
        obx.getObx5_ObservationValue()[0].parse(sample.getProtocolType());
        obx = resultMessage.getSPECIMEN_CONTAINER().getOBX(1);
        obx.getObx5_ObservationValue()[0].parse(obx5ObservationValue);
        obx = resultMessage.getSPECIMEN_CONTAINER().getOBX(2);
        result = sample.getResult();
        
        if(runResult!=null && ssuCount==ssuCnt) {
        obx.getObx5_ObservationValue(0).parse(runResult);
        }
        else {
        	 obx.getObx5_ObservationValue(0).parse(obxPassedValue);	
        }
        obx = resultMessage.getSPECIMEN_CONTAINER().getOBX(3);
        if(ssuCount==ssuCnt) {
        obx.getObx5_ObservationValue()[0].parse(dateFormatter.format(run.getStartTime().getTime()) + "^" + dateFormatter.format(run.getEndTime().getTime()));
        }else {
        	 obx.getObx5_ObservationValue(0).parse(dateFormatter.format(run.getStartTime().getTime()));
        }
        obx = resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getOBX();
        flag=sample.getFlag();
       if(StringUtils.isNotBlank(sample.getRunId())) {
    	   obx.getObx5_ObservationValue(0).parse(sample.getRunId());
       }else {
    	   obx.getObx5_ObservationValue(0).parse(deviceRunID);
       }
      
        if(this.flag!=null && ssuCount==ssuCnt) {
        obx.getObx8_AbnormalFlags(0).setValue(flag);
        count+=1;
        }else {
        	obx.getObx8_AbnormalFlags(0).setValue("");
        	  count+=1;
        }
        obx = resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getOBX(1);
      
        if(this.result!=null  && ssuCount==ssuCnt) {
        obx.getObx5_ObservationValue(0).parse(result);
        count+=1;
        }else {
        	 obx.getObx5_ObservationValue(0).parse(obxPassedValue);
        	 count+=1;
        }
        if(result.equalsIgnoreCase(ContainerType.FAILED) && ssuCount==ssuCnt) {
			context.getParserConfiguration().addForcedEncode("SPM-27");
			context.getParserConfiguration().addForcedEncode("SAC-14");
        	for(int i = 2;i<=obxSize;i++) {
        		resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getOBX(i).clear();
        	}
        		resultMessage.getSPECIMEN_CONTAINER().getSAC().getContainerIdentifier().parse("WASTE");
        		resultMessage.getSPECIMEN_CONTAINER().getSAC().getSac8_ContainerStatus().getCe1_Identifier().setValue("0");
        		resultMessage.getSPECIMEN_CONTAINER().getSAC().getSac8_ContainerStatus().getCe3_NameOfCodingSystem().setValue("HL0370");
        		resultMessage.getSPECIMEN_CONTAINER().getSAC().getSac9_CarrierType().clear();
        		resultMessage.getSPECIMEN_CONTAINER().getSAC().getSac10_CarrierIdentifier().clear();
        		resultMessage.getSPECIMEN_CONTAINER().getSAC().getSac11_PositionInCarrier().clear();
        		resultMessage.getSPECIMEN_CONTAINER().getSAC().getVolumeUnits().parse("");
        		resultMessage.getSPECIMEN_CONTAINER().getSAC().getSac21_ContainerVolume().setValue("");
        		resultMessage.getSPECIMEN_CONTAINER().getSAC().getSac22_AvailableSpecimenVolume().setValue("");
        	
        }else {
                 
        
        
        int consumablesSize=sample.getConsumables().size();
        
			for (int i = 0; i < consumablesSize; i++) {

				if (ssuCount == ssuCnt) {
					obx = resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getOBX(count + i);
					obxValues = sample.getConsumables().get(i).getValue();
					obxName = sample.getConsumables().get(i).getConsumableName();
					if (this.obxValues != null) {
						obx.getObx5_ObservationValue(0).parse(obxValues);
						obx.getObservationIdentifier().getCe1_Identifier().parse(obxName);
					} else {
						obx.clear();
					}

				} else {

					obx = resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getOBX(count + i);
					obx.clear();
				}
			}
        
        for(int i=count+consumablesSize;i<25;i++) {
     	   obx = resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getOBX(i);
     if(obx!=null) {
     	obx.clear();
     }
     }
     }
		if (sample.getResult().equalsIgnoreCase(ContainerType.FAILED) && ssuCount==ssuCnt) {
			resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenParentIDs(0)
					.parse(sample.getSampleId());
			resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenID().clear();
		} else {
			if (StringUtils.isNotEmpty(sample.getOutputContainerId())
					&& StringUtils.isNotEmpty(sample.getOutputPosition())) {
				resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenID()
						.parse(sample.getOutputContainerId() + "_" + sample.getOutputPosition());
			} else if (StringUtils.isEmpty(sample.getOutputContainerId())
					&& StringUtils.isNotEmpty(sample.getOutputPosition())) {
				resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenID()
						.parse(sample.getOutputPosition());
			} else if (StringUtils.isNotEmpty(sample.getOutputContainerId())
					&& StringUtils.isEmpty(sample.getOutputPosition())) {
				resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenID()
						.parse(sample.getOutputContainerId());
			} else {
				resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenID().clear();
			}
			
			if (StringUtils.isNotBlank(sample.getSampleId())) {
				resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenParentIDs(0)
						.parse(sample.getSampleId());
			}
		}
    
        if(StringUtils.isNotBlank(sample.getSampleType())) {
        resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenType().parse(sample.getSampleType());
        }
        if(StringUtils.isNotBlank(sample.getSampleDescription())) {
        resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenDescription()[0].parse(sample.getSampleDescription());
        }
        if(StringUtils.isNotBlank(sample.getSampleId())&&!sample.getResult().equalsIgnoreCase(ContainerType.FAILED)) {
        resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenParentIDs(0).parse(sample.getSampleId());
        }
        return resultMessage;
    }
   
    private String getContainerType(String deviceName) {
    		return MainApp.getFilepath("outputContainerType");
    	
    }




	public Message build(String inputConId, String inputPos) throws IOException, HL7Exception {
		// TODO Auto-generated method stub
		
		 InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/SSU_U03.txt");
	        String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
	        Parser p = this.context.getPipeParser();
	        SSU_U03 resultMessage = (SSU_U03) p.parse(msg);
	        String spm3Seg="";
	        if("true".equalsIgnoreCase(inputConId) && 	"true".equalsIgnoreCase(inputPos) ) {
	    	resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenParentIDs(0).clear();
	        }
	        else if("true".equalsIgnoreCase(inputConId) && 	"false".equalsIgnoreCase(inputPos) ) {
	        	resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenParentIDs(0).parse("A1");
	        } else if("false".equalsIgnoreCase(inputConId) && 	"true".equalsIgnoreCase(inputPos) ) {
	        	resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenParentIDs(0).parse(SampleIdGenerator.getNext());
	        }else {
	        	resultMessage.getSPECIMEN_CONTAINER().getSPECIMEN().getSPM().getSpecimenParentIDs(0).parse(SampleIdGenerator.getNext()+"_A1");
	        }
	        
		return resultMessage;
	}

    
}
