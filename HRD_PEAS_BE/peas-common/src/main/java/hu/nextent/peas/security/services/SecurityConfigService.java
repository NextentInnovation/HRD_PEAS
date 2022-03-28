package hu.nextent.peas.security.services;

public interface SecurityConfigService {

	public Integer expireMinute();
	
	public String secret();
	
	public boolean remoteAddressCheck();
	
	public String defaultCompany();
	
}
