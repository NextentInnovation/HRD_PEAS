package hu.nextent.peas.exceptions;

import java.lang.reflect.Field;

public class ExceptionLabelConstant {
	
	private ExceptionLabelConstant() {
	}

	// Alapértelmezett hibakódok
	public static final String ERROR_NO_CONTENT = "error.no_content";
	public static final String ERROR_NOT_FOUNDED = "error.not_founded";
	public static final String ERROR_NOT_FOUNDED_OBJECT = "error.not_founded_object";
	public static final String ERROR_PAGE_INDEX_UNDER = "error.page_index_under";
	
	// Task hibák
	public static final String ERROR_TASK_ID_REQIRED = "error.task.id_reqired";
	public static final String ERROR_TASK_INVALID_ID = "error.task.invalid_id";
	public static final String ERROR_TASK_NOT_FOUNDED = "error.task.not_founded"; 
	public static final String ERROR_TASK_INVALID_COMPANY = "error.task.invalid_company"; 
	public static final String ERROR_TASK_INVALID_STATUS = "error.task.invalid_status"; 
	public static final String ERROR_TASK_INVALID_TYPE = "error.task.invalid_type"; 
	public static final String ERROR_TASK_INVALID_USER = "error.task.invalid_user";
	public static final String ERROR_TASK_NAME_EMPTY = "error.task.name_empty";
	public static final String ERROR_TASK_DIFFICULTY_NOT_FOUNDED = "error.task.difficulty_not_founded";
	public static final String ERROR_TASK_FACTOR_NOT_FOUNDED = "error.task.factor_not_founded";
	public static final String ERROR_TASK_LEADERVIRTUE_NOT_FOUNDED = "error.task.leadervirtue_not_founded";
	public static final String ERROR_TASK_COMPANYVIRTUE_NOT_FOUNDED = "error.task.companyvirtue_not_founded";
	public static final String ERROR_TASK_EVALUATOR_NOT_FOUNDED = "error.task.evaluator_not_founded";
	public static final String ERROR_TASK_FACTOR_LIMIT_VIOLATION = "error.task.factor_limit_violation";
	public static final String ERROR_TASK_FACTOR_REQUIRED_LIMIT_VIOLATION = "error.task.factor_required_limit_violation";
	public static final String ERROR_TASK_COMPANY_VIRTUE_LIMIT_VIOLATION = "error.task.company_virtue_limit_violation";
	public static final String ERROR_TASK_LEADER_VIRTUE_LIMIT_VIOLATION = "error.task.leader_virtue_limit_violation";
	public static final String ERROR_TASK_EVALUATOR_LIMIT_VIOLATION = "error.task.evaluator_limit_violation";

	// Értékelés hibák
	public static final String ERROR_EVALUATION_ID_REQIRED = "error.evaluation.id_reqired";
	public static final String ERROR_EVALUATION_INVALID_ID = "error.evaluation.invalid_id";
	public static final String ERROR_EVALUATION_NOT_FOUNDED = "error.evaluation.not_founded"; 
	public static final String ERROR_EVALUATION_INVALID_STATUS = "error.evaluation.invalid_status"; 
	public static final String ERROR_EVALUATION_INVALID_USER = "error.evaluation.invalid_user";
	public static final String ERROR_EVALUATION_SELECTION_EMPTY = "error.evaluation.selection_empty";
	public static final String ERROR_EVALUATION_SELECTION_REQUIRED_VIOLATION = "error.evaluation.selection_required_violation";
	
	// Minősítés
	public static final String ERROR_RATING_ID_REQIRED = "error.rating.id_reqired";
	public static final String ERROR_RATING_NOT_FOUNDED = "error.rating.not_founded";
	public static final String ERROR_RATING_INVALID_STATUS = "error.rating.invalid_status"; 
	public static final String ERROR_RATING_INVALID_USER = "error.rating.invalid_user";
	public static final String ERROR_RATING_INVALID_COMPANY = "error.rating.invalid_company"; 

	public static final String ERROR_RATING_GRADERECOMMENDATION_EMPTY = "error.rating.graderecommendation_empty"; 
	public static final String ERROR_RATING_TEXTUALEVALUATION_EMPTY = "error.rating.textualevaluation_empty"; 
	public static final String ERROR_RATING_COOPERATION_EMPTY = "error.rating.invalid_cooperation_empty"; 

	// Period
	public static final String ERROR_PERIOD_NOT_FOUNDED = "error.period.not_founded"; 
	public static final String ERROR_PERIOD_INVALID_COMPANY = "error.period.invalid_company";
	public static final String ERROR_PERIOD_ID_REQIRED = "error.period.id_reqired";
	public static final String ERROR_PERIOD_INVALID_ID = "error.period.invalid_id";
	public static final String ERROR_PERIOD_INVALID_STATUS = "error.period.invalid_status"; 

	// User
	public static final String ERROR_USER_NOT_FOUNDED = "error.user.not_founded"; 
	
	// CompanyVirtue
	public static final String ERROR_COMPANYVIRTUE_ID_REQIRED = "error.companyvirtue.id_reqired";
	public static final String ERROR_COMPANYVIRTUE_INVALID_STATUS = "error.companyvirtue.invalid_status"; 
	public static final String ERROR_COMPANYVIRTUE_INVALID_ID = "error.companyvirtue.invalid_id";
	public static final String ERROR_COMPANYVIRTUE_INVALID_COMPANY = "error.companyvirtue.invalid_company";
	public static final String ERROR_COMPANYVIRTUE_NOT_FOUNDED = "error.companyvirtue.not_founded"; 
	
	
	// Report
	public static final String ERROR_REPORT_PERIOD_NOT_FOUNDED = "error.report.period.not_founded"; 
	public static final String ERROR_REPORT_USER_NOT_FOUNDED = "error.report.user.not_founded"; 
	public static final String ERROR_REPORT_RATING_NOT_FOUNDED = "error.report.rating.not_founded"; 
	public static final String ERROR_REPORT_TASK_NOT_FOUNDED = "error.report.task.not_founded";


	public static void main(String ... args) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = ExceptionLabelConstant.class.getDeclaredFields();
		ExceptionLabelConstant instane = new ExceptionLabelConstant();
		for(Field field : fields) {
			System.out.println(String.format("('hu', '%s', '%s'),", field.get(instane), field.get(instane)));
		}
		
	}
}
