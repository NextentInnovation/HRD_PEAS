package hu.nextent.peas.jpa.generator;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class BaseTester {

	@Test
	public void parseTest() throws CmdLineException {
		List<String> args = Arrays.asList(
			"-p", "hu.nextent.peas.jpa",
			"-o", "./target/test.sql"
		);
		
		
		HibernateExporter bean = new HibernateExporter();
		CmdLineParser parser = new CmdLineParser(bean);
		parser.parseArgument(args);
		
		Assert.assertEquals(bean.packages.get(0), "hu.nextent.peas.jpa");
		Assert.assertEquals(bean.outputFile, new File("./target/test.sql"));
	}
	
	
	@Test
	public void makeAll() throws CmdLineException {
		List<String> args = Arrays.asList(
				"-p", "hu.nextent.peas.jpa",
				"-o", "./target/test.sql"
		);
		
		HibernateExporter bean = new HibernateExporter();
		CmdLineParser parser = new CmdLineParser(bean);
		parser.parseArgument(args);
		
		bean.run();
		
	}
}
