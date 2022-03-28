package hu.nextent.peas.batch;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.email.NotificationEmailService;
import hu.nextent.peas.jpa.dao.BatchEventRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.entity.BatchEvent;
import hu.nextent.peas.jpa.entity.BatchStatusEnum;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationSendStatusEnum;

@Component
@StepScope
@Transactional
public class SendNotificationProcessor implements ItemProcessor<BatchEvent, BatchEvent> {

	@Autowired
	private BatchEventRepository batchEventRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private NotificationEmailService notificationEmailService;
	
	private static List<NotificationSendStatusEnum> SENDABLE_STATUS = Arrays.asList(NotificationSendStatusEnum.NEW, NotificationSendStatusEnum.BEFORE_SEND);
	
	@Override
	public BatchEvent process(BatchEvent evt) throws Exception {
		// Lekérdezi a notifikációt
		Optional<Notification> optNotification = getNotification(evt);
		// Végrehajthatja a küldést ?
		if (isProcessable(optNotification)) {
			// Előkészíti a Notifikációt a küldésre
			before(evt, optNotification);
			try {
				// Notifikáció küldése email-be
				notificationEmailService.sendNotificationEmail(optNotification.get(), null, false);
			} catch (Exception e) {
				// Hiba esetén a BatchEvent-et hibára állítja
				after(evt, optNotification, e);
				return evt;
			}
		}
		// Visszaírja az eredményt az event-be
		after(evt, optNotification, null);
		return evt;
	}
	
	/**
	 * Lekérdezi az adott eseményhez tartozó notifikációt
	 * @param evt: BatchEvent
	 * @return: Opcionális notifikáció
	 */
	private Optional<Notification> getNotification(BatchEvent evt) {
		Long id = evt.getParameterId();
		return notificationRepository.findById(id);
	}

	/**
	 * Akkor végrehajtható a Notifikáció küldés, ha NEW státuszban van a küldés státusza
	 * @param optNotification Notifikáció
	 * @return
	 */
	private boolean isProcessable(Optional<Notification> optNotification) {
		if (!optNotification.isPresent()) {
			return false;
		}
		return SENDABLE_STATUS.contains(optNotification.get().getSendedStatus());
	}
	
	/**
	 * Mielött küldené azelött a Notifikáció státuszát beállítja BEFORE_SEND-be
	 * @param evt: BatchEvnt
	 * @param optNotification Notifikáció
	 */
	private void before(BatchEvent evt, Optional<Notification> optNotification) {
		if (optNotification.isPresent()) {
			optNotification.get().setSendedError(null);
			if (!NotificationSendStatusEnum.BEFORE_SEND.equals(optNotification.get().getSendedStatus())) {
				optNotification.get().setSendedStatus(NotificationSendStatusEnum.BEFORE_SEND);
			}
			notificationRepository.saveAndFlush(optNotification.get());
			// Bind Company
			if (evt.getCompany() == null) {
				evt.setCompany(optNotification.get().getCompany());
			}
			evt.setError(null);
			batchEventRepository.save(evt);
		}
	}
	
	/**
	 * Utófeldolgozás
	 * Ha a notifikáció küldése sikertelen, akkor a hiba státuszba állítja az eseményt
	 * @param evt: BatchEvent
	 * @param optNotification Notifikáció
	 */
	private void after(
			BatchEvent evt, 
			Optional<Notification> optNotification,
			Exception ex
			) {
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		evt.setDone(now);
		if (ex != null) {
			evt.setStatus(BatchStatusEnum.ERROR);
			evt.setError(StringUtils.left(ex.getMessage(), 1000));
		} else {
			if (optNotification.isPresent()) {
				if (NotificationSendStatusEnum.ERROR.equals(optNotification.get().getSendedStatus())) {
					evt.setStatus(BatchStatusEnum.ERROR);
				} else {
					evt.setStatus(BatchStatusEnum.DONE);
				}
			} else {
				evt.setStatus(BatchStatusEnum.DONE);
			}
		}
		batchEventRepository.save(evt);
	}

}
