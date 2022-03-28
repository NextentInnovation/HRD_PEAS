package hu.nextent.peas.service.period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.DatabaseInfoRepository;
import hu.nextent.peas.model.CreatePeriodModel;
import hu.nextent.peas.model.PeriodModel;
import hu.nextent.peas.model.PeriodPageModel;
import hu.nextent.peas.model.PeriodQueryParameterModel;
import hu.nextent.peas.service.PeriodService;
import lombok.val;

@Service
@Transactional
public class PeriodServiceImp
implements PeriodService
{

	@Autowired private CreatAndModifyPeriodDecorator creatAndModifyPeriodDecorator;
	@Autowired private GeneratePeriodTemplateDecorator generatePeriodTemplateDecorator;
	@Autowired private GetPeriodDecorator getPeriodDecorator;
	@Autowired private QueryPeriodDecorator queryPeriodDecorator;
	@Autowired private DatabaseInfoRepository databaseInfoRepository;
	
	@Override
	public ResponseEntity<PeriodModel> createPeriod(CreatePeriodModel body) {
		val ret = creatAndModifyPeriodDecorator.createPeriod(body);
		databaseInfoRepository.flush();
		return ret;
	}

	@Override
	public ResponseEntity<Void> deletePeriod(Long periodId) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ResponseEntity<PeriodModel> getPeriod(Long periodId) {
		val ret = getPeriodDecorator.getPeriod(periodId);
		databaseInfoRepository.flush();
		return ret;
	}

	@Override
	public ResponseEntity<PeriodModel> modifyPeriod(PeriodModel body, Long periodId) {
		val ret = creatAndModifyPeriodDecorator.modifyPeriod(body, periodId);
		databaseInfoRepository.flush();
		return ret;
	}

	@Override
	public ResponseEntity<CreatePeriodModel> periodTemplate() {
		val ret = generatePeriodTemplateDecorator.periodTemplate();
		databaseInfoRepository.flush();
		return ret;
	}

	@Override
	public ResponseEntity<PeriodPageModel> queryPeriod(PeriodQueryParameterModel body) {
		val ret = queryPeriodDecorator.queryPeriod(body);
		databaseInfoRepository.flush();
		return ret;
	}

}
