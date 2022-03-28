package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.lang.Nullable;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "evaluation"
        , uniqueConstraints = {
		        @UniqueConstraint(name = "uk_evaluation", 
        				columnNames = {"evaluator_id", "task_id", "automaticStartDate"}
		        ),
		}
)
@org.hibernate.annotations.Table( appliesTo = "evaluation", comment = "Értékelés" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Evaluation extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 1747372594586308245L;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, updatable = true, length = 100)
	@ColumnDefault(value = "'BEFORE_EVALUATING'")
	@Builder.Default private EvaluationStatusEnum status = EvaluationStatusEnum.BEFORE_EVALUATING;
	
	@Nullable
	@Digits(integer = 8 /*precision*/, fraction = 5 /*scale*/)
	@Column(name = "score", nullable = true, insertable = true, updatable = true, precision=8, scale=5)
	private BigDecimal score;
	
	@Nullable
	@Column(name = "evaluatedStartDate", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime evaluatedStartDate; // Értékelés indításának az időpontja

	@Nullable
	@Column(name = "evaluatedEndDate", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime evaluatedEndDate; // Értékelés zárás időpontja

	@Nullable
	@Column(name = "deadline", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime deadline; // Értékelés határideje időpont
	
	@Nullable
	@Column(name = "note", nullable = true, updatable = true)
    private String note; // Megjegyzés

	@Nullable
	@Column(name = "automaticStartDate", nullable = true, updatable = true)
    private OffsetDateTime automaticStartDate; // Automatikus értékelésnél használt

	@ManyToOne
	@JoinColumn(
			name = "evaluator_id", 
			foreignKey = @ForeignKey(name = "fk_evaluation_evaluator_id"), 
			nullable = true, 
			insertable = true, 
			updatable = false)
	@ToString.Exclude
	private User evaluator; // Értékelő felhasználó
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "task_id", 
			foreignKey = 
			@ForeignKey(name = "fk_evaluation_task_id"), 
			nullable = false, insertable = true, updatable = false)
	@ToString.Exclude
	private Task task;
	
    @NotNull
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_evaluation_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Company company;
	
	@Nullable
	@OneToMany(
			mappedBy="evaluation",
	        cascade = CascadeType.ALL
	    )
	@Builder.Default
	private List<EvaluationSelection> evaluationSelections = new ArrayList<EvaluationSelection>();
	
}
