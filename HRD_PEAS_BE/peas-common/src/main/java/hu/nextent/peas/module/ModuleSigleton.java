package hu.nextent.peas.module;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModuleSigleton {
	
	private static String MODULE_NAME = "build.artifactId";

	private static ModuleSigleton instance;
	
	@Getter
	private Map<String, Properties> modules = new HashMap<String, Properties>();
	
	private Set<Class<?>> checkedClass = new HashSet<Class<?>>();
	
	private ModuleSigleton() {
		
	}
	
	
	public static ModuleSigleton getInstance() {
		if (instance == null) {
			instance = new ModuleSigleton();
		}
		return instance;
	}
	
	
	public void registerModule(Class<?> moduleClass) {
		if (checkedClass.contains(moduleClass)) {
			return;
		}
		
		Properties prop = null;
		try {
			// prop.load(moduleClass.getResourceAsStream("building.properties"));
			prop = load(moduleClass);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.warn(e.getMessage());
			return;
		}
		String moduleName = prop.getProperty(MODULE_NAME);
		if (modules.containsKey(moduleName)) {
			return;
		}
		modules.put(moduleName, prop);
	}
	
	
	private Properties load(Class<?> moduleClass) throws MalformedURLException, IOException {
	      Class<?> clazz = moduleClass;
	      String className = clazz.getSimpleName() + ".class";
	      String classPath = clazz.getResource(className).toString();
	      if (!classPath.startsWith("jar")) {
	        // Class not from JAR
	        String relativePath = clazz.getName().replace('.', File.separatorChar) + ".class";
	        String classFolder = classPath.substring(0, classPath.length() - relativePath.length() - 1);
	        String propertiesPath = classFolder + "/building.properties";
	        log.debug("path={}", propertiesPath);
	        return read(propertiesPath);
	      } else {
	        String propertiesPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/building.properties";
	        log.debug("path={}", propertiesPath);
	        return read(propertiesPath);
	      }
	}
	
	
	private Properties read(String manifestPath) throws MalformedURLException, IOException {
		Properties prop = new Properties();
		prop.load(new URL(manifestPath).openStream());
		return prop;
	}
}
