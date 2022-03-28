package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "difficulty"
        , uniqueConstraints = {
                @UniqueConstraint(name = "uk_difficulty", columnNames = {"company_id", "name"}),
        }
)
@org.hibernate.annotations.Table( appliesTo = "difficulty", comment = "Nehézség" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Difficulty extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = -7016421267264610535L;
		
	@NotNull
	@Column(name = "name", nullable = false, insertable = true, updatable = true, length = 100)
	private String name;
	
	@Nullable
	@Column(name = "description", nullable = true, insertable = true, updatable = true, length = 255)
	private String description;

	@NotNull
	@Digits(integer = 5 /*precision*/, fraction = 2 /*scale*/)
	@Column(name = "multiplier", nullable = false, insertable = true, updatable = true, precision=5, scale=2)
	@ColumnDefault(value = "1")
	@Builder.Default private BigDecimal multiplier = BigDecimal.ONE;
	
	@Column(name = "active", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
	@Builder.Default private Boolean active = true;
	
	@Column(name = "automatic", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "0")
	@Builder.Default private Boolean automatic = false;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_difficulty_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Company company;
	
	
	
}
