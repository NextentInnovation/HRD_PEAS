package hu.nextent.peas.service.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationSelection;
import hu.nextent.peas.jpa.entity.Factor;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskXFactor;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.model.FactorSimpleModel;
import hu.nextent.peas.model.PeriodModel;
import hu.nextent.peas.model.ReportEvaluationModel;
import hu.nextent.peas.model.ReportEvaluationRowModel;
import hu.nextent.peas.model.TaskItemModel;
import hu.nextent.peas.model.UserModel;

@Service
@Transactional
public class ReportEvaluationDecorator
extends AbstractReportDecorator {

	public ResponseEntity<ReportEvaluationModel> reportEvaluation(Long taskId) {

		Task task = getTask(REPORT_EVALUATION, taskId);
		Period period = task.getPeriod();
		User user = task.getOwner();

		// jogosultsagok vizsgalata
		User currentUser = getCurrentUser();
		List<RoleEnum> roleEnumList = getCurrentUserRoleEnum();
		boolean isLeader = roleEnumList.contains(RoleEnum.LEADER) && user.isLedBy(currentUser);
		boolean isOwner = user.equals(currentUser);
		boolean isHr = roleEnumList.contains(RoleEnum.HR);
		if (!isHr && !isLeader && !isOwner) {
			throw new BadCredentialsException("Not Roles");
		}

		// egyeb adatok gyujtese
		Rating rating = ratingRepository.findByPeriodAndUser(period, user).orElse(null);

		// listák
		List<FactorSimpleModel> factorList = new ArrayList<>();
		List<ReportEvaluationRowModel> rowList = new ArrayList<>();
		List<Double> factorScoreAvgList = new ArrayList<>();
		List<Double> factorScoreSumList = new ArrayList<>();
		List<Integer> factorScoreCountList = new ArrayList<>();

		for(TaskXFactor taskXFactor : task.getTaskXFactors()) {
			Factor factor = taskXFactor.getFactor();
			FactorSimpleModel factorSimpleModel = new FactorSimpleModel();
			factorSimpleModel
					.id(factor.getId())
					.name(factor.getName())
					;
			factorList.add(factorSimpleModel);

			factorScoreAvgList.add(0d);
			factorScoreSumList.add(0d);
			factorScoreCountList.add(0);
		}


		Double allSum = 0d;
		int allCount = 0;
		for(Evaluation evaluation : task.getEvaluations()) {
			ReportEvaluationRowModel row = new ReportEvaluationRowModel();
			row.evaluatorName(isOwner && !evaluation.getEvaluator().equals(currentUser) ? "" : evaluation.getEvaluator().getFullName());
			row.note(evaluation.getNote());
			List<Double> scoreList = new ArrayList<>();
			Double sum = 0d;
			int count = 0;
			List<EvaluationSelection> selectionList = evaluation.getEvaluationSelections();
			for(int i = 0, l = factorList.size(); i < l; i++) {
				FactorSimpleModel factorSimpleModel = factorList.get(i);
				Double score = null;
				for(EvaluationSelection selection : selectionList) {
					if (factorSimpleModel.getId().equals(selection.getFactorOption().getFactor().getId())) {
						score = bigDecimalToDouble(selection.getFactorOption().getScore());
						count++;
						sum += score;

						factorScoreCountList.set(i, factorScoreCountList.get(i) + 1);
						factorScoreSumList.set(i, factorScoreSumList.get(i) + score);
						factorScoreAvgList.set(i, factorScoreSumList.get(i) / factorScoreCountList.get(i));
					}
				}
				scoreList.add(score);
			}
			row.factorScores(scoreList);
			row.scoreAvg(count > 0 ? sum / count : 0);
			rowList.add(row);
			if (count > 0) {
				allCount++;
				allSum += sum / count;
			}
		}
		rowList = rowList.stream()
				.sorted(Comparator.comparing(ReportEvaluationRowModel::getEvaluatorName))
				.collect(Collectors.toList());

		factorScoreAvgList.add(allCount > 0 ? allSum / allCount : 0);

		// valasz osszeallitasa
		ReportEvaluationModel model = new ReportEvaluationModel();

		model.setEmployee(conversionService.convert(user, UserModel.class));
		model.setLeader(conversionService.convert(user.getLeader(), UserModel.class));

		if (rating != null) {
			model
					.textualEvaluation(rating.getTextualEvaluation())
					.gradeRecommendation(rating.getGradeRecommendation())
					.cooperation(rating.getCooperation())
					.normalTasksScoreAvg(bigDecimalToDouble(rating.getNormalTaskScore()))
					.automaticTaskScore(bigDecimalToDouble(rating.getAutomaticTaskScore()))
					.score(bigDecimalToDouble(rating.getPeriodScore()));
		}

		model
				.companyScoreAvg(bigDecimalToDouble(ratingRepository
						.avgPeriodScoreByPeriod(period).orElse(BigDecimal.ZERO)))
				.organizationScoreAvg(bigDecimalToDouble(ratingRepository
						.avgPeriodScoreByPeriodAndLeader(period, user.getOrganizationPath() + "%").orElse(BigDecimal.ZERO)))
				.asLeaderOrganization(queryAsLeaderOrganization(user))
				.asLeaderOrganizationScore(queryAsLeaderOrganizationScore(user, period))

				.task(conversionService.convert(task, TaskItemModel.class))

				.factors(factorList)
				.factorScoreAvgs(factorScoreAvgList)
				.rows(rowList)
				.period(conversionService.convert(period, PeriodModel.class))
		;

		return ResponseEntity.ok(model);
	}

}
