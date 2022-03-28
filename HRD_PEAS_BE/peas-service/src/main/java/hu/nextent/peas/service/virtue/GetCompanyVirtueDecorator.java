package hu.nextent.peas.service.virtue;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.entity.CompanyVirtue;
import hu.nextent.peas.model.CompanyVirtueEditableModel;

@Service
@Transactional
public class GetCompanyVirtueDecorator extends AbstractVirtueDecorator {

	public ResponseEntity<CompanyVirtueEditableModel> getEditableCompanyVirtue( Long  virtueId) {
    	checkCompanyVirtueRole();
    	validateGetCompanyVirtue(virtueId);
    	return super.getEditableCompanyVirtue(virtueId);
    }
    
    private void validateGetCompanyVirtue(Long companyVirtueId) {
    	checkCompanyVirtueExists(companyVirtueId);
    	CompanyVirtue companyVirtue = companyVirtueRepository.getOne(companyVirtueId);
    	checkCompanyVirtueActive(companyVirtue);
    	checkCompanyVirtueCompany(companyVirtue);
	} 

}
