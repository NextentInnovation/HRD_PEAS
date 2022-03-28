package hu.nextent.peas.security.services;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import hu.nextent.peas.jpa.dao.SystemParameterRepository;
import hu.nextent.peas.jpa.entity.SystemParameter;
import hu.nextent.peas.security.constant.SecurityConstants;

@Service
@Transactional
@Validated
public class SecurityConfigServiceImpl 
	implements SecurityConfigService
	{

	private static LoadingCache<String, String> cahceSystemParameters = null;

	@Autowired
    private SystemParameterRepository systemParameterRepository;

	@Autowired 
	private ConversionService conversionService;
	
	@PostConstruct
    public void init() {
    	createParameterCache();
    }
    
	
	private void createParameterCache() {
    	if (cahceSystemParameters == null) {
    		cahceSystemParameters = 
    				CacheBuilder
    					.newBuilder()
    					.maximumSize(1000)
    				    .expireAfterWrite(30, TimeUnit.MINUTES)
    				    .build(CacheLoader.from(this::loadSystemParameter))
				;
    	}
    }
	
	private String loadSystemParameter(String key) {
		Optional<SystemParameter> optParam = systemParameterRepository.findByCode(key);
	    return optParam.isPresent() ? optParam.get().getValue() : null;
	}
	
	private <T> T getSystemParam(String key, Class<T> targetClass) {
    	String value = getSystemParam(key);
    	return value == null ? null : conversionService.convert(value, targetClass);
    }

    private String getSystemParam(String key) {
    	try {
			return cahceSystemParameters.get(key);
		} catch (ExecutionException e) {
			return null;
		}
    }
	

	@Override
	public Integer expireMinute() {
		return getSystemParam(SecurityConstants.USER_EXPIRE_MINUTE, Integer.class);
	}
	
	@Override
	public boolean remoteAddressCheck() {
		return getSystemParam(SecurityConstants.REMOTE_ADDRESS_CHECK, Boolean.class);
	}

	@Override
	public String defaultCompany() {
		return getSystemParam(SecurityConstants.DEFAULT_COMPANY, String.class);
	}

	@Override
	public String secret() {
		return getSystemParam(SecurityConstants.SECRET, String.class);
	}


}
