package hu.nextent.peas.jpa.constans;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HibernateConstant {
	
	private HibernateConstant() {}

    public static final String HIBERNATE_DATASOURCE_PASSWORD = "hibernate.datasource.password";
    public static final String HIBERNATE_DATASOURCE_USERNAME = "hibernate.datasource.username";
    public static final String HIBERNATE_DATASOURCE_DRIVER_CLASS_NAME = "hibernate.datasource.driver-class-name";
    public static final String HIBERNATE_DATASOURCE_URL = "hibernate.datasource.url";

    public static final String HIBERNATE_DIALECT = "hibernate.dialect";
    public static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    public static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    public static final String HIBERNATE_ID_NEW_GENERATOR = "hibernate.id.new_generator_mappings";
    public static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

    
    public static final String UNKNOW_USER = "<anonymus>";
    
    
    
    public static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
}
