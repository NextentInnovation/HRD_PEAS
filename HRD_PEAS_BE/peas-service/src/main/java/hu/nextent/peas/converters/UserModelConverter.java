package hu.nextent.peas.converters;

import org.springframework.stereotype.Component;

import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.model.UserModel;

@Component
public class UserModelConverter
extends AbstractUserModelConverter<User, UserModel>
{

	@Override
	public UserModel convert(User source) {
		return convertToUserModel(source);
	}

}
