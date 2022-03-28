package hu.nextent.peas.jpa.dao;

import java.util.List;
import java.util.Optional;

import hu.nextent.peas.jpa.entity.Label;

public interface LabelRepository extends DaoRepository<Label, Long>{

	List<Label> findAllByLanguage(String language);
	
	Optional<Label> findByLanguageAndCode(String language, String code);
	
}
