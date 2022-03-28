package hu.nextent.peas.service.virtue;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PublicCompanyVirtueDecorator extends AbstractVirtueDecorator {

	public ResponseEntity<Void> publicEditableCompanyVirtue(Long virtueId) {
		validate(virtueId);
		val companyVirtue = companyVirtueRepository.getOne(virtueId);
		companyVirtue.setValue(companyVirtue.getEditvalue());
		companyVirtueRepository.save(companyVirtue);
		return ResponseEntity.ok().build();
	}

	private void validate(Long virtueId) {
		checkCompanyVirtueRole();
		checkCompanyVirtueExists(virtueId);
		val companyVirtue = companyVirtueRepository.getOne(virtueId);
		checkCompanyVirtueActive(companyVirtue);
		checkCompanyVirtueCompany(companyVirtue);
		if (companyVirtue.getEditvalue() == null || companyVirtue.getEditvalue().isEmpty()) {
			log.debug(String.format("editablevalue not set"));
			throw new RuntimeException("editablevalue not set");
		}
	}
}
