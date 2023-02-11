package com.roche.swam.labsimulator.mpx.bl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.lis.bl.SampleIdGenerator;
import com.roche.swam.labsimulator.util.LpConstants;
import com.roche.swam.labsimulator.util.Mp96RunData;
import com.roche.swam.labsimulator.util.Mp96SampleData;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.OUL_R21;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.OBR;
import ca.uhn.hl7v2.model.v24.segment.OBX;
import ca.uhn.hl7v2.model.v24.segment.ORC;
import ca.uhn.hl7v2.parser.Parser;

public class ResultMessageBuilder {

    private HapiContext context;

    private SimpleDateFormat dateFormatter;
    private static final char CARRIAGE_RETURN = 13;
    private SampleIdGenerator sampleIdGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultMessageBuilder.class);
    public ResultMessageBuilder() {
    	
        this.context = new DefaultHapiContext();
        this.dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    }

   
    
    
	public Message build(Mp96RunData mp96RunData) throws IOException, HL7Exception {
		InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/OUL_R21.txt");
		String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		 this.context.getParserConfiguration().setDefaultObx2Type("ST");
		Parser p = this.context.getPipeParser();
		OUL_R21 resultMessage = (OUL_R21) p.parse(msg);
		String deviceRunId = this.sampleIdGenerator.getNext();
		String outputContainerID = this.sampleIdGenerator.getNext();
		MSH msh = resultMessage.getMSH();
		msh.getMsh10_MessageControlID().setValue(mp96RunData.getDeviceId());
		String currentTime = dateFormatter.format(new Date()).toString();
		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(new Date()); // sets calendar time/date
		cal.add(Calendar.HOUR_OF_DAY, -2); // adds one hour
		String runStartDate = dateFormatter.format(cal.getTime()).toString();
		mp96RunData.getSamples().size();
		List<String> samplePosition = this.sampleIdGenerator.getSamplePosition();
		List<Mp96SampleData> mp96SampleDataList = mp96RunData.getSamples();
		boolean sampleResult = true;
		
		for(Mp96SampleData mp96SampleData:mp96SampleDataList) {
			if(StringUtils.isBlank(mp96SampleData.getSampleResults()) || (StringUtils.isNotBlank(mp96SampleData.getSampleResults()) && "P".equalsIgnoreCase(mp96SampleData.getSampleResults().trim()))) {
				sampleResult=false;
				break;
			}
			
		}
		msh.getMsh7_DateTimeOfMessage().getTs1_TimeOfAnEvent().setValue(currentTime);
		if(StringUtils.isNotBlank(mp96RunData.getResultMessageType()) && !"OML".equalsIgnoreCase(mp96RunData.getResultMessageType())) {
		msh.getMsh9_MessageType().getMsg1_MessageType().setValue(mp96RunData.getResultMessageType());
		}
		if(StringUtils.isNotBlank(mp96RunData.getResultMessageEvent()) && !"R21".equalsIgnoreCase(mp96RunData.getResultMessageEvent())) {
		msh.getMsh9_MessageType().getMsg2_TriggerEvent().setValue(mp96RunData.getResultMessageEvent());
		}
		if(StringUtils.isNotBlank(mp96RunData.getResultMessageEvent( )) && (!"P".equalsIgnoreCase(mp96RunData.getProcessingId()))) {
		msh.getMsh11_ProcessingID().getPt1_ProcessingID().setValue(mp96RunData.getProcessingId());
		}
		ORC orc = resultMessage.getORDER_OBSERVATION().getORC();

		OBR obr = resultMessage.getORDER_OBSERVATION().getOBR();

		obr.getObr1_SetIDOBR().setValue("3");

		OBX obx = resultMessage.getORDER_OBSERVATION().getOBSERVATION().getOBX();

		obx.getObx1_SetIDOBX().setValue("3");

		NTE nte = resultMessage.getORDER_OBSERVATION().getOBSERVATION().getNTE();

		nte.getNte1_SetIDNTE().setValue("3");

		StringBuilder sb = new StringBuilder();

		sb.append(convertHl7toString(msh.encode())).append(CARRIAGE_RETURN);
		int counter = 0;
		for (Mp96SampleData mp96SampleData : mp96SampleDataList) {

			orc.getOrc1_OrderControl().setValue(String.valueOf(counter + 1));
			obr.getObr1_SetIDOBR().setValue(String.valueOf(counter + 1));
			nte.getNte1_SetIDNTE().setValue(String.valueOf(counter + 1));
			obx.getObx1_SetIDOBX().setValue(String.valueOf(counter + 1));

			orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(mp96RunData.getRunId());
			orc.getOrc2_PlacerOrderNumber().getEi2_NamespaceID().setValue(deviceRunId);
			orc.getOrc3_FillerOrderNumber().getEi1_EntityIdentifier().setValue(mp96RunData.getRunComments());
			orc.getOrc10_EnteredBy(0).getXcn1_IDNumber().setValue(mp96RunData.getOperatorName());
			orc.getOrc9_DateTimeOfTransaction().getTs1_TimeOfAnEvent().setValue(currentTime);
			obr.getObr2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(mp96SampleData.getAccessioningId());
			if (SimulatorVariables.valueOf("FALSE").toString()
					.equalsIgnoreCase(mp96SampleData.getOutputContainerIdEmpty())) {
				if (StringUtils.isNotBlank(mp96SampleData.getOutputContainerId())) {
					obr.getObr3_FillerOrderNumber().getEi1_EntityIdentifier()
							.setValue(mp96SampleData.getOutputContainerId());

				} else {
					obr.getObr3_FillerOrderNumber().getEi1_EntityIdentifier().setValue(outputContainerID);
					mp96SampleData.setOutputContainerId(outputContainerID);
				}
			} else {
				obr.getObr3_FillerOrderNumber().getEi1_EntityIdentifier().clear();
			}
			obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(mp96SampleData.getReagentVersion());
			obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(mp96SampleData.getReagentName());
			obr.getObr7_ObservationDateTime().getTs1_TimeOfAnEvent().setValue(runStartDate);
			obr.getObr8_ObservationEndDateTime().getTs1_TimeOfAnEvent().setValue(currentTime);
			obr.getObr13_RelevantClinicalInfo().setValue(MainApp.getFilepath("plateType"));
			if(StringUtils.isNotEmpty(mp96SampleData.getProtocolName()) && StringUtils.isNotEmpty(mp96SampleData.getProtocolVersion())) {
			obr.getObr15_SpecimenSource().getSps1_SpecimenSourceNameOrCode().getCe1_Identifier()
					.setValue(mp96SampleData.getProtocolName() + " " + mp96SampleData.getProtocolVersion());
			}
			else {
				obr.getObr15_SpecimenSource().clear();
			}
			obr.getObr18_PlacerField1().setValue(mp96SampleData.getSampleVolume());
			obr.getObr19_PlacerField2().setValue(mp96SampleData.getEluateVolume());
			if (SimulatorVariables.valueOf("FALSE").toString()
					.equalsIgnoreCase(mp96SampleData.getOutputContainerPositionEmpty())) {
				if (StringUtils.isNotBlank(mp96SampleData.getOutputContainerposition())) {
					obx.getObx3_ObservationIdentifier().getCe1_Identifier()
							.setValue(mp96SampleData.getOutputContainerposition());
				} else {
					obx.getObx3_ObservationIdentifier().getCe1_Identifier().setValue(samplePosition.get(counter));
					mp96SampleData.setOutputContainerposition(samplePosition.get(counter));
				}
			} else {
				obx.getObx3_ObservationIdentifier().getCe1_Identifier().clear();
			}
			if (StringUtils.isNotBlank(mp96SampleData.getFlag())) {
				obx.getObx4_ObservationSubId().setValue(mp96SampleData.getFlag());
			}
			else {
				obx.getObx4_ObservationSubId().clear();
			}

			if (StringUtils.isNotBlank(mp96SampleData.getSampleResults())) {

				obx.getObx11_ObservationResultStatus().setValue(mp96SampleData.getSampleResults());
			} else {

				obx.getObx11_ObservationResultStatus().setValue(EnumSampleStatus.P.toString());
				mp96SampleData.setSampleResults(EnumSampleStatus.P.toString());
			}
			if (sampleResult) {
				obx.getObx13_UserDefinedAccessChecks().setValue(LpConstants.SAMPLERESULTS_FAILED);
			} else {
				obx.getObx13_UserDefinedAccessChecks().setValue(LpConstants.SAMPLERESULTS);
			}

			sb.append(convertHl7toString(orc.encode())).append(CARRIAGE_RETURN).append(convertHl7toString(obr.encode()))
					.append(CARRIAGE_RETURN).append(convertHl7toString(obx.encode())).append(CARRIAGE_RETURN);
			if (StringUtils.isNotBlank(mp96SampleData.getSampleComments())) {
				nte.getNte3_Comment(0).setValue(mp96SampleData.getSampleComments());
				sb.append(convertHl7toString(nte.encode())).append(CARRIAGE_RETURN);
			}

			counter++;
		}

		String oulmsgString = convertHl7toString(sb.toString());

		return (Message) p.parse(oulmsgString);
	}
   
    
    
    private String convertHl7toString(String message) throws IOException {
    	 InputStream messageResource = new ByteArrayInputStream(message.getBytes());
    	 
	     String msg = CharStreams.toString(new InputStreamReader(messageResource, Charsets.UTF_8)).replace('\n', '\r');
		return msg;
    	
    	
    }

    
}
