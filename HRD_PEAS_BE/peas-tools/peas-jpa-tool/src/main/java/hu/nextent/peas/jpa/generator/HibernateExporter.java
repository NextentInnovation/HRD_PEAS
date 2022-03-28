package hu.nextent.peas.jpa.generator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HibernateExporter {
	
	private static Logger log = LoggerFactory.getLogger(HibernateExporter.class);

	
	@Option(name="-p",usage="Packages")
	List<String> packages = new ArrayList<String>();
	
	@Option(name="--package",usage="Packages")
	public void addPackage(String pack) {
		this.packages.add(pack);
	}
	
	@Option(name="-o",usage="Output File")
	File outputFile = new File("./v1_create_schema.sql");
	
	@Option(name="--output",usage="Output File")
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	@Option(name="-a",
			usage="Action : CREATE/DROP/BOTH"
			)
	String action = SchemaExport.Action.CREATE.name();
	
	@Option(name="--action",usage="Action : CREATE/DROP/BOTH")
	public void setAction(String action) {
		this.action = action;
	}

	SchemaExport.Action schemaAction = SchemaExport.Action.CREATE;
	MetadataSources metadataSources;
	SchemaExport schemaExport; 
	
	
	
	public void run() {
		printArgs();
		prepareArgs();
		buildMetadataSources();
		searchJpaClasses();
		buildSchemaExport();
		clearOutputFileIfExits();
		schemaExport();
		bye();
	}

	void printArgs() {
		log.info(String.format("packages:   %s", packages));
		log.info(String.format("outputFile: %s", outputFile));
		log.info(String.format("action:  	%s", action));
	}
	
	void prepareArgs() {
		log.info("prepareArgs start");
		schemaAction = SchemaExport.Action.valueOf(action);
		log.info("prepareArgs end");
	}
	
	void buildMetadataSources() {
		log.info("buildMetadataSources start");
		StandardServiceRegistryBuilder registryBuilder = 
				new StandardServiceRegistryBuilder()
					.applySetting(AvailableSettings.HBM2DDL_AUTO, "create-drop")
					.applySetting(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL8Dialect")
					.applySetting(AvailableSettings.NON_CONTEXTUAL_LOB_CREATION, false);
		StandardServiceRegistry standardServiceRegistry = registryBuilder.build();
		metadataSources = new MetadataSources(standardServiceRegistry);
		log.info("buildMetadataSources end");
	}
	
	void searchJpaClasses() {
		log.info("searchJpaClasses start");
		for (String entityPackage : packages) {
			metadataSources.addPackage(entityPackage);
            final Reflections reflections = new Reflections(entityPackage);
            for (Class<?> cl : reflections.getTypesAnnotatedWith(MappedSuperclass.class)) {
            	metadataSources.addAnnotatedClass(cl);
            	log.debug(cl.getCanonicalName());
            }
            for (Class<?> cl : reflections.getTypesAnnotatedWith(Entity.class)) {
            	metadataSources.addAnnotatedClass(cl);
            	log.debug(cl.getCanonicalName());
            }
        }
		log.info("searchJpaClasses end");
	}
	
	void buildSchemaExport() {
		log.info("buildSchemaExport start");
		schemaExport = new SchemaExport();
		schemaExport.setDelimiter(";");
		schemaExport.setManageNamespaces(true);
        schemaExport.setHaltOnError(true);
        schemaExport.setFormat(true);
		log.info("buildSchemaExport end");
	}

	void clearOutputFileIfExits() {
		if (outputFile.exists()) {
			log.info("clearOutputFile start");
			outputFile.delete();
			log.info("clearOutputFile end");
		}
	}
	
	
	void schemaExport() {
		log.info("schemaExport start");
		log.info("outputFile: " + outputFile.getAbsolutePath());
        schemaExport.setOutputFile(outputFile.getAbsolutePath());
        schemaExport.execute(
        		EnumSet.of(org.hibernate.tool.schema.TargetType.SCRIPT), 
        		schemaAction, 
        		metadataSources.buildMetadata()
        		);
		log.info("schemaExport end");
	}
	
	void bye() {
		log.info("bye");
	}
	
	public static void main(String ... args) {
		HibernateExporter bean = new HibernateExporter();
		CmdLineParser parser = new CmdLineParser(bean);
		
		try {
            parser.parseArgument(args);
	    } catch (CmdLineException e) {
	        // handling of wrong arguments
	    	log.error(e.getMessage());
	        System.err.println(e.getMessage());
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        parser.printUsage(baos);
	        log.error(new String(baos.toByteArray()));
	    }
        bean.run();
	}
}
