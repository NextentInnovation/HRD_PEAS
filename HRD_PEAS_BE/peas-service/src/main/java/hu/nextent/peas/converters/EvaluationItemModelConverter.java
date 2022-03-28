package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.model.EvaluationItemModel;

@Component
public class EvaluationItemModelConverter 
extends AbstractEvaluationModelConverter<Evaluation, EvaluationItemModel> {

	@Override
	public EvaluationItemModel convert(Evaluation source) {
		return convertEvaluationItemModel(source);
	}

}
