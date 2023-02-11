package com.roche.connect.common.util;

import java.sql.Timestamp;
import java.text.ParseException;

public interface DateUtil {

	Timestamp getCurrentUTCTimeStamp();

	String convertUTCTimeWithServerTime(Timestamp date);

	Long convertUTCTimeWithServerTimeToMs(Timestamp date);

	Timestamp convertDateStringToUtcTimestamp(String dateString) throws ParseException;
}
