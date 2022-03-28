package hu.nextent.peas.service.virtue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.DatabaseInfoRepository;
import hu.nextent.peas.model.CompanyVirtueEditableModel;
import hu.nextent.peas.model.CompanyVirtueEditablePageModel;
import hu.nextent.peas.model.CompanyVirtueQueryParameterModel;
import hu.nextent.peas.service.VirtueService;
import hu.nextent.peas.service.virtue.CreateAndModifyCompanyVirtueDecorator;
import hu.nextent.peas.service.virtue.GetCompanyVirtueDecorator;
import hu.nextent.peas.service.virtue.PublicCompanyVirtueDecorator;
import hu.nextent.peas.service.virtue.QueryCompanyVirtueDecorator;
import lombok.val;

@Service
@Transactional
public class VirtueServiceImp 
implements VirtueService {

	@Autowired private CreateAndModifyCompanyVirtueDecorator createAndModifyCompanyVirtueDecorator;
	@Autowired private GetCompanyVirtueDecorator getCompanyVirtueDecorator;
	@Autowired private PublicCompanyVirtueDecorator publicCompanyVirtueDecorator;
	@Autowired private QueryCompanyVirtueDecorator queryCompanyVirtueDecorator;
	@Autowired private DatabaseInfoRepository databaseInfoRepository;
	
    public ResponseEntity<CompanyVirtueEditableModel> createCompanyVirtue( CompanyVirtueEditableModel  body) {
        val ret = createAndModifyCompanyVirtueDecorator.createCompanyVirtue(body);
		databaseInfoRepository.flush();
		return ret;
    }
    public ResponseEntity<CompanyVirtueEditableModel> getEditableCompanyVirtue( Long  virtueId) {
        val ret = getCompanyVirtueDecorator.getEditableCompanyVirtue(virtueId);
		databaseInfoRepository.flush();
		return ret;
    }
    public ResponseEntity<CompanyVirtueEditableModel> modifyEditableCompanyVirtue( CompanyVirtueEditableModel  body, Long  virtueId) {
        val ret = createAndModifyCompanyVirtueDecorator.modifyEditableCompanyVirtue(body, virtueId);
		databaseInfoRepository.flush();
		return ret;
    }
    public ResponseEntity<Void> publicEditableCompanyVirtue( Long  virtueId) {
        val ret = publicCompanyVirtueDecorator.publicEditableCompanyVirtue(virtueId);
		databaseInfoRepository.flush();
		return ret;
    }
    public ResponseEntity<CompanyVirtueEditablePageModel> queryCompanyVirtues( CompanyVirtueQueryParameterModel  body) {
    	val ret = queryCompanyVirtueDecorator.queryCompanyVirtues(body);
		databaseInfoRepository.flush();
		return ret;
    }
}
