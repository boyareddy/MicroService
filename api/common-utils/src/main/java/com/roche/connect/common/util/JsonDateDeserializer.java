package com.roche.connect.common.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@Component
public class JsonDateDeserializer extends JsonDeserializer<Timestamp> {


	@Override
	public Timestamp deserialize(JsonParser jsonparser, DeserializationContext context)
			throws IOException {
		Timestamp timestamp = null;
		String dateAsString = jsonparser.getText();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS z");
		try {
			if (!dateAsString.isEmpty()) {
				Date dateTimeParsed = sdf.parse(dateAsString);
				timestamp = new Timestamp(dateTimeParsed.getTime());
			}
		} catch (ParseException pexp) {
			throw new IOException(pexp.getMessage());
		} 
		return timestamp;
	}

}
