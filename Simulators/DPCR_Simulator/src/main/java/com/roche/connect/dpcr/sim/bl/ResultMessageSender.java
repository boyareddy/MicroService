package com.roche.connect.dpcr.sim.bl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.connect.dpcr.MainApp;
import com.roche.connect.dpcr.common.bl.hl7.MessageSender;
import com.roche.connect.dpcr.sim.DPCRSimulator;
import com.roche.connect.dpcr.util.ResultBean;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.message.ACK;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

@Component
public class ResultMessageSender implements MessageSender {

	@Autowired
	private SampleRepository samples;

	private HapiContext context;

	private static final Logger LOGGER = LoggerFactory.getLogger(ResultMessageSender.class);

	public void run(ResultBean resultBean) {
		// Socket socket = null;
		LOGGER.info("inside QBS method of result message sender...");
		try {
			ResultMessageBuilder builder = new ResultMessageBuilder();
			Message ssuMessage = null;
			int timeDelay = Integer.parseInt(MainApp.getValueFromPropFile("TimeOut"));
			ssuMessage = builder.build(resultBean);
			LOGGER.info("OUL from Device: " + ssuMessage.toString());
			try (Socket socket = new Socket(MainApp.getValueFromPropFile("HostName"),
					Integer.parseInt(MainApp.getValueFromPropFile("HostPort")));) { // sonar qube code coverages
				LOGGER.info("Connection established: " + socket.isConnected());
				socket.setSoTimeout(timeDelay);
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				String ssuMessageWrapper = "\u000b" + ssuMessage.toString() + "\u001c" + "\r";
				// Send the MLLP-wrapped HL7 message to the server
				out.write(ssuMessageWrapper.getBytes());

				DataInputStream input = new DataInputStream(in);
				BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));

				StringBuilder sb = new StringBuilder();
				String ackData;
				while (((ackData = reader.readLine()) != null) && (!ackData.trim().equals(""))) {
					sb.append("\n" + ackData);
				}
				String ackMessage = sb.toString().trim();
				if (StringUtils.isNotBlank(ackMessage)) {
					LOGGER.info("ACK received: \n  " + ackMessage);
					validateAck(ackMessage.trim());
				}else {
					LOGGER.info("No ACK recieved");
					System.exit(1);
				}
				out.close();
				in.close();

				LOGGER.info("after run method in result message sender...");
			}
		} catch (IOException ioException) {
			LOGGER.error(ioException.getLocalizedMessage());
		} catch (HL7Exception hl7Exception) {
			LOGGER.error(hl7Exception.getLocalizedMessage());
		}
	}

	private void validateAck(String response) {

		try (FileWriter hl7Writer = new FileWriter(
				MainApp.getClassPath() + "/" + MainApp.getValueFromPropFile("MessageTxt"), true)) {
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
				throw new Exception("Error while processing ACK response");
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

	@Override
	public void run() throws ParseException, LLPException {
		// TODO Auto-generated method stub

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
