package com.mrk.myordershop.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonTimeStampSerializer extends JsonSerializer<Date> {

	private DateTimeFormatter format = DateTimeFormat.forPattern("dd-MM-yyyy")
			.withZone(DateTimeZone.forTimeZone(TimeZone.getDefault()));

	@Override
	public void serialize(Date date, JsonGenerator arg1, SerializerProvider arg2)
			throws IOException, JsonProcessingException {

		DateTime dateTime = new DateTime(date);
		arg1.writeString(format.print(dateTime));
	}

	public static List getFormates() {
		DateFormat formate = new SimpleDateFormat("dd-MM-yyyy",
				Locale.getDefault());
		formate.setTimeZone(TimeZone.getDefault());
		formate.setLenient(false);

		DateFormat formate1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy",
				Locale.getDefault());
		formate1.setTimeZone(TimeZone.getDefault());

		DateFormat formate2 = new SimpleDateFormat(
				"E MMM dd yyyy HH:mm:ss 'GMT'z");
		formate2.setTimeZone(TimeZone.getDefault());
		return Arrays.asList(formate, formate1, formate2);
	}
}
