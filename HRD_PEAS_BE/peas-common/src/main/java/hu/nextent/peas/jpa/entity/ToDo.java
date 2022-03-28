package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
		name = "todo"
)
@org.hibernate.annotations.Table( appliesTo = "todo", comment = "Rendszer által generált feladatok" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class ToDo extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 7138268531190079253L;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "toDoType", nullable = false, updatable = true, length = 100)
	private ToDoTypeEnum toDoType;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, updatable = true, length = 100)
	@Builder.Default private ToDoStatusEnum status = ToDoStatusEnum.OPEN;
	
	// Nyelvesített üzenet kód
	@NotNull
	@Column(name = "messageCode", nullable = false, insertable = true, updatable = true, length = 100)
	private String messageCode;
	
	// Határidő
	@NotNull
	@Column(name = "deadline", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime deadline;
	
	// Todo elvégzése
	@Nullable
	@Column(name = "done", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime done;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "referenceType", nullable = false, updatable = true, length = 100)
	private ReferenceTypeEnum referenceType;
		
	// Todo számára
	// Redundás értéke, a gyors keresés miatt lett betével
	// Ez mind megtalálható vagy a task-ban vagy az értékelésben
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "to_user_id", 
			foreignKey = 
			@ForeignKey(name = "fk_todo_to_user_id"), 
			nullable = false, insertable = true, updatable = false)
	@ToString.Exclude
	private User toUser;
	
	@Nullable
	@ManyToOne
	@JoinColumn(
			name = "task_id", 
			foreignKey = @ForeignKey(name = "fk_todo_task_id"), 
			nullable = true, 
			insertable = true, 
			updatable = false
			)
	private Task task;
	
	@Nullable
	@ManyToOne
	@JoinColumn(
			name = "evaluation_id", 
			foreignKey = @ForeignKey(name = "fk_todo_evaluation_id"), 
			nullable = true, 
			insertable = true, 
			updatable = false
			)
	private Evaluation evaluation;

	@Nullable
	@ManyToOne
	@JoinColumn(
			name = "rating_id", 
			foreignKey = @ForeignKey(name = "fk_todo_rating_id"), 
			nullable = true, 
			insertable = true, 
			updatable = false
			)
	private Rating rating;

	@Nullable
	@ManyToOne
	@JoinColumn(
			name = "leadervirtue_id", 
			foreignKey = @ForeignKey(name = "fk_todo_leadervirtue_id"), 
			nullable = true, 
			insertable = true, 
			updatable = false
			)
	private LeaderVirtue leaderVirtue;
	
	@Nullable
	@ManyToOne
	@JoinColumn(
			name = "period_id", 
			foreignKey = @ForeignKey(name = "fk_todo_period_id"), 
			nullable = true, 
			insertable = true, 
			updatable = false
			)
	private Period period;

    @NotNull
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_todo_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Company company;
	
	
}
