package hu.nextent.peas.service.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import hu.nextent.peas.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.User;

@Service
@Transactional
public class ReportEmployeeDecorator
extends AbstractReportDecorator {

	public ResponseEntity<ReportEmployee> reportEmployee(ReportEmployeeQueryModel body) {
		
		Period period;
		User user;

		// adatok osszegyujtese
		Rating rating = null;
		if (body.getRatingId() != null && body.getRatingId() != 0L) {
			rating = ratingRepository.findById(body.getRatingId()).orElseThrow(() -> ExceptionList.report_rating_not_founded(REPORT_EMPLOYEE, body.getRatingId()));
			body.setPeriodId(rating.getPeriod().getId());
			body.setUserId(rating.getUser().getId());
		}
		period = getPeriod(REPORT_EMPLOYEE, body.getPeriodId());
		user = getUser(REPORT_EMPLOYEE, body.getUserId());

		if (rating == null) {
			rating = ratingRepository.findByPeriodAndUser(period, user).orElse(null);
		}

		// jogosultsagok vizsgalata
		List<RoleEnum> roleEnumList = getCurrentUserRoleEnum();
		if (!roleEnumList.contains(RoleEnum.HR)) {
			if (roleEnumList.contains(RoleEnum.LEADER)) {
				if (!getCurrentUser().getId().equals(user.getId()) && !user.isLedBy(getCurrentUser())) {
					throw new BadCredentialsException("Not Roles");
				}
			} else if (roleEnumList.contains(RoleEnum.USER)) {
				if (!getCurrentUser().getId().equals(user.getId())) {
					throw new BadCredentialsException("Not Roles");
				}
			} else {
				throw new BadCredentialsException("Not Roles");
			}
		}

		// valasz osszeallitasa
		ReportEmployee reportEmployee = new ReportEmployee();

		reportEmployee
				.period(conversionService.convert(period, ReportPeriodModel.class))
				.employee(conversionService.convert(user, UserModel.class))
				.leader(conversionService.convert(user.getLeader(), UserModel.class));

		if (rating != null) {
			reportEmployee
					.textualEvaluation(rating.getTextualEvaluation())
					.gradeRecommendation(rating.getGradeRecommendation())
					.cooperation(rating.getCooperation())
					.normalTasksScoreAvg(bigDecimalToDouble(rating.getNormalTaskScore()))
					.automaticTaskScore(bigDecimalToDouble(rating.getAutomaticTaskScore()))
					.score(bigDecimalToDouble(rating.getPeriodScore()));
		}

		reportEmployee
				.companyScoreAvg(bigDecimalToDouble(ratingRepository
						.avgPeriodScoreByPeriod(period).orElse(BigDecimal.ZERO)))
				.organizationScoreAvg(bigDecimalToDouble(ratingRepository
						.avgPeriodScoreByPeriodAndLeader(period, user.getOrganizationPath() + "%").orElse(BigDecimal.ZERO)))
				.asLeaderOrganization(queryAsLeaderOrganization(user))
				.asLeaderOrganizationScore(queryAsLeaderOrganizationScore(user, period))
				;

		List<Task> taskList = taskRepository.findAllByOwnerAndPeriod(user, period);

		List<TaskItemModel> taskItemModelList = new ArrayList<>();
		taskList.stream()
				.filter(task ->
						TaskTypeEnum.NORMAL.equals(task.getTaskType())
						&& (TaskStatusEnum.EVALUATED.equals(task.getStatus()) || TaskStatusEnum.CLOSED.equals(task.getStatus())))
				.forEach(t -> taskItemModelList.add(conversionService.convert(t, TaskItemModel.class)));

		taskList.stream()
				.filter(task -> TaskTypeEnum.AUTOMATIC.equals(task.getTaskType()))
				.forEach(t -> taskItemModelList.add(conversionService.convert(t, TaskItemModel.class)));

		reportEmployee.setTasks(taskItemModelList);

		return ResponseEntity.ok(reportEmployee);
	}

}
