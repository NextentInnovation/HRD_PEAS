package hu.nextent.peas.jpa.dao;

import java.sql.Timestamp;
import java.util.Date;

public interface DatabaseInfoRepository {

    String SELECT_NOW = "SELECT current_timestamp()";

    String SELECT_CURRENT_DATE = "SELECT current_date()";
    
	Timestamp getCurrentTimestamp();

    Date getCurrentDate();

    void flush();
    
}
