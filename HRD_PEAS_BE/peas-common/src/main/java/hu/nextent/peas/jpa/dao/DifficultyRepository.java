package hu.nextent.peas.jpa.dao;

import java.util.List;
import java.util.Optional;

import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Difficulty;

public interface DifficultyRepository extends DaoRepository<Difficulty, Long> {

	List<Difficulty> findAllByActiveTrueAndAutomaticFalseAndCompanyOrderByName(Company company);
	
	Optional<Difficulty> findByActiveTrueAndAutomaticTrueAndCompany(Company company);
}
