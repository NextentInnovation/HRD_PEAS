package hu.nextent.peas.utils;

import java.text.MessageFormat;
import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class MySpelDateTimeFormater {
	
	private static final MessageFormat messageGenerator = new MessageFormat("<formater: {0}, temporal: {1}, excpetion: {2}>");
	private static final String NULL_MESSAGE = "<null>";
	
	private final String name;
	private final DateTimeFormatter formater;

	
	public MySpelDateTimeFormater(String name, DateTimeFormatter formater) {
		this.name = name;
		this.formater = formater;
	}
	
	
	public String format(TemporalAccessor temporal) {
        if (temporal == null) {
        	return NULL_MESSAGE;
        }
        
        try {
        	// return formater.format(temporal);
        	return formater.format(((OffsetDateTime) temporal).atZoneSameInstant(ZoneId.of("+01:00")));
        } catch (DateTimeException e) {
        	return messageGenerator.format(new Object[] {name, temporal, e.getMessage()});
        }
    }
}
