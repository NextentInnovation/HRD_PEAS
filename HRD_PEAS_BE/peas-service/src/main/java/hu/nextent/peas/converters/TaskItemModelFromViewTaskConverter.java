package hu.nextent.peas.converters;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.view.ViewTask;
import hu.nextent.peas.model.DifficultyModel;
import hu.nextent.peas.model.TaskItemModel;
import hu.nextent.peas.model.UserModel;
import lombok.val;

@Component
public class TaskItemModelFromViewTaskConverter extends AbstractConverter<ViewTask, TaskItemModel> {

	@Override
	public TaskItemModel convert(ViewTask source) {
		val model = new TaskItemModel();
		
		model				
			.createdDate(source.getCreatedDate())
			.deadline(source.getDeadline())
			.startDate(source.getStartDate())
			.endDate(source.getEndDate())
			.evaluationPercentage(Optional.ofNullable(source.getEvaluationPercentage()).orElse(BigDecimal.ZERO).doubleValue())
			.score(Optional.ofNullable(source.getScore()).orElse(BigDecimal.ZERO).doubleValue())
			.id(source.getTaskId())
			.taskType(source.getTaskType().name())
			.status(source.getStatus().name())
			.name(source.getName())
			.description(source.getDescription())
			.owner(conversionService.convert(source.getOwner(), UserModel.class))
			.difficulty(conversionService.convert(source.getDifficulty(), DifficultyModel.class))
			;
		
		return model;
	}
	

}
