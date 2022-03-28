package hu.nextent.peas.jpa.utils;

import lombok.Data;

public class LoggedPlacholder {

    private static final ThreadLocal<ValueHolder> userHolder = new ThreadLocal<>();

    public static void login(Long companyId, String userName) {
        userHolder.set(new ValueHolder(companyId, userName));
    }

    public static void logout() {
        userHolder.remove();
    }

    public static String getUserName() {
    	return userHolder.get() == null ? null : userHolder.get().getUserName();
    }
    
    public static Long getCompanyId() {
    	return userHolder.get() == null ? null : userHolder.get().getCompanyId();
    }
    
}

@Data
class ValueHolder {
	
	private String userName;
	private Long companyId;
	
	ValueHolder(Long companyId, String userName) {
		this.userName = userName;
		this.companyId = companyId;
	}
	
	ValueHolder() {
	}
}
