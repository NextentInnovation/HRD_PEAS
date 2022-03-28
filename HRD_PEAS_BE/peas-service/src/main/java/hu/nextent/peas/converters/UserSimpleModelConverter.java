package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.model.UserSimpleModel;

@Component
public class UserSimpleModelConverter
extends AbstractUserModelConverter<User, UserSimpleModel>
{

	@Override
	public UserSimpleModel convert(User source) {
		return convertToUserSimpleModel(source);
	}

}
