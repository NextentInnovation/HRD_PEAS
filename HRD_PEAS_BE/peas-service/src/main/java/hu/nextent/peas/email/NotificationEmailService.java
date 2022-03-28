package hu.nextent.peas.email;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import hu.nextent.peas.jpa.entity.Notification;

public interface NotificationEmailService {

	/**
	 * 
	 * @param notification: Küledendő üzenet
	 * @param defaultLanguage: Alapértelmezett nyelv, nem kötelező, ha nincs megadva, akkor a notification alapján keresi a nyelvet
	 * @param reloadNotification: reloadNotification=true esetén Async Mód esetén a notifikációt újra beolvassa az adatbáziosból
	 */
	public void sendNotificationEmail(
			@NotNull Notification notification,
			@Nullable String defaultLanguage,
			@Nullable Boolean reloadNotification
			);
	
}
