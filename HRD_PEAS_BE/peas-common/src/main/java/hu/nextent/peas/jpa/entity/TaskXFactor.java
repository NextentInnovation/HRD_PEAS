package hu.nextent.peas.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "taskxfactor"
        //, uniqueConstraints = {
		//        @UniqueConstraint(name = "uk_factor", columnNames = {"company_id", "code"}),
		//}
)
@org.hibernate.annotations.Table( appliesTo = "taskxfactor", comment = "Taskok és a hozzájuk rendelt értékelési szempontok" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class TaskXFactor extends AuditableBaseEntity implements Serializable {
	

	private static final long serialVersionUID = -731754563619157891L;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "task_id", 
			foreignKey = @ForeignKey(name = "fk_taskxfactor_task_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Task task;
	
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "factor_id", 
			foreignKey = @ForeignKey(name = "fk_taskxfactor_factor_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Factor factor;
	
	@NotNull
	@Column(name = "required", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "0")
	@Builder.Default
    private Boolean required = false;
	
	
}
