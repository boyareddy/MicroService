package com.roche.connect.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

@Service
public class DateUtilImpl implements DateUtil {

	@Override
	public Timestamp getCurrentUTCTimeStamp() {
		ZoneOffset zoneOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
		TimeZone timezone = TimeZone.getTimeZone(zoneOffset);
		return new Timestamp((new Date().getTime()) - timezone.getOffset(new Date().getTime()));
	}

	@Override
	public String convertUTCTimeWithServerTime(Timestamp date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS z");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		ZoneOffset zoneOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
		TimeZone timezone = TimeZone.getTimeZone(zoneOffset);
		return dateFormat.format(new Date(date.getTime() + timezone.getOffset(new Date().getTime())));

	}

	@Override
	public Long convertUTCTimeWithServerTimeToMs(Timestamp date) {

		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS z");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			ZoneOffset zoneOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
			TimeZone timezone = TimeZone.getTimeZone(zoneOffset);

			return new Date(date.getTime() + timezone.getOffset(new Date().getTime())).getTime();
		}

		return null;
	}

	@Override
	public Timestamp convertDateStringToUtcTimestamp(String dateString) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		ZoneOffset zoneOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
		TimeZone timezone = TimeZone.getTimeZone(zoneOffset);

		return new Timestamp(dateFormat.parse(dateString).getTime() - timezone.getOffset(new Date().getTime()));
	}
}
