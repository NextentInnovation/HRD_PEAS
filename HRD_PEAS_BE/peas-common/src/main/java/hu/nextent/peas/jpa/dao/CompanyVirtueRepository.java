package hu.nextent.peas.jpa.dao;

import java.util.List;

import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.CompanyVirtue;

public interface CompanyVirtueRepository extends DaoRepository<CompanyVirtue, Long>{
	
	List<CompanyVirtue> findAllByActiveTrueAndCompanyAndValueIsNotNullOrderByValue(Company company);

}
