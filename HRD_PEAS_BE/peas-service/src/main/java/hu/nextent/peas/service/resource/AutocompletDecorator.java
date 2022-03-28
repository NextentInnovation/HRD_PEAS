package hu.nextent.peas.service.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.CompanyVirtueRepository;
import hu.nextent.peas.jpa.dao.FactorRepository;
import hu.nextent.peas.jpa.dao.LeaderVirtueRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.CompanyVirtue;
import hu.nextent.peas.jpa.entity.CompanyVirtue_;
import hu.nextent.peas.jpa.entity.Company_;
import hu.nextent.peas.jpa.entity.Factor;
import hu.nextent.peas.jpa.entity.Factor_;
import hu.nextent.peas.jpa.entity.LeaderVirtue;
import hu.nextent.peas.jpa.entity.LeaderVirtue_;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.Task_;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.jpa.entity.User_;
import hu.nextent.peas.jpa.entity.base.BaseEntity;
import hu.nextent.peas.jpa.filter.ExpressionFactory;
import hu.nextent.peas.jpa.filter.SpecificationFactory;
import hu.nextent.peas.model.AutocompletItemModel;
import hu.nextent.peas.model.AutocompletModel;
import hu.nextent.peas.model.AutocompletQueryModel;
import hu.nextent.peas.service.base.BaseDecorator;
import lombok.val;

@Service
@Transactional
public class AutocompletDecorator extends BaseDecorator {

	private static final int DEFAULT_LIMIT = 10;
	
	@Autowired private TaskRepository taskRepository;
	@Autowired private CompanyVirtueRepository companyVirtueRepository;
	@Autowired private LeaderVirtueRepository leaderVirtueRepository;
	@Autowired private FactorRepository factorRepository;
	
	public ResponseEntity<AutocompletModel> queryAutocomplet(AutocompletQueryModel queryBody) {
		queryBody = prepareAutocompletQueryModel(queryBody);
		val filter = queryBody.getFilter();
		val limit = queryBody.getLimit();
		val autocompletType = queryBody.getAutocompletType();
		
		AutocompletModel model = null;
		
		switch (autocompletType) {
			case TASK:
				model = queryAutocompletTask(filter, limit);
				break;
			case USER:
				model = queryAutocompletUser(filter, limit);
				break;
			case COMPANY:
				model = queryAutocompletCompany(filter, limit);
				break;
			case DIFFICULTY:
				model = queryAutocompletDifficulity(filter, limit);
				break;
			case COMPANYVIRTUE:
				model = queryAutocompletCompanyVirtue(filter, limit);
				break;
			case LEADERVIRTUE:
				model = queryAutocompletLeaderVirtue(filter, limit);
				break;
			case FACTOR:
				model = queryAutocompletFactor(filter, limit);
				break;
			default:
				return ResponseEntity.badRequest().build();
		}
		
		model = fillPropererties(model, queryBody);
		return makeResponseEntity(model);
	}
	
	private AutocompletQueryModel prepareAutocompletQueryModel(AutocompletQueryModel body) {
		if (body == null) {
			body = new AutocompletQueryModel();
		}
		body.setLimit(body.getLimit() == null ? DEFAULT_LIMIT : body.getLimit());
		return body;
	}
	
	private AutocompletModel fillPropererties(AutocompletModel model, AutocompletQueryModel queryBody) {
		if (model == null) {
			model = new AutocompletModel();
		}
		val filter = queryBody.getFilter();
		val limit = queryBody.getLimit();
		val autocompletType = queryBody.getAutocompletType();
		
		model.setFilter(filter);
		model.setLimit(limit);
		model.setAutocompletType(autocompletType.name());
		return model;
	}
	
	private ResponseEntity<AutocompletModel> makeResponseEntity(AutocompletModel model) {
		if (model == null) {
			return ResponseEntity.badRequest().build();
		} else if (model.getContent().isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(model);
		} else {
			return ResponseEntity.ok(model);
		}
	}
	
	
	private AutocompletModel queryAutocompletTask(String filter, Integer limit) {
		val filterUser = filterUserByRole();
		val crit = ExpressionFactory.and(
						ExpressionFactory.ne(Task_.STATUS, TaskStatusEnum.DELETED),
						filterUser == null || filterUser.isEmpty() ? null : ExpressionFactory.in(Task_.OWNER, filterUser),
						ExpressionFactory.eq(Task_.COMPANY, getCurrentCompany()),
						ExpressionFactory.eq(Task_.TASK_TYPE, TaskTypeEnum.NORMAL),
						StringUtils.isEmpty(filter) ? null :
							ExpressionFactory.or(
										ExpressionFactory.ilike(Task_.NAME, filter),
										ExpressionFactory.ilike(Task_.DESCRIPTION, filter)
										)
				);
		Specification<Task> spec = SpecificationFactory.make(crit);
		val page = taskRepository.findAll(spec, PageRequest.of(0, limit, Direction.ASC, Task_.NAME));
		val model = new AutocompletModel()
				.content(page.getContent().stream().map(this::convertTo).collect(Collectors.toList()))
				.totalElements(page.getTotalElements());
		return model;
	}
	
	private AutocompletModel queryAutocompletUser(String filter, Integer limit) {
		val crit = ExpressionFactory.and(
						ExpressionFactory.eq(User_.ACTIVE, Boolean.TRUE),
						ExpressionFactory.eq(User_.COMPANY, getCurrentCompany()),
						StringUtils.isEmpty(filter) ? null :
							ExpressionFactory.or(
										ExpressionFactory.ilike(User_.USER_NAME, filter),
										ExpressionFactory.ilike(User_.FULL_NAME, filter),
										ExpressionFactory.ilike(User_.EMAIL, filter)
										)
					);
		
		Specification<User> spec = SpecificationFactory.make(crit);
		val page = userRepository.findAll(spec, PageRequest.of(0, limit, Direction.ASC, User_.USER_NAME));
		val model = new AutocompletModel()
				.content(page.getContent().stream().map(this::convertTo).collect(Collectors.toList()))
				.totalElements(page.getTotalElements());
		return model;
	}
	
	private AutocompletModel queryAutocompletCompany(String filter, Integer limit) {
		if (getCurrentUserRoleEnum().contains(RoleEnum.ADMIN)) {
			val crit = ExpressionFactory.and(
							ExpressionFactory.eq(Company_.ACTIVE, Boolean.TRUE),
							StringUtils.isEmpty(filter) ? null :
								ExpressionFactory.or(
											ExpressionFactory.ilike(Company_.NAME, filter),
											ExpressionFactory.ilike(Company_.FULL_NAME, filter)
										)
						);
			Specification<Company> spec = SpecificationFactory.make(crit);
			val page = companyRepository.findAll(spec, PageRequest.of(0, limit, Direction.ASC, Company_.NAME));
			val model = new AutocompletModel()
					.content(page.getContent().stream().map(this::convertTo).collect(Collectors.toList()))
					.totalElements(page.getTotalElements());
			return model;
		} else {
			val model = new AutocompletModel();
			model.addContentItem(convertTo(getCurrentCompany()));
			model.setTotalElements(1l);
			return model;
		}
	}

	private AutocompletModel queryAutocompletDifficulity(String filter, Integer limit) {
		List<AutocompletItemModel> list = getCompanyDifficulties().stream().map(this::convertTo).collect(Collectors.toList());
		val model = new AutocompletModel()
				.content(list)
				.totalElements(list.size() + 0l);
		return model;
	}
	
	private AutocompletModel queryAutocompletCompanyVirtue(String filter, Integer limit) {
		val crit = ExpressionFactory.and(
						ExpressionFactory.eq(CompanyVirtue_.ACTIVE, Boolean.TRUE),
						ExpressionFactory.eq(CompanyVirtue_.COMPANY, getCurrentCompany()),
						ExpressionFactory.isNotNull(CompanyVirtue_.VALUE),
						StringUtils.isEmpty(filter) ? null :
							ExpressionFactory.ilike(CompanyVirtue_.VALUE, filter)
				);
		
		Specification<CompanyVirtue> spec = SpecificationFactory.make(crit);
		val page = companyVirtueRepository.findAll(spec, PageRequest.of(0, limit, Direction.ASC, CompanyVirtue_.VALUE));
		val model = new AutocompletModel()
				.content(page.getContent().stream().map(this::convertTo).collect(Collectors.toList()))
				.totalElements(page.getTotalElements());
		return model;
	}
	
	private AutocompletModel queryAutocompletLeaderVirtue(String filter, Integer limit) {
		val crit = ExpressionFactory.and(
					ExpressionFactory.eq(LeaderVirtue_.ACTIVE, Boolean.TRUE),
					ExpressionFactory.eq(LeaderVirtue_.OWNER, getCurrentLeader()),
					ExpressionFactory.isNotNull(LeaderVirtue_.VALUE),
					StringUtils.isEmpty(filter) ? null :
						ExpressionFactory.ilike(LeaderVirtue_.VALUE, filter)
				);
		Specification<LeaderVirtue> spec = SpecificationFactory.make(crit);
		val page = leaderVirtueRepository.findAll(spec, PageRequest.of(0, limit, Direction.ASC, LeaderVirtue_.VALUE));
		val model = new AutocompletModel()
				.content(page.getContent().stream().map(this::convertTo).collect(Collectors.toList()))
				.totalElements(page.getTotalElements());
		return model;
	}
	
	private AutocompletModel queryAutocompletFactor(String filter, Integer limit) {
		val crit = ExpressionFactory.and(
						ExpressionFactory.eq(Factor_.ACTIVE, Boolean.TRUE),
						ExpressionFactory.eq(Factor_.AUTOMATIC, Boolean.FALSE),
						ExpressionFactory.eq(Factor_.COMPANY, getCurrentCompany()),
						ExpressionFactory.isNotNull(Factor_.NAME),
						StringUtils.isEmpty(filter) ? null :
							ExpressionFactory.ilike(Factor_.NAME, filter)
					);
		Specification<Factor> spec = SpecificationFactory.make(crit);
		val page = factorRepository.findAll(spec, PageRequest.of(0, limit, Direction.ASC, Factor_.NAME));
		val model = new AutocompletModel()
				.content(page.getContent().stream().map(this::convertTo).collect(Collectors.toList()))
				.totalElements(page.getTotalElements());
		return model;
	}

	private Collection<User> filterUserByRole() {
		Set<User> filterUser = Collections.emptySet();
		val roles = getCurrentUserRoleEnum();
		if (
				roles.contains(RoleEnum.ADMIN) 
				|| roles.contains(RoleEnum.BUSINESS_ADMIN)
				|| roles.contains(RoleEnum.HR)
				) {
		} else if (roles.contains(RoleEnum.LEADER)) {
			filterUser = new HashSet<User>(getChildUser());
		} else if (roles.contains(RoleEnum.USER)) {
			filterUser = new HashSet<User>();
			filterUser.add(getCurrentUser());
		}
		return filterUser;
	}
	
	private <T extends BaseEntity> AutocompletItemModel convertTo(T value) {
		return conversionService.convert(value, AutocompletItemModel.class);
	}

}
