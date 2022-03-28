package hu.nextent.peas.jpa.generator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.persistence.Entity;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;


public class HibernateRepositoryGen {
	
	private static Logger log = LoggerFactory.getLogger(HibernateRepositoryGen.class);

	public static String JPA_REPOSITORY = "org.springframework.data.jpa.repository.JpaRepository";
	public static String JPA_SPECIFICATION_EXECUTOR = "org.springframework.data.jpa.repository.JpaSpecificationExecutor";
	
	@Option(name="-p",usage="Entity Packages")
	List<String> packages = new ArrayList<String>();
	
	@Option(name="--package",usage="Entity Packages")
	public void addPackage(String pack) {
		this.packages.add(pack);
	}
	
	@Option(name="-o",usage="Output Dir")
	File outputDir = new File("");
	
	@Option(name="--output",usage="Output Dir")
	public void setTargetDirectory(File outputDir) {
		this.outputDir = outputDir;
	}
	
	@Option(name="-r",usage="Repository Packages")
	String repositoryPackage = "repository";
	
	@Option(name="--repository",usage="Repository Packages")
	public void addRepositoryPackage(String repositoryPackage) {
		this.repositoryPackage = repositoryPackage;
	}
	
	
	@Option(name="-i",usage="Repository interfaces")
	List<String> interfaces = new ArrayList<String>();
	
	@Option(name="--interface",usage="Repository interfaces")
	public void addInterface(String inf) {
		this.interfaces.add(inf);
	}
	
	@Option(name="-pr",usage="Repository Interface Name Prefix")
	String prefix = "";
	
	@Option(name="--prefix",usage="Repository Interface Name Prefix")
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@Option(name="-s",usage="Repository Interface Name Suffix")
	String suffix = "Repository";
	
	@Option(name="--suffix",usage="Repository Interface Name Suffix")
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	Set<Class<?>> entitiesClasses = new HashSet<>();
	
	public void run() throws IOException, ClassNotFoundException {
		printArgs();
		searchJpaClasses();
		makeClasses();
		bye();
	}

	void printArgs() {
		log.info(String.format("packages:          %s", packages));
		log.info(String.format("outputFile:        %s", outputDir));
		log.info(String.format("repositoryPackage: %s", repositoryPackage));
		log.info(String.format("interfaces:        %s", interfaces));
		log.info(String.format("prefix:            %s", prefix));
		log.info(String.format("suffix:            %s", suffix));
	}
	

	
	void searchJpaClasses() {
		log.info("searchJpaClasses start");
		for (String entityPackage : packages) {
            final Reflections reflections = new Reflections(entityPackage);
            for (Class<?> cl : reflections.getTypesAnnotatedWith(Entity.class)) {
            	entitiesClasses.add(cl);
            	log.debug(cl.getCanonicalName());
            }
        }
		log.info("searchJpaClasses end");
	}
	
	void makeClasses() throws IOException, ClassNotFoundException {
		for(Class<?> entityClazz: entitiesClasses) {
			TypeSpec repositoryTypeSpec = makeTypeSpec(entityClazz);
			JavaFile javaFile = makeJavaFile(repositoryTypeSpec);
			writeTo(javaFile);
		}
	}
	
	TypeSpec makeTypeSpec(Class<?> entityClass) throws ClassNotFoundException {
		ClassName repositoryClassName = makeRepositoryClassName(entityClass);
		log.info("Base: " + entityClass.getSimpleName() + ", repository: " + repositoryClassName.simpleName());

		TypeSpec.Builder repositoryInterfaceBuilder = 
				TypeSpec
					.interfaceBuilder(repositoryClassName)
					.addModifiers(Modifier.PUBLIC);
		List<ParameterizedTypeName> extendsInfs = listExtends(entityClass, findIdClass(entityClass));
		for (ParameterizedTypeName et: extendsInfs) {
			repositoryInterfaceBuilder.addSuperinterface(et);
		}
		TypeSpec repositoryInterface = repositoryInterfaceBuilder.build();
		return repositoryInterface;
	}
	
	ClassName makeRepositoryClassName(Class<?> entityClass) {
		String className = prefix + entityClass.getSimpleName() + suffix;
		ClassName repositoryClassName = ClassName.get(repositoryPackage, className);
		return repositoryClassName;
	}
	
	
	
	TypeName findIdClass(Class<?> entityClass) {
		List<Field> fields = getDeclaredFields(entityClass, true);
		for (Field checkedField : fields) {
			if (checkedField.isAnnotationPresent(javax.persistence.Id.class)) {
				Class<?> typeClass = checkedField.getType();
				return ClassName.get(typeClass);
			}
		}
		return ClassName.get(Long.class);
	}

	List<Field> getDeclaredFields(Class<?> clazz, boolean recursively) {
        List<Field> fields = new LinkedList<Field>();
        Field[] declaredFields = clazz.getDeclaredFields();
        fields.addAll(Arrays.asList(declaredFields));
        Class<?> superClass = clazz.getSuperclass();
        if(superClass != null && recursively) {
        	List<Field> declaredFieldsOfSuper = getDeclaredFields(superClass, recursively);
        	fields.addAll(declaredFieldsOfSuper);
        }

        return fields;
    }

	List<ParameterizedTypeName> listExtends(Class<?> entityClass, TypeName idType) {
		List<ParameterizedTypeName> ret = new ArrayList<>();
		TypeName entityTypeName = TypeName.get(entityClass);

		ClassName repositoryClass = ClassName.bestGuess(JPA_REPOSITORY);
		ParameterizedTypeName paramType = ParameterizedTypeName.get(repositoryClass, entityTypeName, idType);
		ret.add(paramType);

		repositoryClass = ClassName.bestGuess(JPA_SPECIFICATION_EXECUTOR);
		paramType = ParameterizedTypeName.get(repositoryClass, entityTypeName);
		ret.add(paramType);
		
		if (interfaces != null && !interfaces.isEmpty()) {
			for(String intf : interfaces) {
				repositoryClass = ClassName.bestGuess(intf);
				paramType = ParameterizedTypeName.get(repositoryClass, entityTypeName, idType);
				ret.add(paramType);
			}
		}
		
		return ret;
	}
	
	JavaFile makeJavaFile(TypeSpec repositoryTypeSpec) {
		JavaFile javaFile = JavaFile.builder(repositoryPackage, repositoryTypeSpec).build();
		return javaFile;
	}
	
	
	
	
	
	
	void writeTo(JavaFile javaFile) throws IOException {
		javaFile.writeTo(outputDir);
	}
	

	void bye() {
		log.info("bye");
	}
	
	public static void main(String ... args) throws IOException, ClassNotFoundException {
		HibernateRepositoryGen bean = new HibernateRepositoryGen();
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
