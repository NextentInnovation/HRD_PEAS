package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.model.EvaluationModel;

@Component
public class EvaluationModelConverter
extends AbstractEvaluationModelConverter<Evaluation, EvaluationModel> {

	@Override
	public EvaluationModel convert(Evaluation source) {
		return convertEvaluationModel(source);
	}

}
