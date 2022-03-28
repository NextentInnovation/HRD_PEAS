package hu.nextent.peas.jpa.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.RatingStatusEnum;
import hu.nextent.peas.jpa.entity.User;

public interface RatingRepository extends DaoRepository<Rating, Long> {

	Optional<Rating> findByPeriodAndUser(Period period, User user);
	
	List<Rating> findAllByPeriodAndStatus(Period period, RatingStatusEnum status);
	int countByPeriodAndStatus(Period period, RatingStatusEnum status);

	@Query("select avg(r.periodScore) from Rating r where r.period = :period")
	Optional<BigDecimal> avgPeriodScoreByPeriod(@NotNull @Param("period") Period period);

	@Query("select avg(r.periodScore) from Rating r where r.period = :period and r.user.organizationPath like :organizationPathLike")
	Optional<BigDecimal> avgPeriodScoreByPeriodAndLeader(@NotNull @Param("period") Period period, @NotNull @Param("organizationPathLike") String organizationPathLike);

}
