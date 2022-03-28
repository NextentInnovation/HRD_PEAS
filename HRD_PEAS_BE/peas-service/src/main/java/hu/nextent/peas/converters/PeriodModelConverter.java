package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.model.PeriodModel;

@Component
public class PeriodModelConverter 
extends AbstractConverter<Period, PeriodModel>
{

	@Override
	public PeriodModel convert(Period period) {
		return new PeriodModel()
				.id(period.getId())
				.name(period.getName())
				.status(PeriodModel.StatusEnum.valueOf(period.getStatus().name()))
				.startDate(period.getStartDate())
				.endDate(period.getEndDate() == null ? null :period.getEndDate())
				;
	}

}
