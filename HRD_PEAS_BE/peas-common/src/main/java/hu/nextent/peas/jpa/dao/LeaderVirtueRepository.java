package hu.nextent.peas.jpa.dao;

import java.util.List;

import hu.nextent.peas.jpa.entity.LeaderVirtue;
import hu.nextent.peas.jpa.entity.User;

public interface LeaderVirtueRepository extends DaoRepository<LeaderVirtue, Long> {
	
	List<LeaderVirtue> findAllByActiveTrueAndOwnerOrderByValue(User owner);

}
