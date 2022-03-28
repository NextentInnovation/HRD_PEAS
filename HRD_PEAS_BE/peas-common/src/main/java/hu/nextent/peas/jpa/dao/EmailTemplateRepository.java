package hu.nextent.peas.jpa.dao;

import java.util.Optional;

import hu.nextent.peas.jpa.entity.EmailTemplate;

public interface EmailTemplateRepository extends DaoRepository<EmailTemplate, Long> {

	Optional<EmailTemplate> findByCodeAndLanguage(String code, String language);
	
}
