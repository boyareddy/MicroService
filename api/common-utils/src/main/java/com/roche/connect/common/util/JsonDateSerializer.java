package com.roche.connect.common.util;

import java.io.IOException;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class JsonDateSerializer extends JsonSerializer<Timestamp> {

	DateUtilImpl dateUtil = new DateUtilImpl();

	@Override
	public void serialize(Timestamp date, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		Long dateTime = dateUtil.convertUTCTimeWithServerTimeToMs(date);
		gen.writeNumber(dateTime);
	}

}
