package hu.nextent.peas.service.virtue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.CompanyVirtue;
import hu.nextent.peas.jpa.entity.CompanyVirtue_;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.filter.Criterion;
import hu.nextent.peas.jpa.filter.ExpressionFactory;
import hu.nextent.peas.jpa.filter.SpecificationFactory;
import hu.nextent.peas.model.CompanyVirtueEditableModel;
import hu.nextent.peas.model.CompanyVirtueEditablePageModel;
import hu.nextent.peas.model.CompanyVirtueQueryParameterModel;
import hu.nextent.peas.utils.PageModelConverter;
import hu.nextent.peas.utils.PageableFactory;

@Service
@Transactional
public class QueryCompanyVirtueDecorator extends AbstractVirtueDecorator {

	private static Map<String, String> RENAME_FIELDS = new HashMap<String, String>();
	static {
		RENAME_FIELDS.put("id", CompanyVirtue_.ID);
		RENAME_FIELDS.put("value", CompanyVirtue_.VALUE);
		RENAME_FIELDS.put("editvalue", CompanyVirtue_.EDITVALUE);
	};
	
	public ResponseEntity<CompanyVirtueEditablePageModel> queryCompanyVirtues(CompanyVirtueQueryParameterModel body) {
		body = prepareCompanyVirtueQueryParameterModel(body);
		Pageable pageable = PageableFactory.build(getPageSize(), RENAME_FIELDS, body);
		
		List<RoleEnum> roles = getCurrentUserRoleEnum();
		boolean enabled = roles.contains(RoleEnum.ADMIN) || roles.contains(RoleEnum.BUSINESS_ADMIN) || roles.contains(RoleEnum.HR);
		if (!enabled) {
			return ResponseEntity.noContent().build();
		}
		
		Criterion crit = ExpressionFactory.and(
				ExpressionFactory.eq(CompanyVirtue_.ID, body.getId())
				, body.getValue() != null ? 
						ExpressionFactory.or(
								ExpressionFactory.ilike(CompanyVirtue_.VALUE, body.getValue())
								, ExpressionFactory.ilike(CompanyVirtue_.EDITVALUE, body.getValue())
						)
						: null
				, ExpressionFactory.eq(CompanyVirtue_.ACTIVE, Boolean.TRUE)
				, ExpressionFactory.eq(CompanyVirtue_.COMPANY, getCurrentCompany())
		);

		Specification<CompanyVirtue> spec = SpecificationFactory.make(crit);
		
		Page<CompanyVirtue> page = null;
		try {
			page = companyVirtueRepository.findAll(spec, pageable);
		} catch (IllegalArgumentException e) {
			throw ExceptionList.page_index_under(e);
		}
		
		CompanyVirtueEditablePageModel model = 
				new PageModelConverter<CompanyVirtueEditablePageModel, CompanyVirtue, CompanyVirtueEditableModel>()
				.applyPageModelClass(CompanyVirtueEditablePageModel.class)
				.applyPage(page)
				.applyConverter(p -> conversionService.convert(p, CompanyVirtueEditableModel.class))
				.apply();
		
		if (page.getTotalElements() == 0) {
			throw ExceptionList.no_content();
		}
		return ResponseEntity.ok(model);
	}

	private CompanyVirtueQueryParameterModel prepareCompanyVirtueQueryParameterModel(
			CompanyVirtueQueryParameterModel body) {
		if (body == null) {
			body = new CompanyVirtueQueryParameterModel();
		}
		
		if (StringUtils.isEmpty(body.getSort())) {
			body.setSort(CompanyVirtue_.VALUE);
		}
		
		return body;
	}

}
