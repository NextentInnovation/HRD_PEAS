package hu.nextent.peas.converters;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.TaskXFactor;
import hu.nextent.peas.model.EvaluationItemModel;
import hu.nextent.peas.model.EvaluationModel;
import hu.nextent.peas.model.EvaluationSelectionModel;
import hu.nextent.peas.model.FactorModel;
import hu.nextent.peas.model.FactorOptionModel;
import hu.nextent.peas.model.TaskSimpleModel;
import lombok.val;

public abstract class AbstractEvaluationModelConverter<S extends Evaluation, T> 
extends AbstractConverter<S, T> 
{
	
	protected EvaluationItemModel convertEvaluationItemModel(Evaluation eval) {
		val model = new EvaluationModel();
		fillEvaluationItemModel(model, eval);
		return model;
	}

	protected EvaluationModel convertEvaluationModel(Evaluation eval) {
		val model = new EvaluationModel();
		fillEvaluationItemModel(model, eval);
		fillEvaluationModel(model, eval);
		return model;
	}
	
	protected void fillEvaluationItemModel(EvaluationItemModel model, Evaluation eval) {
		model
			.id(eval.getId())
			.evaluatedStartDate(eval.getEvaluatedStartDate())
			.deadline(eval.getDeadline())
			.task(conversionService.convert(eval.getTask(), TaskSimpleModel.class))
			.note(eval.getNote())
			;
	}

	protected void fillEvaluationModel(EvaluationModel model, Evaluation eval) {
		/*
		private java.util.List<EvaluationSelectionModel> factors = null;
		 */
		
		for(TaskXFactor taskXFactor: eval.getTask().getTaskXFactors()) {
			val selModel = new EvaluationSelectionModel();
			selModel.factor(conversionService.convert(taskXFactor.getFactor(), FactorModel.class));
			selModel.required(taskXFactor.getRequired());
			selModel.selected(
					eval.getEvaluationSelections()
						.stream()
						.filter(p -> p.getFactorOption() != null)
						.filter(p -> p.getFactorOption().getActive())
						.filter(p -> p.getFactorOption().getFactor().equals(taskXFactor.getFactor()))
						.map(p -> conversionService.convert(p.getFactorOption(), FactorOptionModel.class))
						.findFirst().orElse(null)
						);

			model.addFactorsItem(selModel);
		}
	}

	
}
