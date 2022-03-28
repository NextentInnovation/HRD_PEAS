package hu.nextent.peas.service.base;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.dao.DatabaseInfoRepository;
import hu.nextent.peas.jpa.dao.DifficultyRepository;
import hu.nextent.peas.jpa.dao.FactorRepository;
import hu.nextent.peas.jpa.dao.LabelRepository;
import hu.nextent.peas.jpa.dao.NotificationActionRepository;
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.dao.UserXRoleRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Difficulty;
import hu.nextent.peas.jpa.entity.Factor;
import hu.nextent.peas.jpa.entity.Role;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.jpa.entity.UserXRole;
import hu.nextent.peas.model.DifficultyModel;
import hu.nextent.peas.model.FactorModel;
import hu.nextent.peas.model.UserModel;
import hu.nextent.peas.security.services.AuthService;
import hu.nextent.peas.utils.CompanyServiceHelper;
import hu.nextent.peas.utils.UserServiceHelper;
import lombok.val;

public abstract class BaseDecorator {
	
	@Autowired
	protected AuthService authService;
	
	@Autowired
	protected ConversionService conversionService;
	
	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
	protected CompanyRepository companyRepository;

	@Autowired
	protected UserXRoleRepository userXRoleRepository;
	
	@Autowired
	protected DifficultyRepository difficultyRepository;
	
	@Autowired
	protected FactorRepository factorRepository;
	
	@Autowired 
	protected DatabaseInfoRepository databaseInfoRepository;

	@Autowired
	protected LabelRepository labelRepository;
	
	@Autowired 
	protected CompanyServiceHelper companyServiceHelper; 
	
	@Autowired 
	protected UserServiceHelper userServiceHelper; 
	
	@Autowired
	protected NotificationActionRepository notificationActionRepository;

	protected User getCurrentUser() {
		return authService.currentUser();
	}

	protected List<Role> getCurrentUserRoles() {
		User user = getCurrentUser();
		if (user == null) {
			return Collections.emptyList();
		}
		return userXRoleRepository
					.findAllByUser(user)
					.stream()
					.map(UserXRole::getRole)
					.distinct()
					.collect(Collectors.toList());
	}

	protected List<RoleEnum> getCurrentUserRoleEnum() {
		List<Role> roles = getCurrentUserRoles();
		if (roles.isEmpty()) {
			return Collections.emptyList();
		}
		return roles
				.stream()
				.map(Role::getName)
				.distinct()
				.collect(Collectors.toList());
	}

	
//	public VirtualUser getCurrentVirtualUser() {
//		val tokenCache = getCurrentTokenCache();
//		return tokenCache.isPresent()? tokenCache.get().getVirtualUser() : null;
//	}
	
	protected UserModel getCurrentUserModel() {
		return getCurrentUser() == null ? null : conversionService.convert(getCurrentUser(), UserModel.class);
	}
	
	protected Company getCurrentCompany() {
		return getCurrentUser() == null ? null : getCurrentUser().getCompany();
	}
	
	protected User getCurrentLeader() {
		val currentUser = getCurrentUser();
		val leader = currentUser == null ? null : getCurrentUser().getLeader();
		
		return leader == null || !leader.getActive() ? null : currentUser.getLeader();
	}
	
	protected UserModel getCurrentLeaderModel() {
		val leader = getCurrentLeader();
		return leader == null ? null : conversionService.convert(leader, UserModel.class);
	}
	
	/**
	 * Beosztottak listája
	 * @return
	 */
	protected List<User> getChildUser() {
		val currentUser = getCurrentUser();
		if (currentUser == null) {
			return Collections.emptyList();
		}
		return userRepository.findByLeader(currentUser);
	}
	
	protected List<Difficulty> getCompanyDifficulties() {
		val currentCompany = getCurrentCompany();
		if (currentCompany == null) {
			return Collections.emptyList();
		}
		
		return difficultyRepository.findAllByActiveTrueAndAutomaticFalseAndCompanyOrderByName(currentCompany);
	}
	
	protected List<DifficultyModel> getCompanyDifficultyModels() {
		return getCompanyDifficulties()
					.stream()
					.map(p -> conversionService.convert(p, DifficultyModel.class))
					.collect(Collectors.toList());
	}
	
	protected List<Factor> getCompanyFactors() {
		val currentCompany = getCurrentCompany();
		if (currentCompany == null) {
			return Collections.emptyList();
		}
		return factorRepository.findAllByActiveTrueAndAutomaticFalseAndCompanyOrderByName(currentCompany);
	}
	
	protected List<FactorModel> getCompanyFactorModels() {
		return getCompanyFactors()
				.stream()
				.map(p -> conversionService.convert(p, FactorModel.class))
				.collect(Collectors.toList());
	}
	
	protected String getLanguage() {
		return userServiceHelper.getLanguage();
	}
	
	protected Integer getPageSize() {
		return companyServiceHelper.getPageSize();
	}
	
}
