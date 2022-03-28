package hu.nextent.peas.service.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.DatabaseInfoRepository;
import hu.nextent.peas.model.NotificationPageModel;
import hu.nextent.peas.model.NotificationQueryParameterModel;
import hu.nextent.peas.model.ToDoPageModel;
import hu.nextent.peas.model.ToDoQueryParameterModel;
import hu.nextent.peas.service.ToDoAndNotificationService;
import lombok.val;

@Service
@Transactional
public class ToDoAndNotificationServiceImp 
implements ToDoAndNotificationService {
	
	@Autowired private QueryTodoDecorator queryTodoDecorator;
	@Autowired private QueryNotificationDecorator queryNotificationDecorator;
	@Autowired private DatabaseInfoRepository databaseInfoRepository;

	@Override
	public ResponseEntity<ToDoPageModel> queryTodo(ToDoQueryParameterModel body) {
		val ret = queryTodoDecorator.queryTodo(body);
		databaseInfoRepository.flush();
		return ret;
	}

	@Override
	public ResponseEntity<NotificationPageModel> queryNotification(NotificationQueryParameterModel body) {
		val ret = queryNotificationDecorator.queryNotification(body);
		databaseInfoRepository.flush();
		return ret;
	}

}
