package hu.nextent.peas.jpa.dao;

import java.util.List;
import java.util.Optional;

import hu.nextent.peas.jpa.entity.Company;

public interface CompanyRepository extends DaoRepository<Company, Long>{

	List<Company> findAllByActiveTrue();
	Optional<Company> findByName(String name);
}
