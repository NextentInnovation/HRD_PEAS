package hu.nextent.peas.exceptions;

import static hu.nextent.peas.exceptions.ExceptionLabelConstant.*;

import java.util.Collection;

import org.springframework.http.HttpStatus;

import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.RatingStatusEnum;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;

public class ExceptionList {

	
	private static BaseResponseException create(HttpStatus status, String reason, Throwable cause,  Object ... args) {
		return new BaseResponseException(status, reason, cause, args);
	}
	
	public static BaseResponseException no_content(String reason, Object ... args) {
		return no_content(reason, null, args);
	}

	public static BaseResponseException no_content(Object ...args) {
		return no_content(null, null, args);
	}
	
	public static BaseResponseException no_content() {
		return no_content((String)null, (Throwable)null);
	}

	public static BaseResponseException no_content(String reason, Throwable cause,  Object ...args) {
		return create(
				HttpStatus.NO_CONTENT, 
				reason == null ? ERROR_NO_CONTENT : reason, 
				cause, 
				args
				);
	}
	
	public static BaseResponseException not_founded() {
		return create(HttpStatus.NOT_FOUND, ERROR_NOT_FOUNDED, (Throwable)null);
	}

	public static BaseResponseException not_founded_object(String type, Object id) {
		return create(
				HttpStatus.NOT_FOUND, 
				ERROR_NOT_FOUNDED_OBJECT, 
				null, 
				type,
				id
				);
	}
	
	public static BaseResponseException page_index_under(IllegalArgumentException e) { return create(HttpStatus.BAD_REQUEST, ERROR_PAGE_INDEX_UNDER, e); }

	public static BaseResponseException task_id_reqired() { return create(HttpStatus.BAD_REQUEST, ERROR_TASK_ID_REQIRED, null); }
	public static BaseResponseException task_invalid_id(Long taskId) { return create(HttpStatus.BAD_REQUEST, ERROR_TASK_INVALID_ID, null, taskId); }
	public static BaseResponseException task_not_founded(Long taskId) { return create(HttpStatus.NOT_FOUND, ERROR_TASK_NOT_FOUNDED, null, taskId); }
	public static BaseResponseException task_invalid_company(Long taskId, Long invalidCompanyId) { return create(HttpStatus.BAD_REQUEST, ERROR_TASK_INVALID_COMPANY, null, taskId, invalidCompanyId); }
	public static BaseResponseException task_invalid_status(Long taskId, Collection<TaskStatusEnum> validStatuses, Object currentStatus) { 
		return create(HttpStatus.BAD_REQUEST, ERROR_TASK_INVALID_STATUS, null, taskId, validStatuses, currentStatus); 
	}
	public static BaseResponseException task_invalid_type(Long taskId, Collection<TaskTypeEnum> validTypes, TaskTypeEnum currentType) { 
		return create(HttpStatus.BAD_REQUEST, ERROR_TASK_INVALID_TYPE, null, taskId, validTypes, currentType); 
	}
	public static BaseResponseException task_invalid_user(Long taskId, Long checkedUserId) { return create(HttpStatus.BAD_REQUEST, ERROR_TASK_INVALID_USER, null, taskId, checkedUserId);}
	public static BaseResponseException task_name_empty(Long taskId) {return create(HttpStatus.BAD_REQUEST, ERROR_TASK_NAME_EMPTY, null, taskId);}
	public static BaseResponseException task_difficulty_not_founded(Long taskId, Long difficulityId) {return create(HttpStatus.NOT_FOUND, ERROR_TASK_DIFFICULTY_NOT_FOUNDED, null, taskId, difficulityId);}
	public static BaseResponseException task_factor_not_founded(Long taskId, Long factorId) {return create(HttpStatus.NOT_FOUND, ERROR_TASK_FACTOR_NOT_FOUNDED, null, taskId, factorId);}
	public static BaseResponseException task_leadervirtue_not_founded(Long taskId, Long leaderVirtueId) {return create(HttpStatus.NOT_FOUND, ERROR_TASK_LEADERVIRTUE_NOT_FOUNDED, null, taskId, leaderVirtueId);}
	public static BaseResponseException task_companyvirtue_not_founded(Long taskId, Long companyVirtueId) {return create(HttpStatus.NOT_FOUND, ERROR_TASK_COMPANYVIRTUE_NOT_FOUNDED, null, taskId, companyVirtueId);}
	public static BaseResponseException task_evaluator_not_founded(Long taskId, Long userId) {return create(HttpStatus.BAD_REQUEST, ERROR_TASK_EVALUATOR_NOT_FOUNDED, null, taskId, userId);}
	public static BaseResponseException task_factor_limit_violation(Long taskId, Integer checkedLimit, Integer min, Integer max) {return create(HttpStatus.BAD_REQUEST, ERROR_TASK_FACTOR_LIMIT_VIOLATION, null, taskId, checkedLimit, min, max);}
	public static BaseResponseException task_factor_required_limit_violation(Long taskId, Integer checkedLimit, Integer min) {return create(HttpStatus.BAD_REQUEST, ERROR_TASK_FACTOR_REQUIRED_LIMIT_VIOLATION, null, taskId, checkedLimit, min);}
	public static BaseResponseException task_company_virtue_limit_violation(Long taskId, Integer checkedLimit, Integer min, Integer max) {return create(HttpStatus.BAD_REQUEST, ERROR_TASK_COMPANY_VIRTUE_LIMIT_VIOLATION, null, taskId, checkedLimit, min, max);}
	public static BaseResponseException task_leader_virtue_limit_violation(Long taskId, Integer checkedLimit, Integer min, Integer max) {return create(HttpStatus.BAD_REQUEST, ERROR_TASK_LEADER_VIRTUE_LIMIT_VIOLATION, null, taskId, checkedLimit, min, max);}
	public static BaseResponseException task_evaluator_limit_violation(Long taskId, Integer checkedLimit, Integer min, Integer max) {return create(HttpStatus.BAD_REQUEST, ERROR_TASK_EVALUATOR_LIMIT_VIOLATION, null, taskId, checkedLimit, min, max);}

	public static BaseResponseException evaluation_id_reqired() { return create(HttpStatus.BAD_REQUEST, ERROR_EVALUATION_ID_REQIRED, null); }
	public static BaseResponseException evaluation_invalid_id(Long evaluationId) { return create(HttpStatus.BAD_REQUEST, ERROR_EVALUATION_INVALID_ID, null, evaluationId); }
	public static BaseResponseException evaluation_not_founded(Long evaluationId) { return create(HttpStatus.NOT_FOUND, ERROR_EVALUATION_NOT_FOUNDED, null, evaluationId); }
	public static BaseResponseException evaluation_invalid_status(Long evaluationId, Collection<EvaluationStatusEnum> validStatuses, Object currentStatus) { 
		return create(HttpStatus.BAD_REQUEST, ERROR_EVALUATION_INVALID_STATUS, null, evaluationId, validStatuses, currentStatus); 
	}
	public static BaseResponseException evaluation_invalid_user(Long evaluationId, Long checkedUserId) { return create(HttpStatus.BAD_REQUEST, ERROR_EVALUATION_INVALID_USER, null, evaluationId, checkedUserId);}
	public static BaseResponseException evaluation_selection_empty(Long evaluationId) { return create(HttpStatus.BAD_REQUEST, ERROR_EVALUATION_SELECTION_EMPTY, null, evaluationId);}
	public static BaseResponseException evaluation_evaluation_selection_required_violation(Long evaluationId, Integer requiredSize, Integer selectedSize) { return create(HttpStatus.BAD_REQUEST, ERROR_EVALUATION_SELECTION_REQUIRED_VIOLATION, null, evaluationId, requiredSize, selectedSize);}

	public static BaseResponseException rating_id_reqired() { return create(HttpStatus.BAD_REQUEST, ERROR_RATING_ID_REQIRED, null); }
	public static BaseResponseException rating_not_founded(Long ratingId) { return create(HttpStatus.NOT_FOUND, ERROR_RATING_NOT_FOUNDED, null, ratingId); }
	public static BaseResponseException rating_invalid_status(Long ratingId, Collection<RatingStatusEnum> validStatuses, Object currentStatus) { 
		return create(HttpStatus.BAD_REQUEST, ERROR_RATING_INVALID_STATUS, null, ratingId, validStatuses, currentStatus); 
	}
	public static BaseResponseException rating_invalid_user(Long ratingId, Long checkedUserId) { return create(HttpStatus.BAD_REQUEST, ERROR_RATING_INVALID_USER, null, ratingId, checkedUserId);}
	public static BaseResponseException rating_invalid_company(Long ratingId, Long invalidCompanyId) { return create(HttpStatus.BAD_REQUEST, ERROR_RATING_INVALID_COMPANY, null, ratingId, invalidCompanyId); }

	public static BaseResponseException rating_gradeRecommendation_empty(Long ratingId) { return create(HttpStatus.BAD_REQUEST, ERROR_RATING_GRADERECOMMENDATION_EMPTY, null, ratingId);}
	public static BaseResponseException rating_textualEvaluation_empty(Long ratingId) { return create(HttpStatus.BAD_REQUEST, ERROR_RATING_TEXTUALEVALUATION_EMPTY, null, ratingId);}
	public static BaseResponseException rating_cooperation_empty(Long ratingId) { return create(HttpStatus.BAD_REQUEST, ERROR_RATING_COOPERATION_EMPTY, null, ratingId);}

	public static BaseResponseException period_not_founded(Long periodId) { return create(HttpStatus.NOT_FOUND, ERROR_PERIOD_NOT_FOUNDED, null, periodId); }
	public static BaseResponseException period_invalid_company(Long periodId, Long invalidCompanyId) { return create(HttpStatus.BAD_REQUEST, ERROR_PERIOD_INVALID_COMPANY, null, periodId, invalidCompanyId); }
	public static BaseResponseException period_id_reqired() { return create(HttpStatus.BAD_REQUEST, ERROR_PERIOD_ID_REQIRED, null); }
	public static BaseResponseException period_invalid_id(Long periodId) { return create(HttpStatus.BAD_REQUEST, ERROR_PERIOD_INVALID_ID, null, periodId); }
	public static BaseResponseException period_invalid_status(Long periodId, Collection<PeriodStatusEnum> validStatuses, Object currentStatus) { 
		return create(HttpStatus.BAD_REQUEST, ERROR_PERIOD_INVALID_STATUS, null, periodId, validStatuses, currentStatus); 
	}

	public static BaseResponseException user_not_founded(Long userId) { return create(HttpStatus.NOT_FOUND, ERROR_USER_NOT_FOUNDED, null, userId); }

	public static BaseResponseException companyvirtue_not_founded(Long companyvirtueId) { return create(HttpStatus.NOT_FOUND, ERROR_COMPANYVIRTUE_NOT_FOUNDED, null, companyvirtueId); }
	public static BaseResponseException companyvirtue_invalid_company(Long companyvirtueId, Long invalidCompanyId) { return create(HttpStatus.BAD_REQUEST, ERROR_COMPANYVIRTUE_INVALID_COMPANY, null, companyvirtueId, invalidCompanyId); }
	public static BaseResponseException companyvirtue_invalid_status(Long companyvirtueId) { return create(HttpStatus.BAD_REQUEST, ERROR_COMPANYVIRTUE_INVALID_STATUS, null, companyvirtueId); }
	public static BaseResponseException companyvirtue_id_reqired() { return create(HttpStatus.BAD_REQUEST, ERROR_COMPANYVIRTUE_ID_REQIRED, null); }
	public static BaseResponseException companyvirtue_invalid_id(Long companyvirtueId) { return create(HttpStatus.BAD_REQUEST, ERROR_COMPANYVIRTUE_INVALID_ID, null, companyvirtueId); }

	public static BaseResponseException report_user_not_founded(String reportCode, Long userId) { return create(HttpStatus.NOT_FOUND, ERROR_REPORT_USER_NOT_FOUNDED, null, reportCode, userId); }
	public static BaseResponseException report_period_not_founded(String reportCode, Long periodId) { return create(HttpStatus.NOT_FOUND, ERROR_REPORT_PERIOD_NOT_FOUNDED, null, reportCode, periodId); }
	public static BaseResponseException report_rating_not_founded(String reportCode, Long ratingId) { return create(HttpStatus.NOT_FOUND, ERROR_REPORT_RATING_NOT_FOUNDED, null, reportCode, ratingId); }
	public static BaseResponseException report_task_not_founded(String reportCode, Long taskId) { return create(HttpStatus.NOT_FOUND, ERROR_REPORT_TASK_NOT_FOUNDED, null, reportCode, taskId); }

}
