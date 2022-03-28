package hu.nextent.peas.jpa.dao;

import java.util.List;
import java.util.Optional;

import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Factor;

public interface FactorRepository extends DaoRepository<Factor, Long>{

	Optional<Factor> findByActiveTrueAndAutomaticTrueAndCompany(Company company);

	List<Factor> findAllByActiveTrueAndAutomaticFalseAndCompanyOrderByName(Company company);

}
