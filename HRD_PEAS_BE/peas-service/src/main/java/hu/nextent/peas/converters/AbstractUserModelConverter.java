package hu.nextent.peas.converters;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import hu.nextent.peas.jpa.dao.RoleXRoleItemRepository;
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.dao.UserXRoleRepository;
import hu.nextent.peas.jpa.entity.Role;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.RoleItem;
import hu.nextent.peas.jpa.entity.RoleXRoleItem;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.jpa.entity.UserXRole;
import hu.nextent.peas.model.UserModel;
import hu.nextent.peas.model.UserSimpleModel;
import lombok.val;

public abstract class AbstractUserModelConverter<S extends User, T> 
extends AbstractConverter<S, T> 
{

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected UserXRoleRepository userRoleRepository;
	
	@Autowired
	protected RoleXRoleItemRepository roleXRoleItemRepository;
	

	
	protected UserSimpleModel convertToUserSimpleModel(User user) {
		val model = new UserSimpleModel();
		fillSimpleProperties(model, user);
		return model;
	}
	
	protected UserModel convertToUserModel(User user) {
		val model = new UserModel();
		fillSimpleProperties(model, user);
		fillProperties(model, user);
		return model;
	}
	
	private void fillSimpleProperties(UserSimpleModel userModel, User user) {
		userModel.setId(user.getId());
		userModel.setActive(user.getActive() && user.getCompany().getActive());
		userModel.setMode(UserModel.ModeEnum.NORMAL);
		userModel.setUserName(user.getUserName());
		userModel.setFullName(user.getFullName());
		userModel.setEmail(user.getEmail());
	}

	private void fillProperties(UserModel userModel, User user) {
		userModel.setInitial(user.getInitial());
		userModel.setOrganization(user.getOrganization());
		if (user.getCompany() != null) {
			userModel.setCompanyFullname(user.getCompany().getFullName());
		}
		
		val userRoles = userRoleRepository.findAllByUser(user);
		val roles = userRoles.stream()
				.map(UserXRole::getRole)
				.distinct()
				.collect(Collectors.toSet());
		
		roles.stream()
			.map(Role::getName)
			.map(RoleEnum::name)
			.map(String::toLowerCase)
			.distinct()
			.forEach(userModel::addRolesItem);
		
		roles.stream()
			.flatMap(role -> roleXRoleItemRepository.findAllByRole(role).stream())
			.map(RoleXRoleItem::getRoleItem)
			.map(RoleItem::getName)
			.distinct()
			.forEach(userModel::addRoleItemsItem);
	}


}
