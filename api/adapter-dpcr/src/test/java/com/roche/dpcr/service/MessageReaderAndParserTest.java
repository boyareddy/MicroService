package com.roche.dpcr.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.dpcr_analyzer.AcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.QueryMessage;
import com.roche.dpcr.dto.MessageExchange;
import com.roche.dpcr.util.InstanceUtil;

import ca.uhn.hl7v2.HL7Exception;

@PrepareForTest({ HMTPLoggerImpl.class, InstanceUtil.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class MessageReaderAndParserTest {
	@InjectMocks
	MessageReaderAndParser messageReaderAndParser;
	@Mock
	MessageHandlerService messageHandlerService;
	@Mock
	OutputStream out;
	@Mock
	MessageExchange omlExchange;
	@Mock
	Map<String, MessageExchange> connectionMap;
	@Mock
	InstanceUtil instanceUtil;

	@BeforeTest
	public void beforeTest() throws JsonProcessingException, UnsupportedEncodingException, HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(messageHandlerService).sendQueryToIMM(Mockito.any(QueryMessage.class));
		Mockito.doNothing().when(messageHandlerService).sendOMLACKToIMM(Mockito.any(AcknowledgementMessage.class));
		Mockito.doNothing().when(messageHandlerService).sendNotification(Mockito.anyString(), Mockito.anyString());
		PowerMockito.mockStatic(InstanceUtil.class);
		Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(connectionMap);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(omlExchange);
		Mockito.when(omlExchange.getRunId()).thenReturn("runId");
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString())).thenReturn(omlExchange);
	}

	/**
	 * We need a special {@link IObjectFactory}.
	 * 
	 * @return {@link PowerMockObjectFactory}.
	 */
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	public MessageExchange getMessageExchange() {
		MessageExchange messageExchange = new MessageExchange();
		messageExchange.setMsgVersion("2.6");
		messageExchange.setOut(out);
		messageExchange.setSerialNo("dsad");
		return messageExchange;
	}

	public MessageExchange getMessageExchangeVersionMatch() {
		MessageExchange messageExchange = new MessageExchange();
		messageExchange.setMsgVersion("2.1");
		messageExchange.setOut(out);
		return messageExchange;
	}

	@Test
	public void readQueryMessageQBPTest() throws HL7Exception, IOException, HMTPException {
		String inMsg = "MSH|^~\\&| COBAS DPRC^12345||AVENIO||20170214080046||QBP^Z01|MSG000001|P|2.6|\n"
				+ "QPD|Z01||plate1_id|plate2_id|plate3_id|plate4_id|plate5_id|plate6_id|plate7_id|plate8_id|plate9_id|plate10_id|plate11_id|plate12_id\n"
				+ "RCP|I";
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchange());
	}

	@Test
	public void readQueryMessageNegativeTest() throws HL7Exception, IOException, HMTPException {
		String inMsg = "input Message";
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchange());
	}

	@Test
	public void readQueryMessageNegativeVersionMismatchTest() throws HL7Exception, IOException, HMTPException {
		String inMsg = "MSH|^~\\&| COBAS DPRC^12345||AVENIO||20170214080046||QBP^Z01|MSG000001|P|2.6|\n"
				+ "QPD|Z01||plate1_id|plate2_id|plate3_id|plate4_id|plate5_id|plate6_id|plate7_id|plate8_id|plate9_id|plate10_id|plate11_id|plate12_id\n"
				+ "RCP|I";
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchangeVersionMatch());
	}

	@Test
	public void readQueryMessageQBPNegativeTest() throws HL7Exception, IOException, HMTPException {
		String QBPNegativeMsg = "MSH|^~\\&| COBAS DPRC^12345||AVENIO||20170214080046||QBP^Z01|MSG000001|OP|2.6|\n"
				+ "QPD|Z01||plate1_id|plate2_id|plate3_id|plate4_id|plate5_id|plate6_id|plate7_id|plate8_id|plate9_id|plate10_id|plate11_id|plate12_id\n"
				+ "RCP|I";
		messageReaderAndParser.readQueryMessage(QBPNegativeMsg, getMessageExchange());
	}

	@Test
	public void readQueryMessageACKTest() throws HL7Exception, IOException, HMTPException {
		instanceUtilMock();
		String inMsg = "MSH|^~\\&|Connect|Roche Diagnostics| COBAS DPRC^12345|Roche Diagnostics|20171204145712||ACK^O21|726f21e7-4728-4d03-9fcd-23a46000bfdf|P|2.6|\n"
				+ "MSA|AA|726f21e7-4728-4d03-9fcd-23a46000bfdf||";
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchange());
	}

	public void instanceUtilMock() {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(InstanceUtil.class);
		Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(connectionMap);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(omlExchange);
		Mockito.when(omlExchange.getRunId()).thenReturn("runId");
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString())).thenReturn(omlExchange);
	}

	@Test
	public void readQueryMessageACKNegativeTest() throws HL7Exception, IOException, HMTPException {
		String inMsg = "MSH|^~\\&|Connect|Roche Diagnostics| COBAS DPRC^12345|Roche Diagnostics|20171204145712||ACK^O21|726f21e7-4728-4d03-9fcd-23a46000bfdf|OP|2.6|\n"
				+ "MSA|AA|726f21e7-4728-4d03-9fcd-23a46000bfdf||";
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchange());
	}

	@Test
	public void readQueryMessageACKNegativeApplicationRejectTest() throws HL7Exception, IOException, HMTPException {
		instanceUtilMock();
		String inMsg = "MSH|^~\\&|Connect|Roche Diagnostics| COBAS DPRC^12345|Roche Diagnostics|20171204145712||ACK^O21|726f21e7-4728-4d03-9fcd-23a46000bfdf|P|2.6|\n"
				+ "MSA|AR|726f21e7-4728-4d03-9fcd-23a46000bfdf||\n"
				+ "ERR|||102^Data type error^HL70357|||||\"Specified sample type is not recognized\"";
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchange());
	}

	@Test
	public void readQueryMessageESUTest() throws HL7Exception, IOException, HMTPException {
		String inMsg = "MSH|^~\\&| COBAS DPRC^12345||AVENIO||20170214080046||ESU^U01|726f21e7-4728-4d03-9fcd-23a46000bfdf |P|2.6|\n"
				+ "EQU|12345|20181028080046|OP";
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchange());
	}

	@Test
	public void readQueryMessageORUTest() throws HL7Exception, IOException, HMTPException {
		String inMsg = "MSH|^~\\&|COBAS DPCR^DPCR-123|Connect|COBAS DPCR|Roche Diagnostics|20190305174738||ORU^R01|87fdae1d-ebbf-4d60-a|P|2.6\n" + 
				"ORC|RE|11221122||RND1958801896|||||20190305174738|Admin|Admin||||||||Admin\n" + 
				"OBR|1|11221122||dPCRAnalyzerAssay1^AP1^1.0^System fluid\n" + 
				"OBX|1|ST|dPCRAnalyzerAssay1||2.9|ml|||||F\n" + 
				"OBX|2|ST|dPCRAnalyzerAssay1||Positive||||||F\n" + 
				"SPM|1|plate \n" + 
				" \n" ;
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchange());
	}
	
	@Test
	public void readQueryMessageESUIDTest() throws HL7Exception, IOException, HMTPException {
		String inMsg = "MSH|^~\\&| COBAS DPRC^12345||AVENIO||20170214080046||ESU^U01|726f21e7-4728-4d03-9fcd-23a46000bfdf |P|2.6|\n"
				+ "EQU|12345|20181028080046|ID";
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchange());
	}
	
	@Test
	public void readQueryMessageESUESTest() throws HL7Exception, IOException, HMTPException {
		String inMsg = "MSH|^~\\&| COBAS DPRC^12345||AVENIO||20170214080046||ESU^U01|726f21e7-4728-4d03-9fcd-23a46000bfdf |P|2.6|\n"
				+ "EQU|12345|20181028080046|ES";
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchange());
	}
	
	@Test
	public void readQueryMessageESUSSTest() throws HL7Exception, IOException, HMTPException {
		String inMsg = "MSH|^~\\&| COBAS DPRC^12345||AVENIO||20170214080046||ESU^U01|726f21e7-4728-4d03-9fcd-23a46000bfdf |P|2.6|\n"
				+ "EQU|12345|20181028080046|SS";
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchange());
	}
	
	@Test
	public void readQueryMessageESUOPNormalTest() throws HL7Exception, IOException, HMTPException {
		String inMsg = "MSH|^~\\&| COBAS DPRC^12345||AVENIO||20170214080046||ESU^U01|726f21e7-4728-4d03-9fcd-23a46000bfdf |P|2.6|\n"
				+ "EQU|12345|20181028080046|OP^NORMAL";
		messageReaderAndParser.readQueryMessage(inMsg, getMessageExchange());
	}

}
