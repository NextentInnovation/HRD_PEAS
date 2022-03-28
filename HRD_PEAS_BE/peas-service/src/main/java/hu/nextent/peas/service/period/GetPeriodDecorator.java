package hu.nextent.peas.service.period;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.model.PeriodModel;
import lombok.val;

@Service
@Transactional
public class GetPeriodDecorator extends AbstractPeriodDecorator {

	
	public ResponseEntity<PeriodModel> getPeriod(Long periodId) {
		val period = validateGetPeriod(periodId);
    	return getPeriodModel(period);
    }

	private Period validateGetPeriod(Long periodId) {
		checkRight();
		checkExists(periodId);
		val period = periodRepository.getOne(periodId);
		checkCompany(period);
		return period;
	}
	
	
}
