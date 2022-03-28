package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.model.TaskModel;

@Component
public class TaskModelConverter extends AbstractTaskModelConverter<Task, TaskModel> {

	@Override
	public TaskModel convert(Task source) {
		return convertToTaskModel(source);
	}

}
