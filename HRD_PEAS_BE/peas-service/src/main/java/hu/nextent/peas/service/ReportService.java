package hu.nextent.peas.service;

import org.springframework.http.ResponseEntity;

import hu.nextent.peas.model.ReportAllEmployeeQueryParameterModel;
import hu.nextent.peas.model.ReportEmployee;
import hu.nextent.peas.model.ReportEmployeeQueryModel;
import hu.nextent.peas.model.ReportEvaluationModel;
import hu.nextent.peas.model.ReportPeriodListModel;
import hu.nextent.peas.model.ReportAllEmployeePageModel;

public interface ReportService {
	
    ResponseEntity<ReportPeriodListModel> queryPeriod();
    
    ResponseEntity<ReportAllEmployeePageModel> reportAllEmployee(ReportAllEmployeeQueryParameterModel body);
    
    ResponseEntity<ReportEmployee> reportEmployee(ReportEmployeeQueryModel body);
    
    ResponseEntity<ReportEvaluationModel> reportEvaluation(Long taskId);
}
