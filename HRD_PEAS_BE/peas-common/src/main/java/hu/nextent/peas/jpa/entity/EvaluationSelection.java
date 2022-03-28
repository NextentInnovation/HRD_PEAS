package hu.nextent.peas.jpa.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "evaluationselection"
        , uniqueConstraints = {
		        @UniqueConstraint(name = "uk_evaluationselection", 
		        		columnNames = {"evaluation_id", "factoroption_id"}
		        ),
		}
)
@org.hibernate.annotations.Table( appliesTo = "evaluationselection", comment = "Értékelt szempontok" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class EvaluationSelection extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 6258215039476037831L;

	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "evaluation_id", 
			foreignKey = 
			@ForeignKey(name = "fk_evaluationselection_evaluation_id"), 
			nullable = false, insertable = true, updatable = false)
	@ToString.Exclude
	private Evaluation evaluation;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "factoroption_id", 
			foreignKey = @ForeignKey(name = "fk_evaluationselection_factoroption_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private FactorOption factorOption;
	
	
	
	
}
