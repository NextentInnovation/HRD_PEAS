package hu.nextent.peas.service.virtue;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.CompanyVirtue;
import hu.nextent.peas.model.CompanyVirtueEditableModel;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class CreateAndModifyCompanyVirtueDecorator extends AbstractVirtueDecorator {

	public ResponseEntity<CompanyVirtueEditableModel> createCompanyVirtue( CompanyVirtueEditableModel  body) {
    	checkCompanyVirtueRole();
    	validateAndPrepareCreate(body);
    	CompanyVirtue companyVirtue = create(body);
        return getEditableCompanyVirtue(companyVirtue);
    }
    
    void validateAndPrepareCreate(CompanyVirtueEditableModel body) {
    	if (body.getId() != null) {
    		throw ExceptionList.companyvirtue_id_reqired();
    	}
    	
    	if (body.getEditvalue() == null || body.getEditvalue().isEmpty()) {
    		log.debug(String.format("editablevalue not set"));
    		throw new RuntimeException("editablevalue not set");
    	}
    }
    
    CompanyVirtue create(CompanyVirtueEditableModel body) {
    	log.debug(String.format("new companyVirtue"));
		CompanyVirtue companyVirtue = 
				CompanyVirtue.builder()
				.active(true)
				.company(getCurrentCompany())
				.editvalue(body.getEditvalue())
				.build();
		
		companyVirtueRepository.save(companyVirtue);
		return companyVirtue;
    }
    
    
    public ResponseEntity<CompanyVirtueEditableModel> modifyEditableCompanyVirtue( CompanyVirtueEditableModel  body, Long  virtueId) {
    	checkCompanyVirtueRole();
    	validateModifyEditable(body, virtueId);
    	var companyVirtue = companyVirtueRepository.getOne(virtueId);
    	if (!body.getEditvalue().equals(companyVirtue.getEditvalue())) {
        	companyVirtue.setEditvalue(body.getEditvalue());
        	companyVirtueRepository.save(companyVirtue);
    	}
		return getEditableCompanyVirtue(companyVirtue);
    }
    
    void validateModifyEditable(CompanyVirtueEditableModel body, Long checkedCompanyVirtueId) {
    	if (body.getId() == null || checkedCompanyVirtueId == null) {
    		throw ExceptionList.companyvirtue_id_reqired();
    	}
    	checkCompanyVirtueExists(checkedCompanyVirtueId);
		if (checkedCompanyVirtueId != null && checkedCompanyVirtueId.equals(body.getId())) {
			throw ExceptionList.companyvirtue_invalid_id(checkedCompanyVirtueId);
		}
		// Old
		CompanyVirtue companyVirtue = companyVirtueRepository.getOne(body.getId());
		checkCompanyVirtueActive(companyVirtue);
		checkCompanyVirtueCompany(companyVirtue);
		if (body.getEditvalue() == null || body.getEditvalue().isEmpty()) {
    		log.debug(String.format("editablevalue not set"));
    		throw new RuntimeException("editablevalue not set");
    	}
    }
    
}
