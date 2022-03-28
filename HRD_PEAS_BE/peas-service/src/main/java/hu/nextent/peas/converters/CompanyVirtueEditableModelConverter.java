package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.CompanyVirtue;
import hu.nextent.peas.model.CompanyVirtueEditableModel;

@Component
public class CompanyVirtueEditableModelConverter 
extends AbstractConverter<CompanyVirtue, CompanyVirtueEditableModel> 
{

	@Override
	public CompanyVirtueEditableModel convert(CompanyVirtue source) {
		return (CompanyVirtueEditableModel)new CompanyVirtueEditableModel()
				.editvalue(source.getEditvalue())
				.id(source.getId())
				.value(source.getValue())
				;
	}

}
