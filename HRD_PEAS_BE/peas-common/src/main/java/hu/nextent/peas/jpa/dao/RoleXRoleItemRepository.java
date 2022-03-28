package hu.nextent.peas.jpa.dao;

import java.util.List;

import hu.nextent.peas.jpa.entity.Role;
import hu.nextent.peas.jpa.entity.RoleXRoleItem;

public interface RoleXRoleItemRepository extends DaoRepository<RoleXRoleItem, Long>{
	
	List<RoleXRoleItem> findAllByRole(Role role);
}
