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
import javax.persistence.UniqueConstraint;
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
@Table(name = "period",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_period", columnNames = {"company_id", "enddate"}),
        }
)
@org.hibernate.annotations.Table( appliesTo = "period", comment = "Periódus" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Period extends AuditableBaseEntity implements Serializable {
	
	private static final long serialVersionUID = -212495819501097005L;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, updatable = true, length = 100)
	@Builder.Default private PeriodStatusEnum status = PeriodStatusEnum.OPEN;

	@NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;
	
	@NotNull
    @Column(name = "startdate", columnDefinition = "TIMESTAMP", nullable = false, insertable = true, updatable = true)
    private OffsetDateTime startDate;
	
	@NotNull
    @Column(name = "enddate", columnDefinition = "TIMESTAMP", nullable = false, insertable = true, updatable = true)
    private OffsetDateTime endDate;
    
	@NotNull
    @Column(name = "ratingEndDate", columnDefinition = "TIMESTAMP", nullable = false, insertable = true, updatable = true)
    private OffsetDateTime ratingEndDate;

    @NotNull
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_period_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Company company;
}
