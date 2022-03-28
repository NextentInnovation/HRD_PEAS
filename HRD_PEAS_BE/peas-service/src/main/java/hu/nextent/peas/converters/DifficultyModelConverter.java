package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.Difficulty;
import hu.nextent.peas.model.DifficultyModel;

@Component
public class DifficultyModelConverter 
extends AbstractConverter<Difficulty, DifficultyModel>
{


	@Override
	public DifficultyModel convert(Difficulty source) {
		return new DifficultyModel()
				.id(source.getId())
				.name(source.getName())
				.description(source.getDescription())
				.multiplier(source.getMultiplier().doubleValue())
		;
	}
	
	
}
