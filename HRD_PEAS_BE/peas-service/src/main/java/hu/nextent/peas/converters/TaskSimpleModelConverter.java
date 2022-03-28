package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.model.TaskSimpleModel;

@Component
public class TaskSimpleModelConverter extends AbstractTaskModelConverter<Task, TaskSimpleModel>{

	@Override
	public TaskSimpleModel convert(Task source) {
		return convertToTaskSimpleModel(source);
	}

}
