package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.FactorOption;
import hu.nextent.peas.model.FactorOptionModel;

@Component
public class FactorOptionModelConverter 
extends AbstractConverter<FactorOption, FactorOptionModel>
{

	@Override
	public FactorOptionModel convert(FactorOption source) {
		return new FactorOptionModel()
				.id(source.getId())
				.name(source.getName())
				.score(source.getScore().doubleValue());
	}


}
