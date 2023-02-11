package com.roche.connect.dpcr.sim.bl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.connect.dpcr.MainApp;
import com.roche.connect.dpcr.common.bl.hl7.MessageSender;
import com.roche.connect.dpcr.sim.DPCRSimulator;
import com.roche.connect.dpcr.util.AssayBean;
import com.roche.connect.dpcr.util.PartitionEngineBean;
import com.roche.connect.dpcr.util.ResultBean;
import com.roche.connect.dpcr.util.SampleBean;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.group.OML_O21_ORDER_PRIOR;
import ca.uhn.hl7v2.model.v26.message.ACK;
import ca.uhn.hl7v2.model.v26.message.OML_O21;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.OBR;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

@Component
public class QueryMessageSender  implements MessageSender {

	private HapiContext context;
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryMessageSender.class);

	public void run() {
		LOGGER.info("inside QBS method");

		try {
			QueryMessageBuilder builder = new QueryMessageBuilder();
			Message queryMessage = null;
			int timeDelay = 600000;
			queryMessage = builder.build();

			LOGGER.info("Request from Device: " + queryMessage.toString());

			try (Socket socket = new Socket(MainApp.getValueFromPropFile("HostName"),
					Integer.parseInt(MainApp.getValueFromPropFile("HostPort")))) {

				System.out.println("Connection established: " + socket.isConnected());
				socket.setSoTimeout(timeDelay);
				StringBuffer qbpQueryMessage = new StringBuffer(queryMessage.toString());

				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				String qbpQueryMessageWrapper = "\u000b" + qbpQueryMessage.toString() + "\u001c" + "\r";

				// Send the MLLP-wrapped HL7 message to the server
				out.write(qbpQueryMessageWrapper.getBytes());
				DataInputStream input = new DataInputStream(in);
				new BufferedReader(new InputStreamReader(input, "UTF-8"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));

				StringBuilder sb = new StringBuilder();
				String AckMessage = null;
				String ormData;
				while (((ormData = reader.readLine()) != null) && (!ormData.trim().equals(""))) {
					if (ormData.contains("OML")) {
						AckMessage = sb.toString();
						sb = new StringBuilder();
					}
					sb.append("\n" + ormData);
				}
				if (StringUtils.isNotBlank(AckMessage)) {
					LOGGER.info("Response ACK received: \n  " + AckMessage);
					validateAck(AckMessage.trim());
				} else if (sb.toString().trim().contains("ACK") && !sb.toString().trim().contains("OML")) {
					LOGGER.info("Response ACK received: \n  " + sb.toString().trim());
					validateAck(sb.toString().trim());
				}
				String response = null;

				response = sb.toString().trim();
				if (StringUtils.isNotBlank(response) && response.contains("OML")) {
					LOGGER.info("Response OML received: \n  " + response);
					processOML(response.trim());
				}
				else {
					LOGGER.info("No response recieved");
					System.exit(1);
				}

			}

		} catch (Exception e) {
			LOGGER.info("Socket connection failed or timed out");
			System.exit(1);
		}

	}

	private void processOML(String response) {
		try (FileWriter hl7Writer = new FileWriter(MainApp.getClassPath() + "/" + MainApp.getValueFromPropFile("MessageTxt"),
				true)) {
			this.context = new DefaultHapiContext();
			Parser p = this.context.getPipeParser();
			OML_O21 oml = (OML_O21) p.parse(hl7VersionUpdate(response));
			ResultBean run = new ResultBean();
			List<SampleBean> sample = new ArrayList<>();
			List<PartitionEngineBean> partitionEngine = new ArrayList<>();
			StringJoiner messageValidate = new StringJoiner(",");
			
			MSH msh = oml.getMSH();

			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			String currentTime = dateFormatter.format(date);
			PartitionEngineBean partitionEngineBean = new PartitionEngineBean();
			partitionEngineBean.setDateandTime(currentTime);
			partitionEngineBean.setFluidId("fluidId1");
			partitionEngineBean.setPlateId("plateId");
			partitionEngineBean.setSerialNumber("engineSN1");
			partitionEngine.add(partitionEngineBean);

			for (int i = 0; i < oml.getORDERReps(); i++) {
				run.setRunId(oml.getORDER(i).getORC().getOrc4_PlacerGroupNumber()
						.getEi1_EntityIdentifier().getValue());

				SampleBean sampleBean = new SampleBean();
				String orderId = oml.getORDER(i).getORC().getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier()
						.getValue();
				if (StringUtils.isBlank(orderId))
					messageValidate.add("200");
				sampleBean.setAccessioningId(orderId);
				sampleBean.setFlag(null);
				String inputChipId = oml.getORDER(i).getOBSERVATION_REQUEST().getSPECIMEN().getCONTAINER().getSAC()
						.getSac10_CarrierIdentifier().getEi1_EntityIdentifier().getValue();
				if (StringUtils.isBlank(inputChipId))
					messageValidate.add("200");
				sampleBean.setInputchipId(inputChipId);

				String inputChipPosition = oml.getORDER(i).getOBSERVATION_REQUEST().getSPECIMEN().getCONTAINER()
						.getSAC().getSac11_PositionInCarrier().getNa1_Value1().getValue();
				if (StringUtils.isBlank(inputChipPosition))
					messageValidate.add("200");
				sampleBean.setInputchipPosition(inputChipPosition);

				sampleBean.setOperatorName("Admin");

				List<AssayBean> assay = new ArrayList<>();
				AssayBean assayBean = new AssayBean();
				assayBean.setAnalysisPackageName(oml.getORDER(i).getOBSERVATION_REQUEST().getOBR()
						.getObr4_UniversalServiceIdentifier().getCwe5_AlternateText().getValue());
				assayBean.setAssayType(oml.getORDER(i).getOBSERVATION_REQUEST().getOBR()
						.getObr4_UniversalServiceIdentifier().getCwe4_AlternateIdentifier().getValue());
				assayBean.setAssayVersion("1.0");
				assayBean.setKit(oml.getORDER(i).getOBSERVATION_REQUEST().getOBR().getObr4_UniversalServiceIdentifier()
						.getCwe7_CodingSystemVersionID().getValue());
				assayBean.setMasterMix(oml.getORDER(i).getOBSERVATION_REQUEST().getOBR()
						.getObr4_UniversalServiceIdentifier().getCwe6_NameOfAlternateCodingSystem().getValue());
				assayBean.setQualitativeValue("Positive");
				assayBean.setQuantitativeValue("2.6");
				assayBean.setQuantitativeResult("F");
				assayBean.setQualitativeResult("F");
				assay.add(assayBean);

				Terser ter = new Terser(oml);
				if(StringUtils.isNotBlank(ter.get("/.OBR-4(1)-4"))) {
				AssayBean assayBean1 = new AssayBean();
				assayBean1.setAnalysisPackageName("ap1");
				assayBean1.setAssayType(ter.get("/.OBR-4(1)-4"));
				assayBean1.setAssayVersion(ter.get("/.OBR-4(1)-5"));
				assayBean1.setKit(ter.get("/.OBR-4(1)-7"));
				assayBean1.setMasterMix(ter.get("/.OBR-4(1)-6"));
				assayBean1.setQualitativeValue("Positive");
				assayBean1.setQuantitativeValue("2.6");
				assayBean1.setQuantitativeResult("F");
				assayBean1.setQualitativeResult("F");
				assay.add(assayBean1);
				}

				sampleBean.setAssay(assay);
				sampleBean.setPartitionEngine(partitionEngine);

				sample.add(sampleBean);

			}

			int count = oml.getORDER().getOBSERVATION_REQUEST().getPRIOR_RESULT().getORDER_PRIORReps();
			for (int i = 0; i < count; i++) {

				OML_O21_ORDER_PRIOR omlPrior = oml.getORDER().getOBSERVATION_REQUEST().getPRIOR_RESULT()
						.getORDER_PRIOR(i);
				OBR obr = omlPrior.getOBR();
				Terser ter = new Terser(oml);
				SampleBean sampleBean = new SampleBean();
				String orderId = omlPrior.getORC().getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().getValue();
				if (StringUtils.isBlank(orderId))
					messageValidate.add("200");
				sampleBean.setAccessioningId(orderId);
				sampleBean.setFlag(null);
				String inputChipId = ter.get("/.ORDER_PRIOR(" + i + ")/SAC(0)-10-1");
				if (StringUtils.isBlank(inputChipId))
					messageValidate.add("200");
				sampleBean.setInputchipId(inputChipId);

				String inputChipPosition = ter.get("/.ORDER_PRIOR(" + i + ")/SAC(0)-11-1");
				if (StringUtils.isBlank(inputChipPosition))
					messageValidate.add("200");
				sampleBean.setInputchipPosition(inputChipPosition);
				sampleBean.setOperatorName("Admin");

				List<AssayBean> assay = new ArrayList<>();
				AssayBean assayBean = new AssayBean();
				assayBean.setAnalysisPackageName("ap1");
				assayBean.setAssayType(
						obr.getObr4_UniversalServiceIdentifier().getCwe4_AlternateIdentifier().getValue());
				assayBean.setAssayVersion(obr.getObr4_UniversalServiceIdentifier().getCwe5_AlternateText().getValue());
				assayBean.setKit(obr.getObr4_UniversalServiceIdentifier().getCwe7_CodingSystemVersionID().getValue());
				assayBean.setMasterMix(
						obr.getObr4_UniversalServiceIdentifier().getCwe6_NameOfAlternateCodingSystem().getValue());
				assayBean.setQualitativeValue("Positive");
				assayBean.setQuantitativeValue("2.6");
				assayBean.setQuantitativeResult("F");
				assayBean.setQualitativeResult("F");
				assay.add(assayBean);
				if(StringUtils.isNotBlank(ter.get("/.ORDER_PRIOR(" + i + ")/OBR-4(1)-4"))) {
				AssayBean assayBean1 = new AssayBean();
				assayBean1.setAnalysisPackageName("ap1");
				assayBean1.setAssayType(ter.get("/.ORDER_PRIOR(" + i + ")/OBR-4(1)-4"));
				assayBean1.setAssayVersion(ter.get("/.ORDER_PRIOR(" + i + ")/OBR-4(1)-5"));
				assayBean1.setKit(ter.get("/.ORDER_PRIOR(" + i + ")/OBR-4(1)-7"));
				assayBean1.setMasterMix(ter.get("/.ORDER_PRIOR(" + i + ")/OBR-4(1)-6"));
				assayBean1.setQualitativeValue("Positive");
				assayBean1.setQuantitativeValue("2.6");
				assayBean1.setQuantitativeResult("F");
				assayBean1.setQualitativeResult("F");
				assay.add(assayBean1);
				}
				sampleBean.setAssay(assay);
				sampleBean.setPartitionEngine(partitionEngine);

				sample.add(sampleBean);

			}
			run.setSample(sample);
			run.setRecevingApplication(msh.getSendingApplication().getNamespaceID().getValue());
			run.setRecevingFacility(msh.getSendingFacility().getNamespaceID().getValue());
			run.setSendingFacility(msh.getReceivingFacility().getHd1_NamespaceID().getValue());
			run.setAckMsgControlId(msh.getMessageControlID().getValue());
			run.setRunResults("Passed");
			run.setHl7Version(DPCRSimulator.getHL7Version());
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(MainApp.getValueFromPropFile("DPCRRunDataPath")), run);
			DPCRSimulator.setAckValidator(messageValidate.toString());

			hl7Writer.write("\n Response OML Message from Connect \n");
			hl7Writer.write("Message ControlId: " + oml.getMSH().getMessageControlID().getValue() + "\n");
			hl7Writer.write("Date : " + new Date() + "\n");
			hl7Writer.write("Message :\n");
			hl7Writer.write(response.trim() + "\n");
		} catch (Exception e) {
			LOGGER.error("Error while processing OML response" + e.getMessage());
		}
	}

	private void validateAck(String response) {

		try (FileWriter hl7Writer = new FileWriter(MainApp.getClassPath() + "/" + MainApp.getValueFromPropFile("MessageTxt"),
				true)) {
			this.context = new DefaultHapiContext();
			Parser p = this.context.getPipeParser();
			ACK ack = (ACK) p.parse(hl7VersionUpdate(response));
			String ackStatus = ack.getMSA().getMsa1_AcknowledgmentCode().getValue();
			Terser ter = new Terser(ack);

			hl7Writer.write("\n Response ACK Message from Connect \n");
			hl7Writer.write("Message ControlId: " + ack.getMSH().getMessageControlID().getValue() + "\n");
			hl7Writer.write("Date : " + new Date() + "\n");
			hl7Writer.write("Message :\n");
			hl7Writer.write(response.trim() + "\n");
			if (ackStatus.equalsIgnoreCase("AR")) {
				System.out.println(
						"Recieved Negative ACK from Connect for Query with error code: " + ter.get("/.ERR-3-1"));
				throw new Exception("Negative ACK response recieved. Error may be due to \n 1)Please try with valid plate ID \n 2)Unregistered device");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			System.exit(1);
		}
	}

	private String hl7VersionUpdate(String inMsg) {

		InputStream resource = new ByteArrayInputStream(inMsg.getBytes());
		String msg = "";
		try {
			msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		} catch (IOException e2) {
			// TOrmmsgODO Auto-generated catch block
			e2.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		String[] msgSplit = msg.split("\r");

		for (int i = 0; i < msgSplit.length; i++) {

			if (msgSplit[i].trim().contains("MSH")) {
				sb.append(updateMsh(msgSplit[i])).append('\r');
			} else {
				sb.append(msgSplit[i]).append('\r');
			}
		}

		return sb.toString();

	}

	private String updateMsh(String inMsg) {
		// TODO Auto-generated method stub

		String[] mshSplit = inMsg.split("\\|");

		if (mshSplit.length > 11) {
			DPCRSimulator.setHL7Version(mshSplit[11]);
			mshSplit[11] = "2.6";
		}
		return String.join("|", mshSplit);
	}

	public void cleanUp(Path path) {
		try {
			Files.delete(path);
		} catch (IOException ioException) {
			LOGGER.error(ioException.getMessage());
		}
	}

	@Override
	public void run(ResultBean resultBean) throws HL7Exception, IOException {

	}

	@Override
	public void run(ResultBean resultBean, String ackCheck) throws HL7Exception, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(String status) throws HL7Exception, IOException {
		// TODO Auto-generated method stub

	}

}
