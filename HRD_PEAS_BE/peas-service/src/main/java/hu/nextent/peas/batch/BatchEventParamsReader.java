package hu.nextent.peas.batch;

import java.util.Collections;
import java.util.Iterator;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.BatchEventRepository;
import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.entity.BatchEvent;
import hu.nextent.peas.jpa.entity.BatchEventTypeEnum;
import hu.nextent.peas.jpa.entity.Company;

@Component
@Transactional
@StepScope
public class BatchEventParamsReader
implements ItemReader<BatchEvent>, StepExecutionListener
{
	public static int PAGE_SIZE = 10;
	
	@Autowired
	private BatchEventRepository batchEventRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	private StepExecution stepExecution;
	
	@Value("#{jobParameters[companyId]}")
	private Long companyId;
	@Value("#{jobParameters[batchEventTypeName]}")
	private String batchEventTypeName;
	
	private Company company;
	private BatchEventTypeEnum batchEventType;
	
	private Page<BatchEvent> pageBatchEvent;
	private Iterator<BatchEvent> itBatchEvent;

    public void prepare() {
    	this.companyId = stepExecution.getJobParameters().getLong("companyId");
    	this.batchEventTypeName = stepExecution.getJobParameters().getString("batchEventType");
    	
        if (batchEventTypeName == null) {
			throw new RuntimeException("BatchEventType is null");
		}
        
        batchEventType = BatchEventTypeEnum.valueOf(batchEventTypeName);
        
		if (companyId == null || companyId == -1) {
			company = null;
		} else {
			company = companyRepository.getOne(companyId);
		}
		
		itBatchEvent = Collections.emptyIterator();
	}
	

	
	@Override
	public BatchEvent read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		prepare();
		
		if (!itBatchEvent.hasNext()) {
			if (!nextPageLoad()) {
				return null;
			}
		}
		
		return itBatchEvent.next();
	}

	private boolean nextPageLoad() {
		Pageable pageable = PageRequest.of(0, PAGE_SIZE);
		pageBatchEvent = null;
		if (company == null) {
			pageBatchEvent = batchEventRepository.findAllByBatchEventTypeAndDoneIsNullOrderByIdDesc(batchEventType, pageable);
		} else {
			pageBatchEvent = batchEventRepository.findAllByCompanyAndBatchEventTypeAndDoneIsNullOrderByIdDesc(company, batchEventType, pageable);
		}
		if (pageBatchEvent.getContent().isEmpty()) {
			itBatchEvent = Collections.emptyIterator();
			return false;
		} else {
			itBatchEvent = pageBatchEvent.iterator();
			return true;
		}
	}



	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return stepExecution.getExitStatus();
	}

}
