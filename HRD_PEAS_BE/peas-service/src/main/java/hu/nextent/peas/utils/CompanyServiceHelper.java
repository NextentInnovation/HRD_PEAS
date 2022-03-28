package hu.nextent.peas.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.cache.ServiceCaches;
import hu.nextent.peas.constant.ServiceConstant;
import hu.nextent.peas.jpa.utils.LoggedPlacholder;

@Service
@Transactional
public class CompanyServiceHelper {

    @Autowired
    private ServiceCaches serviceCaches;

    @Autowired
    private ConversionService conversionService;

    public String getCompanyParam(String key) {
    	return serviceCaches.getCompanyParam(key, LoggedPlacholder.getCompanyId());
    }

    public <T> T getCompanyParam(String key, Class<T> targetClass) {
    	String value = getCompanyParam(key);
    	return value == null ? null : conversionService.convert(value, targetClass);
    }

    public Integer getPageSize() {
    	return getCompanyParam(ServiceConstant.PAGE_SIZE, Integer.class);
    }
    
    public Integer getTaskFactorMin() {
    	return getCompanyParam(ServiceConstant.TASK_EDITOR_FACTOR_MIN, Integer.class);
    }

    public Integer getTaskFactorMax() {
    	return getCompanyParam(ServiceConstant.TASK_EDITOR_FACTOR_MAX, Integer.class);
    }

    public Integer getTaskFactorReqMin() {
    	return getCompanyParam(ServiceConstant.TASK_EDITOR_FACTOR_REQUIRED_MIN, Integer.class);
    }

    public Integer getTaskCompanyVirtueMin() {
    	return getCompanyParam(ServiceConstant.TASK_EDITOR_COMPANY_VIRTUE_MIN, Integer.class);
    }

    public Integer getTaskCompanyVirtueMax() {
    	return getCompanyParam(ServiceConstant.TASK_EDITOR_COMPANY_VIRTUE_MAX, Integer.class);
    }

    public Integer getTaskLeaderVirtueMin() {
    	return getCompanyParam(ServiceConstant.TASK_EDITOR_LEADER_VIRTUE_MIN, Integer.class);
    }

    public Integer getTaskLeaderVirtueMax() {
    	return getCompanyParam(ServiceConstant.TASK_EDITOR_LEADER_VIRTUE_MAX, Integer.class);
    }

    public Integer getTaskUserMin() {
    	return getCompanyParam(ServiceConstant.TASK_EDITOR_USER_MIN, Integer.class);
    }

    public Integer getTaskUserMax() {
    	return getCompanyParam(ServiceConstant.TASK_EDITOR_USER_MAX, Integer.class);
    }

    public Integer getEvaluationExpiredDay() {
    	return getCompanyParam(ServiceConstant.DEADLINE_EVALUATION_EXIPRED, Integer.class);
    }

    public String getDeaultLanguage() {
    	return getCompanyParam(ServiceConstant.DEFAULT_LANGUAGE, String.class);
    }
    
    
	
    public String getDeadlineTaskWarningDays(Long companyId) {
    	return serviceCaches.getCompanyParam(ServiceConstant.DEADLINE_TASK_NOTIFICATION_WARNING, String.class, companyId);
    }

    public Integer getDeadlineTaskDays(Long companyId) {
    	return serviceCaches.getCompanyParam(ServiceConstant.DEADLINE_TASK_WARNING, Integer.class, companyId);
    }

    public String getDeadlineEvaluationWarningDays(Long companyId) {
    	return serviceCaches.getCompanyParam(ServiceConstant.DEADLINE_EVALUATION_NOTIFICATION_WARNING, String.class, companyId);
    }

    public String getDeadlineRatingWarningDays(Long companyId) {
    	return serviceCaches.getCompanyParam(ServiceConstant.DEADLINE_RATING_NOTIFICATION_WARNING, String.class, companyId);
    }

    public Integer getRatingExpiredDay() {
    	return getCompanyParam(ServiceConstant.DEADLINE_RATING_EXIPRED, Integer.class);
    }

    public Integer getPeriodRatingDay() {
    	return getCompanyParam(ServiceConstant.PERIOD_RATING_DAY, Integer.class);
    }

    public Integer getPeriodMinimalDay() {
    	return getCompanyParam(ServiceConstant.PERIOD_MINIMAL_RANGE_DAYS, Integer.class);
    }

    public String getDeadlinePeriodWarningDays(Long companyId) {
    	return serviceCaches.getCompanyParam(ServiceConstant.DEADLINE_PERIOD_NOTIFICATION_WARNING, String.class, companyId);
    }


    
    public Boolean isEmailEnable(Long companyId) {
    	return serviceCaches.getCompanyParam(ServiceConstant.EMAIL_ENABLE, Boolean.class, companyId);
    }
    
    
    public Boolean isEmailTest(Long companyId) {
    	return serviceCaches.getCompanyParam(ServiceConstant.EMAIL_TEST, Boolean.class, companyId);
    }
    
    public String getEmailFrom(Long companyId) {
    	return serviceCaches.getCompanyParam(ServiceConstant.EMAIL_FROM, String.class, companyId);
    }
    
    public String getEmailTestTo(Long companyId) {
    	return serviceCaches.getCompanyParam(ServiceConstant.EMAIL_TEST_TO, String.class, companyId);
    }
    
    public String getEmailBcc(Long companyId) {
    	return serviceCaches.getCompanyParam(ServiceConstant.EMAIL_BCC, String.class, companyId);
    }
    
    public String getEmailCc(Long companyId) {
    	return serviceCaches.getCompanyParam(ServiceConstant.EMAIL_CC, String.class, companyId);
    }
    
}
