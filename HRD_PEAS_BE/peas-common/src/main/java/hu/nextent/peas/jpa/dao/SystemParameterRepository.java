package hu.nextent.peas.jpa.dao;

import java.util.Optional;

import hu.nextent.peas.jpa.entity.SystemParameter;

public interface SystemParameterRepository extends DaoRepository<SystemParameter, Long>{

	Optional<SystemParameter> findByCode(String code);
}
