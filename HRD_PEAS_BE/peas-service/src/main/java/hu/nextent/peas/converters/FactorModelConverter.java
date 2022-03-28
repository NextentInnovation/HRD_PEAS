package hu.nextent.peas.converters;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.Factor;
import hu.nextent.peas.model.FactorModel;
import hu.nextent.peas.model.FactorOptionListModel;
import hu.nextent.peas.model.FactorOptionModel;
import lombok.val;

@Component
public class FactorModelConverter 
extends AbstractConverter<Factor, FactorModel> 
{

	@Override
	public FactorModel convert(Factor factor) {
		val model = new FactorModel();
		val options = new FactorOptionListModel();
		options.addAll(factor.getFactorOptions()
							.stream()
							.map(p -> conversionService.convert(p, FactorOptionModel.class))
							.collect(Collectors.toList()));
		
		model
			.options(options)
			.id(factor.getId())
			.name(factor.getName())
			;
		return model;
	}
	


}
