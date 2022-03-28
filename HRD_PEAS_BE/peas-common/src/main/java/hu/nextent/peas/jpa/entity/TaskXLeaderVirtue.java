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
@Table(name = "taskxleadervirtue" , 
	uniqueConstraints = {
        @UniqueConstraint(name = "uk_taskxleadervirtue", columnNames = {"task_id", "leadervirtue_id"}),
})
@org.hibernate.annotations.Table( appliesTo = "taskxleadervirtue", comment = "Kapcsoló tábla a Task és LeaderVirtue között" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class TaskXLeaderVirtue extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = -6223776926353289599L;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "task_id", 
			foreignKey = @ForeignKey(name = "fk_taskxleadervirtue_task_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Task task;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "leadervirtue_id", 
			foreignKey = @ForeignKey(name = "fk_taskxleadervirtue_leadervirtue_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private LeaderVirtue leaderVirtue;
}
