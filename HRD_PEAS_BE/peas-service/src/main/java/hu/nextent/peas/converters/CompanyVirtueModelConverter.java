package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.CompanyVirtue;
import hu.nextent.peas.model.CompanyVirtueModel;

@Component
public class CompanyVirtueModelConverter 
extends AbstractConverter<CompanyVirtue, CompanyVirtueModel>
{


	@Override
	public CompanyVirtueModel convert(CompanyVirtue source) {
		return new CompanyVirtueModel()
				.id(source.getId())
				.value(source.getValue())
				;
	}

}
