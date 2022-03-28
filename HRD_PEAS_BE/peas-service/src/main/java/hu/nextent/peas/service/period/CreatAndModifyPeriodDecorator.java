package hu.nextent.peas.service.period;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Range;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.model.CreatePeriodModel;
import hu.nextent.peas.model.PeriodModel;

@Service
@Transactional
public class CreatAndModifyPeriodDecorator 
extends AbstractPeriodDecorator {

	
	private Collection<PeriodStatusEnum> VALID_STATUSES = 
			Arrays.asList(
						PeriodStatusEnum.OPEN
						, PeriodStatusEnum.ACTIVE
						, PeriodStatusEnum.RATING
					);
	

	
    public ResponseEntity<PeriodModel> createPeriod( CreatePeriodModel  body) {
    	validateAndPrepareCreatePeriod(body);
    	Period period = createPeriodFromModel(body);
        return getPeriodModel(period);
    }
    
    private void validateAndPrepareCreatePeriod(CreatePeriodModel body) {
    	checkRight();
    	if (body.getEndDate() == null) {
        	// TODO: Make Exceptions
    		throw new RuntimeException("EndDate is null");
    	}
    	if (StringUtils.isEmpty(body.getName())) {
        	// TODO: Make Exceptions
    		throw new RuntimeException("Name is empty");
    	}
    	OffsetDateTime maxEndDate = periodRepository.findByMaxEnddateByCompany(getCurrentCompany());
    	if (maxEndDate != null && body.getEndDate().isAfter(maxEndDate)) {
        	// TODO: Make Exceptions
    		throw new RuntimeException("Enddate overlap");
    	}
    	// TODO minium startDate/endDate dátumtartomány meghatározása
    	
    }
    
    
    private Period createPeriodFromModel(CreatePeriodModel body) {
    	OffsetDateTime maxEndDate = periodRepository.findByMaxEnddateByCompany(getCurrentCompany());
    	OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
    	Period period = Period
    			.builder()
    			.status(PeriodStatusEnum.OPEN)
    			.name(body.getName())
    			.startDate(maxEndDate == null ? maxEndDate : now)
    			.endDate(body.getEndDate())
    			.ratingEndDate(body.getEndDate().plusDays(companyServiceHelper.getPeriodRatingDay()))
    			.company(getCurrentCompany())
    			.build()
    			;
    	periodRepository.save(period);
    	periodRepository.flush();
    	return period;
    }

    public ResponseEntity<PeriodModel> modifyPeriod( PeriodModel  body, Long  periodId) {
    	validateAndPrepareModifyPeriod(body, periodId);
    	Period period = modifyPeriodFromModel(body, periodId);
        return getPeriodModel(period);
    }
    


    private void validateAndPrepareModifyPeriod(PeriodModel body, Long checkPeriodId) {
    	checkRight();
    	
    	if (body.getId() == null || checkPeriodId == null) {
    		throw ExceptionList.period_id_reqired();
    	}
    	checkExists(checkPeriodId);
		if (checkPeriodId != null && !checkPeriodId.equals(body.getId())) {
    		throw ExceptionList.period_invalid_id(checkPeriodId);
		}
		// Old Period
		Period period = periodRepository.getOne(body.getId());
    	
    	if (body.getStartDate() == null) {
    		// TODO: Make Exceptions
    		throw new RuntimeException("StartDate is null");
    	}
    	if (body.getEndDate() == null) {
    		// TODO: Make Exceptions
    		throw new RuntimeException("EndDate is null");
    	}
    	if (body.getRatingEndDate() == null) {
    		// TODO: Make Exceptions
    		throw new RuntimeException("RatingEndDate is null");
    	}
    	if (StringUtils.isEmpty(body.getName())) {
    		// TODO: Make Exceptions
    		throw new RuntimeException("Name is empty");
    	}
    	
    	// Status check
    	if (!VALID_STATUSES.contains(period.getStatus())) {
    		throw ExceptionList.period_invalid_status(checkPeriodId, VALID_STATUSES, period.getStatus());
    	}
    	
    	// Period check
    	if (period.getEndDate().isBefore(period.getStartDate())) {
    		throw new RuntimeException("enddate < startdate");
    	}
    	// TODO Period Check
    	Range<OffsetDateTime> oldRange = Range.closed(period.getStartDate(), period.getEndDate());
    	Range<OffsetDateTime> newRange = Range.closed(body.getStartDate(), body.getEndDate());

    	
    	// 1 Day check
    	long periodHours = period.getStartDate().until(period.getEndDate(), ChronoUnit.HOURS);
    	if (periodHours < 24) {
    		throw new RuntimeException("Period: 1 day minimum");
    	}
    	
    	if (period.getRatingEndDate().isBefore(period.getEndDate())) {
    		throw new RuntimeException("ratingEndDate < enddate");
    	}
    	// 1 Day check
    	long ratingHours = period.getEndDate().until(period.getRatingEndDate(), ChronoUnit.HOURS);
    	if (ratingHours < 24) {
    		throw new RuntimeException("Rating: 1 day minimum");
    	}
    	
    	validateOpenPeriod(body, period);
    	validateActivePeriod(body, period);
    	validateRatingPeriod(body, period);
    }
    
    private void validateOpenPeriod(PeriodModel body, Period period) {
    	if (!PeriodStatusEnum.OPEN.equals(period.getStatus())) {
    		return;
    	}
    	// Csak az endDate és ratingEndDate módosítható
    }

    private void validateActivePeriod(PeriodModel body, Period period) {
    	if (!PeriodStatusEnum.ACTIVE.equals(period.getStatus())) {
    		return;
    	}
    	// Csak az endDate és ratingEndDate módosítható
    }
    
    private void validateRatingPeriod(PeriodModel body, Period period) {
    	if (!PeriodStatusEnum.RATING.equals(period.getStatus())) {
    		return;
    	}
    	// Csak a ratingEndDate módosítható
    	
    }


    private Period modifyPeriodFromModel(PeriodModel body, Long periodId) {
    	Period period = periodRepository.getOne(body.getId());
    	period.setEndDate(body.getEndDate());
    	period.setRatingEndDate(body.getRatingEndDate());
    	period.setName(body.getName());
    	periodRepository.save(period);
		return period;
	}
    
}
