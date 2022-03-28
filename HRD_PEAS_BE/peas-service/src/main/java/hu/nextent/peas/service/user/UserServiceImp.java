package hu.nextent.peas.service.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.Company_;
import hu.nextent.peas.jpa.entity.Task_;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.jpa.entity.User_;
import hu.nextent.peas.jpa.filter.Criterion;
import hu.nextent.peas.jpa.filter.ExpressionFactory;
import hu.nextent.peas.jpa.filter.SpecificationFactory;
import hu.nextent.peas.model.PagingAndSortingModel.OrderEnum;
import hu.nextent.peas.model.UserModel;
import hu.nextent.peas.model.UserPageModel;
import hu.nextent.peas.model.UserQueryParameterModel;
import hu.nextent.peas.model.UserSimpleModel;
import hu.nextent.peas.service.UserService;
import hu.nextent.peas.service.base.BaseDecorator;
import hu.nextent.peas.utils.PageModelConverter;
import hu.nextent.peas.utils.PageableFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserServiceImp 
extends BaseDecorator 
implements UserService {

	private static Map<String, String> RENAME_FIELDS = new HashMap<String, String>();
	static {
		RENAME_FIELDS.put(User_.USER_NAME, User_.USER_NAME);
		RENAME_FIELDS.put("name", User_.USER_NAME);
		RENAME_FIELDS.put(User_.FULL_NAME, User_.FULL_NAME);
		RENAME_FIELDS.put("fullname", User_.FULL_NAME);
		RENAME_FIELDS.put(User_.EMAIL, User_.EMAIL);
		RENAME_FIELDS.put(User_.COMPANY + "." + Company_.NAME, User_.COMPANY + "." + Company_.NAME);
		RENAME_FIELDS.put("companyname", User_.COMPANY + "." + Company_.NAME);
		RENAME_FIELDS.put("companyName", User_.COMPANY + "." + Company_.NAME);
		RENAME_FIELDS.put(User_.COMPANY + "." + Company_.FULL_NAME, User_.COMPANY + "." + Company_.FULL_NAME);
		RENAME_FIELDS.put("companyFullName", User_.COMPANY + "." + Company_.FULL_NAME);
		RENAME_FIELDS.put("companyfullname", User_.COMPANY + "." + Company_.FULL_NAME);
		RENAME_FIELDS.put(User_.ORGANIZATION, User_.ORGANIZATION);
		RENAME_FIELDS.put("organization", User_.ORGANIZATION);
	};
	
	public ResponseEntity<UserPageModel> getAllUser(UserQueryParameterModel body) {
		body = prepareUserQueryParameterModel(body);
		
		Pageable pageable = PageableFactory.build(getPageSize(), RENAME_FIELDS, body);

		Criterion crit = ExpressionFactory.and(
				ExpressionFactory.ilike(User_.USER_NAME, body.getName())
				, ExpressionFactory.ilike(User_.FULL_NAME, body.getFullName())
				, ExpressionFactory.ilike(User_.EMAIL, body.getEmail())
				, ExpressionFactory.ilike(User_.COMPANY + "." + Company_.NAME, body.getCompanyName())
				, ExpressionFactory.ilike(User_.COMPANY + "." + Company_.FULL_NAME, body.getCompanyFullname())
				, ExpressionFactory.ilike(User_.ORGANIZATION, body.getOrganization())
				, ExpressionFactory.eq(Task_.OWNER + "." + User_.COMPANY, getCurrentCompany())
				);
		
		Specification<User> spec = SpecificationFactory.make(crit);
		
		Page<User> page = null;
		try {
			page = userRepository.findAll(spec, pageable);
		} catch (IllegalArgumentException e) {
			throw ExceptionList.page_index_under(e);
		}
		
    	log.debug("Founded " + page.getSize() + " users");
    	
		Class<?> targetClass = 
				UserQueryParameterModel.QueryTypeEnum.NORMAL.equals(body.getQueryType())
					? UserModel.class
					: UserSimpleModel.class;

		UserPageModel model = 
				new PageModelConverter<UserPageModel, User, UserSimpleModel>()
				.applyPageModelClass(UserPageModel.class)
				.applyPage(page)
				.applyConverter(p -> (UserSimpleModel)this.convertTo(p, targetClass))
				.apply();
		
		if (page.getTotalElements() == 0) {
			throw ExceptionList.no_content();
		}
		return (ResponseEntity<UserPageModel>)ResponseEntity.ok(model);
	}
	

	private UserQueryParameterModel prepareUserQueryParameterModel(UserQueryParameterModel body) {
		if (body == null) {
			body = new UserQueryParameterModel();
		}
		if (StringUtils.isEmpty(body.getSort())) {
			body.setSort(User_.USER_NAME);
			body.setOrder(OrderEnum.DESC);
		}

		body.setQueryType(body.getQueryType() == null ? UserQueryParameterModel.QueryTypeEnum.SIMPLE : body.getQueryType());
		return body;
	}


	public ResponseEntity<UserModel> getUser(Long userId) {
		return ResponseEntity.ok(generateUserModel(userId));
	}

	private UserModel generateUserModel(Long userId) {
		Optional<User> jpaUser = userRepository.findById(userId);
		return (UserModel)convertTo(jpaUser.orElseThrow(() -> ExceptionList.user_not_founded(userId)), UserModel.class);
	}
	
	private Object convertTo(User user, Class<?> targetClass) {
		return conversionService.convert(user, targetClass);
	}

	
	
}
