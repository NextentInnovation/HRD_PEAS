package hu.nextent.peas.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * Megpróbálja kiküldeni a leveleket, úgy, hogy feladja a batch elemek közé a 
 * notifikációkat
 * 
 * @author peter.tamas
 *
 */
@Slf4j
@Component
@Transactional
public class SendNotificationScheduler {
	
	@Autowired
	private BatchStarterService batchStarterService;

	@Scheduled(fixedDelay = 300000) // 5 percenként
	public void scheduleSendNotification() {
		log.debug("SendNotificationScheduler");
		batchStarterService.startNotificationSend(true);
		log.debug("SendNotificationScheduler, end");
	}
	
	
}
