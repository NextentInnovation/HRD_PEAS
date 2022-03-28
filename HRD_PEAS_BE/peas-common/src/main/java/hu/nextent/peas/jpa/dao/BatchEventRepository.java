package hu.nextent.peas.jpa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import hu.nextent.peas.jpa.entity.BatchEvent;
import hu.nextent.peas.jpa.entity.BatchEventTypeEnum;
import hu.nextent.peas.jpa.entity.Company;

public interface BatchEventRepository extends DaoRepository<BatchEvent, Long> {

	public Page<BatchEvent> findAllByBatchEventTypeAndDoneIsNullOrderByIdDesc(BatchEventTypeEnum batchEventType, Pageable pageable);
	public Page<BatchEvent> findAllByCompanyAndBatchEventTypeAndDoneIsNullOrderByIdDesc(Company company, BatchEventTypeEnum batchEventType, Pageable pageable);
	
}
