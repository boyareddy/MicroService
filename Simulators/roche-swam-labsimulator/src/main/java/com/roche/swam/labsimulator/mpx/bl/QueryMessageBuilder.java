package com.roche.swam.labsimulator.mpx.bl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.util.JsonPropertyReader;
import com.roche.swam.labsimulator.util.LP24PostJsonPropertyReader;
import com.roche.swam.labsimulator.util.LP24PreJsonPropertyReader;
import com.roche.swam.labsimulator.util.LP24SeqPrepJsonPropertyReader;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
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

	public QBP_Q11 build(Sample loadedSample) throws IOException, HL7Exception, ParseException {

		log.info("inside QBP build method with sample :" + loadedSample.toString());
		QBP_Q11 queryBuilder = new QBP_Q11();
		Sample queryBuilderBean = new Sample();
		String universalTypeId = "M";
		String characterSet = "UNICODE UTF-8";
		String ce3CodingSystem = "IHE_LABTF";
		String ce2Text = "Work Order Step";
		String queryPriority = "I";
		String ce1Identifier = "P";

		if ("LP-PRE-PCR".equalsIgnoreCase(MainApp.getDeviceName())) {
			queryBuilderBean = getLpPreJson(loadedSample);

		} else if ("LP-POST-PCR".equalsIgnoreCase(MainApp.getDeviceName())) {
			queryBuilderBean = getLpPosJson(loadedSample);
		} else if ("LP-SEQ-PP".equalsIgnoreCase(MainApp.getDeviceName())) {
			queryBuilderBean = getLpSeqJson(loadedSample);
		} else {
			queryBuilderBean = getMpJson(loadedSample);
		}

		queryBuilderBean.setSampleId(loadedSample.getSampleId());
		queryBuilder.initQuickstart("QBP", "WOS", "P");
		MSH mshSegment = queryBuilder.getMSH();
		QPD qpdSegment = queryBuilder.getQPD();
		RCP rcpSegment = queryBuilder.getRCP();

		String messageControlId = UUID.randomUUID().toString().substring(0, 20);
		System.out.println("Message control ID :" + messageControlId);

		mshSegment.getMsh3_SendingApplication().getHd2_UniversalID().parse(loadedSample.getInstrumentId());

		mshSegment.getMsh3_SendingApplication().getHd3_UniversalIDType().parse(universalTypeId);

		mshSegment.getDateTimeOfMessage().parse(dateFormatter.format(Calendar.getInstance().getTime()));
		mshSegment.getSendingFacility().getNamespaceID().setValue(queryBuilderBean.getSendingFacilityName());
		mshSegment.getSendingApplication().getNamespaceID().setValue(queryBuilderBean.getSendingApplicationName());
		mshSegment.getReceivingApplication().getNamespaceID().setValue(queryBuilderBean.getReceivingApplicationName());
		mshSegment.getReceivingFacility().getNamespaceID().setValue(queryBuilderBean.getReceivingFacilityName());
		mshSegment.getMessageControlID().setValue(messageControlId);
		mshSegment.getProcessingID().getPt1_ProcessingID().setValue(queryBuilderBean.getProcessingId());
		mshSegment.getVersionID().getVid1_VersionID().setValue(queryBuilderBean.getVersionId());
		mshSegment.getCharacterSet(0).setValue(characterSet);
		log.info("QBP build after msh segment");
		qpdSegment.getQpd1_MessageQueryName().getCe1_Identifier().setValue(queryBuilderBean.getMessageQueryName());
		qpdSegment.getQpd1_MessageQueryName().getCe3_NameOfCodingSystem().setValue(ce3CodingSystem);
		qpdSegment.getQpd1_MessageQueryName().getCe2_Text().setValue(ce2Text);
		qpdSegment.getQpd2_QueryTag().setValue(messageControlId);
		if (MainApp.getDeviceName().equals("MagnaPure24")) {
			mshSegment.getSendingApplication().getNamespaceID().setValue(loadedSample.getSendingApplicationName());
		} else {
			mshSegment.getSendingApplication().getNamespaceID().setValue(MainApp.getFilepath("LP"));
		}

		if (!(loadedSample.getSampleId().trim().equals("") || loadedSample.getSampleId() == null||loadedSample.getSampleId().equals("_"))) {

			Type sampleIdSeg = queryBuilder.getQPD().getQpd3_UserParametersInsuccessivefields().getData();
			sampleIdSeg.parse(loadedSample.getSampleId());
			queryBuilderBean.setSampleId(sampleIdSeg.toString());
			qpdSegment.getQpd3_UserParametersInsuccessivefields().setData(sampleIdSeg);
		} else {
			Type sampleIdSeg = queryBuilder.getQPD().getQpd3_UserParametersInsuccessivefields().getData();
			sampleIdSeg.parse(null);
			queryBuilderBean.setSampleId(sampleIdSeg.toString());
			qpdSegment.getQpd3_UserParametersInsuccessivefields().setData(sampleIdSeg);
		}

		rcpSegment.getQueryPriority().setValue(queryPriority);
		rcpSegment.getRcp3_ResponseModality().getCe1_Identifier().setValue(ce1Identifier);

		log.info("QBP method end with query" + queryBuilder.toString());
		return queryBuilder;
	}

	/**
	 * @return the context
	 */
	public HapiContext getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(HapiContext context) {
		this.context = context;
	}

	/**
	 * @return the instId
	 */
	public Sample getMpJson(Sample loadedSample) {

		JsonPropertyReader jsonParser = new JsonPropertyReader();

		return jsonParser.readJsonData(loadedSample.getSampleId());

	}

	public Sample getLpPreJson(Sample loadedSample) {

		LP24PreJsonPropertyReader jsonParser = new LP24PreJsonPropertyReader();

		return jsonParser.readJsonData(loadedSample.getSampleId());

	}

	public Sample getLpPosJson(Sample loadedSample) {

		LP24PostJsonPropertyReader jsonParser = new LP24PostJsonPropertyReader();

		return jsonParser.readJsonData(loadedSample.getSampleId());

	}

	public Sample getLpSeqJson(Sample loadedSample) {

		LP24SeqPrepJsonPropertyReader jsonParser = new LP24SeqPrepJsonPropertyReader();

		return jsonParser.readJsonData(loadedSample.getSampleId());

	}

}
