package hu.nextent.peas.batch;

import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.BatchEventRepository;
import hu.nextent.peas.jpa.entity.BatchEvent;

@Component
@Transactional
@StepScope
public class BatchEventItemWriter implements ItemWriter<BatchEvent>{

	@Autowired
	private BatchEventRepository batchEventRepository;
	
	@Override
	public void write(List<? extends BatchEvent> items) throws Exception {
		batchEventRepository.saveAll(items);
		batchEventRepository.flush();
	}
	
}
