package hu.nextent.peas.service.task;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.jpa.entity.User_;
import hu.nextent.peas.jpa.filter.Criterion;
import hu.nextent.peas.jpa.filter.ExpressionFactory;
import hu.nextent.peas.jpa.filter.SpecificationFactory;
import hu.nextent.peas.jpa.view.ViewTask;
import hu.nextent.peas.jpa.view.ViewTask_;
import hu.nextent.peas.model.PagingAndSortingModel.OrderEnum;
import hu.nextent.peas.model.RangeDateTimeModel;
import hu.nextent.peas.model.RangeDoubleModel;
import hu.nextent.peas.model.TaskItemModel;
import hu.nextent.peas.model.TaskItemPageModel;
import hu.nextent.peas.model.TaskQueryParameterModel;
import hu.nextent.peas.utils.PageModelConverter;
import hu.nextent.peas.utils.PageableFactory;

@Service
@Transactional
public class TaskQueryDecorator 
extends AbstarctTaskDecorator 
{
	
	
	private static Map<String, String> RENAME_FIELDS = new HashMap<String, String>();
	static {
		RENAME_FIELDS.put("taskStatus", ViewTask_.STATUS_NAME);
		RENAME_FIELDS.put("status", ViewTask_.STATUS_NAME);
		RENAME_FIELDS.put("taskTypeName", ViewTask_.TASK_TYPE_NAME);
		RENAME_FIELDS.put("typeName", ViewTask_.TASK_TYPE_NAME);
		RENAME_FIELDS.put("type", ViewTask_.TASK_TYPE_NAME);
		RENAME_FIELDS.put("name", ViewTask_.NAME);
		RENAME_FIELDS.put("ownerName", ViewTask_.OWNER + "." + User_.USER_NAME);
		RENAME_FIELDS.put("owner.name", ViewTask_.OWNER + "." + User_.USER_NAME);
		RENAME_FIELDS.put(ViewTask_.OWNER + "." + User_.USER_NAME, ViewTask_.OWNER + "." + User_.USER_NAME);
		RENAME_FIELDS.put("ownerId", ViewTask_.OWNER + "." + User_.ID);
		RENAME_FIELDS.put("owner.id", ViewTask_.OWNER + "." + User_.ID);
		RENAME_FIELDS.put("owner.organization", ViewTask_.OWNER + "." + User_.ORGANIZATION);
		RENAME_FIELDS.put(ViewTask_.OWNER + "." + User_.ID, ViewTask_.OWNER + "." + User_.ID);
		RENAME_FIELDS.put("description", ViewTask_.DESCRIPTION);
		RENAME_FIELDS.put("percentage", ViewTask_.EVALUATION_PERCENTAGE);
		RENAME_FIELDS.put("evaluationPercentage", ViewTask_.EVALUATION_PERCENTAGE);
		RENAME_FIELDS.put("score", ViewTask_.SCORE);
		RENAME_FIELDS.put("createdDate", ViewTask_.CREATED_DATE);
		RENAME_FIELDS.put(ViewTask_.CREATED_DATE, ViewTask_.CREATED_DATE);
		RENAME_FIELDS.put("deadline", ViewTask_.DEADLINE);
		RENAME_FIELDS.put("startDate", ViewTask_.START_DATE);
		RENAME_FIELDS.put("endDate", ViewTask_.END_DATE);
	};
	
	
	public ResponseEntity<TaskItemPageModel> queryTask(TaskQueryParameterModel body) {
		body = prepareParameterModel(body);
		//Pageable pageable = PageableFactory.build(getCompanyParameters().get(ServiceConstant.PAGE_SIZE), RENAME_FIELDS, body);
		Pageable pageable = PageableFactory.build(getPageSize(), RENAME_FIELDS, body);
		
		Set<User> filterUser = Collections.emptySet();
		List<RoleEnum> roles = getCurrentUserRoleEnum();
		if ( roles.contains(RoleEnum.ADMIN) 
				|| roles.contains(RoleEnum.BUSINESS_ADMIN)
				|| roles.contains(RoleEnum.HR)
				) {
			
		} else if (roles.contains(RoleEnum.LEADER)) {
			filterUser = new HashSet<User>(getChildUser());
			filterUser.add(getCurrentUser());
		} else if (roles.contains(RoleEnum.USER)) {
			filterUser = new HashSet<User>();
			filterUser.add(getCurrentUser());
		}
		
		TaskTypeEnum taskTypeEnums = body.getTaskType() != null ? TaskTypeEnum.valueOf(body.getTaskType().name()) : null;
		List<TaskStatusEnum> taskStatusEnums = !CollectionUtils.isEmpty(body.getStatus())
				? body.getStatus().stream().map(TaskStatusEnum::valueOf).collect(Collectors.toList())
				: Collections.emptyList();
		
		Criterion crit = ExpressionFactory.and(
						  ExpressionFactory.eq(ViewTask_.TASK_ID, body.getId())
						, ExpressionFactory.eq(ViewTask_.LANGUAGE, userServiceHelper.getLanguage())
						, ExpressionFactory.eq(ViewTask_.TASK_TYPE, taskTypeEnums)
						, ExpressionFactory.ne(ViewTask_.TASK_TYPE, TaskTypeEnum.AUTOMATIC)
						, taskStatusEnums.isEmpty() ? null : ExpressionFactory.in(ViewTask_.STATUS, taskStatusEnums)
						, ExpressionFactory.ne(ViewTask_.STATUS, TaskStatusEnum.DELETED)
						, ExpressionFactory.ilike(ViewTask_.NAME, body.getName())
						, ExpressionFactory.ilike(ViewTask_.DESCRIPTION, body.getDescription())
						, ExpressionFactory.eq(ViewTask_.OWNER + "." + User_.ID, body.getOwnerId())
						, !filterUser.isEmpty() ? ExpressionFactory.in(ViewTask_.OWNER, filterUser) : null
						, ExpressionFactory.between(ViewTask_.EVALUATION_PERCENTAGE, body.getPercentageRange().getMin(), body.getPercentageRange().getMax())
						, ExpressionFactory.between(ViewTask_.SCORE, body.getScoreRange().getMin(), body.getScoreRange().getMax())
						, ExpressionFactory.between(ViewTask_.CREATED_DATE, body.getCreatedDateRange().getMin(), body.getCreatedDateRange().getMax())
						, ExpressionFactory.between(ViewTask_.DEADLINE, body.getDeadlineRange().getMin(), body.getDeadlineRange().getMax())
						, ExpressionFactory.between(ViewTask_.START_DATE, body.getStartDateRange().getMin(), body.getStartDateRange().getMax())
						, ExpressionFactory.between(ViewTask_.END_DATE, body.getEndDateRange().getMin(), body.getEndDateRange().getMax())
						, ExpressionFactory.eq(ViewTask_.COMPANY, getCurrentCompany())
				);
		
		Specification<ViewTask> spec = SpecificationFactory.make(crit);
		
		Page<ViewTask> page = null;
		try {
			page = viewTaskRepository.findAll(spec, pageable);
		} catch (IllegalArgumentException e) {
			throw ExceptionList.page_index_under(e);
		}

		
		TaskItemPageModel model = 
				new PageModelConverter<TaskItemPageModel, ViewTask, TaskItemModel>()
				.applyPageModelClass(TaskItemPageModel.class)
				.applyPage(page)
				.applyConverter(p -> conversionService.convert(p, TaskItemModel.class))
				.apply();
		
		if (page.getTotalElements() == 0) {
			throw ExceptionList.no_content();
		} else {
			return ResponseEntity.ok(model);
		}
		
        
    }
	
	
	private TaskQueryParameterModel prepareParameterModel(TaskQueryParameterModel body) {
		if (body == null) {
			body = new TaskQueryParameterModel();
		}
		
		if (StringUtils.isEmpty(body.getSort())) {
			body.setSort(ViewTask_.START_DATE);
			body.setOrder(OrderEnum.DESC);
		}
		
		body.setPercentageRange(body.getPercentageRange() == null ? new RangeDoubleModel() : body.getPercentageRange());
		body.setScoreRange(body.getScoreRange() == null ? new RangeDoubleModel() : body.getScoreRange());
		body.setCreatedDateRange(body.getCreatedDateRange() == null ? new RangeDateTimeModel() : body.getCreatedDateRange());
		body.setCreatedDateRange(body.getCreatedDateRange() == null ? new RangeDateTimeModel() : body.getCreatedDateRange());
		body.setDeadlineRange(body.getDeadlineRange() == null ? new RangeDateTimeModel() : body.getDeadlineRange());
		body.setStartDateRange(body.getStartDateRange() == null ? new RangeDateTimeModel() : body.getStartDateRange());
		body.setEndDateRange(body.getEndDateRange() == null ? new RangeDateTimeModel() : body.getEndDateRange());
		
		return body;
	}
	

}
