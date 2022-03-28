package hu.nextent.peas.service.period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.facades.PeriodGenerator;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.model.CreatePeriodModel;

@Service
@Transactional
public class GeneratePeriodTemplateDecorator extends AbstractPeriodDecorator {
	
	@Autowired
	private PeriodGenerator periodGenerator;
	
	public ResponseEntity<CreatePeriodModel> periodTemplate() {
		
		Period period = periodGenerator.nextPeriodGenerator(getCurrentCompany());
		
		CreatePeriodModel model = new CreatePeriodModel();
		model.setStartDate(period.getStartDate());
		model.setEndDate(period.getEndDate());
		model.setRatingEndDate(period.getRatingEndDate());
		model.setName(period.getName());
		
		return ResponseEntity.ok(model);
	}
	
	
}
