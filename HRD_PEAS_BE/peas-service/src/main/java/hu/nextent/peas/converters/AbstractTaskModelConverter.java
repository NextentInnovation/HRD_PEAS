package hu.nextent.peas.converters;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskXCompanyVirtue;
import hu.nextent.peas.jpa.entity.TaskXFactor;
import hu.nextent.peas.jpa.entity.TaskXLeaderVirtue;
import hu.nextent.peas.model.CompanyVirtueModel;
import hu.nextent.peas.model.DifficultyModel;
import hu.nextent.peas.model.LeaderVirtueModel;
import hu.nextent.peas.model.PeriodModel;
import hu.nextent.peas.model.TaskEvaluationListModel;
import hu.nextent.peas.model.TaskEvaluationModel;
import hu.nextent.peas.model.TaskFactorModel;
import hu.nextent.peas.model.TaskItemModel;
import hu.nextent.peas.model.TaskModel;
import hu.nextent.peas.model.TaskSimpleModel;
import hu.nextent.peas.model.UserModel;
import hu.nextent.peas.model.UserSimpleModel;
import lombok.val;

public abstract class AbstractTaskModelConverter<S extends Task, T> 
extends AbstractConverter<S, T>
{

	

	protected TaskSimpleModel convertToTaskSimpleModel(Task task) {
		val model = new TaskSimpleModel();
		fillTaskSimpleModel(model, task);
		return model;
	}
	
	protected TaskModel convertToTaskModel(Task task) {
		val model = new TaskModel();
		fillTaskSimpleModel(model, task);
		fillTaskModel(model, task);
		return model;
	}
	
	protected TaskItemModel convertToTaskItemModel(Task task) {
		val model = new TaskItemModel();
		fillTaskSimpleModel(model, task);
		fillTaskItemModel(model, task);
		return model;
	}

	private void fillTaskSimpleModel(TaskSimpleModel model, Task task) {
		/*
		id : Long
		taskType : String
		taskStatus : String
		name : String
		description : String
		owner : UserSimpleModel
		difficulty : DifficultyModel
		*/

		model				
			.id(task.getId())
			.taskType(task.getTaskType().name())
			.status(task.getStatus().name())
			.name(task.getName())
			.description(task.getDescription())
			.owner(conversionService.convert(task.getOwner(), UserModel.class))
			.difficulty(conversionService.convert(task.getDifficulty(), DifficultyModel.class))
		;
	}
	
	private void fillTaskModel(TaskModel model, Task task) {
		val evalList = new TaskEvaluationListModel();
		List<Evaluation> evaluations = Optional.ofNullable(task.getEvaluations()).orElse(Collections.emptyList());
		// Név alapján sorbarendezés
		evaluations.sort(new Comparator<Evaluation>() {

			@Override
			public int compare(Evaluation arg0, Evaluation arg1) {
				return arg0.getEvaluator().getUserName().compareToIgnoreCase(arg1.getEvaluator().getUserName());
			}
			
		});
		for(Evaluation eval: evaluations) {
			val evaluation = new TaskEvaluationModel()
					.evaluator(conversionService.convert(eval.getEvaluator(), UserSimpleModel.class))
					.status(TaskEvaluationModel.StatusEnum.valueOf(eval.getStatus().name()))
					.score(eval.getScore() == null ? 0.0 : eval.getScore().doubleValue())
					;
			evalList.add(evaluation);
		}

		model
			.createdDate(task.getCreatedDate())
			.deadline(task.getDeadline() == null ? null : task.getDeadline().toInstant().atOffset(ZoneOffset.UTC))
			.companyVirtues(
					task.getTaskXCompanyVirtues()
						.stream()
						.map(TaskXCompanyVirtue::getCompanyVirtue)
						.map(p -> conversionService.convert(p, CompanyVirtueModel.class))
						.collect(Collectors.toList())
					)
			.leaderVirtues(
					task.getTaskXLeaderVirtues()
						.stream()
						.map(TaskXLeaderVirtue::getLeaderVirtue)
						.map(p -> conversionService.convert(p, LeaderVirtueModel.class))
						.collect(Collectors.toList())
					)
			.taskfactors(
					task.getTaskXFactors()
						.stream()
						.map(this::to)
						.collect(Collectors.toList())
				)
			.evaluators(evalList)
			.startDate(task.getStartDate())
			.endDate(task.getEndDate())
			.evaluationPercentage(Optional.ofNullable(task.getEvaluationPercentage()).orElse(BigDecimal.ZERO).doubleValue())
			.score(Optional.ofNullable(task.getScore()).orElse(BigDecimal.ZERO).doubleValue())
			.period(task.getPeriod() == null ? null : conversionService.convert(task.getPeriod(), PeriodModel.class))
		;
	}

	
	private TaskFactorModel to(TaskXFactor taskXFactor) {
		val result = new TaskFactorModel();
		result.required(taskXFactor.getRequired());
		result
			.id(taskXFactor.getFactor().getId())
			.name(taskXFactor.getFactor().getName())
			;
		return result;
	}

	private void fillTaskItemModel(TaskItemModel model, Task task) {
		model
			.createdDate(task.getCreatedDate())
			.deadline(task.getDeadline())
			.startDate(task.getStartDate())
			.endDate(task.getEndDate())
			.evaluationPercentage(Optional.ofNullable(task.getEvaluationPercentage()).orElse(BigDecimal.ZERO).doubleValue())
			.score(Optional.ofNullable(task.getScore()).orElse(BigDecimal.ZERO).doubleValue())
		;
		
	}
}
