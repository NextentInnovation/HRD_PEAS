package hu.nextent.peas.batch;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.BatchEventRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.entity.BatchEvent;
import hu.nextent.peas.jpa.entity.BatchEventTypeEnum;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationSendStatusEnum;


@Service
@Transactional
public class BatchEventGeneratorService {

	@Autowired
	private BatchEventRepository batchEventRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	
    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public void generateNotificationSend() {
    	innerGenerateNotificationSend(null);
    }
	
    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public void generateNotificationSend(@NotNull Integer limit) {
    	innerGenerateNotificationSend(limit);
	}
    
    /**
     * Küldhető notifikációk generálása
     * @param limit
     */
    private void innerGenerateNotificationSend(@Nullable Integer limit) {
		List<Notification> notifications = limit == null 
				? notificationRepository.findAllSendable()
				: notificationRepository.findAllSendable(PageRequest.of(0, limit));
		List<BatchEvent> batchEvents =
				notifications.stream()
				.map(
					n -> BatchEvent.builder()
							.batchEventType(BatchEventTypeEnum.NOTIFICATION_SEND)
							.parameterId(n.getId())
							.company(n.getCompany())
							.build()
					)
				.collect(Collectors.toList());
		notifications.forEach(n -> n.setSendedStatus(NotificationSendStatusEnum.BEFORE_SEND));
		notificationRepository.saveAll(notifications);
		batchEventRepository.saveAll(batchEvents);
		batchEventRepository.flush();
	}
}
