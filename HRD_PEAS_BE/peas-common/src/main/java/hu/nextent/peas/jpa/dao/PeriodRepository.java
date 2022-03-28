package hu.nextent.peas.jpa.dao;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;

public interface PeriodRepository extends DaoRepository<Period, Long> {

	Optional<Period> findByCompanyAndStatus(Company company, PeriodStatusEnum status);
	
	List<Period> findAllByCompanyAndStatusInOrderByEndDateDesc(Company company, List<PeriodStatusEnum> statuses);
	
	Optional<Period> findFirstByCompanyAndStatusAndStartDateGreaterThanOrderByStartDateAsc(
			Company company, 
			PeriodStatusEnum status, 
			OffsetDateTime startDate
			);
	
	@Query("select max(p.endDate) from Period p where p.company = :company")
	OffsetDateTime findByMaxEnddateByCompany(@NotNull @Param("company") Company company);
	
}
