package hu.nextent.peas.service.report;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.model.ReportPeriodListModel;
import hu.nextent.peas.model.ReportPeriodModel;


@Service
@Transactional
public class ReportQueryPeriodDecorator 
extends AbstractReportDecorator {

	public final static List<PeriodStatusEnum> REPORT_PERIOD_STATUS = new ArrayList<>();
	static {
		REPORT_PERIOD_STATUS.add(PeriodStatusEnum.ACTIVE);
		REPORT_PERIOD_STATUS.add(PeriodStatusEnum.RATING);
		REPORT_PERIOD_STATUS.add(PeriodStatusEnum.CLOSED);
	}

	public ResponseEntity<ReportPeriodListModel> queryPeriod() {
        List<Period> periodList = periodRepository.findAllByCompanyAndStatusInOrderByEndDateDesc(getCurrentCompany(), REPORT_PERIOD_STATUS);
        if (periodList == null || periodList.isEmpty()) {
        	throw ExceptionList.no_content();
        }
        
        ReportPeriodListModel model = new ReportPeriodListModel();
        periodList.stream().map(this::convertTo).forEach(model::add);

		return ResponseEntity.ok(model);
	}
	
	private ReportPeriodModel convertTo(Period period) {
		return conversionService.convert(period, ReportPeriodModel.class);
	}
}
