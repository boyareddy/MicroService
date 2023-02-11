package com.roche.connect.dpcr.sim.bl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.connect.dpcr.MainApp;
import com.roche.connect.dpcr.lis.bl.SampleIdGenerator;
import com.roche.connect.dpcr.util.PartitionEngineBean;
import com.roche.connect.dpcr.util.ResultBean;
import com.roche.connect.dpcr.util.SampleBean;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.NTE;
import ca.uhn.hl7v2.model.v26.segment.OBR;
import ca.uhn.hl7v2.model.v26.segment.OBX;
import ca.uhn.hl7v2.model.v26.segment.ORC;
import ca.uhn.hl7v2.model.v26.segment.SPM;
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

   
    
    
	public Message build(ResultBean resultBean) throws IOException, HL7Exception {
		InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/ORU_R01.txt");
		String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		this.context.getParserConfiguration().setDefaultObx2Type("ST");
		Parser p = this.context.getPipeParser();
		ORU_R01 oru = (ORU_R01) p.parse(msg);
		Date date = new Date();
		String currentTime = dateFormatter.format(date);
		String messageControlId = setMSH(resultBean, oru, currentTime);
		int count = 0;
		for (SampleBean sampleBean : resultBean.getSample()) {

			ORU_R01_ORDER_OBSERVATION orderObv = oru.getPATIENT_RESULT(0).getORDER_OBSERVATION(count);
			setORC(resultBean, currentTime, sampleBean, orderObv);
			setOBR(sampleBean, orderObv);
			setOBX1(sampleBean, orderObv);
			setOBX2(sampleBean, orderObv);
			setSPM(sampleBean, orderObv);
			count++;
		}
		try (FileWriter hl7Writer = new FileWriter(
				MainApp.getClassPath() + "/" + MainApp.getValueFromPropFile("MessageTxt"), true)) {
			hl7Writer.write("Request Message from " + MainApp.getValueFromPropFile("DeviceSerialNumber")
					+ " with Message ControlId " + messageControlId + "\n");
			hl7Writer.write("Message ControlId: " + messageControlId + "\n");
			hl7Writer.write("Date : " + date + "\n");
			hl7Writer.write("Message :\n");
			hl7Writer.write(oru.toString() + "\n");
		}

		return oru;
	}


	private void setSPM(SampleBean sampleBean, ORU_R01_ORDER_OBSERVATION orderObv) throws DataTypeException {
		try {
		int pCount = 0;
		for (PartitionEngineBean peBean : sampleBean.getPartitionEngine()) {
			SPM spm = orderObv.getSPECIMEN(pCount).getSPM();
			spm.getSetIDSPM().setValue(String.valueOf(pCount + 1));
			spm.getSpecimenID().getPlacerAssignedIdentifier().getEntityIdentifier().setValue(peBean.getPlateId());
			spm.getSpecimenSourceSite().getIdentifier().setValue(peBean.getSerialNumber());
			spm.getSpecimenSourceSiteModifier(0).getIdentifier().setValue(peBean.getFluidId());
			spm.getSpecimenCollectionDateTime().getRangeStartDateTime().setValue(peBean.getDateandTime());
			pCount++;
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	private void setOBX2(SampleBean sampleBean, ORU_R01_ORDER_OBSERVATION orderObv)
			throws HL7Exception {
		try {
		OBX obx = orderObv.getOBSERVATION(1).getOBX();
		obx.getSetIDOBX().setValue(String.valueOf(2));
		obx.getObservationIdentifier().getIdentifier().setValue(sampleBean.getAssay().get(0).getAssayType());
		obx.getObservationValue(0).parse(sampleBean.getAssay().get(0).getQualitativeValue());
		obx.getObservationResultStatus().setValue(sampleBean.getAssay().get(0).getQualitativeResult());
			if (StringUtils.isNotBlank(sampleBean.getFlag())) {
				String[] flags = sampleBean.getFlag().split(",");
				for (int i = 0; i < flags.length; i++) {
					NTE nte = orderObv.getOBSERVATION(1).getNTE(i);
					nte.getSetIDNTE().setValue(String.valueOf(i + 1));
					nte.getSourceOfComment().setValue("L");
					nte.getComment(0).setValue(flags[i]);
				}
			}else {
				orderObv.getOBSERVATION(1).getNTE().clear();
			}
			obx.getObx2_ValueType().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setOBX1(SampleBean sampleBean, ORU_R01_ORDER_OBSERVATION orderObv)
			throws HL7Exception {
		try {
			OBX obx = orderObv.getOBSERVATION(0).getOBX();
			obx.getSetIDOBX().setValue(String.valueOf(1));
			obx.getObservationIdentifier().getIdentifier().setValue(sampleBean.getAssay().get(0).getAssayType());
			obx.getObservationValue(0).parse(sampleBean.getAssay().get(0).getQuantitativeValue());
			obx.getUnits().getIdentifier().setValue("ml");
			obx.getObservationResultStatus().setValue(sampleBean.getAssay().get(0).getQuantitativeResult());
			if (StringUtils.isNotBlank(sampleBean.getFlag())) {
				String[] flags = sampleBean.getFlag().split(",");
				for (int i = 0; i < flags.length; i++) {
					NTE nte = orderObv.getOBSERVATION(0).getNTE(i);
					nte.getSetIDNTE().setValue(String.valueOf(i + 1));
					nte.getSourceOfComment().setValue("L");
					nte.getComment(0).setValue(flags[i]);
				}
			}else {
				orderObv.getOBSERVATION(0).getNTE().clear();;
			}
			obx.getObx2_ValueType().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setOBR(SampleBean sampleBean, ORU_R01_ORDER_OBSERVATION orderObv) throws DataTypeException {
		try {
		OBR obr = orderObv.getOBR();
		obr.getSetIDOBR().setValue(String.valueOf(1));
		obr.getPlacerOrderNumber().getEntityIdentifier().setValue(sampleBean.getAccessioningId());
		obr.getUniversalServiceIdentifier().getIdentifier().setValue(sampleBean.getAssay().get(0).getAssayType());
		obr.getUniversalServiceIdentifier().getText().setValue(sampleBean.getAssay().get(0).getAnalysisPackageName());
		obr.getUniversalServiceIdentifier().getNameOfCodingSystem()
				.setValue(sampleBean.getAssay().get(0).getAssayVersion());
		obr.getUniversalServiceIdentifier().getAlternateIdentifier().setValue(sampleBean.getAssay().get(0).getKit());
	}catch(Exception e) {
		e.printStackTrace();
	}
	}

	private void setORC(ResultBean resultBean, String currentTime, SampleBean sampleBean,
			ORU_R01_ORDER_OBSERVATION orderObv) throws DataTypeException {
		try {
		ORC orc = orderObv.getORC();
		orc.getOrderControl().setValue("RE");
		orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(sampleBean.getAccessioningId());
		orc.getPlacerGroupNumber().getEi1_EntityIdentifier()
				.setValue(resultBean.getRunId());
		orc.getDateTimeOfTransaction().setValue(currentTime);
		orc.getEnteredBy(0).getXcn1_IDNumber().setValue(sampleBean.getOperatorName());
		orc.getVerifiedBy(0).getXcn1_IDNumber().setValue(sampleBean.getOperatorName());
		orc.getOrderControlCodeReason().getIdentifier().setValue(resultBean.getRunNotes());
		orc.getActionBy(0).getXcn1_IDNumber().setValue(sampleBean.getOperatorName());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private String setMSH(ResultBean resultBean, ORU_R01 oru, String currentTime) throws DataTypeException {
		String messageControlId="";
		try {
		MSH msh = oru.getMSH();
		 messageControlId = UUID.randomUUID().toString().substring(0, 20);
		msh.getMsh7_DateTimeOfMessage().setValue(currentTime);
		msh.getSendingApplication().getHd1_NamespaceID().setValue(MainApp.getValueFromPropFile("DeviceName"));
		msh.getMessageControlID().setValue(messageControlId);
		msh.getSendingApplication().getHd2_UniversalID().clear();
		msh.getSendingFacility().getHd1_NamespaceID().setValue(MainApp.getValueFromPropFile("DeviceSerialNumber"));
		msh.getReceivingApplication().getHd1_NamespaceID().setValue(resultBean.getRecevingApplication());
		msh.getReceivingFacility().getHd1_NamespaceID().setValue("");
		if (StringUtils.isNotBlank(resultBean.getHl7Version())
				&& (!"2.6".equalsIgnoreCase(resultBean.getHl7Version()))) {

			msh.getVersionID().getVid1_VersionID().setValue(resultBean.getHl7Version());
		}}catch(Exception e) {
			e.printStackTrace();
		}
		return messageControlId;
	}
    

    
}
