package hu.nextent.peas.service;

import org.springframework.http.ResponseEntity;

import hu.nextent.peas.model.CompanyVirtueEditableModel;
import hu.nextent.peas.model.CompanyVirtueEditablePageModel;
import hu.nextent.peas.model.CompanyVirtueQueryParameterModel;

public interface VirtueService {

	public ResponseEntity<CompanyVirtueEditableModel> createCompanyVirtue( CompanyVirtueEditableModel  body);

    public ResponseEntity<CompanyVirtueEditableModel> getEditableCompanyVirtue( Long  virtueId);

    public ResponseEntity<CompanyVirtueEditableModel> modifyEditableCompanyVirtue( CompanyVirtueEditableModel  body, Long  virtueId);

    public ResponseEntity<Void> publicEditableCompanyVirtue( Long  virtueId);

    public ResponseEntity<CompanyVirtueEditablePageModel> queryCompanyVirtues( CompanyVirtueQueryParameterModel  body);

}
