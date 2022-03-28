package hu.nextent.peas.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.nextent.peas.cache.ServiceCaches;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.model.NotificationModel;
import hu.nextent.peas.model.ReferenceTypeEnumModel;
import hu.nextent.peas.utils.SpelFormater;
import hu.nextent.peas.utils.UserServiceHelper;
import lombok.val;

@Component
public class NotificationModelConverter 
extends AbstractConverter<Notification, NotificationModel>
{
	@Autowired
	private UserServiceHelper userServiceHelper;
	
	@Autowired
	private ServiceCaches serviceCaches;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Override
	public NotificationModel convert(Notification notification) {
		
		formatIfNeeded(notification);
		
		val model = new NotificationModel();
		model
			.id(notification.getId())
			.subject(notification.getSubject())
			.body(notification.getBody())
			.createdDate(notification.getCreatedDate())
			.notificationType(notification.getNotificationType().name())
			.status(notification.getStatus().name())
			.readed(notification.getReaded() != null)
			.referenceType(ReferenceTypeEnumModel.valueOf(notification.getReferenceType().name()))
		;
		
		switch (notification.getReferenceType()) {
			case TASK:
				model.setReference(notification.getTask().getId());
				break;
			case EVALUATION:
				model.setReference(notification.getEvaluation().getId());
				break;
			case RATING:
				model.setReference(notification.getRating().getId());
				break;
			case LEADERVIRTUE:
				model.setReference(notification.getLeaderVirtue().getId());
				break;
			case PERIOD:
				model.setReference(notification.getPeriod().getId());
				break;
			default:
				break;
		}
		
		return model;
	}

	private void formatIfNeeded(Notification notification) {
		if (!notification.getNeedGenerate()) {
			return;
		}
		String lang = userServiceHelper.getLanguage();
		notification.setSubject(format(lang, notification.getSubject(), notification));
		notification.setBody(format(lang, notification.getBody(), notification));
		notification.setNeedGenerate(false);
		notificationRepository.save(notification);
		
	}
	
	private String format(String lang, String template, Notification notification) {
		String message = serviceCaches.getLabel(template, lang);
		String formated = SpelFormater.format(
				message, 
				notification,
				serviceCaches.labelMapProxy(lang)
				);
		return formated;
	}
	
}
