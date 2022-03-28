package hu.nextent.peas.service.todo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hu.nextent.peas.jpa.dao.ViewToDoRepository;
import hu.nextent.peas.jpa.view.ViewToDo;
import hu.nextent.peas.jpa.view.ViewToDo_;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.dao.ToDoRepository;
import hu.nextent.peas.jpa.entity.ToDoStatusEnum;
import hu.nextent.peas.jpa.entity.ToDoTypeEnum;
import hu.nextent.peas.jpa.filter.ExpressionFactory;
import hu.nextent.peas.jpa.filter.SpecificationFactory;
import hu.nextent.peas.model.PagingAndSortingModel.OrderEnum;
import hu.nextent.peas.model.RangeDateTimeModel;
import hu.nextent.peas.model.ToDoModel;
import hu.nextent.peas.model.ToDoPageModel;
import hu.nextent.peas.model.ToDoQueryParameterModel;
import hu.nextent.peas.service.base.BaseDecorator;
import hu.nextent.peas.utils.PageModelConverter;
import hu.nextent.peas.utils.PageableFactory;
import lombok.val;

@Service
@Transactional
public class QueryTodoDecorator 
extends BaseDecorator 
{

	
	private static Map<String, String> RENAME_FIELDS = new HashMap<String, String>();
	static {
		RENAME_FIELDS.put("createdDate", ViewToDo_.CREATED_DATE);
		RENAME_FIELDS.put("status", ViewToDo_.STATUS_NAME);
		RENAME_FIELDS.put(ViewToDo_.CREATED_DATE, ViewToDo_.CREATED_DATE);
	};
	
	@Autowired private ViewToDoRepository viewToDoRepository;

	public ResponseEntity<ToDoPageModel> queryTodo(ToDoQueryParameterModel body) {
		body = prepareToDoQueryParameterModel(body);
		
		Pageable pageable = PageableFactory.build(getPageSize(), RENAME_FIELDS, body);

		List<ToDoTypeEnum> todoTypeEnums = !CollectionUtils.isEmpty(body.getTodoType())
				? body.getTodoType().stream().map(ToDoTypeEnum::valueOf).collect(Collectors.toList())
				: Collections.emptyList();
				
		List<ToDoStatusEnum> todoStatusEnums = !CollectionUtils.isEmpty(body.getStatus())
				? body.getStatus().stream().map(ToDoStatusEnum::valueOf).collect(Collectors.toList())
				: Collections.emptyList();
				
    	val criterions = ExpressionFactory.and(
				ExpressionFactory.eq(ViewToDo_.TO_USER, getCurrentUser())
				, ExpressionFactory.eq(ViewToDo_.COMPANY, getCurrentCompany())
				, ExpressionFactory.eq(ViewToDo_.ID, body.getId())
				, todoTypeEnums.isEmpty() ? null : ExpressionFactory.in(ViewToDo_.TO_DO_TYPE, todoTypeEnums)
				, todoStatusEnums.isEmpty() ? null : ExpressionFactory.in(ViewToDo_.STATUS, todoStatusEnums)
				, ExpressionFactory.between(ViewToDo_.DEADLINE, body.getDeadlineRange().getMin(), body.getDeadlineRange().getMax())
				, ExpressionFactory.between(ViewToDo_.DONE, body.getDoneRange().getMin(), body.getDoneRange().getMax())
    			);
    	
		Specification<ViewToDo> spec = SpecificationFactory.make(criterions);
		
		Page<ViewToDo> page = null;
		try {
			page = viewToDoRepository.findAll(spec, pageable);
		} catch (IllegalArgumentException e) {
			throw ExceptionList.page_index_under(e);
		}
		
		ToDoPageModel model = 
				new PageModelConverter<ToDoPageModel, ViewToDo, ToDoModel>()
				.applyPageModelClass(ToDoPageModel.class)
				.applyPage(page)
				.applyConverter(p -> conversionService.convert(p, ToDoModel.class))
				.apply();
		
		if (page.getTotalElements() == 0) {
			throw ExceptionList.no_content();
		} else {
			return ResponseEntity.ok(model);
		}

    }

	private ToDoQueryParameterModel prepareToDoQueryParameterModel(ToDoQueryParameterModel body) {
		if (body == null) {
			body = new ToDoQueryParameterModel();
		}
		
		if (StringUtils.isEmpty(body.getSort())) {
			body.setSort(ViewToDo_.CREATED_DATE);
			body.setOrder(OrderEnum.DESC);
		}
		
		body.setDeadlineRange(body.getDeadlineRange() == null ? new RangeDateTimeModel() : body.getDeadlineRange());
		body.setDoneRange(body.getDoneRange() == null ? new RangeDateTimeModel() : body.getDoneRange());
		return body;
	}

}
