package hu.nextent.peas.jpa.entity;

public enum EvaluationStatusEnum {

	BEFORE_EVALUATING, // Még nincs értékelve
	EVALUATING, // Értékelendő
	EVALUATED, // Értékelt
	EXPIRED, // Lejárt
	CLOSED, // Lezárt
	DELETED

}
