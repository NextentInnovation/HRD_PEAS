package hu.nextent.peas.jpa.dao.impl;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.DatabaseInfoRepository;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class DatabaseInfoRepositoryImpl implements DatabaseInfoRepository {

	@PersistenceContext
	private EntityManager entityManager;

    @Override
    public Timestamp getCurrentTimestamp() {
        return (Timestamp) entityManager.createNativeQuery(SELECT_NOW).getSingleResult();
    }


    @Override
    public Date getCurrentDate() {
        return (Date) entityManager.createNativeQuery(SELECT_CURRENT_DATE).getSingleResult();
    }
    
	@Override
	public void flush() {
		entityManager.flush();
	}
	
}
