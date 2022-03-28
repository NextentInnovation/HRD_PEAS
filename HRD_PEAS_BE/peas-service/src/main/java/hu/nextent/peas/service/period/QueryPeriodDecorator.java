package hu.nextent.peas.service.period;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.Period_;
import hu.nextent.peas.jpa.filter.Criterion;
import hu.nextent.peas.jpa.filter.ExpressionFactory;
import hu.nextent.peas.jpa.filter.SpecificationFactory;
import hu.nextent.peas.model.PeriodModel;
import hu.nextent.peas.model.PeriodPageModel;
import hu.nextent.peas.model.PeriodQueryParameterModel;
import hu.nextent.peas.model.RangeDateTimeModel;
import hu.nextent.peas.utils.PageModelConverter;
import hu.nextent.peas.utils.PageableFactory;

@Service
@Transactional
public class QueryPeriodDecorator extends AbstractPeriodDecorator {
	
	private static Map<String, String> RENAME_FIELDS = new HashMap<String, String>();
	static {
		RENAME_FIELDS.put("id", Period_.ID);
		RENAME_FIELDS.put("name", Period_.NAME);
		RENAME_FIELDS.put("status", Period_.STATUS);
		RENAME_FIELDS.put("startDate", Period_.START_DATE);
		RENAME_FIELDS.put("endDate", Period_.END_DATE);
		RENAME_FIELDS.put("ratingEndDate", Period_.RATING_END_DATE);
		RENAME_FIELDS.put("createdDate", Period_.CREATED_DATE);
		
	};
	
	 public ResponseEntity<PeriodPageModel> queryPeriod(PeriodQueryParameterModel body) {
		checkRight();
		body = prepareParameterModel(body);
		Pageable pageable = PageableFactory.build(getPageSize(), RENAME_FIELDS, body);
		
		List<PeriodStatusEnum> periodStatusEnums = !CollectionUtils.isEmpty(body.getStatus())
				? body.getStatus().stream().map(PeriodStatusEnum::valueOf).collect(Collectors.toList())
				: Collections.emptyList();
				
		Criterion crit = ExpressionFactory.and(
				ExpressionFactory.eq(Period_.ID, body.getId())
				, ExpressionFactory.eq(Period_.STATUS, periodStatusEnums)
				, ExpressionFactory.eq(Period_.NAME, body.getName())
				, ExpressionFactory.between(Period_.START_DATE, body.getStartDateRange().getMin(), body.getStartDateRange().getMax())
				, ExpressionFactory.between(Period_.END_DATE, body.getEndDateRange().getMin(), body.getEndDateRange().getMax())
				, ExpressionFactory.eq(Period_.COMPANY, getCurrentCompany())
				);

		Specification<Period> spec = SpecificationFactory.make(crit);
		
		Page<Period> page = null;
		try {
			page = periodRepository.findAll(spec, pageable);
		} catch (IllegalArgumentException e) {
			throw ExceptionList.page_index_under(e);
		}

		PeriodPageModel model = 
				new PageModelConverter<PeriodPageModel, Period, PeriodModel>()
				.applyPageModelClass(PeriodPageModel.class)
				.applyPage(page)
				.applyConverter(p -> conversionService.convert(p, PeriodModel.class))
				.apply();
		
		if (page.getTotalElements() == 0) {
			throw ExceptionList.no_content();
		} else {
			return ResponseEntity.ok(model);
		}
	 }

	private PeriodQueryParameterModel prepareParameterModel(PeriodQueryParameterModel body) {
		if (body == null) {
			body = new PeriodQueryParameterModel();
		}
		body.setStartDateRange(body.getStartDateRange() == null ? new RangeDateTimeModel() : body.getStartDateRange());
		body.setEndDateRange(body.getEndDateRange() == null ? new RangeDateTimeModel() : body.getEndDateRange());
		return body;
	}
	 
	 
}
