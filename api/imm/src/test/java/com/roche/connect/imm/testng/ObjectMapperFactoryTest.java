package com.roche.connect.imm.testng;

import java.io.IOException;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.dpcr_analyzer.ESUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUMessage;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.imm.utils.JsonFileReaderAsString;
import com.roche.connect.imm.utils.ObjectMapperFactory;

public class ObjectMapperFactoryTest {

	public static final String mp96QueryMessage = "src/test/java/resource/MP96dPCRQBPPositive.json";
	public static final String mp96ACKMessage = "src/test/java/resource/MP96dPCRACKPositive.json";
	public static final String mp96OULMessage = "src/test/java/resource/MP96dPCROULPositive.json";

	public static final String lp24QueryMessage = "src/test/java/resource/lp24QueryMessage.json";
	public static final String lp24RSPMessage = "src/test/java/resource/lp24RSPMessage.json";
	public static final String lp24SSUMessage = "src/test/java/resource/lp24SSUMessage.json";
	public static final String lp24ACKMessage = "src/test/java/resource/lp24ACKMessage.json";

	public static final String mp24ACKMessage = "src/test/java/resource/MP24ACKMessage.json";
	public static final String mp24QueryMessage = "src/test/java/resource/mp24QueryMessage.json";
	
	public static final String dpcrQBPMessage = "src/test/java/resource/dpcrQBPMessage.json";
	public static final String dpcrACKMessage = "src/test/java/resource/dpcrACKMessage.json";
	public static final String dpcrESUMessage = "src/test/java/resource/dpcrESUMessage.json";
	public static final String dpcrORUMessage = "src/test/java/resource/dpcrORUMessage.json";

	private ObjectMapperFactory objectMapperFactory = new ObjectMapperFactory();

	@Test
	public void nullJSONString() throws IOException {

		Object readObjectValue = objectMapperFactory.readObjectValue(null, ObjectMapperFactory.DEVICE_STR,
				DeviceType.MP96, MessageType.MP96_QBP);
		Assert.assertNull(readObjectValue);
	}

	@Test
	public void nullKeyString() throws IOException {
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(mp96QueryMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, null, null, null);
		Assert.assertNull(readObjectValue);
	}

	@Test
	public void mp96QueryMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(mp96QueryMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.DEVICE_STR,
				DeviceType.MP96, MessageType.MP96_QBP);
		Assert.assertTrue(readObjectValue instanceof com.roche.connect.common.mp96.QueryMessage);
	}

	@Test
	public void mp96ACKMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(mp96ACKMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.DEVICE_STR,
				DeviceType.MP96, MessageType.MP96_ACK);
		Assert.assertTrue(readObjectValue instanceof com.roche.connect.common.mp96.AdaptorACKMessage);
	}

	@Test
	public void mp96OULMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(mp96OULMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.DEVICE_STR,
				DeviceType.MP96, MessageType.MP96_OUL);
		Assert.assertTrue(readObjectValue instanceof com.roche.connect.common.mp96.OULRunResultMessage);
	}

	@Test
	public void lp24QueryMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(lp24QueryMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.DEVICE_STR,
				DeviceType.LP24, MessageType.LP24_QBP);
		Assert.assertTrue(readObjectValue instanceof QueryMessage);
	}

	@Test
	public void lp24RSPMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(lp24RSPMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.CONNECT_STR,
				DeviceType.LP24, MessageType.LP24_RSP);
		Assert.assertTrue(readObjectValue instanceof ResponseMessage);
	}

	@Test
	public void lp24SSUMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(lp24SSUMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.DEVICE_STR,
				DeviceType.LP24, MessageType.LP24_U03);
		Assert.assertTrue(readObjectValue instanceof SpecimenStatusUpdateMessage);
	}

	@Test
	public void mp24QueryMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(mp24QueryMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.DEVICE_STR,
				DeviceType.MP24, MessageType.MP24_STATUS_UPDATE);
		Assert.assertTrue(readObjectValue instanceof AdaptorRequestMessage);
	}

	@Test
	public void lp24ACKMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(lp24ACKMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.CONNECT_STR,
				DeviceType.LP24, MessageType.LP24_ACK);
		Assert.assertTrue(readObjectValue instanceof AcknowledgementMessage);
	}

	@Test
	public void mp24ACKMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(mp24ACKMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.CONNECT_STR,
				DeviceType.MP24, MessageType.MP24_ACK);
		Assert.assertTrue(readObjectValue instanceof AdaptorResponseMessage);
	}

	@Test
	public void nullKeyString2() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(mp24ACKMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, "", DeviceType.MP24,
				MessageType.MP24_ACK);
		Assert.assertNull(readObjectValue);
	}

	@Test
	public void nullKeyString3() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(mp24ACKMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.CONNECT_STR, "",
				MessageType.MP24_ACK);
		Assert.assertNull(readObjectValue);
	}

	@Test
	public void nullKeyString4() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(mp24ACKMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.CONNECT_STR,
				DeviceType.MP24, "");
		Assert.assertNull(readObjectValue);
	}

	@Test
	public void testInvalidSourceMP96() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(mp96QueryMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, "source", DeviceType.MP96,
				MessageType.MP96_QBP);
		Assert.assertNull(readObjectValue);
	}

	@Test
	public void testInvalidMessageTypeMP96() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(mp96QueryMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.DEVICE_STR,
				DeviceType.MP96, "InvalidMessageType");
		Assert.assertNull(readObjectValue);
	}
	
	@Test
	public void dpcrQueryMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(dpcrQBPMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.DEVICE_STR,
				DeviceType.DPCR_ANALYZER, MessageType.DPCR_ANALYZER_QBP);
		Assert.assertTrue(readObjectValue instanceof com.roche.connect.common.dpcr_analyzer.QueryMessage);
	}

	@Test
	public void dpcrACKMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(dpcrACKMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.DEVICE_STR,
				DeviceType.DPCR_ANALYZER, MessageType.DPCR_ANALYZER_ACK);
		Assert.assertTrue(readObjectValue instanceof com.roche.connect.common.dpcr_analyzer.AcknowledgementMessage);
	}

	@Test
	public void dpcrESUMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(dpcrESUMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.DEVICE_STR,
				DeviceType.DPCR_ANALYZER, MessageType.DPCR_ANALYZER_ESU);
		Assert.assertTrue(readObjectValue instanceof ESUMessage);
	}
	
	@Test
	public void dpcrORUMessage() throws IOException {

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(dpcrORUMessage);
		Object readObjectValue = objectMapperFactory.readObjectValue(jsonContent, ObjectMapperFactory.DEVICE_STR,
				DeviceType.DPCR_ANALYZER, MessageType.DPCR_ANALYZER_ORU);
		Assert.assertTrue(readObjectValue instanceof ORUMessage);
	}

}
