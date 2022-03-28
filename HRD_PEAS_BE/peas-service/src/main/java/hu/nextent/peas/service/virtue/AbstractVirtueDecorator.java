package hu.nextent.peas.service.virtue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.dao.CompanyVirtueRepository;
import hu.nextent.peas.jpa.entity.CompanyVirtue;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.model.CompanyVirtueEditableModel;
import hu.nextent.peas.service.base.BaseDecorator;
import lombok.val;

public abstract class AbstractVirtueDecorator extends BaseDecorator {

	@Autowired protected CompanyVirtueRepository companyVirtueRepository;
	
	protected void checkCompanyVirtueExists(Long companyVirtueId) {
		companyVirtueRepository.findById(companyVirtueId).orElseThrow(() -> ExceptionList.companyvirtue_not_founded(companyVirtueId));
    }
    
	protected void checkCompanyVirtueActive(CompanyVirtue companyVirtue) {
    	if (!companyVirtue.getActive()) {
    		throw ExceptionList.companyvirtue_invalid_status(companyVirtue.getId());
    	}
    }
    
	protected void checkCompanyVirtueCompany(CompanyVirtue companyVirtue) {
    	if (!companyVirtue.getCompany().equals(getCurrentCompany())) {
    		throw ExceptionList.companyvirtue_invalid_company(companyVirtue.getId(), companyVirtue.getCompany().getId());
    	}
    }
    
	protected void checkCompanyVirtueRole() {
		val roles = this.getCurrentUserRoleEnum();
    	if (!(roles.contains(RoleEnum.ADMIN) || roles.contains(RoleEnum.BUSINESS_ADMIN) || roles.contains(RoleEnum.HR) )) {
    		throw new BadCredentialsException("Not Roles");
    	}
    }
	
	protected ResponseEntity<CompanyVirtueEditableModel> getEditableCompanyVirtue(Long companyVirtueId) {
    	checkCompanyVirtueExists(companyVirtueId);
    	val companyVirtue = companyVirtueRepository.getOne(companyVirtueId);
    	return ResponseEntity.ok(conversionService.convert(companyVirtue, CompanyVirtueEditableModel.class));
    }
	
	protected ResponseEntity<CompanyVirtueEditableModel> getEditableCompanyVirtue(CompanyVirtue companyVirtue) {
    	return ResponseEntity.ok(conversionService.convert(companyVirtue, CompanyVirtueEditableModel.class));
    }
	
	
	
}
