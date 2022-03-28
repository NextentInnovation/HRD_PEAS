package hu.nextent.peas.jpa.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.CompanyParameter;

public interface CompanyParameterRepository extends DaoRepository<CompanyParameter, Long> {

	List<CompanyParameter> findAllByCompany(@NonNull Company company);
	
	Optional<CompanyParameter> findByCompanyAndCode(@NonNull Company company, String code);
	Optional<CompanyParameter> findByCompany_IdAndCode(@NonNull Long companyId, String code);
}
