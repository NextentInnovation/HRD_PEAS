package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "factoroption"
        //, uniqueConstraints = {
		//        @UniqueConstraint(name = "uk_factor", columnNames = {"company_id", "code"}),
		//}
)
@org.hibernate.annotations.Table( appliesTo = "factoroption", comment = "Értékelési szempontokra adható válaszok" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class FactorOption extends AuditableBaseEntity implements Serializable {
	
	private static final long serialVersionUID = -1044896370498477654L;
	
	@Column(name = "active", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
    private Boolean active;
	
	@NotNull
	@Column(name = "name", nullable = false, insertable = true, updatable = true, length = 100)
	private String name;
	
	@NotNull
	@Digits(integer = 4 /*precision*/, fraction = 2 /*scale*/)
	@Column(name = "score", nullable = false, insertable = true, updatable = true, precision=4, scale=2)
	@Builder.Default private BigDecimal score = BigDecimal.ZERO;

	@Nullable
	@Column(name = "best", nullable = true, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
    private Boolean best;

	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "factor_id", 
			foreignKey = @ForeignKey(name = "fk_factoroption_factor_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	@ToString.Exclude
	private Factor factor;

}
