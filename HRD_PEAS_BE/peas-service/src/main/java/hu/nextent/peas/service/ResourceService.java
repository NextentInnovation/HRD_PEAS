package hu.nextent.peas.service;

import org.springframework.http.ResponseEntity;

import hu.nextent.peas.model.AutocompletModel;
import hu.nextent.peas.model.AutocompletQueryModel;
import hu.nextent.peas.model.PeasAppInfoModel;

public interface ResourceService {

	public ResponseEntity<Object> getAllLabel();
	
	public ResponseEntity<PeasAppInfoModel> getInfo();
	
	public ResponseEntity<AutocompletModel> queryAutocomplet(AutocompletQueryModel queryBody);
}
