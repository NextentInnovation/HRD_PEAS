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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "task"
        //, uniqueConstraints = {
        //        @UniqueConstraint(name = "uk_task", columnNames = {"language", "code"}),
        //}
)
@org.hibernate.annotations.Table( appliesTo = "task", comment = "Feladatok" )
@NamedQueries({
    @NamedQuery(name = "Task.AutomaticTaskClose",
            query = "UPDATE Task t\n"
                    + "SET t.status = 'EVALUATED'\n"
            		+ "    , t.endDate = :endDate\n"
            		+ "    , t.version = t.version + 1\n"
                    + "WHERE t.taskType = 'AUTOMATIC'\n"
                    + "      and t.status = 'UNDER_EVALUATION'\n"
                    + "      and t.period = :activePeriod\n"
                    )
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Task extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 4146830684914803683L;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, updatable = true, length = 100)
	@ColumnDefault(value = "'PARAMETERIZATION'")
	@Builder.Default 
	private TaskStatusEnum status = TaskStatusEnum.PARAMETERIZATION;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "taskType", nullable = false, updatable = true, length = 100)
	@Builder.Default 
	private TaskTypeEnum taskType = TaskTypeEnum.NORMAL;
	
	@NotNull
	@Column(name = "name", nullable = false, insertable = true, updatable = true, length = 100)
	private String name;
	
	@Nullable
	@Column(name = "description", nullable = true, insertable = true, updatable = true, length = 255)
	private String description;
	
	@Nullable
	@Column(name = "startDate", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime startDate; // Értékelés indításának az ideje.

	@Nullable
	@Column(name = "endDate", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime endDate; // Az értékelés kezdetén az értékelés határidejét jelenti, az értékelés végén az utolsó értékelés időpontját

	@Nullable
	@Column(name = "deadline", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime deadline; // Értékelés határideje időpont
	
	// Értékelők száma
	@NotNull
	@Column(name = "evaluaterCount", nullable = false, insertable = true, updatable = true, precision=5, scale=2)
	@ColumnDefault(value = "0")
	@Builder.Default 
	private Integer evaluaterCount = 0;
	
	// Kitöltött értékelések száma
	@NotNull
	@Column(name = "evaluatedCount", nullable = false, insertable = true, updatable = true, precision=5, scale=2)
	@ColumnDefault(value = "0")
	@Builder.Default 
	private Integer evaluatedCount = 0;

	// Kitöltöttség százalékban
	// 100 * evaluationCount / evaluaterCount
	@NotNull
	@Digits(integer = 8 /*precision*/, fraction = 5 /*scale*/)
	@Column(name = "evaluationPercentage", nullable = false, insertable = true, updatable = true, precision=8, scale=5)
	@ColumnDefault(value = "0.0")
	@Builder.Default 
	private BigDecimal evaluationPercentage = BigDecimal.ZERO;

	@Nullable
	@Digits(integer = 8 /*precision*/, fraction = 5 /*scale*/)
	@Column(name = "score", nullable = true, insertable = true, updatable = true, precision=8, scale=5)
	private BigDecimal score;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "owner_id", 
			foreignKey = 
			@ForeignKey(name = "fk_task_owner_id"), 
			nullable = false, insertable = true, updatable = false)
	@ToString.Exclude
	private User owner;	// Tulajdonos
	
	@Nullable
	@ManyToOne
	@JoinColumn(
			name = "period_id", 
			foreignKey = 
			@ForeignKey(name = "fk_task_period_id"), 
			nullable = true, insertable = true, updatable = true)
	@ToString.Exclude
	private Period period;	// Periódus
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "difficulty_id", 
			foreignKey = 
			@ForeignKey(name = "fk_task_difficulty_id"), 
			nullable = false, insertable = true, updatable = true)
	private Difficulty difficulty; // Nehézség
	
    @NotNull
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_task_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Company company;
    
	@ToString.Exclude
	@Nullable
	@OneToMany(
			mappedBy="task",
	        cascade = CascadeType.ALL
	    )
	@Builder.Default 
	private List<TaskXCompanyVirtue> taskXCompanyVirtues = new ArrayList<TaskXCompanyVirtue>();

	@ToString.Exclude
	@Nullable
	@OneToMany(
			mappedBy="task",
	        cascade = CascadeType.ALL
	    )
	@Builder.Default 
	private List<TaskXLeaderVirtue> taskXLeaderVirtues = new ArrayList<TaskXLeaderVirtue>();
	
	@ToString.Exclude
	@Nullable
	@OneToMany(
			mappedBy="task",
	        cascade = CascadeType.ALL
	    )
	@Builder.Default 
	private List<TaskXFactor> taskXFactors = new ArrayList<TaskXFactor>();
	
	
	@ToString.Exclude
	@Nullable
	@OneToMany(
			mappedBy="task",
	        cascade = CascadeType.ALL
	    )
	@Builder.Default 
	private List<Evaluation> evaluations = new ArrayList<Evaluation>();

}
