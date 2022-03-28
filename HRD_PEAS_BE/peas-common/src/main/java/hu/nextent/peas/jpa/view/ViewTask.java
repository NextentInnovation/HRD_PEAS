package hu.nextent.peas.jpa.view;

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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Immutable;
import org.springframework.lang.Nullable;

import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Difficulty;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.TaskXCompanyVirtue;
import hu.nextent.peas.jpa.entity.TaskXFactor;
import hu.nextent.peas.jpa.entity.TaskXLeaderVirtue;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.jpa.entity.base.JpaEntity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Entity
@Table(name = "view_task")
@Immutable
@NoArgsConstructor(force = true)
@Value
@ToString
@EqualsAndHashCode
public class ViewTask implements JpaEntity<String>, Serializable {

	private static final long serialVersionUID = -717590555504393413L;

	@Id
	@Column(updatable = false, nullable = false)
    private String id;
	
	@Column(updatable = false, nullable = false)
    private Long taskId;

    @Column(name = "language", nullable = true, length = 255)
    private String language;
	
	// Audit
	@Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private OffsetDateTime createdDate;

    @Column(name = "modified_at", columnDefinition = "TIMESTAMP")
    private OffsetDateTime modifiedDate;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;
    
    // Task
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, updatable = true, length = 100)
	private TaskStatusEnum status;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "taskType", nullable = false, updatable = true, length = 100)
	private TaskTypeEnum taskType;
	
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
	@Column(name = "evaluaterCount", nullable = false, insertable = true, updatable = true, precision=5, scale=2)
	private Integer evaluaterCount = 0;
	
	// Kitöltött értékelések száma
	@Column(name = "evaluatedCount", nullable = false, insertable = true, updatable = true, precision=5, scale=2)
	private Integer evaluatedCount = 0;

	// Kitöltöttség százalékban
	@Column(name = "evaluationPercentage", nullable = false, insertable = true, updatable = true, precision=8, scale=5)
	private BigDecimal evaluationPercentage = BigDecimal.ZERO;

	@Column(name = "score", nullable = true, insertable = true, updatable = true, precision=8, scale=5)
	private BigDecimal score;
	
	@ManyToOne
	@JoinColumn(
			name = "owner_id", 
			foreignKey = 
			@ForeignKey(name = "fk_task_owner_id"), 
			nullable = false, insertable = true, updatable = false)
	@ToString.Exclude
	private User owner;	// Tulajdonos
	
	@ManyToOne
	@JoinColumn(
			name = "period_id", 
			foreignKey = 
			@ForeignKey(name = "fk_task_period_id"), 
			nullable = true, insertable = true, updatable = true)
	@ToString.Exclude
	private Period period;	// Periódus
	
	@ManyToOne
	@JoinColumn(
			name = "difficulty_id", 
			foreignKey = 
			@ForeignKey(name = "fk_task_difficulty_id"), 
			nullable = false, insertable = true, updatable = true)
	private Difficulty difficulty; // Nehézség
	
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_task_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Company company;
    
	@OneToMany(
			mappedBy="task",
	        cascade = CascadeType.ALL
	    )
	@ToString.Exclude
	private List<TaskXCompanyVirtue> taskXCompanyVirtues = new ArrayList<TaskXCompanyVirtue>();

	@Nullable
	@OneToMany(
			mappedBy="task",
	        cascade = CascadeType.ALL
	    )
	@ToString.Exclude
	private List<TaskXLeaderVirtue> taskXLeaderVirtues = new ArrayList<TaskXLeaderVirtue>();
	
	@Nullable
	@OneToMany(
			mappedBy="task",
	        cascade = CascadeType.ALL
	    )
	@ToString.Exclude
	private List<TaskXFactor> taskXFactors = new ArrayList<TaskXFactor>();
	
	
	@OneToMany(
			mappedBy="task",
	        cascade = CascadeType.ALL
	    )
	@ToString.Exclude
	private List<Evaluation> evaluations = new ArrayList<Evaluation>();
	
	// Label Base    
    @Column(name = "statusName", nullable = true)
    private String statusName;
    
    @Column(name = "taskTypeName", nullable = true)
    private String taskTypeName;
	
}
