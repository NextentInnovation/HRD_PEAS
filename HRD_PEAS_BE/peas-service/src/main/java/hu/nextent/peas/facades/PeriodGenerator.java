package hu.nextent.peas.facades;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.cache.ServiceCaches;
import hu.nextent.peas.constant.ServiceConstant;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import lombok.val;

// TODO Mockito teszt szükséges
@Service
@Transactional
public class PeriodGenerator {
	
	private static final DateTimeFormatter MONTH_DAY_FORMAT = DateTimeFormatter.ofPattern("MM.dd");
	
	@Autowired
	private ServiceCaches serviceCaches;

	@Autowired
	private PeriodRepository periodRepository;
	
	public Period nextPeriodGenerator(Company company) {
		OffsetDateTime startDay = calculateStartDate(company);
		OffsetDateTime endDay = calculateEndDate(company, startDay);
		OffsetDateTime ratingEndDate = calculateRatingEndDate(company, endDay);
		String templateName = generateName(company, startDay, endDay);
		
		Period period = Period
				.builder()
				.company(company)
				.endDate(endDay)
				.name(templateName)
				.ratingEndDate(ratingEndDate)
				.startDate(startDay)
				.status(PeriodStatusEnum.OPEN)
				.build();
		
		return period;
	}
	
	private OffsetDateTime calculateStartDate(Company company) {
		OffsetDateTime startDate = periodRepository.findByMaxEnddateByCompany(company);
		startDate = startDate == null ? OffsetDateTime.now(ZoneOffset.UTC) : startDate;
		return startDate;
	}

	private OffsetDateTime calculateEndDate(Company company, OffsetDateTime startDate) {
		Integer templateRangeDays = getPeriodTemplateRangeDays(company);
		LocalDate minimumEnd = startDate.plusDays(templateRangeDays).toLocalDate();
		List<LocalDate> possibleEndDates = getEndDates(minimumEnd, getMonthDayList(company));
		for(LocalDate possibleEndDate : possibleEndDates) {
			if (minimumEnd.isAfter(possibleEndDate)) {
				return minimumEnd.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
			}
		}
		
		return startDate.plusDays(templateRangeDays);
	}
	
	private List<MonthDay> getMonthDayList(Company company) {
		List<String> listStrMonthDays = Arrays.asList(StringUtils.splitByWholeSeparator(getPeriodTemplateEndDays(company), ","));
		listStrMonthDays.removeIf(p -> StringUtils.isEmpty(p) || StringUtils.isBlank(p));
		listStrMonthDays.replaceAll(p -> StringUtils.trim(p));
		List<MonthDay> result = new ArrayList<MonthDay>();
		for(String strMonthDay : listStrMonthDays) {
			if (StringUtils.isEmpty(strMonthDay)) {
				continue;
			}
			val monthDay = MonthDay.parse(strMonthDay, MONTH_DAY_FORMAT); 
			result.add(monthDay);
		}
		return result;
	}
	
	private List<LocalDate> getEndDates(LocalDate startDate, List<MonthDay> monthDays) {
		int year = startDate.getYear();
		List<LocalDate> result = new ArrayList<>();
		for(int i=0;i<2;i++) {
			for(int j=0;j<monthDays.size();j++) {
				MonthDay monthDay = monthDays.get(j);
				result.add(LocalDate.of(year + i, monthDay.getMonth(), monthDay.getDayOfMonth()));
			}
		}
		Collections.sort(result);
		return result;
	}
	
	private OffsetDateTime calculateRatingEndDate(Company company, OffsetDateTime endDate) {
		return endDate.plusDays(getPeriodTemplateRatingDay(company));
	}
	
	private String generateName(Company company, OffsetDateTime startDate, OffsetDateTime endDate) {
		return String.format(
						getPeriodTemplateName(company), 
						startDate.format(DateTimeFormatter.ISO_DATE_TIME),
						endDate.format(DateTimeFormatter.ISO_DATE_TIME)
						);
	}
	
	
	private Integer getPeriodTemplateRangeDays(Company company) {
		return serviceCaches.getCompanyParam(ServiceConstant.PERIOD_TEMPLATE_RANGE_DAYS, Integer.class, company);
	}
	
	private String getPeriodTemplateEndDays(Company company) {
		return serviceCaches.getCompanyParam(ServiceConstant.PERIOD_TEMPLATE_END_DAYS, company);
	}
	
    public Integer getPeriodTemplateRatingDay(Company company) {
    	return serviceCaches.getCompanyParam(ServiceConstant.PERIOD_TEMPLATE_RATING_DAY, Integer.class, company);
    }
    
	private String getPeriodTemplateName(Company company) {
		return serviceCaches.getCompanyParam(ServiceConstant.PERIOD_TEMPLATE_NAME, company);
	}

}
