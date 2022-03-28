package hu.nextent.peas.scheduler;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import hu.nextent.peas.cache.ServiceCaches;
import hu.nextent.peas.constant.ServiceConstant;
import hu.nextent.peas.jpa.entity.Company;
import lombok.val;

@Component
public class SchedulerServiceHelper {
	
	@Autowired
	private ConversionService conversionService;
	
	@Autowired
	private ServiceCaches serviceCaches;
	
	@SuppressWarnings("unchecked")
	private List<Integer> getCompanyParam(String key, Long companyId) {
		String value = serviceCaches.getCompanyParam(key, companyId);
		if (value == null) {
			return Collections.emptyList();
		} else {
			val result = (List<Integer>)conversionService.convert(
					value
					, TypeDescriptor.valueOf(String.class)
					, TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Integer.class))
					);
			Collections.sort(result);
			return result;
		}
	}
	
	private List<Integer> getCompanyParam(String key, Company company) {
		return getCompanyParam(key, company == null ? null : company.getId());
	}
	
    public List<Integer> getDeadlineEvaluationWarningDays(Company company) {
    	return getCompanyParam(ServiceConstant.DEADLINE_EVALUATION_NOTIFICATION_WARNING, company);
    }

    public List<Integer> getDeadlinePeriodWarningDays(Company company) {
    	return getCompanyParam(ServiceConstant.DEADLINE_PERIOD_NOTIFICATION_WARNING, company);
    }
    
    public List<Integer> getDeadlineTaskWarningDays(Company company) {
    	return getCompanyParam(ServiceConstant.DEADLINE_TASK_NOTIFICATION_WARNING, company);
    }

    public Integer getDeadlineAfterTaskWarningDays(Company company) {
    	return serviceCaches.getCompanyParam(ServiceConstant.DEADLINE_TASK_NOTIFICATION_AFTER_WARNING, Integer.class, company);
    }

    public List<Integer> getDeadlineRatingWarningDays(Company company) {
    	return getCompanyParam(ServiceConstant.DEADLINE_RATING_NOTIFICATION_WARNING, company);
    }
}
