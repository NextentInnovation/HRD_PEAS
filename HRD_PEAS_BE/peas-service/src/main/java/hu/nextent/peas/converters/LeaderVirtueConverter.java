package hu.nextent.peas.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.LeaderVirtue;
import hu.nextent.peas.model.LeaderVirtueModel;
import hu.nextent.peas.model.UserSimpleModel;
import hu.nextent.peas.security.services.AuthService;
import lombok.val;

@Component
public class LeaderVirtueConverter
extends AbstractConverter<LeaderVirtue, LeaderVirtueModel> 
{
	@Autowired
	private AuthService authService;

	@Override
	public LeaderVirtueModel convert(LeaderVirtue source) {
		val currentUser = authService.currentUser();
		return new LeaderVirtueModel()
				.id(source.getId())
				.own(
						source.getOwner() != null
						&& currentUser != null
						&& currentUser.getId().equals(source.getOwner().getId())
						)
				.owner(conversionService.convert(source.getOwner(), UserSimpleModel.class))
				.value(source.getValue())
				;
	}

}
