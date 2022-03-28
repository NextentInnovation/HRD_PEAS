package hu.nextent.peas.service;

import org.springframework.http.ResponseEntity;

import hu.nextent.peas.model.CreatePeriodModel;
import hu.nextent.peas.model.PeriodModel;
import hu.nextent.peas.model.PeriodPageModel;
import hu.nextent.peas.model.PeriodQueryParameterModel;

public interface PeriodService {

    public ResponseEntity<PeriodModel> createPeriod(CreatePeriodModel body);

    public ResponseEntity<Void> deletePeriod(Long periodId);

    public ResponseEntity<PeriodModel> getPeriod(Long periodId);

    public ResponseEntity<PeriodModel> modifyPeriod(PeriodModel body,Long periodId);

    public ResponseEntity<CreatePeriodModel> periodTemplate();

    public ResponseEntity<PeriodPageModel> queryPeriod(PeriodQueryParameterModel body);
}
