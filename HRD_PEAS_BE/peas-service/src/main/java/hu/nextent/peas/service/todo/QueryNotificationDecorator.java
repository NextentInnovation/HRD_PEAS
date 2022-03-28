package hu.nextent.peas.service.todo;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationAction;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Notification_;
import hu.nextent.peas.jpa.filter.Criterion;
import hu.nextent.peas.jpa.filter.ExpressionFactory;
import hu.nextent.peas.jpa.filter.SpecificationFactory;
import hu.nextent.peas.model.NotificationModel;
import hu.nextent.peas.model.NotificationPageModel;
import hu.nextent.peas.model.NotificationQueryParameterModel;
import hu.nextent.peas.model.PagingAndSortingModel.OrderEnum;
import hu.nextent.peas.model.RangeDateTimeModel;
import hu.nextent.peas.service.base.BaseDecorator;
import hu.nextent.peas.utils.PageModelConverter;
import hu.nextent.peas.utils.PageableFactory;

@Service
@Transactional
public class QueryNotificationDecorator 
extends BaseDecorator 
{

	private static Map<String, String> RENAME_FIELDS = new HashMap<String, String>();
	static {
		RENAME_FIELDS.put("createdDate", Notification_.CREATED_DATE);
		RENAME_FIELDS.put(Notification_.CREATED_DATE, Notification_.CREATED_DATE);
	};

	@Autowired
	private NotificationRepository notificationRepository;

	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseEntity<NotificationPageModel> queryNotification(NotificationQueryParameterModel body) {
		body = prepareNotificationQueryParameterModel(body);
		Pageable pageable = PageableFactory.build(getPageSize(), RENAME_FIELDS, body);

		List<NotificationTypeEnum> enableNotifTypes = enableNotificationTypes();
		
		if (enableNotifTypes == null || enableNotifTypes.isEmpty() ) {
			throw ExceptionList.no_content();
		}

		Criterion cirterion = ExpressionFactory.and(
				body.isHideReaded() 
						? ExpressionFactory.isNull(Notification_.READED) 
						: null
				, ExpressionFactory.eq(Notification_.TO_USER, getCurrentUser())
				//, ExpressionFactory.eq(Notification_.SHOW_CLIENT, true)
				, ExpressionFactory.in(Notification_.NOTIFICATION_TYPE, enableNotifTypes)
				, ExpressionFactory.eq(Notification_.COMPANY, getCurrentCompany())
				, ExpressionFactory.between(Notification_.CREATED_DATE, body.getCreatedDateRange().getMin(), body.getCreatedDateRange().getMax())
				);

		Specification<Notification> spec = SpecificationFactory.make(cirterion);

		Page<Notification> page = null;
		try {
			page = notificationRepository.findAll(spec, pageable);
		} catch (IllegalArgumentException e) {
			throw ExceptionList.page_index_under(e);
		}

		
		NotificationPageModel model = new PageModelConverter<NotificationPageModel, Notification, NotificationModel>()
				.applyPageModelClass(NotificationPageModel.class)
				.applyPage(page)
				.applyConverter(p -> conversionService.convert(p, NotificationModel.class))
				.apply();
		
		if (Boolean.TRUE.equals(body.isMarkReaded())) {
			OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
			List<Notification> notifications = notificationRepository.findAllByToUserAndReadedIsNull(getCurrentUser());
			notifications.stream()
				.peek(n -> n.setReaded(now))
				.forEach(notificationRepository::save);
		}

		if (page.getTotalElements() == 0) {
			throw ExceptionList.no_content();
		} else {
			return ResponseEntity.ok(model);
		}

	}

	
	private NotificationQueryParameterModel prepareNotificationQueryParameterModel(NotificationQueryParameterModel body) {
		if (body == null) {
			body = new NotificationQueryParameterModel();
		}
		
		if (StringUtils.isEmpty(body.getSort())) {
			body.setSort(Notification_.CREATED_DATE);
			body.setOrder(OrderEnum.DESC);
		}
		
		body.setCreatedDateRange(body.getCreatedDateRange() == null ? new RangeDateTimeModel() : body.getCreatedDateRange());
		// Alapértelmezésben, kivéve, az olvasott elemek rejtése
		body.setHideReaded(body.isHideReaded() == null ? true : body.isHideReaded());
		
		return body;
	}
	
	private List<NotificationTypeEnum> enableNotificationTypes() {
		List<NotificationAction> listCompanyNotifAction = notificationActionRepository.findAllByCompany(getCurrentCompany());
		Map<NotificationTypeEnum, Boolean> showClientNotifAction = 
				listCompanyNotifAction
					.stream()
					.collect(
							Collectors.toMap(NotificationAction::getNotificationType, NotificationAction::getShowable)
							);
		
		List<NotificationAction> listSystemNotifAction = notificationActionRepository.findAllByCompanyIsNull();
		listSystemNotifAction
			.stream()
			.forEach(na -> showClientNotifAction.putIfAbsent(na.getNotificationType(), na.getShowable()));
		
		// Egyébb elemek
		Stream.of(NotificationTypeEnum.values()).forEach(nt -> showClientNotifAction.putIfAbsent(nt, true));
		
		return showClientNotifAction.entrySet()
					.stream()
					.filter(e -> e == null || e.getValue())
					.map(e -> e.getKey())
					.collect(Collectors.toList());
	}
}
