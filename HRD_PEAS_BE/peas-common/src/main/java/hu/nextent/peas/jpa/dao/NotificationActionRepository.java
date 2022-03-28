package hu.nextent.peas.jpa.dao;

import java.util.List;
import java.util.Optional;

import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.NotificationAction;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;

public interface NotificationActionRepository extends DaoRepository<NotificationAction, Long>{

	
	Optional<NotificationAction> findByNotificationTypeAndCompanyIsNull(NotificationTypeEnum notificationType);
	Optional<NotificationAction> findByNotificationTypeAndCompany_id(NotificationTypeEnum notificationType, Long companyId);
	
	List<NotificationAction> findAllByCompany(Company company);
	List<NotificationAction> findAllByCompanyIsNull();
}
