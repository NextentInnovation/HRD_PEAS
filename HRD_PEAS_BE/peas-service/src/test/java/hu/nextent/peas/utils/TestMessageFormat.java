package hu.nextent.peas.utils;

import java.text.MessageFormat;

import org.junit.Test;

public class TestMessageFormat {

	
	@Test
	public void testMe() {
		System.out.print(MessageFormat.format("{0}", (Object)null, (Object)null));
	}
}
