package com.roche.swam.labsimulator.mpx.bl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.util.LP24JsonPropertyReader;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v251.message.QBP_Q11;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.QPD;
import ca.uhn.hl7v2.model.v251.segment.RCP;

public class QueryMessageBuilder {

	
	private static final Logger log = LoggerFactory.getLogger(QueryMessageBuilder.class);
	private HapiContext context;
	private SimpleDateFormat dateFormatter;

	public QueryMessageBuilder() {
		this.setContext(new DefaultHapiContext());
		 this.dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
	}

	

	public Message build(Sample loadedSample) throws IOException, HL7Exception, ParseException {

		
		log.info("inside QBP build method with sample :" +loadedSample.toString());
		QBP_Q11 queryBuilder = new QBP_Q11();
		Sample queryBuilderBean = new Sample();
		String universalTypeId = "M";
		String characterSet = "UNICODE UTF-8";
		String ce3CodingSystem = "IHE_LABTF";
		String ce2Text = "Work Order Step";
		String queryPriority = "I";
		String ce1Identifier = "R";
		
		
		
			queryBuilderBean = getLpxJson(loadedSample);

		
		
		queryBuilderBean.setSampleId(loadedSample.getSampleId());
		queryBuilder.initQuickstart("QBP", "WOS", "P");
		MSH mshSegment = queryBuilder.getMSH();
		QPD qpdSegment = queryBuilder.getQPD();
		RCP rcpSegment = queryBuilder.getRCP();
		
		String messageControlId = UUID.randomUUID().toString().substring(0, 20);
		System.out.println("Message control ID :"+messageControlId);
		
		mshSegment.getMsh3_SendingApplication().getHd2_UniversalID().parse(loadedSample.getInstrumentId());
		
		
		mshSegment.getMsh3_SendingApplication().getHd3_UniversalIDType().parse(universalTypeId);
		
		mshSegment.getDateTimeOfMessage().parse(dateFormatter.format(Calendar.getInstance().getTime()));
		mshSegment.getSendingFacility().getNamespaceID().setValue(queryBuilderBean.getSendingFacilityName());
		mshSegment.getSendingApplication().getNamespaceID().setValue(queryBuilderBean.getSendingApplicationName());
		mshSegment.getReceivingApplication().getNamespaceID().setValue(queryBuilderBean.getReceivingApplicationName());
		mshSegment.getReceivingFacility().getNamespaceID().setValue(queryBuilderBean.getReceivingFacilityName());
		mshSegment.getMessageControlID().setValue(messageControlId);
		mshSegment.getProcessingID().getPt1_ProcessingID().setValue(queryBuilderBean.getQueryProcessingId());
		if (!"QBP".equalsIgnoreCase(loadedSample.getQueryMsgType())) {
			mshSegment.getMessageType().getMsg1_MessageCode().setValue(loadedSample.getQueryMsgType());
		}
		if (!"WOS".equalsIgnoreCase(loadedSample.getQueryMsgEvent())) {
			mshSegment.getMessageType().getMsg2_TriggerEvent().setValue(loadedSample.getQueryMsgEvent());
		}
		mshSegment.getVersionID().getVid1_VersionID().setValue(queryBuilderBean.getVersionId());
		mshSegment.getCharacterSet(0).setValue(characterSet);
		log.info("QBP build after msh segment");
		qpdSegment.getQpd1_MessageQueryName().getCe1_Identifier().setValue(queryBuilderBean.getMessageQueryName());
		qpdSegment.getQpd1_MessageQueryName().getCe3_NameOfCodingSystem().setValue(ce3CodingSystem);
		qpdSegment.getQpd1_MessageQueryName().getCe2_Text().setValue(ce2Text);
		qpdSegment.getQpd2_QueryTag().setValue(messageControlId);
			mshSegment.getSendingApplication().getNamespaceID().setValue(loadedSample.getSendingApplicationName());
			Type sampleIdSeg = queryBuilder.getQPD().getQpd3_UserParametersInsuccessivefields().getData();
			sampleIdSeg.parse(loadedSample.getSampleId());
			queryBuilderBean.setSampleId(sampleIdSeg.toString());
			qpdSegment.getQpd3_UserParametersInsuccessivefields().setData(sampleIdSeg);
		
		
		rcpSegment.getQueryPriority().setValue(queryPriority);
		rcpSegment.getRcp3_ResponseModality().getCe1_Identifier().setValue(ce1Identifier);
		
		log.info("QBP method end with query"+ queryBuilder.toString());
		return queryBuilder;
	}

	/**
	 * @return the context
	 */
	public HapiContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(HapiContext context) {
		this.context = context;
	}

	/**
	 * @return the instId
	 */
public Sample getLpxJson(Sample loadedSample) {
	
	LP24JsonPropertyReader jsonParser = new LP24JsonPropertyReader();
	
	return jsonParser.readJsonData(loadedSample.getSampleId());
	
	
}
	
}
