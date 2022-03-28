package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.model.TaskItemModel;

@Component
public class TaskItemModelFromTaskConverter extends AbstractTaskModelConverter<Task, TaskItemModel> {

	@Override
	public TaskItemModel convert(Task source) {
		return convertToTaskItemModel(source);
	}
	

}
