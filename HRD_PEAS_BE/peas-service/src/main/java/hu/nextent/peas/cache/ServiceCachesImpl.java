package hu.nextent.peas.cache;

import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import hu.nextent.peas.constant.CacheNames;
import hu.nextent.peas.jpa.dao.CompanyParameterRepository;
import hu.nextent.peas.jpa.dao.LabelRepository;
import hu.nextent.peas.jpa.dao.NotificationActionRepository;
import hu.nextent.peas.jpa.dao.SystemParameterRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.CompanyParameter;
import hu.nextent.peas.jpa.entity.NotificationAction;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.SystemParameter;
import hu.nextent.peas.utils.LabelMapProxy;
import lombok.val;

@Component
public class ServiceCachesImpl implements ServiceCaches {

	@Autowired
    private ConversionService conversionService;
	
    @Autowired
    private SystemParameterRepository systemParameterRepository;
    
    @Autowired
    private CompanyParameterRepository companyParameterRepository;
    
    @Autowired
    private LabelRepository labelRepository;
    
    @Autowired
    private NotificationActionRepository notificationActionRepository;
    
	@Autowired
    private ServiceCachesImpl _self;
    
    @Cacheable(value = CacheNames.SYSTEM_PARAMETER_CACHE)
    public String getSystemParam(@NotNull String key) {
    	Optional<SystemParameter> optParam = systemParameterRepository.findByCode(key);
    	
    	if (optParam.isPresent()) {
    		return optParam.get().getValue();
    	}
    	
    	return null;
    }
    
    public <T> T getSystemParam(@NotNull String key, @NotNull Class<T> targetClass) {
    	String value = _self.getSystemParam(key);
    	return value == null ? null : conversionService.convert(value, targetClass);
    }

    @Cacheable(value = CacheNames.COMPANY_PARAMETER_CACHE)
    public String getCompanyParam(@NotNull String key, @Nullable Long companyId) {
    	
    	if (companyId == null) {
    		return _self.getSystemParam(key);
    	}

    	Optional<CompanyParameter> optParam = companyParameterRepository.findByCompany_IdAndCode(companyId, key);
    	
    	if (optParam.isPresent()) {
    		return optParam.get().getValue();
    	}
    	
    	return _self.getSystemParam(key);
    }
    
    public String getCompanyParam(@NotNull String key, @Nullable Company company) {
    	return _self.getCompanyParam(key, company == null ? null : company.getId());
    }
    
    public <T> T getCompanyParam(@NotNull String key, @NotNull Class<T> targetClass, @Nullable Long companyId) {
    	String value = _self.getCompanyParam(key, companyId);
    	return value == null ? null : conversionService.convert(value, targetClass);
    }
    
    public <T> T getCompanyParam(@NotNull String key, @NotNull Class<T> targetClass, @Nullable Company company) {
    	return _self.getCompanyParam(key, targetClass, company == null ? null : company.getId());
    }
    
    @Cacheable(value = CacheNames.LABEL_CACHE)
	public String getLabel(@NotNull String code, @NotNull String language) {
    	val optLabel = labelRepository.findByLanguageAndCode(language, code);
    	if (optLabel.isPresent()) {
    		return optLabel.get().getLabel();
    	}
    	return String.format("language:%s code:%s Not Founded", language, code);
	}
    
 
    public Map<String, String> labelMapProxy(@NotNull String language) {
    	return new LabelMapProxy(language, this);
    }
    
    @Cacheable(value = CacheNames.NOTIFICATION_ACTION_CACHE)
    public @NotNull NotificationAction getNotificationAction(@NotNull NotificationTypeEnum notificationType, @Nullable Long companyId) {
    	if (companyId == null) {
    		Optional<NotificationAction> optNotificationAction = notificationActionRepository.findByNotificationTypeAndCompanyIsNull(notificationType);
    		if (optNotificationAction.isPresent()) {
    			return optNotificationAction.get();
    		}
    		return defaultNotificationAction(notificationType);
    	} else {
    		Optional<NotificationAction> optNotificationAction = notificationActionRepository.findByNotificationTypeAndCompany_id(notificationType, companyId);
    		if (optNotificationAction.isPresent()) {
    			return optNotificationAction.get();
    		}
    		
    		optNotificationAction = notificationActionRepository.findByNotificationTypeAndCompanyIsNull(notificationType);
    		
    		if (optNotificationAction.isPresent()) {
    			return optNotificationAction.get();
    		}
    		
    		return defaultNotificationAction(notificationType);
    	}
    }
    
    public @NotNull NotificationAction getNotificationAction(@NotNull NotificationTypeEnum notificationType, @Nullable Company company) {
    	return _self.getNotificationAction(notificationType, company == null ? null : company.getId());
    }

	private NotificationAction defaultNotificationAction(NotificationTypeEnum notificationType) {
		return NotificationAction.builder().notificationType(notificationType).createable(true).sendable(true).showable(true).build();
	}
}