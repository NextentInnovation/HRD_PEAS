package hu.nextent.peas.jpa.dao;

import java.util.List;

import hu.nextent.peas.jpa.entity.Factor;
import hu.nextent.peas.jpa.entity.FactorOption;

public interface FactorOptionRepository extends DaoRepository<FactorOption, Long> {

	List<FactorOption> findAllByActiveTrueAndFactor(Factor factor);
	
}
