package hu.nextent.peas.utils;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Konstansok a Spel formatálóhoz
 *
 */
public class SpelConstant {
	
	private SpelConstant() {
	}
	
	public static final MySpelDateTimeFormater HU_DATETIME = new MySpelDateTimeFormater("HU_DATETIME", DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
	public static final MySpelDateTimeFormater HU_DATE = new MySpelDateTimeFormater("HU_DATE",  DateTimeFormatter.ofPattern("yyyy.MM.dd."));
	
	public static final MySpelDateTimeFormater EN_DATETIME = new MySpelDateTimeFormater("EN_DATETIME", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	public static final MySpelDateTimeFormater EN_DATE = new MySpelDateTimeFormater("EN_DATE", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

	
	private static Map<String, Object> CONSTANT_MAP = new HashMap<String, Object>();
	private static boolean initMap = false;
	
	public static Map<String, Object> getConstantMap() {
		try {
			init();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return CONSTANT_MAP;
	}
	
	private static void init() throws IllegalArgumentException, IllegalAccessException {
		if (initMap) {
			return;
		}
		Field[] fields = SpelConstant.class.getDeclaredFields();
		SpelConstant instane = new SpelConstant();
		for(Field field : fields) {
			CONSTANT_MAP.put(field.getName(), field.get(instane));
		}
		initMap = true;
	}
	

}
