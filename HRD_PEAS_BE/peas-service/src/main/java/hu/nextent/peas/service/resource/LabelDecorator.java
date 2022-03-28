package hu.nextent.peas.service.resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.Label;
import hu.nextent.peas.service.base.BaseDecorator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class LabelDecorator 
extends BaseDecorator 
{

	public ResponseEntity<Object> getAllLabel() {
		Map<String,String> labelMap = getLabels();
		log.debug("Founded " + labelMap.size() + " labels");
		if (labelMap.isEmpty()) {
			throw ExceptionList.no_content();
		} else {
			return ResponseEntity.ok(labelMap);
		}
	}
	
	private Map<String,String> getLabels() {
		List<Label> listOfLabel = labelRepository.findAllByLanguage(getLanguage());
		log.debug("Founded " + listOfLabel.size() + " labels");
		return listOfLabel
					.stream()
					.collect(Collectors.toMap(p -> p.getCode(), p -> p.getLabel()))
					;
	}
	
}
