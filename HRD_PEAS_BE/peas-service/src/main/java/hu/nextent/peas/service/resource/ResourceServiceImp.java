package hu.nextent.peas.service.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.model.AutocompletModel;
import hu.nextent.peas.model.AutocompletQueryModel;
import hu.nextent.peas.model.PeasAppInfoModel;
import hu.nextent.peas.service.ResourceService;

@Service
@Transactional
public class ResourceServiceImp 
implements ResourceService
{

	@Autowired private AutocompletDecorator autocompletDecorator;
	@Autowired private InfoDecorator infoDecorator;
	@Autowired private LabelDecorator labelDecorator;
	
	@Override
	public ResponseEntity<Object> getAllLabel() {
		return labelDecorator.getAllLabel();
	}

	@Override
	public ResponseEntity<PeasAppInfoModel> getInfo() {
		return infoDecorator.getInfo();
	}

	@Override
	public ResponseEntity<AutocompletModel> queryAutocomplet(AutocompletQueryModel queryBody) {
		return autocompletDecorator.queryAutocomplet(queryBody);
	}

}
