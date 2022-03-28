package hu.nextent.peas.service.period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.model.PeriodModel;
import hu.nextent.peas.service.base.BaseDecorator;
import lombok.val;

public abstract class AbstractPeriodDecorator extends BaseDecorator {

	@Autowired protected PeriodRepository periodRepository;
	
	protected ResponseEntity<PeriodModel> getPeriodModel(Long periodId) {
		checkExists(periodId);
		val period = periodRepository.getOne(periodId);
    	return getPeriodModel(period);
	}
	
	protected ResponseEntity<PeriodModel> getPeriodModel(Period period) {
		return ResponseEntity.ok(conversionService.convert(period, PeriodModel.class));
	}
	
	protected void checkExists(Long periodId) {
		periodRepository.findById(periodId).orElseThrow(() -> ExceptionList.period_not_founded(periodId));
    }

	
	protected void checkRight() {
		if (!getCurrentUserRoleEnum().contains(RoleEnum.BUSINESS_ADMIN)) {
			// TODO jogosúltság kezeléshez
			throw new BadCredentialsException("Not Roles");
		}
    }
    
	protected void checkCompany(Period period) {
    	if (!period.getCompany().equals(getCurrentCompany())) {
    		throw ExceptionList.period_invalid_company(period.getId(), period.getCompany().getId());
    	}
    }
}
