package hu.nextent.peas.jpa.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
@Table(name = "taskxcompanyvirtue" , 
	uniqueConstraints = {
        @UniqueConstraint(name = "uk_taskxcompanyvirtue", columnNames = {"task_id", "companyvirtue_id"}),
})
@org.hibernate.annotations.Table( appliesTo = "taskxcompanyvirtue", comment = "Kapcsoló tábla a Task és CompanyVirtue között" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class TaskXCompanyVirtue extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 5387674319381626696L;

	@NotNull
	@ManyToOne(optional=false, cascade=CascadeType.MERGE)
	@JoinColumn(
			name = "task_id", 
			foreignKey = @ForeignKey(name = "fk_taskxcompanyvirtue_task_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Task task;
	
	@NotNull
	@ManyToOne(optional=false, cascade=CascadeType.MERGE)
	@JoinColumn(
			name = "companyvirtue_id", 
			foreignKey = @ForeignKey(name = "fk_taskxcompanyvirtue_companyvirtue_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private CompanyVirtue companyVirtue;

	
}
