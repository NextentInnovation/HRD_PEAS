package hu.nextent.peas.jpa.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.User;

public interface UserRepository extends DaoRepository<User, Long> 
{
	Optional<User> findByUserNameAndCompany_Name(@NonNull String userName, @NonNull String companyName);
	
	List<User> findByLeader(User user);
	
	List<User> findAllByCompanyAndActiveIsTrue(Company company);
	int countByCompanyAndActiveIsTrue(Company company);
	Page<User> findAllByCompanyAndActiveIsTrueOrderById(Company company, Pageable pageable);
	
	@Query("select distinct u from User u inner join u.userXRole uxr where u.company = :company and uxr.role.name in :roleEnums")
	List<User> findAllByCompanyAndRoleEnums(@Param("company") Company company, @Param("roleEnums") List<RoleEnum> roleEnums);
	
	@Query("select count(distinct u) from User u inner join u.userXRole uxr where u.company = :company and uxr.role.name in :roleEnums")
	int countByCompanyAndRoleEnums(@Param("company") Company company, @Param("roleEnums") List<RoleEnum> roleEnums);
	
}
