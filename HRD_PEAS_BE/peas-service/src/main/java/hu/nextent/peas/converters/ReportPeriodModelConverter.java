package hu.nextent.peas.converters;

import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.model.ReportPeriodModel;
import org.springframework.stereotype.Component;

@Component
public class ReportPeriodModelConverter
extends AbstractConverter<Period, ReportPeriodModel>
{

	@Override
	public ReportPeriodModel convert(Period period) {
		return new ReportPeriodModel()
				.id(period.getId())
				.name(period.getName())
				;
	}

}
