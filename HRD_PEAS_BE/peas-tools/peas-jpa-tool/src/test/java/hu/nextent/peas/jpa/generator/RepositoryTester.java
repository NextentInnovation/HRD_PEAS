package hu.nextent.peas.jpa.generator;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class RepositoryTester {


	
	@Test
	public void parseTest() throws CmdLineException {
		List<String> args = Arrays.asList(
			"-p", "hu.nextent.peas.jpa",
			"-o", "./target/generated-sources/repository",
			"-r", "hu.nextent.peas.repositoryPackage",
			"-i", "hu.nextent.peas.jpa.dao.DaoRepository"
		);
		
		
		HibernateRepositoryGen bean = new HibernateRepositoryGen();
		CmdLineParser parser = new CmdLineParser(bean);
		parser.parseArgument(args);
	}
	
	
	@Test
	public void makeAll() throws Exception {
		List<String> args = Arrays.asList(
				"-p", "hu.nextent.peas.jpa",
				"-o", "./target/generated-sources/repository",
				"-r", "hu.nextent.peas.repositoryPackage",
				"-i", "hu.nextent.peas.jpa.dao.DaoRepository"
		);
		
		HibernateRepositoryGen bean = new HibernateRepositoryGen();
		CmdLineParser parser = new CmdLineParser(bean);
		parser.parseArgument(args);
		
		bean.run();
		
	}
}
