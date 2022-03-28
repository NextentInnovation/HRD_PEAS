package hu.nextent.peas.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.DatabaseInfoRepository;
import hu.nextent.peas.model.ReportAllEmployeeQueryParameterModel;
import hu.nextent.peas.model.ReportEmployee;
import hu.nextent.peas.model.ReportEmployeeQueryModel;
import hu.nextent.peas.model.ReportEvaluationModel;
import hu.nextent.peas.model.ReportPeriodListModel;
import hu.nextent.peas.model.ReportAllEmployeePageModel;
import hu.nextent.peas.service.ReportService;
import lombok.val;

@Service
@Transactional
public class ReportServiceImp implements ReportService {
	
	@Autowired private ReportQueryPeriodDecorator reportQueryPeriodDecorator;
	@Autowired private ReportAllEmployeeDecorator reportAllEmployeeDecorator;
	@Autowired private ReportEmployeeDecorator reportEmployeeDecorator;
	@Autowired private ReportEvaluationDecorator reportEvaluationDecorator;
	@Autowired private DatabaseInfoRepository databaseInfoRepository;

	@Override
	public ResponseEntity<ReportPeriodListModel> queryPeriod() {
		val result = reportQueryPeriodDecorator.queryPeriod();
		databaseInfoRepository.flush();
		return result;
	}

	@Override
	public ResponseEntity<ReportAllEmployeePageModel> reportAllEmployee(ReportAllEmployeeQueryParameterModel body) {
		val result = reportAllEmployeeDecorator.reportAllEmployee(body);
		databaseInfoRepository.flush();
		return result;
	}

	@Override
	public ResponseEntity<ReportEmployee> reportEmployee(ReportEmployeeQueryModel body) {
		val result = reportEmployeeDecorator.reportEmployee(body);
		databaseInfoRepository.flush();
		return result;
	}

	@Override
	public ResponseEntity<ReportEvaluationModel> reportEvaluation(Long taskId) {
		val result = reportEvaluationDecorator.reportEvaluation(taskId);
		databaseInfoRepository.flush();
		return result;
	}

}
