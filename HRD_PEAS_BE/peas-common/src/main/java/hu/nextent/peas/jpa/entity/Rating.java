package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
@Table(name = "rating",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_rating", columnNames = {"period_id", "user_id"}),
        }
)
@org.hibernate.annotations.Table( appliesTo = "rating", comment = "Vezetői minősítés" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Rating extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 7338781016741200470L;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, updatable = true, length = 100)
	@Builder.Default private RatingStatusEnum status = RatingStatusEnum.OPEN;

	// Értékelések alapján átlag pontszám
	@Nullable
	@Digits(integer = 8 /*precision*/, fraction = 5 /*scale*/)
	@Column(name = "normalTaskScore", nullable = true, insertable = true, updatable = true, precision=8, scale=5)
	private BigDecimal normalTaskScore;

	// Automatikus taskok alapján számolt pontszám
	@Nullable
	@Digits(integer = 8 /*precision*/, fraction = 5 /*scale*/)
	@Column(name = "automaticTaskScore", nullable = true, insertable = true, updatable = true, precision=8, scale=5)
	private BigDecimal automaticTaskScore;

	// normalTaskScore + automaticTaskScore
	@Nullable
	@Digits(integer = 8 /*precision*/, fraction = 5 /*scale*/)
	@Column(name = "periodScore", nullable = true, insertable = true, updatable = true, precision=8, scale=5)
	private BigDecimal periodScore;
	
	// Szöveges értékelés
	@Nullable
	@Size(max = 1000)
	@Column(name = "textualEvaluation", nullable = true, updatable = true, length = 1000)
    private String textualEvaluation; 
	
	// Besorolás és juttatatási javaslat
	@Nullable
	@Size(max = 1000)
	@Column(name = "gradeRecommendation", nullable = true, updatable = true, length = 1000)
    private String gradeRecommendation; 

	// Eggyüttműködés igen/nem
	@Nullable
	@Column(name = "cooperation", nullable = true, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
	@Builder.Default private Boolean cooperation = true;
	
	@NotNull
	@Column(name = "deadline", nullable = false, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime deadline; // Értékelés határideje időpont

	@Nullable
	@Column(name = "ratingStartDate", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime ratingStartDate; // Értékelés vége

	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "user_id", 
			foreignKey = 
			@ForeignKey(name = "fk_rating_user_id"), 
			nullable = false, insertable = true, updatable = false)
	@ToString.Exclude
	private User user;

	@Nullable
	@ManyToOne
	@JoinColumn(
			name = "leader_id", 
			foreignKey = 
			@ForeignKey(name = "fk_rating_leader_id"), 
			nullable = true, insertable = true, updatable = false)
	@ToString.Exclude
	private User leader;

	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "period_id", 
			foreignKey = 
			@ForeignKey(name = "fk_rating_period_id"), 
			nullable = false, insertable = true, updatable = false)
	@ToString.Exclude
	private Period period;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = 
			@ForeignKey(name = "fk_periodevaluation_company_id"), 
			nullable = false, insertable = true, updatable = false)
	@ToString.Exclude
	private Company company;
	
}
