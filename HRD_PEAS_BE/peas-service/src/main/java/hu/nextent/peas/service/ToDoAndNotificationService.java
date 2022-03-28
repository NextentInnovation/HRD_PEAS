package hu.nextent.peas.service;

import org.springframework.http.ResponseEntity;

import hu.nextent.peas.model.NotificationPageModel;
import hu.nextent.peas.model.NotificationQueryParameterModel;
import hu.nextent.peas.model.ToDoPageModel;
import hu.nextent.peas.model.ToDoQueryParameterModel;

public interface ToDoAndNotificationService {

	public ResponseEntity<ToDoPageModel> queryTodo(ToDoQueryParameterModel body);
	
	public ResponseEntity<NotificationPageModel> queryNotification(NotificationQueryParameterModel body);
	
}
